package jp.ac.ems.form.teacher;

import java.util.List;

import javax.validation.constraints.Max;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 先生用提示課題Form(present question task for teacher).
 * @author tejc999999
 */
@Data
@NoArgsConstructor
public class PresentTaskForm {

    /**
     * 課題ID(task id).
     */
    private String id;

    /**
     * 先生ID(teacher id).
     */
    private String teacherId;

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
