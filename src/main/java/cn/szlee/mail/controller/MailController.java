package cn.szlee.mail.controller;

import cn.szlee.mail.config.Constant;
import cn.szlee.mail.entity.Mail;
import cn.szlee.mail.service.MailService;
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
 * 邮件控制器类
 * <p/>
 * <b>Creation Time:</b> 2019-04-14 20:01.
 *
 * @author 李尚哲
 * @since mail 1.0
 */
@RestController
@RequestMapping("/api/mail")
@CrossOrigin
public class MailController {

    @Autowired
    private MailService service;

    @GetMapping("/getList/{box}")
    public List<Mail> getList(@PathVariable String box, HttpSession session) {
        IMAPStore userStore = (IMAPStore) session.getAttribute("userStore");
        return service.getListByBox(box, userStore);
    }

    @GetMapping("/getMsg/{box}/{id}")
    public Mail getMessageById(@PathVariable String box, @PathVariable Integer id, HttpSession session) {
        if (id == null) {
            return null;
        }
        IMAPStore userStore = (IMAPStore) session.getAttribute("userStore");
        return service.getMessageById(box, id, userStore);
    }

    @GetMapping("/search/{box}/{pattern}")
    public List<Mail> search(@PathVariable String box, @PathVariable String pattern, HttpSession session) {
        IMAPStore userStore = (IMAPStore) session.getAttribute("userStore");
        return service.search(userStore, box, pattern);
    }

    @PostMapping
    public void send(@RequestBody Mail mail, HttpSession session) throws MessagingException, UnsupportedEncodingException {
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
        service.saveToBox(userStore, Constant.OUTBOX, message);
    }

    @PutMapping("/setSeen")
    public void setSeen(@RequestBody int[] msgIds, HttpSession session) {
        IMAPStore userStore = (IMAPStore) session.getAttribute("userStore");
        service.setSeen(userStore, msgIds);
    }

    @PutMapping("/move/{src}/{dest}")
    public void move(@PathVariable String src, @PathVariable String dest, @RequestBody int[] msgIds, HttpSession session) {
        IMAPStore userStore = (IMAPStore) session.getAttribute("userStore");
        service.moveToBox(userStore, src, dest, msgIds);
    }

    @DeleteMapping("/delete/{box}")
    public void delete(@PathVariable String box, @RequestBody int[] msgIds, HttpSession session) {
        IMAPStore userStore = (IMAPStore) session.getAttribute("userStore");
        service.delete(userStore, box, msgIds);
    }

    @PatchMapping("/mark/{box}/{type}")
    public void markAs(@PathVariable String box,
                       @PathVariable String type,
                       @RequestBody int[] msgIds,
                       HttpSession session) {
        IMAPStore userStore = (IMAPStore) session.getAttribute("userStore");
        service.markAs(userStore, box, type, msgIds);
    }
}
