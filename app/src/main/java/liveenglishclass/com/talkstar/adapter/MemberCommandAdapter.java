package liveenglishclass.com.talkstar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import liveenglishclass.com.talkstar.R;
import liveenglishclass.com.talkstar.dto.MemberCommandDTO;

public class MemberCommandAdapter extends ArrayAdapter<MemberCommandDTO> {
    List<MemberCommandDTO> commandList;
    private LayoutInflater mInflater;

    Context context;

    private TextView voicefragment_command_title, voicefragment_command_return;

    // Constructors
    public MemberCommandAdapter(Context context, List<MemberCommandDTO> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        commandList = objects;
    }

    @Override
    public MemberCommandDTO getItem(int position) {
        return commandList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_voice_row_list, parent, false);
        }


        voicefragment_command_title = (TextView) convertView.findViewById(R.id.voicefragment_command_title);
        voicefragment_command_return = (TextView) convertView.findViewById(R.id.voicefragment_command_return);

        MemberCommandDTO item = getItem(position);
        String commandVoice = item.getCOMMAND_VOICE();
        String commandReturn = item.getCOMMAND_RETURN();

        voicefragment_command_title.setText(commandVoice);
        voicefragment_command_return.setText(commandReturn);

        return convertView;

    }

}
