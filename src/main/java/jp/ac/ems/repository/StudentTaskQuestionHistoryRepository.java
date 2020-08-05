package jp.ac.ems.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.ac.ems.bean.StudentTaskQuestionHistoryBean;

/**
 * 学生課題-問題履歴用リポジトリ(student task-question history repository).
 * @author tejc999999
 */
public interface StudentTaskQuestionHistoryRepository extends JpaRepository<StudentTaskQuestionHistoryBean, Long> {

	/**
	 * 学生の、特定の課題に対する問題回答履歴を取得する（Get the student's question answer history for a specific assignment）.
     * @param userId ユーザーID(user id)
     * @param taskId 課題ID（task id）
	 * @param questionId 問題ID(question id)
	 * @return 回答履歴(task question history)
	 */
    Optional<StudentTaskQuestionHistoryBean> findByUserIdAndTaskIdAndQuestionId(String userId, Long taskId, Long questionId);

    /**
     * 学生の、特定の課題に対する回答履歴を全て取得する(Get all the response history of a student for a specific task)
     * @param userId ユーザーID(user id)
     * @param taskId 課題ID（task id）
     * @return 回答履歴リスト（task question history list)
     */
    List<StudentTaskQuestionHistoryBean> findByUserIdAndTaskId(String userId, Long taskId);
}