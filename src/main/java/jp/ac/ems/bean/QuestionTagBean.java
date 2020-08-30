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
 * 学生問題タグBean(student question tag Bean).
 * @author tejc999999
 */
@Entity
@Setter
@Getter
@Table(name = "t_question_tag")
public class QuestionTagBean {

    /**
     * タグID(tag id).
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * タグ名(tag name).
     */
    @Column(name = "name")
    private String name;
}
