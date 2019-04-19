package cn.szlee.mail.config;

/**
 * <b><code>Constant</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019-04-17 10:39.
 *
 * @author 李尚哲
 * @since mail 1.0
 */
public class Constant {

    public static final String DOMAIN = "szlee.cn";
    public static final String MAIL_SUFFIX = "@" + DOMAIN;
    public static final String IMAP = "imap";
    public static final String INBOX = "INBOX";
    public static final String OUTBOX = "发件箱";
    public static final String DRAFT_BOX = "草稿箱";
    public static final String SPAM_BOX = "垃圾箱";
    public static final String RECYCLE = "回收站";
    public static final String MAIL_TEXT = "text/*";
    public static final String MAIL_RFC822 = "message/rfc822";
    public static final String MAIL_MULTIPART = "multipart/*";
    public static final String UTF8 = "UTF-8";

    private Constant() {
    }
}
