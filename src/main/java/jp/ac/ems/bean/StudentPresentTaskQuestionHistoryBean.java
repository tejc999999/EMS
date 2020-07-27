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
 * 学生、提示課題、問題履歴Bean(student/present task/question hisotory bean).
 * @author tejc999999
 *
 */
@Setter
@Getter
@Entity
@Table(name = "t_student_present_task_question_history")
public class StudentPresentTaskQuestionHistoryBean {

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
    @Column(name = "student_id")
    private String studentId;

    /**
     * 提示課題ID(present task id).
     */
    @Column(name = "present_task_id")
    private Long presentTaskId;

    /**
     * 問題ID(question id).
     */
    @Column(name = "question_id")
    private Long questionId;

    /**
     * 回答(answer).
     * 1~4(ア~エ)
     */
    @Column(name = "answer")
    private Byte answer;

    /**
     * 正解(correct).
     * 1~4(ア~エ)
     */
    @Column(name = "correct")
    private Byte correct;
    
    /**
     * 更新日時(update date time).
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date updateDate;
}
