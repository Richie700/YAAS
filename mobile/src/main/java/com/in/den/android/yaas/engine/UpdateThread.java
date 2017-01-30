package com.in.den.android.yaas.engine;

/**
 * Created by harumi on 24/01/2017.
 */

public class UpdateThread extends MyThread {

    GameEngine gameEngine;

    public UpdateThread(GameEngine gameEngine) {
        super(gameEngine);
        this.gameEngine = gameEngine;
    }

    @Override
    public void doIt(long elapsedMillis) {
        gameEngine.onUpdate(elapsedMillis);
    }
}
