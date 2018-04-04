package liveenglishclass.com.talkstar.dto;

public class QnaDTO {
    public final String ERR_CODE;
    public final String UID;
    public final String USEREMAIL;
    public final String USERNAME;
    public final String QUESTION;

    public QnaDTO(String ERR_CODE, String UID, String USEREMAIL, String USERNAME, String QUESTION)
    {
        this.ERR_CODE = ERR_CODE;
        this.UID = UID;
        this.USEREMAIL = USEREMAIL;
        this.USERNAME = USERNAME;
        this.QUESTION = QUESTION;
    }
}
