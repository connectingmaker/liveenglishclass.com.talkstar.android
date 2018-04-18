package liveenglishclass.com.talkstar;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import liveenglishclass.com.talkstar.adapter.StudyChapterAdapter;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.custom.CustomAnswerCheckDialog;
import liveenglishclass.com.talkstar.custom.CustomAnswerCheckDialogX;
import liveenglishclass.com.talkstar.custom.CustormLoadingDialog;
import liveenglishclass.com.talkstar.dto.QuestionClass;
import liveenglishclass.com.talkstar.dto.StudyChapterList;
import liveenglishclass.com.talkstar.dto.StudyDTO;
import liveenglishclass.com.talkstar.dto.StudyNextDTO;
import liveenglishclass.com.talkstar.dto.StudyStartDTO;
import liveenglishclass.com.talkstar.dto.VoiceSearchDTO;
import liveenglishclass.com.talkstar.util.Shared;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudyChapterQuestionActivity extends AppCompatActivity {

    private final String debugTag = "StudyChapterActivity";
    private Retrofit retrofit;
    ApiService apiService;


    private String classesCode = "";
    private String chapterCode = "";
    private String partCode = "";
    private Integer orderId = 0;
    private String questionType = "";


    private TextToSpeech myTTS_EN;
    private TextToSpeech myTTS_KR;

    private TextView tv_explanation;
    private TextView tv_answer;
    private ImageButton next_btn;

    private LinearLayout line;

    private Integer QuestionAnsWerCnt = 0;
    private Boolean nextVoice = false;
    private Boolean questionVoice = false;

    /****** 음성인식 ***************/
    private Intent intent;
    private SpeechRecognizer mRecognizer;

    private Button answerBtn;

    private QuestionClass qc;

    private CustomAnswerCheckDialog cacd;
    private CustomAnswerCheckDialogX cacdx;

    private static SoundPool soundPool;
    private int sound_beep;

    private Handler mHandler;
    private Runnable mRunnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_chapter_question);


        cacd = new CustomAnswerCheckDialog(this);
        cacd.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        cacd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        cacdx = new CustomAnswerCheckDialogX(this);
        cacdx.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        cacdx.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        answerBtn = (Button) findViewById(R.id.answerBtn);
        tv_explanation = (TextView) findViewById(R.id.tv_explanation);
        next_btn = (ImageButton) findViewById(R.id.next_btn);
        tv_answer = (TextView) findViewById(R.id.tv_answer);
        line = (LinearLayout) findViewById(R.id.line);

        qc = new QuestionClass();

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
                Log.d("test", "status="+String.valueOf(status));
                if(status != TextToSpeech.ERROR) {
                    myTTS_KR.setLanguage(Locale.KOREAN);
                }
            }

        });

        if(b!=null)
        {
            classesCode =(String) b.get("classesCode");
            chapterCode = (String) b.get("chapterCode");

            this._dataSend();

        } else {

        }



    }


    private void _dataSend()
    {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);

                Call<StudyStartDTO> call = apiService.StudyStart("1111", classesCode, chapterCode);
                call.enqueue(new Callback<StudyStartDTO>() {

                    @Override
                    public void onResponse(Call<StudyStartDTO> call, Response<StudyStartDTO> response) {
                        //Log.d(debugTag, response.body().)
                        StudyStartDTO studyDTO = response.body();


                        String voiceType = studyDTO.VOICE_TYPE;
                        String voice = studyDTO.VOICE;
                        String temp[] = studyDTO.NEXT_STUDY.split("///");
                        questionType = studyDTO.QUESTION_TYPE;

                        partCode = temp[0];
                        orderId = Integer.parseInt(temp[1]);
                        tv_explanation.setText(voice);


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            switch (voiceType) {
                                case "KR":
                                    Log.d("test", "OK");
                                    ttsGreater21_KR(voice);
                                    break;
                                case "EN":
                                    ttsGreater21(voice);
                                    break;

                            }

                        } else {

                            switch (voiceType) {
                                case "KR":
                                    ttsUnder20_KR(voice);
                                    break;
                                case "EN":
                                    ttsUnder20(voice);
                                    break;

                            }

                        }




                    }

                    @Override
                    public void onFailure(Call<StudyStartDTO> call, Throwable t) {

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


    public void _nextSend()
    {
        final CustormLoadingDialog dialog = new CustormLoadingDialog(this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);

                Call<StudyStartDTO> call = apiService.StudyNext("1111", classesCode, chapterCode, partCode, orderId);
                call.enqueue(new Callback<StudyStartDTO>() {

                    @Override
                    public void onResponse(Call<StudyStartDTO> call, Response<StudyStartDTO> response) {
                        //Log.d(debugTag, response.body().)
                        dialog.dismiss();
                        StudyStartDTO studyDTO = response.body();



                        String voiceType = studyDTO.VOICE_TYPE;
                        String voice = studyDTO.VOICE;
                        String temp[] = studyDTO.NEXT_STUDY.split("///");
                        questionType = studyDTO.QUESTION_TYPE;
                        String english_string = studyDTO.ENGLISH_STRING;

                        QuestionAnsWerCnt = 0;


                        qc.set_nextStudy(studyDTO.NEXT_STUDY);
                        qc.set_quetionValue(english_string);

                        partCode = temp[0];

                        if(questionType.equals("Q")) {
                            line.setVisibility(View.VISIBLE);
                            nextVoice = true;
                            answerBtn.setVisibility(View.VISIBLE);

                            QuestionAnsWerCnt = 0;

                            tv_answer.setText("발음평가 해주세요.");
                        } else {
                            answerBtn.setVisibility(View.GONE);
                        }

                        orderId = Integer.parseInt(temp[1]);
                        tv_explanation.setText(voice);



                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            switch (voiceType) {
                                case "KR":
                                    if(questionType.equals("Q")) {
                                        ttsGreater21(english_string);
                                    } else {
                                        ttsGreater21_KR(voice);
                                    }

                                    break;
                                case "EN":
                                    if(questionType.equals("Q")) {
                                        ttsGreater21(english_string);
                                    } else {
                                        ttsGreater21(voice);
                                    }
                                    //ttsGreater21(voice);
                                    break;

                            }

                        } else {

                            switch (voiceType) {
                                case "KR":

                                    if(questionType.equals("Q")) {
                                        ttsUnder20(english_string);
                                    } else {
                                        ttsUnder20_KR(voice);
                                    }


                                    break;
                                case "EN":
                                    if(questionType.equals("Q")) {
                                        ttsUnder20(english_string);
                                    } else {
                                        ttsUnder20(voice);
                                    }
                                    //ttsUnder20(voice);
                                    break;

                            }

                        }

                        //dialog.dismiss();



                    }

                    @Override
                    public void onFailure(Call<StudyStartDTO> call, Throwable t) {

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

    private void NextQuestion() {
        if(QuestionAnsWerCnt < 3) {
            Toast.makeText(this, "3번 발음평가를 맞춰 주세요.", Toast.LENGTH_LONG).show();
        } else {
            if(qc.get_nextStudy().equals("")) {
                //학습 완료
            } else {
                //다음학습
                String temp[] = qc.get_nextStudy().split("///");
                partCode = temp[0];
                orderId = Integer.parseInt(temp[1]);
                this._nextSend();
            }

            //this._nextSend();
        }
    }

    public void SkipQuetion() {
        if(qc.get_nextStudy().equals("")) {
            //학습 완료
        } else {
            //다음학습
            String temp[] = qc.get_nextStudy().split("///");
            partCode = temp[0];
            orderId = Integer.parseInt(temp[1]);
            this._nextSend();
        }
    }

    public void studyChapterQuestionOnClick(View v) {
        Log.d("test", "OK");
        Log.d("test", questionType);
        switch (v.getId()) {
            case R.id.next_btn:
                switch (this.questionType) {
                    case "Q":
                        this.NextQuestion();
                        break;
                    case "T":
                        this._nextSend();
                        break;
                }

                break;
        }
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        myTTS_KR.stop();
        myTTS_EN.stop();
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        myTTS_EN.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        myTTS_KR.stop();
        myTTS_EN.stop();
        String utteranceId=this.hashCode() + "";
        myTTS_EN.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20_KR(String text) {
        myTTS_KR.stop();
        myTTS_EN.stop();
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        myTTS_KR.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21_KR(String text) {
        myTTS_KR.stop();
        myTTS_EN.stop();
        String utteranceId=this.hashCode() + "";
        myTTS_KR.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    public void onPause(){
        if(myTTS_KR !=null){
            myTTS_KR.stop();
            myTTS_KR.shutdown();
        }

        if(myTTS_EN !=null){
            myTTS_EN.stop();
            myTTS_EN.shutdown();
        }
        super.onPause();
    }


    public void voiceAnswer(View v) {
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString());

        questionVoice = true;
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
        mRecognizer.startListening(intent);

        v = v;
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

            if(questionVoice == true) {
                if(qc.get_quetionValue().equals(searchName)) {
                    QuestionAnsWerCnt = QuestionAnsWerCnt + 1;
                    //cacd.answer("o");
                    cacd.show();

                    mRunnable = new Runnable() {
                        @Override
                        public void run() {
                            cacd.dismiss();


                            if(QuestionAnsWerCnt < 3) {


                                intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString());


                                questionVoice = true;
                                mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplication());
                                mRecognizer.setRecognitionListener(listener);
                                mRecognizer.startListening(intent);
                            } else {
                                NextQuestion();
                            }
                        }
                    };

                    mHandler = new Handler();
                    mHandler.postDelayed(mRunnable, 1000);
                } else {
                    cacdx.show();
                }


                tv_answer.setText(String.valueOf(QuestionAnsWerCnt) + " = " + searchName);
                questionVoice = false;





            }


        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };
}
