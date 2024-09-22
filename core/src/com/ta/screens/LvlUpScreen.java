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
import com.ta.data.LvlUpRequest;

public class LvlUpScreen extends InputAdapter implements Screen {
    private Stage stage;
    private Skin skin;
    private final ClientGDX game;
    private final LvlUpRequest request;
    private final UserService userService;

    int pointsMain;
    int str;
    int agi;
    int inte;

    public LvlUpScreen(ClientGDX game, LvlUpRequest lvlUpRequest) {
        this.game = game;
        this.request = lvlUpRequest;
        this.userService = new UserService(game);
        pointsMain=request.getUnallocatedMainPoints();
        str=request.getStr();
        agi=request.getAgi();
        inte=request.getInte();
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        table.center();



        Label pointsLabel = new Label("Main Points ", skin);
        table.add(pointsLabel).pad(10);
        Label pointsCountLabel = new Label(String.valueOf(pointsMain), skin);
        table.add(pointsCountLabel).pad(10);
        table.row();
        Label strengthLabel = new Label("Strength ", skin);
        table.add(strengthLabel).pad(10);
        TextButton strengthButton = new TextButton("+", skin);
        table.add(strengthButton).pad(10);
        TextButton minusStrengthButton = new TextButton("-", skin);
        table.add(minusStrengthButton).pad(10);
        table.row();
        Label agiLabel = new Label("Agility ", skin);
        table.add(agiLabel).pad(10);
        TextButton agiButton = new TextButton("+", skin);
        table.add(agiButton).pad(10);
        TextButton minusAgiButton = new TextButton("-", skin);
        table.add(minusAgiButton).pad(10);
        table.row();
        Label inteLabel = new Label("Intelligence ", skin);
        table.add(inteLabel).pad(10);
        TextButton inteButton = new TextButton("+", skin);
        table.add(inteButton).pad(10);
        TextButton minusInteButton = new TextButton("-", skin);
        table.add(minusInteButton).pad(10);
        table.row();

        TextButton applyButton = new TextButton("Apply", skin);
        table.add(applyButton).pad(10);

        applyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //
            }
        });

        strengthButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(pointsMain>0) {
                    str++;
                    pointsMain--;
                    pointsCountLabel.setText(pointsMain+" ");
                    strengthLabel.setText(str);
                }
            }
        });
        minusStrengthButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(str>0) {
                    str--;
                    pointsMain++;
                    pointsCountLabel.setText(pointsMain);
                    strengthLabel.setText(str);
                }
            }
        });
        agiButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(pointsMain>0) {
                    agi++;
                    pointsMain--;
                    pointsCountLabel.setText(pointsMain);
                    agiLabel.setText(agi);
                }
            }
        });


        minusAgiButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(agi>0) {
                    agi--;
                    pointsMain++;
                    pointsCountLabel.setText(pointsMain);
                    agiLabel.setText(agi);
                }
            }
        });

        inteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(pointsMain>0) {
                    inte++;
                    pointsMain--;
                    pointsCountLabel.setText(pointsMain);
                    inteLabel.setText(inte);
                }
            }
        });

        minusInteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(inte>0) {
                    inte--;
                    pointsMain++;
                    pointsCountLabel.setText(pointsMain);
                    inteLabel.setText(inte);
                }
            }
        });


        TextButton backButton = new TextButton("Back", skin);
        backButton.setPosition(10, 10);  // Bottom-left corner
        stage.addActor(backButton);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                userService.getCharacterInfo();
            }
        });

        stage.addActor(table);
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
