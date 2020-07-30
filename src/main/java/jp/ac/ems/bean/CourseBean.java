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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * コースBean(course Bean).
 * @author tejc999999
 */
@Entity
@Setter
@Getter
@Table(name = "t_course")
public class CourseBean {

    /**
     * コースコード(course code).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * コース名(course name).
     */
    @Column(name = "name")
    private String name;
    
    /**
     * コンストラクタ(constructor).
     */
    public CourseBean() {
        studentCourseBeans = new HashSet<>();
        classCourseBeans = new HashSet<>();
    }

    /**
     * 学生所属コース：相互参照オブジェクト(student belonging course：cross reference object).
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    Set<StudentCourseBean> studentCourseBeans;
    
    /**
     * クラス所属コース：相互参照オブジェクト(class belonging course：cross reference object).
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    Set<ClassCourseBean> classCourseBeans;

    /**
     * 学生・コース情報クリア(clear info(student/course)).
     */
    public void clearStudentCourseBean() {
        studentCourseBeans.clear();
    }
    
    /**
     * クラス・コース情報クリア(clear info(class/course)).
     */
    public void clearClassCourseBean() {
        classCourseBeans.clear();
    }

    /**
     * ユーザー・コース情報を追加する(add an info(user/course)).
     * @param userCourseBean ユーザー・コースBean(user/course bean)
     */
    public void addUserCourseBean(StudentCourseBean userCourseBean) {
        studentCourseBeans.add(userCourseBean);
    }
    
    /**
     * クラス・コース情報を追加する(add an info(class/course)).
     * @param classCourseBean クラス・コースBean(class/course bean)
     */
    public void addClassCourseBean(ClassCourseBean classCourseBean) {
        classCourseBeans.add(classCourseBean);
    }
    
    /**
     * コースに紐づく学生IDリストを取得する(get the student id list linked to the course).
     * ※コースに紐づくクラスに所属する学生を除く
     * @return 学生IDリスト(student id list)
     */
    public List<String> getPartStudentIdList() {
        List<String> list = new ArrayList<>();
        studentCourseBeans.forEach(studentCourseBean -> {
            list.add(String.valueOf(studentCourseBean.getUserId()));
        });
        return list;
    }

    /**
     * コースに紐づくクラスIDリストを取得する(get the class id list linked to the course).
     * @return クラスIDリスト(class id list)
     */
    public List<String> getClassIdList() {
        List<String> list = new ArrayList<>();
        classCourseBeans.forEach(classCourseBean -> {
            list.add(String.valueOf(classCourseBean.getClassId()));
        });
        return list;
    }
}
