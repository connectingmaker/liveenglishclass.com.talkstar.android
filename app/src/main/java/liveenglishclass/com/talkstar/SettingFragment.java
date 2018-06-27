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
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import liveenglishclass.com.talkstar.core.ActivityManager;
import liveenglishclass.com.talkstar.util.Shared;
import liveenglishclass.com.talkstar.util.Util;

/**
 * Created by kwangheejung on 2018. 3. 5..
 */

public class SettingFragment extends Fragment implements View.OnClickListener {
    private Activity activity;
    private LinearLayout setting_logout, fragment_setting_privacy, fragment_setting_term, fragment_setting_qna, fragment_setting_notice, fragment_setting_version,fragment_setting_command;
    private Intent intent;



    private final String debugTag = "SettingFragment";


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

        fragment_setting_privacy = (LinearLayout) getActivity().findViewById(R.id.fragment_setting_privacy);
        fragment_setting_privacy.setOnClickListener(this);

        fragment_setting_term = (LinearLayout) getActivity().findViewById(R.id.fragment_setting_term);
        fragment_setting_term.setOnClickListener(this);

        fragment_setting_qna = (LinearLayout) getActivity().findViewById(R.id.fragment_setting_qna);
        fragment_setting_qna.setOnClickListener(this);

        fragment_setting_notice = (LinearLayout) getActivity().findViewById(R.id.fragment_setting_notice);
        fragment_setting_notice.setOnClickListener(this);

        fragment_setting_command = (LinearLayout) getActivity().findViewById(R.id.fragment_setting_command);
        fragment_setting_command.setOnClickListener(this);


        fragment_setting_version = (LinearLayout) getActivity().findViewById(R.id.fragment_setting_version);
        fragment_setting_version.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.setting_logout:

                Shared.removePrefercences(getActivity(), "SESS_UID");
                Shared.removePrefercences(getActivity(), "SESS_USEREMAIL");
                Shared.removePrefercences(getActivity(), "SESS_USERNAME");

                ((MainActivity)getActivity()).appLogout();

                break;
            case R.id.fragment_setting_privacy:
                Log.d(debugTag, "OK");

                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("TITLE", "개인정보");
                intent.putExtra("URL", "member/privacy");
                startActivity(intent);
                break;

            case R.id.fragment_setting_term:
                intent = new Intent(getActivity(),WebViewActivity.class);
                intent.putExtra("TITLE", "이용약관");
                intent.putExtra("URL","member/agree");
                startActivity(intent);
                break;

            case R.id.fragment_setting_qna:
                Log.d(debugTag,"qna");
                intent = new Intent(getActivity(),QnaActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);

                break;

            case R.id.fragment_setting_notice:
                intent = new Intent(getActivity(),NoticeActivity.class);

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;
            case R.id.fragment_setting_command:
                intent = new Intent(getActivity(),CommandActivity.class);

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;
            case R.id.fragment_setting_version:
                String version = Util.getAppVersion(getActivity());
                Toast.makeText(getActivity(), "버전 = " + version, Toast.LENGTH_LONG).show();
                break;
        }

    }

}
