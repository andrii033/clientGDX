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
    private final UserService userService;
    private Array<CharacterRequest> characters = new Array<>(); // Initialize the array
    private final ClientGDX game;

    public ChooseCharacterScreen(ClientGDX game) {
        this.userService = new UserService(game);
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), new TextureAtlas("skin/uiskin.atlas"));

        userService.getCharacters(this); // Fetch characters from server

//        TextButton backButton = new TextButton("Back", skin);
//        backButton.setPosition(10, 10);  // Bottom-left corner
//        stage.addActor(backButton);
//
//        backButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                game.setScreen(new ChooseCharacterScreen(game));
//            }
//        });
    }

    public void setCharacters(Array<CharacterRequest> characters) {
        if (characters == null) {
            Gdx.app.log("ChooseCharacterScreen", "Received null characters array.");
            this.characters = new Array<>(); // Prevent null issues
        } else {
            Gdx.app.log("ChooseCharacterScreen", "Received characters: " + characters.size);
            this.characters = characters;
        }
        displayCharacters();
    }

    private void refreshScreen() {
        userService.getCharacters(this); // Fetch characters from server again
    }

    private void displayCharacters() {
        stage.clear();
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton createButton = new TextButton("Create ", skin);
        createButton.setPosition(200, 100);
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //userService.createCharacter();
                //refreshScreen(); // Refresh the screen after creating a character
                game.setScreen(new CreateCharacteScreen(game));
            }
        });

        createButton.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1f)));
        table.add(createButton).center().padBottom(10);
        table.row();

        for (final CharacterRequest character : characters) {
            Label characterNameLabel = new Label(character.getCharacterName() + " lvl " + character.getLvl(), skin);
            TextButton button = new TextButton("Choose " + character.getCharacterName() + " " + character.getId(), skin);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("ChooseCharacterScreen", "Character chosen: " + character.getCharacterName());
                    userService.chooseCharacter(character.getId().toString(),character);
                }
            });

            button.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1f)));

            table.add(button).center().padBottom(10);
            table.row();
            table.add(characterNameLabel).center().padBottom(10);
            table.row();
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
