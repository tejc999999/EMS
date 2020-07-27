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
 * 提示課題、問題・連関エンティティBean(present task, question : Intersection Entity bean).
 * @author tejc999999
 *
 */
@Entity
@Setter
@Getter
@Table(name = "t_present_task_question")
public class PresentTaskQuestionBean {

    /**
     * サロゲートキー(surrogate key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
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
     * 順番(sequence number).
     */
    @Column(name = "sequence_number")
    private Short sequenceNumber;
}
