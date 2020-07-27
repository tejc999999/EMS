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
 * クラスBean(class Bean).
 * @author tejc999999
 */
@Entity
@Setter
@Getter
@Table(name = "t_class")
public class ClassBean {

    /**
     * クラスコード(class code).
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * クラス名(class name).
     */
    @Column(name = "name")
    private String name;
    
    /**
     * コンストラクタ（constructor）.
     */
    public ClassBean() {
        userClassBeans = new HashSet<>();
        classCourseBeans = new HashSet<>();
    }

    /**
     * 学生所属クラス：相互参照オブジェクト(student belonging class：cross reference object).
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "class_id")
    private Set<StudentClassBean> userClassBeans;
    
    /**
     * コース所属クラス：相互参照オブジェクト(class belonging course：cross reference object).
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "class_id")
    private Set<ClassCourseBean> classCourseBeans;
    
    /**
     * 学生所属クラス：相互参照オブジェクトを追加する(add an object(class belonging student)).
     * @param userClassBean ユーザ所属クラスBean
     */
    public void addUserClassBean(StudentClassBean userClassBean) {
        userClassBeans.add(userClassBean);
    }

    /**
     * 学生所属クラス：相互参照オブジェクトを削除する(delete an object(class belonging student)).
     */
    public void clearUserClassBean() {
        userClassBeans.clear();
    }
    
    /**
     * 学生IDリストを取得する(get student id list).
     * @return 学生IDリスト(student id list)
     */
    public List<String> getUserIdList() {
        List<String> list = new ArrayList<>();
        userClassBeans.forEach(studentClassBean -> {
            list.add(studentClassBean.getStudentId());
        });
        return list;
    }

    /**
     * コース所属クラス：相互参照オブジェクトを追加する(add an object(course affiliation class)).
     * @param classCourseBean コース所属クラスBean(course affiliation class bean)
     */
    public void addClassCourseBean(ClassCourseBean classCourseBean) {
        classCourseBeans.add(classCourseBean);
    }

    /**
     * コース所属クラス：相互参照オブジェクトを削除する(delete an object(course affiliation class)).
     */
    public void clearClassCourseBean() {
        classCourseBeans.clear();
    }

    /**
     * コースIDリストを取得する(get course id list).
     * @return コースIDリスト(course id list)
     */
    public List<String> getCourseIdList() {
        List<String> list = new ArrayList<>();
        classCourseBeans.forEach(classCourseBean -> {
            list.add(String.valueOf(classCourseBean.getCourseId()));
        });
        return list;
    }
}
