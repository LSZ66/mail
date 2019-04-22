package cn.szlee.mail.service;

import cn.szlee.mail.entity.Draft;

import java.util.List;

/**
 * <b><code>DraftService</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/4/21 13:57.
 *
 * @author 李尚哲
 * @since mail ${PROJECT_VERSION}
 */
public interface DraftService {

    /**
     * 保存草稿到草稿箱
     *
     * @param draft 稿件实体类
     */
    void save(Draft draft);

    /**
     * 根据用户id获取所有草稿
     *
     * @param userId 草稿
     * @return 草稿列表
     */
    List<Draft> getList(int userId);

    /**
     * 根据草稿id获取稿件
     *
     * @param id 草稿id
     * @return 稿件实体类
     */
    Draft getById(int id);

    /**
     * 根据id删除稿件
     *
     * @param id 稿件id
     */
    void deleteById(int id);
}
