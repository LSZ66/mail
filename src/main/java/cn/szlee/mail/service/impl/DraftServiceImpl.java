package cn.szlee.mail.service.impl;

import cn.szlee.mail.entity.Draft;
import cn.szlee.mail.repository.DraftRepository;
import cn.szlee.mail.service.DraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <b><code>DraftServiceImpl</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/4/21 14:01.
 *
 * @author 李尚哲
 * @since mail ${PROJECT_VERSION}
 */
@Service
public class DraftServiceImpl implements DraftService {

    @Autowired
    private DraftRepository repo;

    @Override
    public void save(Draft draft) {
        repo.save(draft);
    }

    @Override
    public List<Draft> getList(int userId) {
        return repo.findAll();
    }

    @Override
    public Draft getById(int id) {
        return repo.getOne(id);
    }

    @Override
    public void deleteById(int id) {
        repo.deleteById(id);
    }
}
