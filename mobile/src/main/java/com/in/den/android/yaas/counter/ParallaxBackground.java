package com.in.den.android.yaas.counter;


import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.in.den.android.yaas.R;
import com.in.den.android.yaas.engine.GameEngine;
import com.in.den.android.yaas.engine.Sprite;

/**
 * Created by harumi on 24/01/2017.
 */

public class ParallaxBackground extends Sprite {

   private double mScreenHeight;
   private double mScreenWidth;
   private double mTargetWidth;
   private double mSpeedY;
   private int speed = 10;

    protected ParallaxBackground(GameEngine gameEngine) {

        super(gameEngine,  R.drawable.background);

        mSpeedY = mPixelFactor * speed/1000d;

        mScreenHeight = gameEngine.mHeight;
        mScreenWidth = gameEngine.mWidth;
        mTargetWidth = Math.min(mImageWidth, mScreenWidth);
    }

    @Override
    public void startGame() {

    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mPositionY += mSpeedY * elapsedMillis;
    }


    @Override
    public void onDraw(Canvas canvas) {
       // efficientDraw(canvas);

        float widthscale = (float) mScreenWidth / mImageWidth;
        float heightscale = (float) mScreenHeight / mImageHeight;

        if (mPositionY > 0) {
            mMatrix.reset();
            mMatrix.postScale((float) (mPixelFactor) * widthscale,
                    (float) (mPixelFactor) * heightscale);
            mMatrix.postTranslate(0, (float) (mPositionY - mImageHeight));
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }
        mMatrix.reset();
        mMatrix.postScale((float) (mPixelFactor) * widthscale,
                (float) (mPixelFactor) * heightscale);
        mMatrix.postTranslate(0, (float) mPositionY);
        canvas.drawBitmap(mBitmap, mMatrix, null);
        if (mPositionY > mScreenHeight) {
            mPositionY -= mImageHeight;

       }
    }


    private Rect mSrcRect = new Rect();
    private Rect mDstRect = new Rect();

    private void efficientDraw(Canvas canvas) {
        if (mPositionY < 0) {
            mSrcRect.set(0,
                    (int) (-mPositionY/mPixelFactor),
                    (int) (mTargetWidth/mPixelFactor),
                    (int) ((mScreenHeight - mPositionY)/mPixelFactor));
            mDstRect.set(0,
                    0,
                    (int) mTargetWidth,
                    (int) mScreenHeight);
            canvas.drawBitmap(mBitmap, mSrcRect, mDstRect, null);
        }
        else {
            mSrcRect.set(0,
                    0,
                    (int) (mTargetWidth/mPixelFactor),
                    (int) ((mScreenHeight - mPositionY) / mPixelFactor));
            mDstRect.set(0,
                    (int) mPositionY,
                    (int) mTargetWidth,
                    (int) mScreenHeight);
            canvas.drawBitmap(mBitmap, mSrcRect, mDstRect, null);
            // We need to draw the previous block
            mSrcRect.set(0,
                    (int) ((mImageHeight - mPositionY) / mPixelFactor),
                    (int) (mTargetWidth/mPixelFactor),
                    (int) (mImageHeight/mPixelFactor));
            mDstRect.set(0,
                    0,
                    (int) mTargetWidth,
                    (int) mPositionY);
            canvas.drawBitmap(mBitmap, mSrcRect, mDstRect, null);
        }
        if (mPositionY > mScreenHeight) {
            mPositionY -= mImageHeight;
        }
    }
}
