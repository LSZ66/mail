package cn.szlee.mail.utils;

import cn.szlee.mail.config.Constant;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * <b><code>MailUtil</code></b>
 * <p/>
 * 邮件接收器
 * <p/>
 * <b>Creation Time:</b> 2019/4/4 17:34.
 *
 * @author 李尚哲
 * @since mail 1.0
 */
public class MailUtil {

    /**
     * 获取用户邮箱空间
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户邮箱空间
     */
    public static IMAPStore getStore(String username, String password) throws MessagingException {
        Properties props = System.getProperties();
        props.setProperty("mail.imap.host", Constant.DOMAIN);
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.ssl.enable", "true");
        props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.store.protocol", Constant.IMAP);
        Session session = Session.getInstance(props);
        IMAPStore store = (IMAPStore) session.getStore(Constant.IMAP);
        store.connect(username + Constant.MAIL_SUFFIX, password);
        return store;
    }

    /**
     * 获取用户收件箱
     *
     * @param store 用户邮箱空间
     * @return 用户收件箱
     */
    public static IMAPFolder getInbox(IMAPStore store) throws MessagingException {
        IMAPFolder folder = (IMAPFolder) store.getFolder(Constant.INBOX);
        if (folder.exists()) {
            folder.open(Folder.READ_WRITE);
        }
        return folder;
    }

    /**
     * 获取用户发件箱
     *
     * @param store 用户邮箱空间
     * @return 用户发件箱
     */
    public static IMAPFolder getOutbox(IMAPStore store) throws MessagingException {
        IMAPFolder folder = (IMAPFolder) store.getFolder(Constant.OUTBOX);
        if (!folder.exists()) {
            folder.create(Folder.HOLDS_MESSAGES);
        }
        folder.open(Folder.READ_WRITE);
        return folder;
    }

    /**
     * 获得邮件主题
     *
     * @param msg 邮件内容
     * @return 解码后的邮件主题
     */
    public static String getSubject(MimeMessage msg) throws UnsupportedEncodingException, MessagingException {
        return msg.getSubject() != null ? MimeUtility.decodeText(msg.getSubject()) : null;
    }

    /**
     * 获得邮件发件人
     *
     * @param msg 邮件内容
     * @return 若存在用户签名，则返回用户签名，否则返回邮箱地址
     */
    public static String getFrom(MimeMessage msg) throws MessagingException {
        Address[] froms = msg.getFrom();
        if (froms.length < 1) {
            return "没有发件人";
        }

        InternetAddress address = (InternetAddress) froms[0];
        String person = address.getPersonal();
        return person != null ? person : address.getAddress();
    }

    /**
     * 获取发件人所有信息
     *
     * @param msg 邮件体
     * @return 发件人信息
     */
    public static String getFullFrom(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {
        Address[] froms = msg.getFrom();
        if (froms.length < 1) {
            return "没有发件人!";
        }

        InternetAddress address = (InternetAddress) froms[0];
        String person = address.getPersonal();
        if (person != null) {
            person = MimeUtility.decodeText(person) + " ";
        } else {
            person = "";
        }
        return person + "<" + address.getAddress() + ">";
    }

    /**
     * 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人
     * <p>RecipientType.TO  收件人</p>
     * <p>RecipientType.CC  抄送</p>
     * <p>RecipientType.BCC 密送</p>
     *
     * @param msg 邮件内容
     * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ...
     */
    public static String getReceiveAddress(MimeMessage msg) throws MessagingException {
        StringBuilder receiveAddress = new StringBuilder();
        Address[] address = msg.getAllRecipients();

        if (address == null || address.length < 1) {
            return "没有收件人";
        }
        for (Address x : address) {
            InternetAddress internetAddress = (InternetAddress) x;
            receiveAddress.append(internetAddress.toUnicodeString()).append(",");
        }

        //删除最后一个逗号
        receiveAddress.deleteCharAt(receiveAddress.length() - 1);
        return receiveAddress.toString();
    }

    /**
     * 获得邮件发送时间
     *
     * @param msg 邮件内容
     * @return yyyy年mm月dd日 星期X HH:mm
     */
    public static String getSentDate(MimeMessage msg) throws MessagingException {
        Date receivedDate = msg.getSentDate();
        if (receivedDate == null) {
            return "-";
        }

        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(receivedDate);
    }

    /**
     * 获取邮件文本内容
     *
     * @param message 邮件体
     * @return 邮件文本内容
     */
    public static String getMailTextContent(Message message) throws IOException, MessagingException {
        StringBuffer sb = new StringBuffer(2048);
        getMailTextContent(message, sb);
        return sb.toString();
    }

    /**
     * 获得邮件文本内容
     *
     * @param part    邮件体
     * @param content 存储邮件文本内容的字符串
     */
    private static void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {
        //如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
        if (part.isMimeType(Constant.MAIL_TEXT) && !isContainTextAttach) {
            content.append(part.getContent().toString());
        } else if (part.isMimeType(Constant.MAIL_RFC822)) {
            getMailTextContent((Part) part.getContent(), content);
        } else if (part.isMimeType(Constant.MAIL_MULTIPART)) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                getMailTextContent(bodyPart, content);
            }
        }
    }

    /**
     * 判断邮件是否已读
     *
     * @param msg 邮件体
     * @return 如果邮件已读返回true, 否则返回false
     */
    public static boolean isSeen(MimeMessage msg) throws MessagingException {
        return msg.getFlags().contains(Flags.Flag.SEEN);
    }

    /**
     * 判断邮件是否已回复
     *
     * @param msg 邮件体
     * @return 如果邮件已回复返回true, 否则返回false
     */
    public static boolean isAnswered(MimeMessage msg) throws MessagingException {
        return msg.getFlags().contains(Flags.Flag.ANSWERED);
    }

    /**
     * 判断邮件是否已删除
     *
     * @param msg 邮件体
     * @return 如果邮件已删除返回true, 否则返回false
     */
    public static boolean isDeleted(MimeMessage msg) throws MessagingException {
        return msg.getFlags().contains(Flags.Flag.DELETED);
    }
}