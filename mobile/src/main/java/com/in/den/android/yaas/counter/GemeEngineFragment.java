package com.in.den.android.yaas.counter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.in.den.android.yaas.SoundManager;
import com.in.den.android.yaas.engine.BasicInputController;
import com.in.den.android.yaas.engine.GameView;
import com.in.den.android.yaas.R;
import com.in.den.android.yaas.YassActivity;
import com.in.den.android.yaas.YassFragment;
import com.in.den.android.yaas.engine.GameEngine;

import static android.content.Context.WINDOW_SERVICE;

/**
 * A placeholder fragment containing a simple view.
 */
public class GemeEngineFragment extends YassFragment
        implements View.OnClickListener, GameControler.Callback{

    int mScreenWidth;
    int mScreenHeight;
    int mGameBordWidth;
    int mGamdBordHeight;
    private GameEngine mGameEngine;
    private ImageButton playPauseButton;
    private ImageButton replayButton;
    private ImageButton audioButton;
    private GameControler gameControler;
    private SoundManager soundManager;

    public GemeEngineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_yass, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initGameEngine(view);
    }

    private void initGameEngine(View view) {
        DisplayMetrics metrics = new DisplayMetrics();

        Display display = ((WindowManager) getContext().getSystemService(WINDOW_SERVICE))
                .getDefaultDisplay();

        display.getMetrics(metrics);

        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;

        mGameBordWidth = mScreenWidth - view.getPaddingRight() - view.getPaddingLeft();
        mGamdBordHeight = mScreenHeight - view.getPaddingBottom() - view.getPaddingTop();

        playPauseButton = (ImageButton) view.findViewById(R.id.btn_play_pause);
        playPauseButton.setOnClickListener(this);

        GameView gameView = (GameView) getView().findViewById(R.id.gameView);

        mGameEngine = new GameEngine(getActivity(), gameView, mGameBordWidth, mGamdBordHeight);

        gameControler = new GameControler(mGameEngine);
        gameControler.setCallback(this);

        replayButton = (ImageButton) view.findViewById(R.id.btn_replay);
        replayButton.setVisibility(Button.GONE);
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameControler.replay();

            }
        });

        soundManager = ((YassActivity)getActivity()).getSoundManager();

        audioButton = (ImageButton) view.findViewById(R.id.btn_audio_on) ;
        if(soundManager.isSoundEnabled()) {
            audioButton.setImageResource(R.drawable.audioon);
        }
        else {
            audioButton.setImageResource(R.drawable.audiooff);
        }

        audioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soundManager.isSoundEnabled()) {
                    soundManager.setSoundDisable();
                    audioButton.setImageResource(R.drawable.audiooff);
                }
                else {
                    soundManager.setSoundEnable();
                    audioButton.setImageResource(R.drawable.audioon);
                }
            }
        });

        mGameEngine.setInputController(new BasicInputController(getView()));
        mGameEngine.addGameObject(new ParallaxBackground(mGameEngine));
        mGameEngine.addGameObject(gameControler);

        mGameEngine.startGame();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_play_pause) {
            pauseGameAndShowPauseDialog();
        }
    }

    private void pauseGameAndShowPauseDialog() {
        mGameEngine.pauseGame();
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.pause_dialog_title)
                .setMessage(R.string.pause_dialog_message)
                .setPositiveButton(R.string.resume,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mGameEngine.resumeGame();
                            }
                        })
                .setNegativeButton(R.string.stop,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mGameEngine.stopGame();
                                ((YassActivity) getActivity()).navigateBack();
                            }
                        })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        mGameEngine.resumeGame();
                    }
                })
                .create()
                .show();
    }

    @Override
    public boolean onBackPressed() {
        if (mGameEngine.isRunning()) {
            pauseGameAndShowPauseDialog();
            return true;
        }
        return false;
    }


    @Override
    public void onPause() {
        super.onPause();

        if (mGameEngine.isRunning()) {
//            pauseGameAndShowPauseDialog();
        }


    }

    @Override
    public void onStop() {
        Log.d("GameEngineFragment", "onStop called");
        super.onStop();
        mGameEngine.stopGame();
    }

    @Override
    public void onDestroy() {
        Log.d("GameEngineFragment", "onDestroy called");
        super.onDestroy();
        mGameEngine.stopGame();
    }

    @Override
    public void changeReplayButtonVisibility(final boolean visible) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(visible) replayButton.setVisibility(Button.VISIBLE);
                else replayButton.setVisibility(Button.GONE);

            }
        });
    }

}
