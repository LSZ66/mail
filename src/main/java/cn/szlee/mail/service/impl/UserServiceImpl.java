package cn.szlee.mail.service.impl;

import cn.szlee.mail.config.Constant;
import cn.szlee.mail.entity.User;
import cn.szlee.mail.repository.UserRepository;
import cn.szlee.mail.service.UserService;
import com.sun.mail.imap.IMAPStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Folder;
import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private UserRepository repo;

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
    public Map<String, Integer> getMessageCount(IMAPStore store) {
        Map<String, Integer> map = new HashMap<>(4);
        try {
            Folder folder = openFolder(store, Constant.INBOX);
            map.put("unread", folder.getNewMessageCount());
            map.put("inbox", folder.getMessageCount());
            folder.close();
            folder = openFolder(store, Constant.OUTBOX);
            map.put("outbox", folder.getMessageCount());
            folder.close();
            folder = openFolder(store, Constant.DRAFT_BOX);
            map.put("draft", folder.getMessageCount());
            folder.close();
        } catch (MessagingException e) {
            e.printStackTrace();
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
}