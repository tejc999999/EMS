package jp.ac.ems.form.student;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生用問題Form(question question for student).
 * @author tejc999999
 */
@Data
@NoArgsConstructor
public class QuestionForm {

    /**
     * 問題コード(question code).
     */
    private Long id;

    /**
     * 試験区分(division).
     */
    private String division;

    /**
     * 期(term).
     * A or H
     */
    private String term;

    /**
     * 年度(year).
     * ex)R01
     */
    private String year;

    /**
     * 問番(question number).
     */
    private Byte number;

    
    /**
     * 大分類(large field).
     * 
     */
    private Byte fieldLId;
    
    /**
     * 中分類(middle field).
     * 
     */
    private Byte fieldMId;
    
    
    /**
     * 小分類(small field).
     * 
     */
    private Byte fieldSId;
    
    /**
     * 正解(answer).
     * 1-4:ア~エ
     */
    private Byte correct;
    
    /**
     * 課題ID(task id).
     */
    private String taskId;
    
    /**
     * 問題画像ファイルパス
     */
    private String imagePath;
}
