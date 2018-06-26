package liveenglishclass.com.talkstar.dto;

public class QuestionClass {
    private String questionValue;
    private String nextStudy;
    private String prevStudy;
    private String answerValue;
    private Boolean voiceCheck = false;

    public String get_quetionValue() {
        return this.questionValue;
    }

    public void set_quetionValue(String questionValue) {
        this.questionValue = questionValue;
    }


    public String get_nextStudy()
    {
        return this.nextStudy;
    }

    public void set_nextStudy(String nextStudy)
    {
        this.nextStudy = nextStudy;
    }

    public String get_prevStudy()
    {
        return this.prevStudy;
    }

    public void set_prevStudy(String prevStudy)
    {
        this.prevStudy = prevStudy;
    }

    public void set_answerValue(String answerValue) { this.answerValue = answerValue; }

    public String get_answerValue()
    {
        return this.answerValue;
    }


    public void set_voiceCheck(Boolean voiceCheck) {
        this.voiceCheck = voiceCheck;
    }
    public Boolean get_voiceCheck() {
        return this.voiceCheck;
    }
}
