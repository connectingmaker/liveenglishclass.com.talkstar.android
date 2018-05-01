package liveenglishclass.com.talkstar.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import liveenglishclass.com.talkstar.dto.QuestionClass;

public class PlayAudioManager {
    public static MediaPlayer mediaPlayer;
    public static Boolean voiceCheck = false;
    public static void playAudio(final Context context, final String url) throws Exception {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, Uri.parse(url));
        }
        voiceCheck = false;
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                killMediaPlayer();
            }
        });
        mediaPlayer.start();
    }
    public static void killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                voiceCheck = true;
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
