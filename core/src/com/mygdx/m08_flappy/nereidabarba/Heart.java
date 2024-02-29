package com.mygdx.m08_flappy.nereidabarba;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;


public class Heart extends Actor {
    Rectangle bounds;
    Array<Texture> heartFrames;
    Animation<Texture> heartAnimation;
    AssetManager manager;

    float stateTime;

    public Heart () {
        setSize(30, 30);
        bounds = new Rectangle();
        setVisible(false);

        heartFrames = new Array<>();
        for (int i = 1; i <= 9; i++) {
            //Texture frame = new Texture(Gdx.files.internal("heart-"+i+".png"));
            Texture frame = new Texture(Gdx.files.internal("heart-op"+i+".png"));
            heartFrames.add(frame);
        }

        float frameDuration = 0.1f;
        heartAnimation = new Animation<>(frameDuration, heartFrames, Animation.PlayMode.LOOP);

    }

    @Override
    public void act(float delta) {
        moveBy(-200 * delta, 0);
        bounds.set(getX(), getY(), getWidth(), getHeight());
        if(!isVisible())
            setVisible(true);
        if (getX() < -64)
            remove(); //Elimina el actor del stage
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        stateTime += Gdx.graphics.getDeltaTime();
        Texture currentFrame = heartAnimation.getKeyFrame(stateTime, true);

        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void setManager(AssetManager manager) {
        this.manager = manager;
    }
}
