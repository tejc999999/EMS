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
    
    /**
     * 完了フラグ（true:完了、false:未完了）
     */
    private boolean completeFlag;
    
    /**
     * 問題Form(question Form).
     */
    // 1問ずつ表示するためコレクションを使用しない
    private QuestionForm questionForm;
}
