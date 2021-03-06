package liveenglishclass.com.talkstar.dto;

import com.google.gson.annotations.SerializedName;

public class StudyStartDTO {
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

    @SerializedName("QUESTION_TYPE")
    public String QUESTION_TYPE = "";


    @SerializedName("VOICE_TYPE")
    public String VOICE_TYPE = "";

    @SerializedName("VOICE")
    public String VOICE = "";

    @SerializedName("VOICE_FILE")
    public String VOICE_FILE = "";

    @SerializedName("ENGLISH_STRING")
    public String ENGLISH_STRING = "";

    @SerializedName("KOREA_STRING")
    public String KOREA_STRING = "";


    @SerializedName("NEXT_STUDY")
    public String NEXT_STUDY = "";

    @SerializedName("PREV_STUDY")
    public String PREV_STUDY = "";

    @SerializedName("ANSWER_ENGLISH")
    public String ANSWER_ENGLISH = "";

    @SerializedName("PER")
    public Integer PER = 0;

    @SerializedName("COMMAND_NAME")
    public String COMMAND_NAME = "";



}
