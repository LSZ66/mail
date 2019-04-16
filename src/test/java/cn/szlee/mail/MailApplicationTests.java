package cn.szlee.mail;

import cn.szlee.mail.entity.Mail;
import cn.szlee.mail.service.MailService;
import cn.szlee.mail.utils.MailUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailApplicationTests {

    @Autowired
    private MailService service;

    @Test
    public void getInbox() throws MessagingException {
        List<Mail> inboxList = service.getInboxList(MailUtil.getInbox("lsz@szlee.cn", "lsz.0929"));
        inboxList.forEach(System.out::println);
    }
}
