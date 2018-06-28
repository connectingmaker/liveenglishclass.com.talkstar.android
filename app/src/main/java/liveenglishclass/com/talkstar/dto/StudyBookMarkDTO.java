package liveenglishclass.com.talkstar.dto;

import com.google.gson.annotations.SerializedName;

public class StudyBookMarkDTO {
    @SerializedName("CLASSES_CODE")
    public String CLASSES_CODE = "";

    @SerializedName("CHAPTER_CODE")
    public String CHAPTER_CODE = "";

    @SerializedName("STUDY_CODE")
    public String STUDY_CODE = "";

    @SerializedName("ORDERID")
    public Integer ORDERID = 0;


    @SerializedName("QUESTION_TYPE")
    public String QUESTION_TYPE = "";

    @SerializedName("VOICE_TYPE")
    public String VOICE_TYPE = "";


    @SerializedName("VOICE_FILE")
    public String VOICE_FILE = "";

    @SerializedName("ENGLISH_STRING")
    public String ENGLISH_STRING = "";

    @SerializedName("KOREA_STRING")
    public String KOREA_STRING = "";

    @SerializedName("EXPLANATION")
    public String EXPLANATION = "";

    @SerializedName("NEXT_STUDY")
    public String NEXT_STUDY = "";



    @SerializedName("ANSWER_ENGLISH")
    public String ANSWER_ENGLISH = "";

    @SerializedName("BOOKMARK_YN")
    public String BOOKMARK_YN = "";

    public String getCLAESS_CODE()
    {
        return this.CLASSES_CODE;
    }
    public void setCLAESS_CODE(String CLASSES_CODE)
    {
        this.CLASSES_CODE = CLASSES_CODE;
    }

    public String getCHAPTER_CODE()
    {
        return this.CHAPTER_CODE;
    }
    public void setCHAPTER_CODE(String CLASSES_CODE)
    {
        this.CHAPTER_CODE = CHAPTER_CODE;
    }

    public String getSTUDY_CODE()
    {
        return this.STUDY_CODE;
    }
    public void setSTUDY_CODE(String STUDY_CODE)
    {
        this.STUDY_CODE = STUDY_CODE;
    }

    public Integer getORDERID()
    {
        return this.ORDERID;
    }
    public void setORDERID(Integer ORDERID)
    {
        this.ORDERID = ORDERID;
    }

    public String getENGLISH_STRING()
    {
        return this.ENGLISH_STRING;
    }
    public void setENGLISH_STRING(String ENGLISH_STRING)
    {
        this.ENGLISH_STRING = ENGLISH_STRING;
    }

    public String getKOREA_STRING()
    {
        return this.KOREA_STRING;
    }
    public void setKOREA_STRING(String KOREA_STRING)
    {
        this.KOREA_STRING = KOREA_STRING;
    }









}
