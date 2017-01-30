package com.in.den.android.yaas.counter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;

import com.in.den.android.yaas.R;
import com.in.den.android.yaas.engine.GameEngine;
import com.in.den.android.yaas.engine.GameObject;
import com.in.den.android.yaas.engine.Sprite;

/**
 * Created by harumi on 27/01/2017.
 */

public class GameOver extends Sprite {
    private long mTimemillis;
    private long TIMESHOW = 5000;
    private int mMaxX, mMaxY;



    public GameOver(GameEngine gameEngine) {
       super(gameEngine, R.drawable.gameover);
        mMaxX = (int)(gameEngine.mWidth - mImageWidth);
        mMaxY = (int)(gameEngine.mHeight - mImageHeight);
    }


    @Override
    public void startGame() {
        mTimemillis = 0;
        mPositionX = mMaxX / 2;
        mPositionY = mMaxY / 2;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mTimemillis += elapsedMillis;
        if(mTimemillis > TIMESHOW) {
            gameEngine.removeGameObject(this);
        }
    }

}
