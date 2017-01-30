package com.in.den.android.yaas.counter;

import android.graphics.Rect;
import android.graphics.RectF;

import com.in.den.android.yaas.engine.Sprite;

import java.io.Serializable;

/**
 * Created by harumi on 25/01/2017.
 */

public class Utility {

    public static boolean hasIntersection(Sprite objecta, Sprite objectb) {
        boolean res = false;

        return RectF.intersects(new RectF((float)objecta.mPositionX, (float) objecta.mPositionY,
                (float)(objecta.mPositionX + objecta.mImageWidth),
                (float) (objecta.mPositionY + objecta.mImageHeight)),
                new RectF((float)objectb.mPositionX, (float) objectb.mPositionY,
                        (float)(objectb.mPositionX + objectb.mImageWidth),
                        (float) (objectb.mPositionY + objectb.mImageHeight)));
    }
}
