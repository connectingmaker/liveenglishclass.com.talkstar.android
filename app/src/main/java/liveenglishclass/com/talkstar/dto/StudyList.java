package liveenglishclass.com.talkstar.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class StudyList {
    @SerializedName("err_code")
    public String err_code = "";



    @SerializedName("data")
    @Expose
    private ArrayList<StudyDTO> data = new ArrayList<>();

    /**
     * @return The contacts
     */
    public ArrayList<StudyDTO> getDatas() {
        return data;
    }

    /**
     * @param datas The contacts
     */
    public void setDatas(ArrayList<StudyDTO> datas) {
        this.data = datas;
    }
}
