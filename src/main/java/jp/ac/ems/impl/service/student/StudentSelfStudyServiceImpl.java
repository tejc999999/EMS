package jp.ac.ems.impl.service.student;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jp.ac.ems.bean.ClassBean;
import jp.ac.ems.bean.CourseBean;
import jp.ac.ems.bean.QuestionBean;
import jp.ac.ems.bean.StudentQuestionHistoryBean;
import jp.ac.ems.bean.StudentTaskBean;
import jp.ac.ems.bean.TaskBean;
import jp.ac.ems.bean.TaskQuestionBean;
import jp.ac.ems.config.ExamDivisionCode;
import jp.ac.ems.config.FieldLarge;
import jp.ac.ems.config.FieldMiddle;
import jp.ac.ems.config.FieldSmall;
import jp.ac.ems.form.student.SelfStudyForm;
import jp.ac.ems.form.student.SelfStudyQuestionForm;
import jp.ac.ems.form.teacher.TaskForm;
import jp.ac.ems.repository.QuestionRepository;
import jp.ac.ems.repository.StudentQuestionHistoryRepository;
import jp.ac.ems.repository.TaskRepository;
import jp.ac.ems.service.student.StudentSelfStudyService;
import lombok.Data;

/**
 * 学生用自習サービスクラス(self study service class for student).
 * 
 * @author user01-m
 */
@Service
public class StudentSelfStudyServiceImpl implements StudentSelfStudyService {

	/**
	 * 問題リポジトリ(quesiton repository)
	 */
	@Autowired
	private QuestionRepository questionRepository;
	
	/**
	 * 学生問題回答履歴リポジトリ(student question history repository)
	 */
	@Autowired
	private StudentQuestionHistoryRepository studentQuestionHistoryRepository;

	/**
	 * 課題リポジトリ(task repository)
	 */
	@Autowired
	private TaskRepository taskRepository;

    /**
     * ドロップダウン項目設定(Set dropdown param).
     * @param form 自習Form(self study form)
     * @param model モデル(model)
     */
	@Override
    public void setSelectData(SelfStudyForm form, Model model) {
    	// 年度取得
        Map<String, String> yearMap = findAllYearMap();
        model.addAttribute("yearDropItems", yearMap);
    	
    	// 大分類取得
        Map<String, String> fieldLMap = findAllFieldLMap();
        model.addAttribute("fieldLDropItemsItems", fieldLMap);
    	
    	// 中分類取得
        Map<String, String> fieldMMap = findAllFieldMMap(form.getSelectFieldL());
        model.addAttribute("fieldMDropItems", fieldMMap);
    	
    	// 小分類取得
        Map<String, String> fieldSMap = findAllFieldSMap(form.getSelectFieldM());
        model.addAttribute("fieldSDropItems", fieldSMap);
    	
    }

	/**
     * セレクトボックス項目設定(Set select box param).
     * @param form 自習Form(self study form)
     * @param model モデル(model)
	 */
	@Override
	public void setCheckItems(SelfStudyForm form, Model model) {
		Map<String, String> conditionCheckMap = new LinkedHashMap<>();
		conditionCheckMap.put(SelfStudyForm.CONDITION_1_KEY_UNANSWERED, SelfStudyForm.CONDITION_1_VALUE_UNANSWERED);
		conditionCheckMap.put(SelfStudyForm.CONDITION_2_KEY_LOW_ACC_RATE, SelfStudyForm.CONDITION_2_VALUE_LOW_ACC_RATE);
		conditionCheckMap.put(SelfStudyForm.CONDITION_3_KEY_MIX, SelfStudyForm.CONDITION_3_VALUE_MIX);
		conditionCheckMap.put(SelfStudyForm.CONDITION_4_KEY_ALL, SelfStudyForm.CONDITION_4_VALUE_ALL);
		model.addAttribute("conditionCheckItems", conditionCheckMap);
		
		Map<String, String> sortCheckMap = new LinkedHashMap<>();
		sortCheckMap.put(SelfStudyForm.SORT_1_KEY_LATEST, SelfStudyForm.SORT_1_VALUE_LATEST);
		sortCheckMap.put(SelfStudyForm.SORT_2_KEY_PREVIOUS, SelfStudyForm.SORT_2_VALUE_PREVIOUS);
		sortCheckMap.put(SelfStudyForm.SORT_3_KEY_RANDOM, SelfStudyForm.SORT_3_VALUE_RANDOM);
		model.addAttribute("sortCheckItems", sortCheckMap);
	}
	
