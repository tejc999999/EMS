package jp.ac.ems.service.teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jp.ac.ems.bean.UserBean;
import jp.ac.ems.config.RoleCode;
import jp.ac.ems.form.teacher.StudentForm;
import jp.ac.ems.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 先生用学生Serviceクラス（teacher student Service Class）.
 * 
 * @author tejc999999
 *
 */
public interface TeacherStudentService {

    /**
     * 全ての学生を取得する.
     * @return 全ての学生Formリスト
     */
    public List<StudentForm> findAll();

    
    /**
     * 学生を保存する.
     * @param form 学生Form
     * @return 登録済み学生Form
     */
    public StudentForm save(StudentForm form);

    
    /**
     * 学生を取得する.
     * @param id ユーザーID
     * @return 学生Form
     */
    public StudentForm findById(String id);

    
    /**
     * 学生を削除する.
     * @param id ユーザーID
     */
    public void delete(String id);
}
