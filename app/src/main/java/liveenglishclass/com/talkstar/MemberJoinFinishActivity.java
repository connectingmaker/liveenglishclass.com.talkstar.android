package liveenglishclass.com.talkstar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import liveenglishclass.com.talkstar.core.ActivityManager;

public class MemberJoinFinishActivity extends AppCompatActivity {
    private ActivityManager actManager = ActivityManager.getInstance();
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_join_finish);

        actManager.addActivity(this);

    }

    public void btnEventClick(View v) {
        switch(v.getId()) {
            case R.id.memberjoinfinish_btn_start:


                /**** 진행된 Activity 전체 삭제 *****/
                actManager.finishAllActivity();


                intent = new Intent(MemberJoinFinishActivity.this, MainActivity.class);
                startActivity(intent);
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
