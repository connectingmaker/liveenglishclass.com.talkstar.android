package liveenglishclass.com.talkstar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class QnaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna);
    }

    public void btnClickEvent(View v) {
        switch (v.getId()) {
            case R.id.preview:
                onBackPressed();
                break;
        }
    }

}
