package jp.ac.ems.impl.service.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jp.ac.ems.bean.UserBean;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.service.shared.SharedStudentQuestionHistoryService;

/**
 * 学生課題履歴用共通サービス
 * @author user01-m
 *
 */
@Service
public class SharedStudentQuestionHistoryServiceImpl implements SharedStudentQuestionHistoryService {

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
}
