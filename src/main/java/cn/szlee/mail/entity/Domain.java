package cn.szlee.mail.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author 李尚哲
 * 域名实体类
 */
@Entity
@Table(name = "virtual_domains")
@Data
@ToString
public class Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(insertable = false, nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;
}