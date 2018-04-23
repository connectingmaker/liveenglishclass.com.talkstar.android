package liveenglishclass.com.talkstar;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Member;
import java.util.regex.Pattern;

import liveenglishclass.com.talkstar.core.ActivityManager;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.dto.MemberDTO;
import liveenglishclass.com.talkstar.util.Shared;
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
        memberjoin_et_email = (EditText)findViewById(R.id.memberjoin_et_email);
        memberjoin_et_username = (EditText)findViewById(R.id.memberjoin_et_username);
        memberjoin_et_phone = (EditText)findViewById(R.id.memberjoin_et_phone);
        memberjoin_et_pw = (EditText)findViewById(R.id.memberjoin_et_pw);
        memberjoin_et_pw2 = (EditText)findViewById(R.id.memberjoin_et_pw2);

        memberjoin_et_email.setNextFocusDownId(R.id.memberjoin_et_username);

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

        /*** 핸드폰 엔터 ***********/
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

        /*** 비밀번호 엔터 ***********/
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

        /*** 비밀번호2 엔터 ***********/
        memberjoin_et_pw2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == event.KEYCODE_ENTER) {
                    keyboardHide();
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

            case R.id.activity_member_privacy:
                intent = new Intent(MemberJoinActivity.this, WebViewActivity.class);
                intent.putExtra("TITLE", "개인정보");
                intent.putExtra("URL", "member/privacy");
                startActivity(intent);
                break;


            case R.id.activity_member_agree:
                intent = new Intent(MemberJoinActivity.this, WebViewActivity.class);
                intent.putExtra("TITLE", "이용약관");
                intent.putExtra("URL", "member/agree");
                startActivity(intent);
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
        String Token = "";
        if(Shared.getPerferences(this,"SESS_TOKEN").equals("") == true) {
            Token = Util.getToken();
            Shared.savePreferences(this,"SESS_TOKEN", Token);
        } else {
            Token = Shared.getPerferences(this,"SESS_TOKEN");
        }
        final String DeviceName = Util.getDeviceName();
        final String DeviceModel = Util.getDeviceModel();
        final String OSVersion = Util.getAndroidVersion();
        final String finalToken = Token;
        /******** 회원가입 네트워크 연동 ************/


        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);
                Call<MemberDTO> call = apiService.MemberDTO_userJoinSuccess(txt_email, txt_username, txt_phone, txt_pwd, finalToken, DeviceName, DeviceModel, OSVersion);
                call.enqueue(new Callback<MemberDTO>() {
                    @Override
                    public void onResponse(Call<MemberDTO> call, Response<MemberDTO> response) {
                        MemberDTO memberDTO = response.body();

                        if(memberDTO.ERR_CODE.equals("000") == true) {
                            Shared.savePreferences(MemberJoinActivity.this, "SESS_UID", memberDTO.UID);
                            Shared.savePreferences(MemberJoinActivity.this, "SESS_TOKEN", finalToken);
                            Shared.savePreferences(MemberJoinActivity.this, "SESS_USEREMAIL", txt_email);
                            Shared.savePreferences(MemberJoinActivity.this, "SESS_USERNAME", txt_username);

                            intent = new Intent(MemberJoinActivity.this, MemberJoinFinishActivity.class);
                            startActivity(intent);
                        } else {
                            String ERROR = Util.getStringValue(MemberJoinActivity.this, memberDTO.ERR_CODE);
                            Toast.makeText(MemberJoinActivity.this, ERROR, Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<MemberDTO> call, Throwable t) {
                        Toast.makeText(MemberJoinActivity.this, "네트워크 오류 발생", Toast.LENGTH_LONG).show();
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
