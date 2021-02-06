package jp.ac.ems.form.teacher;

import java.util.List;

import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 先生用課題Form(question task for teacher).
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
    @Size(max=50)
    private String title = "";

    /**
     * 説明文(description).
     */
    @Size(max=500)
    private String description = "";
    
    /**
     * 問題IDリスト(question id list).
     */
    private List<String> questionCheckedList;
    
    /**
     * 対象コースIDリスト(target course list).
     */
    private List<String> courseCheckedList;

    /**
     * 対象クラスIDリスト(target class list).
     */
    private List<String> classCheckedList;
    
    /**
     * 対象ユーザIDリスト(target user list).
     */
    private List<String> userCheckedList;
}
