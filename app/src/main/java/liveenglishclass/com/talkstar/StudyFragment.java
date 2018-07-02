package liveenglishclass.com.talkstar;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import liveenglishclass.com.talkstar.adapter.StudyAdapter;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.custom.CustormLoadingDialog;
import liveenglishclass.com.talkstar.dto.StudyDTO;
import liveenglishclass.com.talkstar.dto.StudyList;
import liveenglishclass.com.talkstar.util.Shared;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.widget.Toast.*;

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


    private Intent intent;


    private Handler mHandler;
    private Runnable mRunnable;


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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    String classesCode = _studyLists.get(position).getClassCode();
                    String classesName = _studyLists.get(position).getClassName();

                    intent = new Intent(getActivity(), StudyChapterActivity.class);
                    intent.putExtra("classesCode", classesCode);
                    intent.putExtra("classesName", classesName);

                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                } else {
                    Integer position_temp = position - 1;
                    Integer checkPer_main = _studyLists.get(position).getPerOrder();
                    Integer checkPer = _studyLists.get(position_temp).getPerOrder();

                    //Log.d("test", String.valueOf(checkPer));

                    if(checkPer == 0 && checkPer_main == 0) {
                        Toast.makeText(getContext(), "이전 단계를 완료하신 후 수업이 가능합니다", Toast.LENGTH_LONG).show();

                    } else {
                        String classesCode = _studyLists.get(position).getClassCode();
                        String classesName = _studyLists.get(position).getClassName();

                        intent = new Intent(getActivity(), StudyChapterActivity.class);
                        intent.putExtra("classesCode", classesCode);
                        intent.putExtra("classesName", classesName);

                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);

                    }


                }

                //Log.d(debugTag, _studyLists.get(position).getClassName());
            }
        });
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
        final CustormLoadingDialog dialog = new CustormLoadingDialog(getActivity());
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);

                Call<StudyList> call = apiService.StudyList(Shared.getPerferences(getActivity(), "SESS_UID"));
                call.enqueue(new Callback<StudyList>() {
                    @Override
                    public void onResponse(Call<StudyList> call, Response<StudyList> response) {
                        mRunnable = new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        };

                        mHandler = new Handler();
                        mHandler.postDelayed(mRunnable, 1000);

                        /*Log.d(debugTag, response.body().err_code);*/
                        String err_code = response.body().err_code;
                        if(err_code.equals("000")) {
                            _studyLists = response.body().getDatas();

                            study_adapter = new StudyAdapter(getActivity(), _studyLists);

                            listView.setAdapter(study_adapter);

                            //ArrayAdapter study_adapter = new ArrayAdapter(getActivity(), android.R.layout.activity_list_item, _studyLists);
                            //listView.setAdapter(study_adapter);

                        } else {
                            makeText(getActivity(), "오류발생", LENGTH_LONG).show();
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
