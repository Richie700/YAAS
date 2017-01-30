package com.in.den.android.yaas.engine;

import android.graphics.Canvas;

/**
 * Created by harumi on 19/01/2017.
 */

public abstract class GameObject {

    public abstract void startGame();
    public abstract void onUpdate(long elapsedMillis, GameEngine gameEngine);
    public abstract void onDraw(Canvas canvas);

    public final Runnable mOnAddedRunnable = new Runnable() {
        @Override
        public void run() {
            onAddedToGameUiThread();
        }
    };

    public final Runnable mOnRemovedRunnable = new Runnable() {
        @Override
        public void run() {
            onRemovedFromGameUiThread();
        }
    };

    public  void onRemovedFromGameUiThread(){}
    public void onAddedToGameUiThread() {}
}
