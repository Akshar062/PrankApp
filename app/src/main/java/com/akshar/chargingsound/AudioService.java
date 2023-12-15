package com.akshar.chargingsound;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class AudioService extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        playChickenSound();
        return START_STICKY;
    }

    private void playChickenSound() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release(); // Release any previous instance
            }

            mediaPlayer = MediaPlayer.create(this, R.raw.chicken_sound);
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(1.0f, 1.0f); // Set volume to maximum
                mediaPlayer.setLooping(true); // Set to repeat continuously
                mediaPlayer.start();
            } else {
                Log.e("MediaPlayer", "Failed to create MediaPlayer");
            }
        } catch (Exception e) {
            Log.e("MediaPlayer", "Exception: " + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }
}
