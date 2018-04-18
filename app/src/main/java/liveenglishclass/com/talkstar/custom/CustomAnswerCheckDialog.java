package liveenglishclass.com.talkstar.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
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

public class CustomAnswerCheckDialog extends Dialog implements View.OnClickListener {
    private static SoundPool soundPool;
    private int sound_beep;

    private LinearLayout answer_o, answer_x;

    private ImageButton answer_o_close, answer_x_next_btn;
    private Context context;

    public CustomAnswerCheckDialog(Context context) {
        super(context);
        context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.customanswercheckdialog);

        answer_o_close = (ImageButton) findViewById(R.id.answer_o_close);

        answer_o_close.setOnClickListener(this);

        getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.MATCH_PARENT);
    }


    public void soundPlay()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(8).build();
        }
        else {
            soundPool = new SoundPool(8, AudioManager.STREAM_NOTIFICATION, 0);
        }

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sampleId, 1f, 1f, 0, 0, 1f);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.answer_o_close:
                this.dismiss();
                break;
        }

    }
}
