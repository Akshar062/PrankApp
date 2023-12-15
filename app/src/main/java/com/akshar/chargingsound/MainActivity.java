package com.akshar.chargingsound;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private VolumeChangeReceiver volumeChangeReceiver;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize AudioManager
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Register BroadcastReceiver for volume changes
        volumeChangeReceiver = new VolumeChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(volumeChangeReceiver, filter);

        // Increase overall device volume
        increaseDeviceVolume();

        // Play the chicken sound
        playChickenSound();

        startHomeActivity();
    }

    @Override
    protected void onDestroy() {
        // Unregister the BroadcastReceiver when the activity is destroyed
        unregisterReceiver(volumeChangeReceiver);
        super.onDestroy();
    }

    private void increaseDeviceVolume() {
        try {
            // Get the maximum volume
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

            // Set the volume to a higher level (e.g., 80% of max volume)
            int targetVolume = (int) (0.5 * maxVolume);

            // Increase the volume
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, targetVolume, 0);
        } catch (Exception e) {
            Log.e("VolumeControl", "Exception: " + e.getMessage());
        }
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

    // BroadcastReceiver to handle volume changes
    public class VolumeChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null &&
                    intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {

                // Get the current volume
                int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

                // Get the maximum volume
                int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

                // Set the volume back to 80% if it goes below
                if (currentVolume < 0.5 * maxVolume) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            (int) (0.5 * maxVolume), 0);
                }
            }
        }
    }

    // gesture detection

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        startHomeActivity();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    // on long press
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Log.d("MainActivity", "User is leaving the app");
        startHomeActivity();
    }


    @Override
    protected void onPause() {
        super.onPause();
        startHomeActivity();
        Log.d("MainActivity", "App is going into the background");
    }


    private void startHomeActivity() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startHomeActivity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startHomeActivity();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startHomeActivity();
    }
}
