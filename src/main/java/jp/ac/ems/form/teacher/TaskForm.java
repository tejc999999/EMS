package jp.ac.ems.form.teacher;

import java.util.List;

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
    private String title;

    /**
     * 説明文(description).
     */
    private String description;
    
    /**
     * 問題IDリスト(question id list).
     */
    private List<String> questionCheckedList;
    
    /**
     * 除外年度リスト(exclusion year list).
     */
    private List<String> exclusionYearList;

    /**
     * 除外大分類リスト(exclusion large field list).
     */
    private List<String> exclusionFieldLList;

    /**
     * 除外中分類リスト(exclusion middle field list).
     */
    private List<String> exclusionFieldMList;

    /**
     * 除外小分類リスト(exclusion small field list).
     */
    private List<String> exclusionFieldSList;
}
