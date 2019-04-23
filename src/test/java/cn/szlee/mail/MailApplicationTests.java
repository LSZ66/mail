package cn.szlee.mail;

import cn.szlee.mail.service.UserService;
import com.sun.mail.imap.IMAPStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailApplicationTests {

    @Autowired
    private UserService service;
    @Test
    public void hello() {
        Map<String, Object> map = service.getSenderAndReceiver("lsz@szlee.cn", "lsz.0929");
        IMAPStore store = (IMAPStore) map.get("store");
        Map<String, Integer> dateCount = service.getDateCount(store);
        System.out.println(dateCount);
    }
}
