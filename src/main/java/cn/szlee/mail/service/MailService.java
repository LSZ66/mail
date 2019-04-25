package cn.szlee.mail.service;

import cn.szlee.mail.entity.Mail;
import com.sun.mail.imap.IMAPStore;

import javax.mail.internet.MimeMessage;
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
     * 获取某文件夹中邮件列表
     *
     * @param box   文件夹
     * @param store 用户邮箱空间
     * @return 该文件夹邮件列表
     */
    List<Mail> getListByBox(String box, IMAPStore store);

    /**
     * 根据文件夹和id取出一份邮件
     *
     * @param box   指定文件夹
     * @param id    邮件id
     * @param store 用户邮件空间
     * @return id对应的邮件
     */
    Mail getMessageById(String box, int id, IMAPStore store);

    /**
     * 将邮件保存到指定文件夹
     *
     * @param store     用户邮件空间
     * @param box       文件夹
     * @param message   信息
     */
    void saveToBox(IMAPStore store, String box, MimeMessage... message);

    /**
     * 移动邮件
     *
     * @param store   用户邮件空间
     * @param srcBox  源文件夹
     * @param destBox 目标文件夹
     * @param msgIds  邮件id
     */
    void moveToBox(IMAPStore store, String srcBox, String destBox, int... msgIds);

    /**
     * 删除邮件
     *
     * @param store  用户邮件空间
     * @param box    文件夹
     * @param msgIds 邮件id
     */
    void delete(IMAPStore store, String box, int... msgIds);

    /**
     * 设置收件箱邮件已读
     *
     * @param store  用户邮件空间
     * @param msgIds 邮件id
     */
    void setSeen(IMAPStore store, int... msgIds);

    /**
     * 搜索邮件内容
     *
     * @param store   用户邮件空间
     * @param box     文件夹
     * @param pattern 搜索的关键字
     * @return 搜索到的邮件
     */
    List<Mail> search(IMAPStore store, String box, String pattern);

    /**
     * 标记一封邮件为垃圾/普通邮件
     *
     * @param store 用户邮件空间
     * @param box   文件夹
     * @param id    邮件id
     * @param type  类型：垃圾邮件或正常邮件
     */
    void markAs(IMAPStore store, String box, int id, String type);
}
