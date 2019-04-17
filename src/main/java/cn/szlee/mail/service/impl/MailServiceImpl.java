package cn.szlee.mail.service.impl;

import cn.szlee.mail.entity.Mail;
import cn.szlee.mail.service.MailService;
import cn.szlee.mail.utils.MailUtil;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import org.springframework.stereotype.Service;

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

    @Override
    public List<Mail> getInboxList(IMAPStore store) {
        List<Mail> inbox = new LinkedList<>();
        IMAPFolder folder;
        Message[] messages;
        try {
            folder = MailUtil.getInbox(store);
            messages = folder.getMessages();
            for (Message message : messages) {
                if (MailUtil.isDeleted((MimeMessage) message)) {
                    //邮件已被删除
                    continue;
                }
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
                inbox.add(0, mail);
            }
            folder.close();
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return inbox;
    }

    @Override
    public List<Mail> getOutboxList(IMAPStore store) {
        List<Mail> outbox = new LinkedList<>();
        IMAPFolder folder;
        Message[] messages;
        try {
            folder = MailUtil.getOutbox(store);
            messages = folder.getMessages();
            for (Message message : messages) {
                MimeMessage msg = (MimeMessage) message;
                Mail mail = new Mail();
                mail.setId(msg.getMessageNumber());
                mail.setTo(MailUtil.getReceiveAddress(msg));
                mail.setSubject(MailUtil.getSubject(msg));
                mail.setSendTime(MailUtil.getSentDate(msg));
                outbox.add(0, mail);
            }
            folder.close();
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return outbox;
    }

    @Override
    public Mail getMessageById(IMAPFolder folder, int id) {
        Mail mail = new Mail();
        MimeMessage message;
        try {
            if (!folder.isOpen()) {
                folder.open(Folder.READ_ONLY);
            }
            message = (MimeMessage) folder.getMessage(id);
            mail.setId(id);
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
            IMAPFolder folder = MailUtil.getOutbox(store);
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
