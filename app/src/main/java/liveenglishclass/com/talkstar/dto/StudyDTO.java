package liveenglishclass.com.talkstar.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudyDTO {

    @SerializedName("CLASSES_CODE")
    private String CLASSES_CODE = "";

    @SerializedName("CLASSES_NAME")
    private String CLASSES_NAME = "";


    @SerializedName("CLASSES_LEVEL")
    private String CLASSES_LEVEL = "";

    @SerializedName("USER_Q_CNT")
    private Integer USER_Q_CNT = 0;

    @SerializedName("PART_CNT")
    private Integer PART_CNT = 0;

    @SerializedName("TOTAL_PER")
    private Integer TOTAL_PER = 0;



    public String getClassCode()
    {
        return this.CLASSES_CODE;
    }

    public void setClassCode(String CLASSES_CODE)
    {
        this.CLASSES_CODE = CLASSES_CODE;
    }

}