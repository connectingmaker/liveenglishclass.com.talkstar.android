package liveenglishclass.com.talkstar.core;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import liveenglishclass.com.talkstar.dto.CommandDTO;
import liveenglishclass.com.talkstar.dto.CommandList;
import liveenglishclass.com.talkstar.dto.Contributor;
import liveenglishclass.com.talkstar.dto.MemberCommandList;
import liveenglishclass.com.talkstar.dto.MemberDTO;
import liveenglishclass.com.talkstar.dto.MemberLoginDTO;
import liveenglishclass.com.talkstar.dto.MypageDTO;
import liveenglishclass.com.talkstar.dto.NoticeList;
import liveenglishclass.com.talkstar.dto.StudyBookMark;
import liveenglishclass.com.talkstar.dto.StudyBookMarkList;
import liveenglishclass.com.talkstar.dto.StudyChapterList;
import liveenglishclass.com.talkstar.dto.StudyDTO;
import liveenglishclass.com.talkstar.dto.StudyFinish;
import liveenglishclass.com.talkstar.dto.StudyList;
import liveenglishclass.com.talkstar.dto.QnaDTO;
import liveenglishclass.com.talkstar.dto.StudyNextDTO;
import liveenglishclass.com.talkstar.dto.StudyStartDTO;
import liveenglishclass.com.talkstar.dto.StudyStartDTO_20180620;
import liveenglishclass.com.talkstar.dto.VoiceSearchDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by kwangheejung on 2018. 3. 7..
 */

public interface ApiService {
    //접근 URL
    public static final String API_URL = "http://192.168.0.10:7890/";
    //public static final String API_URL = "http://www.brs.kr:7890/";

    @GET("repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> contributors(@Path("owner") String owner, @Path("repo") String repo);

    @FormUrlEncoded
    @POST("member/userJoinSuccess")
    Call<MemberDTO> MemberDTO_userJoinSuccess(
            @Field("email") String email
            , @Field("username") String username
            , @Field("phone_number") String phone_number
            , @Field("pwd") String pwd
            , @Field("Token") String Token
            , @Field("DeviceName") String DeviceName
            , @Field("DeviceModel") String DeviceModel
            , @Field("OSVersion") String OSVersion
    );

    @FormUrlEncoded
    @POST("member/loginProcess")
    Call<MemberLoginDTO> MemberLogin_Process(
            @Field("useremail") String useremail
            ,@Field("userpwd") String userpwd
            ,@Field("token") String token
    );

    @FormUrlEncoded
    @POST("member/qnaProcess")
    Call<QnaDTO> Qna_Process(
            @Field("uid") String uid
            ,@Field("question") String question

    );


    Call<MemberLoginDTO> MemberLogin_Process(String login_et_id, String login_et_pw);


    @GET("study/list")
    //Call<List<StudyDTO>> listStudy(@Query("uid") String uid);
    Call<StudyList> StudyList(@Query("uid") String uid);


    @GET("study/chapter")
        //Call<List<StudyDTO>> listStudy(@Query("uid") String uid);
    Call<StudyChapterList> StudyChapterList(@Query("uid") String uid, @Query("classes_code") String classes_code);

    @GET("voice/search")
    Call<VoiceSearchDTO> voiceSearch(@Query("uid") String uid, @Query("searchName") String searchName);


    /*********** 공지사항 ******************/
    @GET("member/noticeList")
    Call<NoticeList> NoticeList();

    /************ 명령어 *****************/
    @GET("command/list")
    Call<CommandList> CommandList();


    @GET("command/memberCommand")
    Call<MemberCommandList> MemberCommandList(@Query("uid") String uid);

    /********** 수업시작 ******************/
    @GET("study/start")
    Call<StudyStartDTO> StudyStart(@Query("uid") String uid, @Query("classes_code") String classes_code, @Query("chapter_code") String chapter_code);

    @GET("study/start_20180620")
    Call<StudyStartDTO_20180620> StudyStart_20180620(@Query("uid") String uid, @Query("classes_code") String classes_code, @Query("chapter_code") String chapter_code, @Query("orderId") String orderId, @Query("studyCode") String studyCode, @Query("questionAnswer") String questionAnswer);


    /********** 수업다음진행 ***************/
    @GET("study/finish")
    Call<StudyFinish> StudyFinish(@Query("uid") String uid, @Query("classes_code") String classes_code, @Query("chapter_code") String chapter_code);

    @GET("study/bookmark")
    Call<StudyBookMark> StudyBook(@Query("uid") String uid, @Query("study_code") String study_code);


    @GET("study/bookmarklist")
    Call<StudyBookMarkList> StudyBookMarkList(@Query("uid") String uid);


    /********** 수업다음진행 ***************/
    @FormUrlEncoded
    @GET("study/next")
    Call<StudyStartDTO> StudyNext(@Query("uid") String uid, @Query("classes_code") String classes_code, @Query("chapter_code") String chapter_code, @Query("part_code") String part_code, @Query("orderid") Integer orderid, @FieldMap Map<String, String> fields);


    @FormUrlEncoded
    @POST("study/next_reject")
    Call<StudyStartDTO> StudyNext_Reject(@Query("uid") String uid, @Query("classes_code") String classes_code, @Query("chapter_code") String chapter_code, @Query("part_code") String part_code, @Query("orderid") Integer orderid, @FieldMap Map<String, String> fields);



    @GET("member/mypage")
    Call<MypageDTO> Mypage(@Query("uid") String uid);


//    @FormUrlEncoded
//    @POST("study/next")
//    Call<StudyStartDTO> StudyNext2(@Query("uid") String uid, @Query("classes_code") String classes_code, @Query("chapter_code") String chapter_code, @Query("part_code") String part_code, @Query("orderid") Integer orderid, @FieldMap Map<String, String> fields);
}
