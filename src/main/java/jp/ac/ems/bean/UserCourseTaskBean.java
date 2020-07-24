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
 * ユーザー、課題・連関エンティティ兼コードBean(user/task : Intersection Entity bean).
 * @author tejc999999
 *
 */
@Setter
@Getter
@Entity
@Table(name = "t_user_task")
public class UserCourseTaskBean {

    /**
     * サロゲートキー(surrogate key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * ユーザーID(user id).
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 課題ID(task id).
     */
    @Column(name = "task_id")
    private Long taskId;

    /**
     * コースID(course id).
     */
    @Column(name = "course_id")
    private Long courseId;

    /**
     * 回答フラグ(amswer flg).
     * 0:未回答(0:unanswerd)
     * 1:回答済(1:answered)
     */
    @Column(name = "answer_flg")
    private boolean answerFlg;
}
