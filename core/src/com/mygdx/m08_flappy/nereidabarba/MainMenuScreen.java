package com.mygdx.m08_flappy.nereidabarba;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {
    final Bird game;
    OrthographicCamera camera;

    public MainMenuScreen(final Bird gam) {
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update(); //Siempre se hace la llamada a la camara
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin(); //Llamada para empezar a pintar cosas
        game.batch.draw(game.manager.get("background.png", Texture.class), 0, 0);
        game.bigFont.draw(game.batch, "Welcome to Puig Bird!!! ", 20, 300);
        game.bigFont.draw(game.batch, "Tap anywhere to begin!", 20, 180);
        game.batch.end();

        if (Gdx.input.justTouched()) { //Verifica si se ha tocado la pantalla
            game.setScreen(new GameScreen(game));
            dispose(); //Destruye la pantalla
        }
    }

    @Override
    public void resize(int width, int height) {
    }
    @Override
    public void show() {
    }
    @Override
    public void hide() {
    }
    @Override
    public void pause() {
    }
    @Override
    public void resume() {
    }
    @Override
    public void dispose() {
    }
}