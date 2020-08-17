package jp.ac.ems.impl.service.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jp.ac.ems.bean.ClassBean;
import jp.ac.ems.bean.UserBean;
import jp.ac.ems.bean.StudentClassBean;
import jp.ac.ems.config.RoleCode;
import jp.ac.ems.form.teacher.ClassForm;
import jp.ac.ems.repository.ClassRepository;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.service.teacher.TeacherClassService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 先生用クラスServiceクラス（teacher class Service Class）.
 * @author tejc999999
 */
@Service
public class TeacherClassServiceImpl implements TeacherClassService {

    /**
     * クラス用リポジトリ(class repository).
     */
    @Autowired
    ClassRepository classRepository;

    /**
     * ユーザー用リポジトリ(class repository).
     */
    @Autowired
    UserRepository userRepository;

    /**
     * 全てのクラス情報を取得する.
     * @return クラスFormリスト
     */
    public List<ClassForm> findAll() {

        List<ClassForm> classFormList = new ArrayList<ClassForm>();

        for (ClassBean classBean : classRepository.findAll()) {
            ClassForm classForm = new ClassForm();
            classForm.setId(String.valueOf(classBean.getId()));
            classForm.setName(classBean.getName());
            classFormList.add(classForm);
        }
        
        return classFormList;
    }
    
    /**
     * 全ての学生について、IDと名前の対応マップを返す.
     * @return 学生のIDと名前の対応マップ
     */
    public Map<String, String> getUserIdMap() {
        
        Map<String, String> userMap = new HashMap<>();
        List<UserBean> userBeanList = userRepository.findByRoleId(RoleCode.ROLE_STUDENT.getId());
        
        if (userBeanList != null) {
            userBeanList.forEach(userBean -> {
                userMap.put(userBean.getId(), userBean.getName());
            });
        }
        
        return userMap;
    }
    
    /**
     * クラスを保存する（関連する先のテーブルは更新しない）.
     * @param form 登録するクラス情報
     * @return  登録したクラス情報
     */
    public ClassForm save(ClassForm form) {

        // Formの値を（クラスの情報）Beanにコピーする
        if (form.getId() != null && !form.getId().equals("")) {
        	// 更新時
        	
        	// 一旦、削除を行う（関連情報を個別に削除する必要があるため）
        	List<ClassBean> classBeanList = new ArrayList<>();
        	Optional<ClassBean> optClass = classRepository.findById(Long.valueOf(form.getId()));
        	optClass.ifPresent(bean -> classBeanList.add(bean));
        	classRepository.delete(classBeanList.get(0));
        }
        	
        ClassBean classBean = new ClassBean();
        // Formの値（クラスに所属するユーザーの情報）をBeanにコピーする
        List<String> userIdList = form.getUserCheckedList();
        if (userIdList != null) {
            for (int i = 0; i < userIdList.size(); i++) {
                StudentClassBean userClassBean = new StudentClassBean();
                userClassBean.setUserId(userIdList.get(i));
                // 新規登録なので、クラスIDはセットしない
                classBean.addUserClassBean(userClassBean);
            }
        }
	        
        // DBに保存する
    	classBean.setName(form.getName());
        classBean = classRepository.save(classBean);
        
        // Beanの値をFormにコピーする
        ClassForm resultForm = new ClassForm();
        resultForm.setId(String.valueOf(classBean.getId()));
        resultForm.setName(classBean.getName());
        resultForm.setUserCheckedList(classBean.getUserIdList());

        return resultForm;
    }
    
    
    /**
     * クラス情報を取得する.
     * @return クラスForm
     */
    public ClassForm findById(String id) {
        
        ClassForm classForm = new ClassForm();
        Optional<ClassBean> optClass = classRepository.findById(Long.valueOf(id));
        optClass.ifPresent(classBean -> {
            classForm.setId(String.valueOf(classBean.getId()));
            classForm.setName(classBean.getName());
            classForm.setUserCheckedList(classBean.getUserIdList());
        });
        
        return classForm;
    }
    
    /**
     * クラス削除.
     * @param id クラスID
     */
    public void delete(String id) {
        List<ClassBean> classBeanList = new ArrayList<>();
        Optional<ClassBean> optClass = classRepository.findById(Long.valueOf(id));
        optClass.ifPresent(classBean -> classBeanList.add(classBean));
        classRepository.delete(classBeanList.get(0));
    }
}
