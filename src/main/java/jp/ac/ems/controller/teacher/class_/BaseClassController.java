package jp.ac.ems.controller.teacher.class_;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import jp.ac.ems.form.teacher.ClassForm;
import jp.ac.ems.service.teacher.TeacherClassService;

/**
 * 先生用クラスContoller基底クラス（teacher class Controller Base Class）.
 * @author tejc999999
 */
public class BaseClassController {

    /**
     * クラス用サービス(class service).
     */
    @Autowired
    TeacherClassService classService;
    
    /**
     * モデルにフォームをセット(set form the model).
     * @return クラスForm(class form)
     */
    @ModelAttribute
    ClassForm setupForm() {
        return new ClassForm();
    }
}
