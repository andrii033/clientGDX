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
import com.ta.ClientGDX;
import com.ta.auth.UserService;
import com.ta.data.User;

public class RegistrarionScreen extends InputAdapter implements Screen {
    private Stage stage;
    private TextField usernameField;
    private TextField passwordField;
    private TextField rePasswordField;
    private TextField emailField;
    private Label messageLabel;
    private final UserService userService;
    private final ClientGDX game;

    public RegistrarionScreen(ClientGDX game) {
        this.game = game;
        this.userService = new UserService(game); // Pass the game instance to UserService
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        Label usernameLabel = new Label("Username:", skin);

        usernameField = new TextField("", skin);
        usernameField.setSize(150, 30);

        Label passwordLabel = new Label("Password:", skin);

        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        Label rePasswordLabel = new Label("Re-enter Password:", skin);

        rePasswordField = new TextField("", skin);
        rePasswordField.setPasswordMode(true);
        rePasswordField.setPasswordCharacter('*');

        Label emailLabel = new Label("Email:", skin);

        emailField = new TextField("", skin);

        TextButton registerButton = new TextButton("Register", skin);

        messageLabel = new Label("", skin);

        TextButton backButton = new TextButton("Back", skin);

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

        backButton.setPosition(10, 10);  // Bottom-left corner
        stage.addActor(backButton);

        stage.setKeyboardFocus(usernameField);

        // Register button click listener
        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleRegister();
            }
        });
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LoginSignupScreen(game));
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
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
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
