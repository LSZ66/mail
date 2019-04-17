package cn.szlee.mail.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author 李尚哲
 * 用户实体类
 */
@Entity
@Data
@Table(name = "virtual_users")
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(insertable = false, nullable = false)
    private Integer id;

    @Column(name = "domain_id", nullable = false)
    private Integer domainId = 1;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column
    private String name;
}