package jp.ac.ems.impl.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jp.ac.ems.bean.ClassBean;
import jp.ac.ems.bean.CourseBean;
import jp.ac.ems.bean.StudentQuestionHistoryBean;
import jp.ac.ems.common.data.GradeData;
import jp.ac.ems.form.GradeForm;
import jp.ac.ems.form.teacher.ClassForm;
import jp.ac.ems.repository.ClassRepository;
import jp.ac.ems.repository.CourseRepository;
import jp.ac.ems.repository.StudentQuestionHistoryRepository;
import jp.ac.ems.repository.UserRepository;
import jp.ac.ems.service.GradeService;
import jp.ac.ems.service.shared.SharedGradeService;

/**
 * 学生用成績Serviceクラス（student grade Service Class）.
 * @author tejc999999
 */
@Service
public class GradeServiceImpl  implements GradeService {

	/**
	 * 共通成績サービス(common grade service)
	 */
	@Autowired
	private SharedGradeService sharedGradeService;
	
	/**
	 * 問題回答履歴リポジトリ(question answer history repository)
	 */
	@Autowired
	private StudentQuestionHistoryRepository studentQuestionHistoryRepository;

	/**
	 * クラスリポジトリ(class repository).
	 */
	@Autowired
	private ClassRepository classRepository;

	/**
	 * コースリポジトリ(course repository).
	 */
	@Autowired
	private CourseRepository courseRepository;
	
	/**
	 * 全問題の成績を取得する.
	 * @param form 成績Form(grad form)
	 * @param correctSortFlg 正解率順ソートフラグ(sort correct rate flag)
	 * @return 成績Form(grad form)
	 */
    @Override
    public GradeForm getGradeFormDefault(GradeForm form) {

    	return getGradeForm(form);
	}
    
//	/**
//	 * 特定年度の成績を取得する.
//	 * @param form 成績Form(grad form)
//	 * @return 成績Form(grad form)
//	 */
//    @Override
//	public GradeForm getGradeFormByField(GradeForm form) {
//		
//		form.setSelectYear(null);
//		
//		return getGradeForm(form);
//	}
//
//	/**
//	 * 特定分類の成績を取得する.
//	 * @param form 成績Form(grad form)
//	 * @return 成績Form(grad form)
//	 */
//    @Override
//	public GradeForm getGradeFormByYear(GradeForm form) {
//		
//		form.setSelectFieldL(null);
//		form.setSelectFieldM(null);
//		form.setSelectFieldS(null);
//		
//		return getGradeForm(form);
//	}

