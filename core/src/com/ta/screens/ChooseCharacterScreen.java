package com.ta.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.ta.ClientGDX;
import com.ta.auth.UserService;
import com.ta.data.CharacterRequest;

public class ChooseCharacterScreen extends InputAdapter implements Screen {
    private Stage stage;
    private Skin skin;
    private UserService userService;
    private ClientGDX game;
    private Array<CharacterRequest> characters = new Array<>(); // Initialize the array

    public ChooseCharacterScreen(ClientGDX game) {
        this.game = game;
        this.userService = new UserService(game);
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"),new TextureAtlas("skin/uiskin.atlas"));

        userService.getCharacters(this); // Fetch characters from server
    }

    public void setCharacters(Array<CharacterRequest> characters) {
        this.characters = characters;
        displayCharacters();
    }

    private void displayCharacters() {
        stage.clear();
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        int y = 300;
        TextButton createButton = new TextButton("Create ", skin);
        createButton.setPosition(200, 100);
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                userService.createCharacter();
            }
        });

        createButton.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1f)));
        table.add(createButton).center().padBottom(10);
        table.row();

        for (final CharacterRequest character : characters) {
            Label characterNameLabel = new Label(character.getCharacterName(), skin);
            TextButton button = new TextButton("Choose " + character.getCharacterName() + " " + character.getId(), skin);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("ChooseCharacterScreen", "Character chosen: " + character.getCharacterName());
                    userService.chooseCharacter(character.getId().toString());
                }
            });

            button.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1f)));

            table.add(characterNameLabel).left().padRight(10);
            table.add(button).right().padBottom(10);
            table.row();
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
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