	/**
	 * 条件に該当する問題IDリストを取得する.
	 * 
	 * @param form 自習Form(self study form)
	 * @return 自習Form(self study form)
	 */
	@Override
	public SelfStudyForm getQuestionList(SelfStudyForm form) {
		
		List<QuestionBean> questionBeanList = new ArrayList<>();
		
		// 年度：分野の条件なし
		if((form.getSelectYear() == null || form.getSelectYear().equals(""))
				&& (form.getSelectFieldS() == null || form.getSelectFieldS().equals(""))
						&& (form.getSelectFieldM() == null || form.getSelectFieldM().equals(""))
								&& (form.getSelectFieldL() == null || form.getSelectFieldL().equals(""))) {
			List<QuestionBean> list = questionRepository.findAll();
			if(list != null) {
				questionBeanList.addAll(list);
			}
		} else {
			if((form.getSelectYear() != null && !form.getSelectYear().equals(""))) {
				// 年度条件あり
				String yearStr = form.getSelectYear().substring(0, 4);
				String termStr = form.getSelectYear().substring(4, 5);

				if(form.getSelectFieldS() != null && !form.getSelectFieldS().equals("")) {
					// 年度＋小分野条件
					List<QuestionBean> list = questionRepository.findByYearAndTermAndFieldSId(yearStr, termStr, Byte.valueOf(form.getSelectFieldS()));
					if(list != null) {
						questionBeanList.addAll(list);
					}
				} else if(form.getSelectFieldM() != null && !form.getSelectFieldM().equals("")) {
					// 年度＋中分野条件
					List<QuestionBean> list = questionRepository.findByYearAndTermAndFieldMId(yearStr, termStr, Byte.valueOf(form.getSelectFieldM()));
					if(list != null) {
						questionBeanList.addAll(list);
					}
				} else if((form.getSelectFieldL() != null && !form.getSelectFieldL().equals(""))) {
					// 年度＋大分野条件
					List<QuestionBean> list = questionRepository.findByYearAndTermAndFieldLId(yearStr, termStr, Byte.valueOf(form.getSelectFieldL()));
					if(list != null) {
						questionBeanList.addAll(list);
					}
				} else {
					// 年度条件のみ
					List<QuestionBean> list = questionRepository.findByYearAndTerm(yearStr, termStr);
					if(list != null) {
						questionBeanList.addAll(list);
					}
				}
			} else {
				// 年度条件なし
				if(form.getSelectFieldS() != null && !form.getSelectFieldS().equals("")) {
					// 小分野条件
					List<QuestionBean> list = questionRepository.findByFieldSId(Byte.valueOf(form.getSelectFieldS()));
					if(list != null) {
						questionBeanList.addAll(list);
					}
				} else if(form.getSelectFieldM() != null && !form.getSelectFieldM().equals("")) {
					// 中分野条件
					List<QuestionBean> list = questionRepository.findByFieldMId(Byte.valueOf(form.getSelectFieldM()));
					if(list != null) {
						questionBeanList.addAll(list);
					}
				} else if((form.getSelectFieldL() != null && !form.getSelectFieldL().equals(""))) {
					// 大分野条件
					List<QuestionBean> list = questionRepository.findByFieldLId(Byte.valueOf(form.getSelectFieldL()));
					if(list != null) {
						questionBeanList.addAll(list);
					}
				}
			}
		}
		
		List<String> questionIdList = new ArrayList<>();
		for(QuestionBean bean : questionBeanList) {
			questionIdList.add(String.valueOf(bean.getId()));
		}

		// 条件による除外
		Map<String, RateData> questionIdForIncorrect50 = new LinkedHashMap<>();
		List<String> tempRemoveQuetionId = new ArrayList<>();
		if(form.getConditionChecked() != null && !form.getConditionChecked().equals(SelfStudyForm.CONDITION_4_KEY_ALL)) {

	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        String userId = auth.getName();
	        List<StudentQuestionHistoryBean> sqhBeanList = studentQuestionHistoryRepository.findAllByUserId(userId);
	        for(StudentQuestionHistoryBean bean : sqhBeanList) {
	
				if(form.getConditionChecked().equals(SelfStudyForm.CONDITION_1_KEY_UNANSWERED)
						|| form.getConditionChecked().equals(SelfStudyForm.CONDITION_3_KEY_MIX)) {
		        	// 回答済み除外（未回答のみ）
					
					tempRemoveQuetionId.add(String.valueOf(bean.getQuestionId()));
				}
				if(form.getConditionChecked().equals(SelfStudyForm.CONDITION_2_KEY_LOW_ACC_RATE)
						|| form.getConditionChecked().equals(SelfStudyForm.CONDITION_3_KEY_MIX)) {
					// 低回答率（50%以下）のみを別リストに退避する（正解数ゼロ、不正解数１以上の正解率0%を含む）
					
			        	if(questionIdList.contains(String.valueOf(bean.getQuestionId()))) {
			        		
			        		RateData rateData = null;
			        		if(questionIdForIncorrect50.containsKey(String.valueOf(bean.getQuestionId()))) {
			        			rateData = questionIdForIncorrect50.get(String.valueOf(bean.getQuestionId()));
			        		} else {
			        			rateData = new RateData();
			        		}
			        		if(bean.getCorrectFlg()) {
			        			rateData.setCorrectCnt(rateData.getCorrectCnt() + 1);
			        		} else {
			        			rateData.setIncorrectCnt(rateData.getIncorrectCnt() + 1);
			        		}
			        		questionIdForIncorrect50.put(String.valueOf(bean.getQuestionId()), rateData);
			        	}
				}
			}
	        // 低回答率が含まれるか検証するため、検証後にリストから除外する
	        questionIdList.removeAll(tempRemoveQuetionId);
	        
	        if(form.getConditionChecked().equals(SelfStudyForm.CONDITION_2_KEY_LOW_ACC_RATE)) {
	        	// 一旦リストを空にする
	        	questionIdList = new ArrayList<>();
	        	if(questionIdForIncorrect50 != null) {
	        		for(Map.Entry<String, RateData> entry : questionIdForIncorrect50.entrySet()) {
	        			if(entry.getValue().getCorrectCnt() <= entry.getValue().getIncorrectCnt())
	        				questionIdList.add(entry.getKey());
	        		}
	        	}
	        } else if(form.getConditionChecked().equals(SelfStudyForm.CONDITION_3_KEY_MIX)) {
	        	if(questionIdForIncorrect50 != null) {
	        		for(Map.Entry<String, RateData> entry : questionIdForIncorrect50.entrySet()) {
	        			if(entry.getValue().getCorrectCnt() <= entry.getValue().getIncorrectCnt())
	        				questionIdList.add(entry.getKey());
	        		}
	        	}
	        }
		}
		
		if(form.isLatestFlg()) {
			// 直近6回分だけにする
			
			List<String> removeQuestionIdList = new ArrayList<>();
			for(String questionId : questionIdList) {
				// 直近6回より前の問題を除外する
				// TODO:問い合わせ回数軽減策検討
				Optional<QuestionBean> optQuestion = questionRepository.findById(Long.valueOf(questionId));
				optQuestion.ifPresent(questionBean -> {
					// ※中止試験があった場合、コード修正が必要
					int yearInt = Integer.valueOf(questionBean.getYear());
					String termStr = questionBean.getTerm();
					int nowYearInt = Calendar.getInstance().get(Calendar.YEAR);
					int nowMonthInt = Calendar.getInstance().get(Calendar.MONTH);
					if(nowYearInt >= 2013 && nowMonthInt > 10) {
						// 2020春試験（中止）の影響なし
						if(nowMonthInt < 5) {
							// (1-4月)春試験：年度後
							if((nowYearInt - yearInt) > 3) {
								removeQuestionIdList.add(String.valueOf(questionBean.getId()));
							}
						} else if(nowMonthInt > 10) {
							// (11-12月)春試験：年度前
							if((nowYearInt - yearInt) > 2) {
								removeQuestionIdList.add(String.valueOf(questionBean.getId()));
							}
						} else if(nowMonthInt < 11) {
							// (5-10月)秋試験:
							if(((nowYearInt - yearInt) > 3) || ((nowYearInt - yearInt) == 3) && "H".equals(termStr)) {
								removeQuestionIdList.add(String.valueOf(questionBean.getId()));
							}
						}
					} else {
						// 2020春試験（中止）の影響あり
						if(nowMonthInt < 5) {
							// (1-4月)春試験：年度後
							if(((nowYearInt - yearInt) > 4) || ((nowYearInt - yearInt) == 4) && "H".equals(termStr)) {
								removeQuestionIdList.add(String.valueOf(questionBean.getId()));
							}
						} else if(nowMonthInt > 10) {
							// (11-12月)春試験：年度前
							if(((nowYearInt - yearInt) > 3) || ((nowYearInt - yearInt) == 3) && "H".equals(termStr)) {
								removeQuestionIdList.add(String.valueOf(questionBean.getId()));
							}
						} else if(nowMonthInt < 11) {
							// (5-10月)秋試験:
							if((nowYearInt - yearInt) > 3) {
								removeQuestionIdList.add(String.valueOf(questionBean.getId()));
							}
						}					}
				});
			}
			questionIdList.removeAll(removeQuestionIdList);
		}
		
		form.setQuestionList(questionIdList);
		
		return form;
	}

