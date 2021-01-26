package jp.ac.ems.controller.teacher.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import jp.ac.ems.form.teacher.TaskForm;
import jp.ac.ems.service.teacher.TeacherTaskService;

/**
 * 先生用課題基底Contollerクラス（teacher task basic Controller Class）.
 */
public class BaseTeacherTaskController {

    @Autowired
    TeacherTaskService taskService;

    /**
     * モデルにフォームをセットする(set form the model).
     * @return 課題Form(task form)
     */
    @ModelAttribute
    TaskForm setupForm() {
        return new TaskForm();
    }
}
