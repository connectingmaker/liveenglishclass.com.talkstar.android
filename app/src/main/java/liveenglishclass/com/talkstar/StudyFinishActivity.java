package liveenglishclass.com.talkstar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.timqi.sectorprogressview.ColorfulRingProgressView;

import liveenglishclass.com.talkstar.core.ActivityManager;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.custom.CustormLoadingDialog;
import liveenglishclass.com.talkstar.dto.MypageDTO;
import liveenglishclass.com.talkstar.dto.StudyFinishResultDTO;
import liveenglishclass.com.talkstar.util.Shared;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudyFinishActivity extends AppCompatActivity {
    private final String debugTag = "HomeFragment";
    private Retrofit retrofit;
    ApiService apiService;

    private ActivityManager actManager = ActivityManager.getInstance();

    private Handler mHandler;
    private Runnable mRunnable;


    private String UID;

    private ColorfulRingProgressView circular_progress_bar;
    private TextView star_count,star_count_now,per;
    private TextView  star_count_yesterday,context;

    private String classesCode;
    private String chapterCode;
    private String classesCode_next;
    private String chapterCode_next;
    private String classesName;
    private String chapterName;
    private String chapterLearning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_finish);

        actManager.addActivity(this);

        star_count_now = (TextView) findViewById(R.id.star_count_now);
        per = (TextView) findViewById(R.id.per);
        star_count = (TextView) findViewById(R.id.star_count);
        star_count_yesterday = (TextView) findViewById(R.id.star_count_yesterday);
        context = (TextView) findViewById(R.id.context);
        circular_progress_bar = (ColorfulRingProgressView) findViewById(R.id.circular_progress_bar);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            classesCode =(String) b.get("classesCode");
            chapterCode = (String) b.get("chapterCode");
        }

        Log.d("test", "OK");


        _dataList();

    }



    public void btnClickEvent(View v) {
        switch (v.getId()) {
            case R.id.activity_notice_prev_btn:

                onBackPressed();
                break;

            case R.id.NextChapter:

                Log.d("test", classesCode_next+"///" + chapterCode_next + "///" + chapterName + "///" + chapterLearning);
                Intent intent = new Intent(StudyFinishActivity.this, StudyChapterStartActivity.class);
                intent.putExtra("classesCode", classesCode_next);
                intent.putExtra("chapterCode", chapterCode_next);
                intent.putExtra("classesName", classesName);
                intent.putExtra("chapterName", chapterName);
                intent.putExtra("chapterLearning", chapterLearning);

                startActivity(intent);

                finish();
                break;

        }
    }

    public void onBackPressed() {

        actManager.finishAllActivity();

        Intent intent = new Intent(StudyFinishActivity.this, MainActivity.class);
        intent.putExtra("fragment_move", "mypage");
        startActivity(intent);
        //finish();

        //HomeFragment homeFragment = new HomeFragment();


        super.onBackPressed();
    }

    private void _dataList() {

        final CustormLoadingDialog dialog = new CustormLoadingDialog(this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        UID = Shared.getPerferences(this, "SESS_UID");
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);


                Call<StudyFinishResultDTO> call = apiService.StudyFinishResult(UID,classesCode,chapterCode);
                call.enqueue(new Callback<StudyFinishResultDTO>() {
                    @Override
                    public void onResponse(Call<StudyFinishResultDTO> call, Response<StudyFinishResultDTO> response) {
                        dialog.dismiss();
                        StudyFinishResultDTO studyFinishResultDTO = response.body();

                        per.setText(Html.fromHtml("<span style='font-size:10dp;'>전체 달성률</span> <br> <b><span style='color:#5b76eb;'> "+String.valueOf(studyFinishResultDTO.PER)+" % </b>"));
                        star_count.setText(String.valueOf(studyFinishResultDTO.STAR_COUNT));
                        star_count_now.setText(String.valueOf(studyFinishResultDTO.STAR_COUNT_NOW));

                        classesCode_next = String.valueOf(studyFinishResultDTO.CLASSES_CODE);
                        chapterCode_next = String.valueOf(studyFinishResultDTO.CHAPTER_CODE);
                        classesName = String.valueOf(studyFinishResultDTO.CLASSES_NAME);
                        chapterName = String.valueOf(studyFinishResultDTO.CHAPTER_NAME);
                        chapterLearning = String.valueOf(studyFinishResultDTO.LEARNING_NOTES);

                        if(studyFinishResultDTO.STAR_COUNT_YESTERDAY2 - studyFinishResultDTO.STAR_COUNT_YESTERDAY > 0){
                            star_count_yesterday.setText(Html.fromHtml("<b><span style='color:#5b76eb;'> + "+String.valueOf(studyFinishResultDTO.STAR_COUNT_YESTERDAY)+"</b>"));
                            context.setText("학습량이 줄었는데 분발하세요!");
                        }else if(studyFinishResultDTO.STAR_COUNT_YESTERDAY2 - studyFinishResultDTO.STAR_COUNT_YESTERDAY < 0){
                            star_count_yesterday.setText(Html.fromHtml("<b><span style='color:#b01c1c;'> + "+String.valueOf(studyFinishResultDTO.STAR_COUNT_YESTERDAY)+"</b>"));
                            context.setText("와우! 이전보다 더 열심히 하셨네요! ");
                        }else{
                            star_count_yesterday.setText(Html.fromHtml("<b><span style='color:#353333;'> + "+String.valueOf(studyFinishResultDTO.STAR_COUNT_YESTERDAY)+"</b>"));
                            context.setText("꾸준히 연습하세요!");
                        }


                        circular_progress_bar.setPercent(studyFinishResultDTO.PER);
                        circular_progress_bar.setStartAngle(0);
                        circular_progress_bar.setFgColorStart(0xffffe400);
                        circular_progress_bar.setFgColorEnd(0xffff4800);
                        circular_progress_bar.setStrokeWidthDp(21);

                    }

                    @Override
                    public void onFailure(Call<StudyFinishResultDTO> call, Throwable t) {
                        Log.d("test", "오류");
                        dialog.dismiss();
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
