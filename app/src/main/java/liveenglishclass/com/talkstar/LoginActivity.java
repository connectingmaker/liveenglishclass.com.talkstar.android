package liveenglishclass.com.talkstar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import liveenglishclass.com.talkstar.core.ActivityManager;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.dto.MemberDTO;
import liveenglishclass.com.talkstar.dto.MemberLoginDTO;
import liveenglishclass.com.talkstar.util.Shared;
import liveenglishclass.com.talkstar.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private ActivityManager actManager = ActivityManager.getInstance();
    private Intent intent;

    private EditText activity_login_et_id;
    private EditText activity_login_et_pwd;


    private final String debugTag = "LoginActivity";
    private Retrofit retrofit;
    ApiService apiService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();


    }


    private void init()
    {
        actManager.addActivity(this);


        activity_login_et_id = (EditText) findViewById(R.id.activity_login_et_id);
        activity_login_et_pwd = (EditText) findViewById(R.id.activity_login_et_pwd);

        activity_login_et_id.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Log.d(keyCode);
                if(keyCode == event.KEYCODE_ENTER) {
                    keyboardHide();
                    activity_login_et_pwd.requestFocus();
                    return true;
                }
                return false;
            }
        });

        activity_login_et_pwd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == event.KEYCODE_ENTER) {
                    keyboardHide();
                    loginProcess();
                    //activity_login_et_pwd.requestFocus();
                    return true;
                }
                return false;
            }
        });
    }

    public void btnClickEvent(View v) {
        switch(v.getId()){
            case R.id.loginactivity_btn_login:
                loginProcess();
                break;
            case R.id.loginactivity_btn_join:
                intent = new Intent(LoginActivity.this, MemberJoinActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void loginProcess()
    {
        final String login_et_id = activity_login_et_id.getText().toString();
        final String login_et_pw = activity_login_et_pwd.getText().toString();
        String Token = "";
        if(!Shared.getPerferences(LoginActivity.this, "SESS_TOKEN").equals("")) {
            Token = Shared.getPerferences(LoginActivity.this, "SESS_TOKEN");
        } else {
            Token = Util.getToken();
            Shared.savePreferences(this,"SESS_TOKEN", Token);
        }
        final String finalToken = Token;


        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);
                Call<MemberLoginDTO> call = apiService.MemberLogin_Process(login_et_id, login_et_pw, finalToken);
                call.enqueue(new Callback<MemberLoginDTO>() {
                    @Override
                    public void onResponse(Call<MemberLoginDTO> call, Response<MemberLoginDTO> response) {
                        MemberLoginDTO memberLoginDTO = response.body();
                        Log.d("test", memberLoginDTO.ERR_CODE);
                        if(memberLoginDTO.ERR_CODE.equals("000")) {

                            Shared.savePreferences(LoginActivity.this, "SESS_UID", memberLoginDTO.UID);
                            Shared.savePreferences(LoginActivity.this, "SESS_TOKEN", finalToken);
                            Shared.savePreferences(LoginActivity.this, "SESS_USEREMAIL", memberLoginDTO.USEREMAIL);
                            Shared.savePreferences(LoginActivity.this, "SESS_USERNAME", memberLoginDTO.USERNAME);


                            actManager.finishAllActivity();
                            intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);



                        } else {
                            String ERROR = Util.getStringValue(LoginActivity.this, memberLoginDTO.ERR_CODE);
                            Toast.makeText(LoginActivity.this, ERROR, Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<MemberLoginDTO> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "네트워크 오류 발생", Toast.LENGTH_LONG).show();
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

    private void keyboardHide()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        actManager.removeActivity(this);
    }
}
