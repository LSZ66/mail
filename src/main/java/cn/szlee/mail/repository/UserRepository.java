package cn.szlee.mail.repository;

import cn.szlee.mail.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 李尚哲
 * 用户持久层
 */
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    /**
     * 新增用户
     *
     * @param email    用户名
     * @param password 密码
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query(value =
            "insert into virtual_users(domain_id, email, password) " +
                    "value(1, ?1, ENCRYPT(?2, CONCAT('$6$', SUBSTRING(SHA(RAND()), -16))))",
            nativeQuery = true)
    void insert(String email, String password);

    /**
     * 根据email查找用户
     *
     * @param email 用户邮箱地址
     * @return 用户实体类
     */
    User findByEmail(String email);

    /**
     * 根据email和登陆密码查询用户
     *
     * @param email    用户邮箱地址
     * @param password 用户密码
     * @return 用户实体类
     */
    @Query(value = "SELECT * FROM virtual_users " +
            "WHERE email = ?1 AND `password` = ENCRYPT(?2, `password`)",
            nativeQuery = true)
    User findByEmailAndPassword(String email, String password);
}