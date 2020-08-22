package jp.ac.ems.service.student;

import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;

import jp.ac.ems.form.student.SelfStudyForm;
import jp.ac.ems.form.student.SelfStudyQuestionForm;
import jp.ac.ems.form.student.TopForm;

public interface StudentTopService {

    /**
     * ドロップダウン項目設定(Set dropdown param).
     * @param form 自習Form(self study form)
     * @param model モデル(model)
     */
    public TopForm getTopForm();
}
