package cn.szlee.mail.controller;

import cn.szlee.mail.config.Constant;
import cn.szlee.mail.entity.User;
import cn.szlee.mail.service.UserService;
import cn.szlee.mail.utils.MailUtil;
import com.sun.mail.imap.IMAPStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <b><code>UserController</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019-04-14 19:51.
 *
 * @author 李尚哲
 * @since mail 1.0
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/login")
    public boolean login(String username, String password, HttpSession session) throws MessagingException {
        User user = service.queryForLogin(username, password);
        if (user != null) {
            session.setAttribute("userName", user.getName());
            session.setAttribute("userEmail", user.getEmail());
            getOverview(username, password, session);
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("/overview")
    public Map<String, Integer> getOverview(String username, String password, HttpSession session) throws MessagingException {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(Constant.DOMAIN);
        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.auth", true);
        mailProps.put("mail.smtp.starttls.enable", true);
        sender.setJavaMailProperties(mailProps);
        sender.setUsername(username + Constant.MAIL_SUFFIX);
        sender.setPassword(password);
        session.setAttribute("userSender", sender);
        IMAPStore store;
        try {
            store = MailUtil.getStore(username, password);
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }
        session.setAttribute("userStore", store);
        Map<String, Integer> map = new HashMap<>(4);
        map.put("unread", MailUtil.getNewMessageCount(store));
        map.put("inbox", MailUtil.getInboxMessageCount(store));
        map.put("outbox", MailUtil.getOutboxMessageCount(store));
        map.put("draft", 2);
        return map;
    }

    @PostMapping("/register")
    public boolean register(String username, String password) {
        return service.register(username, password);
    }

    @GetMapping("/logout")
    public void logout(HttpSession session) {
        session.removeAttribute("userName");
        session.removeAttribute("userEmail");
        session.removeAttribute("userSender");
        session.removeAttribute("userStore");
    }

    @GetMapping("/getName")
    public String getName(HttpSession session) {
        Object name = session.getAttribute("userName");
        return name != null ?
                (String) name :
                (String) session.getAttribute("userEmail");
    }
}
