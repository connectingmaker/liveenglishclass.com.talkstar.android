package liveenglishclass.com.talkstar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import liveenglishclass.com.talkstar.core.ApiService;
import liveenglishclass.com.talkstar.util.Util;

public class WebViewActivity extends AppCompatActivity {

    private WebView webviewactivity_view;
    private TextView webview_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        _init();
    }


    private void _init() {
        webviewactivity_view = (WebView) findViewById(R.id.webviewactivity_view);
        webview_title = (TextView) findViewById(R.id.webview_title);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();


        if(b!=null)
        {
            String url =(String) b.get("URL");
            String title = (String) b.get("TITLE");
            Log.d("test", ApiService.API_URL + url);

            webviewactivity_view.loadUrl(ApiService.API_URL + url);
            webview_title.setText(title);
        } else {
            Toast.makeText(this, "URL이 셋팅되지 않았습니다", Toast.LENGTH_LONG).show();
        }
    }

    public void webviewClickEvent(View v) {
        switch(v.getId()) {
            case R.id.activity_webview_prev_btn:
                finish();
                break;
        }
    }
}
