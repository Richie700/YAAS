package com.in.den.android.yaas;

import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.in.den.android.yaas.counter.GemeEngineFragment;

/*
*** 2D game for android : Space Shooter YASS (Yet Another Space Shooter)
*** code written by following instructions from
*
***-----------------------------------------------------------------------
*** "Mastering Android Game Development" by Raul Pautals (Edition Packed)
***-----------------------------------------------------------------------
***
* * with some modifications.
*
*** Credit for graphical and sound materials from openart.org
*** ---------------------------------------------------------------------------
***  http://opengameart.org/content/space-asteroids-shooter-pack (Michael Carey)
*** ----------------------------------------------------------------------------
 ** laser8
 ** sound http://opengameart.org/content/laser-fire by  dklo
 *
 ** Muffled Distant Explosion.wa
 ** http://opengameart.org/content/muffled-distant-explosion by NenadSimic
 *
 ** Chunky Explosion Joth
 ** http://opengameart.org/content/chunky-explosion
 *
 */


public class YassActivity extends AppCompatActivity {

    public static String GAMEFRAGMENT = "GAMEFRAGMENT";
    private SoundManager soundManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yass);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
/*
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, new MenuFragment(), YassActivity.GAMEFRAGMENT)
                .commit();*/

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, new GemeEngineFragment(), YassActivity.GAMEFRAGMENT)

                .commit();

        soundManager = new SoundManager(this);
        soundManager.loadSounds();

    }


    @Override
    public void onBackPressed() {
        final YassFragment fragment = (YassFragment)
                getSupportFragmentManager().findFragmentByTag(GAMEFRAGMENT);
        if (!fragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void navigateBack() {
        super.onBackPressed();
    }
/*
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LOW_PROFILE);
            }
            else {
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
            }
        }
        */


    public SoundManager getSoundManager() {
        return soundManager;
    }
}
