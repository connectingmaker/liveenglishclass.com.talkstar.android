package liveenglishclass.com.talkstar.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kwangheejung on 2018. 3. 19..
 */

public class VoiceSearchDTO {
    /*
    public final String GROUP_CODE;
    public final String ACTION_CODE;
    public final String ACTION_NAME;
    public final String COMMAND_CODE;
    public final String COMMAND_NAME;
    public final String score;
    public final String ENGLISH;
    public final String ENGLISH_FILE;
    */

    @SerializedName("GROUP_CODE")
    public String GROUP_CODE = "";

    @SerializedName("ACTION_CODE")
    public String ACTION_CODE = "";

    @SerializedName("ACTION_NAME")
    public String ACTION_NAME = "";

    @SerializedName("COMMAND_CODE")
    public String COMMAND_CODE = "";

    @SerializedName("COMMAND_NAME")
    public String COMMAND_NAME = "";

    @SerializedName("score")
    public String score = "";

    @SerializedName("ENGLISH")
    public String ENGLISH = "";

    @SerializedName("ENGLISH_FILE")
    public String ENGLISH_FILE = "";

    @SerializedName("RETURN_MSG")
    public String RETURN_MSG = "";


}
