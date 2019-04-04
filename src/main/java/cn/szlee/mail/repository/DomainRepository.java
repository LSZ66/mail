package cn.szlee.mail.repository;

import cn.szlee.mail.entity.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author 李尚哲
 * 域名持久层
 */
public interface DomainRepository extends JpaRepository<Domain, Integer>, JpaSpecificationExecutor<Domain> {

}