	/**
	 * 自習問題リストをソートする
	 * 
	 * @param selfStudyForm 自習Form(self study form)
	 * @return 自習Form(self study form)
	 */
	@Override
	public SelfStudyForm sortQuestionList(SelfStudyForm form) {
		
		if(form.getSortChecked() != null) {
			List<String> questionIdList = form.getQuestionList();
			
			if(form.getSortChecked().equals(SelfStudyForm.SORT_3_KEY_RANDOM)) {
				// ランダムでソート
			
				Collections.shuffle(questionIdList);
				
				form.setQuestionList(questionIdList);
			} else {
	
				List<QuestionBean> sortQuestionBeanList = new ArrayList<>();
				List<QuestionBean> questionBeanList = questionRepository.findAll();
				for(QuestionBean bean : questionBeanList) {
					if(questionIdList.contains(String.valueOf(bean.getId()))) {
						sortQuestionBeanList.add(bean);
					}
				}
	
				List<QuestionBean> sortedQuestionBeanList = new ArrayList<>();
				if(form.getSortChecked().equals(SelfStudyForm.SORT_1_KEY_LATEST)) {
					// 新しい順でソート
		
			    	sortedQuestionBeanList = sortQuestionBeanList.stream()
			    	        .sorted(Comparator.comparing(QuestionBean::getYear, Comparator.reverseOrder())
			    	        		.thenComparing(QuestionBean::getTerm, Comparator.naturalOrder()))
			    	        .collect(Collectors.toList());
		
				} else if(form.getSortChecked().equals(SelfStudyForm.SORT_2_KEY_PREVIOUS)) {
					// 古い順でソート
					
			    	sortedQuestionBeanList = sortQuestionBeanList.stream()
			    	        .sorted(Comparator.comparing(QuestionBean::getYear, Comparator.naturalOrder())
			    	        		.thenComparing(QuestionBean::getTerm, Comparator.reverseOrder()))
			    	        .collect(Collectors.toList());
			
				}
				
				List<String> sortedQuestionIdList = new ArrayList<>();
				for(QuestionBean bean : sortedQuestionBeanList) {
					sortedQuestionIdList.add(String.valueOf(bean.getId()));
				}
	
				form.setQuestionList(sortedQuestionIdList);
			}
		}
			
		return form;
	}
	
