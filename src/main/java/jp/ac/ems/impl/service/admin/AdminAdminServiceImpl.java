package jp.ac.ems.impl.service.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jp.ac.ems.bean.UserBean;
import jp.ac.ems.config.RoleCode;
import jp.ac.ems.form.admin.AdminForm;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.service.admin.AdminAdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 管理者用管理者Serviceクラス（admin admin Service Class）.
 * 
 * @author tejc999999
 *
 */
@Service
public class AdminAdminServiceImpl implements AdminAdminService {

    /**
     * ユーザー用リポジトリ(class repository).
     */
    @Autowired
    UserRepository userRepository;

    /**
     * 全ての管理者を取得する.
     * @return 全ての管理者Formリスト
     */
    @Override
    public List<AdminForm> findAll() {

        List<AdminForm> adminFormList = new ArrayList<>();

        for (UserBean userBean : userRepository.findByRoleId(RoleCode.ROLE_ADMIN.getId())) {
            AdminForm adminForm = new AdminForm();
            adminForm.setId(userBean.getId());
            adminForm.setPassword(userBean.getPassword());
            adminForm.setName(userBean.getName());
            adminFormList.add(adminForm);
        }
        
        return adminFormList;
    }
    
    /**
    * 重複を検証する
    * @param form 管理者Form
    * @return 重複有無（true:重複あり、false:重複なし）
    */
    @Override
    public boolean checkDupulicate(AdminForm form) {

    	boolean dupulicateFlg = true;
    	
       Optional<UserBean> opt = userRepository.findById(form.getId());
       if(opt.orElse(null) == null) {
    	   dupulicateFlg = false;
       }
	   
	   return dupulicateFlg;
   }
    
    /**
     * 管理者を保存する.
     * @param form 管理者Form
     * @return 登録済み管理者Form
     */
    @Override
    public AdminForm save(AdminForm form) {

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
        AdminForm resultForm = new AdminForm();
        resultForm.setId(saveUserBean.getId());
        resultForm.setPassword(saveUserBean.getPassword());
        resultForm.setName(saveUserBean.getName());
        
        return resultForm;
    }
    
    /**
     * 管理者を取得する.
     * @param id ユーザーID
     * @return 管理者Form
     */
   @Override
    public AdminForm findById(String id) {

        AdminForm adminForm = new AdminForm();

        Optional<UserBean> optUser = userRepository.findById(id);
        optUser.ifPresent(userBean -> {
            adminForm.setId(userBean.getId());
            adminForm.setName(userBean.getName());
        });
        
        return adminForm;
    }
    
    /**
     * 管理者を削除する.
     * @param id ユーザーID
     */
   @Override
    public void delete(String id) {
        Optional<UserBean> optUser = userRepository.findById(id);
        optUser.ifPresent(userBean -> userRepository.delete(userBean));
    }
   
   /**
    * 自分自身のIDかを検証する.
    * @param id ユーザーID
    * @return 検証結果(true:異なる, false:同一)
    */
   @Override
   public boolean selfCheck(String id) {
	   
	   boolean checkFlg = false;
	   
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       String userId = auth.getName();
       
       if(id == null || !id.equals(userId)) {
    	   
    	   checkFlg = true;
       }

       return checkFlg;
   }
}
