package liveenglishclass.com.talkstar;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import liveenglishclass.com.talkstar.adapter.StudyAdapter;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.dto.MemberLoginDTO;
import liveenglishclass.com.talkstar.dto.StudyDTO;
import liveenglishclass.com.talkstar.dto.StudyList;
import liveenglishclass.com.talkstar.util.Shared;
import liveenglishclass.com.talkstar.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kwangheejung on 2018. 3. 5..
 */

public class StudyFragment extends Fragment {
    private final String debugTag = "StudyFragment";
    private Retrofit retrofit;
    ApiService apiService;


    private ListView listView;
    private ArrayList<StudyDTO> _studyLists;
    private StudyAdapter study_adapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study, container, false);


        listView = (ListView) view.findViewById(R.id.listView);
        _studyLists = new ArrayList<>();
        //this._dataList();


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        this._dataList();
        super.onActivityCreated(savedInstanceState);

    }



    private void _dataList()
    {
        Log.d(debugTag, "OK");
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);

                Call<StudyList> call = apiService.StudyList("11111");
                call.enqueue(new Callback<StudyList>() {
                    @Override
                    public void onResponse(Call<StudyList> call, Response<StudyList> response) {
                        /*Log.d(debugTag, response.body().err_code);*/
                        String err_code = response.body().err_code;
                        if(err_code.equals("000")) {
                            _studyLists = response.body().getDatas();

                            study_adapter = new StudyAdapter(getActivity(), _studyLists);

                            listView.setAdapter(study_adapter);

                            //ArrayAdapter study_adapter = new ArrayAdapter(getActivity(), android.R.layout.activity_list_item, _studyLists);
                            //listView.setAdapter(study_adapter);

                        } else {
                            Toast.makeText(getActivity(), "오류발생", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<StudyList> call, Throwable t) {

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
