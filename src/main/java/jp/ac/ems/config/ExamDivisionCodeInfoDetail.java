package jp.ac.ems.config;

import lombok.Data;

/**
 * ローカルファイル情報(Local file information).
 * @author tejc999999
 */
@Data
public class ExamDivisionCodeInfoDetail {

    /**
     * 試験区分コード(division code).
     */
    private String code;

    /**
     * 問題画像ファイルパス(question image file path).
     */
    private String filepath;
}
