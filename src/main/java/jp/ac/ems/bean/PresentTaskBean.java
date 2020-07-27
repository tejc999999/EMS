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
 * 提示課題Bean(present task bean).
 * @author tejc999999
 *
 */
@Entity
@Setter
@Getter
@Table(name = "t_present_task")
public class PresentTaskBean {

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
    private String studentId;
    
    /**
     * タイトル(title).
     */
    @Column(name = "title")
    private String title;

    /**
     * 説明(description).
     */
    @Column(name = "description")
    private String description;
}
