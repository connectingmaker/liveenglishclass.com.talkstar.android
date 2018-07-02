package liveenglishclass.com.talkstar.dto;

import com.google.gson.annotations.SerializedName;

public class MypageDTO {@SerializedName("CLASSES_NAME")
public String CLASSES_NAME = "";

    @SerializedName("CHAPTER_NAME")
    public String CHAPTER_NAME = "";

    @SerializedName("CHAPTER_ALL")
    public Integer CHAPTER_ALL = 0;


    @SerializedName("USER_CHAPTER_COMPLATE")
    public Integer USER_CHAPTER_COMPLATE = 0;

    @SerializedName("STUDY_CNT")
    public Integer STUDY_CNT = 0;

    @SerializedName("QUESTION_CNT")
    public Integer QUESTION_CNT = 0;

    @SerializedName("USER_QUESTION_CNT")
    public String USER_QUESTION_CNT = "";

    @SerializedName("ENGLISH1")
    public String ENGLISH1 = "";

    @SerializedName("ENGLISH2")
    public String ENGLISH2 = "";




}
