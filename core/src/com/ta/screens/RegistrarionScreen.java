package com.ta.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ta.ClientGDX;
import com.ta.auth.UserService;
import com.ta.data.User;

public class RegistrarionScreen extends InputAdapter implements Screen {
    private Stage stage;
    private Skin skin;
    private TextField usernameField;
    private TextField passwordField;
    private TextField emailField;
    private TextButton registerButton;
    private Label messageLabel;
    private UserService userService;
    private ClientGDX game;

    public RegistrarionScreen(ClientGDX game) {
        this.game = game;
        this.userService = new UserService(game); // Pass the game instance to UserService
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        Label usernameLabel = new Label("Username:", skin);
        usernameLabel.setPosition(100, 300);
        stage.addActor(usernameLabel);

        usernameField = new TextField("", skin);
        usernameField.setPosition(200, 300);
        usernameField.setSize(100, 100);
        stage.addActor(usernameField);

        Label passwordLabel = new Label("Password:", skin);
        passwordLabel.setPosition(100, 250);
        stage.addActor(passwordLabel);

        passwordField = new TextField("", skin);
        passwordField.setPosition(200, 250);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        stage.addActor(passwordField);

        Label emailLabel = new Label("Email:", skin);
        emailLabel.setPosition(100, 200);
        stage.addActor(emailLabel);

        emailField = new TextField("", skin);
        emailField.setPosition(200, 200);
        stage.addActor(emailField);

        registerButton = new TextButton("Register", skin);
        registerButton.setPosition(200, 150);
        stage.addActor(registerButton);

        messageLabel = new Label("", skin);
        messageLabel.setPosition(100, 100);
        stage.addActor(messageLabel);

        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                String email = emailField.getText();
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);
                userService.createUser(user);
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
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
    }
}
