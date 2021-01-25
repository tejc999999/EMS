package jp.ac.ems.service.student;

import jp.ac.ems.form.student.TopForm;

public interface StudentTopService {

    /**
     * ドロップダウン項目設定(Set dropdown param).
     * @param form 自習Form(self study form)
     * @param model モデル(model)
     */
    public TopForm getTopForm();
}
