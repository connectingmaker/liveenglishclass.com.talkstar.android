package liveenglishclass.com.talkstar.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import liveenglishclass.com.talkstar.IntroActivity;
import liveenglishclass.com.talkstar.MainActivity;
import liveenglishclass.com.talkstar.R;
import liveenglishclass.com.talkstar.util.Shared;


public class CustomExplain extends Dialog {
    private ImageView customexplain;

    public CustomExplain(Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.customexplain);



        getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.MATCH_PARENT);

    }


}
