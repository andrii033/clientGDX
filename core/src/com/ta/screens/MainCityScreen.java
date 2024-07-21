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
import com.ta.data.CharacterRequest;

public class MainCityScreen extends InputAdapter implements Screen {

    private Stage stage;
    private Skin skin;
    private TextButton moveButton;
    private ClientGDX game;
    CharacterRequest character;

    public MainCityScreen(ClientGDX game, CharacterRequest character) {
        this.game = game;
        this.character = character;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        moveButton = new TextButton("Move to moveScreen", skin);
        // Enlarge buttons
        moveButton.setSize(200, 80); // Width = 200, Height = 80

        // Optionally, scale the font
        moveButton.getLabel().setFontScale(1.5f);

        Table table = new Table();
        table.setFillParent(true);
        table.center(); // Center the table in the stage

        // Add buttons to the table with size parameters
        table.add(moveButton).size(200, 80).padBottom(10).row();

        // Add the table to the stage
        stage.addActor(table);

        moveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Move", "clicked");
                game.setScreen(new MoveScreen(game,character));
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
        stage.dispose();
        skin.dispose();
    }

}
