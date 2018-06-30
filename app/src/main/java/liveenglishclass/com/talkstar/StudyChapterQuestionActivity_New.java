package liveenglishclass.com.talkstar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import liveenglishclass.com.talkstar.core.ActivityManager;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.custom.CustomAnswerCheckDialog;
import liveenglishclass.com.talkstar.custom.CustomAnswerCheckDialogX;
import liveenglishclass.com.talkstar.dto.StudyBookMark;
import liveenglishclass.com.talkstar.dto.StudyFinish;
import liveenglishclass.com.talkstar.dto.StudyStartDTO;
import liveenglishclass.com.talkstar.dto.StudyStartDTO_20180620;
import liveenglishclass.com.talkstar.dto.VoiceSearchDTO;
import liveenglishclass.com.talkstar.util.Shared;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudyChapterQuestionActivity_New extends AppCompatActivity {

    /***** activity 관리 및 네트워크 ***********/
    private ActivityManager actManager = ActivityManager.getInstance();
    private final String debugTag = "StudyChapterActivity";
    private Retrofit retrofit;
    ApiService apiService;


    /********** 변수 **************************/
    private String UID, classesCode, chapterCode, chapterOrder, AutoNext  = "";
    private String prev_chapterOrder = "";
    private String answerData = "";
    private String questionAnswer = "";
    private String studyCode = "";
    private Boolean voiceFinish = false;
    private String bookmarkYN_check = "N";

    private String _voiceFile = "";
    private String _explanation = "";
    private String _voiceType = "";
    private String _questionType = "";
    private String _englishString = "";
    private String _koreaString = "";
    private HashMap<String, String> questionData;
    private Boolean _voiceStart = false;


    /********* LAYOUT MODULE ***********/
    private ProgressBar studyProgressBar;
    private Button answerBtn;
    private TextView question_random_command, activity_studypart_title;
    private ImageButton next_btn, study_bookmark_btn, prev_btn;
    private LinearLayout add_layout;
    private EditText et_answer;
    private LinearLayout mic_linear;
    private TextView tv_answer;
    private ImageButton micEnVoiceBtn;

    /******** TTS ***********************/
    private TextToSpeech myTTS_EN;
    private TextToSpeech myTTS_KR;
    private MediaPlayer mediaPlayer;

    /********* 음성인식 *******************/
    private Intent intent;
    private SpeechRecognizer mRecognizer;
    private String micVoiceType = "";


    /******* THREAD *********************/
    private Boolean threadAutoRunning = true;
    private Thread threadAuto;
    private Runnable answerRunnable;
    private Handler mHandler;


    /****** CUSTOM DIALOG *************/
    private CustomAnswerCheckDialog customDialogO;
    private CustomAnswerCheckDialogX customDialogX;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_chapter_question_new);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        actManager.addActivity(this);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        /******** UID 값 가져오기 ***************/
        UID = Shared.getPerferences(this, "SESS_UID");

        if(b!=null)
        {

            classesCode =(String) b.get("classesCode");
            chapterCode = (String) b.get("chapterCode");
            chapterOrder = (String) b.get("chapterOrder");
            if(b.get("bookmark").equals("") == false) {
                bookmarkYN_check = (String) b.get("bookmark");
            } else {
                bookmarkYN_check = "N";
            }

        }





        /******** LAYOUT 초기화 ************/
        _layoutInit();

        /********* TTS 초기화 **************/
        _ttsInit();


        /********* 데이터 호출 *************/
        _studyData();


        /********** 쓰레드 실행 ************/
        _threadAutoInit();
    }

    /******* LAYOUT 초기화 *******************/
    private void _layoutInit()
    {
        studyProgressBar = (ProgressBar) findViewById(R.id.studyProgressBar);
        answerBtn = (Button) findViewById(R.id.answerBtn);

        question_random_command = (TextView) findViewById(R.id.question_random_command);
        activity_studypart_title = (TextView) findViewById(R.id.activity_studypart_title);

        next_btn = (ImageButton) findViewById(R.id.next_btn);
        prev_btn = (ImageButton) findViewById(R.id.prev_btn);
        study_bookmark_btn = (ImageButton) findViewById(R.id.study_bookmark_btn);


        activity_studypart_title.setText("CHAPTER " + chapterOrder);

        add_layout = (LinearLayout) findViewById(R.id.add_layout);

        customDialogO = new CustomAnswerCheckDialog(this);
        customDialogO.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        customDialogO.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        customDialogX = new CustomAnswerCheckDialogX(this);
        customDialogX.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        customDialogX.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mic_linear = (LinearLayout) findViewById(R.id.mic_linear);

        /*
        if(bookmarkYN_check.equals("Y")) {
            //prev_
            next_btn.setVisibility(View.GONE);
            prev_btn.setVisibility(View.GONE);
        } else {
            next_btn.setVisibility(View.VISIBLE);
            prev_btn.setVisibility(View.VISIBLE);
        }
        */

    }


    /******** TTS 초기화 ***********/
    private void _ttsInit()
    {
        //마이크 초기화
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);


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

    private void _threadAutoInit()
    {
        threadAuto = new Thread() {
            @Override
            public void run() {
                try {
                    while(threadAutoRunning) {
                        if(AutoNext.equals("A") || AutoNext.equals("T")) {
                            if(voiceFinish) {

                                //Log.d("test", "완료 === 다음페이지");
                                //_studyData();
                                voiceFinish = false;
                            }
                        }
                        sleep(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        threadAuto.start();
    }

    private void _prevData()
    {
        _ttsStop();
        _killMediaPlayer();

        threadAutoRunning = false;

        questionData = new HashMap<>();

        if(prev_chapterOrder.equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "첫페이지 입니다.", Toast.LENGTH_LONG); toast.show();
        } else {

            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                    apiService = retrofit.create(ApiService.class);
                    Call<StudyStartDTO_20180620> call = apiService.StudyPrev(UID, classesCode, chapterCode, prev_chapterOrder,bookmarkYN_check);
                    call.enqueue(new Callback<StudyStartDTO_20180620>() {

                        @Override
                        public void onResponse(Call<StudyStartDTO_20180620> call, Response<StudyStartDTO_20180620> response) {
                            StudyStartDTO_20180620 studyDTO = response.body();

                            Log.d("test", studyDTO.STUDY_CODE);


                            String Study_Code = studyDTO.STUDY_CODE;
                            Integer OrderId = studyDTO.ORDERID;
                            String QuestionType = studyDTO.QUESTION_TYPE;
                            String VoiceType = studyDTO.VOICE_TYPE;
                            String VoiceFile = studyDTO.VOICE_FILE;
                            String EnglishString = studyDTO.ENGLISH_STRING;
                            String KoreaString = studyDTO.KOREA_STRING;
                            String Explanation = studyDTO.EXPLANATION;
                            String bookmarkYN = studyDTO.BOOKMARK_YN;
                            Integer Per = studyDTO.PER;

                            _questionType = QuestionType;
                            _englishString = EnglishString;
                            _koreaString = KoreaString;
                            _voiceType = VoiceType;
                            _voiceFile = VoiceFile;
                            _explanation = Explanation;



                            if(studyDTO.PREV_STUDY.equals("")) {
                                prev_chapterOrder = "";
                            } else {
                                String nextDataTemp[] = studyDTO.PREV_STUDY.split("///");
                                prev_chapterOrder = nextDataTemp[1];
                            }



                            if(studyDTO.NEXT_STUDY.equals("")) {
                                chapterOrder = "";
                            } else {
                                String nextDataTemp[] = studyDTO.NEXT_STUDY.split("///");
                                chapterOrder = nextDataTemp[1];
                            }

                            //chapterOrder = String.valueOf(OrderId);



                            if(bookmarkYN.equals("Y")) {
                                study_bookmark_btn.setImageResource(R.mipmap.sclap_btn_on);
                            } else {
                                study_bookmark_btn.setImageResource(R.mipmap.sclap_btn_off);
                            }



                            studyProgressBar.setProgress(Per);

                            AutoNext = studyDTO.QUESTION_TYPE;


                            answerData = studyDTO.ANSWER_ENGLISH;
                            studyCode = Study_Code;


                            _voice(QuestionType, VoiceType, VoiceFile, EnglishString, KoreaString, Explanation);


                        }

                        @Override
                        public void onFailure(Call<StudyStartDTO_20180620> call, Throwable t) {

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

    }

    private void _studyData()
    {
        _ttsStop();
        _killMediaPlayer();

        threadAutoRunning = true;

        Log.d("test", "다음="+chapterOrder);

        questionData = new HashMap<>();


        if (chapterOrder.equals("")) {



            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                    apiService = retrofit.create(ApiService.class);
                    Call<StudyFinish> call = apiService.StudyFinish(UID, classesCode, chapterCode);
                    call.enqueue(new Callback<StudyFinish>() {

                        @Override
                        public void onResponse(Call<StudyFinish> call, Response<StudyFinish> response) {
                            StudyFinish studyDTO = response.body();

                            if(bookmarkYN_check.equals("N")) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.putExtra("fragment_move", "study");
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.putExtra("fragment_move", "command");
                                startActivity(intent);
                                finish();
                            }


                        }

                        @Override
                        public void onFailure(Call<StudyFinish> call, Throwable t) {

                        }
                    });






                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                }

            }.execute();
        } else {
            if(_questionType.equals("E")) {

                if(questionAnswer.equals("")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        String utteranceId=this.hashCode() + "";
                        myTTS_KR.speak("음성 및 텍스트로 답변해주세요", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                    } else {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                        myTTS_KR.speak("음성 및 텍스트로 답변해주세요", TextToSpeech.QUEUE_FLUSH, map);
                    }

                    Toast.makeText(getApplicationContext(), "음성 및 텍스트로 답변해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... params) {
                            retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                            apiService = retrofit.create(ApiService.class);
                            Call<StudyStartDTO_20180620> call = apiService.StudyStart_20180620(UID, classesCode, chapterCode, chapterOrder, studyCode, questionAnswer, bookmarkYN_check);
                            call.enqueue(new Callback<StudyStartDTO_20180620>() {

                                @Override
                                public void onResponse(Call<StudyStartDTO_20180620> call, Response<StudyStartDTO_20180620> response) {
                                    StudyStartDTO_20180620 studyDTO = response.body();

                                    Log.d("test", studyDTO.STUDY_CODE);


                                    String Study_Code = studyDTO.STUDY_CODE;
                                    Integer OrderId = studyDTO.ORDERID;
                                    String QuestionType = studyDTO.QUESTION_TYPE;
                                    String VoiceType = studyDTO.VOICE_TYPE;
                                    String VoiceFile = studyDTO.VOICE_FILE;
                                    String EnglishString = studyDTO.ENGLISH_STRING;
                                    String KoreaString = studyDTO.KOREA_STRING;
                                    String Explanation = studyDTO.EXPLANATION;
                                    String bookmarkYN = studyDTO.BOOKMARK_YN;
                                    Integer Per = studyDTO.PER;

                                    _questionType = QuestionType;
                                    _englishString = EnglishString;


                                    studyProgressBar.setProgress(Per);

                                    _questionType = QuestionType;
                                    _englishString = EnglishString;
                                    _koreaString = KoreaString;
                                    _voiceType = VoiceType;
                                    _voiceFile = VoiceFile;
                                    _explanation = Explanation;



                                    if(studyDTO.PREV_STUDY.equals("")) {
                                        prev_chapterOrder = "";
                                    } else {
                                        String nextDataTemp[] = studyDTO.PREV_STUDY.split("///");
                                        prev_chapterOrder = nextDataTemp[1];
                                    }


                                    if(studyDTO.NEXT_STUDY.equals("")) {
                                        chapterOrder = "";
                                    } else {
                                        String nextDataTemp[] = studyDTO.NEXT_STUDY.split("///");
                                        chapterOrder = nextDataTemp[1];
                                    }

                                    if(bookmarkYN.equals("Y")) {
                                        study_bookmark_btn.setImageResource(R.mipmap.sclap_btn_on);
                                    } else {
                                        study_bookmark_btn.setImageResource(R.mipmap.sclap_btn_off);
                                    }





                                    AutoNext = studyDTO.QUESTION_TYPE;


                                    answerData = studyDTO.ANSWER_ENGLISH;
                                    studyCode = Study_Code;


                                    _voice(QuestionType, VoiceType, VoiceFile, EnglishString, KoreaString, Explanation);
                                    questionAnswer = "";


                                }

                                @Override
                                public void onFailure(Call<StudyStartDTO_20180620> call, Throwable t) {

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


            } else {
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                        apiService = retrofit.create(ApiService.class);
                        Call<StudyStartDTO_20180620> call = apiService.StudyStart_20180620(UID, classesCode, chapterCode, chapterOrder, studyCode, questionAnswer, bookmarkYN_check);
                        call.enqueue(new Callback<StudyStartDTO_20180620>() {

                            @Override
                            public void onResponse(Call<StudyStartDTO_20180620> call, Response<StudyStartDTO_20180620> response) {
                                StudyStartDTO_20180620 studyDTO = response.body();

                                Log.d("test", studyDTO.STUDY_CODE);


                                String Study_Code = studyDTO.STUDY_CODE;
                                Integer OrderId = studyDTO.ORDERID;
                                String QuestionType = studyDTO.QUESTION_TYPE;
                                String VoiceType = studyDTO.VOICE_TYPE;
                                String VoiceFile = studyDTO.VOICE_FILE;
                                String EnglishString = studyDTO.ENGLISH_STRING;
                                String KoreaString = studyDTO.KOREA_STRING;
                                String Explanation = studyDTO.EXPLANATION;
                                String bookmarkYN = studyDTO.BOOKMARK_YN;
                                Integer Per = studyDTO.PER;

                                _questionType = QuestionType;
                                _englishString = EnglishString;


                                studyProgressBar.setProgress(Per);

                                _questionType = QuestionType;
                                _englishString = EnglishString;
                                _koreaString = KoreaString;
                                _voiceType = VoiceType;
                                _voiceFile = VoiceFile;
                                _explanation = Explanation;



                                if(studyDTO.PREV_STUDY.equals("")) {
                                    prev_chapterOrder = "";
                                } else {
                                    String nextDataTemp[] = studyDTO.PREV_STUDY.split("///");
                                    prev_chapterOrder = nextDataTemp[1];
                                }


                                if(studyDTO.NEXT_STUDY.equals("")) {
                                    chapterOrder = "";
                                } else {
                                    String nextDataTemp[] = studyDTO.NEXT_STUDY.split("///");
                                    chapterOrder = nextDataTemp[1];
                                }

                                if(bookmarkYN.equals("Y")) {
                                    study_bookmark_btn.setImageResource(R.mipmap.sclap_btn_on);
                                } else {
                                    study_bookmark_btn.setImageResource(R.mipmap.sclap_btn_off);
                                }





                                AutoNext = studyDTO.QUESTION_TYPE;


                                answerData = studyDTO.ANSWER_ENGLISH;
                                studyCode = Study_Code;


                                _voice(QuestionType, VoiceType, VoiceFile, EnglishString, KoreaString, Explanation);


                            }

                            @Override
                            public void onFailure(Call<StudyStartDTO_20180620> call, Throwable t) {

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

        }

    }

    private void _voice(String question_type, String voice_type, String voice_file, String english_string, String korea_string, String explanation)
    {


        if(voice_type.equals("T")) {
            study_bookmark_btn.setVisibility(View.GONE);


            LayoutInflater mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View mAddView = mInflater.inflate(R.layout.activity_study_chapter_question_new_boxlayout_title, null, true);
            TextView tv_explanation = (TextView) mAddView.findViewById(R.id.tv_explanation);
            tv_explanation.setText(explanation);
            add_layout.removeAllViews();
            add_layout.addView(mAddView);
        } else {
            LayoutInflater mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View mAddView = mInflater.inflate(R.layout.activity_study_chapter_question_new_boxlayout_question, null, true);
            mAddView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
            //nodeView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));//JR - fix for MATCH_PARENT
            TextView tv_explanation = (TextView) mAddView.findViewById(R.id.tv_explanation);
            tv_answer = (TextView) mAddView.findViewById(R.id.tv_answer);
            et_answer = (EditText) mAddView.findViewById(R.id.et_answer);
            if(voice_type.equals("KR")) {
                tv_explanation.setText(korea_string);
            } else {
                tv_explanation.setText(english_string);
            }

            micEnVoiceBtn = (ImageButton) mAddView.findViewById(R.id.micEnVoiceBtn);
            ImageButton textVoiceBtn = (ImageButton) mAddView.findViewById(R.id.textVoiceBtn);
            micEnVoiceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    micVoiceType = "EN";
                    //micEnVoiceBtn.

                    _voiceStart = false;
                    Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_mic_sub);
                    micEnVoiceBtn.startAnimation(startAnimation);
                    _micVoice(micVoiceType);

                }
            });

            if(question_type.equals("A")) {
                micEnVoiceBtn.setVisibility(View.GONE);
                textVoiceBtn.setVisibility(View.GONE);
            }

            if(question_type.equals("Q") || question_type.equals("A")) {
                if(voice_type.equals("KR")) {
                    tv_answer.setText(english_string);
                } else {
                    tv_answer.setText(korea_string);
                }
            } else {
                tv_answer.setText("");
            }

            et_answer.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                        String[] answerValueTemp = answerData.split("///");
                        Boolean answerCheck = false;
                        for(int j = 0; j<answerValueTemp.length; j++) {
                            if(answerValueTemp[j].trim().toLowerCase().matches(et_answer.getText().toString().toLowerCase().trim())) {
                                //Log.d("test", "정답 데이터 ====== " + answerValueTemp[j] + "///" + mResult.get(i).toLowerCase());
                                questionAnswer = answerValueTemp[j] + "///" + et_answer.getText().toString() + "///1";
                                answerCheck = true;
                                break;
                            }
                        }

                        if(answerCheck == false){
                            questionAnswer = answerValueTemp[0] + "///" + et_answer.getText().toString() + "///2";
                            customDialogX.show();

                        } else {
                            customDialogO.show();


                        }

                        final Boolean finalAnswerCheck = answerCheck;
                        answerRunnable = new Runnable() {
                            @Override
                            public void run() {
                                //cacd.dismiss();
                                if(finalAnswerCheck == false) {
                                    //customDialogX.dismiss();

                                    ImageButton answer_x_close = (ImageButton) customDialogX.findViewById(R.id.answer_x_close);
                                    ImageButton answer_x_next_btn = (ImageButton) customDialogX.findViewById(R.id.answer_x_next_btn);
                                    ImageButton answer_retry = (ImageButton) customDialogX.findViewById(R.id.answer_retry);

                                    answer_x_close.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            customDialogX.dismiss();
                                        }
                                    });
                                    answer_x_next_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            customDialogX.dismiss();
                                            _studyData();
                                        }
                                    });

                                    answer_retry.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            customDialogX.dismiss();
                                            //this.answerCheck = true;
                                        }
                                    });




                                } else {
                                    customDialogO.dismiss();
                                    _studyData();
                                }



                            }
                        };

                        mHandler = new Handler();
                        mHandler.postDelayed(answerRunnable, 1000);

                        return true;
                    }

                    return false;
                }
            });


            if(question_type.equals("Q")){
                study_bookmark_btn.setVisibility(View.VISIBLE);
                study_bookmark_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _bookmarkSave();
                        //Log.d("test", UID + "///" + studyCode);
                    }
                });
            } else {
                study_bookmark_btn.setVisibility(View.GONE);
            }


            add_layout.removeAllViews();
            add_layout.addView(mAddView);
        }



        Log.d("test", "실행");
        Log.d("test", voice_type);
        /********* 설명 ********/
        switch(voice_type) {


            case "T":

                if(voice_file.equals("")) {
                    /********* TTS **********/
                    _ttsStop();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        String utteranceId=this.hashCode() + "";
                        myTTS_KR.speak(explanation, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                    } else {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                        myTTS_KR.speak(explanation, TextToSpeech.QUEUE_FLUSH, map);
                    }


                } else {
                    /********* MP3 **********/
                    voiceFinish = false;
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(ApiService.API_URL + voice_file));
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            voiceFinish = true;
                            //mp.release();
                            _killMediaPlayer();
                        }
                    });

                    mediaPlayer.start();
                }

            break;

            default:




                if(voice_file.equals("")) {
                    if(voice_type.equals("KR")) {
                        /********* TTS **********/
                        _ttsStop();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            String utteranceId=this.hashCode() + "";
                            myTTS_KR.speak(korea_string, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                        } else {
                            HashMap<String, String> map = new HashMap<>();
                            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                            myTTS_KR.speak(korea_string, TextToSpeech.QUEUE_FLUSH, map);
                        }
                    } else {
                        /********* TTS **********/
                        _ttsStop();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            String utteranceId=this.hashCode() + "";
                            myTTS_EN.speak(english_string, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                        } else {
                            HashMap<String, String> map = new HashMap<>();
                            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                            myTTS_EN.speak(english_string, TextToSpeech.QUEUE_FLUSH, map);
                        }
                    }
                } else {
                    voiceFinish = false;
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(ApiService.API_URL + voice_file));
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            voiceFinish = true;
                            //mp.release();
                            _killMediaPlayer();
                        }
                    });

                    mediaPlayer.start();
                }
            break;

        }
    }

    private void _voiceMsg(String voice_type, String voice_file, String korea_string, String english_string, String explanation)
    {


        //Log.d("test", voice_type + "///" + voice_file + "///" + korea_string +"///" + english_string + "///" + explanation);
        switch(voice_type) {


            case "T":

                if(voice_file.equals("")) {
                    /********* TTS **********/
                    _ttsStop();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        String utteranceId=this.hashCode() + "";
                        myTTS_KR.speak(explanation, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                    } else {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                        myTTS_KR.speak(explanation, TextToSpeech.QUEUE_FLUSH, map);
                    }


                } else {
                    /********* MP3 **********/
                    voiceFinish = false;
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(ApiService.API_URL + voice_file));
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            voiceFinish = true;
                            //mp.release();
                            _killMediaPlayer();
                        }
                    });

                    mediaPlayer.start();
                }

                break;

            default:




                if(voice_file.equals("")) {
                    if(voice_type.equals("KR")) {
                        /********* TTS **********/
                        _ttsStop();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            String utteranceId=this.hashCode() + "";
                            myTTS_KR.speak(korea_string, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                        } else {
                            HashMap<String, String> map = new HashMap<>();
                            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                            myTTS_KR.speak(korea_string, TextToSpeech.QUEUE_FLUSH, map);
                        }
                    } else {
                        /********* TTS **********/
                        _ttsStop();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            String utteranceId=this.hashCode() + "";
                            myTTS_EN.speak(english_string, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                        } else {
                            HashMap<String, String> map = new HashMap<>();
                            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                            myTTS_EN.speak(english_string, TextToSpeech.QUEUE_FLUSH, map);
                        }
                    }
                } else {
                    voiceFinish = false;
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(ApiService.API_URL + voice_file));
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            voiceFinish = true;
                            //mp.release();
                            _killMediaPlayer();
                        }
                    });

                    mediaPlayer.start();
                }
                break;

        }
    }

    private void _bookmarkSave()
    {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);
                Call<StudyBookMark> call = apiService.StudyBook(UID, studyCode);
                call.enqueue(new Callback<StudyBookMark>() {

                    @Override
                    public void onResponse(Call<StudyBookMark> call, Response<StudyBookMark> response) {
                        StudyBookMark studyDTO = response.body();

                        if(studyDTO.USE_YN.equals("Y")) {
                            study_bookmark_btn.setImageResource(R.mipmap.sclap_btn_on);
                            Toast.makeText(getApplicationContext(), "북마크가 설정되었습니다.", Toast.LENGTH_LONG).show();
                        } else {
                            study_bookmark_btn.setImageResource(R.mipmap.sclap_btn_off);
                            Toast.makeText(getApplicationContext(), "북마크가 해제되었습니다.", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<StudyBookMark> call, Throwable t) {

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

    private void _micVoice(String voiceType)
    {
        _killMediaPlayer();
        switch (voiceType) {
            case "KR":
                intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREA.toString());
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);
                break;
            case "EN":

                intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString());
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);
                break;
        }

        //intent.putExtra(RecognitionListener.EXTRA_SUPPORTED_LANGUAGES, )

        mRecognizer.stopListening();
        mRecognizer.setRecognitionListener(listener);
        mRecognizer.startListening(intent);

    }

    public void btnEvent(View v) {
        switch(v.getId()) {
            case R.id.prev_btn:
                _prevData();
                break;
            case R.id.next_btn:
                _studyData();
                break;
            case R.id.mic_btn:
                micVoiceType = "KR";
                _micVoice("KR");

                Log.d("test", "애니메이션");

                Animation default_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.mic_default);
                mic_linear.setAnimation(default_anim);
                mic_linear.startAnimation(default_anim);
                mic_linear.setBackgroundResource(R.mipmap.mic_active_bg);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_mic_rotate);
                Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.mic_fadein);
                Animation scale = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_scale);

                //mic_linear.setAlpha();

                AnimationSet s = new AnimationSet(false);
                s.addAnimation(scale);
                s.addAnimation(fadeIn);
                s.addAnimation(animation);
                //mic_linear.setAnimation(fadeIn);
                //mic_linear.setAnimation(animation);
                mic_linear.startAnimation(s);

                break;

            case R.id.textVoiceBtn:
                et_answer.setVisibility(View.VISIBLE);
                break;
            case R.id.study_bookmark_btn:

                //Log.d("test", studyCode + "///" + UID);
                //studyCode
                break;
        }
    }

    public RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {

            _ttsStop();
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
            if(_voiceStart == false) {
                _voiceStart = true;
            } else {
                if(micVoiceType.equals("EN")) {
                    micEnVoiceBtn.clearAnimation();
                    micEnVoiceBtn.animate().cancel();
                } else {
                    mic_linear.setBackgroundResource(R.mipmap.mic_active_bg_off);
                    mic_linear.clearAnimation();
                    mic_linear.animate().cancel();
                }
            }


//            Log.d("test", micVoiceType);
//            if(micVoiceType.equals("EN")) {
//                micEnVoiceBtn.clearAnimation();
//                micEnVoiceBtn.animate().cancel();
//            } else {
//                mic_linear.setBackgroundResource(R.mipmap.mic_active_bg_off);
//                mic_linear.clearAnimation();
//                mic_linear.animate().cancel();
//            }



            Log.d("test", "onError");
            //Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG); toast.show();
        }

        @Override
        public void onResults(Bundle results) {
            _voiceStart = false;
            if(micVoiceType.equals("EN")) {

                micEnVoiceBtn.clearAnimation();
                micEnVoiceBtn.animate().cancel();
                String key = "";
                Boolean answerCheck = false;
                key = SpeechRecognizer.RESULTS_RECOGNITION;
                ArrayList<String> mResult = results.getStringArrayList(key);
                String[] rs = new String[mResult.size()];
                mResult.toArray(rs);

                String searchName = rs[0];
                questionAnswer = "";
                String dataAnswer = "";
                String[] answerValueTemp = answerData.split("///");

                for(int i=0;i<mResult.size();i++) {

                    for(int j = 0; j<answerValueTemp.length; j++) {
                        if(answerValueTemp[j].trim().toLowerCase().matches(mResult.get(i).trim().toLowerCase())) {
                            Log.d("test", "정답 데이터 ====== " + answerValueTemp[j] + "///" + mResult.get(i).toLowerCase());
                            questionAnswer = answerValueTemp[j] + "///" + mResult.get(i) + "///1";
                            dataAnswer = mResult.get(i);

                            answerCheck = true;
                            break;
                        }
                    }
                }

                if(answerCheck == false){
                    tv_answer.setText(searchName);
                    questionAnswer = answerValueTemp[0] + "///" + searchName + "///2";
                    customDialogX.show();
                } else {
                    customDialogO.show();

                    tv_answer.setText(dataAnswer);


                }

                final Boolean finalAnswerCheck = answerCheck;
                answerRunnable = new Runnable() {
                    @Override
                    public void run() {
                        //cacd.dismiss();
                        if(finalAnswerCheck == false) {
                            //customDialogX.dismiss();

                            ImageButton answer_x_close = (ImageButton) customDialogX.findViewById(R.id.answer_x_close);
                            ImageButton answer_x_next_btn = (ImageButton) customDialogX.findViewById(R.id.answer_x_next_btn);
                            ImageButton answer_retry = (ImageButton) customDialogX.findViewById(R.id.answer_retry);

                            answer_x_close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    customDialogX.dismiss();
                                }
                            });
                            answer_x_next_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    customDialogX.dismiss();
//                                    _studyData();
//                                    if(bookmarkYN_check.equals("N")) {
//                                        _studyData();
//                                    }
                                }
                            });

                            answer_retry.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    customDialogX.dismiss();
                                    //this.answerCheck = true;
                                }
                            });




                        } else {
                            customDialogO.dismiss();
//                            if(bookmarkYN_check.equals("N")) {
//                                _studyData();
//                            }
                        }



                    }
                };

                mHandler = new Handler();
                mHandler.postDelayed(answerRunnable, 1000);
            } else {
                mic_linear.setBackgroundResource(R.mipmap.mic_active_bg_off);
                mic_linear.clearAnimation();
                mic_linear.animate().cancel();
                String key = "";
                key = SpeechRecognizer.RESULTS_RECOGNITION;
                ArrayList<String> mResult = results.getStringArrayList(key);
                String[] rs = new String[mResult.size()];
                mResult.toArray(rs);

                final String searchName = rs[0];
                Log.d("test", searchName);





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




                                switch(voiceSearchDTO.ACTION_CODE) {
                                    case "A001":
                                        _voiceMsg(_voiceType, _voiceFile, _koreaString, _englishString, _explanation);
                                        Toast.makeText(getApplicationContext(), "반복", Toast.LENGTH_LONG).show();
                                        break;

                                    case "A002":
                                        _studyData();
                                        //Toast.makeText(MainActivity.this, "다음 프로세스 진행", Toast.LENGTH_LONG).show();

                                        break;



                                    case "A003":
                                        _ttsStop();
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            String utteranceId=this.hashCode() + "";
                                            myTTS_EN.speak(voiceSearchDTO.COMMAND_RETURN, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                                        } else {
                                            HashMap<String, String> map = new HashMap<>();
                                            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                                            myTTS_EN.speak(voiceSearchDTO.COMMAND_RETURN, TextToSpeech.QUEUE_FLUSH, map);
                                        }

                                        Toast.makeText(getApplicationContext(), "영어 = " + voiceSearchDTO.COMMAND_RETURN, Toast.LENGTH_LONG).show();
                                        break;

                                    case "A004":

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            String utteranceId=this.hashCode() + "";
                                            myTTS_KR.speak("수업을 중단합니다", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                                        } else {
                                            HashMap<String, String> map = new HashMap<>();
                                            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                                            myTTS_KR.speak("수업을 중단합니다", TextToSpeech.QUEUE_FLUSH, map);
                                        }

                                        finish();
                                        //Toast.makeText(getApplicationContext(), "수업중단", Toast.LENGTH_LONG).show();
                                        break;

                                    case "A005":
                                        if(_questionType.equals("Q") || _questionType.equals("E")) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                String utteranceId=this.hashCode() + "";
                                                myTTS_EN.speak(_englishString, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                                            } else {
                                                HashMap<String, String> map = new HashMap<>();
                                                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                                                myTTS_EN.speak(_englishString, TextToSpeech.QUEUE_FLUSH, map);
                                            }

                                            Toast.makeText(getApplicationContext(), _englishString, Toast.LENGTH_LONG).show();
                                        } else {

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                String utteranceId=this.hashCode() + "";
                                                myTTS_KR.speak("문제 부분만 가능합니다.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                                            } else {
                                                HashMap<String, String> map = new HashMap<>();
                                                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
                                                myTTS_KR.speak("문제 부분만 가능합니다.", TextToSpeech.QUEUE_FLUSH, map);
                                            }


                                            Toast.makeText(getApplicationContext(), "문제 부분만 가능합니다.", Toast.LENGTH_LONG).show();
                                        }

                                        //Toast.makeText(MainActivity.this, "답이 궁금할 떄", Toast.LENGTH_LONG).show();
                                        break;

                                    case "A999":

                                        break;


                                    default:

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
            }


        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

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

    private void _ttsStop() {
        myTTS_EN.stop();
        myTTS_KR.stop();
    }

    private void _killMediaPlayer() {
        _ttsStop();

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
        finish();
    }

    @Override
    public void onBackPressed() {
        Log.d("test", "BACK");
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        threadAutoRunning = false;
    }

    @Override
    protected void onDestroy()
    {
        _killMediaPlayer();
        actManager.removeActivity(this);
        threadAutoRunning = false;
        super.onDestroy();


    }


    @Override
    protected void onStop()
    {
        threadAutoRunning = false;
        _killMediaPlayer();
        super.onStop();
    }



}
