package liveenglishclass.com.talkstar;

import android.util.Log;

import liveenglishclass.com.talkstar.core.FontOverride;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("test", "폰트설정");
        /** 나눔 폰트를 기본으로 설정한다 */
        FontOverride.setDefaultFont(this, "monospace", "nanumgothic.ttf");
    }
}