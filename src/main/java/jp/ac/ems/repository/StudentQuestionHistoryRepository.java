package jp.ac.ems.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.ac.ems.bean.StudentQuestionHistoryBean;
import jp.ac.ems.bean.StudentTaskQuestionHistoryBean;

/**
 * 学生-問題履歴用リポジトリ(student question history repository).
 * @author tejc999999
 */
public interface StudentQuestionHistoryRepository extends JpaRepository<StudentQuestionHistoryBean, Long> {

	/**
	 * 
	 * @return
	 */
	Optional<StudentQuestionHistoryBean> findByUserIdAndQuestionId(String userId, Long questionId);
}