package cn.szlee.mail.service;

import cn.szlee.mail.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

@Service
public class UserService {

    /**
     * 邮箱域名
     */
    private final String domain = "@szlee.cn";

    /**
     * Spring Mail发送器，用于检测是否能登录
     */
    @Autowired
    private JavaMailSenderImpl javaMailSender;

    /**
     * 用户持久层类
     */
    @Autowired
    private UserRepository repository;

    /**
     * 添加/注册一个用户
     * @param username  用户名
     * @param password  用户密码
     * @return  如果数据库中用户名不存在，则注册成功返回true；否则返回false。
     */
    @Transactional
    public boolean add(String username, String password) {
        String email = username + domain;
        if (repository.findByEmail(email) != null) {
            //已存在该用户，不允许注册
            return false;
        }
        repository.insert(email, password);
        return true;
    }

    /**
     * 用户登录
     * @param username  用户名
     * @param password  用户密码
     * @return  如果用户名和密码匹配，则登陆成功返回true；否则返回false。
     */
    public boolean login(String username, String password) {
        try {
            javaMailSender.setUsername(username + domain);
            javaMailSender.setPassword(password);
            javaMailSender.testConnection();
        } catch (MessagingException e) {
            return false;
        }
        return true;
    }
}
