package jp.ac.ems.form.student;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 学生用課題問題Form(question task question for student).
 * @author tejc999999
 */
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TaskQuestionForm extends QuestionForm {
    
    /**
     * 課題番号
     */
    private String taskNumber;
}
