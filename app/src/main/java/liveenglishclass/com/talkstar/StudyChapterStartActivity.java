package liveenglishclass.com.talkstar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class StudyChapterStartActivity extends AppCompatActivity {
    private Intent intent;


    private String classesCode = "";
    private String chapterCode = "";
    private String chapterName = "";
    private String chapterLearning = "";





    private TextView activity_study_chapter_title, activity_study_chapter_learning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_chapter_start);

        activity_study_chapter_title = (TextView) findViewById(R.id.activity_study_chapter_title);
        activity_study_chapter_learning = (TextView) findViewById(R.id.activity_study_chapter_learning);


        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            classesCode =(String) b.get("classesCode");
            chapterCode = (String) b.get("chapterCode");
            chapterName = (String) b.get("chapterName");
            chapterLearning = (String) b.get("chapterLearning");

            activity_study_chapter_title.setText(chapterName);
            activity_study_chapter_learning.setText(chapterLearning);



            Log.d("test", classesCode + "///" + chapterCode + "///" + chapterName + "///" + chapterLearning);


        } else {

        }
    }

    public void chapterOnClick(View v) {
        switch(v.getId()) {
            case R.id.activity_studychapterstart_start_btn:
                intent = new Intent(StudyChapterStartActivity.this, StudyChapterQuestionActivity.class);

                intent.putExtra("classesCode", classesCode);
                intent.putExtra("chapterCode", chapterCode);

                startActivity(intent);
                break;
        }
    }
}
