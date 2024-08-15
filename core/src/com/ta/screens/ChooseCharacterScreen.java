package com.ta.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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
import com.ta.data.CreateCharacterRequest;

public class ChooseCharacterScreen extends InputAdapter implements Screen {
    private Stage stage;
    private Skin skin;
    private final UserService userService;
    private Array<CreateCharacterRequest> characters = new Array<>(); // Initialize the array
    private final ClientGDX game;
    Texture iconTexture;
    Image iconCharacter;

    public ChooseCharacterScreen(ClientGDX game) {
        this.userService = new UserService(game);
        this.game = game;
    }

    public ChooseCharacterScreen(ClientGDX game, Array<CreateCharacterRequest> characters) {
        this.userService = new UserService(game);
        this.game = game;
        this.characters = characters;
    }

    @Override
    public void show() {
        stage = new Stage();
        iconTexture = new Texture(Gdx.files.internal("assets/badlogic.jpg"));
        iconCharacter = new Image(iconTexture);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), new TextureAtlas("skin/uiskin.atlas"));

        //userService.getCharacters(this); // Fetch characters from server
        Table table = new Table();
        table.setFillParent(true);


        TextButton createButton = new TextButton("Create", skin);
        createButton.setPosition(200, 100);
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new CreateCharacteScreen(game));
            }
        });

        for (final CreateCharacterRequest character : characters) {
            TextButton button = new TextButton(character.getName()+" lvl "+character.getLvl(), skin);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("ChooseCharacterScreen", "Character chosen: " + character.getName());
                    userService.chooseCharacter(String.valueOf(character.getId()), character);
                }
            });

            button.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1f)));


            table.add(iconCharacter).size(64, 64);
            table.add(button).center().padBottom(10);
            table.row();
        }

        createButton.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1f)));
        table.row().height(20);
        table.row().height(20);
        table.add(createButton).center().pad(10);
        table.row();
        stage.addActor(table);

        // Adding the "Back" button to the bottom-left corner of the screen
        TextButton backButton = new TextButton("Back", skin);
        backButton.setPosition(10, 10); // Bottom-left corner
        stage.addActor(backButton);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LoginSignupScreen(game));
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
