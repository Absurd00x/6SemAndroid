package com.example.mireaproject;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import java.io.IOException;

public class MyService extends Service {
    private MediaPlayer mediaPlayer = null;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private final int NONE = -1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        if (mediaPlayer == null) {
            int fileId = intent.getIntExtra("id", NONE);
            if (fileId == NONE) {
                Uri fileUri = Uri.parse(intent.getStringExtra("uri"));
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioAttributes(
                        new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                );
                try {
                    mediaPlayer.setDataSource(getApplicationContext(), fileUri);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                mediaPlayer = MediaPlayer.create(this, fileId);
            }
            mediaPlayer.setLooping(true);
        }
        mediaPlayer.start();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}