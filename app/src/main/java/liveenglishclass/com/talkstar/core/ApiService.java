package liveenglishclass.com.talkstar.core;

import java.util.List;

import liveenglishclass.com.talkstar.dto.Contributor;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by kwangheejung on 2018. 3. 7..
 */

public interface ApiService {
    //접근 URL
    public static final String API_URL = " https://api.github.com/";

    @GET("repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> contributors(@Path("owner") String owner, @Path("repo") String repo);

}
