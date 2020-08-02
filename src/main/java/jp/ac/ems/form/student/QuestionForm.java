package jp.ac.ems.form.student;

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
    private String id;

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
    private String number;

    
    /**
     * 大分類(large field).
     * 
     */
    private String fieldLId;
    
    /**
     * 中分類(middle field).
     * 
     */
    private String fieldMId;
    
    
    /**
     * 小分類(small field).
     * 
     */
    private String fieldSId;
    
    /**
     * 正解(answer correct).
     * 1-4:ア~エ
     */
    private String correct;
    
    /**
     * 回答(answer).
     * 1-4:ア~エ
     */
    private String answer;
    
    /**
     * 問題画像ファイルパス
     */
    private String imagePath;
}
