package cn.szlee.mail;

import cn.szlee.mail.repository.AliasRepository;
import cn.szlee.mail.repository.DomainRepository;
import cn.szlee.mail.repository.UserRepository;
import cn.szlee.mail.service.UserService;
import cn.szlee.mail.utils.MailSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private AliasRepository aliasRepository;

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

    @Autowired
    private MailSender mailSender;

    @Test
    public void sendSimpleMail() {
        mailSender.send("lsz@szlee.cn", "你好啊", "抽取方法");
    }

    @Autowired
    private UserService userService;

    @Test
    public void loginTest() {
        System.out.println(userService.login("fps", "fps.2108"));
    }

    @Test
    public void registerTest() {
        System.out.println(userService.add("fps", "fps.2108"));
    }
}
