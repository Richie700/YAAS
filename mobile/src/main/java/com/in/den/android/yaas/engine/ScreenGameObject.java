package com.in.den.android.yaas.engine;

import android.graphics.Rect;


/**
 * Created by harumi on 25/01/2017.
 */

public interface ScreenGameObject {

    Rect mBoundingRect = new Rect();

    boolean checkCollision(ScreenGameObject otherObject);
    void onCollision(GameEngine gameEngine,
                            ScreenGameObject sgo) ;

}
