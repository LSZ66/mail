package cn.szlee.mail.controller;

import cn.szlee.mail.entity.User;
import cn.szlee.mail.service.UserService;
import com.sun.mail.imap.IMAPStore;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * <b><code>UserController</code></b>
 * <p/>
 * 用户控制器类
 * <p/>
 * <b>Creation Time:</b> 2019-04-14 19:51.
 *
 * @author 李尚哲
 * @since mail 1.0
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public boolean register(@RequestBody Map<String, String> user) {
        return service.register(user.get("username"), user.get("password"));
    }

    @PostMapping("/login")
    public boolean login(@RequestBody Map<String, String> req, HttpSession session) {
        String originPassword = req.get("password");
        User user = service.queryForLogin(req.get("username"), originPassword);
        if (user != null) {
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getName());
            session.setAttribute("password", originPassword);
            session.setAttribute("userEmail", user.getEmail());
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("/banner")
    public String getBannerName(HttpSession session) {
        String name = (String) session.getAttribute("userName");
        return name != null ?
                name : (String) session.getAttribute("userEmail");
    }

    @GetMapping("/name")
    public String getName(HttpSession session) {
        return (String) session.getAttribute("userName");
    }

    @GetMapping("/overview")
    public Map<String, Integer> getOverview(HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return null;
        }
        JavaMailSender userSender = (JavaMailSender) session.getAttribute("userSender");
        Map<String, Integer> overview;
        //未登录
        if (userSender == null) {
            //获取原始密码登录
            String password = (String) session.getAttribute("password");
            Map<String, Object> map = service.getSenderAndReceiver(userEmail, password);
            Object sender = map.get("sender");
            Object store = map.get("store");
            session.setAttribute("userSender", sender);
            session.setAttribute("userStore", store);
            Map<String, Folder> userFolder = service.getUserFolder((IMAPStore) store);
            userFolder.forEach(session::setAttribute);
            overview = service.getMessageCount(userFolder);
        } else {
            Folder inbox = (Folder) session.getAttribute("inbox");
            Folder outbox = (Folder) session.getAttribute("outbox");
            Folder spam = (Folder) session.getAttribute("spam");
            Folder recycle = (Folder) session.getAttribute("recycle");
            Map<String, Folder> userFolder = new HashMap<>(4);
            userFolder.put("inbox", inbox);
            userFolder.put("outbox", outbox);
            userFolder.put("spam", spam);
            userFolder.put("recycle", recycle);
            overview = service.getMessageCount(userFolder);
        }
        return overview;
    }

    @GetMapping("/logout")
    public void logout(HttpSession session) throws MessagingException {
        ((Folder) session.getAttribute("inbox")).close();
        ((Folder) session.getAttribute("outbox")).close();
        ((Folder) session.getAttribute("spam")).close();
        ((Folder) session.getAttribute("recycle")).close();
        IMAPStore store = (IMAPStore) session.getAttribute("userStore");
        store.close();
        session.removeAttribute("userName");
        session.removeAttribute("userId");
        session.removeAttribute("userEmail");
        session.removeAttribute("userSender");
        session.removeAttribute("userStore");
        session.removeAttribute("password");
    }

    @PatchMapping
    public void updateName(String name, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (name.length() == 0) {
            name = null;
        }
        service.updateName(userId, name);
        session.removeAttribute("username");
        session.setAttribute("userName", name);
    }

    @PutMapping
    public boolean updateInfo(@RequestBody Map<String, String> req, HttpSession session) throws InvocationTargetException, IllegalAccessException {
        String oldPassword = (String) session.getAttribute("password");
        String old = req.get("old");
        if (!oldPassword.equals(old)) {
            return false;
        }
        User user = new User();
        BeanUtils.populate(user, req);
        Integer userId = (Integer) session.getAttribute("userId");
        user.setId(userId);
        service.updateInfo(user);
        return true;
    }

    @GetMapping("/dateCount")
    public List<Map> getDateCount(HttpSession session) {
        IMAPStore store = (IMAPStore) session.getAttribute("userStore");
        Set<Map.Entry<String, Integer>> entries = service.getDateCount(store).entrySet();
        List<Map> list = new ArrayList<>();
        entries.forEach(item -> {
            HashMap<String, Object> map = new HashMap<>(1);
            map.put("date", item.getKey());
            map.put("acc", item.getValue());
            list.add(map);
        });
        return list;
    }
}
