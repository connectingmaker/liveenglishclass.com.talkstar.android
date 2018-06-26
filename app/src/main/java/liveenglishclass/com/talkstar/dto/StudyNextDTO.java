package liveenglishclass.com.talkstar.dto;

import com.google.gson.annotations.SerializedName;

public class StudyNextDTO {
    @SerializedName("CLASSES_CODE")
    public String CLASSES_CODE = "";

    @SerializedName("CHAPTER_CODE")
    public String CHAPTER_CODE = "";

    @SerializedName("PART_CODE")
    public String PART_CODE = "";


    @SerializedName("ORDERID")
    public Integer ORDERID = 0;

    @SerializedName("GUBUN_CODE")
    public String GUBUN_CODE = "";


    @SerializedName("COMMAND_TYPE")
    public Integer COMMAND_TYPE = 0;

    @SerializedName("ENGLISH_STRING")
    public String ENGLISH_STRING = "";

    @SerializedName("KOREA_STRING")
    public String KOREA_STRING = "";

    @SerializedName("EXPLANATION")
    public String EXPLANATION = "";

    @SerializedName("NEXT_STUDY")
    public String NEXT_STUDY = "";

    @SerializedName("NEXT_STEP")
    public String NEXT_STEP = "";

    @SerializedName("PREV_STUDY")
    public String PREV_STUDY = "";

}
