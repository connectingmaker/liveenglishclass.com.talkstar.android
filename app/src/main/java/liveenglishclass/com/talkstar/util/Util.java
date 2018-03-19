package liveenglishclass.com.talkstar.util;

import android.content.Context;
import android.os.Build;
import android.util.TypedValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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




}
