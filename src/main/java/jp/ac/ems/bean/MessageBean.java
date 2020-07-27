package jp.ac.ems.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import javax.validation.constraints.Max;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * メッセージBean(message Bean).
 * @author tejc999999
 */
@Entity
@Setter
@Getter
@Table(name = "t_message")
public class MessageBean {

    /**
     * メッセージコード(message code).
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * メッセージ(message).
     */
    @Column(name = "message")
    private String message;
    
    /**
     * 返信メッセージ(response message).
     */
    @Column(name = "response_message")
    private String responseMessage;

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
     * 既読フラグ(already read flag).
     */
    @Column(name = "read_flg")
    private Boolean readFlg;
}
