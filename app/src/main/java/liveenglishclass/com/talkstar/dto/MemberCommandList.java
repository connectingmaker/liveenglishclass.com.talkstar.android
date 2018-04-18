package liveenglishclass.com.talkstar.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MemberCommandList {
    @SerializedName("err_code")
    public String err_code = "";



    @SerializedName("data")
    @Expose
    private ArrayList<MemberCommandDTO> data = new ArrayList<>();

    /**
     * @return The contacts
     */
    public ArrayList<MemberCommandDTO> getDatas() {
        return data;
    }

    /**
     * @param datas The contacts
     */
    public void setDatas(ArrayList<MemberCommandDTO> datas) {
        this.data = datas;
    }
}
