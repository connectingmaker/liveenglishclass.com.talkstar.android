package liveenglishclass.com.talkstar.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StudyChapterList {
    @SerializedName("err_code")
    public String err_code = "";



    @SerializedName("data")
    @Expose
    private ArrayList<StudyChapterDTO> data = new ArrayList<>();

    /**
     * @return The contacts
     */
    public ArrayList<StudyChapterDTO> getDatas() {
        return data;
    }

    /**
     * @param datas The contacts
     */
    public void setDatas(ArrayList<StudyChapterDTO> datas) {
        this.data = datas;
    }
}
