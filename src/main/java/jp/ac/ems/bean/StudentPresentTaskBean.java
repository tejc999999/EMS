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
 * 学生、提示課題・連関エンティティ兼コードBean(student/present task : Intersection Entity bean).
 * @author tejc999999
 *
 */
@Setter
@Getter
@Entity
@Table(name = "t_student_present_task")
public class StudentPresentTaskBean {

    /**
     * サロゲートキー(surrogate key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 学生ID(student id).
     */
    @Column(name = "student_id")
    private String StudentId;

    /**
     * 提示課題ID(present task id).
     */
    @Column(name = "present_task_id")
    private Long presentTaskId;

    /**
     * 回答フラグ(amswer flg).
     * 0:未回答(0:unanswerd)
     * 1:回答済(1:answered)
     */
    @Column(name = "answer_flg")
    private boolean answerFlg;
    
    /**
     * 更新日時(update date time).
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date updateDate;
}
