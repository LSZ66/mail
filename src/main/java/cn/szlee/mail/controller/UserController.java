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

    @PostMapping("/register")
    public boolean register(@RequestBody Map<String, String> user) {
        return service.register(user.get("username"), user.get("password"));
    }

    @PostMapping("/login")
    public boolean login(@RequestBody User req, HttpSession session) {
        User user = service.queryForLogin(req.getEmail(), req.getPassword());
        if (user != null) {
            session.setAttribute("userName", user.getName());
            session.setAttribute("password", req.getPassword());
            session.setAttribute("userEmail", user.getEmail());
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("/getName")
    public String getName(HttpSession session) {
        Object name = session.getAttribute("userName");
        return name != null ?
                (String) name :
                (String) session.getAttribute("userEmail");
    }

    @GetMapping("/overview")
    public Map<String, Integer> getOverview(HttpSession session) throws MessagingException {
        String userEmail = (String) session.getAttribute("userEmail");
        String password = (String) session.getAttribute("password");
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(Constant.DOMAIN);
        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.auth", true);
        mailProps.put("mail.smtp.starttls.enable", true);
        sender.setJavaMailProperties(mailProps);
        sender.setUsername(userEmail);
        sender.setPassword(password);
        session.setAttribute("userSender", sender);
        IMAPStore store;
        try {
            store = MailUtil.getStore(userEmail, password);
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }
        session.setAttribute("userStore", store);
        return service.getMessageCount(store);
    }

    @GetMapping("/logout")
    public void logout(HttpSession session) {
        session.removeAttribute("userName");
        session.removeAttribute("userEmail");
        session.removeAttribute("userSender");
        session.removeAttribute("userStore");
    }
}
