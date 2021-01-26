package jp.ac.ems.controller.teacher.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import jp.ac.ems.form.teacher.StudentForm;
import jp.ac.ems.service.teacher.TeacherStudentService;

/**
 * 先生用学生基底Contollerクラス（teacher base student Controller Class）.
 * @author tejc999999
 */
public class BaseStudentController {

    /**
     * 学生サービス(student service).
     */
    @Autowired
    TeacherStudentService studentService;

    /**
     * モデルにフォームをセットする(set form the model).
     * @return 学生Form(student form)
     */
    @ModelAttribute
    StudentForm setupForm() {
        return new StudentForm();
    }
}
