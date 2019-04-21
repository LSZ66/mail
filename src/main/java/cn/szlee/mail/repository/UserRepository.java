package cn.szlee.mail.repository;

import cn.szlee.mail.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 李尚哲
 * 用户持久层
 */
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    /**
     * 获取加密的密码
     *
     * @param password  明文密码
     * @return 加密的密码
     */
    @Query(value = "SELECT ENCRYPT(?1, CONCAT('$6$', SUBSTRING(SHA(RAND()), -16)))", nativeQuery = true)
    char[] getEncryptPassword(String password);

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