package cn.szlee.mail.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component("myMailSender")
public class MailSender {

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    public void send(String to, String subject, String text) {
        //(String)session.getAttribute("username")
        String username = "hyb@szlee.cn";
        //(String) session.getAttribute("password")
        String password = "hyb.2616";

//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("黄悦彬");
//        message.setFrom(javaMailSender.getUsername());
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//        javaMailSender.send(message);
    }
}
