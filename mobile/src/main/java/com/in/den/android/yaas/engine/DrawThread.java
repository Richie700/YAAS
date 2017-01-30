package com.in.den.android.yaas.engine;


/**
 * Created by harumi on 23/01/2017.
 */

public class DrawThread extends MyThread {

    private GameEngine mGameEngine;

    public DrawThread(GameEngine gameEngine) {
       super(gameEngine);
        mGameEngine = gameEngine;
    }

    @Override
    public void doIt(long elapsedMillis) {
        if (elapsedMillis < 20) { // This is 50 fps
            try {
                Thread.sleep(20-elapsedMillis);
            } catch (InterruptedException e) {
// We just continue.
            }
        }

        mGameEngine.onDraw();
    }

}
