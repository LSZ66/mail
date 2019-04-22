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
            case "spam":
                box = Constant.SPAM_BOX;
                break;
            case "recycle":
                box = Constant.RECYCLE;
                break;
            default:
                break;
        }
        IMAPFolder folder = (IMAPFolder) store.getFolder(box);
        if (!folder.exists()) {
            folder.create(Folder.HOLDS_MESSAGES);
        }
        folder.open(Folder.READ_WRITE);
        return folder;
    }

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
                        mail = setMail(message);
                        mail.setFrom(MailUtil.getFrom(message));
                        mail.setReceiveTime(MailUtil.getSentDate(message));
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
                    case "outbox":
                        mail = setMail(message);
                        mail.setTo(MailUtil.getReceiveAddress(message));
                        mail.setSendTime(MailUtil.getSentDate(message));
                        break;
                    case "recycle":
                    case "spam":
                        mail = setMail(message);
                        mail.setFrom(MailUtil.getFrom(message));
                        mail.setReceiveTime(MailUtil.getSentDate(message));
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
            MimeMessage message = (MimeMessage) folder.getMessage(id);
            message.setFlags(new Flags(Flags.Flag.SEEN), true);
            mail.setId(id);
            mail.setSubject(MailUtil.getSubject(message));
            mail.setFrom(MailUtil.getFullFrom(message));
            mail.setTo(MailUtil.getReceiveAddress(message));
            mail.setReceiveTime(MailUtil.getSentDate(message));
            mail.setText(MailUtil.getHtmlContent(message));
            folder.close();
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        return mail;
    }

    @Override
    public void saveToBox(IMAPStore store, String box, MimeMessage... message) {
        try {
            IMAPFolder folder = getFolder(box, store);
            folder.appendMessages(message);
            folder.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void moveToBox(IMAPStore store, String srcBox, String destBox, int... msgIds) {
        try {
            IMAPFolder srcFolder = getFolder(srcBox, store);
            IMAPFolder destFolder = getFolder(destBox, store);
            Message[] messages = srcFolder.getMessages();
            srcFolder.copyMessages(messages, destFolder);
            srcFolder.setFlags(msgIds, new Flags(Flags.Flag.DELETED), true);
            srcFolder.close();
            destFolder.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(IMAPStore store, String box, int... msgIds) {
        try {
            IMAPFolder folder = getFolder(box, store);
            folder.setFlags(msgIds, new Flags(Flags.Flag.DELETED), true);
            folder.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSeen(IMAPStore store, int... msgIds) {
        try {
            IMAPFolder folder = getFolder(Constant.INBOX, store);
            folder.setFlags(msgIds, new Flags(Flags.Flag.SEEN), true);
            folder.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
