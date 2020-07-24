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
@Table(name = "t_question")
public class QuestionBean {

    /**
     * 問題コード(question code).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * タイトル(title).
     */
    @Column(name = "division")
    private String division;

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
    private Long number;

    /**
     * 正解(answer).
     */
    @Column(name = "correct")
    private Long correct;

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
