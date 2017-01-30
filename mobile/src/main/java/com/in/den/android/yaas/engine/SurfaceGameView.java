package com.in.den.android.yaas.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.List;

/**
 * Created by harumi on 20/01/2017.
 */

public class SurfaceGameView extends SurfaceView implements GameView, SurfaceHolder.Callback {

    private List<GameObject> mGameObjects;
    private boolean mReady;

    public SurfaceGameView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public SurfaceGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }


    @Override
    public void draw() {
        if (!mReady) {
            return;
        }
        Canvas canvas = getHolder().lockCanvas();
        if (canvas == null) {
            return;
        }
        canvas.drawRGB(0,0,0);
        synchronized (mGameObjects) {
            int numObjects = mGameObjects.size();
            for (int i = 0; i < numObjects; i++) {
                mGameObjects.get(i).onDraw(canvas);
            }
        }
        getHolder().unlockCanvasAndPost(canvas);

    }

    @Override
    public void setGameObjects(List<GameObject> gameObjects) {
            mGameObjects = gameObjects;
    }


    //----------------------------
    //call back methods
    //---------------------------

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mReady = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mReady = false;
    }
}
