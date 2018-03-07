package liveenglishclass.com.talkstar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import liveenglishclass.com.talkstar.core.ActivityManager;

public class MemberJoinActivity extends AppCompatActivity {
    private ActivityManager actManager = ActivityManager.getInstance();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_join);

        actManager.addActivity(this);
    }


    public void btnClickEvent(View v) {
        switch(v.getId()){
            case R.id.memberjoin_btn_complate:
                intent = new Intent(MemberJoinActivity.this, MemberJoinFinishActivity.class);
                break;
        }

        startActivity(intent);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        actManager.removeActivity(this);
    }
}
