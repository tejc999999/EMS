package jp.ac.ems.controller.student.selfstudy;

import org.springframework.beans.factory.annotation.Autowired;

import jp.ac.ems.service.student.StudentSelfStudyService;

/**
 * 学生用自習基底Contollerクラス（student base self study Controller Class）.
 * @author tejc999999
 */
public class BaseSelfStudyController {
	/**
	 * 自習サービス
	 */
	@Autowired
	StudentSelfStudyService studentSelfStudyService;
	    
}