	/**
	 * 成績Formを取得する(get grade form).
	 * 
	 * @param form 成績Form（grade form）
	 * @return 成績マップ(grade map)
	 */
    @Override
	public GradeForm getGradeForm(GradeForm form) {

		// 成績作成
		List<StudentQuestionHistoryBean> allQuestionHistory = new ArrayList<>();
    	if((form.getSelectClass() == null || "".equals(form.getSelectClass()))
    			&& (form.getSelectCourse() == null || "".equals(form.getSelectCourse()))) {
    		// クラス、コースの指定なし（全員の回答履歴を取得）
    		allQuestionHistory.addAll(studentQuestionHistoryRepository.findAll());
    	} else if(form.getSelectClass() != null && !"".equals(form.getSelectClass())
    			&& (form.getSelectCourse() != null && !"".equals(form.getSelectCourse()))) {
    		// クラス、コースの両方を指定（共通のユーザーのみ抽出）
        	List<String> userIdList = new ArrayList<>();
        	List<String> classUserIdList = new ArrayList<>();
        	List<String> courseUserIdList = new ArrayList<>();
	    	if(form.getSelectClass() != null && !"".equals(form.getSelectClass())) {
	            Optional<ClassBean> optClass = classRepository.findById(Long.valueOf(form.getSelectClass()));
	            optClass.ifPresent(classBean -> {
	            	classUserIdList.addAll(classBean.getUserIdList());
	            });
	    	}
	    	if(form.getSelectCourse() != null && !"".equals(form.getSelectCourse())) {
	    		// コースに所属するユーザーとクラスを取得
	    		List<String> classIdList = new ArrayList();
	            Optional<CourseBean> optClass = courseRepository.findById(Long.valueOf(form.getSelectCourse()));
	            optClass.ifPresent(courseBean -> {
	            	courseUserIdList.addAll(courseBean.getPartStudentIdList());
	            	classIdList.addAll(courseBean.getClassIdList());
	            });
	            // コースに所属するクラスのユーザーを取得
	            for(String classId : classIdList) {
		            Optional<ClassBean> optClassByCourse = classRepository.findById(Long.valueOf(classId));
		            optClassByCourse.ifPresent(classBean -> {
		            	courseUserIdList.addAll(classBean.getUserIdList());
		            });
	            }
	    	}
	    	// クラスとコースの両方に存在するユーザーのみ残す
	    	for(String userId : classUserIdList) {
	    		if(courseUserIdList.contains(userId)) {
	    			userIdList.add(userId);
	    		}
	    	}
	    	// 該当ユーザーの回答履歴をすべて取得
	    	for(String userId : userIdList) {
	    		allQuestionHistory.addAll(studentQuestionHistoryRepository.findAllByUserId(userId));
	    	}
    	} else {
    		// クラス、コースの指定あり
        	List<String> userIdList = new ArrayList<>();
	    	if(form.getSelectClass() != null && !"".equals(form.getSelectClass())) {
	            Optional<ClassBean> optClass = classRepository.findById(Long.valueOf(form.getSelectClass()));
	            optClass.ifPresent(classBean -> {
	            	userIdList.addAll(classBean.getUserIdList());
	            });
	    	}
	    	if(form.getSelectCourse() != null && !"".equals(form.getSelectCourse())) {
	    		// コースに所属するユーザーとクラスを取得
	    		List<String> classIdList = new ArrayList();
	            Optional<CourseBean> optClass = courseRepository.findById(Long.valueOf(form.getSelectCourse()));
	            optClass.ifPresent(courseBean -> {
	            	userIdList.addAll(courseBean.getPartStudentIdList());
	            	classIdList.addAll(courseBean.getClassIdList());
	            });
	            // コースに所属するクラスのユーザーを取得
	            for(String classId : classIdList) {
		            Optional<ClassBean> optClassByCourse = classRepository.findById(Long.valueOf(classId));
		            optClassByCourse.ifPresent(classBean -> {
		            	userIdList.addAll(classBean.getUserIdList());
		            });
	            }
	    	}
	    	// 該当ユーザーの回答履歴をすべて取得
	    	for(String userId : userIdList) {
	    		allQuestionHistory.addAll(studentQuestionHistoryRepository.findAllByUserId(userId));
	    	}
    	}
		
		Map<String, GradeData> gradeMap = sharedGradeService.createGrade(form, allQuestionHistory);
		
		// 成績のソート
    	List<GradeData> sortGradeList = null;
		if(GradeForm.SORT_CORRECT_KEY.equals(form.getSelectSortKey())) {
			// 正解率でソート
			List<GradeData> gradeList = new ArrayList<>(gradeMap.values());
	    	sortGradeList = gradeList.stream()
	    	        .sorted(Comparator.comparingDouble(GradeData::getCorrectRate).reversed())
	    	        .collect(Collectors.toList());
			
		} else {
			// 合計カウントの降順でソート
			List<GradeData> gradeList = new ArrayList<>(gradeMap.values());
	    	sortGradeList = gradeList.stream()
	    	        .sorted(Comparator.comparingInt(GradeData::getTotalCnt).reversed())
	    	        .collect(Collectors.toList());
		}

		// ユーザ情報設定
		sharedGradeService.viewSettingUser(form, sortGradeList);
		
		// グラフ描画設定
		int width = 10;
		if(sortGradeList != null && sortGradeList.size() > 0) {
			width = String.valueOf(sortGradeList.get(0).getTotalCnt()).length();
		}
		int height = form.getUserNameList().size();
		sharedGradeService.viewSettingGraph(form, height, width);
		
		return form;
	}
	
    /**
     * ドロップダウン項目設定(Set dropdown param).
     * @param form　成績Form(grade form)
     * @param model モデル(model)
     */
	@Override
    public void setSelectData(GradeForm form, Model model) {
		
		String fieldL = form.getSelectFieldL();
		String fieldM = form.getSelectFieldM();
		sharedGradeService.setSelectData(fieldL, fieldM, model);
    }

}
