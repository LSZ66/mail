package cn.szlee.mail.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author 李尚哲
 * 邮箱别名实体类
 */
@Data
@Entity
@Table(name = "virtual_aliases")
@ToString
public class Alias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;

    @Column(name = "domain_id", nullable = false)
    private Integer domainId;

    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "destination", nullable = false)
    private String destination;
}