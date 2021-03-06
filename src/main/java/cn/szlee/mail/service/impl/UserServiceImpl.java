package cn.szlee.mail.service.impl;

import cn.szlee.mail.config.Constant;
import cn.szlee.mail.entity.User;
import cn.szlee.mail.repository.UserRepository;
import cn.szlee.mail.service.UserService;
import cn.szlee.mail.utils.CalenderUtil;
import cn.szlee.mail.utils.MailUtil;
import com.sun.mail.imap.IMAPStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SentDateTerm;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * <b><code>UserServiceImpl</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019-04-14 19:28.
 *
 * @author 李尚哲
 * @since mail 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository repo;

    @Autowired
    private TaskExecutor taskExecutor;

    @Override
    public User queryForLogin(String username, String password) {
        return repo.findByEmailAndPassword(username + Constant.MAIL_SUFFIX, password);
    }

    @Override
    public boolean register(String username, String password) {
        if (getUser(username) == null) {
            String email = username + Constant.MAIL_SUFFIX;
            User user = new User();
            user.setEmail(email);
            String encrypt = new String(repo.getEncryptPassword(password));
            user.setPassword(encrypt);
            repo.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User getUser(String username) {
        return repo.findByEmail(username + Constant.MAIL_SUFFIX);
    }

    @Override
    public User getUser(int id) {
        return repo.getOne(id);
    }

    private Folder openFolder(IMAPStore store, String box) throws MessagingException {
        Folder folder = store.getFolder(box);
        if (!folder.exists()) {
            folder.create(Folder.HOLDS_MESSAGES);
        }
        folder.open(Folder.READ_WRITE);
        return folder;
    }

    @Override
    public Map<String, Integer> getMessageCount(Map<String, Folder> folderMap) {
        Map<String, Integer> map = new HashMap<>(3);
        try {
            map.put("unread", folderMap.get("inbox").getUnreadMessageCount());
            map.put("inbox", folderMap.get("inbox").getMessageCount());
            map.put("outbox", folderMap.get("outbox").getMessageCount());
        } catch (MessagingException e) {
            LOGGER.error("打开文件夹失败", e);
        }
        return map;
    }

    @Override
    public Map<String, Folder> getUserFolder(IMAPStore store) {
        Map<String, Folder> map = new ConcurrentHashMap<>(4);
        final CountDownLatch latch = new CountDownLatch(4);
        taskExecutor.execute(()->{
            LOGGER.info("open inbox");
            Folder folder = null;
            try {
                folder = openFolder(store, Constant.INBOX);
            } catch (MessagingException e) {
                LOGGER.error("打开收件箱失败", e);
            }
            map.put("inbox", folder);
            latch.countDown();
            LOGGER.info("open inbox success");
        });
        taskExecutor.execute(()->{
            LOGGER.info("open outbox");
            Folder folder = null;
            try {
                folder = openFolder(store, Constant.OUTBOX);
            } catch (MessagingException e) {
                LOGGER.error("打开发件箱失败", e);
            }
            map.put("outbox", folder);
            latch.countDown();
            LOGGER.info("open outbox success");
        });
        taskExecutor.execute(()->{
            LOGGER.info("open spam");
            Folder folder = null;
            try {
                folder = openFolder(store, Constant.SPAM_BOX);
            } catch (MessagingException e) {
                LOGGER.error("打开垃圾箱失败", e);
            }
            map.put("spam", folder);
            latch.countDown();
            LOGGER.info("open spam success");
        });
        taskExecutor.execute(()->{
            LOGGER.info("open recycle");
            Folder folder = null;
            try {
                folder = openFolder(store, Constant.RECYCLE);
            } catch (MessagingException e) {
                LOGGER.error("打开回收站失败", e);
            }
            map.put("recycle", folder);
            latch.countDown();
            LOGGER.info("open recycle success");
        });
        try {
            latch.await();
            LOGGER.info("await");
        } catch (InterruptedException e) {
            LOGGER.error("多线程执行错误", e);
        }
        return map;
    }

    @Override
    public void updateName(int id, String name) {
        User user = repo.getOne(id);
        user.setName(name);
        repo.save(user);
    }

    @Override
    public void updateInfo(User newInfo) {
        String encrypt = new String(repo.getEncryptPassword(newInfo.getPassword()));
        User user = repo.getOne(newInfo.getId());
        user.setName(newInfo.getName());
        user.setPassword(encrypt);
        repo.save(user);
    }

    @Override
    public Map<String, Object> getSenderAndReceiver(String email, String password) {
        Map<String, Object> map = new HashMap<>(2);
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(Constant.HOST);
        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.auth", true);
        mailProps.put("mail.smtp.starttls.enable", true);
        sender.setJavaMailProperties(mailProps);
        sender.setUsername(email);
        sender.setPassword(password);
        IMAPStore store = null;
        try {
            store = MailUtil.getStore(email, password);
        } catch (MessagingException e) {
            LOGGER.error("登陆IMAP服务器失败", e);
        }
        map.put("sender", sender);
        map.put("store", store);
        return map;
    }

    @Override
    public Map<String, Integer> getDateCount(IMAPStore store) {
        SearchTerm ge = new SentDateTerm(ComparisonTerm.GE, CalenderUtil.twoWeeksBefore());
        SearchTerm le = new SentDateTerm(ComparisonTerm.LE, CalenderUtil.now());
        SearchTerm term = new AndTerm(ge, le);
        Folder folder;
        Map<String, Integer> map = new HashMap<>();
        try {
            folder = openFolder(store, Constant.INBOX);
            Message[] messages = folder.search(term);
            for (Message message : messages) {
                String date = MailUtil.getDate(message);
                map.merge(date, 1, Integer::sum);
            }
            folder.close();
            folder = openFolder(store, Constant.OUTBOX);
            messages = folder.search(term);
            for (Message message : messages) {
                String date = MailUtil.getDate(message);
                map.merge(date, 1, Integer::sum);
            }
        } catch (MessagingException e) {
            LOGGER.error("打开文件夹失败", e);
        }
        return map;
    }
}