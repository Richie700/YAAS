package com.in.den.android.yaas.engine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by harumi on 23/01/2017.
 */

public abstract class Sprite extends GameObject {
    protected final Bitmap mBitmap;
    public final int mImageHeight;
    public final int mImageWidth;
    protected final Matrix mMatrix = new Matrix();
    public double mPositionX;
    public double mPositionY;
    protected final double mPixelFactor;

    protected Sprite(GameEngine gameEngine, int drawableRes) {
        Resources r = gameEngine.mActivity.getResources();
        Drawable spriteDrawable = r.getDrawable(drawableRes);
        mPixelFactor = gameEngine.mPixelFactor;
        mImageHeight = (int)
                (spriteDrawable.getIntrinsicHeight()*mPixelFactor);
        mImageWidth = (int)
                (spriteDrawable.getIntrinsicWidth()*mPixelFactor);
        mBitmap = ((BitmapDrawable) spriteDrawable).getBitmap();
    }

    @Override
    public void onDraw(Canvas canvas) {
        mMatrix.reset();
        mMatrix.postScale((float) mPixelFactor, (float) mPixelFactor);
        mMatrix.postTranslate((float) mPositionX, (float) mPositionY);
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }
}
