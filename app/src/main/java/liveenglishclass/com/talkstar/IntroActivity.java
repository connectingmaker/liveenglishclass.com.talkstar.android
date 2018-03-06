package liveenglishclass.com.talkstar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class IntroActivity extends AppCompatActivity {

    private ImageView im_intro_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        im_intro_logo = (ImageView) findViewById(R.id.im_intro_logo);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.intro_animation);
        im_intro_logo.startAnimation(animation);
        //view.startAnimation(animation);
    }

}
