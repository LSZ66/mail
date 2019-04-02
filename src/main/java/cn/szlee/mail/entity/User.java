package cn.szlee.mail.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * <b><code>User</code></b>
 * <p/>
 * 用户实体类
 * <p/>
 * <b>Creation Time:</b> 2019/4/2 16:19.
 *
 * @author 李尚哲
 * @since mail 1.0
 */
@Data
@Entity
public class User {
    /**
     * 主键
     */
    @Id
    private int id;
    /**
     * 用户名，唯一索引
     */
    @Column(nullable = false, unique = true)
    private String username;
    /**
     * 密码
     */
    @Column(nullable = false)
    private String password;
    /**
     * 用户签名，发送邮件时显示
     */
    @Column
    private String sign;
}
