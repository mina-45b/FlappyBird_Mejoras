package com.mygdx.m08_flappy.nereidabarba;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {
    /*
    * Para añadir un corazon en el spawObstacle hago que haya un paramentro de añadir corazon
    * creo un contador de tiempo aleatorio que cuente cuando falta para que aparezca el corazon
    * y se vuelva a true
    * */
    Stage stage;
    Player player;

    boolean dead;

    Array<Pipe> obstacles;

    Array<Heart> hearts;

    long lastObstacleTime;

    boolean isPlayerInmune;
    float inmunityDuration;
    float inmunityTimeRemaining;

    boolean addHeart;
    long lastHeartAddTime;
    long randomInterval;

    int score;
    int numHearts;

    float movingTimer;
    float movingDuration;
    float movingInterval;




    OrthographicCamera camera; //Camara 2d
    final Bird game;
    public GameScreen(final Bird gam) {
        this.game = gam;


        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        player = new Player();
        player.setManager(game.manager);
        stage = new Stage();
        stage.getViewport().setCamera(camera);
        stage.addActor(player);

        // create the obstacles array and spawn the first obstacle
        obstacles = new Array<Pipe>();
        spawnObstacle();

        //Crear los corazones
        hearts = new Array<Heart>();
        lastHeartAddTime = TimeUtils.nanoTime();
        addHeart = false;
        intervalRandomHeart();

        score = 0;

        numHearts = 0;

        isPlayerInmune = false;
        inmunityDuration = 3.0f;
        inmunityTimeRemaining = 0.0f;

        movingTimer = 0.0f;
        movingInterval = 10.0f;
        movingDuration = MathUtils.random(10,15);



    }
    private void spawnObstacle() {
        // Calcula la alçada de l'obstacle aleatòriament
        float holey = MathUtils.random(50, 230);

        // Crea dos obstacles: Una tubería superior i una inferior
        Pipe pipe1 = new Pipe();
        pipe1.setX(800);
        pipe1.setY(holey - 230);
        pipe1.setUpsideDown(true);
        pipe1.setManager(game.manager);
        obstacles.add(pipe1);
        stage.addActor(pipe1);

        Pipe pipe2 = new Pipe();
        pipe2.setX(800);
        pipe2.setY(holey + 200);
        pipe2.setUpsideDown(false);
        pipe2.setManager(game.manager);
        obstacles.add(pipe2);
        stage.addActor(pipe2);


        lastObstacleTime = TimeUtils.nanoTime(); //Guarda el tiempo cuando fue la ultima tuberia

        // Verificar si ha pasado el intervalo
        if (movingTimer >= movingInterval) {
            pipe1.startMoving();
            pipe2.startMoving();

            movingDuration --;

            if (movingDuration <= 0.0f) {
                pipe1.stopMoving();
                pipe2.stopMoving();

                movingDuration = MathUtils.random(10,20);
                movingTimer=0;

            }
        }

        //lastHeartAddTime = TimeUtils.nanoTime() + MathUtils.random(40000000, 60000000);

        //Generar corazones
        if (addHeart) {
            Heart heart = new Heart();

            float heartX = (pipe1.getX() + pipe2.getX() + pipe2.getWidth()) / 2;
            float heartY = (pipe1.getY() + pipe1.getHeight() + pipe2.getY()) / 2;

            heart.setX(heartX);
            heart.setY(heartY);

            heart.setManager(game.manager);
            hearts.add(heart);
            stage.addActor(heart);

            addHeart = false;
            lastHeartAddTime = TimeUtils.nanoTime();
        }

    }

    private void intervalRandomHeart() {
        randomInterval = MathUtils.random(5000000000L, 15000000000L);
    }

    @Override
    public void render(float delta) {
        // Render Pintado de pantalla ==============

        // clear the screen with a color
        ScreenUtils.clear(0.3f, 0.8f, 0.8f, 1);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch
        game.batch.begin();
        game.batch.draw(game.manager.get("background.png", Texture.class), 0, 0);
        game.batch.end();

        // Stage batch: Actors
        stage.getBatch().setProjectionMatrix(camera.combined);
        stage.draw(); //Pintar los actores que hay en el screen

        game.batch.begin();
        game.smallFont.draw(game.batch, "Score: " + score, 10, 470);
        game.batch.end();


        game.batch.begin();
        game.smallFont.draw(game.batch, "Life:" + numHearts, 200, 470);
        game.batch.end();

        //Actualización de la lógica ===============
        stage.act();

        movingTimer += delta;

        //La puntuació augmenta amb el temps de joc
        //score += Gdx.graphics.getDeltaTime();

        // process user input
        if (Gdx.input.justTouched()) {  //Si alguien toco la pantalla en el ultimo frame
            player.impulso();
            game.manager.get("flap.wav", Sound.class).play();
        }

        dead = false;

        // Comprova que el jugador no es surt de la pantalla.
        // Si surt per la part inferior, game over
        if (player.getBounds().y > 480 - 45)
            player.setY( 480 - 45 );
        if (player.getBounds().y < 0 - 45) {
            dead = true;
        }

        // Comprova si cal generar un obstacle nou
        if (TimeUtils.nanoTime() - lastObstacleTime > 1500000000) spawnObstacle();

        // Verifica para agregar un nuevo corazon
        if (TimeUtils.nanoTime() - lastHeartAddTime > randomInterval) {
            addHeart = true;
            intervalRandomHeart();
        }

        if (isPlayerInmune) {
            inmunityTimeRemaining -= Gdx.graphics.getDeltaTime();
            player.startFlashing();
            if (inmunityTimeRemaining <= 0) {
                isPlayerInmune = false;
                player.stopFlashing();
            }
        }



        // Comprova si les tuberies colisionen amb el jugador
        Iterator<Pipe> iter = obstacles.iterator();
        while (iter.hasNext()) {
            Pipe pipe = iter.next();
            if (pipe.getX() + pipe.getWidth() < player.getX() && !pipe.isScored()) {
                // El jugador ha pasado entre las tuberías, incrementa la puntuación
                score += 1;
                pipe.setScored(true);
            }
            if(!isPlayerInmune) {
                if (pipe.getBounds().overlaps(player.getBounds())) {
                    if (numHearts > 0) {
                        numHearts--;
                        isPlayerInmune = true;
                        inmunityTimeRemaining = inmunityDuration;
                    } else {
                        dead = true;
                    }
                }
            }
        }





        // pajaro toca el corazon
        Iterator<Heart> iterator = hearts.iterator();
        while (iterator.hasNext()) {
            Heart heart = iterator.next();
            if (heart.getBounds().overlaps(player.getBounds())) {
                game.manager.get("yay.mp3", Sound.class).play();
                numHearts ++;
                iterator.remove(); //elimina el corazón de la lista
                heart.remove();
            }
        }


        // Treure de l'array les tuberies que estan fora de pantalla
        iter = obstacles.iterator();
        while (iter.hasNext()) {
            Pipe pipe = iter.next();
            if (pipe.getX() < -64) {
                obstacles.removeValue(pipe, true);
            }
        }

        if(dead) {
            game.manager.get("fail.wav", Sound.class).play();
            game.lastScore = (int)score;
            if(game.lastScore > game.topScore) game.topScore = game.lastScore;

            game.setScreen(new GameOverScreen(game));
            dispose();
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