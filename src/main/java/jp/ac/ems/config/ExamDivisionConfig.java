package jp.ac.ems.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * サーバー関連設定ファイル読み込み(Load server related configuration file).
 * @author tejc999999
 */
@Configuration
public class ExamDivisionConfig {

    /**
     * AP区分画像ファイルパス(AP division image file path).
     */
    @Value("${ap.question.imagefilepath}")
    private String apQuestionImageFilePath;

    /**
     * プロパティを取得する(get the property).
     * @return プロパティ(property)
     */
    @Bean
    public ExamDivisionCodeProperties examDivisionCodeProperties() {

        ExamDivisionCodeInfoDetail apInfo = new ExamDivisionCodeInfoDetail();
        apInfo.setName(ExamDivisionCode.AP.getName());
        apInfo.setFilepath(apQuestionImageFilePath);

        ExamDivisionCodeInfoDetail feInfo = new ExamDivisionCodeInfoDetail();
        feInfo.setName(ExamDivisionCode.FE.getName());

        Map<String, ExamDivisionCodeInfoDetail> map
                = new HashMap<String, ExamDivisionCodeInfoDetail>();

        map.put(apInfo.getName(), apInfo);
        map.put(feInfo.getName(), feInfo);

        return new ExamDivisionCodeProperties(map);
    }

    /**
     * サーバー文字コード(server character code).
     */
    @Value("${server.charactercode}")
    private String characterCode;

    /**
     * プロパティを取得する(get the property).
     * @return プロパティ(property)
     */
    @Bean
    public ServerProperties serverProperties() {

        return new ServerProperties(characterCode);
    }
}
