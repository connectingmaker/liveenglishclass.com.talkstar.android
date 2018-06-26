package liveenglishclass.com.talkstar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class StudyChapterQuestionActivity_boxLayout_Title extends LinearLayout {

    public StudyChapterQuestionActivity_boxLayout_Title(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }


    public StudyChapterQuestionActivity_boxLayout_Title(Context context) {
        super(context);
    }

    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_study_chapter_question_new_boxlayout_title,this,true);
    }
}
