package liveenglishclass.com.talkstar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StudyChapterStartActivity extends AppCompatActivity {
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_chapter_start);
    }

    public void chapterOnClick(View v) {
        switch(v.getId()) {
            case R.id.activity_studychapterstart_start_btn:
                intent = new Intent(StudyChapterStartActivity.this, StudyChapterQuestionActivity.class);
                startActivity(intent);
                break;
        }
    }
}
