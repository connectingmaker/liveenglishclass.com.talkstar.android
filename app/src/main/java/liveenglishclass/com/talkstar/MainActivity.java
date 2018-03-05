package liveenglishclass.com.talkstar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /******* FRAGMENT ***********/
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fr;


    /****** 음성인식 ***************/
    private Intent intent;
    private SpeechRecognizer mRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");


//        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
//        mRecognizer.setRecognitionListener(listener);
//        mRecognizer.startListening(intent);




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

    private void setFragment() {
        Log.d("test", "fragment 변환");
        this.fragmentTransaction.replace(R.id.viewFragment, this.fr);
        this.fragmentTransaction.commit();
    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {

        }

        @Override
        public void onResults(Bundle results) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            Log.d("test", rs[0]);
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };



}
