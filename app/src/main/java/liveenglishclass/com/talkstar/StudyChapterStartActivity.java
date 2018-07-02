package liveenglishclass.com.talkstar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import liveenglishclass.com.talkstar.core.ActivityManager;

public class StudyChapterStartActivity extends AppCompatActivity {
    private Intent intent;


    private String classesCode = "";
    private String chapterCode = "";
    private String chapterName = "";
    private String chapterLearning = "";
    private String chapterOrder = "";

    private ActivityManager actManager = ActivityManager.getInstance();



    private TextView activity_study_chapter_title, activity_study_chapter_learning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_chapter_start);

        actManager.addActivity(this);

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
            chapterOrder = (String) b.get("chapterOrder");

            activity_study_chapter_title.setText(chapterName);
            activity_study_chapter_learning.setText(chapterLearning);



            Log.d("test", classesCode + "///" + chapterCode + "///" + chapterName + "///" + chapterLearning);


        } else {

        }
    }

    public void chapterOnClick(View v) {
        switch(v.getId()) {
            case R.id.activity_studychapterstart_start_btn:

//                intent = new Intent(StudyChapterStartActivity.this, StudyChapterQuestionActivity_New.class);
//
//                intent.putExtra("classesCode", classesCode);
//                intent.putExtra("chapterCode", chapterCode);
//                intent.putExtra("chapterOrder", chapterOrder);
//                intent.putExtra("bookmark", "N");
//
//                startActivity(intent);

                Intent intent = new Intent(getApplicationContext(), StudyFinishActivity.class);
                intent.putExtra("classesCode", classesCode);
                intent.putExtra("chapterCode", chapterCode);


                startActivity(intent);
//                finish();
                break;
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        actManager.removeActivity(this);
    }
}
