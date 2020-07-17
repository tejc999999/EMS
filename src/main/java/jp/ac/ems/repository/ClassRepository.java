
package jp.ac.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.ac.ems.bean.ClassBean;

/**
 * 問題用リポジトリ(question repository).
 * 
 * @author tejc999999
 *
 */
public interface ClassRepository  extends JpaRepository<ClassBean, Long> {
    
}
