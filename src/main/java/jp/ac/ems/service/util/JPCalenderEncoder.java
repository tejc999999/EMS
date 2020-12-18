package jp.ac.ems.service.util;

import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Locale;

/**
 * 西暦、和暦変換クラス
 * 
 * @author user01
 *
 */
public class JPCalenderEncoder {
	
	/**
	 * コンストラクタ
	 */
	private JPCalenderEncoder( ) {
	}

	/**
	 * 和暦変換インスタンス取得
	 * 
	 * @return 和暦変換インスタンス
	 */
	public static JPCalenderEncoder getInstance() {
		return JPCalenderEncoderHolder.instance;
	}

	/**
	 * 西暦->和暦変換
	 * 
	 * @param year 年（西暦）
	 * @param term 期（"A" or "H")
	 * @return
	 */
	public String convertJpCalender(String year, String term) {

		StringBuffer jpCalenderStrBuff = new StringBuffer();
		
		int yearInt = Integer.valueOf(year);
		if(yearInt == 2019 && "A".equals(term)) {
			// 特殊パターン：2019年秋のみ平成31年->令和元年に変更
			
			jpCalenderStrBuff.append("令和元年");
		} else  {
			JapaneseDate japaneseDate = JapaneseDate.of(yearInt, 1, 1);
			// 元号を日本語（漢字）出力
			DateTimeFormatter jpFormatter = DateTimeFormatter.ofPattern("G", Locale.JAPAN);
			String jpCal = jpFormatter.format(japaneseDate);
			int yearOfJpCal = japaneseDate.get(ChronoField.YEAR_OF_ERA);
			
			jpCalenderStrBuff.append(jpCal);
			jpCalenderStrBuff.append(yearOfJpCal);
			jpCalenderStrBuff.append("年");
		}
		
    	if("H".equals(term)) {
    		jpCalenderStrBuff.append("春");
    	} else if("A".equals(term)) {
    		jpCalenderStrBuff.append("秋");
    	}
    	
		return jpCalenderStrBuff.toString();
	}
	
	/**
	 * 和暦変換インスタンスHolder
	 * @author user01
	 *
	 */
	private static class JPCalenderEncoderHolder {
		
		private static final JPCalenderEncoder instance = new JPCalenderEncoder();
	}
}
