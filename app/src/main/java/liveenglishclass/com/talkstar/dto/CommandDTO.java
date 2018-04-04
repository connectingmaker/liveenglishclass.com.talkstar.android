package liveenglishclass.com.talkstar.dto;

import com.google.gson.annotations.SerializedName;

public class CommandDTO {
    @SerializedName("COMMAND_GROUP")
    private String COMMAND_GROUP = "";

    @SerializedName("COMMAND_NAME")
    private String COMMAND_NAME = "";



    public String getCOMMAND_GROUP()
    {
        return this.COMMAND_GROUP;
    }
    public void setCOMMAND_GROUP(String COMMAND_GROUP)
    {
        this.COMMAND_GROUP = COMMAND_GROUP;
    }

    public String getCOMMAND_NAME() {
        String tempCommandName = this.COMMAND_NAME.replaceAll(",","");
        return tempCommandName.substring(0, tempCommandName.length() - 2);
    }
    public void setCOMMAN_NAME(String COMMAND_NAME) { this.COMMAND_NAME = COMMAND_NAME.replaceAll(",",""); }

}
