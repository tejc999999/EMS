package jp.ac.ems.impl.service.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jp.ac.ems.bean.UserBean;
import jp.ac.ems.config.RoleCode;
import jp.ac.ems.form.admin.TeacherForm;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.service.admin.AdminTeacherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 管理者用先生Serviceクラス（admin teacher Service Class）.
 * 
 * @author tejc999999
 *
 */
@Service
public class AdminTeacherServiceImpl implements AdminTeacherService {

    /**
     * ユーザー用リポジトリ(class repository).
     */
    @Autowired
    UserRepository userRepository;

    /**
     * 全ての先生を取得する.
     * @return 全ての先生Formリスト
     */
    @Override
    public List<TeacherForm> findAll() {

        List<TeacherForm> teacherFormList = new ArrayList<>();

        for (UserBean userBean : userRepository.findByRoleId(RoleCode.ROLE_TEACHER.getId())) {
            TeacherForm teacherForm = new TeacherForm();
            teacherForm.setId(userBean.getId());
            teacherForm.setPassword(userBean.getPassword());
            teacherForm.setName(userBean.getName());
            teacherFormList.add(teacherForm);
        }
        
        return teacherFormList;
    }
    
    /**
    *重複を検証する
    * @param form 先生Form
    * @return 重複有無（true:重複あり、false:重複なし）
    */
    @Override
    public boolean checkDupulicate(TeacherForm form) {

    	boolean dupulicateFlg = true;
    	
       Optional<UserBean> opt = userRepository.findById(form.getId());
       if(opt.orElse(null) == null) {
    	   dupulicateFlg = false;
       }
	   
	   return dupulicateFlg;
   }
    
    /**
     * 先生を保存する.
     * @param form 学生Form
     * @return 登録済み先生Form
     */
    @Override
    public TeacherForm save(TeacherForm form) {

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
        TeacherForm resultForm = new TeacherForm();
        resultForm.setId(saveUserBean.getId());
        resultForm.setPassword(saveUserBean.getPassword());
        resultForm.setName(saveUserBean.getName());
        
        return resultForm;
    }
    
    /**
     * 先生を取得する.
     * @param id ユーザーID
     * @return 学生Form
     */
   @Override
    public TeacherForm findById(String id) {

        TeacherForm teacherForm = new TeacherForm();

        Optional<UserBean> optUser = userRepository.findById(id);
        optUser.ifPresent(userBean -> {
            teacherForm.setId(userBean.getId());
            teacherForm.setName(userBean.getName());
        });
        
        return teacherForm;
    }
    
    /**
     * 先生を削除する.
     * @param id ユーザーID
     */
   @Override
    public void delete(String id) {
        Optional<UserBean> optUser = userRepository.findById(id);
        optUser.ifPresent(userBean -> userRepository.delete(userBean));
    }
}
