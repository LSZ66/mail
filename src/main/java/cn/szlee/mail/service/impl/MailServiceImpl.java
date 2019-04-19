package cn.szlee.mail.service.impl;

import cn.szlee.mail.config.Constant;
import cn.szlee.mail.entity.Mail;
import cn.szlee.mail.service.MailService;
import cn.szlee.mail.utils.MailUtil;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import org.springframework.stereotype.Service;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    private IMAPFolder getFolder(String box, IMAPStore store) throws MessagingException {
        switch (box) {
            case "inbox":
                box = Constant.INBOX;
                break;
            case "outbox":
                box = Constant.OUTBOX;
                break;
            case "draft":
                box = Constant.DRAFT_BOX;
                break;
            case "spam":
                box = Constant.SPAM_BOX;
                break;
            case "recycle":
                box = Constant.RECYCLE;
                break;
            default:
                throw new MessagingException("没有此文件夹");
        }
        IMAPFolder folder = (IMAPFolder) store.getFolder(box);
        if (!folder.exists()) {
            folder.create(Folder.HOLDS_MESSAGES);
        }
        folder.open(Folder.READ_WRITE);
        return folder;
    }

    /**
     * 封装接收类邮件（收件/垃圾邮件）
     *
     * @param message 邮件体
     * @return 邮件实体类
     */
    private Mail setReceiveMail(Message message) throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = (MimeMessage) message;
        Mail mail = new Mail();
        mail.setId(msg.getMessageNumber());
        mail.setFrom(MailUtil.getFrom(msg));
        mail.setSubject(MailUtil.getSubject(msg));
        mail.setReceiveTime(MailUtil.getSentDate(msg));
        int state = 0;
        if (MailUtil.isSeen(msg)) {
            if (MailUtil.isAnswered(msg)) {
                state = 2;
            } else {
                state = 1;
            }
        }
        mail.setState(state);
        return mail;
    }

    /**
     * 封装发送的邮件
     *
     * @param message 邮件体
     * @return 邮件实体类
     */
    private Mail setSendMail(Message message) throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = (MimeMessage) message;
        Mail mail = new Mail();
        mail.setId(msg.getMessageNumber());
        mail.setTo(MailUtil.getReceiveAddress(msg));
        mail.setSubject(MailUtil.getSubject(msg));
        mail.setSendTime(MailUtil.getSentDate(msg));
        return mail;
    }

    /**
     * 封装草稿邮件
     *
     * @param message 邮件体
     * @return 邮件实体类
     */
    private Mail setDraftMail(Message message) throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = (MimeMessage) message;
        Mail mail = new Mail();
        mail.setId(msg.getMessageNumber());
        mail.setFrom(MailUtil.getFrom(msg));
        mail.setSubject(MailUtil.getSubject(msg));
        mail.setLastModifyTime(MailUtil.getSentDate(msg));
        return mail;
    }

    /**
     * 封装回收站邮件
     *
     * @param message 邮件体
     * @return 邮件实体类
     */
    private Mail setRecycleMail(Message message) throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = (MimeMessage) message;
        Mail mail = new Mail();
        mail.setId(msg.getMessageNumber());
        mail.setFrom(MailUtil.getFrom(msg));
        mail.setSubject(MailUtil.getSubject(msg));
        mail.setDeleteTime(MailUtil.getSentDate(msg));
        return mail;
    }

    @Override
    public List<Mail> getListByBox(String box, IMAPStore store) {
        List<Mail> list = new LinkedList<>();
        IMAPFolder folder;
        Message[] messages;
        try {
            folder = getFolder(box, store);
            messages = folder.getMessages();
            for (Message message : messages) {
                Mail mail;
                switch (box) {
                    case "inbox":
                    case "spam":
                        mail = setReceiveMail(message);
                        break;
                    case "outbox":
                        mail = setSendMail(message);
                        break;
                    case "draft":
                        mail = setDraftMail(message);
                        break;
                    case "recycle":
                        mail = setRecycleMail(message);
                        break;
                    default:
                        mail = null;
                }
                list.add(0, mail);
            }
            folder.close();
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Mail getMessageById(String box, int id, IMAPStore store) {
        Mail mail = new Mail();
        try {
            IMAPFolder folder = getFolder(box, store);
            if (!folder.isOpen()) {
                folder.open(Folder.READ_ONLY);
            }
            MimeMessage message = (MimeMessage) folder.getMessage(id);
            message.setFlags(new Flags(Flags.Flag.SEEN), true);
            mail.setId(id);
            mail.setSubject(MailUtil.getSubject(message));
            mail.setFrom(MailUtil.getFullFrom(message));
            mail.setTo(MailUtil.getReceiveAddress(message));
            mail.setReceiveTime(MailUtil.getSentDate(message));
            mail.setText(MailUtil.getMailTextContent(message));
            folder.close();
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        return mail;
    }

    @Override
    public void saveToOutbox(MimeMessage message, IMAPStore store) {
        try {
            IMAPFolder folder = getFolder("outbox", store);
            if (!folder.exists()) {
                folder.create(Folder.HOLDS_MESSAGES);
            }
            folder.appendMessages(new Message[]{message});
            folder.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
