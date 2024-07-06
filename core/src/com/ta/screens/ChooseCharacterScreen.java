package com.ta.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.ta.ClientGDX;
import com.ta.auth.UserService;
import com.ta.data.CharacterRequest;
import com.ta.data.User;

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
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        userService.getCharacters(this); // Fetch characters from server
    }

    public void setCharacters(Array<CharacterRequest> characters) {
        this.characters = characters;
        displayCharacters();
    }

    private void displayCharacters() {
        //stage.clear(); // Clear previous actors
        int y = 300;
        TextButton createButton = new TextButton("Create ", skin);
        createButton.setPosition(200, 100);
        stage.addActor(createButton);
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                userService.createCharacter();
            }
        });

        for (final CharacterRequest character : characters) {
            Label characterNameLabel = new Label(character.getCharacterName(), skin);
            characterNameLabel.setPosition(50, y);
            stage.addActor(characterNameLabel);

            TextButton button = new TextButton("Choose " + character.getCharacterName()+" "+character.getId(), skin);
            button.setPosition(200, y);
            stage.addActor(button);

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("ChooseCharacterScreen", "Character chosen: " + character.getCharacterName());
                    userService.chooseCharacter(character.getId().toString());
                }
            });

            y -= 50;
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
