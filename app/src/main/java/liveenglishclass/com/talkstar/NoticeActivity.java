package liveenglishclass.com.talkstar;

/**
 * Created by jccho on 2018. 3. 6..
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NoticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
    }

    public void btnClickEvent(View v) {
        switch (v.getId()) {
            case R.id.preview:
                onBackPressed();
                break;
        }
    }
}
