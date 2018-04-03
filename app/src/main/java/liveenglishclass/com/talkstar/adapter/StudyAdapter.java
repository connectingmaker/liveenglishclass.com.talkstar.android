package liveenglishclass.com.talkstar.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import liveenglishclass.com.talkstar.R;
import liveenglishclass.com.talkstar.dto.StudyDTO;

public class StudyAdapter extends ArrayAdapter<StudyDTO> {

    List<StudyDTO> studyList;
    private LayoutInflater mInflater;

    Context context;

    private TextView question_cnt;

    // Constructors
    public StudyAdapter(Context context, List<StudyDTO> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        studyList = objects;
    }

    @Override
    public StudyDTO getItem(int position) {
        return studyList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_study_row_list, parent, false);

            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_study_row_list, parent, false);
        }


        question_cnt = (TextView) convertView.findViewById(R.id.question_cnt);
//        question_cnt.setText(Html.fromHtml("<span style='color:#000;'><b style='font-size:14em'>0</b>/3</span>"));

        question_cnt.setText("1111");


        return convertView;

    }


}