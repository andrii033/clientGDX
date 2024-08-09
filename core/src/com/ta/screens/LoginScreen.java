package com.ta.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ta.ClientGDX;
import com.ta.auth.UserService;
import com.ta.data.User;

public class LoginScreen extends InputAdapter implements Screen {
    private Stage stage;
    private Skin skin;
    private final UserService userService;
    private final ClientGDX game;

    public LoginScreen(ClientGDX game) {
        userService = new UserService(game);
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        Label usernameLabel = new Label("Username:", skin);

        TextField usernameField = new TextField("", skin);


        Label passwordLabel = new Label("Password:", skin);

        TextField passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        TextButton loginButton = new TextButton("Login", skin);

        Label messageLabel = new Label("", skin);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(usernameLabel).pad(10);
        table.add(usernameField).pad(10);
        table.row();
        table.add(passwordLabel).pad(10);
        table.add(passwordField).pad(10);
        table.row();
        table.add(loginButton).pad(10);
        table.row();
        table.add(messageLabel).pad(10);

        stage.addActor(table);

        TextButton backButton = new TextButton("Back", skin);
        backButton.setPosition(10, 10);  // Bottom-left corner
        stage.addActor(backButton);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LoginSignupScreen(game));
            }
        });

        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                userService.signIn(user);
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
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
