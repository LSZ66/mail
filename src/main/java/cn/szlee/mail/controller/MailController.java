package cn.szlee.mail.controller;

import cn.szlee.mail.entity.Mail;
import cn.szlee.mail.service.MailService;
import com.sun.mail.imap.IMAPFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <b><code>MailController</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019-04-14 20:01.
 *
 * @author 李尚哲
 * @since mail 1.0
 */
@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService service;

    @GetMapping("/getInbox")
    public List<Mail> getInboxList(HttpSession session) {
        IMAPFolder userInbox = (IMAPFolder) session.getAttribute("userInbox");
        return service.getInboxList(userInbox);
    }

    @GetMapping("/getMsg")
    public Mail getMessageById(Integer id, HttpSession session) {
        if (id == null) {
            return null;
        }
        IMAPFolder userInbox = (IMAPFolder) session.getAttribute("userInbox");
        return service.getMessageById(userInbox, id);
    }

    @PostMapping
    public void send(String to, String subject, String text, HttpSession session) {
        JavaMailSender sender = (JavaMailSender)session.getAttribute("userSender");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(((JavaMailSenderImpl) sender).getUsername());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        sender.send(message);
    }
}
