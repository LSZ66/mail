package cn.szlee.mail.service;

import cn.szlee.mail.entity.Mail;
import com.sun.mail.imap.IMAPFolder;

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
     * @param folder 文件夹
     * @param pageNo 页码
     * @return 该文件夹邮件列表
     */
    List<Mail> getListByBox(IMAPFolder folder, int pageNo);

    /**
     * 获取一份邮件
     *
     * @param folder 文件夹
     * @param id     邮件id
     * @return 邮件
     */
    Mail getMessageById(IMAPFolder folder, int id);

    /**
     * 将邮件保存到指定文件夹
     *
     * @param folder  文件夹
     * @param message 邮件
     */
    void saveToBox(IMAPFolder folder, MimeMessage... message);

    /**
     * 移动邮件
     *
     * @param srcFolder  源文件夹
     * @param destFolder 目标文件夹
     * @param msgIds     邮件id
     */
    void moveToBox(IMAPFolder srcFolder, IMAPFolder destFolder, int... msgIds);

    /**
     * 删除邮件
     *
     * @param folder 文件夹
     * @param msgIds 邮件id
     */
    void delete(IMAPFolder folder, int... msgIds);

    /**
     * 设置收件箱邮件已读
     *
     * @param folder 文件夹
     * @param msgIds 邮件id
     */
    void setSeen(IMAPFolder folder, int... msgIds);

    /**
     * 设置邮件已回复
     *
     * @param folder 文件夹
     * @param msgIds 邮件id
     */
    void setReply(IMAPFolder folder, int... msgIds);


    /**
     * 搜索邮件内容
     *
     * @param folder  文件夹
     * @param pattern 搜索的关键字
     * @return 搜索到的邮件
     */
    List<Mail> search(IMAPFolder folder, String pattern);

    /**
     * 标记一封邮件为垃圾/普通邮件
     *
     * @param folder 文件夹
     * @param type   类型：垃圾邮件或正常邮件
     * @param msgIds 邮件id
     */
    void markAs(IMAPFolder folder, String type, int... msgIds);

    /**
     * 查询邮件总数
     *
     * @param folder 文件夹
     * @return 文件夹邮件总数
     */
    int getTotalCount(IMAPFolder folder);
}
