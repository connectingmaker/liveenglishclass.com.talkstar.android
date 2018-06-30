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
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.timqi.sectorprogressview.ColorfulRingProgressView;

import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.custom.CustormLoadingDialog;
import liveenglishclass.com.talkstar.dto.MypageDTO;
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

    private Handler mHandler;
    private Runnable mRunnable;


    private String UID;

    private ColorfulRingProgressView circular_progress_bar;
    private TextView star_count,star_count_yesterday,context,per;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_finish);

        per = (TextView) findViewById(R.id.per);
        star_count = (TextView) findViewById(R.id.star_count);
        star_count_yesterday = (TextView) findViewById(R.id.star_count_yesterday);
        context = (TextView) findViewById(R.id.context);
        circular_progress_bar = (ColorfulRingProgressView) findViewById(R.id.circular_progress_bar);

        _dataList();

    }
//    Intent intent = new Intent(getApplicationContext(), StudyFinishActivity.class);
//    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//    //intent.putExtra("fragment_move", "mypage");
//    startActivity(intent);
//    finish();

    public void btnClickEvent(View v) {
        switch (v.getId()) {
            case R.id.activity_notice_prev_btn:

                onBackPressed();
                break;

        }
    }

    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("fragment_move", "study");
        startActivity(intent);
        finish();

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



                Call<MypageDTO> call = apiService.Mypage(UID);
                call.enqueue(new Callback<MypageDTO>() {
                    @Override
                    public void onResponse(Call<MypageDTO> call, Response<MypageDTO> response) {
                        dialog.dismiss();
                        MypageDTO mypageDTO = response.body();

                        per.setText(String.valueOf(mypageDTO.PER));
                        star_count.setText(String.valueOf(mypageDTO.STAR_COUNT));

                        if(mypageDTO.STAR_COUNT_YESTERDAY2 - mypageDTO.STAR_COUNT_YESTERDAY > 0){
                            star_count_yesterday.setText(Html.fromHtml("<b><span style='color:#5b76eb;'> + "+String.valueOf(mypageDTO.STAR_COUNT_YESTERDAY+"</b>")));
                            context.setText("학습량이 줄었는데 분발하세요!");
                        }else if(mypageDTO.STAR_COUNT_YESTERDAY2 - mypageDTO.STAR_COUNT_YESTERDAY < 0){
                            star_count_yesterday.setText(Html.fromHtml("<b><span style='color:#b01c1c;'> + "+String.valueOf(mypageDTO.STAR_COUNT_YESTERDAY+"</b>")));
                            context.setText("와우! 이전보다 더 열심히 하셨네요! ");
                        }else{
                            star_count_yesterday.setText(Html.fromHtml("<b><span style='color:#353333;'> + "+String.valueOf(mypageDTO.STAR_COUNT_YESTERDAY+"</b>")));
                            context.setText("꾸준히 연습하세요!");
                        }

                        //circular_progress_bar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

                        //circular_progress_bar.setProgress(mypageDTO.PER);

                        circular_progress_bar.setPercent(mypageDTO.PER);
                        circular_progress_bar.setStartAngle(0);
                        circular_progress_bar.setFgColorStart(0xffffe400);
                        circular_progress_bar.setFgColorEnd(0xffff4800);
                        circular_progress_bar.setStrokeWidthDp(21);



//                        fragment_main_ing_study.setText(Html.fromHtml("<b><span style='color:#5b76eb;'>"+String.valueOf(mypageDTO.CHAPTER_ALL)+"</span>챕터 중 <span style='color:#5b76eb;'>"+String.valueOf(mypageDTO.USER_CHAPTER_COMPLATE)+"</span>챕터 진행 완료</b>"), TextView.BufferType.SPANNABLE);


//                        english_1.setText(Html.fromHtml("<b><span style='color:#5b76eb;'>"+mypageDTO.ENGLISH1+"</span></b>"), TextView.BufferType.SPANNABLE);
//                        english_2.setText(Html.fromHtml("<b><span style='color:#5b76eb;'>"+mypageDTO.ENGLISH2+"</span></b>"), TextView.BufferType.SPANNABLE);
                    }

                    @Override
                    public void onFailure(Call<MypageDTO> call, Throwable t) {
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
