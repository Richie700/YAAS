package com.in.den.android.yaas.counter;

import android.graphics.Canvas;
import android.view.View;
import android.widget.TextView;

import com.in.den.android.yaas.engine.GameEngine;
import com.in.den.android.yaas.engine.GameObject;

import org.w3c.dom.Text;

/**
 * Created by harumi on 19/01/2017.
 */
public class ScoreGameObject extends GameObject {

    private TextView mText;
    private long mTotalMilis;

    public ScoreGameObject(TextView tv ) {
        super();
        mText = tv;
    }


    public ScoreGameObject(View view, int viewResId) {
        mText = (TextView) view.findViewById(viewResId);
    }
    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine)
    {
        mTotalMilis += elapsedMillis;
    }
    @Override
    public void startGame() {
        mTotalMilis = 0;
    }
    @Override
    public void onDraw(Canvas canvas) {
        mText.setText(String.valueOf(mTotalMilis / 1000 ));
    }
}
