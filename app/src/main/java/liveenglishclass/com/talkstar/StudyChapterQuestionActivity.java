package liveenglishclass.com.talkstar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import liveenglishclass.com.talkstar.adapter.StudyChapterAdapter;
import liveenglishclass.com.talkstar.core.ActivityManager;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.custom.CustomAnswerCheckDialog;
import liveenglishclass.com.talkstar.custom.CustomAnswerCheckDialogX;
import liveenglishclass.com.talkstar.custom.CustomExplain;
import liveenglishclass.com.talkstar.custom.CustormLoadingDialog;
import liveenglishclass.com.talkstar.dto.QuestionClass;
import liveenglishclass.com.talkstar.dto.StudyChapterList;
import liveenglishclass.com.talkstar.dto.StudyDTO;
import liveenglishclass.com.talkstar.dto.StudyNextDTO;
import liveenglishclass.com.talkstar.dto.StudyStartDTO;
import liveenglishclass.com.talkstar.dto.VoiceSearchDTO;
import liveenglishclass.com.talkstar.util.PlayAudioManager;
import liveenglishclass.com.talkstar.util.Shared;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudyChapterQuestionActivity extends AppCompatActivity {
    private ActivityManager actManager = ActivityManager.getInstance();
    private final String debugTag = "StudyChapterActivity";
    private Retrofit retrofit;
    ApiService apiService;


    private String classesCode = "";
    private String chapterCode = "";
    private String partCode = "";
    private Integer orderId = 0;
    private String reject = "";
    private String questionType = "";


    private TextToSpeech myTTS_EN;
    private TextToSpeech myTTS_KR;

    private TextView tv_explanation;
    private TextView tv_answer;
    private ImageButton next_btn;
    private ProgressBar studyProgressBar;

    private LinearLayout line;

    private Integer QuestionAnsWerCnt = 0;
    private Integer QuestionAllAnswerCnt = 0;
    private Boolean nextVoice = false;
    private Boolean questionVoice = false;

    /****** 음성인식 ***************/
    private Intent intent;
    private SpeechRecognizer mRecognizer;
    private SpeechRecognizer mRecognizerCnt;

    private Button answerBtn;

    private QuestionClass qc;

    private CustomAnswerCheckDialog cacd;
    private CustomAnswerCheckDialogX cacdx;
    private CustomExplain cexplain;

    private static SoundPool soundPool;
    private int sound_beep;

    private Handler mHandler;
    private Runnable mRunnable;
    private HashMap<String, String> questionData;

    private String _voiceType = "";
    private String _voiceString ="";
    private String _voiceFile ="";
    private String _voiceSend ="";



    private String UID;
    private String explaignCheck = "false";

    private Boolean ynBool = false;
    private Boolean voiceFinish = false;


    private Thread thread;
    private Boolean popupCheck = false;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_chapter_question);



        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        actManager.addActivity(this);

        cacd = new CustomAnswerCheckDialog(this);
        cacd.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        cacd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        cacdx = new CustomAnswerCheckDialogX(this);
        cacdx.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        cacdx.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));






        studyProgressBar = (ProgressBar) findViewById(R.id.studyProgressBar);


        UID = Shared.getPerferences(this, "SESS_UID");
        Log.d("test", UID);


        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        answerBtn = (Button) findViewById(R.id.answerBtn);
        tv_explanation = (TextView) findViewById(R.id.tv_explanation);
        next_btn = (ImageButton) findViewById(R.id.next_btn);
        tv_answer = (TextView) findViewById(R.id.tv_answer);
        line = (LinearLayout) findViewById(R.id.line);

        qc = new QuestionClass();

        this._Thread();



        myTTS_EN=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(status != TextToSpeech.ERROR) {
                    myTTS_EN.setLanguage(Locale.ENGLISH);
                    myTTS_EN.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            voiceFinish = false;
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            voiceFinish = true;
                        }

                        @Override
                        public void onError(String utteranceId) {

                        }
                    });
                }
            }

            public void onUtteranceCompleted(String utteranceId) {
                Log.d("test", "EN_종료");
            }
        });

        myTTS_KR=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.d("test", "status="+String.valueOf(status));
                if(status != TextToSpeech.ERROR) {
                    myTTS_KR.setLanguage(Locale.KOREAN);
                    myTTS_KR.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            voiceFinish = false;
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            voiceFinish = true;
                        }

                        @Override
                        public void onError(String utteranceId) {

                        }
                    });
                }
            }

        });

        if(b!=null)
        {
            classesCode =(String) b.get("classesCode");
            chapterCode = (String) b.get("chapterCode");










        } else {

        }

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizerCnt = SpeechRecognizer.createSpeechRecognizer(this);



        explaignCheck = Shared.getPerferences(this, "explaignCheck");
        if(explaignCheck.equals("")) {
            cexplain = new CustomExplain(this);
            cexplain.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            cexplain.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            cexplain.show();
            Shared.savePreferences(this, "explaignCheck", "true");

            ImageView customexplain = (ImageView) cexplain.findViewById(R.id.customexplain);
            customexplain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    _dataSend();
                    cexplain.dismiss();
                }
            });

        } else {
            _dataSend();
        }




    }

    @Override
    protected void onStart() {


        super.onStart();  // Always call the superclass method first

        myTTS_EN=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(status != TextToSpeech.ERROR) {
                    myTTS_EN.setLanguage(Locale.ENGLISH);
                    myTTS_EN.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            voiceFinish = false;
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            voiceFinish = true;
                        }

                        @Override
                        public void onError(String utteranceId) {

                        }
                    });
                }
            }

            public void onUtteranceCompleted(String utteranceId) {
                Log.d("test", "EN_종료");
            }
        });

        myTTS_KR=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.d("test", "status="+String.valueOf(status));
                if(status != TextToSpeech.ERROR) {
                    myTTS_KR.setLanguage(Locale.KOREAN);
                    myTTS_KR.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            voiceFinish = false;
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            voiceFinish = true;
                        }

                        @Override
                        public void onError(String utteranceId) {

                        }
                    });
                }
            }

        });


    }


    private void _Thread()
    {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while(true) {
                        Log.d("test", "쓰레드 실행===="+voiceFinish.toString());
                        //Log.d("test", "오디오 실행 === " + PlayAudioManager.mediaPlayer.isPlaying());
                        if(voiceFinish == true) {


                            if (questionType.equals("Q2") == true || questionType.equals("T") == true) {
                                _nextSend();

                            }
                            voiceFinish = false;


                        }

                        sleep(2000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }


    private void _dataSend()
    {
        myTTS_KR.stop();
        myTTS_EN.stop();
        killMediaPlayer();


        questionData = new HashMap<>();
        //HashMap<String, String> map = new HashMap<>();
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);

                Call<StudyStartDTO> call = apiService.StudyStart(UID, classesCode, chapterCode);
                call.enqueue(new Callback<StudyStartDTO>() {

                    @Override
                    public void onResponse(Call<StudyStartDTO> call, Response<StudyStartDTO> response) {
                        //Log.d(debugTag, response.body().)
                        StudyStartDTO studyDTO = response.body();

                        studyProgressBar.setProgress(studyDTO.PER);

                        Log.d("test", "데이터 ===== " + studyDTO.ANSWER_ENGLISH);

                        String voiceType = studyDTO.VOICE_TYPE;
                        String voice = studyDTO.VOICE;
                        String voiceFile = studyDTO.VOICE_FILE;
                        String temp[] = studyDTO.NEXT_STUDY.split("///");
                        questionType = studyDTO.QUESTION_TYPE;

                        qc.set_nextStudy(studyDTO.NEXT_STUDY);
                        qc.set_quetionValue("");
                        qc.set_answerValue(studyDTO.ANSWER_ENGLISH);

                        partCode = temp[0];
                        orderId = Integer.parseInt(temp[1]);
                        tv_explanation.setText(voice);


                        _voiceType = voiceType;
                        _voiceString = voice;
                        _voiceFile = voiceFile;
                        Log.d("test", "파일로드==="+ApiService.API_URL + voiceFile);

                        if(voiceFile.equals("")) {


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

                            _voiceSend = voice;
                        } else {
                            try {
                                voiceFinish = false;
                                killMediaPlayer();
                                mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(ApiService.API_URL + voiceFile));
                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        voiceFinish = true;
                                        mp.release();
                                        Log.d("test", "실행완료");
                                        killMediaPlayer();
                                    }
                                });

                                mediaPlayer.start();
                                //PlayAudioManager.playAudio(getApplication(), ApiService.API_URL + voiceFile);
                            } catch (Exception e) {
                                e.printStackTrace();
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
        voiceFinish = false;
        myTTS_KR.stop();
        myTTS_EN.stop();

        killMediaPlayer();

        Log.d(debugTag, "_nextSend = " + orderId + "=" + partCode);

//        final CustormLoadingDialog dialog = new CustormLoadingDialog(getApplicationContext());
//        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.show();
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);

                Call<StudyStartDTO> call = apiService.StudyNext(UID, classesCode, chapterCode, partCode, orderId, questionData);
                questionData = new HashMap<>();
                call.enqueue(new Callback<StudyStartDTO>() {

                    @Override
                    public void onResponse(Call<StudyStartDTO> call, Response<StudyStartDTO> response) {
                        questionData = new HashMap<>();
                        //dialog.dismiss();
                        StudyStartDTO studyDTO = response.body();



                        String voiceType = studyDTO.VOICE_TYPE;
                        String voice = studyDTO.VOICE;
                        String voiceFile = studyDTO.VOICE_FILE;

                        questionType = studyDTO.QUESTION_TYPE;
                        String english_string = studyDTO.ENGLISH_STRING;

                        QuestionAnsWerCnt = 0;

                        studyProgressBar.setProgress(studyDTO.PER);


                        qc.set_nextStudy(studyDTO.NEXT_STUDY);
                        qc.set_quetionValue(english_string);
                        qc.set_answerValue(studyDTO.ANSWER_ENGLISH);

                        _voiceType = voiceType;
                        _voiceString = voice;
                        _voiceFile = voiceFile;

//                        String temp[] = studyDTO.NEXT_STUDY.split("///");
//                        partCode = temp[0];
//                        orderId = Integer.parseInt(temp[1]);

                        String temp[] = studyDTO.NEXT_STUDY.split("///");
                        if(studyDTO.NEXT_STUDY.equals("") != true) {
                            partCode = temp[0];
                            orderId = Integer.parseInt(temp[1]);
                        }

                        Log.d("test", "질문지 타입 == " +questionType);

                        if(questionType.equals("Q")) {
                            Log.d("test", "수업");
                            line.setVisibility(View.VISIBLE);
                            nextVoice = true;
                            tv_answer.setVisibility(View.VISIBLE);
                            answerBtn.setVisibility(View.VISIBLE);

                            QuestionAnsWerCnt = 0;
                            tv_explanation.setText(studyDTO.ENGLISH_STRING);
                            tv_answer.setText(_voiceString);
                        } else if(questionType.equals("Q2")) {
                            line.setVisibility(View.VISIBLE);
                            nextVoice = true;
                            tv_answer.setVisibility(View.VISIBLE);
                            answerBtn.setVisibility(View.VISIBLE);

                            QuestionAnsWerCnt = 0;
                            tv_explanation.setText(voice);
                            tv_answer.setText(studyDTO.ENGLISH_STRING);
                            answerBtn.setVisibility(View.GONE);
                        } else if(questionType.equals("E")) {
                            line.setVisibility(View.VISIBLE);
                            nextVoice = true;
                            tv_answer.setVisibility(View.VISIBLE);
                            answerBtn.setVisibility(View.VISIBLE);

                            QuestionAnsWerCnt = 0;
                            tv_explanation.setText(voice);
                            tv_answer.setText("영어로 발음해 주세요.");
                        } else {
                            answerBtn.setVisibility(View.GONE);
                            answerBtn.setVisibility(View.GONE);
                            tv_answer.setVisibility(View.GONE);
                            line.setVisibility(View.GONE);

                            tv_explanation.setText(voice);
                        }


                        Log.d("test", "파일로드==="+ApiService.API_URL + voiceFile);


                        if(voiceFile.equals("")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                switch (voiceType) {
                                    case "KR":
                                        if(questionType.equals("Q")) {
                                            _voiceSend = english_string;
                                            ttsGreater21(english_string);
                                        } else {
                                            _voiceSend = voice;
                                            ttsGreater21_KR(voice);
                                        }

                                        break;
                                    case "EN":
                                        if(questionType.equals("Q")) {
                                            _voiceSend = english_string;
                                            ttsGreater21(english_string);
                                        } else {
                                            _voiceSend = voice;
                                            ttsGreater21(voice);
                                        }
                                        //ttsGreater21(voice);
                                        break;

                                }

                            } else {

                                switch (voiceType) {
                                    case "KR":

                                        if(questionType.equals("Q")) {
                                            _voiceSend = english_string;
                                            ttsUnder20(english_string);
                                        } else {
                                            _voiceSend = voice;
                                            ttsUnder20_KR(voice);
                                        }


                                        break;
                                    case "EN":
                                        if(questionType.equals("Q")) {
                                            _voiceSend = english_string;
                                            ttsUnder20(english_string);
                                        } else {
                                            _voiceSend = voice;
                                            ttsUnder20(voice);
                                        }
                                        //ttsUnder20(voice);
                                        break;

                                }

                            }
                        } else {
                            try {

                                voiceFinish = false;
                                killMediaPlayer();
                                mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(ApiService.API_URL + voiceFile));
                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        voiceFinish = true;
                                        mp.release();
                                        Log.d("test", "실행완료");
                                        killMediaPlayer();
                                    }
                                });
                                mediaPlayer.start();
                                //PlayAudioManager.playAudio(getApplication(), ApiService.API_URL + voiceFile);
                            } catch (Exception e) {
                                Log.d("test", "음성로드 실패");
                                e.printStackTrace();
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

    public void _nextSend_reject()
    {
        voiceFinish = false;
        myTTS_KR.stop();
        myTTS_EN.stop();

        killMediaPlayer();

        Log.d(debugTag, "_nextSend_REJECT = " + orderId + "=" + partCode);

//        final CustormLoadingDialog dialog = new CustormLoadingDialog(getApplicationContext());
//        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.show();
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);

                Call<StudyStartDTO> call = apiService.StudyNext_Reject(UID, classesCode, chapterCode, partCode, orderId, questionData);
                questionData = new HashMap<>();
                call.enqueue(new Callback<StudyStartDTO>() {

                    @Override
                    public void onResponse(Call<StudyStartDTO> call, Response<StudyStartDTO> response) {
                        //Log.d(debugTag, response.body().)
                        questionData = new HashMap<>();
                        //dialog.dismiss();
                        StudyStartDTO studyDTO = response.body();



                        String voiceType = studyDTO.VOICE_TYPE;
                        String voice = studyDTO.VOICE;
                        String voiceFile = studyDTO.VOICE_FILE;

                        questionType = studyDTO.QUESTION_TYPE;
                        String english_string = studyDTO.ENGLISH_STRING;

                        QuestionAnsWerCnt = 0;


                        qc.set_nextStudy(studyDTO.NEXT_STUDY);
                        qc.set_quetionValue(english_string);
                        qc.set_answerValue(studyDTO.ANSWER_ENGLISH);

                        _voiceType = voiceType;
                        _voiceString = voice;
                        _voiceFile = voiceFile;

//                        String temp[] = studyDTO.NEXT_STUDY.split("///");
//                        partCode = temp[0];
//                        orderId = Integer.parseInt(temp[1]);

                        String temp[] = studyDTO.NEXT_STUDY.split("///");
                        if(studyDTO.NEXT_STUDY.equals("") != true) {
                            partCode = temp[0];
                            orderId = Integer.parseInt(temp[1]);
                        }

                        Log.d("test", "질문지 타입 == " +questionType);

                        if(questionType.equals("Q")) {
                            Log.d("test", "수업");
                            line.setVisibility(View.VISIBLE);
                            nextVoice = true;
                            tv_answer.setVisibility(View.VISIBLE);
                            answerBtn.setVisibility(View.VISIBLE);

                            QuestionAnsWerCnt = 0;
                            tv_explanation.setText(studyDTO.ENGLISH_STRING);
                            tv_answer.setText(_voiceString);
                        } else if(questionType.equals("Q2")) {
                            line.setVisibility(View.VISIBLE);
                            nextVoice = true;
                            tv_answer.setVisibility(View.VISIBLE);
                            answerBtn.setVisibility(View.VISIBLE);

                            QuestionAnsWerCnt = 0;
                            tv_explanation.setText(voice);
                            tv_answer.setText(studyDTO.ENGLISH_STRING);
                            answerBtn.setVisibility(View.GONE);
                        } else if(questionType.equals("E")) {
                            line.setVisibility(View.VISIBLE);
                            nextVoice = true;
                            tv_answer.setVisibility(View.VISIBLE);
                            answerBtn.setVisibility(View.VISIBLE);

                            QuestionAnsWerCnt = 0;
                            tv_explanation.setText(voice);
                            tv_answer.setText("발음평가 해주세요.");
                        } else {
                            answerBtn.setVisibility(View.GONE);
                            answerBtn.setVisibility(View.GONE);
                            tv_answer.setVisibility(View.GONE);
                            line.setVisibility(View.GONE);

                            tv_explanation.setText(voice);
                        }





                        if(voiceFile.equals("")) {
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
                        } else {
                            try {

                                voiceFinish = false;
                                mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(ApiService.API_URL + voiceFile));
                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        voiceFinish = true;
                                        mp.release();
                                        Log.d("test", "실행완료");
                                        killMediaPlayer();
                                    }
                                });
                                mediaPlayer.start();
                                //PlayAudioManager.playAudio(getApplication(), ApiService.API_URL + voiceFile);
                            } catch (Exception e) {
                                e.printStackTrace();
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

    private void NextQuestion() {
        //this._nextSend();


//        if(QuestionAnsWerCnt == -1) {
        if(questionType.equals("Q") || questionType.equals("Q2")) {
            if(QuestionAnsWerCnt < 3) {
                Toast.makeText(this, "3번 따라 읽어주세요.", Toast.LENGTH_LONG).show();
            } else {
                if(qc.get_nextStudy().equals("")) {

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("fragment_move", "study");
                    startActivity(intent);
                    finish();

                } else {
                    //다음학습

                    String temp[] = qc.get_nextStudy().split("///");
                    partCode = temp[0];
                    orderId = Integer.parseInt(temp[1]);
                    reject = temp[2];

                    Log.d("test", "reject="+reject);

                    if(reject.equals("R")) {
                        this._nextSend_reject();;

                    } else {
                        this._nextSend();
                    }
                }

            }
        } else if(questionType.equals("E")) {
            if(QuestionAnsWerCnt < 1) {
                Toast.makeText(this, "1번 발음평가를 맞춰 주세요.", Toast.LENGTH_LONG).show();
            } else {
                if(qc.get_nextStudy().equals("")) {

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("fragment_move", "study");
                    startActivity(intent);
                    finish();

                } else {
                    //다음학습

                    String temp[] = qc.get_nextStudy().split("///");
                    partCode = temp[0];
                    orderId = Integer.parseInt(temp[1]);
                    reject = temp[2];

                    Log.d("test", "reject="+reject);

                    if(reject.equals("R")) {
                        this._nextSend_reject();;

                    } else {
                        this._nextSend();
                    }
                }

            }
        } else {
            if(qc.get_nextStudy().equals("")) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("fragment_move", "study");
                startActivity(intent);
                finish();

            } else {
                //다음학습

                String temp[] = qc.get_nextStudy().split("///");
                partCode = temp[0];
                orderId = Integer.parseInt(temp[1]);
                reject = temp[2];

                Log.d("test", "reject="+reject);

                if(reject.equals("R")) {
                    this._nextSend_reject();;

                } else {
                    this._nextSend();
                }
            }
        }
//        if(QuestionAnsWerCnt < 3) {
//            Toast.makeText(this, "3번 발음평가를 맞춰 주세요.", Toast.LENGTH_LONG).show();
//        } else {
//            if(qc.get_nextStudy().equals("")) {
//
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.putExtra("fragment_move", "study");
//                startActivity(intent);
//                finish();
//
//            } else {
//                //다음학습
//
//                String temp[] = qc.get_nextStudy().split("///");
//                partCode = temp[0];
//                orderId = Integer.parseInt(temp[1]);
//                reject = temp[2];
//
//                Log.d("test", "reject="+reject);
//
//                if(reject.equals("R")) {
//                    this._nextSend_reject();;
//
//                } else {
//                    this._nextSend();
//                }
//            }
//
//        }
    }

    public void SkipQuetion() {
        if(qc.get_nextStudy().equals("")) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("fragment_move", "study");
            startActivity(intent);
            finish();
        } else {
            //다음학습
            String temp[] = qc.get_nextStudy().split("///");
            partCode = temp[0];
            orderId = Integer.parseInt(temp[1]);
            reject = temp[2];


            if(reject.equals("R")) {
                this._nextSend_reject();;

            } else {
                this._nextSend();
            }
        }
    }

    public void studyChapterQuestionOnClick(View v) {
        Log.d("test", "OK");
        Log.d("test", questionType);
        switch (v.getId()) {
            case R.id.next_btn:
                switch (this.questionType) {
                    case "Q":
                    case "E":
                    case "R":
                        this.NextQuestion();
                        break;
                    case "Q2":
                    case "T":
                        if(qc.get_nextStudy().equals("")) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("fragment_move", "study");
                            startActivity(intent);
                            finish();
                        } else {
                            this._nextSend();
                        }

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


        mRecognizerCnt.stopListening();
        mRecognizer.setRecognitionListener(listener);
        mRecognizer.startListening(intent);



        v = v;
    }

    public void micBtn(View v) {

        myTTS_KR.stop();
        myTTS_EN.stop();


        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREA.toString());


        mRecognizer.stopListening();
        mRecognizerCnt.setRecognitionListener(mic_listener);
        mRecognizerCnt.startListening(intent);
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
            Boolean answerCheck = false;
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            String searchName = rs[0];
            QuestionAllAnswerCnt++;
            Log.d("test", searchName);
            Log.d("test", "결과 === " + qc.get_answerValue());




            String questionAnswer = "";
            String questionAnswerData = "";
            //questionData.put("english_string", qc.get_quetionValue());
            //questionData.put("user_english", searchName);

            QuestionAnsWerCnt = QuestionAnsWerCnt + 1;

            if(questionVoice == true) {
                for(int i=0;i<mResult.size();i++) {
                    String[] answerValueTemp = qc.get_answerValue().split("///");
                    for(int j = 0; j<answerValueTemp.length; j++) {
                        if(answerValueTemp[j].trim().toLowerCase().matches(mResult.get(i).trim().toLowerCase())) {
                            //if(mResult.get(i).matches(".*"+qc.get_answerValue()+".*")) {
                            //searchName = mResult.get(i);

                            Log.d("test", "정답 데이터 ====== " + answerValueTemp[j] + "///" + mResult.get(i).toLowerCase());

                            questionAnswer = mResult.get(i).toLowerCase();
                            searchName = mResult.get(i).toLowerCase();
                            answerCheck = true;
                            break;
                        }
                    }

                }

                if (answerCheck == true) {
//                if(Arrays.asList(answerArray).contains(searchName)){
//                if(qc.get_quetionValue().equals(searchName)) {
                    questionAnswer = qc.get_quetionValue().toLowerCase() + "///" + questionAnswer + "///1";


                    questionData.put("data"+QuestionAllAnswerCnt, questionAnswer);
                    cacd.show();





                    mRunnable = new Runnable() {
                        @Override
                        public void run() {
                            cacd.dismiss();

                            if(questionType.equals("Q") || questionType.equals("Q2")) {
                                if(QuestionAnsWerCnt < 3) {


                                    intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                    intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString());


                                    questionVoice = true;
                                    mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplication());
                                    mRecognizer.setRecognitionListener(listener);
                                    mRecognizer.startListening(intent);


                                } else {
                                    voiceFinish = true;
                                    NextQuestion();
                                }
                            } else if(questionType.equals("E")) {
                                if(QuestionAnsWerCnt < 1) {


                                    intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                    intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString());


                                    questionVoice = true;
                                    mRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplication());
                                    mRecognizer.setRecognitionListener(listener);
                                    mRecognizer.startListening(intent);


                                } else {
                                    voiceFinish = true;
                                    NextQuestion();
                                }
                            } else {
                                NextQuestion();
                            }

                        }
                    };

                    mHandler = new Handler();
                    mHandler.postDelayed(mRunnable, 1000);
                } else {
                    questionAnswer = qc.get_quetionValue().toLowerCase() + "///" + searchName.toLowerCase() + "///2";
                    questionData.put("data"+QuestionAllAnswerCnt, questionAnswer);
                    cacdx.show();
                    //cacdx.findViewById(R.id.)


//                    answer_x_close = (ImageButton) findViewById(R.id.answer_x_close);
//                    answer_x_close.setOnClickListener(this);

                    ImageButton answer_x_close = (ImageButton) cacdx.findViewById(R.id.answer_x_close);
                    ImageButton answer_x_next_btn = (ImageButton) cacdx.findViewById(R.id.answer_x_next_btn);
                    ImageButton answer_retry = (ImageButton) cacdx.findViewById(R.id.answer_retry);
                    answer_x_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cacdx.dismiss();
                        }
                    });
                    answer_x_next_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cacdx.dismiss();
                            SkipQuetion();
                        }
                    });

                    answer_retry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cacdx.dismiss();
                            //this.answerCheck = true;
                        }
                    });



                }

                if(questionType.equals("Q") || questionType.equals("Q2")) {
                    tv_answer.setText(String.valueOf(QuestionAnsWerCnt) + "차시도 = " + searchName);
                    questionVoice = false;
                } else if(questionType.equals("E")) {
                    tv_answer.setText(searchName);
                }





            }


        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };


    public RecognitionListener mic_listener = new RecognitionListener() {
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

                    Call<VoiceSearchDTO> call = apiService.voiceSearch(UID, searchName);
                    call.enqueue(new Callback<VoiceSearchDTO>() {
                        @Override
                        public void onResponse(Call<VoiceSearchDTO> call, Response<VoiceSearchDTO> response) {


                            VoiceSearchDTO voiceSearchDTO = response.body();
                            Log.d("test", voiceSearchDTO.ACTION_CODE);




                            switch(voiceSearchDTO.ACTION_CODE) {
                                case "A001":
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        switch (_voiceType) {
                                            case "KR":
                                                Log.d("test", "OK");
                                                ttsGreater21_KR(_voiceString);
                                                break;
                                            case "EN":
                                                ttsGreater21(_voiceString);
                                                break;

                                        }

                                    } else {

                                        switch (_voiceType) {
                                            case "KR":
                                                ttsUnder20_KR(_voiceString);
                                                break;
                                            case "EN":
                                                ttsUnder20(_voiceString);
                                                break;

                                        }

                                    }
                                    break;

                                case "A002":
                                    SkipQuetion();

                                    break;



                                case "A003":
                                    /*
                                    if (voiceSearchDTO.ENGLISH_FILE.equals("TTS")) {

                                    } else {
                                        myTTS.
                                    }
                                    */

                                    Log.d("test", voiceSearchDTO.COMMAND_RETURN);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        ttsGreater21(voiceSearchDTO.COMMAND_RETURN);
                                    } else {
                                        ttsUnder20(voiceSearchDTO.COMMAND_RETURN);
                                    }

                                    //Toast.makeText(MainActivity.this, "영어 = " + voiceSearchDTO.COMMAND_RETURN, Toast.LENGTH_LONG).show();
                                    break;

                                case "A004":

                                    ynBool = false;
                                    ynBool = true;


                                    finish();




                                    break;

                                case "A005":
                                    Log.d("test", "2222");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        switch (_voiceType) {
                                            case "KR":
                                                if(questionType.equals("Q")) {
                                                    ttsGreater21(_voiceString);
                                                } else {
                                                    ttsGreater21_KR(_voiceString);
                                                }

                                                break;
                                            case "EN":
                                                if(questionType.equals("Q")) {
                                                    ttsGreater21(_voiceString);
                                                } else {
                                                    ttsGreater21(_voiceString);
                                                }
                                                //ttsGreater21(voice);
                                                break;

                                        }

                                    } else {

                                        switch (_voiceType) {
                                            case "KR":

                                                if(questionType.equals("Q")) {
                                                    ttsUnder20(_voiceString);
                                                } else {
                                                    ttsUnder20_KR(_voiceString);
                                                }


                                                break;
                                            case "EN":
                                                if(questionType.equals("Q")) {
                                                    ttsUnder20(_voiceString);
                                                } else {
                                                    ttsUnder20(_voiceString);
                                                }
                                                //ttsUnder20(voice);
                                                break;

                                        }

                                    }
                                    //Toast.makeText(getApplication(), "A005", Toast.LENGTH_LONG).show();
                                    //Toast.makeText(MainActivity.this, "답이 궁금할 떄", Toast.LENGTH_LONG).show();
                                    break;

                                case "A999":
//                                    String voiceReturn = voiceSearchDTO.COMMAND_RETURN;
//                                    String username = Shared.getPerferences(getApplication(), "SESS_USERNAME");
//                                    voiceReturn = voiceReturn.replace("[_NAME_]", username);
//
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                        ttsGreater21_KR(voiceReturn);
//                                    } else {
//                                        ttsUnder20_KR(voiceReturn);
//                                    }
                                    break;


                                default:
//                                    String voiceReturn2 = voiceSearchDTO.COMMAND_RETURN;
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                        ttsGreater21_KR(voiceReturn2);
//                                    } else {
//                                        ttsUnder20_KR(voiceReturn2);
//                                    }
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

    public void speakBtn(View v) {
        Log.d("test", _voiceFile +"///" + _voiceType + "///" + _voiceString);
        if(_voiceFile.equals("")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                switch (_voiceType) {
                    case "KR":
                        //Log.d("test", "OK");
                        ttsGreater21_KR(_voiceSend);
                        break;
                    case "EN":
                        ttsGreater21(_voiceSend);
                        break;

                }

            } else {

                switch (_voiceType) {
                    case "KR":
                        ttsUnder20_KR(_voiceSend);
                        break;
                    case "EN":
                        ttsUnder20(_voiceSend);
                        break;

                }

            }

        } else {
            try {
                //PlayAudioManager.killMediaPlayer();
                //PlayAudioManager.playAudio(getApplication(), ApiService.API_URL + _voiceFile);
                voiceFinish = false;
                mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(ApiService.API_URL + _voiceFile));
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        voiceFinish = true;
                        mp.release();
                        Log.d("test", "실행완료");
                        killMediaPlayer();
                    }
                });

                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public  void killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void studyClickEvent(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Log.d("test", "BACK");
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    @Override
    protected void onDestroy()
    {
        killMediaPlayer();
        myTTS_EN.stop();
        myTTS_KR.stop();
        super.onDestroy();
        thread.interrupt();
        actManager.removeActivity(this);
    }


    @Override
    protected void onStop()
    {
        killMediaPlayer();
        myTTS_EN.stop();
        myTTS_KR.stop();
        super.onStop();
        thread.interrupt();
    }



}
