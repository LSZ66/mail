package cn.szlee.mail.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * <b><code>Draft</code></b>
 * <p/>
 * 草稿箱实体类
 * <p/>
 * <b>Creation Time:</b> 2019/4/21 13:51.
 *
 * @author 李尚哲
 * @since mail ${PROJECT_VERSION}
 */
@Entity
@Data
@ToString
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Draft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int userId;

    @Column(name = "[to]")
    private String to;

    @Column(name = "[subject]")
    private String subject;

    @Column(columnDefinition = "text")
    private String text;

    @Column
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Timestamp lastModifyTime;
}
