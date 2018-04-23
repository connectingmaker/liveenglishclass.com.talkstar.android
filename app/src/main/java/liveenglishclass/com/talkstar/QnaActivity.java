package liveenglishclass.com.talkstar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import liveenglishclass.com.talkstar.core.ActivityManager;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.custom.CustormLoadingDialog;
import liveenglishclass.com.talkstar.dto.QnaDTO;
import liveenglishclass.com.talkstar.util.Shared;
import liveenglishclass.com.talkstar.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QnaActivity extends AppCompatActivity {

    private Intent intent;
    private EditText activity_qna_et_q;
    private Retrofit retrofit;
    ApiService apiService;
    private ActivityManager actManager = ActivityManager.getInstance();
    private String qna_q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna);


        activity_qna_et_q = (EditText) findViewById(R.id.activity_qna_et_q);
    }

    public void btnClickEvent(View v) {
        //Log.d("test", "OK");
        switch (v.getId()) {
            case R.id.preview:
                onBackPressed();
                break;

            case R.id.qnaactivity_btn:

                Log.d("test", "Qna_btn");
                qna_q = activity_qna_et_q.getText().toString();
                if(qna_q.equals("") == true){
                    Toast.makeText(this, "의견 내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                    activity_qna_et_q.requestFocus();
                }else {
                    qnaProcess();
                }
                break;
        }
    }


    private void qnaProcess()
    {
        Log.d("qnaProcess_test", "processOK");
        final String qna_id = Shared.getPerferences(QnaActivity.this, "SESS_UID");
        Log.d("test", qna_id);

        final String qna_q = activity_qna_et_q.getText().toString();


        final CustormLoadingDialog dialog = new CustormLoadingDialog(this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);

                Call<QnaDTO> call = apiService.Qna_Process(qna_id , qna_q);
                call.enqueue(new Callback<QnaDTO>() {
                    @Override
                    public void onResponse(Call<QnaDTO> call, Response<QnaDTO> response) {
                        QnaDTO QnaDTO = response.body();
                        Log.d("ERR_CODE", QnaDTO.ERR_CODE);

                        dialog.dismiss();

                        //actManager.finishAllActivity();
                        //intent = new Intent(QnaActivity.this, SettingFragment.class);
                        //startActivity(intent);
                        Toast.makeText(getApplication(), "정상적으로 접수되었습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<QnaDTO> call, Throwable t) {
                        Toast.makeText(QnaActivity.this, "네트워크 오류 발생", Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        Log.d("test", "BACK");
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }


}
