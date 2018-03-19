package liveenglishclass.com.talkstar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import liveenglishclass.com.talkstar.core.ActivityManager;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.dto.Contributor;
import liveenglishclass.com.talkstar.util.BackPressCloseHandler;
import liveenglishclass.com.talkstar.util.Util;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler;

    private ActivityManager actManager = ActivityManager.getInstance();
    private final String degubTag = "MainActivity";
    private Retrofit retrofit;
    ApiService apiService;


    /******* FRAGMENT ***********/
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fr;


    /****** 음성인식 ***************/
    private Intent intent;
    private SpeechRecognizer mRecognizer;



    private boolean mIsRecording = false;
    private Handler mHandler;
    private ViewVoice mViewVoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notification();


        actManager.addActivity(this);



        backPressCloseHandler = new BackPressCloseHandler(this);


        fr = new HomeFragment();

        this.fragmentManager = getFragmentManager();
        this.fragmentTransaction = fragmentManager.beginTransaction();
        this.setFragment();
    }

    public void tabClick(View v) {
        switch(v.getId()) {
            case R.id.tab01:
                fr = new HomeFragment();
                break;

            case R.id.tab02:
                fr = new StudyFragment();
                break;

            case R.id.tab03:
                fr = new CommandFragment();
                break;

            case R.id.tab04:
                fr = new SettingFragment();
                break;
        }



        this.fragmentManager = getFragmentManager();
        this.fragmentTransaction = this.fragmentManager.beginTransaction();

        this.setFragment();

    }

    public void micClickEvent(View v) {
        fr = new VoiceFragment();
        this.fragmentManager = getFragmentManager();
        this.fragmentTransaction = this.fragmentManager.beginTransaction();

        this.setFragment();


        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "eu-US");


        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
        mRecognizer.startListening(intent);

    }

    private void setFragment() {
        Log.d("test", "fragment 변환");
        this.fragmentTransaction.replace(R.id.viewFragment, this.fr);
        this.fragmentTransaction.commit();
    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Log.d("test", "onReadyForSpeech");
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.d("test", "onBeginningOfSpeech");
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            Log.d("test", "onRmsChanged");
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            Log.d("test", "onBufferReceived");
        }

        @Override
        public void onEndOfSpeech() {
            Log.d("test", "onEndOfSpeech");
        }

        @Override
        public void onError(int error) {
            Log.d("test", "onError");
            //Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG); toast.show();
        }

        @Override
        public void onResults(Bundle results) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);


            for(int i = 0; i<rs.length; i++) {
                Log.d("test", rs[i].toString());
            }


            Toast toast = Toast.makeText(getApplicationContext(), rs[0], Toast.LENGTH_LONG); toast.show();

        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };


    public void appLogout()
    {
        /**** 진행된 Activity 전체 삭제 *****/
        actManager.finishAllActivity();


        intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }



    private void notification()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.notification_channel_id);
            String channelName = getString(R.string.notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        actManager.removeActivity(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }




}
