package jp.ac.ems.bean;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 問題Bean(question Bean).
 * 
 * @author tejc999999
 *
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@Table(name = "t_question")
public class QuestionBean {

	/**
	 * 年度抽出用コンストラクタ
	 * 
	 * @param year 年度
	 * @param term 期
	 */
	public QuestionBean(String year, String term) {
		this.year = year;
		this.term = term;
	}
	
    /**
     * 問題コード(question code).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 試験区分(division).
     */
    @Column(name = "division")
    private String division;

    /**
     * 期(term).
     * A or H
     */
    @Column(name = "term")
    private String term;

    /**
     * 年度(year).
     * ex)R01
     */
    @Column(name = "year")
    private String year;

    /**
     * 問番(question number).
     */
    @Column(name = "number")
    private Byte number;

    
    /**
     * 大分類(large field).
     * 
     */
    @Column(name = "field_l_id")
    private Byte fieldLId;
    
    /**
     * 中分類(middle field).
     * 
     */
    @Column(name = "field_m_id")
    private Byte fieldMId;
    
    
    /**
     * 小分類(small field).
     * 
     */
    @Column(name = "field_s_id")
    private Byte fieldSId;
    
    
    /**
     * 正解(answer).
     * 1-4:ア~エ
     */
    @Column(name = "correct")
    private Byte correct;

    /**
     * コンストラクタ(constructor).
     */
    public QuestionBean() {
        taskQuestionBeans = new HashSet<>();
    }

    /**
     * 課題・問題：相互参照オブジェクト(task・question：cross reference object).
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    private Set<TaskQuestionBean> taskQuestionBeans;
}
