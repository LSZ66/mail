package cn.szlee.mail.repository;

import cn.szlee.mail.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 李尚哲
 * 用户持久层
 */
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    @Modifying
    @Query(value =
            "insert into virtual_users(domain_id, email, password) " +
            "value(1, ?1, ENCRYPT(?2, CONCAT('$6$', SUBSTRING(SHA(RAND()), -16))))",
            nativeQuery = true)
    void insert(String username, String password);

    User findByEmail(String email);
}