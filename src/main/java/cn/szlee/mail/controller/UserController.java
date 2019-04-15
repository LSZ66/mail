package cn.szlee.mail.controller;

import cn.szlee.mail.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

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
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/login")
    public boolean login(String username, String password, HttpSession session) {
        JavaMailSender mailSender = service.login(username, password);
        if (mailSender != null) {
            session.setAttribute("userSender", mailSender);
            return true;
        } else {
            return false;
        }
    }

    @PostMapping("/register")
    public boolean register(String username, String password) {
        return service.register(username, password);
    }
}
