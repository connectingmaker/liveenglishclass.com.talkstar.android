package liveenglishclass.com.talkstar.util;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by kwangheejung on 2018. 3. 8..
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "firebaseIDService";

    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String token = FirebaseInstanceId.getInstance().getToken();

    }


}
