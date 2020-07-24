package jp.ac.ems.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

/**
 * ユーザー、問題履歴Bean(user/question hisotory bean).
 * @author tejc999999
 *
 */
@Setter
@Getter
@Entity
@Table(name = "t_user_question_history")
public class UserQuestionHistoryBean {

    /**
     * 履歴コード(history code).
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ユーザーID(user id).
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 問題ID(question id).
     */
    @Column(name = "question_id")
    private Long questionId;

    /**
     * 正解回数(correct count).
     */
    @Column(name = "correct_cnt")
    private Long correctCnt;

    /**
     * 不正解回数(incorrect count).
     */
    @Column(name = "incorrect_cnt")
    private Long incorrectCnt;
    
    /**
     * 更新日時(update date time).
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date updateDate;
}
