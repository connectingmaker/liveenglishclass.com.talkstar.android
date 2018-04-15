package liveenglishclass.com.talkstar.dto;

import com.google.gson.annotations.SerializedName;

public class StudyChapterDTO {

    @SerializedName("CLASSES_CODE")
    private String CLASSES_CODE = "";

    @SerializedName("CLASSES_NAME")
    private String CLASSES_NAME = "";

    @SerializedName("CLASSES_LEVEL")
    private String CLASSES_LEVEL = "";


    @SerializedName("CHAPTER_CODE")
    private String CHAPTER_CODE = "";

    @SerializedName("CHAPTER_NAME")
    private String CHAPTER_NAME = "";


    @SerializedName("SENTENCE")
    private String SENTENCE = "";

    @SerializedName("LEARNING_NOTES")
    private String LEARNING_NOTES = "";


    public String getClassName() {
        return this.CLASSES_NAME;
    }

    public String getClassLevel() {
        return this.CLASSES_LEVEL;
    }

    public String getChapterCode(){
        return this.CHAPTER_CODE;
    }

    public String getChapterName() {
        return this.CHAPTER_NAME;
    }

    public String getSentence(){
        return this.SENTENCE;
    }

    public String getLearningNotes()
    {
        return this.LEARNING_NOTES;
    }








}