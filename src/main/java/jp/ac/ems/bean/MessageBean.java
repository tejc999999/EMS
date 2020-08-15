package jp.ac.ems.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
    @Column(name = "user_id")
    private String userId;

    /**
     * 提示課題ID(present task id).
     */
    @Column(name = "task_id")
    private Long taskId;


    /**
     * 既読フラグ(already read flag).
     */
    @Column(name = "read_flg")
    private Boolean readFlg;
}
