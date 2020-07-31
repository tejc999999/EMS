package jp.ac.ems.service.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.config.FieldMiddle;
import jp.ac.ems.config.FieldSmall;
import jp.ac.ems.form.teacher.QuestionForm;
import jp.ac.ems.repository.QuestionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;


/**
 * 先生用問題Serviceクラス（teacher question Service Class）.
 * @author tejc999999
 *
 */
public interface TeacherQuestionService {
    
    /**
     * 全ての問題を取得する(get all question).
     * @return 全ての問題Formリスト
     */
    public List<QuestionForm> findAll();

    /**
     * 問題を取得する.
     * @param id 問題ID
     * @return 問題Form
     */
    public QuestionForm findById(String id);
    
    /**
     * 問題を登録する.
     * @param form 問題Form
     * @return 登録済み問題Form
     */
    public QuestionForm save(QuestionForm form);
    
    /**
     * 問題削除.
     * @param id 問題ID
     */
    public void delete(String id);
}
