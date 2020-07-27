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
 * 学生、クラス・連関エンティティBean(student/class : Intersection Entity bean).
 * @author tejc999999
 *
 */
@Entity
@Setter
@Getter
@Table(name = "t_student_class")
public class StudentClassBean {
    
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
     * クラスID(class id).
     */
    @Column(name = "class_id")
    private Long classId;
    
}
