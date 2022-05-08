package jp.ac.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.ems.bean.QuestionBean;

/**
 * 問題用リポジトリ(question repository).
 * @author tejc999999
 */
public interface QuestionRepository extends
                                    JpaRepository<QuestionBean, Long> {

	/**
	 * 年度、期を指定した全問題取得(get all question by specifying the year and term).
	 * @param year 年度(ex).R01)
	 * @param term 期（'A' or 'H')
	 * @return 問題Beanリスト
	 */
	List<QuestionBean> findByYearAndTerm(String year, String term);

	/**
	 * 試験区分、年度、期を指定した全問題取得(get all question by specifying the division and year and term).
	 * @param division 試験区分コード(division Code).
	 * @param year 年度(year (ex).R01).
	 * @param term 期（term 'A' or 'H').
	 * @return 問題Beanリスト(question Bean list).
	 */
	List<QuestionBean> findByDivisionAndYearAndTerm(String division, String year, String term);

	/**
	 * 大分類を指定した全問取得(get all question by specifying the large field).
	 * @param field_l_id 大分類(large field).
	 * @return 問題Beanリスト(question Bean list).
	 */
	List<QuestionBean> findByFieldLId(Byte field_l_id);

	/**
	 * 試験区分、大分類を指定した全問取得(get all question by specifying the division and large field).
	 * @param division 試験区分コード(division Code).
	 * @param field_l_id 大分類(large field).
	 * @return 問題Beanリスト(question Bean list).
	 */
	List<QuestionBean> findByDivisionAndFieldLId(String division, Byte field_l_id);

	/**
	 * 中分類を指定した全問取得(get all question by specifying the middle field).
	 * @param field_m_id 中分類(middle field)
	 * @return 問題Beanリスト(question Bean list).
	 */
	List<QuestionBean> findByFieldMId(Byte field_m_id);

	/**
	 * 試験区分、中分類を指定した全問取得(get all question by specifying the division and middle field).
	 * @param division 試験区分コード(division Code).
	 * @param field_m_id 中分類(middle field)
	 * @return 問題Beanリスト(question Bean list).
	 */
	List<QuestionBean> findByDivisionAndFieldMId(String division, Byte field_m_id);

	/**
	 * 小分類を指定した全問取得(get all question by specifying the small field).
	 * @param field_s_id 小分類(small field)
	 * @return 問題Beanリスト(question Bean list).
	 */
	List<QuestionBean> findByFieldSId(Byte field_s_id);

	/**
	 * 試験区分、小分類を指定した全問取得(get all question by specifying the division and small field).
	 * @param division 試験区分コード(division Code).
	 * @param field_s_id 小分類(small field)
	 * @return 問題Beanリスト(question Bean list).
	 */
	List<QuestionBean> findByDivisionAndFieldSId(String division, Byte field_s_id);

	/**
	 * 年度、期、大分類を指定した全問取得(get all question by specifying the year and term and large field).
	 * @param year 年度(year (ex).R01)
	 * @param term 期（term 'A' or 'H')
	 * @param field_l_id 大分類(large field)
	 * @return 問題Beanリスト(question Bean list).
	 */
	List<QuestionBean> findByYearAndTermAndFieldLId(String year, String term, Byte field_l_id);

	/**
	 * 試験区分、年度、期、大分類を指定した全問取得(get all question by specifying the division and year and term and large field).
	 * @param division 試験区分コード(division Code).
	 * @param year 年度(year (ex).R01)
	 * @param term 期（term 'A' or 'H')
	 * @param field_l_id 大分類(large field)
	 * @return 問題Beanリスト(question Bean list).
	 */
	List<QuestionBean> findByDivisionAndYearAndTermAndFieldLId(String division, String year, String term, Byte field_l_id);
	
	/**
	 * 年度、期、中分類を指定した全問取得(get all question by specifying the year and term and middle field).
	 * @param year 年度(year (ex).R01)
	 * @param term 期（term 'A' or 'H')
	 * @param field_m_id 中分類(middle field)
	 * @return 問題Beanリスト(question Bean list).
	 */
	List<QuestionBean> findByYearAndTermAndFieldMId(String year, String term, Byte field_m_id);

	/**
	 * 試験区分、年度、期、中分類を指定した全問取得(get all question by specifying the division and year and term and middle field).
	 * @param division 試験区分コード(division Code).
	 * @param year 年度(year (ex).R01)
	 * @param term 期（term 'A' or 'H')
	 * @param field_m_id 中分類(middle field)
	 * @return 問題Beanリスト(question Bean list).
	 */
	List<QuestionBean> findByDivisionAndYearAndTermAndFieldMId(String division, String year, String term, Byte field_m_id);

	/**
	 * 年度、期、小分類を指定した全問取得(get all question by specifying the year and term and small field).
	 * @param year 年度(year (ex).R01)
	 * @param term 期（term 'A' or 'H')
	 * @param field_s_id 小分類(small field)
	 * @return 問題Beanリスト(question Bean list).
	 */
	List<QuestionBean> findByYearAndTermAndFieldSId(String year, String term, Byte field_s_id);

	/**
	 * 試験区分、年度、期、小分類を指定した全問取得(get all question by specifying the division and year and term and small field).
	 * @param division 試験区分コード(division Code).
	 * @param year 年度(year (ex).R01)
	 * @param term 期（term 'A' or 'H')
	 * @param field_s_id 小分類(small field)
	 * @return 問題Beanリスト(question Bean list).
	 */
	List<QuestionBean> findByDivisionAndYearAndTermAndFieldSId(String division, String year, String term, Byte field_s_id);

	/**
	 * 試験区分を指定した全問取得(get all question by specifying the division).
	 * @param division 試験区分コード(division Code).
	 * @return 問題Beanリスト(question Bean list).
	 */
	List<QuestionBean> findByDivision(String division);
	
	  /**
	  * 年度、期の重複無し全問取得.
	  * 
	 * @return 問題Beanリスト(question Bean list).
	  */
//	 List<QuestionBean> findDistinctYearAndTerm();
	 @Query("select distinct new jp.ac.ems.bean.QuestionBean(q.year, q.term) from QuestionBean q")
	 List<QuestionBean> findDistinctYearAndTerm();

	  /**
	  * 試験区分を指定した年度、期の重複無し全問取得.
	  * 
	 * @return 問題Beanリスト(question Bean list).
	  */
	 @Query("select distinct new jp.ac.ems.bean.QuestionBean(q.year, q.term) from QuestionBean q where division = :divisionCode")
	 List<QuestionBean> findDistinctYearAndTermByDivision(@Param("divisionCode") String division);

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
