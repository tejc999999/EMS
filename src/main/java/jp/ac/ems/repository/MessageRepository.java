package jp.ac.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.ac.ems.bean.MessageBean;
import jp.ac.ems.bean.StudentTaskHistoryBean;

/**
 * メッセージ用リポジトリ(message repository).
 * @author tejc999999
 */
public interface MessageRepository extends JpaRepository<MessageBean, Long> {
}