package liveenglishclass.com.talkstar.dto;

import com.google.gson.annotations.SerializedName;

public class NoticeDTO {

    @SerializedName("NOTICE_TITLE")
    private String NOTICE_TITLE = "";

    @SerializedName("NOTICE_CONTENT")
    private String NOTICE_CONTENT = "";


    public String getNOTICE_TITLE()
    {
        return this.NOTICE_TITLE;
    }
    public void setNOTICE_TITLE(String NOTICE_TITLE)
    {
        this.NOTICE_TITLE = NOTICE_TITLE;
    }

    public String getNOTICE_CONTENT() { return this.NOTICE_CONTENT; }
    public void setNOTICE_CONTENT(String NOTICE_CONTENT) {
        this.NOTICE_CONTENT = NOTICE_CONTENT;
    }

}
