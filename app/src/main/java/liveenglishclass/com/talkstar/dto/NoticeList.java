package liveenglishclass.com.talkstar.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NoticeList {
    @SerializedName("err_code")
    public String err_code = "";

    @SerializedName("data")
    @Expose
    private ArrayList<NoticeDTO> data = new ArrayList<>();

    /**
     * @return The contacts
     */
    public ArrayList<NoticeDTO> getDatas() {
        return data;
    }

    /**
     * @param datas The contacts
     */
    public void setDatas(ArrayList<NoticeDTO> datas) {
        this.data = datas;
    }

}
