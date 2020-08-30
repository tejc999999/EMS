package jp.ac.ems.config;

/**
 * 問題タグコードEnum(question tag code enum).
 * @author tejc999999
 */
public enum QuestionTag {
    QUESTION_TAG_1_TAG_RED((long)1, "タグ赤"),
    QUESTION_TAG_2_TAG_GREEN((long)2, "タグ緑"),
    QUESTION_TAG_3_TAG_BLUE((long)3, "タグ青");

    /**
     * タグID(tag id).
     */
    private final Long id;

    /**
     * タグ名(tag name).
     */
    private final String name;

    /**
     * コンストラクタ(Constructor).
     * @param id タグID(tag id)
     * @param name タグ名(tag name)
     */
    private QuestionTag(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * タグID取得(Get tag id).
     * @return タグID(tag id)
     */
    public Long getId() {
        return this.id;
    }
    
    /**
     * タグ名取得(Get tag name).
     * @return タグ名(tag name)
     */
    public String getName() {
        return this.name;
    }
}
