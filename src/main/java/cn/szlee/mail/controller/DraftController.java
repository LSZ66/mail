package cn.szlee.mail.controller;

import cn.szlee.mail.entity.Draft;
import cn.szlee.mail.service.DraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <b><code>DraftController</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/4/21 14:29.
 *
 * @author 李尚哲
 * @since mail ${PROJECT_VERSION}
 */
@RestController
@RequestMapping("/api/draft")
public class DraftController {

    @Autowired
    private DraftService service;

    @PostMapping
    public void add(@RequestBody Draft draft, HttpSession session) {
        int userId = (Integer) session.getAttribute("userId");
        draft.setUserId(userId);
        service.save(draft);
    }

    @GetMapping("/list")
    public List<Draft> getList(HttpSession session) {
        int userId = (Integer) session.getAttribute("userId");
        return service.getList(userId);
    }

    @GetMapping("/{id}")
    public Draft getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PutMapping
    public void update(@RequestBody Draft draft) {
        service.save(draft);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.deleteById(id);
    }

    @DeleteMapping("/list")
    public void delete(@RequestBody List<Integer> ids) {
        service.deleteById(ids);
    }
}
