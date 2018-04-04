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

import java.util.List;

import liveenglishclass.com.talkstar.R;
import liveenglishclass.com.talkstar.dto.CommandDTO;
import liveenglishclass.com.talkstar.dto.StudyDTO;

public class CommandAdapter extends ArrayAdapter<CommandDTO> {

    List<CommandDTO> commandList;
    private LayoutInflater mInflater;

    Context context;

    private TextView command_group, command_name;

    // Constructors
    public CommandAdapter(Context context, List<CommandDTO> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        commandList = objects;
    }

    @Override
    public CommandDTO getItem(int position) {
        return commandList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_command_row_list, parent, false);
        }

        CommandDTO item = getItem(position);

        command_group = (TextView) convertView.findViewById(R.id.command_group);
        command_name = (TextView) convertView.findViewById(R.id.command_name);

        command_group.setText(item.getCOMMAND_GROUP());
        command_name.setText(item.getCOMMAND_NAME());


        return convertView;

    }


}
