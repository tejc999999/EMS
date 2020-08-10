package jp.ac.ems.service.student;

import java.util.List;

import jp.ac.ems.form.student.GradeForm;

/**
 * 学生用成績Serviceクラス（student grade Service Class）.
 * @author tejc999999
 */
public interface GradeService {
	
    /**
     * 全ての学生の成績を取得する(get grade for all student).
     * @return 全ての成績Form(all grade form)
     */
    public GradeForm getAllGrade();

}
