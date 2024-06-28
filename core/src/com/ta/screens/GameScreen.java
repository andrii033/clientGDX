package com.ta.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ta.ClientGDX;
import com.ta.data.User;
import com.ta.auth.UserService;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Texture img;
    private UserService userService;
    private Skin skin;
    private Stage stage;
    private TextButton button;
    private ClientGDX game;


    @Override
    public void show() {
        Gdx.app.log("GameScreen", "show");
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        userService = new UserService(game);

        User user = new User();
        user.setUsername("player");
        user.setPassword("12345678");
        user.setEmail("player@example.com");

        userService.createUser(user);
        //userService.signIn(user);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Load the default skin
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Create a button
        button = new TextButton("Click Me!", skin);
        button.setSize(200, 80);
        button.setPosition(0,
                Gdx.graphics.getHeight() - button.getHeight());

        // Add a click listener to the button
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Button clicked!");
            }
        });

        // Add the button to the stage
        stage.addActor(button);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        stage.dispose();
        skin.dispose();
    }
}
