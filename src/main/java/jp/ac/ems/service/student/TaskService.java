package jp.ac.ems.service.student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.ems.bean.TaskBean;
import jp.ac.ems.bean.UserBean;
import jp.ac.ems.form.teacher.TaskForm;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.repository.TaskRepository;
import jp.ac.ems.repository.UserRepository;

/**
 * 学生用課題Serviceクラス（student task Service Class）.
 * @author tejc999999
 */
@Service
public class TaskService {
	
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
     * 全ての問題を取得する.
     * @return 全ての問題Formリスト
     */
    public List<TaskForm> findAllByStudent(String username) {
        List<TaskForm> list = new ArrayList<>();

        List<String> taskIdList = new ArrayList<String>();
        Optional<UserBean> optUser = userRepository.findByIdFetchUserTask(username);
        optUser.ifPresent(userBean -> {
        	taskIdList.addAll(userBean.getTaskIdList());
        });
    	for(String taskId : taskIdList) {
            Optional<TaskBean> optTask = taskRepository.findById(Long.parseLong(taskId));
            optTask.ifPresent(taskBean -> {
            	TaskForm taskForm = new TaskForm();
            	taskBean.getTitle();
            	taskBean.getDescription();
                list.add(taskForm);
            });
    	}

        return list;
    }
}
