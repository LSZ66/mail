package cn.szlee.mail.repository;

import cn.szlee.mail.entity.Alias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author 李尚哲
 * 邮箱别名持久层
 */
public interface AliasRepository extends JpaRepository<Alias, Integer>, JpaSpecificationExecutor<Alias> {

}