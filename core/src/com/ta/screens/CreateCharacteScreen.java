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
import com.ta.data.CreateCharacterRequest;

public class CreateCharacteScreen extends InputAdapter implements Screen {
    private Stage stage;
    private Skin skin;
    private final ClientGDX game;
    private final UserService userService;
    private Integer points;
    private Integer str;
    private Integer agi;
    private Integer inte;

    public CreateCharacteScreen(ClientGDX game) {
        this.game = game;
        userService = new UserService(game);
        points = 3;
        str=0;
        agi=0;
        inte=0;
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
        Label pointsLabel = new Label("Points ", skin);
        table.add(pointsLabel).pad(10);
        Label pointsCountLabel = new Label(points.toString(), skin);
        table.add(pointsCountLabel).pad(10);
        table.row();
        Label strLabel = new Label("Str ", skin);
        table.add(strLabel).pad(10);
        Label strengthLabel = new Label("0", skin);
        table.add(strengthLabel).pad(10);
        TextButton strengthButton = new TextButton("+", skin);
        table.add(strengthButton).pad(10);
        TextButton minusStrengthButton = new TextButton("-", skin);
        table.add(minusStrengthButton).pad(10);
        table.row();
        Label agiLabel1 = new Label("Agi", skin);
        table.add(agiLabel1).pad(10);
        Label agiLabel = new Label("0", skin);
        table.add(agiLabel).pad(10);
        TextButton agiButton = new TextButton("+", skin);
        table.add(agiButton).pad(10);
        TextButton minusAgiButton = new TextButton("-", skin);
        table.add(minusAgiButton).pad(10);
        table.row();
        Label inteLabel1 = new Label("Int", skin);
        table.add(inteLabel1).pad(10);
        Label inteLabel = new Label("0", skin);
        table.add(inteLabel).pad(10);
        TextButton inteButton = new TextButton("+", skin);
        table.add(inteButton).pad(10);
        TextButton minusInteButton = new TextButton("-", skin);
        table.add(minusInteButton).pad(10);
        table.row();

        TextButton createButton = new TextButton("Create", skin);
        table.add(createButton).pad(10);

        stage.addActor(table);

        strengthButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(points>0) {
                    str++;
                    points--;
                    pointsCountLabel.setText(points.toString());
                    strengthLabel.setText(str);
                }
            }
        });
        minusStrengthButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(str>0) {
                    str--;
                    points++;
                    pointsCountLabel.setText(points.toString());
                    strengthLabel.setText(str);
                }
            }
        });
        agiButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(points>0) {
                    agi++;
                    points--;
                    pointsCountLabel.setText(points.toString());
                    agiLabel.setText(agi);
                }
            }
        });


        minusAgiButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(agi>0) {
                    agi--;
                    points++;
                    pointsCountLabel.setText(points.toString());
                    agiLabel.setText(agi);
                }
            }
        });

        inteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(points>0) {
                    inte++;
                    points--;
                    pointsCountLabel.setText(points.toString());
                    inteLabel.setText(inte);
                }
            }
        });

        minusInteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(inte>0) {
                    inte--;
                    points++;
                    pointsCountLabel.setText(points.toString());
                    inteLabel.setText(inte);
                }
            }
        });

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
                createCharacter(nameTextField.getText(),str,agi,inte);
            }
        });

        // Input listener for Enter key press
        InputListener enterListener = new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    createCharacter(nameTextField.getText(),str,agi,inte);
                    return true;
                }
                return false;
            }
        };

        nameTextField.addListener(enterListener);
    }

    private void createCharacter(String name, Integer str, Integer agi, Integer inte){
        CreateCharacterRequest characterRequest = new CreateCharacterRequest();
        characterRequest.setName(name);
        characterRequest.setStr(str);
        characterRequest.setAgi(agi);
        characterRequest.setInte(inte);
        userService.createCharacter(characterRequest);
        game.setScreen(new ChooseCharacterScreen(game));
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
