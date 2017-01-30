package com.in.den.android.yaas.engine;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by harumi on 20/01/2017.
 */

public interface GameView  {


    void draw();

    void setGameObjects(List<GameObject> gameObjects);


}
