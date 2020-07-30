package jp.ac.ems.config;

import java.util.Map;

import lombok.Data;

/**
 * ローカル設定関連プロパティファイル設定値(Local settings related property file settings).
 * @author tejc999999
 */
@Data
public class ExamDivisionCodeProperties {

    private Map<String, ExamDivisionCodeInfoDetail> map;

    /**
     * コンストラクタ(Constructor).
     * @param map 試験区分設定マップ(Exam division code setting map)
     */
    public ExamDivisionCodeProperties(Map<String, ExamDivisionCodeInfoDetail> map) {
        this.map = map;
    }
}
