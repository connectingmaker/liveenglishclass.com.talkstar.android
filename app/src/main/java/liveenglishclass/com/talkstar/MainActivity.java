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

import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.dto.Contributor;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

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


    private Button main_btn_mic;


    private void sampleNetWork()
    {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);
                Call<List<Contributor>> call = apiService.contributors("square", "retrofit");

                call.enqueue(new Callback<List<Contributor>>() {
                    @Override
                    public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                        List<Contributor> contributors = response.body();
                        // 받아온 리스트를 순회하면서
                        for (Contributor contributor : contributors) {

                            Log.d("test", contributor.login);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Contributor>> call, Throwable t) {

                    }
                });

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

        }.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notification();


        sampleNetWork();

//        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");


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

    public void micClickEvent(View v) {
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");


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
            //Log.d("test", rs[0]);

            Toast toast = Toast.makeText(getApplicationContext(), rs[0], Toast.LENGTH_LONG); toast.show();

        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };



    private void notification()
    {
        String token = FirebaseInstanceId.getInstance().getToken();

        Log.d("test","token="+token);


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



}
