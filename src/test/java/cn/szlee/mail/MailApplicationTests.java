package cn.szlee.mail;

import cn.szlee.mail.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailApplicationTests {

    @Autowired
    private UserRepository repository;

    @Test
    public void login() {
        System.out.println(repository.findByEmailAndPassword("lsz@szlee.cn", "lsz.092"));
    }
}
