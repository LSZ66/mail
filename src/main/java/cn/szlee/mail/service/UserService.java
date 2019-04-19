package cn.szlee.mail.service;

import cn.szlee.mail.entity.User;
import com.sun.mail.imap.IMAPStore;

import java.util.Map;

/**
 * <b><code>UserService</code></b>
 * <p/>
 * 用户Service层
 * <p/>
 * <b>Creation Time:</b> 2019-04-14 19:23.
 *
 * @author 李尚哲
 * @since mail 1.0
 */
public interface UserService {

    /**
     * 用户登陆
     * @param username  用户名
     * @param password  密码
     * @return 如果登陆成功，返回一个用户实体对象，如果登陆失败，则返回null
     */
    User queryForLogin(String username, String password);

    /**
     * 用户注册
     * @param username  用户名
     * @param password  密码
     * @return  如果用户名不存在，则注册成功，返回true，否则注册失败，返回false
     */
    boolean register(String username, String password);

    /**
     * 通过username获取用户实体类
     *
     * @param username 用户名
     * @return 用户实体类
     */
    User getUser(String username);

    /**
     * 获取各文件夹中邮件计数
     *
     * @param store 用户邮箱空间
     * @return 邮件计数
     */
    Map<String, Integer> getMessageCount(IMAPStore store);
}