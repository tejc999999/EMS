package jp.ac.ems.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * ユーザーBean(user Bean).
 * @author tejc999999
 */
@Setter
@Getter
@Entity
@Table(name = "t_user")
public class UserBean {

    /**
     * ユーザーID(user id).
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * パスワード(password).
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * ユーザー名(user name).
     */
    @Column(name = "name")
    private String name;

    /**
     * 権限ID(role id).
     */
    @Column(name = "role_id", nullable = false)
    private Byte roleId;

    /**
     * 学生所属クラス：相互参照オブジェクト(user belonging class：cross reference object).
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Set<StudentClassBean> studentClassBeans;

    /**
     * 学生所属コース：相互参照オブジェクト(user belonging course：cross reference object).
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Set<StudentCourseBean> studentCourseBeans;
    
    /**
     * 学生・コース・課題コードBean：相互参照オブジェクト(user・course・task：cross reference object).
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Set<StudentTaskBean> studentTaskBeans;

    /**
     * 学生・問題履歴Bean：相互参照オブジェクト(user・question history：cross reference object).
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Set<StudentQuestionHistoryBean> studentQuestionHistoryBeans;

    /**
     * コンストラクタ(constructor).
     */
    public UserBean() {
        studentClassBeans = new HashSet<>();
        studentCourseBeans = new HashSet<>();
        studentTaskBeans = new HashSet<>();
        studentQuestionHistoryBeans = new HashSet<>();
    }
    
    /**
     * クラスIDリストを取得する(get class id list).
     * @return クラスIDリスト(class id list)
     */
    public List<String> getClassIdList() {
        List<String> list = new ArrayList<>();
        studentClassBeans.forEach(studentClassBean -> {
            list.add(String.valueOf(studentClassBean.getClassId()));
        });
        return list;
    }
    
    /**
     * コースIDリストを取得する(get course id list).
     * @return コースIDリスト(course id list)
     */
    public List<String> getCourseIdList() {
        List<String> list = new ArrayList<>();
        studentCourseBeans.forEach(studentCourseBean -> {
            list.add(String.valueOf(studentCourseBean.getCourseId()));
        });
        return list;
    }

    /**
     * 課題IDリストを取得する(get task id list).
     * @return 課題IDリスト(task id list)
     */
    public List<String> getTaskIdList() {
        List<String> list = new ArrayList<>();
        studentTaskBeans.forEach(studentTaskBean -> {
            list.add(String.valueOf(studentTaskBean.getTaskId()));
        });
        return list;
    }

}
