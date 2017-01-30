package com.in.den.android.yaas.counter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.in.den.android.yaas.engine.GameEngine;
import com.in.den.android.yaas.engine.GameEvent;
import com.in.den.android.yaas.engine.GameEventListner;
import com.in.den.android.yaas.engine.GameObject;

/**
 * Created by harumi on 24/01/2017.
 */

public class FPSCounter extends GameObject implements GameEventListner {
    private  double mPixelFactor;
    private  float mTextWidth;
    private  float mTextHeight;
    private Paint mPaint;
    private String mFpsText = "";

    private int score = 0;
    private int SCORE_ASTEROIDHIT = 50;
    private int SCORE_ASTEROIDMISSED = -1;
    private boolean bGameEnd = false;

    private TextPaint mPaintScore;
    private StaticLayout mScorelayout;

    public FPSCounter(GameEngine gameEngine) {
        mPaint = new Paint();
        mPaint.setTextAlign(Paint.Align.CENTER);
        mTextHeight = (float) (25 * gameEngine.mPixelFactor);
        mTextWidth = (float) (50 * gameEngine.mPixelFactor);
        mPaint.setTextSize(mTextHeight / 2);

        mPaintScore = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaintScore.setTextSize(50);
        mPaintScore.setColor(Color.WHITE);
    }

    @Override
    public void startGame() {
        mFpsText="";
        score = 0;
        bGameEnd = false;
    }
    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        if(bGameEnd) {
            mFpsText =
                    "Your final score is "+ String.format("%08d", score);
        }
        else {
            mFpsText = "Score : " + String.format("%08d", score);
        }

        float textWidth = mPaintScore.measureText(mFpsText, 0, mFpsText.length());
        mScorelayout = new StaticLayout(mFpsText, mPaintScore, (int) textWidth,
                Layout.Alignment.ALIGN_CENTER, 1f, 0f, true);
    }

    @Override
    public void onDraw(Canvas canvas) {

        canvas.save();
        canvas.translate(50, 50);
        mScorelayout.draw(canvas);
        canvas.restore();
    }

    @Override
    public void onGameEvent(GameEvent gameEvents) {
        if(gameEvents == GameEvent.AsteroidHit) {
            score += SCORE_ASTEROIDHIT;
        }
        else if(gameEvents == GameEvent.AsteroidMissed) {
            score += SCORE_ASTEROIDMISSED;
        }
        else if(gameEvents == GameEvent.SpaceshipHit) {
            bGameEnd = true;
        }
    }
}
