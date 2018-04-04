package liveenglishclass.com.talkstar.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CommandList {
    @SerializedName("err_code")
    public String err_code = "";



    @SerializedName("data")
    @Expose
    private ArrayList<CommandDTO> data = new ArrayList<>();

    /**
     * @return The contacts
     */
    public ArrayList<CommandDTO> getDatas() {
        return data;
    }

    /**
     * @param datas The contacts
     */
    public void setDatas(ArrayList<CommandDTO> datas) {
        this.data = datas;
    }
}
