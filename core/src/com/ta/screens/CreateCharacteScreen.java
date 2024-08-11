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
import com.ta.data.CreateCharacterRequest;

public class CreateCharacteScreen extends InputAdapter implements Screen {
    private Stage stage;
    private Skin skin;
    private final ClientGDX game;
    private final UserService userService;

    public CreateCharacteScreen(ClientGDX game) {
        this.game = game;
        userService = new UserService(game);
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label characterNameLabel = new Label("Name ", skin);
        table.add(characterNameLabel).pad(10);
        TextField nameTextField = new TextField("", skin);
        table.add(nameTextField).pad(10);
        table.row();
        TextButton createButton = new TextButton("Create", skin);
        table.add(createButton).pad(10);

        stage.addActor(table);

        stage.setKeyboardFocus(nameTextField);

        TextButton backButton = new TextButton("Back", skin);
        backButton.setPosition(10, 10);  // Bottom-left corner
        stage.addActor(backButton);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ChooseCharacterScreen(game));
            }
        });

        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                CreateCharacterRequest characterRequest = new CreateCharacterRequest();
                characterRequest.setName(nameTextField.getText());
                characterRequest.setStr(1);
                characterRequest.setAgi(1);
                characterRequest.setInte(1);
                userService.createCharacter(characterRequest);
                game.setScreen(new ChooseCharacterScreen(game));
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
