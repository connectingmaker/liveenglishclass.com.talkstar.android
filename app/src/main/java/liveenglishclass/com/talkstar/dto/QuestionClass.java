package liveenglishclass.com.talkstar.dto;

public class QuestionClass {
    private String questionValue;
    private String nextStudy;
    private String answerValue;

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

    public void set_answerValue(String answerValue) { this.answerValue = answerValue; }

    public String get_answerValue()
    {
        return this.answerValue;
    }



}
