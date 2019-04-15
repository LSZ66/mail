package cn.szlee.mail.controller;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

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
