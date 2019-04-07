package cn.szlee.mail.controller;

import cn.szlee.mail.utils.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b><code>SendMailController</code></b>
 * <p/>
 * 发送邮件的控制器
 * <p/>
 * <b>Creation Time:</b> 2019/4/2 16:24.
 *
 * @author 李尚哲
 * @since mail 1.0
 */
@RestController
@RequestMapping("/sendmail")
public class SendMailController {

    @Autowired
    private MailSender sender;

    @PostMapping
    public void sendSimpleMail(String to, String subject, String text) {
        sender.send(to, subject, text);
    }
}
