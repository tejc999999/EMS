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
 * 学生、コース・連関エンティティBean(student/course : Intersection Entity bean).
 * @author tejc999999
 *
 */
@Setter
@Getter
@Entity
@Table(name = "t_student_course")
public class StudentCourseBean {

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
     * コースID(course id).
     */
    @Column(name = "course_id")
    private Long courseId;
}
