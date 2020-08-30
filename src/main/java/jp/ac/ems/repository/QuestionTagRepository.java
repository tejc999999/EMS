
package jp.ac.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.ac.ems.bean.QuestionTagBean;

/**
 * 問題タグ用リポジトリ(question tag repository).
 * 
 * @author tejc999999
 *
 */
public interface QuestionTagRepository  extends JpaRepository<QuestionTagBean, Long> {
}
