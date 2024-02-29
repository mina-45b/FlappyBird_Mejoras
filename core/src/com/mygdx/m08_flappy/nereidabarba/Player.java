package com.mygdx.m08_flappy.nereidabarba;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;


public class Player extends Actor {

    Rectangle bounds; //Limites
    AssetManager manager;

    Array<Texture> birdFrames;
    Animation<Texture> birdAnimation;

    Array<Texture> birdFlashBird;
    Animation<Texture> birdFlashAnimation;

    boolean isFlashing;


    float stateTime;


    float speedy, gravity;

    Player()
    {
        setX(200);
        setY(280 / 2 - 64 / 2);
        setSize(64,45);
        bounds = new Rectangle();

        speedy = 0;
        gravity = 850f;

        birdFrames = new Array<>();
        for (int i = 1; i <= 8; i++) {
            Texture frame = new Texture(Gdx.files.internal("bird-"+i+".png"));
            birdFrames.add(frame);
        }

        float frameDuration = 0.1f;
        birdAnimation = new Animation<>(frameDuration, birdFrames, Animation.PlayMode.LOOP);

         //Initializer flash
        birdFlashBird = new Array<>();
        for (int i = 1; i <= 8; i++) {
            Texture frame = new Texture(Gdx.files.internal("flash-"+i+".png"));
            birdFlashBird.add(frame);
        }

        birdFlashAnimation = new Animation<>(frameDuration, birdFlashBird, Animation.PlayMode.LOOP);



    }

    @Override
    public void act(float delta) {
        //Actualitza la posició del jugador amb la velocitat vertical
        moveBy(0, speedy * delta);

        //Actualitza la velocitat vertical amb la gravetat
        speedy -= gravity * delta;
        bounds.set(getX(), getY(), getWidth(), getHeight());
    }

    void impulso()
    {
        speedy = 400f;
    }

  /*  public void flashingBird() {
        birdFlashBird = new Array<>();
        for (int i = 1; i <= 8; i++) {
            Texture frame = new Texture(Gdx.files.internal("flash-"+i+".png"));
            birdFlashBird.add(frame);
        }

        float frameDuration = 0.1f;
        birdFlashAnimation = new Animation<>(frameDuration, birdFlashBird, Animation.PlayMode.LOOP);

    }*/

    public void startFlashing() {
        isFlashing = true;
       // stateTime = 0f;
    }

    public void stopFlashing() {
        isFlashing = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        //batch.draw(manager.get("bird.png", Texture.class), getX(), getY());

        stateTime += Gdx.graphics.getDeltaTime();

        if (isFlashing) {
            Texture currentFlashFrame = birdFlashAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFlashFrame, getX(), getY(), getWidth(), getHeight());
        } else {
            Texture currentFrame = birdAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
        }

    }
    public Rectangle getBounds() {
        return bounds;
    }
    public void setManager(AssetManager manager) {
        this.manager = manager;
    }
}