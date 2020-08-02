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
 * 学生、課題履歴Bean(student/task history bean).
 * @author tejc999999
 *
 */
@Setter
@Getter
@Entity
@Table(name = "t_student_task_history")
public class StudentTaskHistoryBean {

    /**
     * 履歴コード(history code).
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 学生ID(student id).
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 問題ID(question id).
     */
    @Column(name = "question_id")
    private Long questionId;

    /**
     * 正解数(correct count).
     */
    @Column(name = "correct_cnt")
    private Short correctCnt;
    
    /**
     * 不正解数(incorrect count).
     */
    @Column(name = "incorrect_cnt")
    private Short incorrectCnt;
    
    /**
     * 更新日時(update date time).
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date updateDate;
}
