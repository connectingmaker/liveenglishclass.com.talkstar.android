package liveenglishclass.com.talkstar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

import liveenglishclass.com.talkstar.adapter.StudyAdapter;
import liveenglishclass.com.talkstar.adapter.StudyChapterAdapter;
import liveenglishclass.com.talkstar.core.ActivityManager;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.dto.StudyChapterDTO;
import liveenglishclass.com.talkstar.dto.StudyChapterList;
import liveenglishclass.com.talkstar.dto.StudyList;
import liveenglishclass.com.talkstar.util.Shared;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudyChapterActivity extends AppCompatActivity {
    private ActivityManager actManager = ActivityManager.getInstance();

    private final String debugTag = "StudyChapterActivity";
    private Retrofit retrofit;
    ApiService apiService;


    private TextView activity_studypart_title, activity_studychapter_question, activity_studychapter_question_skip, chapter_title;
    private ListView activity_studypart_list;

    private ArrayList<StudyChapterDTO> _studyLists;
    private StudyChapterAdapter study_adapter;


    private Intent intent;
    private String classesName;
    private String classesCode;
    private String chapterCode;
    private String chapterName;
    private String chapterLearning;
    private String UID = "";
    private Integer positionData = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_chapter);

        actManager.addActivity(this);


        _init();
    }

    private void _init()
    {

        activity_studypart_title = (TextView) findViewById(R.id.activity_studypart_title);
        //activity_studychapter_question = (TextView) findViewById(R.id.activity_studychapter_question);
        //activity_studychapter_question_skip = (TextView) findViewById(R.id.activity_studychapter_question_skip);
        activity_studypart_list = (ListView) findViewById(R.id.activity_studypart_list);

        UID = Shared.getPerferences(this, "SESS_UID");




        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            classesCode =(String) b.get("classesCode");
            classesName = (String) b.get("classesName");

            activity_studypart_title.setText(classesName);



        } else {

        }




        final View header = getLayoutInflater().inflate(R.layout.activity_study_chapter_header, null, false) ;

        chapter_title = header.findViewById(R.id.chapter_title);



        _studyLists = new ArrayList<>();




        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);

                Call<StudyChapterList> call = apiService.StudyChapterList(UID, classesCode);
                call.enqueue(new Callback<StudyChapterList>() {

                    @Override
                    public void onResponse(Call<StudyChapterList> call, Response<StudyChapterList> response) {
                        /*Log.d(debugTag, response.body().err_code);*/
                        String err_code = response.body().err_code;
                        if(err_code.equals("000")) {
                            _studyLists = response.body().getDatas();

                            //getContext


                            study_adapter = new StudyChapterAdapter(getBaseContext(), _studyLists);
                            activity_studypart_list.addHeaderView(header);
                            activity_studypart_list.setAdapter(study_adapter);
                            activity_studypart_list.setOnItemClickListener(mItemClickListener);


                            chapter_title.setText(_studyLists.size() + " LESSONS");

                            //ArrayAdapter study_adapter = new ArrayAdapter(getActivity(), android.R.layout.activity_list_item, _studyLists);
                            //listView.setAdapter(study_adapter);

                        } else {
                        }
                    }

                    @Override
                    public void onFailure(Call<StudyChapterList> call, Throwable t) {

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

    public void studyClickEvent(View v) {
        Log.d("test", "OK");

        switch(v.getId()) {
            case R.id.activity_study_topLeft_btn:
            case R.id.activity_stidy_topleft_image_btn:
                onBackPressed();
                break;
        }

    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long l_position) {



            chapterCode = _studyLists.get(position-1).getChapterCode();
            chapterName = _studyLists.get(position-1).getChapterName();
            chapterLearning = _studyLists.get(position-1).getLearningNotes();

            if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[] { android.Manifest.permission.RECORD_AUDIO },
                        1000);
                return;
            } else {

                if(position == 0) {
                    positionData = position;
                    intent = new Intent(StudyChapterActivity.this, StudyChapterStartActivity.class);
                    intent.putExtra("classesCode", classesCode);
                    intent.putExtra("chapterCode", chapterCode);
                    intent.putExtra("chapterName", chapterName);
                    intent.putExtra("chapterLearning", chapterLearning);

//                intent.putExtra("chapterOrder", positionData.toString());

                    intent.putExtra("chapterOrder", "1");

                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_down, R.anim.anim_slide_out_up);
                } else {
                    Integer positionTemp = position - 1;

                    String userCheck = _studyLists.get(positionTemp).getUserCheck();

                    if(userCheck.equals("Y")) {
                        intent = new Intent(StudyChapterActivity.this, StudyChapterStartActivity.class);
                        intent.putExtra("classesCode", classesCode);
                        intent.putExtra("chapterCode", chapterCode);
                        intent.putExtra("chapterName", chapterName);
                        intent.putExtra("chapterLearning", chapterLearning);


                        intent.putExtra("chapterOrder", "1");

                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_down, R.anim.anim_slide_out_up);
                    } else {
                        Toast.makeText(getApplication(), "이전 단계를 완료하신 후 수업이 가능합니다", Toast.LENGTH_LONG).show();
                        //Toast.makeText(this, "이전 단계를 완료하신 후 수업이 가능합니다", Toast.LENGTH_LONG).show();
                    }
                }



            }




        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1000: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    intent = new Intent(StudyChapterActivity.this, StudyChapterStartActivity.class);
                    intent.putExtra("classesCode", classesCode);
                    intent.putExtra("chapterCode", chapterCode);
                    intent.putExtra("chapterName", chapterName);
                    intent.putExtra("chapterLearning", chapterLearning);
//                    intent.putExtra("chapterOrder", positionData.toString());
                    intent.putExtra("chapterOrder", "1");
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_down, R.anim.anim_slide_out_up);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "권한을 승인해주세요.", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }





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
        super.onDestroy();
        actManager.removeActivity(this);
    }



}
