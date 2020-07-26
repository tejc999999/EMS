package jp.ac.ems.controller.teacher;

import java.util.List;
import java.util.Map;

import jp.ac.ems.form.teacher.TaskForm;
import jp.ac.ems.service.teacher.QuestionService;
import jp.ac.ems.service.teacher.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 先生用課題Contollerクラス（teacher task Controller Class）.
 * @author tejc999999
 */
@Controller
@RequestMapping("/teacher/task")
public class TaskController {

    @Autowired
    TaskService taskService;

//    @Autowired
//    QuestionService questionService;

    /**
     * モデルにフォームをセットする(set form the model).
     * @return 課題Form(task form)
     */
    @ModelAttribute
    TaskForm setupForm() {
        return new TaskForm();
    }
    
    /**
     * 課題登録用問題一覧ページ表示(show question list page for task add).
     * @param model 問題一覧保存用モデル(model to save question list)
     * @return 課題登録用問題一覧ページビュー(question list page view for task add)
     */
    @GetMapping
    String list(Model model) {

        List<TaskForm> list = taskService.findAll();

        model.addAttribute("tasks", list);

        return "teacher/task/list";
    }
    
    /**
     * 課題登録(task add).
     * @return 課題情報登録用ページビュー(task info add page view)
     */
    @GetMapping(path = "add")
    public String add(Model model) {

        return "/teacher/task/add";
    }

    /**
     * 課題編集(task edit).
     * @return 課題情報登録用ページビュー(task info add page view)
     */
    @PostMapping(path = "edit")
    public String edit(@RequestParam String id, Model model) {

        TaskForm taskForm = taskService.findById(id);

        model.addAttribute("taskForm", taskForm);
    	
        return "/teacher/task/edit";
    }
    
    /**
     * 課題編集処理(edit process for task).
     * @return 課題一覧ページリダイレクト(task list page redirect)
     */
    @PostMapping(path = "editprocess")
    public String editprocess(TaskForm form, Model model) {

    	taskService.save(form);
    	
        return "redirect:/teacher/task";
    }
    
    /**
     * 課題登録(task add).
     * @return 課題一覧(task list)
     */
    @PostMapping(path = "delete")
    public String delete(@RequestParam String id, Model model) {

    	// 削除処理
    	taskService.delete(id);
    	
        return "redirect:/teacher/task";
    }

    /**
     * 課題情報登録用(task info add).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model to save xxx)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(path = "add_question")
    public String addInfo(@Validated TaskForm form, BindingResult result,
            Model model) {

//        model.addAttribute("taskForm", form);

    	// 年度取得
        Map<String, String> yearMap = taskService.findAllYearMap();
        model.addAttribute("yearDropItems", yearMap);
    	
    	// 大分類取得
        Map<String, String> fieldLMap = taskService.findAllFieldLMap();
        model.addAttribute("fieldLDropItemsItems", fieldLMap);
    	
    	// 中分類取得
        Map<String, String> fieldMMap = taskService.findAllFieldMMap(form.getSelectFieldL());
        model.addAttribute("fieldMDropItems", fieldMMap);
    	
    	// 小分類取得
        Map<String, String> fieldSMap = taskService.findAllFieldSMap(form.getSelectFieldM());
        model.addAttribute("fieldSDropItems", fieldSMap);
    	
        // 全問題取得
        Map<String, String> questionMap = taskService.findAllMap();
        model.addAttribute("questionCheckItems", questionMap);
        
        return "/teacher/task/add_question";
    }
    
    /**
     * 課題登録(task add).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model to save xxx)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(path = "add_process")
    public String addProcess(@Validated TaskForm form, BindingResult result,
            Model model) {

        // コース情報をDBに保存する
        taskService.save(form);
        
        return "redirect:/teacher/task";
    }
    
    /**
     * 年度別問題取得(Obtaining questions by year).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(path = "add_process", params = "selectYearBtn")
    public String addSelectYear(@Validated TaskForm form, BindingResult result,
            Model model) {

    	Map<String, String> questionMap = taskService.findAllQuestionByYearAndTerm(form.getSelectYear());
    	model.addAttribute("questionCheckItems", questionMap);
    	
    	// 年度取得
        Map<String, String> yearMap = taskService.findAllYearMap();
        model.addAttribute("yearDropItems", yearMap);
    	
    	// 大分類取得
        Map<String, String> fieldLMap = taskService.findAllFieldLMap();
        model.addAttribute("fieldLDropItemsItems", fieldLMap);
    	
    	// 中分類取得
        Map<String, String> fieldMMap = taskService.findAllFieldMMap(form.getSelectFieldL());
        model.addAttribute("fieldMDropItems", fieldMMap);
    	
    	// 小分類取得
        Map<String, String> fieldSMap = taskService.findAllFieldSMap(form.getSelectFieldM());
        model.addAttribute("fieldSDropItems", fieldSMap);
    	
        // 入力状態保持のため
        model.addAttribute("courseForm", form);
        
        return "/teacher/task/add_question";
    }
    
    /**
     * 中分類取得(get field middle list).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(path = "add_process", params = "selectFieldLargeBtn")
    public String addSelectFieldMiddle(@Validated TaskForm form, BindingResult result,
            Model model) {
        
        return addInfo(form, result, model);
    }
    
    /**
     * 小分類取得(get field small list).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(path = "add_process", params = "selectFieldMiddleBtn")
    public String addSelectFieldSmall(@Validated TaskForm form, BindingResult result,
            Model model) {
        
        return addInfo(form, result, model);
    }
    
    /**
     * 分野別問題取得(Obtaining questions by field).
     * @param form 課題Form(task form)
     * @param result エラーチェック結果(error validate result)
     * @param model モデル(model)
     * @return 課題問題登録用ページビュー(task question add page view)
     */
    @PostMapping(path = "add_process", params = "selectFieldBtn")
    public String addSelectField(@Validated TaskForm form, BindingResult result,
            Model model) {

    	Map<String, String> questionMap = taskService.findAllQuestionByField(form.getSelectFieldL(), form.getSelectFieldM(), form.getSelectFieldS());
    	model.addAttribute("questionCheckItems", questionMap);
    	
    	// 年度取得
        Map<String, String> yearMap = taskService.findAllYearMap();
        model.addAttribute("yearDropItems", yearMap);
    	
    	// 大分類取得
        Map<String, String> fieldLMap = taskService.findAllFieldLMap();
        model.addAttribute("fieldLDropItemsItems", fieldLMap);
    	
    	// 中分類取得
        Map<String, String> fieldMMap = taskService.findAllFieldMMap(form.getSelectFieldL());
        model.addAttribute("fieldMDropItems", fieldMMap);
    	
    	// 小分類取得
        Map<String, String> fieldSMap = taskService.findAllFieldSMap(form.getSelectFieldM());
        model.addAttribute("fieldSDropItems", fieldSMap);
    	
        // 入力状態保持のため
        model.addAttribute("courseForm", form);
        
        return "/teacher/task/add_question";
    }
}
