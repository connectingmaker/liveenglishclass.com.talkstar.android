package liveenglishclass.com.talkstar.dto;

/**
 * Created by kwangheejung on 2018. 3. 19..
 */

public class MemberLoginDTO {
    public final String ERR_CODE;
    public final String UID;
    public final String USEREMAIL;
    public final String USERNAME;

    public MemberLoginDTO(String ERR_CODE, String UID, String USEREMAIL, String USERNAME)
    {
        this.ERR_CODE = ERR_CODE;
        this.UID = UID;
        this.USEREMAIL = USEREMAIL;
        this.USERNAME = USERNAME;
    }
}
