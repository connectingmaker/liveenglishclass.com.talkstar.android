package liveenglishclass.com.talkstar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import liveenglishclass.com.talkstar.core.ApiService;

public class WebViewActivity extends AppCompatActivity {

    private WebView webviewactivity_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        _init();
    }


    private void _init() {
        webviewactivity_view = (WebView) findViewById(R.id.webviewactivity_view);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String url =(String) b.get("URL");
            Log.d("test", ApiService.API_URL + url);



            webviewactivity_view.loadUrl(ApiService.API_URL + url);
        } else {
            Toast.makeText(this, "URL이 셋팅되지 않았습니다", Toast.LENGTH_LONG).show();
        }
    }
}
