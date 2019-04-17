package cn.szlee.mail.controller;

import cn.szlee.mail.config.Constant;
import cn.szlee.mail.entity.Mail;
import cn.szlee.mail.service.MailService;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * <b><code>MailController</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019-04-14 20:01.
 *
 * @author 李尚哲
 * @since mail 1.0
 */
@RestController
@RequestMapping("/mail")
@CrossOrigin
public class MailController {

    @Autowired
    private MailService service;

    @GetMapping("/getInbox")
    public List<Mail> getInboxList(HttpSession session) {
        IMAPStore userStore = (IMAPStore) session.getAttribute("userStore");
        return service.getInboxList(userStore);
    }

    @GetMapping("/getOutbox")
    public List<Mail> getOutboxList(HttpSession session) {
        IMAPStore userStore = (IMAPStore) session.getAttribute("userStore");
        return service.getOutboxList(userStore);
    }

    @GetMapping("/getMsg")
    public Mail getMessageById(Integer id, HttpSession session) throws MessagingException {
        if (id == null) {
            return null;
        }
        IMAPStore userStore = (IMAPStore) session.getAttribute("userStore");
        return service.getMessageById((IMAPFolder) userStore.getFolder(Constant.INBOX), id);
    }

    @PostMapping
    public void send(@RequestBody Mail mail, HttpSession session) throws MessagingException, UnsupportedEncodingException {
        System.out.println(mail);
        JavaMailSender sender = (JavaMailSender) session.getAttribute("userSender");
        String email = (String) session.getAttribute("userEmail");
        String name = (String) session.getAttribute("userName");
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, Constant.UTF8);
        helper.setFrom(email, name);
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getText(), true);
        sender.send(message);
        IMAPStore userStore = (IMAPStore) session.getAttribute("userStore");
        service.saveToOutbox(message, userStore);
    }
}
