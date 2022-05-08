package jp.ac.ems.form.teacher;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 先生用個課題Form(question individual task for teacher).
 * @author tejc999999
 */
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class TaskIndividualForm extends TaskForm {

	/**
	 * 選択試験区分（select exam division）.
	 */
	private String selectExamDivision = "";
	
	/**
    * 選択年度(select year).
    */
   private String selectYear = "";
   
   /**
    * 選択大分類(select large field).
    */
   private String selectFieldL = "";
   
   /**
    * 選択中分類(select middle field).
    */
   private String selectFieldM = "";
   
   /**
    * 選択小分類（select small field).
    */
   private String selectFieldS = "";
}
