
package liveenglishclass.com.talkstar.dto;

import com.google.gson.annotations.SerializedName;

public class StudyFinishResultDTO {


    @SerializedName("STAR_COUNT_NOW")
    public Integer STAR_COUNT_NOW = 0;

    @SerializedName("STAR_COUNT")
    public Integer STAR_COUNT = 0;

    @SerializedName("STAR_COUNT_YESTERDAY")
    public Integer STAR_COUNT_YESTERDAY = 0;

    @SerializedName("STAR_COUNT_YESTERDAY2")
    public Integer STAR_COUNT_YESTERDAY2 = 0;

    @SerializedName("STAR_COUNT_TODAY")
    public Integer STAR_COUNT_TODAY = 0;

    @SerializedName("PER")
    public Integer PER = 0;

    @SerializedName("CLASSES_CODE")
    public Integer CLASSES_CODE = 0;

    @SerializedName("CHAPTER_CODE")
    public Integer CHAPTER_CODE = 0;

    @SerializedName("CLASSES_NAME")
    public String CLASSES_NAME = "";

    @SerializedName("CHAPTER_NAME")
    public String CHAPTER_NAME = "";

    @SerializedName("LEARNING_NOTES")
    public String LEARNING_NOTES = "";


}
