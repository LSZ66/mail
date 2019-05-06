package cn.szlee.mail.service.impl;

import cn.szlee.mail.algorithm.BayesClassifier;
import cn.szlee.mail.config.Constant;
import cn.szlee.mail.entity.Mail;
import cn.szlee.mail.service.MailService;
import cn.szlee.mail.utils.BayesUtil;
import cn.szlee.mail.utils.MailUtil;
import cn.szlee.mail.utils.WordsUtil;
import com.sun.mail.imap.IMAPFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.search.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * <b><code>MailServiceImpl</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019-04-16 15:25.
 *
 * @author 李尚哲
 * @since mail 1.0
 */
@Service
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    /**
     * 封装接收类邮件
     *
     * @param message 邮件体
     * @return 邮件实体类
     */
    private Mail setMail(Message message) throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = (MimeMessage) message;
        Mail mail = new Mail();
        mail.setId(msg.getMessageNumber());
        mail.setSubject(MailUtil.getSubject(msg));
        return mail;
    }

    @Override
    public List<Mail> getListByBox(IMAPFolder folder, int pageNo) {
        int pageSize = 10;
        List<Mail> list = new ArrayList<>();
        Message[] messages;
        try {
            int total = folder.getMessageCount();
            if ((total - pageNo * pageSize) > 0) {
                messages = folder.getMessages(total - pageNo * pageSize + 1, total - (pageNo - 1) * pageSize);
            } else {
                messages = folder.getMessages(1, total - (pageNo - 1) * pageSize);
            }
            //对邮件进行时间排序
            Arrays.sort(messages, (o1, o2) -> {
                try {
                    return o2.getSentDate().compareTo(o1.getSentDate());
                } catch (MessagingException e) {
                    LOGGER.error("获取邮件发送时间失败", e);
                    return 0;
                }
            });
            for (Message message : messages) {
                Mail mail;
                switch (folder.getName()) {
                    case Constant.INBOX:
                        mail = setMail(message);
                        mail.setFrom(MailUtil.getFrom(message));
                        mail.setReceiveTime(MailUtil.getDateTime(message));
                        int state = 0;
                        if (MailUtil.isSeen(message)) {
                            if (MailUtil.isAnswered(message)) {
                                state = 2;
                            } else {
                                state = 1;
                            }
                        }
                        mail.setState(state);
                        break;
                    case Constant.OUTBOX:
                        mail = setMail(message);
                        mail.setTo(MailUtil.getReceiveAddress(message));
                        mail.setSendTime(MailUtil.getDateTime(message));
                        break;
                    case Constant.RECYCLE:
                    case Constant.SPAM_BOX:
                        mail = setMail(message);
                        mail.setFrom(MailUtil.getFrom(message));
                        mail.setReceiveTime(MailUtil.getDateTime(message));
                        break;
                    default:
                        mail = null;
                }
                list.add(mail);
            }
        } catch (MessagingException | IOException e) {
            LOGGER.error("获取邮件列表出错", e);
        }
        return list;
    }

    @Override
    public Mail getMessageById(IMAPFolder folder, int id) {
        Mail mail = new Mail();
        try {
            MimeMessage message = (MimeMessage) folder.getMessage(id);
            mail.setId(id);
            mail.setSubject(MailUtil.getSubject(message));
            mail.setFrom(MailUtil.getFullFrom(message));
            mail.setTo(MailUtil.getReceiveAddress(message));
            mail.setReceiveTime(MailUtil.getDateTime(message));
            boolean check = true;
            if (MailUtil.isSeen(message)) {
                check = false;
            }
            mail.setText(MailUtil.getHtmlContent(message));
            if (check) {
                //检查是否为垃圾邮件
                List<String> separate = WordsUtil.separate(mail.getText());
                System.out.println(separate);
                var bayes = BayesUtil.getBayes();
                String category = bayes.classify(separate).getCategory();
                if (Constant.SPAM.equals(category)) {
                    mail.setState(1);
                }
            }
        } catch (MessagingException | IOException e) {
            LOGGER.error("获取/解析邮件出错", e);
        }
        return mail;
    }

    @Override
    public void saveToBox(IMAPFolder folder, MimeMessage... message) {
        try {
            folder.appendMessages(message);
        } catch (MessagingException e) {
            LOGGER.error("打开文件夹出错", e);
        }
    }

    @Override
    public void moveToBox(IMAPFolder srcFolder, IMAPFolder destFolder, int... msgIds) {
        try {
            Message[] messages = srcFolder.getMessages(msgIds);
            srcFolder.copyMessages(messages, destFolder);
            srcFolder.setFlags(msgIds, new Flags(Flags.Flag.DELETED), true);
            srcFolder.close(true);
            srcFolder.open(Folder.READ_WRITE);
        } catch (MessagingException e) {
            LOGGER.error("操作文件夹出错", e);
        }
    }

    @Override
    public void delete(IMAPFolder folder, int... msgIds) {
        try {
            folder.setFlags(msgIds, new Flags(Flags.Flag.DELETED), true);
            folder.close(true);
            folder.open(Folder.READ_WRITE);
        } catch (MessagingException e) {
            LOGGER.error("操作文件夹出错", e);
        }
    }

    @Override
    public void setSeen(IMAPFolder folder, int... msgIds) {
        try {
            folder.setFlags(msgIds, new Flags(Flags.Flag.SEEN), true);
        } catch (MessagingException e) {
            LOGGER.error("操作文件夹出错", e);
        }
    }

    @Override
    public List<Mail> search(IMAPFolder folder, String pattern) {
        List<Mail> list = new LinkedList<>();
        try {
            SearchTerm term = new OrTerm(new SearchTerm[]{
                    new FromStringTerm(pattern),
                    new SubjectTerm(pattern),
                    new BodyTerm(pattern)});
            Message[] result = folder.search(term);
            for (Message message : result) {
                Mail mail = setMail(message);
                mail.setFrom(MailUtil.getFrom(message));
                mail.setReceiveTime(MailUtil.getDateTime(message));
                int state = 0;
                if (MailUtil.isSeen(message)) {
                    if (MailUtil.isAnswered(message)) {
                        state = 2;
                    } else {
                        state = 1;
                    }
                }
                mail.setState(state);
                list.add(0, mail);
            }
        } catch (MessagingException | UnsupportedEncodingException e) {
            LOGGER.error("解析邮件出错", e);
        }
        return list;
    }

    @Override
    public void markAs(IMAPFolder folder, String type, int... msgIds) {
        try {
            Message[] messages = folder.getMessages(msgIds);
            for (Message message : messages) {
                String content = MailUtil.getHtmlContent(message);
                List<String> separate = WordsUtil.separate(content);
                BayesUtil.getBayes().learn(type, separate);
            }
        } catch (MessagingException | IOException e) {
            LOGGER.error("读取/解析邮件出错", e);
        }
    }

    @Override
    public int getTotalCount(IMAPFolder folder) {
        try {
            return folder.getMessageCount();
        } catch (MessagingException e) {
            LOGGER.error("获取邮件总数出错", e);
            return 0;
        }
    }
}
