package liveenglishclass.com.talkstar;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import liveenglishclass.com.talkstar.adapter.CommandAdapter;

import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.dto.CommandDTO;
import liveenglishclass.com.talkstar.dto.CommandList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jccho on 2018.4. 4..
 */

public class CommandFragment extends Fragment {

    private final String debugTag = "CommandFragment";
    private Retrofit retrofit;
    ApiService apiService;


    private ListView listView;
    private ArrayList<CommandDTO> _commandLists;
    private CommandAdapter command_adapter;


    private Intent intent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_command, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        _commandLists = new ArrayList<>();




        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        Log.d("test", "OK");
        this._dataList();
        super.onActivityCreated(savedInstanceState);

    }

    private void _dataList()
    {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);

                Call<CommandList> call = apiService.CommandList();
                call.enqueue(new Callback<CommandList>() {

                    @Override
                    public void onResponse(Call<CommandList> call, Response<CommandList> response) {

                        String err_code = response.body().err_code;
                        if(err_code.equals("000")) {

                            _commandLists = response.body().getDatas();


                            command_adapter = new CommandAdapter(getActivity(), _commandLists);
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


}
