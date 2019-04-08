package cn.szlee.mail.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component("myMailSender")
public class MailSender {

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    public void send(String to, String subject, String text) {
        //(String)session.getAttribute("username")
        String username = "lsz@szlee.cn";
        //(String) session.getAttribute("password")
        String password = "lsz.0929";
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("admin@szlee.cn");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
}
