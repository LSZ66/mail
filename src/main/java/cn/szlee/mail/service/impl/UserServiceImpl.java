package cn.szlee.mail.service.impl;

import cn.szlee.mail.config.Constant;
import cn.szlee.mail.entity.User;
import cn.szlee.mail.repository.UserRepository;
import cn.szlee.mail.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public User queryForLogin(String username, String password) {
        return repository.findByEmailAndPassword(username + Constant.MAIL_SUFFIX, password);
    }

    @Override
    public boolean register(String username, String password) {
        if (getUser(username) == null) {
            repository.insert(username, password);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User getUser(String username) {
        return repository.findByEmail(username + Constant.MAIL_SUFFIX);
    }
}