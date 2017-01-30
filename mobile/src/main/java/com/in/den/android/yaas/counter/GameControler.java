package com.in.den.android.yaas.counter;

import android.graphics.Canvas;
import com.in.den.android.yaas.R;
import com.in.den.android.yaas.engine.GameEngine;
import com.in.den.android.yaas.engine.GameEvent;
import com.in.den.android.yaas.engine.GameEventListner;
import com.in.den.android.yaas.engine.GameObject;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by harumi on 24/01/2017.
 */

public class GameControler extends GameObject implements GameEventListner {

    private long mCurrentMillis;
    private List<Asteroid> mAsteroidPool = new ArrayList<Asteroid>();
    private int mEnemiesSpawned = 0;
    private int TIME_BETWEEN_ENEMIES = 1000;
    private int INITIAL_ASTEROID_POOL_AMOUNT = 10;
    private GameEngine gameEngine;
    private GAMESTATE currentState = GAMESTATE.WAITSTART;
    private Player mPlayer;
    private Callback callback;
    private long mWaitingTime;
    private long WAITING_TIME = 5000;
    private GameOver mGameOver;
    private FPSCounter mFpsCounter;
    private boolean bCounterset = false;


    private enum GAMESTATE {
        START,
        WAITWAIVE,
        WAIVING,
        GAMEOVER,
        WAITSTART
    }

    public GameControler(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        initAsteroidPool(gameEngine);
        mPlayer = new Player(gameEngine);
        mGameOver = new GameOver(gameEngine);

        mFpsCounter = new FPSCounter(gameEngine);
        mFpsCounter.startGame();


    }

    private void initAsteroidPool(GameEngine gameEngine) {
        for (int i = 0; i < INITIAL_ASTEROID_POOL_AMOUNT; i++) {
            if(i == 4 || i == 8) {
                mAsteroidPool.add(new Asteroid.BigAsteroid(gameEngine));
            }
            else {
                mAsteroidPool.add(new Asteroid(gameEngine));
            }
        }
    }

    public void returnToPool(Asteroid b) {
        mAsteroidPool.add(b);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {

        switch (currentState) {
            case START :
                //counter is set only once
                if(!bCounterset) gameEngine.addGameObject(mFpsCounter);


                mFpsCounter.startGame();
                mPlayer.startGame();
                gameEngine.addGameObject(mPlayer);
                currentState = GAMESTATE.WAITWAIVE;
                callback.changeReplayButtonVisibility(false);
                break;
            case WAITWAIVE:
                currentState = GAMESTATE.WAIVING;
                break;
            case WAIVING:
                waiving(elapsedMillis, gameEngine);
                break;
            case GAMEOVER:
                gameEngine.removeGameObject(mPlayer);

                mGameOver.startGame();
                gameEngine.addGameObject(mGameOver);
                currentState = GAMESTATE.WAITSTART;
                break;
            case WAITSTART:
                mWaitingTime += elapsedMillis;
                if ( mWaitingTime > WAITING_TIME) {
                    callback.changeReplayButtonVisibility(true);
                    mWaitingTime = 0;

                }
                break;
        }
    }

    private void waiving(long elapsedMillis, GameEngine gameEngine) {
        mCurrentMillis += elapsedMillis;
        long waveTimestamp = mEnemiesSpawned*TIME_BETWEEN_ENEMIES;
        if (mCurrentMillis > waveTimestamp) {
            // Spawn a new enemy
            if(mAsteroidPool.isEmpty()) return;
            Asteroid a = mAsteroidPool.remove(0);
            a.init(this, gameEngine);
            gameEngine.addGameObject(a);
            mEnemiesSpawned++;
        }
    }

    @Override
    public void startGame() {
        mCurrentMillis = 0;
    }

    @Override
    public void onDraw(Canvas canvas) {
    }

    @Override
    public void onGameEvent(GameEvent gameEvents) {
        if(gameEvents == GameEvent.SpaceshipHit) {
           currentState = GAMESTATE.GAMEOVER;
        }
    }

    public void replay() {
        currentState = GAMESTATE.START;
    }


    public interface Callback {
        void changeReplayButtonVisibility(final boolean visible);
    }
}
