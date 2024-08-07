package com.ta.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.ta.ClientGDX;
import com.ta.auth.UserService;
import com.ta.data.User;

public class RegistrarionScreen extends InputAdapter implements Screen {
    private Stage stage;
    private Skin skin;
    private TextField usernameField;
    private TextField passwordField;
    private TextField rePasswordField;
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

        usernameField = new TextField("", skin);
        usernameField.setPosition(200, 300);
        usernameField.setSize(150, 30);

        Label passwordLabel = new Label("Password:", skin);
        passwordLabel.setPosition(100, 250);

        passwordField = new TextField("", skin);
        passwordField.setPosition(200, 250);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        Label rePasswordLabel = new Label("Re-enter Password:", skin);
        rePasswordLabel.setPosition(100, 200);

        rePasswordField = new TextField("", skin);
        rePasswordField.setPosition(200, 200);
        rePasswordField.setPasswordMode(true);
        rePasswordField.setPasswordCharacter('*');

        Label emailLabel = new Label("Email:", skin);
        emailLabel.setPosition(100, 150);

        emailField = new TextField("", skin);
        emailField.setPosition(200, 150);

        registerButton = new TextButton("Register", skin);
        registerButton.setPosition(200, 100);

        messageLabel = new Label("", skin);
        messageLabel.setPosition(100, 50);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(usernameLabel).bottom().pad(10);
        table.add(usernameField).bottom().pad(10).row();
        table.add(passwordLabel).pad(10).bottom();
        table.add(passwordField).pad(10).bottom().row();
        table.add(rePasswordLabel).pad(10).bottom();
        table.add(rePasswordField).pad(10).bottom().row();
        table.add(emailLabel).pad(10).bottom();
        table.add(emailField).pad(10).bottom().row();
        table.add(registerButton).pad(10).bottom();
        table.add(messageLabel).pad(10).bottom().row();

        stage.addActor(table);

        stage.setKeyboardFocus(usernameField);

        // Register button click listener
        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleRegister();
            }
        });

        // Input listener for Enter key press
        InputListener enterListener = new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    handleRegister();
                    return true;
                }
                return false;
            }
        };

        usernameField.addListener(enterListener);
        passwordField.addListener(enterListener);
        rePasswordField.addListener(enterListener);
        emailField.addListener(enterListener);
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String rePassword = rePasswordField.getText();
        String email = emailField.getText();

        if (username.isEmpty() || password.isEmpty() || rePassword.isEmpty() || email.isEmpty()) {
            messageLabel.setText("All fields must be filled.");
        } else if (!password.equals(rePassword)) {
            messageLabel.setText("Passwords do not match.");
        } else {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            userService.createUser(user);
            messageLabel.setText("User registered successfully.");
        }
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
