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
 * ユーザー、コース、課題履歴Bean(user/course/task hisotory bean).
 * @author tejc999999
 *
 */
@Setter
@Getter
@Entity
@Table(name = "t_user_course_task_history")
public class UserCourseTaskHistoryBean {

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
     * コースID(course id).
     */
    @Column(name = "course_id")
    private Long courseId;

    /**
     * 課題ID(task id).
     */
    @Column(name = "task_id")
    private Long taskId;

    /**
     * 問題ID(question id).
     */
    @Column(name = "question_id")
    private Long questionId;

    /**
     * 回答(answer).
     * 1-4(ア~エ）
     */
    @Column(name = "answer")
    private Long answer;
    
    /**
     * 正解(correct).
     * 1-4(ア~エ）
     */
    @Column(name = "correct")
    private Long correct;
    
    /**
     * 更新日時(update date time).
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date updateDate;
}
