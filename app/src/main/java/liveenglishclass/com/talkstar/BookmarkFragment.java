package liveenglishclass.com.talkstar;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import liveenglishclass.com.talkstar.adapter.BookMarkAdapter;
import liveenglishclass.com.talkstar.adapter.CommandAdapter;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.custom.CustormLoadingDialog;
import liveenglishclass.com.talkstar.dto.CommandDTO;
import liveenglishclass.com.talkstar.dto.CommandList;
import liveenglishclass.com.talkstar.dto.QuestionClass;
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


    private ListView bookmark_listView;
    private ArrayList<StudyBookMarkDTO> _bookmarkLists;
    private BookMarkAdapter bookmark_adapter;

    private Handler mHandler;
    private Runnable mRunnable;

    private String UID;

    private Thread threadAuto;

    private Intent intent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        bookmark_listView = (ListView) view.findViewById(R.id.bookmark_listView);
        _bookmarkLists = new ArrayList<>();


//        _threadAutoInit();


//        Log.d("test", "호출");

//        bookmark_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("test", "list view 클릭");
////                switch(view.getId()) {
////                    case R.id.bookmark_delete_btn:
////                        Log.d("test", "삭제");
////                        break;
////
////
////                    default:
////                        Log.d("test", "VIEW");
////                        break;
////                }
////                Log.d("test", "아이템 클릭");
////                String classesCode = _bookmarkLists.get(position).getCLAESS_CODE();
////                String chapterCode = _bookmarkLists.get(position).getCHAPTER_CODE();
////                String chapterOrder = String.valueOf(_bookmarkLists.get(position).getORDERID());
////                String bookmark = "Y";
////
////                intent = new Intent(getActivity(), StudyChapterQuestionActivity_New.class);
////                intent.putExtra("classesCode", classesCode);
////                intent.putExtra("chapterCode", chapterCode);
////                intent.putExtra("chapterOrder", chapterOrder);
////                intent.putExtra("bookmark", bookmark);
////
////
////                startActivity(intent);
////                getActivity().overridePendingTransition(R.anim.anim_slide_in_down, R.anim.anim_slide_out_up);
//
//            }
//        });



        return view;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        this._dataList();
        super.onActivityCreated(savedInstanceState);

    }

    public void onStart()
    {
        this._dataList();

        super.onStart();


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


                        bookmark_listView.setAdapter(bookmark_adapter);


                        //listView.setOnItemClickListener(mItemClickListener);

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


//    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
//        @RequiresApi(api = Build.VERSION_CODES.M)
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position,
//                                long l_position) {
//
//            Log.d("test", "클릭");
//
//            String classesCode = _bookmarkLists.get(position-1).getCLAESS_CODE();
//            String chapterCode = _bookmarkLists.get(position-1).getCHAPTER_CODE();
//            String chapterOrder = String.valueOf(_bookmarkLists.get(position-1).getORDERID());
//            String bookmark = "Y";
//
//            intent = new Intent(getActivity(), StudyChapterQuestionActivity_New.class);
//            intent.putExtra("classesCode", classesCode);
//            intent.putExtra("chapterCode", chapterCode);
//            intent.putExtra("chapterOrder", chapterOrder);
//            intent.putExtra("bookmark", bookmark);
//
//
//            startActivity(intent);
//            getActivity().overridePendingTransition(R.anim.anim_slide_in_down, R.anim.anim_slide_out_up);
//
//
//
//
//        }
//    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override

        public void onClick(View v) {
            Log.d("test", "클릭이벤트");
        }

    };


}
