package liveenglishclass.com.talkstar;

/**
 * Created by jccho on 2018. 3. 6..
 */

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import liveenglishclass.com.talkstar.adapter.NoticeAdapter;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.dto.NoticeDTO;
import liveenglishclass.com.talkstar.dto.NoticeList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NoticeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView notice_list;

    private Retrofit retrofit;
    ApiService apiService;

    private ArrayList<NoticeDTO> _noticeLists;
    private NoticeAdapter notice_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        _init();
    }

    public void btnClickEvent(View v) {
        switch (v.getId()) {
            case R.id.preview:
                onBackPressed();
                break;
        }
    }

    private void _init()
    {
        notice_list = (ListView) findViewById(R.id.notice_list);

        _noticeLists = new ArrayList<>();


        notice_list.setOnItemClickListener(this);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);

                Call<NoticeList> call = apiService.NoticeList();
                call.enqueue(new Callback<NoticeList>() {

                    @Override
                    public void onResponse(Call<NoticeList> call, Response<NoticeList> response) {
                        Log.d("Debug", response.body().err_code);
                        String err_code = response.body().err_code;
                        if(err_code.equals("000")) {
                            _noticeLists = response.body().getDatas();

                            notice_adapter = new NoticeAdapter(getBaseContext(), _noticeLists);
                            notice_list.setAdapter(notice_adapter);


                        } else {
                        }
                    }

                    @Override
                    public void onFailure(Call<NoticeList> call, Throwable t) {

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout linear_notice_contents = view.findViewById(R.id.notice_content);
        //linear_notice_contents.setVisibility(View.VISIBLE);
    }
}
