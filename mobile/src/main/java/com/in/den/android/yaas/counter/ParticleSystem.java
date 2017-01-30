package com.in.den.android.yaas.counter;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.in.den.android.yaas.R;
import com.in.den.android.yaas.engine.GameEngine;
import com.in.den.android.yaas.engine.GameObject;
import com.in.den.android.yaas.engine.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by harumi on 26/01/2017.
 */

public class ParticleSystem extends GameObject {

    List<Particle> mParticlePool = new ArrayList<Particle>();
    List<ParticleModifier> mModifiers = new ArrayList<ParticleModifier>();

    Random mRandom = new Random();
    long mTimeToLive = 300;
    int maxParticles = 200;
    int mActivatedParticles = 0;
    int mX, mY;

    public ParticleSystem(GameEngine gameEngine) {
        for (int i=0; i<maxParticles; i++) {
            mParticlePool.add(new Particle(this, gameEngine));
        }
    }

    private void activateParticle(GameEngine gameEngine) {
        Particle p = mParticlePool.remove(0);
        p.init(gameEngine);

        p.activate(gameEngine, mTimeToLive, mX, mY, mModifiers);
        mActivatedParticles++;
    }

    @Override
    public void startGame() {

    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {

    }

    @Override
    public void onDraw(Canvas canvas) {

    }

    private void returnToPool(Particle particle) {
        mParticlePool.add(particle);
    }


    public class Particle extends Sprite {

        float mScale;
        float mRotation;
        Paint mPaint = new Paint();
        int mAlpha = 125;
        long mTotalMillis;
        long mTimeToLive;
        double mSpeedX;
        double mSpeedY;
        double mSpeed=5;
        double mRotationSpeed;
        ParticleSystem mParent;
        List<ParticleModifier> mModifiers;


        protected Particle(ParticleSystem parent, GameEngine gameEngine) {
            super(gameEngine, R.drawable.particle);
            mParent = parent;

        }


        protected void init(GameEngine gameEngine) {
            double angle =
                    gameEngine.mRandom.nextDouble()*Math.PI/3d - Math.PI/6d;
            mSpeedX = mSpeed * Math.sin(angle);
            mSpeedY = mSpeed * Math.cos(angle);
            mRotation = gameEngine.mRandom.nextInt(360);
        }

        public void activate(
                GameEngine gameEngine,
                long timeToLive,
                double x,
                double y,
                List<ParticleModifier> modifiers
                ) {
            mTimeToLive = timeToLive;
            mPositionX = x - mImageWidth/2;
            mPositionY = y - mImageHeight/2;
            mModifiers = modifiers;
            mTotalMillis = 0;

            gameEngine.addGameObject(this);
        }

        @Override
        public void startGame() {

        }

        @Override
        public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
            mTotalMillis += elapsedMillis;
            if (mTotalMillis > mTimeToLive) {
                mParent.returnToPool(this);
            }
            else {
                mPositionX += mSpeedX*elapsedMillis;
                mPositionY += mSpeedY*elapsedMillis;
                mRotation += mRotationSpeed*elapsedMillis/1000d;
                for (int i=0; i<mModifiers.size(); i++) {
                    mModifiers.get(i).apply(this, mTotalMillis);
                }
            }
        }

        @Override
        public void onDraw(Canvas canvas) {
            float scaleFactor = (float) (mPixelFactor*mScale);
            mMatrix.reset();
            mMatrix.postScale(scaleFactor, scaleFactor);
            mMatrix.postTranslate((float) mPositionX, (float) mPositionY);
            mMatrix.postRotate((float) mRotation,
                    (float) (mPositionX + mImageWidth*mScale / 2),
                    (float) (mPositionY + mImageHeight*mScale / 2));
            mPaint.setAlpha(mAlpha);
            canvas.drawBitmap(mBitmap, mMatrix, mPaint);
        }

    }
}
