package liveenglishclass.com.talkstar;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import liveenglishclass.com.talkstar.core.ActivityManager;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.dto.MemberDTO;
import liveenglishclass.com.talkstar.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MemberJoinActivity extends AppCompatActivity {
    private ActivityManager actManager = ActivityManager.getInstance();
    private Intent intent;


    private final String debugTag = "MemberJoinActivity";
    private Retrofit retrofit;
    ApiService apiService;


    private LinearLayout memberjoin_layout;
    private EditText memberjoin_et_email;
    private EditText memberjoin_et_username;
    private EditText memberjoin_et_phone;
    private EditText memberjoin_et_pw;
    private EditText memberjoin_et_pw2;


    private String txt_email = "";
    private String txt_username = "";
    private String txt_phone = "";
    private String txt_pwd = "";
    private String txt_pwd2 = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_join);

        actManager.addActivity(this);

        init();
    }

    private void init()
    {
        memberjoin_et_email = (EditText) findViewById(R.id.memberjoin_et_email);
        memberjoin_et_username = (EditText) findViewById(R.id.memberjoin_et_username);
        memberjoin_et_phone = (EditText) findViewById(R.id.memberjoin_et_phone);
        memberjoin_et_pw = (EditText) findViewById(R.id.memberjoin_et_pw);
        memberjoin_et_pw2 = (EditText) findViewById(R.id.memberjoin_et_pw2);

        memberjoin_layout = (LinearLayout) findViewById(R.id.memberjoin_layout);
        memberjoin_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboardHide();
            }
        });


        /*** 이메일 엔터 ********/
        memberjoin_et_email.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == event.KEYCODE_ENTER) {
                    keyboardHide();
                    memberjoin_et_username.requestFocus();
                    return true;
                }
                return false;
            }
        });

        /*** 이름 엔터 ***********/
        memberjoin_et_username.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == event.KEYCODE_ENTER) {
                    memberjoin_et_phone.requestFocus();
                    return true;
                }
                return false;
            }
        });

        /*** 이름 엔터 ***********/
        memberjoin_et_phone.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == event.KEYCODE_ENTER) {
                    memberjoin_et_pw.requestFocus();
                    return true;
                }
                return false;
            }
        });

        /*** 이름 엔터 ***********/
        memberjoin_et_pw.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == event.KEYCODE_ENTER) {
                    memberjoin_et_pw2.requestFocus();
                    return true;
                }
                return false;
            }
        });

        /*** 이름 엔터 ***********/
        memberjoin_et_pw2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == event.KEYCODE_ENTER) {
                    return true;
                }
                return false;
            }
        });
    }


    private void keyboardHide()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void btnClickEvent(View v) {
        switch(v.getId()){
            case R.id.memberjoin_btn_complate:

                /******* 필드 체크 ********/
                if(filedCheck() == true) {
                    /*
                    intent = new Intent(MemberJoinActivity.this, MemberJoinFinishActivity.class);
                    startActivity(intent);
                    */

                    memberCheck();
                }


                break;
        }


    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        actManager.removeActivity(this);
    }


    private boolean filedCheck()
    {
        txt_email = memberjoin_et_email.getText().toString();
        txt_username = memberjoin_et_username.getText().toString();
        txt_phone = memberjoin_et_phone.getText().toString();
        txt_pwd = memberjoin_et_pw.getText().toString();
        txt_pwd2 = memberjoin_et_pw2.getText().toString();


        memberjoin_et_email.setBackgroundResource(R.drawable.login_edittext);
        memberjoin_et_username.setBackgroundResource(R.drawable.login_edittext);
        memberjoin_et_phone.setBackgroundResource(R.drawable.login_edittext);
        memberjoin_et_pw.setBackgroundResource(R.drawable.login_edittext);
        memberjoin_et_pw2.setBackgroundResource(R.drawable.login_edittext);


        if(Util.validateEmail(txt_email) == false) {


            Toast.makeText(this, "이메일 형식이 올바르지 않습니다", Toast.LENGTH_SHORT).show();
            memberjoin_et_email.setBackgroundResource(R.drawable.login_edittext_not);
            memberjoin_et_email.requestFocus();
            return false;
        }

        if(txt_username.equals("") == true) {
            Toast.makeText(this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            memberjoin_et_username.setBackgroundResource(R.drawable.login_edittext_not);
            memberjoin_et_username.requestFocus();
            return false;
        }

        if(txt_phone.equals("") == true) {
            Toast.makeText(this, "핸드폰번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            memberjoin_et_phone.setBackgroundResource(R.drawable.login_edittext_not);
            memberjoin_et_phone.requestFocus();
            return false;
        }

        if(Util.validatePassword(txt_pwd) == false) {
            Toast.makeText(this, "패스워드 4~16자리로 입력해주세요", Toast.LENGTH_SHORT).show();
            memberjoin_et_pw.setBackgroundResource(R.drawable.login_edittext_not);
            memberjoin_et_pw.requestFocus();
            return false;
        }

        if(txt_pwd.equals(txt_pwd2) == false) {
            Toast.makeText(this, "패스워드가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
            memberjoin_et_pw2.setBackgroundResource(R.drawable.login_edittext_not);
            memberjoin_et_pw2.requestFocus();
            return false;
        }

        /*
        if(Util.validatePhone(phone) == false) {
            Log.d(debugTag, "핸드폰번호가 올바르지 않습니다");
            return false;
        }
        */

        return true;
    }

    private void memberCheck()
    {
        /******** 회원가입 네트워크 연동 ************/
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);
                Call<MemberDTO> call = apiService.MemberDTO_userJoinSuccess(txt_email, txt_username, txt_phone, txt_pwd);
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
