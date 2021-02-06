package jp.ac.ems.form.teacher;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 先生用ランダム課題Form(question random task for teacher).
 * @author tejc999999
 */
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TaskRandomForm extends TaskForm {

    /**
     * 選択分類(select field).
     */
    private String fieldChecked = "0";
    
    /**
     * 出題問題数
     */
    private String totalNumber;
    
    /**
     * 最新フラグ
     */
    private boolean latestFlg;

}
