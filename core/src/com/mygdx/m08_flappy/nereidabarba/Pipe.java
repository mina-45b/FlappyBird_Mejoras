package com.mygdx.m08_flappy.nereidabarba;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Pipe extends Actor {
    Rectangle bounds;
    boolean upsideDown;
    AssetManager manager;
    boolean isScored;
    boolean moving;

    float timeMoving;

    Pipe() {
        setSize(64, 230);
        bounds = new Rectangle();
        setVisible(false);
        isScored = false;
        moving = false;
        timeMoving = 2.0f;
    }

    @Override
    public void act(float delta) {
        moveBy(-200 * delta, 0);

        if(!isVisible())
            setVisible(true);

        if (getX() < -64)
            remove(); //Elimina el actor del stage

        timeMoving -= delta;

        if(moving) {
            if(upsideDown) {
                if (timeMoving == 1.0f) {
                    moveBy(0, -8*delta);
                } else {
                    moveBy(0, 8*delta);
                }

            } else {
                if (timeMoving == 1.0f) {
                    moveBy(0, 8*delta);
                } else {
                    moveBy(0, -8*delta);
                }
            }
        }

        bounds.set(getX(), getY(), getWidth(), getHeight());
    }

    public void startMoving() {
        moving = true;
    }

    public void stopMoving() {
        moving = false;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw( manager.get( upsideDown ? "pipe_up.png" :
                "pipe_down.png", Texture.class), getX(), getY() );
    }
    public Rectangle getBounds() {
        return bounds;
    }
    public boolean isUpsideDown() {
        return upsideDown;
    }

    public boolean isScored() {
        return isScored;
    }

    public void setUpsideDown(boolean upsideDown) {
        this.upsideDown = upsideDown;
    }
    public void setManager(AssetManager manager) {
        this.manager = manager;
    }

    public void setScored(boolean scored) {
        isScored = scored;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }
}