package liveenglishclass.com.talkstar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import liveenglishclass.com.talkstar.R;
import liveenglishclass.com.talkstar.dto.NoticeDTO;

public class NoticeAdapter extends ArrayAdapter<NoticeDTO> {

    List<NoticeDTO> noticeList;
    private LayoutInflater mInflater;


    Context context;

    private TextView notice_title, notice_content;

    // Constructors
    public NoticeAdapter(Context context, List<NoticeDTO> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        noticeList = objects;
    }

    @Override
    public NoticeDTO getItem(int position) {
        return noticeList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_notice_row_list, parent, false);



            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_study_row_list, parent, false);
        }


        notice_title = (TextView) convertView.findViewById(R.id.notice_title);
        notice_content = (TextView) convertView.findViewById(R.id.notice_content);

        NoticeDTO item = getItem(position);

        notice_title.setText(item.getNOTICE_TITLE());
        notice_content.setText(item.getNOTICE_CONTENT());


        return convertView;

    }
}
