package com.in.den.android.yaas.engine;

/**
 * Created by harumi on 19/01/2017.
 */
public abstract class MyThread extends Thread {

    GameEngine mGameEngine;
    public volatile boolean mGameIsRunning;
    public volatile boolean mPauseGame;
    Object mLock = new Object();

    public MyThread(GameEngine gameEngine) {
        mGameEngine = gameEngine;
        mGameIsRunning = false;
    }

    public abstract void doIt(long elapsedMillis);

    @Override
    public void start() {
        mGameIsRunning = true;
        mPauseGame = false;
        super.start();
    }

    public void stopGame() {
        mGameIsRunning = false;
        resumeGame();
    }

    @Override
    public void run() {
        long previousTimeMillis;
        long currentTimeMillis;
        long elapsedMillis;
        previousTimeMillis = System.currentTimeMillis();
        while (mGameIsRunning) {
            currentTimeMillis = System.currentTimeMillis();
            elapsedMillis = currentTimeMillis - previousTimeMillis;
            if (mPauseGame) {
                while (mPauseGame) {
                    try {
                        synchronized (mLock) {
                            mLock.wait();
                        }
                    } catch (InterruptedException e) {
                    // We stay on the loop
                    }
                }
                currentTimeMillis = System.currentTimeMillis();
            }

            if(!mGameIsRunning) break;

            doIt(elapsedMillis);

            previousTimeMillis = currentTimeMillis;
        }
    }

    public void resumeGame() {
        if (mPauseGame == true) {
            mPauseGame = false;
            synchronized (mLock) {
                mLock.notify();
            }
        }
    }

    public void pauseGame() {
        mPauseGame = true;
    }
}
