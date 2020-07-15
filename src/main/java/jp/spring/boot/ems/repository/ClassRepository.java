
package jp.spring.boot.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.spring.boot.ems.bean.ClassBean;

/**
 * 問題用リポジトリ(question repository).
 * 
 * @author tejc999999
 *
 */
public interface ClassRepository  extends JpaRepository<ClassBean, Long> {
    
}
