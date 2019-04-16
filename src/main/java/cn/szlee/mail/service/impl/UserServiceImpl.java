package cn.szlee.mail.service.impl;

import cn.szlee.mail.entity.User;
import cn.szlee.mail.repository.UserRepository;
import cn.szlee.mail.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Properties;

/**
 * <b><code>UserServiceImpl</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019-04-14 19:28.
 *
 * @author 李尚哲
 * @since mail 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    /**
     * 查询是否存在此用户
     * @param username  要查询的用户名
     * @return  如果用户名已存在，则返回true，否则返回false
     */
    private boolean checkUsername(String username) {
        return getUser(username) != null;
    }


    @Override
    public JavaMailSender login(String username, String password) {
        JavaMailSenderImpl sender = null;
        if (checkUsername(username)) {
            sender = new JavaMailSenderImpl();
            sender.setHost(DOMAIN.substring(1));
            Properties javaMailProperties = new Properties();
            javaMailProperties.put("mail.smtp.auth", true);
            javaMailProperties.put("mail.smtp.starttls.enable", true);
            javaMailProperties.put("mail.smtp.timeout", 5000);
            sender.setJavaMailProperties(javaMailProperties);
            sender.setUsername(username + DOMAIN);
            sender.setPassword(password);
            try {
                sender.testConnection();
            } catch (MessagingException e) {
                return null;
            }
        }
        return sender;
    }

    @Override
    public boolean register(String username, String password) {
        if (!checkUsername(username)) {
            repository.insert(username, password);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User getUser(String username) {
        return repository.findByEmail(username + DOMAIN);
    }
}