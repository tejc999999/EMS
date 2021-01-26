package jp.ac.ems.controller.teacher.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import jp.ac.ems.form.teacher.CourseForm;
import jp.ac.ems.service.teacher.TeacherCourseService;

/**
 * 先生用コース基底Contollerクラス（base teacher class Controller Class）.
 * @author tejc999999
 */
public class BaseCourseController {

    /**
     * コースサービス.
     */
    @Autowired
    TeacherCourseService courseService;
    
    /**
     * モデルにフォームをセットする(set form the model).
     * @return コースForm(course form)
     */
    @ModelAttribute
    CourseForm setupForm() {
        return new CourseForm();
    }
}
