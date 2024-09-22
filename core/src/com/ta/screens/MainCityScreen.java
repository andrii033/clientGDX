package com.ta.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ta.ClientGDX;
import com.ta.auth.UserService;
import com.ta.data.CharacterRequest;

public class MainCityScreen extends InputAdapter implements Screen {

    private Stage stage;
    private Skin skin;
    private TextButton moveButton;
    private ClientGDX game;
    private CharacterRequest characterRequest;
    private UserService userService;

    public MainCityScreen(ClientGDX game, CharacterRequest characterRequest) {
        this.game = game;
        this.characterRequest = characterRequest;
        this.userService = new UserService(game);
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        moveButton = new TextButton("Dungeons", skin);
        moveButton.getLabel().setFontScale(1.5f);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Character button in the top-left corner
        TextButton characterButton = new TextButton(characterRequest.getCharacterName()+"\n lvl "+characterRequest.getLvl()+
                " hp: "+characterRequest.getHp(), skin);
        characterButton.getLabel().setFontScale(1.5f);
        table.top().left();
        table.add(characterButton).size(250, 100).expand().fill().padBottom(10);
        characterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Character button clicked", characterRequest.getCharacterName());
                userService.getLvlUp();
            }
        });

        // City label next to the character button
        Label cityLabel = new Label("MainCity", skin);
        table.add(cityLabel).size(250, 100).expand().fill().padBottom(10);

        //Inventory button
        TextButton inventoryButton = new TextButton("Inventory", skin);
        inventoryButton.getLabel().setFontScale(1.5f);
        table.add(inventoryButton).size(250, 100).expand().fill().padBottom(10).row();

        //Shop button
        TextButton shopButton = new TextButton("Shop", skin);
        shopButton.getLabel().setFontScale(1.5f);
        table.add(shopButton).size(250, 100).expand().fill().padBottom(10).colspan(3).row();

        //Blacksmith button
        TextButton blacksmithButton = new TextButton("Blacksmith", skin);
        blacksmithButton.getLabel().setFontScale(1.5f);
        table.add(blacksmithButton).size(250, 100).expand().fill().padBottom(10).colspan(3).row();

        // Move button on a new row
        table.add(moveButton).size(250, 100).expand().fill().padBottom(10).colspan(3); // colspan=2 to make it span across both columns

        // Add the table to the stage
        stage.addActor(table);

        moveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Move", "clicked");
                game.setScreen(new MoveScreen(game, characterRequest));
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
