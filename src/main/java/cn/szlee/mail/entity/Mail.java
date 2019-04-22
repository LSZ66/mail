package cn.szlee.mail.entity;

import lombok.Data;

/**
 * <b><code>Mail</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019-04-16 15:23.
 *
 * @author 李尚哲
 * @since mail 1.0
 */
@Data
public class Mail {

    private int id;
    private String from;
    private String to;
    private String subject;
    private String receiveTime;
    private String sendTime;
    private String text;
    private Integer state;
}
