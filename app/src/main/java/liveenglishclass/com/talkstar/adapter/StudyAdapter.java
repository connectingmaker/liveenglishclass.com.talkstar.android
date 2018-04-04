package liveenglishclass.com.talkstar.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import liveenglishclass.com.talkstar.R;
import liveenglishclass.com.talkstar.dto.StudyDTO;

public class StudyAdapter extends ArrayAdapter<StudyDTO> {

    List<StudyDTO> studyList;
    private LayoutInflater mInflater;

    Context context;

    private TextView classes_name, question_cnt;
    private RatingBar classes_level;

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

        StudyDTO item = getItem(position);

        classes_name = (TextView) convertView.findViewById(R.id.classes_name);
        question_cnt = (TextView) convertView.findViewById(R.id.question_cnt);
        classes_level = (RatingBar) convertView.findViewById(R.id.classes_level);


        Log.d("test", item.getClassName());
        classes_name.setText(item.getClassName());
        classes_level.setRating(Float.parseFloat(item.getClassLevel()));
        question_cnt.setText(Html.fromHtml("<span color='#404040'><font size='30'>"+item.getUserQCnt().toString()+"</font><small>/"+item.getPartCnt().toString()+"</small></span>"));


        return convertView;

    }


}