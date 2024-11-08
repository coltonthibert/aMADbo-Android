package com.example.amadbo;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * AppAudioService class to handle sound effects and music
 */
public class AppAudioService {
    private static MediaPlayer mediaPlayer;
    private static MediaPlayer backgroundMusic;


    /**
     * Play a sound effect
     * @param context Context of the activity
     * @param soundId the sound id
     * @param loop whether to loop the sound
     */
    public static void playSound(Context context, int soundId, boolean loop){
        if (!Settings.getSoundPref(context)) return; // if sound is disabled, return
        mediaPlayer = MediaPlayer.create(context, soundId);
        mediaPlayer.setVolume(0.75f, 0.75f);
        mediaPlayer.setLooping(loop);
        mediaPlayer.start();

        // cull the sound if it's not looping
        if (!loop){
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopSound();
                }
            });
        }

    }

    /**
     * Stop the sound effect
     */
    public static void stopSound(){
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * Play the background music
     * @param context Context of the activity
     * @param soundId the sound id
     */
    public static void playBackgroundMusic(Context context, int soundId) {
        if (!Settings.getMusicPref(context)) return; // if music is disabled, return
        backgroundMusic = MediaPlayer.create(context, soundId);
        backgroundMusic.setVolume(0.25f, 0.25f);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();
    }

    /**
     * Stop the background music
     */
    public static void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.release();
            backgroundMusic = null;
        }
    }

    /**
     * Pause the background music
     */
    public static void pauseBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.pause();
        }
    }

    /**
     * Resume the background music
     */
    public static void resumeBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.start();
        }
    }
}