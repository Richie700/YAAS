package com.in.den.android.yaas.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by harumi on 20/01/2017.
 */

public class StandardGameView extends View implements GameView {

    private List<GameObject> mGameObjects;

    public StandardGameView(Context context) {
        super(context);
    }

    public StandardGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StandardGameView(Context context, AttributeSet attrs,
                    int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        synchronized (mGameObjects) {
            int numObjects = mGameObjects.size();
            for (int i = 0; i < numObjects; i++) {
                mGameObjects.get(i).onDraw(canvas);
            }
        }
    }
    @Override
    public void draw() {
        postInvalidate();
    }
    @Override
    public void setGameObjects(List<GameObject> gameObjects) {
        mGameObjects = gameObjects;
    }
}
