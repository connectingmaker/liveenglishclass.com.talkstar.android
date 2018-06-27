package liveenglishclass.com.talkstar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;

import liveenglishclass.com.talkstar.adapter.CommandAdapter;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.custom.CustormLoadingDialog;
import liveenglishclass.com.talkstar.dto.CommandDTO;
import liveenglishclass.com.talkstar.dto.CommandList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommandActivity extends AppCompatActivity {

    private Retrofit retrofit;
    ApiService apiService;


    private ListView listView;
    private ArrayList<CommandDTO> _commandLists;
    private CommandAdapter command_adapter;

    private Handler mHandler;
    private Runnable mRunnable;


    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);

        _layoutInit();


        _dataList();

    }

    public void btnClickEvent(View v) {
        switch (v.getId()) {
            case R.id.activity_command_prev_btn:
                onBackPressed();
                break;

        }
    }


    private void _layoutInit()
    {
        listView = (ListView) findViewById(R.id.command_list);
    }

    private void _dataList() {

        final CustormLoadingDialog dialog = new CustormLoadingDialog(this);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);

                Call<CommandList> call = apiService.CommandList();
                call.enqueue(new Callback<CommandList>() {

                    @Override
                    public void onResponse(Call<CommandList> call, Response<CommandList> response) {

                        mRunnable = new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        };

                        mHandler = new Handler();
                        mHandler.postDelayed(mRunnable, 1000);


                        String err_code = response.body().err_code;
                        if (err_code.equals("000")) {

                            _commandLists = response.body().getDatas();


                            command_adapter = new CommandAdapter(getBaseContext(), _commandLists);
                            listView.setAdapter(command_adapter);


                        } else {
                        }
                    }

                    @Override
                    public void onFailure(Call<CommandList> call, Throwable t) {

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