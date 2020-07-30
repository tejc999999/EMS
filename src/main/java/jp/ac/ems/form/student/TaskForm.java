package jp.ac.ems.form.student;

import java.util.List;

import javax.validation.constraints.Max;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生用課題Form(question task for student).
 * @author tejc999999
 */
@Data
@NoArgsConstructor
public class TaskForm {

    /**
     * 課題ID(task id).
     */
    private String id;

    /**
     * タイトル(title).
     */
    @Max(50)
    private String title;

    /**
     * 説明文(description).
     */
    @Max(500)
    private String description;
}
