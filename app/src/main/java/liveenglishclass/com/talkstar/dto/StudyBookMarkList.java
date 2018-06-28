package liveenglishclass.com.talkstar.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StudyBookMarkList {
    @SerializedName("err_code")
    public String err_code = "";



    @SerializedName("data")
    @Expose
    private ArrayList<StudyBookMarkDTO> data = new ArrayList<>();

    /**
     * @return The contacts
     */
    public ArrayList<StudyBookMarkDTO> getDatas() {
        return data;
    }

    /**
     * @param datas The contacts
     */
    public void setDatas(ArrayList<StudyBookMarkDTO> datas) {
        this.data = datas;
    }
}
