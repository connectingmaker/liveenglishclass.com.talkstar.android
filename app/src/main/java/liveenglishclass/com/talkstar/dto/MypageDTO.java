package liveenglishclass.com.talkstar.dto;

import com.google.gson.annotations.SerializedName;

public class MypageDTO {
    @SerializedName("CLASSES_NAME")
    private String CLASSES_NAME = "";

    @SerializedName("CHAPTER_NAME")
    private String CHAPTER_NAME = "";

    @SerializedName("CHAPTER_ALL")
    private Integer CHAPTER_ALL = 0;


    @SerializedName("USER_CHAPTER_COMPLATE")
    private Integer USER_CHAPTER_COMPLATE = 0;

    @SerializedName("STUDY_CNT")
    private Integer STUDY_CNT = 0;

    @SerializedName("QUESTION_CNT")
    private Integer QUESTION_CNT = 0;

    @SerializedName("USER_QUESTION_CNT")
    private String USER_QUESTION_CNT = "";

    @SerializedName("ENGLISH1")
    private String ENGLISH1 = "";

    @SerializedName("ENGLISH2")
    private String ENGLISH2 = "";



}
