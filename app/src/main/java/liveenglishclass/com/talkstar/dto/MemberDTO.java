package liveenglishclass.com.talkstar.dto;

/**
 * Created by kwangheejung on 2018. 3. 15..
 */

public class MemberDTO {
    public final String ERR_CODE;
    public final String ERR_MSG;

    public MemberDTO(String ERR_CODE, String ERR_MSG)
    {
        this.ERR_CODE = ERR_CODE;
        this.ERR_MSG = ERR_MSG;
    }
}
