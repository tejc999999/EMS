package jp.ac.ems.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.ems.bean.TaskBean;

/**
 * 課題用リポジトリ(taks repository).
 * @author tejc999999
 */
public interface TaskRepository extends JpaRepository<TaskBean, Long> {

	/**
	 * 課題-問題関連Beanを含めたTaskBean取得（Fetch.LAZY対応）
	 * (Acquisition of Task Bean including related beans (Fetch.LAZY compatible)).
	 * 
	 * @param id 課題ID(Task Id).
	 * @return 課題Bean（Task Bean）.
	 */
    @Query("SELECT DISTINCT t FROM TaskBean t"
    		+ " LEFT JOIN FETCH t.taskQuestionBeans"
//    		+ " LEFT JOIN FETCH t.studentTaskBeans"
//    		+ " LEFT JOIN FETCH t.studentTaskQuestionHistoryBeans"
    		+ " WHERE t.id = :id")
    Optional<TaskBean> findByIdFetchTaskQuestion(@Param("id") Long id);

}