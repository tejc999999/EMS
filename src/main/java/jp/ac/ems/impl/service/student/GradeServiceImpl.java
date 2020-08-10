package jp.ac.ems.impl.service.student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.bean.StudentQuestionHistoryBean;
import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.form.student.GradeForm;
import jp.ac.ems.form.student.TaskForm;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.repository.StudentQuestionHistoryRepository;
import jp.ac.ems.service.student.GradeService;
import jp.ac.ems.service.student.StudentTaskService;
import lombok.Data;

/**
 * 学生用成績Serviceクラス（student grade Service Class）.
 * @author tejc999999
 */
@Service
public class GradeServiceImpl  implements GradeService {

	/**
	 * 問題回答履歴リポジトリ(question answer history repository)
	 */
	@Autowired
	private StudentQuestionHistoryRepository studentQuestionHistoryRepository;
	
	/**
	 * 問題リポジトリ(quesiton repository)
	 */
	@Autowired
	private QuestionRepository questionRepository;
	
    /**
     * 全ての学生の成績を取得する(get grade for all student).
     * @return 全ての成績Formリスト(all grade form list)
     */
	@Override
	public GradeForm getAllGrade() {

		GradeForm gradeForm = new GradeForm();
		
		Map<String, Grade> gradeMap = new LinkedHashMap<>();

		List<StudentQuestionHistoryBean> studentQuestHistoryBeanList = studentQuestionHistoryRepository.findAll();
		for(StudentQuestionHistoryBean sqhBean : studentQuestHistoryBeanList) {
			String userId = sqhBean.getUserId();
			Long questionId = sqhBean.getQuestionId();
			int answerCnt = sqhBean.getCorrectCnt() + sqhBean.getIncorrectCnt();
			Optional<QuestionBean> optQuestion = questionRepository.findById(questionId);
			optQuestion.ifPresent(questionBean -> {
				Grade grade = null;
				if(gradeMap.containsKey(userId)) {
					grade = gradeMap.get(userId);
				} else {
					grade = new Grade();
					grade.setUserId(userId);
				}
				// 合計カウント＋
				grade.setTotalCnt(grade.getTotalCnt() + answerCnt);
				Byte fieldLId = questionBean.getFieldLId();
				if(fieldLId == FieldLarge.AP_FL_1_TECHNOLOGY.getId()) {
					// テクノロジ＋
					grade.setTechnologyCnt(grade.getTechnologyCnt() + answerCnt);
				} else if(fieldLId == FieldLarge.AP_FL_2_MANAGEMENT.getId()) {
					// マネジメント＋
					grade.setManagementCnt(grade.getManagementCnt() + answerCnt);
				} else if(fieldLId == FieldLarge.AP_FL_3_STRATEGY.getId()) {
					// ストラテジ＋
					grade.setStrategyCnt(grade.getStrategyCnt() + answerCnt);
				}
				gradeMap.put(userId, grade);
			});
		}
		
		// 合計カウントの降順でソート
		List<Grade> gradeList = new ArrayList<>(gradeMap.values());
    	List<Grade> sortGradeList = gradeList.stream()
    	        .sorted(Comparator.comparingInt(Grade::getTotalCnt).reversed())
    	        .collect(Collectors.toList());
		
		List<String> userIdList = new ArrayList<>();
		List<String> gradeTotalList = new ArrayList<>();
		List<String> gradeTechnologyList = new ArrayList<>();
		List<String> gradeManagementList = new ArrayList<>();
		List<String> gradeStrategyList = new ArrayList<>();
		for(Grade grade : sortGradeList) {
			userIdList.add(grade.getUserId());
			gradeTotalList.add(String.valueOf(grade.getTotalCnt()));
			gradeTechnologyList.add(String.valueOf(grade.getTechnologyCnt()));
			gradeManagementList.add(String.valueOf(grade.getManagementCnt()));
			gradeStrategyList.add(String.valueOf(grade.getStrategyCnt()));
		}
		gradeForm.setUserIdList(userIdList);
		gradeForm.setGradeTotalList(gradeTotalList);
		gradeForm.setGradeTechnologyList(gradeTechnologyList);
		gradeForm.setGradeManagementList(gradeManagementList);
		gradeForm.setGradeStrategyList(gradeStrategyList);
		
		// グラフ描画領域縦幅設定
		gradeForm.setCanvasHeight(String.valueOf(gradeForm.getUserIdList().size() * 50));

		// グラフ横目盛り幅設定
		int length = String.valueOf(gradeForm.getGradeTotalList().get(0)).length();
		int xStepSize = 1;
		if(length > 2) {
			xStepSize = (int) Math.pow(Double.valueOf(10), Double.valueOf(length - 2));
		}
		gradeForm.setXStepSize(String.valueOf(xStepSize));
		
		return gradeForm;
	}

	/**
	 * 内部処理用成績クラス
	 * 
	 * @author user01-m
	 */
	@Data
	class Grade {
		
		private String userId;
		
		private int totalCnt = 0;
		
		private int technologyCnt = 0;
		
		private int managementCnt = 0;
		
		private int strategyCnt = 0;
	}
}
