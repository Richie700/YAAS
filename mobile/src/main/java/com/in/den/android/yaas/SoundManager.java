package com.in.den.android.yaas;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.in.den.android.yaas.engine.GameEvent;

import java.io.IOException;
import java.util.HashMap;

/**
 * Sound credit
 * laser8
 * http://opengameart.org/content/laser-fire by  dklo
 *
 * Muffled Distant Explosion.wa
 * http://opengameart.org/content/muffled-distant-explosion by NenadSimic
 *
 * Chunky Explosion Joth
 * http://opengameart.org/content/chunky-explosion
 */

public class SoundManager {

    private final Context mContext;
    private int MAX_STREAMS = 5;
    private SoundPool mSoundPool;
    private HashMap<GameEvent, Integer> mSoundsMap;
    private String classname = SoundManager.class.getSimpleName();
    private boolean mSoundEnabled;
    private static final String SOUNDS_PREF_KEY="sound_pref_key";


    public SoundManager(Context context) {
        mContext = context;
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(context);
        mSoundEnabled = prefs.getBoolean(SOUNDS_PREF_KEY, true);
    }

    public void setSoundDisable() {
        mSoundEnabled= false;
        setSoundSetting(mSoundEnabled);
    }

    public void setSoundEnable() {
        mSoundEnabled= true;
        setSoundSetting(mSoundEnabled);
    }

    public boolean isSoundEnabled() {
        return mSoundEnabled;
    }

    private void setSoundSetting(boolean setting) {
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(mContext);

        prefs.edit().putBoolean(SOUNDS_PREF_KEY, setting).commit();
    }

    private void createSoundPool() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mSoundPool = new SoundPool(MAX_STREAMS,
                    AudioManager.STREAM_MUSIC, 0);
        }
        else {
            AudioAttributes audioAttributes = new
                    AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            mSoundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(MAX_STREAMS)
                    .build();
        }
    }

    private void loadEventSound(Context context, GameEvent event, String
            filename) {
        try {
            AssetFileDescriptor descriptor =
                    context.getAssets().openFd("sfx/" + filename);
            int soundId = mSoundPool.load(descriptor, 1);
            mSoundsMap.put(event, soundId);
        } catch (IOException e) {
            Log.e(classname, "unable to load sound " + e.getMessage());

        }
    }

    private void unloadSounds() {
        mSoundPool.release();
        mSoundPool = null;
        mSoundsMap.clear();
    }

    public void loadSounds() {
        createSoundPool();
        mSoundsMap = new HashMap<GameEvent, Integer>();

        loadEventSound(mContext, GameEvent.AsteroidHit,
                "Muffled Distant Explosion.wav");
        loadEventSound(mContext, GameEvent.SpaceshipHit, "Chunky Explosion.mp3");

        loadEventSound(mContext, GameEvent.LaserFired,
                "laser8.wav");
    }

    public void playSoundForGameEvent(GameEvent event) {
        if(!mSoundEnabled) return;

        Integer soundId = mSoundsMap.get(event);
        if (soundId != null) {
            mSoundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

}
