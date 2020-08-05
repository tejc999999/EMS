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
     * 回答済みフラグ(answered flag).
     */
    private boolean answeredFlg;
    
    /**
     * 問題情報文字列
     */
    private String questionInfoStr;
    
    /**
     * 問題分野情報文字列
     */
    private String questionFieldInfoStr;
    
    /**
     * 問題数
     */
    private String questionSize;
    
    /**
     * 現在の問題順番
     */
    private String questionCnt;
    
    /**
     * 回答済問題数
     */
    private String answeredQuestionCnt;
    
    /**
     * 問題Form(question Form).
     */
    // 1問ずつ表示するためコレクションを使用しない
    private QuestionForm questionForm;
}
