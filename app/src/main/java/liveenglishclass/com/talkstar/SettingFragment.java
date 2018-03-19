package liveenglishclass.com.talkstar;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import liveenglishclass.com.talkstar.core.ActivityManager;
import liveenglishclass.com.talkstar.util.Shared;

/**
 * Created by kwangheejung on 2018. 3. 5..
 */

public class SettingFragment extends Fragment implements View.OnClickListener {
    private Activity activity;
    private LinearLayout setting_logout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        this.activity = (Activity)context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setting_logout = (LinearLayout) getActivity().findViewById(R.id.setting_logout);
        setting_logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Shared.removePrefercences(getActivity(), "SESS_UID");
        Shared.removePrefercences(getActivity(), "SESS_USEREMAIL");
        Shared.removePrefercences(getActivity(), "SESS_USERNAME");

        ((MainActivity)getActivity()).appLogout();
    }
}
