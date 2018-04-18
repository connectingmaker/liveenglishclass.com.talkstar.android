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

    @SerializedName("SEQ")
    public Integer SEQ;


    @SerializedName("ACTION_CODE")
    public String ACTION_CODE = "";

    @SerializedName("COMMAND_VOICE")
    public String COMMAND_VOICE = "";

    @SerializedName("COMMAND_RETURN")
    public String COMMAND_RETURN = "";


}
