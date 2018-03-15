package liveenglishclass.com.talkstar;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import liveenglishclass.com.talkstar.core.ActivityManager;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.dto.MemberDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MemberJoinActivity extends AppCompatActivity {
    private ActivityManager actManager = ActivityManager.getInstance();
    private Intent intent;


    private final String debugTag = "MainActivity";
    private Retrofit retrofit;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_join);

        actManager.addActivity(this);

        this.memberEvent();
    }


    public void btnClickEvent(View v) {
        switch(v.getId()){
            case R.id.memberjoin_btn_complate:
                intent = new Intent(MemberJoinActivity.this, MemberJoinFinishActivity.class);
                break;
        }

        startActivity(intent);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        actManager.removeActivity(this);
    }

    private void memberEvent()
    {
        /******** 회원가입 네트워크 연동 ************/
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);
                Call<MemberDTO> call = apiService.MemberDTO_userJoinSuccess();
                call.enqueue(new Callback<MemberDTO>() {
                    @Override
                    public void onResponse(Call<MemberDTO> call, Response<MemberDTO> response) {
                        MemberDTO memberDTO = response.body();
                        Log.d(debugTag, memberDTO.ERR_CODE);
                        Log.d(debugTag, response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<MemberDTO> call, Throwable t) {

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
