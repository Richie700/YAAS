package com.in.den.android.yaas.counter;

import android.graphics.Canvas;

import com.in.den.android.yaas.R;
import com.in.den.android.yaas.engine.GameEngine;
import com.in.den.android.yaas.engine.GameEvent;
import com.in.den.android.yaas.engine.ScreenGameObject;
import com.in.den.android.yaas.engine.Sprite;

import static android.R.attr.angle;

/**
 * Created by harumi on 27/01/2017.
 */

public class Asteroid extends Sprite implements ScreenGameObject {

    private double mSpeedX;
    private double mSpeedY;
    private double mSpeed;
    private float mRotation;
    private double mRotationSpeed;
    private GameControler mController;

    protected Asteroid(GameEngine gameEngine) {
        this(gameEngine, R.drawable.asteroid2);
    }

    protected Asteroid(GameEngine gameEngine, int drawableRes) {
        super(gameEngine, drawableRes );
        mSpeed = mPixelFactor * 200d/1000d;
    }

    protected void init(GameControler astroidsControler, GameEngine gameEngine) {
        mController = astroidsControler;

        double angle =
                gameEngine.mRandom.nextDouble()*Math.PI/3d - Math.PI/6d;

        mSpeedX = mSpeed * Math.sin(angle);
        mSpeedY = mSpeed * Math.cos(angle);
        // Asteroids initialize in the central 50% of the screen
        mPositionX = gameEngine.mRandom.nextInt((int)(gameEngine.mWidth/2)) +
                gameEngine.mWidth/4;
        // They initialize outside of the screen vertically
        mPositionY = -mImageHeight;

        mRotation = gameEngine.mRandom.nextInt(360);
    }

    @Override
    public void startGame() {

    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mPositionX += mSpeedX * elapsedMillis;
        mPositionY += mSpeedY * elapsedMillis;
// Check of the sprite goes out of the screen
        if (mPositionY > gameEngine.mHeight) {
// Return to the pool
            gameEngine.removeGameObject(this);
            mController.returnToPool(this);

            gameEngine.onGameEvent(GameEvent.AsteroidMissed);
        }

        mRotationSpeed = angle *(180d / Math.PI)/250d;
        mRotation += mRotationSpeed * elapsedMillis;
        if (mRotation > 360) {
            mRotation = 0;
        }
        else if (mRotation < 0) {
            mRotation = 360;
        }
    }


    @Override
    public void onDraw(Canvas canvas) {

        mMatrix.reset();
        mMatrix.postScale((float) mPixelFactor, (float) mPixelFactor);
        mMatrix.postTranslate((float) mPositionX, (float) mPositionY);
        mMatrix.postRotate((float) mRotation,
                (float) (mPositionX + mImageWidth/2),
                (float) (mPositionY + mImageHeight/2));
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }

    @Override
    public boolean checkCollision(ScreenGameObject otherObject) {

        return Utility.hasIntersection(this, (Sprite)otherObject);
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject sgo) {
        if(sgo instanceof Player.Bullet) {
            gameEngine.removeGameObject(this);
            mController.returnToPool(this);

            gameEngine.onGameEvent(GameEvent.AsteroidHit);
        }
    }

    public static class BigAsteroid extends Asteroid {

        protected BigAsteroid(GameEngine gameEngine) {
            super(gameEngine, R.drawable.bigasteroid_2);
        }
    }
}
