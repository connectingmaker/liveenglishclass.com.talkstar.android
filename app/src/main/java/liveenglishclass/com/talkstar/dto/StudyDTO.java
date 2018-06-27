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

    @SerializedName("PER_ORDER")
    private Integer PER_ORDER = 0;



    public String getClassCode()
    {
        return this.CLASSES_CODE;
    }
    public void setClassCode(String CLASSES_CODE)
    {
        this.CLASSES_CODE = CLASSES_CODE;
    }

    public String getClassName() { return this.CLASSES_NAME; }
    public void setClassName(String CLASSES_NAME) {
        this.CLASSES_NAME = CLASSES_NAME;
    }

    public String getClassLevel() {
        return this.CLASSES_LEVEL;
    }

    public void setClassLevel(String CLASSES_LEVEL) {
        this.CLASSES_LEVEL = CLASSES_LEVEL;
    }

    public Integer getUserQCnt()
    {
        return this.USER_Q_CNT;
    }

    public void setUserQCnt(Integer USER_Q_CNT) {
        this.USER_Q_CNT = USER_Q_CNT;
    }

    public Integer getPartCnt()
    {
        return this.PART_CNT;
    }

    public void setPartCnt(Integer PART_CNT)
    {
        this.PART_CNT = PART_CNT;
    }


    public Integer getTotalPer()
    {
        return this.TOTAL_PER;
    }

    public void setTotalPer(Integer PARTOTAL_PERT_CNT)
    {
        this.TOTAL_PER = TOTAL_PER;
    }

    public Integer getPerOrder()
    {
        return this.PER_ORDER;
    }

}