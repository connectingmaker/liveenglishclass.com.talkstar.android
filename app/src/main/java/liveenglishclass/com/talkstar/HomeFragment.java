package liveenglishclass.com.talkstar;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.timqi.sectorprogressview.ColorfulRingProgressView;
import com.timqi.sectorprogressview.SectorProgressView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import liveenglishclass.com.talkstar.adapter.MemberCommandAdapter;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.custom.CustormLoadingDialog;
import liveenglishclass.com.talkstar.dto.MemberCommandDTO;
import liveenglishclass.com.talkstar.dto.MemberCommandList;
import liveenglishclass.com.talkstar.dto.MypageDTO;
import liveenglishclass.com.talkstar.util.Shared;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kwangheejung on 2018. 3. 5..
 */

public class HomeFragment extends Fragment {

    private final String debugTag = "HomeFragment";
    private Retrofit retrofit;
    ApiService apiService;

    private Handler mHandler;
    private Runnable mRunnable;


    private String UID;


    private TextView  fragment_main_classes_name;
    private TextView fragment_main_ing_study;
    private TextView english_1, english_2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);


        fragment_main_classes_name = (TextView) view.findViewById(R.id.fragment_main_classes_name);
        fragment_main_ing_study = (TextView) view.findViewById(R.id.fragment_main_ing_study);
        fragment_main_ing_study.setText(Html.fromHtml("<b><span style='color:#5b76eb;'>20</span>챕터 중 <span style='color:#5b76eb;'>13</span>챕터 진행 완료</b>"), TextView.BufferType.SPANNABLE);
//        english_1 = (TextView) view.findViewById(R.id.english_1);
        english_2 = (TextView) view.findViewById(R.id.english_2);
        this._dataList();

        return view;
    }


    private void _dataList() {

        final CustormLoadingDialog dialog = new CustormLoadingDialog(getActivity());
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        UID = Shared.getPerferences(getActivity(), "SESS_UID");
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);



                Call<MypageDTO> call = apiService.Mypage(UID);
                call.enqueue(new Callback<MypageDTO>() {
                    @Override
                    public void onResponse(Call<MypageDTO> call, Response<MypageDTO> response) {
                        dialog.dismiss();
                        MypageDTO mypageDTO = response.body();
                        fragment_main_classes_name.setText(mypageDTO.CLASSES_NAME);
                        fragment_main_ing_study.setText(Html.fromHtml("<b><span style='color:#5b76eb;'>"+String.valueOf(mypageDTO.CHAPTER_ALL)+"</span>챕터 중 <span style='color:#5b76eb;'>"+String.valueOf(mypageDTO.USER_CHAPTER_COMPLATE)+"</span>챕터 진행 완료</b>"), TextView.BufferType.SPANNABLE);


//                        english_1.setText(Html.fromHtml("<b><span style='color:#5b76eb;'>"+mypageDTO.ENGLISH1+"</span></b>"), TextView.BufferType.SPANNABLE);
                        english_2.setText(Html.fromHtml("<b><span style='color:#5b76eb;'>"+mypageDTO.ENGLISH2+"</span></b>"), TextView.BufferType.SPANNABLE);
                    }

                    @Override
                    public void onFailure(Call<MypageDTO> call, Throwable t) {

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