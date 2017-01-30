package com.in.den.android.yaas.engine;

import android.app.Activity;
import android.util.Log;

import com.in.den.android.yaas.YassActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by harumi on 19/01/2017.
 */
public class GameEngine {


    private UpdateThread mUpdateThread;
    private DrawThread mDrawThread;

    private List<GameObject> mGameObjects = new ArrayList<GameObject>();
    private List<GameObject> mObjectsToAdd = new ArrayList<GameObject>();
    private List<GameObject> mObjectsToRemove = new ArrayList<GameObject>();
    private List<ScreenGameObject> mCollisionableObjects = new ArrayList<ScreenGameObject>();

    private boolean running = false;
    public Activity mActivity;
    public InputController inputController;
    private GameView mGameView;
    public double mPixelFactor;
    public double mWidth;
    public double mHeight;
    public Random mRandom = new Random();

    public GameEngine (Activity a, GameView gameView, double bordwidth, double bordheight) {
        mActivity = a;
        mGameView = gameView;
        mGameView.setGameObjects(mGameObjects);
        mWidth = bordwidth;
        mHeight = bordheight;
        mPixelFactor = mHeight / 400d;
    }

    //--------------------------------------------------------
    // methods to change state of Game Engine
    // start, stop, pause, resume
    //--------------------------------------------------------

    public void startGame() {
        // Stop a game if it is running
        stopGame();
        // Setup the game objects
        int numGameObjects = mGameObjects.size();
        for (int i=0; i<numGameObjects; i++) {
            mGameObjects.get(i).startGame();
        }
        // Start the update thread
        mUpdateThread = new UpdateThread(this);
        mUpdateThread.start();
        // Start the drawing thread
        mDrawThread = new DrawThread(this);
        mDrawThread.start();
    }

    public boolean isPaused() {
        return mUpdateThread != null &&  mUpdateThread.mPauseGame;
    }

    public void stopGame() {
        if (mUpdateThread != null) {
            mUpdateThread.stopGame();
        }
        if (mDrawThread != null) {
            mDrawThread.stopGame();
        }
    }

    public void resumeGame() {
        mUpdateThread.resumeGame();
        mDrawThread.resumeGame();
    }

    public void pauseGame() {

        mUpdateThread.pauseGame();
        mDrawThread.pauseGame();
    }

    //=================================================================================


    public void addGameObject(final GameObject gameObject) {
        if (isRunning()){
            mObjectsToAdd.add(gameObject);
        }
        else {
            mGameObjects.add(gameObject);
            if(gameObject instanceof ScreenGameObject) {
                mCollisionableObjects.add((ScreenGameObject)gameObject);
            }
        }

        //The following line of code is not used for now
        //mActivity.runOnUiThread(gameObject.mOnAddedRunnable);
    }

    public void removeGameObject(final GameObject gameObject) {
        if (isRunning()) {
            mObjectsToRemove.add(gameObject);
        }
        else {
            mGameObjects.remove(gameObject);
            if(gameObject instanceof ScreenGameObject) {
                mCollisionableObjects.remove(gameObject);
            }
        }
        //The following line of code is not used for now
        //mActivity.runOnUiThread(gameObject.mOnRemovedRunnable);
    }

    public boolean isRunning() {
        return mUpdateThread != null && mUpdateThread.mGameIsRunning;
    }

    public void onUpdate(long elapsedMillis) {
        int numGameObjects = mGameObjects.size();
        for (int i=0; i<numGameObjects; i++) {
            mGameObjects.get(i).onUpdate(elapsedMillis, this);
        }

        checkCollisions();

        synchronized (mGameObjects) {
            while (!mObjectsToRemove.isEmpty()) {

                GameObject go = mObjectsToRemove.remove(0);
                mGameObjects.remove(go);
                if(go instanceof ScreenGameObject) {
                    mCollisionableObjects.remove(go);
                }
            }

            while (!mObjectsToAdd.isEmpty()) {
                GameObject go = mObjectsToAdd.remove(0);
                mGameObjects.add(go);
                if(go instanceof ScreenGameObject) {
                    mCollisionableObjects.add((ScreenGameObject)go);
                }
            }
        }
    }

    private void checkCollisions() {
        int numObjects = mCollisionableObjects.size();
        for (int i = 0; i < numObjects; i++) {
            ScreenGameObject objectA = mCollisionableObjects.get(i);
            for (int j = i + 1; j < numObjects; j++) {
                ScreenGameObject objectB = mCollisionableObjects.get(j);
                if (objectA.checkCollision(objectB)) {
                    objectA.onCollision(this, objectB);
                    objectB.onCollision(this, objectA);
                }
            }
        }
    }

    public void onDraw() {

        mGameView.draw();
    }

    public void setInputController(InputController inputController) {
        this.inputController = inputController;
    }

    public void onGameEvent (GameEvent gameEvent) {
        // We notify all the GameObjects
        int numObjects = mGameObjects.size();
        for (int i = 0; i < numObjects; i++) {
            if(mGameObjects.get(i) instanceof GameEventListner)
                ((GameEventListner)mGameObjects.get(i)).onGameEvent(gameEvent);
        }

        ((YassActivity)mActivity).getSoundManager().playSoundForGameEvent(gameEvent);
    }
}
