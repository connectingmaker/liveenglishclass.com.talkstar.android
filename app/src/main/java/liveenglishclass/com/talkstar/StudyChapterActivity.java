package liveenglishclass.com.talkstar;

import android.content.Intent;
import android.os.AsyncTask;
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

import liveenglishclass.com.talkstar.adapter.StudyAdapter;
import liveenglishclass.com.talkstar.adapter.StudyChapterAdapter;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.dto.StudyChapterDTO;
import liveenglishclass.com.talkstar.dto.StudyChapterList;
import liveenglishclass.com.talkstar.dto.StudyList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudyChapterActivity extends AppCompatActivity {
    private final String debugTag = "StudyChapterActivity";
    private Retrofit retrofit;
    ApiService apiService;


    private TextView activity_studypart_title, activity_studychapter_question, activity_studychapter_question_skip;
    private ListView activity_studypart_list;

    private ArrayList<StudyChapterDTO> _studyLists;
    private StudyChapterAdapter study_adapter;


    private Intent intent;
    private String classesName;
    private String classesCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_chapter);

        _init();
    }

    private void _init()
    {

        //activity_studypart_title = (TextView) findViewById(R.id.activity_studypart_title);
        activity_studychapter_question = (TextView) findViewById(R.id.activity_studychapter_question);
        activity_studychapter_question_skip = (TextView) findViewById(R.id.activity_studychapter_question_skip);
        activity_studypart_list = (ListView) findViewById(R.id.activity_studypart_list);






        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            classesCode =(String) b.get("classesCode");
            classesName = (String) b.get("classesName");

            //activity_studypart_title.setText(classesName);



        } else {

        }




        final View header = getLayoutInflater().inflate(R.layout.activity_study_chapter_header, null, false) ;



        _studyLists = new ArrayList<>();




        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);

                Call<StudyChapterList> call = apiService.StudyChapterList("11111", classesCode);
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
        /*
        switch(v.getId()) {
            case R.id.activity_study_topLeft_btn:
            case R.id.activity_stidy_topleft_image_btn:
                finish();
                break;
        }
        */
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long l_position) {

            intent = new Intent(StudyChapterActivity.this, StudyChapterStartActivity.class);
            startActivity(intent);

        }
    };



}
