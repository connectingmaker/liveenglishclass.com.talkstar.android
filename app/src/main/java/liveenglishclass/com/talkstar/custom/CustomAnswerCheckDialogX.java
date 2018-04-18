package liveenglishclass.com.talkstar.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Image;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import liveenglishclass.com.talkstar.R;
import liveenglishclass.com.talkstar.StudyChapterQuestionActivity;

public class CustomAnswerCheckDialogX extends Dialog implements View.OnClickListener {
    private static SoundPool soundPool;
    private int sound_beep;

    private ImageButton answer_x_close, answer_x_next_btn;
    private Context context;

    public CustomAnswerCheckDialogX(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.customanswercheckdialogx);
        answer_x_close = (ImageButton) findViewById(R.id.answer_x_close);
        answer_x_close.setOnClickListener(this);



        getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.MATCH_PARENT);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.answer_x_close:
                this.dismiss();
                break;
        }

    }
}

