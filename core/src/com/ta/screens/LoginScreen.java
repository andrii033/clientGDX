package com.ta.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.ta.ClientGDX;
import com.ta.auth.UserService;

public class LoginScreen extends InputAdapter implements Screen {
    private Stage stage;
    private Skin skin;
    private UserService userService;
    private ClientGDX game;

    public LoginScreen(ClientGDX game) {
        this.game = game;
        userService = new UserService();
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        Label usernameLabel = new Label("Username:", skin);
        usernameLabel.setPosition(100, 250);
        stage.addActor(usernameLabel);

        TextField usernameField = new TextField("", skin);
        usernameField.setPosition(200, 250);
        stage.addActor(usernameField);

        Label passwordLabel = new Label("Password:", skin);
        passwordLabel.setPosition(100, 200);
        stage.addActor(passwordLabel);

        TextField passwordField = new TextField("", skin);
        passwordField.setPosition(200, 200);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        stage.addActor(passwordField);

        TextButton loginButton = new TextButton("Login", skin);
        loginButton.setPosition(200, 150);
        stage.addActor(loginButton);

        Label messageLabel = new Label("", skin);
        messageLabel.setPosition(100, 100);
        stage.addActor(messageLabel);

//        loginButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                String username = usernameField.getText();
//                String password = passwordField.getText();
//                User user = new User();
//                user.setUsername(username);
//                user.setPassword(password);
//                boolean loginSuccess = userService.signIn(user);
//                if (loginSuccess) {
//                    game.setScreen(new CharScreen(game));
//                } else {
//                    messageLabel.setText("Login failed. Please try again.");
//                }
//            }
//        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 1);
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
