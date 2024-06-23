package com.ta.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ta.ClientGDX;

public class LoginSignupScreen extends InputAdapter implements Screen {
    private Stage stage;
    private Skin skin;
    private TextButton loginButton;
    private TextButton registerButton;
    private ClientGDX game;

    public LoginSignupScreen(ClientGDX game) {
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.app.log("LoginSignupScreen", "show");
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        loginButton = new TextButton("Login", skin);
        registerButton = new TextButton("Register", skin);

        // Use a table to layout the buttons
        Table table = new Table();
        table.setFillParent(true);
        table.center(); // Center the table in the stage

        // Add buttons to the table
        table.add(loginButton).padBottom(10).row();
        table.add(registerButton).padTop(10);

        // Add the table to the stage
        stage.addActor(table);

        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("LoginSignupScreen", "clicked");
                game.setScreen(new LoginScreen(game));
            }
        });

        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("LoginSignupScreen", "register clicked");
                game.setScreen(new RegistrarionScreen(game));
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
