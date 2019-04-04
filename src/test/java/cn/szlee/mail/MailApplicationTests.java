package cn.szlee.mail;

import cn.szlee.mail.repository.AliasRepository;
import cn.szlee.mail.repository.DomainRepository;
import cn.szlee.mail.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailApplicationTests {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private AliasRepository aliasRepository;

    @Test
    public void sendSimpleMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        mailSender.setUsername("lsz@szlee.cn");
        mailSender.setPassword("lsz.0929");
        message.setFrom("lsz@szlee.cn");
        message.setTo("lisz929@163.com");
        message.setSubject("主题：简单邮件");
        message.setText("测试邮件内容");
        mailSender.send(message);
    }

    @Test
    public void findAll() {
        System.out.println(userRepository.findAll());
        System.out.println(aliasRepository.findAll());
        System.out.println(domainRepository.findAll());
    }

    @Test
    public void showAllUser() {
        ArrayList<String> list = new ArrayList<>();
        userRepository.findAll().forEach((item) -> list.add(item.getEmail()));
        System.out.println(list);
    }
}
