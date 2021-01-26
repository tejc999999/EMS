package jp.ac.ems.controller.student.task;

import org.springframework.beans.factory.annotation.Autowired;

import jp.ac.ems.service.student.StudentTaskService;

/**
 * 学生用課題基底Contollerクラス（student base task Controller Class）.
 * @author tejc999999
 */
public class BaseStudentTaskController {
	/**
	 * 課題サービス
	 */
    @Autowired
    StudentTaskService taskService;
}
