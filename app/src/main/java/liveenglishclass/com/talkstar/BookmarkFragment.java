package liveenglishclass.com.talkstar;

import android.app.Fragment;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import liveenglishclass.com.talkstar.adapter.BookMarkAdapter;
import liveenglishclass.com.talkstar.adapter.CommandAdapter;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.custom.CustormLoadingDialog;
import liveenglishclass.com.talkstar.dto.CommandDTO;
import liveenglishclass.com.talkstar.dto.CommandList;
import liveenglishclass.com.talkstar.dto.StudyBookMarkDTO;
import liveenglishclass.com.talkstar.dto.StudyBookMarkList;
import liveenglishclass.com.talkstar.dto.StudyStartDTO;
import liveenglishclass.com.talkstar.dto.StudyStartDTO_20180620;
import liveenglishclass.com.talkstar.util.Shared;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jccho on 2018.4. 4..
 */

public class BookmarkFragment extends Fragment {

    private final String debugTag = "BookMarkFragment";
    private Retrofit retrofit;
    ApiService apiService;


    private ListView listView;
    private ArrayList<StudyBookMarkDTO> _bookmarkLists;
    private BookMarkAdapter bookmark_adapter;

    private Handler mHandler;
    private Runnable mRunnable;

    private String UID;


    private Intent intent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        _bookmarkLists = new ArrayList<>();



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

        UID = Shared.getPerferences(getActivity(), "SESS_UID");
        final CustormLoadingDialog dialog = new CustormLoadingDialog(getActivity());
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);

                Call<StudyBookMarkList> call = apiService.StudyBookMarkList(UID);
                call.enqueue(new Callback<StudyBookMarkList>() {

                    @Override
                    public void onResponse(Call<StudyBookMarkList> call, Response<StudyBookMarkList> response) {

                        mRunnable = new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        };

                        mHandler = new Handler();
                        mHandler.postDelayed(mRunnable, 1000);

                        _bookmarkLists = response.body().getDatas();


                        bookmark_adapter = new BookMarkAdapter(getActivity(), _bookmarkLists);
                        listView.setAdapter(bookmark_adapter);

                    }

                    @Override
                    public void onFailure(Call<StudyBookMarkList> call, Throwable t) {

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
