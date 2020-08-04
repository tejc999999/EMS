package jp.ac.ems.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.ems.bean.StudentTaskHistoryBean;

/**
 * 学生課題履歴用リポジトリ(student task history repository).
 * @author tejc999999
 */
public interface StudentTaskHistoryRepository extends JpaRepository<StudentTaskHistoryBean, Long> {

	/**
	 * 学生・課題-問題履歴Beanを含めたStudentTaskHistoryBean取得（Fetch.LAZY対応）
	 * (Acquisition of Student task history Bean including related beans (Fetch.LAZY compatible)).
	 * 
	 * @param id 学生・課題履歴ID(student task question Id).
	 * @return 学生課題履歴Bean（Student task history Bean）.
	 */
    @Query("SELECT DISTINCT s FROM StudentTaskHistoryBean s"
    		+ " LEFT JOIN FETCH s.TaskQuestionHistoryBeans"
    		+ " WHERE s.id = :id")
    Optional<StudentTaskHistoryBean> findByIdFetchUserTask(@Param("id") String id);
}