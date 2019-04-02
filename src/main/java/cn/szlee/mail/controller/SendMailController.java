package cn.szlee.mail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
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
    private JavaMailSender mailSender;

    @GetMapping
    public void sendSimpleMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("lcm@lcm.com");
        message.setTo("lisz929@163.com");
        message.setSubject("主题：简单邮件");
        message.setText("测试邮件内容");
        mailSender.send(message);
    }
}
