package liveenglishclass.com.talkstar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import liveenglishclass.com.talkstar.core.ActivityManager;

public class IntroActivity extends AppCompatActivity {
    private ActivityManager actManager = ActivityManager.getInstance();


    private Intent intent;
    private ImageView im_intro_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        actManager.addActivity(this);

        //im_intro_logo = (ImageView) findViewById(R.id.im_intro_logo);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.intro_animation);
        //im_intro_logo.startAnimation(animation);
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



}
