package jp.ac.ems.bean;

import java.util.HashSet;
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
 * 課題Bean(task Bean).
 * @author tejc999999
 */
@Entity
@Setter
@Getter
@Table(name = "t_task")
public class TaskBean {

    /**
     * 課題コード(task code).
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 課題タイトル(task title).
     */
    @Column(name = "title")
    private String title;

    /**
     * 課題説明文(task description).
     */
    @Column(name = "description")
    private String description;
    
    /**
     * コンストラクタ(constructor).
     */
    public TaskBean() {
        taskQuestionBeans = new HashSet<>();
        studentTaskBeans = new HashSet<>();
    }

    /**
     * 課題・問題：相互参照オブジェクト(task・question：cross reference object).
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private Set<TaskQuestionBean> taskQuestionBeans;
    
    /**
     * 学生・課題：相互参照オブジェクト(user・task：cross reference object).
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private Set<StudentTaskBean> studentTaskBeans;

    /**
     * 課題・問題情報を追加する(add an info(task/question)).
     * @param userCourseBean 課題・問題Bean(task/question bean)
     */
    public void addTaskQuestionBean(TaskQuestionBean taskQuestionBean) {
        taskQuestionBeans.add(taskQuestionBean);
    }
    
    /**
     * 課題・問題情報クリア(clear info(task/question)).
     */
    public void clearTaskQuestionBean() {
    	taskQuestionBeans.clear();
    }
    
    /**
     *　学生・課題情報を追加する(add an info(task/question)).
     * @param userCourseBean 課題・問題Bean(task/question bean)
     */
    public void addStudentTaskBean(StudentTaskBean studentTaskBean) {
        studentTaskBeans.add(studentTaskBean);
    }
    
    /**
     * 学生・課題情報クリア(clear info(student/task)).
     */
    public void clearStudentTaskBeans() {
    	studentTaskBeans.clear();
    }
}
