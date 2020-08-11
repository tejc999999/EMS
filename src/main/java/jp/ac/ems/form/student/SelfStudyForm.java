package jp.ac.ems.form.student;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自習Form(self study form for student).
 * @author tejc999999
 */
@Data
@NoArgsConstructor
public class SelfStudyForm {
	
    /**
     * 選択年度(select year).
     */
    private String selectYear;
    
    /**
     * 選択大分類(select large field).
     */
    private String selectFieldL;
    
    /**
     * 選択中分類(select middle field).
     */
    private String selectFieldM;
    
    /**
     * 選択小分類（select small field).
     */
    private String selectFieldS;
    
    /**
     * 問題リスト
     */
    private List<String> questionList;
    
    private Map<String, String> sortCheckItems;
    
    private Map<String, String> conditionCheckItems;
    
    private String conditionChecked;
    
    private boolean latestFlg;

    private List<String> sortCheckedList;
}
