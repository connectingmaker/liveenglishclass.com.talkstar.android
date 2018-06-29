package liveenglishclass.com.talkstar;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;

import liveenglishclass.com.talkstar.adapter.CommandAdapter;
import liveenglishclass.com.talkstar.adapter.MemberCommandAdapter;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.custom.CustormLoadingDialog;
import liveenglishclass.com.talkstar.dto.CommandDTO;
import liveenglishclass.com.talkstar.dto.CommandList;
import liveenglishclass.com.talkstar.dto.MemberCommandDTO;
import liveenglishclass.com.talkstar.dto.MemberCommandList;
import liveenglishclass.com.talkstar.util.Shared;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kwangheejung on 2018. 3. 19..
 */

public class VoiceFragment extends Fragment {


    private final String debugTag = "CommandFragment";
    private Retrofit retrofit;
    ApiService apiService;

    private ListView listView;
    private ArrayList<MemberCommandDTO> _commandLists;
    private MemberCommandAdapter command_adapter;

    private Handler mHandler;
    private Runnable mRunnable;


    private String UID;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        UID = Shared.getPerferences(getActivity(), "SESS_UID");


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voice, container, false);

        listView = (ListView) view.findViewById(R.id.voiceListview);
        this._dataList();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        Log.d("test", "OK");

        super.onActivityCreated(savedInstanceState);

    }



    private void _dataList() {

        final CustormLoadingDialog dialog = new CustormLoadingDialog(getActivity());
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);



                Call<MemberCommandList> call = apiService.MemberCommandList(UID);
                call.enqueue(new Callback<MemberCommandList>() {
                    @Override
                    public void onResponse(Call<MemberCommandList> call, Response<MemberCommandList> response) {
                        _commandLists = response.body().getDatas();


                        mRunnable = new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        };

                        mHandler = new Handler();
                        mHandler.postDelayed(mRunnable, 1000);





                        command_adapter = new MemberCommandAdapter(getActivity(), _commandLists);
                        listView.setAdapter(command_adapter);

                        listView.setSelection(command_adapter.getCount() - 1);

                    }

                    @Override
                    public void onFailure(Call<MemberCommandList> call, Throwable t) {

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

    public void addItem(Integer SEQ, String ACTION_CODE, String COMMAND_VOICE, String COMMAND_RETURN)
    {
        MemberCommandDTO dto = new MemberCommandDTO();
        dto.setSEQ(SEQ);
        dto.setACTION_CODE(ACTION_CODE);
        dto.setCOMMAND_VOICE(COMMAND_VOICE);
        dto.setCOMMAND_RETURN(COMMAND_RETURN);
        _commandLists.add(dto);
        command_adapter.notifyDataSetChanged();
        Integer position = _commandLists.size() - 1;

        listView.setSelection(position);
    }
}
