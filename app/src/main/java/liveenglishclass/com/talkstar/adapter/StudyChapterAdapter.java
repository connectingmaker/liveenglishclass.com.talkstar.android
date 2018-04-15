package liveenglishclass.com.talkstar.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import liveenglishclass.com.talkstar.R;
import liveenglishclass.com.talkstar.dto.StudyChapterDTO;
import liveenglishclass.com.talkstar.dto.StudyDTO;

public class StudyChapterAdapter extends ArrayAdapter<StudyChapterDTO> {

    List<StudyChapterDTO> studyList;
    private LayoutInflater mInflater;

    Context context;

    private TextView chapter_name, chapter_sentence, chapter_learning;
    private RatingBar classes_level;

    // Constructors
    public StudyChapterAdapter(Context context, List<StudyChapterDTO> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        studyList = objects;
    }

    @Override
    public StudyChapterDTO getItem(int position) {
        return studyList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_study_chapter_row_list, parent, false);

            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_study_row_list, parent, false);
        }

        chapter_name = (TextView) convertView.findViewById(R.id.chapter_name);
        chapter_sentence = (TextView) convertView.findViewById(R.id.chapter_sentence);
        chapter_learning = (TextView) convertView.findViewById(R.id.chapter_learning);

        StudyChapterDTO item = getItem(position);

        chapter_name.setText(item.getChapterName());
        chapter_sentence.setText(item.getSentence());
        chapter_learning.setText(item.getLearningNotes());


        return convertView;

    }


}