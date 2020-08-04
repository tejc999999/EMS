package jp.ac.ems.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.ac.ems.bean.StudentTaskHistoryBean;
import jp.ac.ems.bean.StudentTaskQuestionHistoryBean;

/**
 * 学生課題-問題履歴用リポジトリ(student task-question history repository).
 * @author tejc999999
 */
public interface StudentTaskQuestionHistoryRepository extends JpaRepository<StudentTaskQuestionHistoryBean, Long> {

    Optional<StudentTaskQuestionHistoryBean> findByUserIdAndTaskId(String userId, Long taskId);
}