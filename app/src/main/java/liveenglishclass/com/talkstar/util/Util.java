package liveenglishclass.com.talkstar.util;

import android.os.Build;

/**
 * Created by kwangheejung on 2018. 3. 8..
 */

public class Util {
    /********* 디바이스 명 가져오기 ******/
    public String getDeviceName() {
        return Build.BRAND;
    }

    /********* 모델명 가져오기 ******/
    public String getDeviceModel() {
        return Build.MODEL;
    }

    /********* 안드로이드 버전 가져오기 ******/
    public String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }
}
