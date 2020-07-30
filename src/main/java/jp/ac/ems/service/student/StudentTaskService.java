package jp.ac.ems.service.student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.ems.bean.TaskBean;
import jp.ac.ems.bean.UserBean;
import jp.ac.ems.form.student.TaskForm;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.repository.TaskRepository;
import jp.ac.ems.repository.UserRepository;

/**
 * 学生用課題Serviceクラス（student task Service Class）.
 * @author tejc999999
 */
@Service
public class StudentTaskService {
	
    /**
     * 課題リポジトリ(task repository).
     */
    @Autowired
    TaskRepository taskRepository;

    /**
     * ユーザーリポジトリ(user repository).
     */
    @Autowired
    UserRepository userRepository;

    /**
     * 問題リポジトリ(question repository).
     */
    @Autowired
    QuestionRepository questionRepository;

    /**
     * ユーザに紐づく全ての課題を取得する.
     * @param userId ユーザID
     * @return 全ての問題Formリスト
     */
    public List<TaskForm> findAllByStudent(String userId) {
        List<TaskForm> list = new ArrayList<>();

        List<String> taskIdList = new ArrayList<String>();
        Optional<UserBean> optUser = userRepository.findByIdFetchUserTask(userId);
        optUser.ifPresent(userBean -> {
        	taskIdList.addAll(userBean.getTaskIdList());
        });
    	for(String taskId : taskIdList) {
            Optional<TaskBean> optTask = taskRepository.findById(Long.parseLong(taskId));
            optTask.ifPresent(taskBean -> {
            	TaskForm taskForm = new TaskForm();
            	taskForm.setTitle(taskBean.getTitle());
            	taskForm.setDescription(taskBean.getDescription());
                list.add(taskForm);
            });
    	}

        return list;
    }
}
