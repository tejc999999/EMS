
package jp.ac.ems.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.ems.bean.ClassBean;

/**
 * 問題用リポジトリ(question repository).
 * 
 * @author tejc999999
 *
 */
public interface ClassRepository  extends JpaRepository<ClassBean, Long> {
    
    @Query("SELECT DISTINCT c FROM ClassBean c"
    		+ " LEFT JOIN FETCH c.userClassBeans"
    		+ " LEFT JOIN FETCH c.classCourseBeans"
    		+ " WHERE c.id = :id")
    Optional<ClassBean> findByIdFetchAll(@Param("id") Long id);
    
    @Query("SELECT DISTINCT c FROM ClassBean c"
    		+ " LEFT JOIN FETCH c.userClassBeans"
    		+ " LEFT JOIN FETCH c.classCourseBeans")
    List<ClassBean> findAllFetchAll();
}
