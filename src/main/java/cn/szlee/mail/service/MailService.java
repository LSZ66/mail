package cn.szlee.mail.service;

import cn.szlee.mail.entity.Mail;
import com.sun.mail.imap.IMAPFolder;

import java.util.List;

/**
 * <b><code>MailService</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019-04-15 15:30.
 *
 * @author 李尚哲
 * @since mail 1.0
 */
public interface MailService {

    /**
     * 获取用户收件箱列表
     *
     * @param folder 用户收件箱
     * @return 收件箱列表
     */
    List<Mail> getInboxList(IMAPFolder folder);

    /**
     * 从收件箱中取出一份邮件
     *
     * @param folder 收件箱
     * @param id     邮件id
     * @return id对应的邮件
     */
    Mail getMessageById(IMAPFolder folder, int id);
}
