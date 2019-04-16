package cn.szlee.mail.service.impl;

import cn.szlee.mail.entity.Mail;
import cn.szlee.mail.service.MailService;
import cn.szlee.mail.utils.MailUtil;
import com.sun.mail.imap.IMAPFolder;
import org.springframework.stereotype.Service;

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
    public List<Mail> getInboxList(IMAPFolder folder) {
        List<Mail> inbox = new LinkedList<>();
        Message[] messages;
        try {
            messages = folder.getMessages();
            for (Message message : messages) {
                MimeMessage msg = (MimeMessage) message;
                if (MailUtil.isDeleted(msg)) {
                    //邮件已被删除
                    continue;
                }
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
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return inbox;
    }

    @Override
    public Mail getMessageById(IMAPFolder folder, int id) {
        Mail mail = new Mail();
        MimeMessage message;
        try {
            message = (MimeMessage) folder.getMessage(id);
            mail.setId(id);
            mail.setFrom(MailUtil.getFullFrom(message));
            mail.setTo(MailUtil.getReceiveAddress(message));
            mail.setReceiveTime(MailUtil.getSentDate(message));
            mail.setText(MailUtil.getMailTextContent(message));
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        return mail;
    }
}
