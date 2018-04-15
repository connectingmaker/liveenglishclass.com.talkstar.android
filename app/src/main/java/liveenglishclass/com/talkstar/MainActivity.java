package liveenglishclass.com.talkstar;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import liveenglishclass.com.talkstar.core.ActivityManager;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.custom.CustormLoadingDialog;
import liveenglishclass.com.talkstar.dto.Contributor;
import liveenglishclass.com.talkstar.dto.MemberLoginDTO;
import liveenglishclass.com.talkstar.dto.VoiceSearchDTO;
import liveenglishclass.com.talkstar.util.BackPressCloseHandler;
import liveenglishclass.com.talkstar.util.Shared;
import liveenglishclass.com.talkstar.util.Util;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity  {
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


    private TextToSpeech myTTS_EN;
    private TextToSpeech myTTS_KR;

    private VoiceFragment voiceFragment;
    private StudyFragment studyFragment;
    private CommandFragment commandFragment;
    private SettingFragment settingFragment;

    private String fragmentCheck = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notification();


        actManager.addActivity(this);



        backPressCloseHandler = new BackPressCloseHandler(this);


        fr = new VoiceFragment();

        this.fragmentManager = getFragmentManager();
        this.fragmentTransaction = fragmentManager.beginTransaction();
        this.setFragment();

        myTTS_EN=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    myTTS_EN.setLanguage(Locale.ENGLISH);
                }
            }
        });

        myTTS_KR=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    myTTS_KR.setLanguage(Locale.KOREAN);
                }
            }
        });



    }

    public void tabClick(View v) {
        switch(v.getId()) {
            case R.id.tab01:
                voiceFragment = new VoiceFragment();
                fragmentCheck = "voice";
                break;

            case R.id.tab02:
                studyFragment = new StudyFragment();
                fragmentCheck = "study";
                break;

            case R.id.tab03:
                commandFragment = new CommandFragment();
                fragmentCheck = "command";
                break;

            case R.id.tab04:
                settingFragment = new SettingFragment();
                fragmentCheck = "setting";
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


        Log.d("test", Locale.US.toString());
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREA.toString());
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString());
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.US.toString());


        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
        mRecognizer.startListening(intent);

    }

    private void setFragment() {
        Log.d("test", "fragment 변환");
        switch(fragmentCheck) {
            case "voice":
                this.fragmentTransaction.replace(R.id.viewFragment, this.voiceFragment);
                this.fragmentTransaction.commit();
                break;
            case "study":
                this.fragmentTransaction.replace(R.id.viewFragment, this.studyFragment);
                this.fragmentTransaction.commit();
                break;
            case "command":
                this.fragmentTransaction.replace(R.id.viewFragment, this.commandFragment);
                this.fragmentTransaction.commit();
                break;
            case "setting":
                this.fragmentTransaction.replace(R.id.viewFragment, this.settingFragment);
                this.fragmentTransaction.commit();
                break;
        }

    }

    public RecognitionListener listener = new RecognitionListener() {
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

            final String searchName = rs[0];
            Log.d("test", searchName);

//            final CustormLoadingDialog loading = new CustormLoadingDialog(MainActivity.this);
//            loading.show();



            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                    apiService = retrofit.create(ApiService.class);

                    //loading.show();
                    Log.d("test", "전송");

                    Call<VoiceSearchDTO> call = apiService.voiceSearch("1111", searchName);
                    call.enqueue(new Callback<VoiceSearchDTO>() {
                        @Override
                        public void onResponse(Call<VoiceSearchDTO> call, Response<VoiceSearchDTO> response) {

                            VoiceSearchDTO voiceSearchDTO = response.body();
                            //loading.dismiss();

                            Log.d("test", voiceSearchDTO.ACTION_CODE);

                            switch(voiceSearchDTO.ACTION_CODE) {
                                case "A001":
                                    Toast.makeText(MainActivity.this, "반복", Toast.LENGTH_LONG).show();
                                    break;

                                case "A002":
                                    Toast.makeText(MainActivity.this, "다음 프로세스 진행", Toast.LENGTH_LONG).show();
                                    break;



                                case "A003":
                                    /*
                                    if (voiceSearchDTO.ENGLISH_FILE.equals("TTS")) {

                                    } else {
                                        myTTS.
                                    }
                                    */

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        ttsGreater21(voiceSearchDTO.ENGLISH);
                                    } else {
                                        ttsUnder20(voiceSearchDTO.ENGLISH);
                                    }

                                    Toast.makeText(MainActivity.this, "영어 = " + voiceSearchDTO.ENGLISH, Toast.LENGTH_LONG).show();
                                break;

                                case "A004":
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        ttsGreater21_KR("해당 명령어를 수업진행시만 가능합니다.");
                                    } else {
                                        ttsUnder20_KR("해당 명령어를 수업진행시만 가능합니다.");
                                    }
                                    Toast.makeText(MainActivity.this, "수업중단", Toast.LENGTH_LONG).show();
                                    break;

                                case "A005":
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        ttsGreater21_KR("해당 명령어를 수업진행시만 가능합니다.");
                                    } else {
                                        ttsUnder20_KR("해당 명령어를 수업진행시만 가능합니다.");
                                    }
                                    //Toast.makeText(MainActivity.this, "답이 궁금할 떄", Toast.LENGTH_LONG).show();
                                    break;

                                case "A999":
                                    String voiceReturn = voiceSearchDTO.RETURN_MSG;
                                    String username = Shared.getPerferences(MainActivity.this, "SESS_USERNAME");
                                    voiceReturn = voiceReturn.replace("[_NAME_]", username);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        ttsGreater21_KR(voiceReturn);
                                    } else {
                                        ttsUnder20_KR(voiceReturn);
                                    }
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call<VoiceSearchDTO> call, Throwable t) {
                            Log.d("test", "실패");
                        }
                    });


                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                }

            }.execute();


            //mRecognizer.startListening(intent);


            //Toast toast = Toast.makeText(getApplicationContext(), rs[0], Toast.LENGTH_LONG); toast.show();

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


    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        myTTS_EN.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId=this.hashCode() + "";
        myTTS_EN.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20_KR(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        myTTS_KR.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21_KR(String text) {
        String utteranceId=this.hashCode() + "";
        myTTS_KR.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }



}
