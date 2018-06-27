package liveenglishclass.com.talkstar.adapter;

import android.content.Context;
import android.media.Image;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

    private TextView classes_name, question_cnt, study_progressTxt;
    private RatingBar classes_level;
    private ImageView fragment_study_icon;
    private LinearLayout study_linearLayout;

    private ProgressBar pg;

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
        fragment_study_icon = (ImageView) convertView.findViewById(R.id.fragment_study_icon);

        pg = (ProgressBar) convertView.findViewById(R.id.study_progressbar);
        study_progressTxt = (TextView) convertView.findViewById(R.id.study_progressTxt);


        Log.d("test", item.getClassName());
        classes_name.setText(item.getClassName());
        classes_level.setRating(Float.parseFloat(item.getClassLevel()));
        pg.setProgress(item.getTotalPer());
        study_progressTxt.setText(item.getTotalPer().toString() + "%");

        study_linearLayout = (LinearLayout) convertView.findViewById(R.id.study_linearLayout);

        if(item.getPerOrder() == 1) {
            fragment_study_icon.setImageResource(R.mipmap.fragment_study_icon_on);
        } else {
            fragment_study_icon.setImageResource(R.mipmap.fragment_study_icon_off);
        }

        if(item.getPerOrder() == 0) {
            study_linearLayout.setBackgroundResource(R.drawable.study_round_btn);
        } else {
            study_linearLayout.setBackgroundResource(R.drawable.study_round_disabled);
        }

        question_cnt.setText(Html.fromHtml("<span color='#404040'><font size='30'>"+item.getUserQCnt().toString()+"</font><small>/"+item.getPartCnt().toString()+"</small></span>"));


        return convertView;

    }


}