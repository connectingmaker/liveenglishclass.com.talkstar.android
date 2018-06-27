package liveenglishclass.com.talkstar.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import liveenglishclass.com.talkstar.Application;
import liveenglishclass.com.talkstar.MainActivity;
import liveenglishclass.com.talkstar.R;
import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.dto.CommandDTO;
import liveenglishclass.com.talkstar.dto.StudyBookMark;
import liveenglishclass.com.talkstar.dto.StudyBookMarkDTO;
import liveenglishclass.com.talkstar.dto.StudyBookMarkList;
import liveenglishclass.com.talkstar.util.Property;
import liveenglishclass.com.talkstar.util.Shared;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookMarkAdapter extends ArrayAdapter<StudyBookMarkDTO>
 {

    List<StudyBookMarkDTO> commandList;
    private LayoutInflater mInflater;

    private Retrofit retrofit;
    ApiService apiService;

    Context context;

    private TextView command_group, command_name;
    private String UID;
    private String studyCode = "";
    private Integer _position = -1;

    private Property property;


    // Constructors
    public BookMarkAdapter(Context context, List<StudyBookMarkDTO> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        commandList = objects;



    }

    @Override
    public StudyBookMarkDTO getItem(int position) {
        return commandList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();


        SharedPreferences sharedPreferences = context.getSharedPreferences("talkstarKeyValue9991@", context.MODE_PRIVATE);
        UID = sharedPreferences.getString("SESS_UID", "");;


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_bookmark_row_list, parent, false);
        }


        StudyBookMarkDTO item = getItem(position);


        TextView english_string = (TextView) convertView.findViewById(R.id.english_string);
        ImageButton study_bookmark_btn = (ImageButton) convertView.findViewById(R.id.study_bookmark_btn);
//        study_bookmark_btn.setOnClickListener((View.OnClickListener) context);
        study_bookmark_btn.setTag(position);




        english_string.setText(item.getENGLISH_STRING());
        //study_bookmark_btn.setOnClickListener(this);
        study_bookmark_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudyBookMarkDTO item = getItem((Integer) v.getTag());

                studyCode = item.getSTUDY_CODE();
                _position = (Integer) v.getTag();
                _bookmarkSave();
                //Log.d("test", v.getTag().toString());
            }
        });



        return convertView;

    }

    private void _bookmarkSave()
    {
        /******** UID 값 가져오기 ***************/


        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                apiService = retrofit.create(ApiService.class);
                Call<StudyBookMark> call = apiService.StudyBook(UID, studyCode);
                call.enqueue(new Callback<StudyBookMark>() {

                    @Override
                    public void onResponse(Call<StudyBookMark> call, Response<StudyBookMark> response) {
                        StudyBookMark studyDTO = response.body();

                        if(studyDTO.USE_YN.equals("Y")) {
                            Log.d("test", "OK");
                        } else {
                            //StudyBookMarkDTO item = getItem(_position);
                            commandList.remove(_position);
                            notifyDataSetChanged();
                            //commandList.notify();
                            _position = -1;

                        }

                    }

                    @Override
                    public void onFailure(Call<StudyBookMark> call, Throwable t) {

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
