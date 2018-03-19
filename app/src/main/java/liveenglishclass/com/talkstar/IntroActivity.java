package liveenglishclass.com.talkstar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.iid.FirebaseInstanceId;

import liveenglishclass.com.talkstar.core.ActivityManager;
import liveenglishclass.com.talkstar.util.Shared;
import liveenglishclass.com.talkstar.util.Util;

public class IntroActivity extends AppCompatActivity {
    private ActivityManager actManager = ActivityManager.getInstance();


    private Intent intent;
    private ImageView im_intro_logo;

    private String debugTag = "introActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        actManager.addActivity(this);

        Log.d("test", "intro");
        Util.getStringValue(this, "E01");

        tokenUpdate();

        im_intro_logo = (ImageView) findViewById(R.id.im_intro_logo);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.intro_animation);
        im_intro_logo.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(debugTag, "애니메이션 시작");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(!Shared.getPerferences(IntroActivity.this, "SESS_UID").equals("") == true) {
                    intent = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //view.startAnimation(animation);
    }

    public void btnClickEvent(View v) {

        switch(v.getId())
        {
            case R.id.intro_btn_login:
                intent = new Intent(IntroActivity.this, LoginActivity.class);
                break;

            case R.id.intro_btn_memberjoin:
                intent = new Intent(IntroActivity.this, MemberJoinActivity.class);
                break;

        }


        startActivity(intent);
        //overridePendingTransition(R.anim.anim_slide_up, R.anim.anim_slide_down);
        //finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        actManager.removeActivity(this);
    }


    private void tokenUpdate()
    {
        if(Shared.getPerferences(this, "SESS_TOKEN").equals("") == true) {
            Shared.savePreferences(this,"SESS_TOKEN", Util.getToken());
        }
    }


}
