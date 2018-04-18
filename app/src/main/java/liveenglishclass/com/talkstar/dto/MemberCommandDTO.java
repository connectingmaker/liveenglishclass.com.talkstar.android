package liveenglishclass.com.talkstar.dto;

import com.google.gson.annotations.SerializedName;

public class MemberCommandDTO {
    @SerializedName("SEQ")
    private Integer SEQ;

    @SerializedName("ACTION_CODE")
    private String ACTION_CODE = "";

    @SerializedName("COMMAND_VOICE")
    private String COMMAND_VOICE = "";

    @SerializedName("COMMAND_RETURN")
    private String COMMAND_RETURN = "";



    public Integer getSEQ() {
        return this.SEQ;
    }

    public void setSEQ(Integer SEQ) {
        this.SEQ = SEQ;
    }

    public String getACTION_CODE()
    {
        return this.ACTION_CODE;
    }

    public void setACTION_CODE(String ACTION_CODE)
    {
        this.ACTION_CODE = ACTION_CODE;
    }

    public String getCOMMAND_VOICE()
    {
        return this.COMMAND_VOICE;
    }

    public void setCOMMAND_VOICE(String COMMAND_VOICE) {
        this.COMMAND_VOICE = COMMAND_VOICE;
    }


    public String getCOMMAND_RETURN()
    {
        return this.COMMAND_RETURN;
    }

    public void setCOMMAND_RETURN(String COMMAND_RETURN) {
        this.COMMAND_RETURN = COMMAND_RETURN;
    }



}