	/**
	 * 特定の問題の自習問題Formを取得する.
	 * 
	 * @param selfStudyForm 自習Form(self study form)
	 * @param number 問題番号(question number)
	 * @return 自習問題Form(self study question form)
	 */
	@Override
	public SelfStudyQuestionForm getQuestion(SelfStudyQuestionForm form, int number) {
		
		SelfStudyQuestionForm selfStudyQuestionForm = getSelfStudyQuestionForm(form, number);
		
		// 回答を語句に変換
		selfStudyQuestionForm.setCorrect(convertAnsweredIdToWord(selfStudyQuestionForm.getCorrect()));
		
		return selfStudyQuestionForm;
	}
	
	/**
	 * 回答処理を行い、特定の問題の自習問題Formを取得する.
	 * 
	 * @param selfStudyForm 自習Form(self study form)
	 * @param number 問題番号(question number)
	 * @return 自習問題Form(self study question form)
	 */
	@Override
	public SelfStudyQuestionForm getQuestionAndAnswer(SelfStudyQuestionForm form, int number) {
		
		SelfStudyQuestionForm selfStudyQuestionForm = getSelfStudyQuestionForm(form, number);
        String questionId = form.getQuestionList().get(number);

		// 回答履歴を保存する
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        
		StudentQuestionHistoryBean newSqhBean = new StudentQuestionHistoryBean();
		if(selfStudyQuestionForm.getCorrect().equals(form.getAnswer())) {
			newSqhBean.setCorrectFlg(true);
		} else {
			newSqhBean.setCorrectFlg(false);
		}
		newSqhBean.setQuestionId(Long.valueOf(questionId));
		newSqhBean.setUserId(userId);
		newSqhBean.setUpdateDate(new Date());
		studentQuestionHistoryRepository.save(newSqhBean);
		
		// 履歴保存後に回答を語句に変換
		selfStudyQuestionForm.setCorrect(convertAnsweredIdToWord(selfStudyQuestionForm.getCorrect()));
		
		return selfStudyQuestionForm;
	}
	
