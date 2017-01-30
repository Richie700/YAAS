package com.in.den.android.yaas.counter;

import com.in.den.android.yaas.R;
import com.in.den.android.yaas.engine.GameEvent;
import com.in.den.android.yaas.engine.InputController;
import com.in.den.android.yaas.engine.GameEngine;
import com.in.den.android.yaas.engine.ScreenGameObject;
import com.in.den.android.yaas.engine.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harumi on 20/01/2017.
 */

public class Player extends Sprite implements ScreenGameObject {
    private static final int INITIAL_BULLET_POOL_AMOUNT = 6;
    private double mSpeedFactor;
    private int mMaxY;
    private int mMaxX;
    public List<Bullet> mBullets = new ArrayList<Bullet>();
    private long mTimeSinceLastFire;
    private long TIME_BETWEEN_BULLETS = 1000;

    public Player(GameEngine gameEngine) {
        super(gameEngine, R.drawable.ship);

        mSpeedFactor = mPixelFactor * 100d / 1000d;
        mMaxX = (int)(gameEngine.mWidth - mImageWidth);
        mMaxY = (int)(gameEngine.mHeight - mImageHeight);
        initBulletPool(gameEngine);
    }

    //----------------------------------------------------
    // Override GameObject Method
    // methode is invoked from GameEngine to set the initial position
    //-----------------------------------------------------
    @Override
    public void startGame() {
        mPositionX = mMaxX / 2;
        mPositionY = mMaxY / 2;
    }

    //----------------------------------------------------
    // Override GameObject Method
    // methode is invoked from GameEngine
    //elapsed time is calculated by UpdateThread
    //-----------------------------------------------------
    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        updatePosition(elapsedMillis, gameEngine.inputController);
        checkFiring(elapsedMillis, gameEngine);
    }

    private void initBulletPool(GameEngine gameEngine) {
        for (int i = 0; i < INITIAL_BULLET_POOL_AMOUNT; i++) {
            mBullets.add(new Bullet(gameEngine));
        }
    }

    private Bullet getBullet() {
        if (mBullets.isEmpty()) {
            return null;
        }
        return mBullets.remove(0);
    }

    private void releaseBullet(Bullet b) {
        mBullets.add(b);
    }

    private void checkFiring(long elapsedMillis, GameEngine gameEngine) {

        if (gameEngine.inputController.mIsFiring
                && mTimeSinceLastFire > TIME_BETWEEN_BULLETS) {

            Bullet b = getBullet();
            if (b == null) {
                return;
            }
            b.init(this, mPositionX + mImageWidth / 2, mPositionY);
            gameEngine.addGameObject(b);
            gameEngine.onGameEvent(GameEvent.LaserFired);
            mTimeSinceLastFire = 0;
        } else {
            mTimeSinceLastFire += elapsedMillis;

        }
    }

    public void updatePosition(long elapsedMillis, InputController inputController) {

        mPositionX +=
                mSpeedFactor * inputController.mHorizontalFactor * elapsedMillis;
        if (mPositionX < 0) {
            mPositionX = 0;
        }

        if (mPositionX > mMaxX) {
            mPositionX = mMaxX;
        }
        mPositionY +=
                mSpeedFactor * inputController.mVerticalFactor * elapsedMillis;
        if (mPositionY < 0) {
            mPositionY = 0;
        }
        if (mPositionY > mMaxY) {
            mPositionY = mMaxY;
        }
    }

    @Override
    public boolean checkCollision(ScreenGameObject otherObject) {
        return Utility.hasIntersection(this, (Sprite)otherObject);
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject sgo) {
        if(sgo instanceof Asteroid.BigAsteroid) {
            gameEngine.onGameEvent(GameEvent.SpaceshipHit);
        }
    }

    //-------------------------------------------------------------------
    // inner class Bullet
    //-------------------------------------------------------------------
    public class Bullet extends Sprite implements ScreenGameObject {
        private Player mParent;
        private double mSpeedFactor;

        public Bullet(GameEngine gameEngine) {
            super(gameEngine, R.drawable.lazer);
            mSpeedFactor = gameEngine.mPixelFactor * -300d / 1000d;
        }


        public void init(Player parent, double positionX, double positionY) {
            mPositionX = positionX - mImageWidth/2;
            mPositionY = positionY - mImageHeight/2;
            mParent = parent;
        }

        //----------------------------------------------------
        // Override GameObject Method
        // methode is invoked from GameEngine to set the initial position
        //-----------------------------------------------------

        @Override
        public void startGame() {

        }

        //----------------------------------------------------
        // Override GameObject Method
        // methode is invoked from GameEngine
        //-----------------------------------------------------
        @Override
        public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
            mPositionY += mSpeedFactor * elapsedMillis;
            if (mPositionY < -mImageHeight) {
                gameEngine.removeGameObject(this);
                mParent.releaseBullet(this);
            }
        }

        @Override
        public boolean checkCollision(ScreenGameObject otherObject) {

            return Utility.hasIntersection(this, (Sprite)otherObject);
        }

        @Override
        public void onCollision(GameEngine gameEngine, ScreenGameObject sgo) {
            if (sgo instanceof Asteroid) {
                // Remove both from the game (and return them to their pools)
                gameEngine.removeGameObject(this);
                mParent.releaseBullet(this);

            }
        }
    }
}
