package jp.ac.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.bean.TaskBean;

/**
 * 問題用リポジトリ(question repository).
 * @author tejc999999
 */
public interface QuestionRepository extends
                                    JpaRepository<QuestionBean, Long> {

	
	  /**
	  * 年度、期、タイトルと説明文に検索文字列が含まれることを条件とした取得.
	  * 
	  * @return 問題Beanリスト
	  */
//	 List<QuestionBean> findDistinctYearAndTerm();
	 @Query("select distinct new jp.ac.ems.bean.QuestionBean(q.year, q.term) from QuestionBean q")
	 List<QuestionBean> findDistinctYearAndTerm();
	
//    /**
//     * タイトルと説明文に検索文字列が含まれることを条件とした取得.
//     * 
//     * @param searchTitleStr タイトル対象検索文字列
//     * @param searchDescriptionStr 説明文対象検索文字列
//     * @return 問題Beanリスト
//     */
//    List<QuestionBean> findByTitleLikeOrDescriptionLike(
//            String searchTitleStr, String searchDescriptionStr);   
}
