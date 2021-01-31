package jp.ac.ems.impl.service.shared;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jp.ac.ems.bean.UserBean;
import jp.ac.ems.config.QuestionTag;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.service.shared.SharedTagService;

/**
 * タグ共通サービス
 * @author user01-m
 *
 */
@Service
public class SharedTagServiceImpl implements SharedTagService {

	/**
	 * ユーザー情報リポジトリ
	 */
	@Autowired
	UserRepository userRepository;
	
    /**
     * 問題タグ情報保存.
     * 
     * @param form 問題ID
     * @param tagId タグID
     * @param tagChangeFlg タグ変更状態（true:有効化、false:無効化)
     */
    @Override
    public void saveQuestionTag(String questionId, String tagId, String tagCheckFlg) {

		List<UserBean> userBeanList = new ArrayList<UserBean>();
		// 一旦ユーザー情報を削除
		// TODO:中間テーブルをクリアすることで対応してみる
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        Optional<UserBean> optUser = userRepository.findById(userId);
        optUser.ifPresent(userBean -> {
        	userBeanList.add(userBean);
        });
        UserBean userBean = userBeanList.get(0);
        userBean.updateQuestionTagId(questionId, tagId, Boolean.valueOf(tagCheckFlg));
    }
    
    /**
     * 問題のタグ情報リストを取得する。
     * 
     * @param questionId 問題ID
     * @return タグ情報リスト
     */
    @Override
    public List<String> getQuestionTagList(String questionId) {
    	
    	List<String> list = new ArrayList<String>();
    	
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        
        Optional<UserBean> optUser = userRepository.findById(userId);
        optUser.ifPresent(userBean -> {
			List<String> tagIdList = userBean.getQuestionTagList(questionId);
			if(tagIdList != null && tagIdList.size() > 0) {
				list.addAll(tagIdList);
        	}
        });
        
    	return list;
    }
    
    /**
     * 問題タグアイテム取得.
     * 
     * @return 問題タグアイテムマップ
     */
	@Override
    public Map<String, String> getQuestionTagSelectedItems() {
        Map<String, String> selectMap = new LinkedHashMap<String, String>();
        selectMap.put(String.valueOf(QuestionTag.QUESTION_TAG_1_TAG_RED.getId()), QuestionTag.QUESTION_TAG_1_TAG_RED.getName());
        selectMap.put(String.valueOf(QuestionTag.QUESTION_TAG_2_TAG_GREEN.getId()), QuestionTag.QUESTION_TAG_2_TAG_GREEN.getName());
        selectMap.put(String.valueOf(QuestionTag.QUESTION_TAG_3_TAG_BLUE.getId()), QuestionTag.QUESTION_TAG_3_TAG_BLUE.getName());
        return selectMap;
	}
}
