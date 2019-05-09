package cn.szlee.mail.repository;

import cn.szlee.mail.entity.Draft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * <b><code>DraftRepository</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/4/21 13:51.
 *
 * @author 李尚哲
 * @since mail ${PROJECT_VERSION}
 */
public interface DraftRepository extends JpaRepository<Draft, Integer>, JpaSpecificationExecutor<Draft> {

    /**
     * 根据id倒叙查询
     *
     * @return 稿件
     */
    List<Draft> findAllByUserIdOrderByIdDesc(int userId);
}