    /**
     * 回答アイテム取得
     * 
     * @return 回答アイテムマップ
     */
    @Override
    public Map<String,String> getAnswerSelectedItems(){
        Map<String, String> selectMap = new LinkedHashMap<String, String>();
        selectMap.put("1", "ア");
        selectMap.put("2", "イ");
        selectMap.put("3", "ウ");
        selectMap.put("4", "エ");
        return selectMap;
    }
    
    /**
     * 自習用課題作成.
     * @param form 自習Form
     */
    public void createSelfTask(SelfStudyForm form) {
    	
    	TaskBean taskBean = new TaskBean();

        // 課題作成者（自身のID）を設定する
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();
        taskBean.setTeacherId(userId);
        
        // 課題、問題中間情報をBeanに設定する
    	taskBean.clearTaskQuestionBean();
        List<String> questionList = form.getQuestionList();
        if(questionList != null) {
        	int i = 0;
        	for(String questionId : questionList) {
        		
	            TaskQuestionBean taskQuestionBean = new TaskQuestionBean();

	            Optional<QuestionBean> optQuestion = questionRepository.findById(Long.valueOf(questionId));
	            optQuestion.ifPresent(questionBean -> {

		            taskQuestionBean.setQuestionId(questionBean.getId());
	            });
	            taskQuestionBean.setSeqNumber(Long.valueOf(i++));

	            taskBean.addTaskQuestionBean(taskQuestionBean);
        	}
        }
        // 問題数を設定する
        taskBean.setQuestionSize(Long.valueOf(questionList.size()));
        
        // 提示先情報（ユーザ、課題中間情報）をBeanに設定する
    	StudentTaskBean studentTaskBean = new StudentTaskBean();
        studentTaskBean.setUserId(userId);
        
        taskBean.addStudentTaskBean(studentTaskBean);
    
	    // DBに保存する
	    taskBean.setTitle("【自習問題】");
	    taskBean.setDescription("【作成日】" + new SimpleDateFormat("yyyy年MM月dd日(E) H時mm分").format(new Date()));
	    taskBean = taskRepository.save(taskBean);
    }
    
