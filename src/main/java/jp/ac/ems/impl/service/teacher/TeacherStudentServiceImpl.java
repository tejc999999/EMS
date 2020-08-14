package jp.ac.ems.impl.service.teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jp.ac.ems.bean.UserBean;
import jp.ac.ems.config.RoleCode;
import jp.ac.ems.form.teacher.StudentForm;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.service.teacher.TeacherStudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 先生用学生Serviceクラス（teacher student Service Class）.
 * 
 * @author tejc999999
 *
 */
@Service
public class TeacherStudentServiceImpl implements TeacherStudentService {

    /**
     * ユーザー用リポジトリ(class repository).
     */
    @Autowired
    UserRepository userRepository;

    /**
     * 全ての学生を取得する.
     * @return 全ての学生Formリスト
     */
    @Override
    public List<StudentForm> findAll() {

        List<StudentForm> studentFormList = new ArrayList<StudentForm>();

        for (UserBean userBean : userRepository.findByRoleId(RoleCode.ROLE_STUDENT.getId())) {
            StudentForm userForm = new StudentForm();
            userForm.setId(userBean.getId());
            userForm.setPassword(userBean.getPassword());
            userForm.setName(userBean.getName());
            studentFormList.add(userForm);
        }
        
        return studentFormList;
    }
    
    /**
    * 学生の重複を検証する
    * @param form 学生Form
    * @return 重複有無（true:重複あり、false:重複なし）
    */
    @Override
    public boolean checkDupulicate(StudentForm form) {

    	boolean dupulicateFlg = true;
    	
       Optional<UserBean> opt = userRepository.findById(form.getId());
       if(opt.orElse(null) == null) {
    	   dupulicateFlg = false;
       }
	   
	   return dupulicateFlg;
   }
    
    /**
     * 学生を保存する.
     * @param form 学生Form
     * @return 登録済み学生Form
     */
    @Override
    public StudentForm save(StudentForm form) {

        UserBean saveUserBean;
        String userId = form.getId();
        List<UserBean> userBeanList = new ArrayList<>();
        if (userId != null) {
            Optional<UserBean> opt = userRepository.findById(userId);
            opt.ifPresent(userBean -> {
                userBeanList.add(userBean);
            });
        }
        if (userBeanList.size() > 0) {
            saveUserBean = userBeanList.get(0);
        } else {
            saveUserBean = new UserBean();
        }
        saveUserBean.setId(form.getId());
        if(form.getPasswordNoChangeFlg() == null || !form.getPasswordNoChangeFlg()) {
        	// パスワードを変更する場合のみ、パスワードを設定する
	        // エンコード
	        saveUserBean.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
        }
        saveUserBean.setName(form.getName());
        saveUserBean.setRoleId(form.getRoleId());
        
        saveUserBean = userRepository.save(saveUserBean);
        StudentForm resultForm = new StudentForm();
        resultForm.setId(saveUserBean.getId());
        resultForm.setPassword(saveUserBean.getPassword());
        resultForm.setName(saveUserBean.getName());
        
        return resultForm;
    }
    
    /**
     * 学生を取得する.
     * @param id ユーザーID
     * @return 学生Form
     */
   @Override
    public StudentForm findById(String id) {

        StudentForm studentForm = new StudentForm();

        Optional<UserBean> optUser = userRepository.findById(id);
        optUser.ifPresent(userBean -> {
            studentForm.setId(userBean.getId());
            studentForm.setName(userBean.getName());
        });
        
        return studentForm;
    }
    
    /**
     * 学生を削除する.
     * @param id ユーザーID
     */
   @Override
    public void delete(String id) {
        Optional<UserBean> optUser = userRepository.findById(id);
        optUser.ifPresent(userBean -> userRepository.delete(userBean));
    }
}
