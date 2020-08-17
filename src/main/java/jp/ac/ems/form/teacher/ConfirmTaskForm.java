package jp.ac.ems.form.teacher;

import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 先生用課題確認用課題Form.
 * @author tejc999999
 */
@Data
@NoArgsConstructor
public class ConfirmTaskForm {

    /**
     * 課題ID(task id).
     */
    private String id;

    /**
     * タイトル(title).
     */
    @Size(max=50)
    private String title;

    /**
     * 説明文(description).
     */
    @Size(max=500)
    private String description;

    /**
     * 問題数
     */
    private String questionSize;
    
    /**
     * 問題Form(question Form).
     */
    // 1問ずつ表示するためコレクションを使用しない
    private ConfirmQuestionForm questionForm;
}