    /**
     * 
     * @param form
     * @param number
     * @return
     */
    private SelfStudyQuestionForm getSelfStudyQuestionForm(SelfStudyQuestionForm form, int number) {
    	
		SelfStudyQuestionForm selfStudyQuestionForm = new SelfStudyQuestionForm();
		
		// 自習用の問題情報をセットする
		selfStudyQuestionForm.setQuestionList(form.getQuestionList());
		selfStudyQuestionForm.setSelectQuestionNumber(number);
		
		selfStudyQuestionForm.setAnswer(form.getAnswer());

        String questionId = form.getQuestionList().get(number);
        
		Optional<QuestionBean> optQuestion = questionRepository.findById(Long.valueOf(questionId));
		optQuestion.ifPresent(questionBean -> {
    		// 問題の情報をセットする
			selfStudyQuestionForm.setCorrect(String.valueOf(questionBean.getCorrect()));
			selfStudyQuestionForm.setDivision(questionBean.getDivision());
			selfStudyQuestionForm.setFieldLId(String.valueOf(questionBean.getFieldLId()));
			selfStudyQuestionForm.setFieldMId(String.valueOf(questionBean.getFieldMId()));
			selfStudyQuestionForm.setFieldSId(String.valueOf(questionBean.getFieldSId()));
			selfStudyQuestionForm.setId(String.valueOf(questionBean.getId()));
			selfStudyQuestionForm.setNumber(String.valueOf(questionBean.getNumber()));
			selfStudyQuestionForm.setTerm(questionBean.getTerm());
			selfStudyQuestionForm.setYear(questionBean.getYear());
    	});
		String imagePath = selfStudyQuestionForm.getYear() + "_" + selfStudyQuestionForm.getTerm()
			+ "/" + String.format("%02d", Integer.parseInt(selfStudyQuestionForm.getNumber())) + ".png";
		selfStudyQuestionForm.setImagePath(imagePath);
		
    	// 問題情報文字列を作成し、Formにセットする
    	StringBuffer questionInfoStrBuff = new StringBuffer();
    	int yearInt = Integer.valueOf(selfStudyQuestionForm.getYear());
    	String termStr = selfStudyQuestionForm.getTerm();
    	if(yearInt < 2019) {
    		questionInfoStrBuff.append("平成");
    		questionInfoStrBuff.append(yearInt - 1988 + "年");
    	} else if(yearInt == 2019) {
    		if("H".equals(termStr)) {
        		questionInfoStrBuff.append("平成");
        		questionInfoStrBuff.append(yearInt - 1988 + "年");
    		} else if("A".equals(termStr)) {
        		questionInfoStrBuff.append("令和元年");
    		}
    	} else if(yearInt > 2020) {
    		questionInfoStrBuff.append("令和");
    		questionInfoStrBuff.append(yearInt - 2019 + "年");
    	}
    	if("H".equals(termStr)) {
    		questionInfoStrBuff.append("春");
    	} else if("A".equals(termStr)) {
    		questionInfoStrBuff.append("秋");
    	}
		questionInfoStrBuff.append("期 問" + selfStudyQuestionForm.getNumber());
		selfStudyQuestionForm.setQuestionInfoStr(questionInfoStrBuff.toString());
    	
    	// 問題分野情報文字列を作成し、Formにセットする
		selfStudyQuestionForm.setQuestionFieldInfoStr(
    			FieldLarge.getName(ExamDivisionCode.AP.getName(), Byte.valueOf(selfStudyQuestionForm.getFieldLId())) + "/"
    			+ FieldMiddle.getName(ExamDivisionCode.AP.getName(), Byte.valueOf(selfStudyQuestionForm.getFieldMId())) + "/"
    			+ FieldSmall.getName(ExamDivisionCode.AP.getName(), Byte.valueOf(selfStudyQuestionForm.getFieldSId())));
		
		return selfStudyQuestionForm;
    }
	
