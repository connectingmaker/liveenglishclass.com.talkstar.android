package liveenglishclass.com.talkstar.util;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import liveenglishclass.com.talkstar.R;

/**
 * Created by kwangheejung on 2018. 3. 8..
 */

public class Util {
    //이메일 정규식
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    //비밀번호정규식
    public static final Pattern VALID_PASSWOLD_REGEX_ALPHA_NUM = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$"); // 4자리 ~ 16자리까지 가능


    //핸드폰번호정규식
    public static final Pattern VAILD_PHONE_REGEX = Pattern.compile("^01(?:0|1|[6-9]) - (?:\\d{3}|\\d{4}) - \\d{4}$");

    /********* 디바이스 명 가져오기 ******/
    public static String getDeviceName() {
        return Build.BRAND;
    }

    /********* 모델명 가져오기 ******/
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /********* 안드로이드 버전 가져오기 ******/
    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }


    /******** DP -> PX 변환 ****************/
    public static int dp2px(Context context, int dp){
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

    /****** 이메일 여부 확인 ******************/
    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }


    /****** 패스워드 정규식 *****************/
    public static boolean validatePassword(String pwStr) {
        Matcher matcher = VALID_PASSWOLD_REGEX_ALPHA_NUM.matcher(pwStr);
        return matcher.matches();
    }

    /****** 핸드폰번호 정규식 *****************/
    public static boolean validatePhone(String number) {
        Matcher matcher = VAILD_PHONE_REGEX.matcher(number);
        return matcher.matches();
    }


    /******* Token 가져오기 *****************/
    public static String getToken()
    {
        try {
            String token = FirebaseInstanceId.getInstance().getToken();
            return token;
        } catch(Exception e) {
            return "";
        }
    }


    public static String getStringValue(Activity activity, String value)
    {

        String packName = activity.getPackageName();
        String ActivityName = activity.getLocalClassName();
        String resName = "@string/"+ActivityName+"_"+value;
        int resID = activity.getResources().getIdentifier(resName, "string", packName);
        if(resID == 0) {
            return "";
        } else {
            return activity.getResources().getString(resID);
        }

    }

    public static String getAppVersion(Context ctx)
    {
        String version = "";
        try {
            version = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("tag", e.getMessage());
        }

        return version;
    }




}
