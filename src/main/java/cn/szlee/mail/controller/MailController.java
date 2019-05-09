package cn.szlee.mail.controller;

import cn.szlee.mail.config.Constant;
import cn.szlee.mail.entity.Mail;
import cn.szlee.mail.service.MailService;
import cn.szlee.mail.utils.MailUtil;
import com.sun.mail.imap.IMAPFolder;
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

    @GetMapping("/count/{box}")
    public int getTotalCount(@PathVariable String box, HttpSession session) {
        IMAPFolder folder = (IMAPFolder) session.getAttribute(box);
        return service.getTotalCount(folder);
    }

    @GetMapping("/list/{box}/{pageNo}")
    public List<Mail> getList(@PathVariable String box,
                              @PathVariable Integer pageNo,
                              HttpSession session) {
        IMAPFolder folder = (IMAPFolder) session.getAttribute(box);
        return service.getListByBox(folder, pageNo);
    }

    @GetMapping("/msg/{box}/{id}")
    public Mail getMessageById(@PathVariable String box, @PathVariable Integer id, HttpSession session) {
        if (id == null) {
            return null;
        }
        IMAPFolder folder = (IMAPFolder) session.getAttribute(box);
        return service.getMessageById(folder, id);
    }

    @GetMapping("/listCond/{box}/{pattern}")
    public List<Mail> search(@PathVariable String box, @PathVariable String pattern, HttpSession session) {
        IMAPFolder folder = (IMAPFolder) session.getAttribute(box);
        return service.search(folder, pattern);
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
        if (mail.isReply()) {
            IMAPFolder inbox = (IMAPFolder) session.getAttribute("inbox");
            service.setReply(inbox, mail.getId());
        }
        IMAPFolder folder = (IMAPFolder) session.getAttribute("outbox");
        service.saveToBox(folder, message);
    }

    @PutMapping
    public void setSeen(@RequestBody int[] msgIds, HttpSession session) {
        IMAPFolder folder = (IMAPFolder) session.getAttribute("inbox");
        service.setSeen(folder, msgIds);
    }

    @PutMapping("/{src}/{dest}")
    public void move(@PathVariable String src, @PathVariable String dest, @RequestBody int[] msgIds, HttpSession session) {
        IMAPFolder srcFolder = (IMAPFolder) session.getAttribute(src);
        IMAPFolder destFolder = (IMAPFolder) session.getAttribute(dest);
        service.moveToBox(srcFolder, destFolder, msgIds);
    }

    @DeleteMapping("/{box}")
    public void delete(@PathVariable String box, @RequestBody int[] msgIds, HttpSession session) {
        IMAPFolder folder = (IMAPFolder) session.getAttribute(box);
        service.delete(folder, msgIds);
    }

    @PatchMapping("/{box}/{type}")
    public void markAs(@PathVariable String box,
                       @PathVariable String type,
                       @RequestBody int[] msgIds,
                       HttpSession session) {
        IMAPFolder folder = (IMAPFolder) session.getAttribute(box);
        service.markAs(folder, type, msgIds);
    }
}
