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
 * 学生、問題タグBean(student/question tag Bean).
 * @author tejc999999
 */
@Entity
@Setter
@Getter
@Table(name = "t_student_question_tag")
public class StudentQuestionTagBean {

    /**
     * ID(id).
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
     * 問題ID(question id).
     */
    @Column(name = "tag_id")
    private Long tagId;
}
