package jp.ac.ems.form.student;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 学生用自習問題Form(question self study question for student).
 * @author tejc999999
 */
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class SelfStudyQuestionForm extends QuestionForm {
    
    /**
     * 自習問題番号
     */
    private int selectQuestionNumber;
    
    /**
     * 自習問題IDリスト
     */
    private List<String> questionList;
}