    /**
     * 画面用年度マップ取得
     * @return 画面用年度マップ（key:ドロップダウンリストID、value：年度ラベル）
     */
    private Map<String, String> findAllYearMap() {
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	
    	for(QuestionBean questionBean : questionRepository.findDistinctYearAndTerm()) {
    		StringBuffer keyBuff = new StringBuffer();
    		StringBuffer valueBuff = new StringBuffer();
    		// 年度
    		keyBuff.append(questionBean.getYear());
    		
    		int yearInt = Integer.valueOf(questionBean.getYear());
    		String termStr = questionBean.getTerm();
        	if(yearInt < 2019) {
        		valueBuff.append("平成");
        		valueBuff.append(yearInt - 1988 + "年");
        	} else if(yearInt == 2019) {
        		if("H".equals(termStr)) {
        			valueBuff.append("平成");
        			valueBuff.append(yearInt - 1988 + "年");
        		} else if("A".equals(termStr)) {
        			valueBuff.append("令和元年");
        		}
        	} else if(yearInt > 2020) {
        		valueBuff.append("令和");
        		valueBuff.append(yearInt - 2019 + "年");
        	}
    		// 期
    		if("H".equals(termStr)) {
    			keyBuff.append("H");
    			valueBuff.append("春");
    		} else {
    			keyBuff.append("A");
    			valueBuff.append("秋");
    		}
   			
   			map.put(keyBuff.toString(), valueBuff.toString());
    	}
    	return map;
    }
    
    /**
     * 画面用大分類マップ取得(Get large  map for screen).
     * @return 画面用大分類マップ（key:ドロップダウンリストID、value：大分類ラベル）
     */
    private Map<String, String> findAllFieldLMap() {
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();

    	EnumSet.allOf(FieldLarge.class)
    	  .forEach(fieldL -> map.put(String.valueOf(fieldL.getId()), fieldL.getName()));
    	
    	return map;
    }
    
    /**
     * 画面用中分類マップ取得(Get middle filed map for screen).
     * @param parentId 大分類ID(large field id)
     * @return 画面用中分類マップ（key:ドロップダウンリストID、value：中分類ラベル）
     */
    private Map<String, String> findAllFieldMMap(String parentId) {

    	Map<String, String> map = new LinkedHashMap<String, String>();
    	if(parentId != null && !parentId.equals("")) {
    		map.putAll(FieldMiddle.getMap(Byte.valueOf(parentId)));
    	}
    	return map;
    }
    
    /**
     * 画面用小分類マップ取得(Get small filed map for screen).
     * @param parentId 中分類ID(middle field id)
     * @return 画面用小分類マップ（key:ドロップダウンリストID、value：小分類ラベル）
     */
    private Map<String, String> findAllFieldSMap(String parentId) {
    	
    	
    	Map<String, String> map = new LinkedHashMap<String, String>();
    	if(parentId != null && !parentId.equals("")) {
    		map.putAll(FieldSmall.getMap(Byte.valueOf(parentId)));
    	}
    	return map;
    }
    
	/**
	 * 回答IDを語句に置き換える(Convert answered id to word).
	 * @param answeredId 回答ID(answered id)
	 * @return 回答語句(answered word)
	 */
	private String convertAnsweredIdToWord(String answeredId) {
		String answeredWord = answeredId;
		switch(answeredId) {
			case "1":
				answeredWord = "ア";
				break;
			case "2":
				answeredWord = "イ";
				break;
			case "3":
				answeredWord = "ウ";
				break;
			case "4":
				answeredWord = "エ";
				break;
			default:
				answeredWord = "";
				break;
		}
		return answeredWord;
	}
	
	/**
	 * 内部処理用クラス
	 * @author user01
	 *
	 */
	@Data
	private class RateData {
		
		private int correctCnt = 0;
		
		private int incorrectCnt = 0;
	}
}
