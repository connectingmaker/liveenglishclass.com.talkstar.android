package liveenglishclass.com.talkstar.core;

import java.util.List;

import liveenglishclass.com.talkstar.dto.Contributor;
import liveenglishclass.com.talkstar.dto.MemberDTO;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by kwangheejung on 2018. 3. 7..
 */

public interface ApiService {
    //접근 URL
    public static final String API_URL = " http://192.168.0.10:7890/";

    @GET("repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> contributors(@Path("owner") String owner, @Path("repo") String repo);

    @FormUrlEncoded
    @POST("member/userJoinSuccess")
    Call<MemberDTO> MemberDTO_userJoinSuccess(@Field("email") String email, @Field("username") String username, @Field("phone_number") String phone_number, @Field("pwd") String pwd);

}
