package com.ta.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ta.ClientGDX;
import com.ta.data.EnemyRequest;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;

public class BattleCityScreen extends InputAdapter implements Screen {
    private Stage stage;
    private Skin skin;
    private ClientGDX game;
    private Table rootTable;
    private Table leftEnemyTable;
    private Table rightEnemyTable;
    private Table turnOrderTable;

    private List<EnemyRequest> enemies;

    private static Image currentlyEnlargedIcon = null;
    private static boolean isIconEnlarged = false;

    public BattleCityScreen(ClientGDX game, List<EnemyRequest> enemies) {
        this.game = game;
        this.enemies = enemies;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        // Initialize tables
        rootTable = new Table();
        rootTable.setFillParent(true);

        leftEnemyTable = new Table();
        rightEnemyTable = new Table();
        turnOrderTable = new Table();

        // Set table alignments
        leftEnemyTable.top().left();
        rightEnemyTable.top().right();
        turnOrderTable.bottom();

        // Add enemy tables to the root table
        rootTable.add(leftEnemyTable).expand().top().left().pad(10);
        rootTable.add(rightEnemyTable).expand().top().right().pad(10).row();
        rootTable.add(turnOrderTable).expandX().bottom().center().pad(10).colspan(2);

        stage.addActor(rootTable);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        populateLeftEnemyTable(enemies);
        populateRightEnemyTable(enemies);
        populateTurnOrderTable(enemies);
    }

    private void populateLeftEnemyTable(List<EnemyRequest> enemies) {
        for (EnemyRequest enemy : enemies) {
            Table enemyRow = new Table();
            Image icon = new Image(new Texture(Gdx.files.internal("obstacle.png"))); // Placeholder for enemy icon
            Label nameLabel = new Label(enemy.getName() + " " + enemy.getId(), skin);
            Label hpLabel = new Label("HP: " + enemy.getHp(), skin);
            Label latestDamLabel = new Label("Latest Damage: " + enemy.getLatestDam(), skin);

            // Add input listener to the icon
            icon.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("Icon Clicked", "Enemy: " + enemy.getName() + " ID: " + enemy.getId());
                    // Add your logic here for what happens when the icon is clicked
                }
            });

            enemyRow.add(icon).size(70, 70).pad(5);
            enemyRow.add(nameLabel).pad(5);
            enemyRow.add(hpLabel).pad(5);
            enemyRow.add(latestDamLabel).pad(5);

            leftEnemyTable.add(enemyRow).row();
        }
    }

    private void populateRightEnemyTable(List<EnemyRequest> enemies) {
        for (EnemyRequest enemy : enemies) {
            Table enemyRow = new Table();
            Image icon = new Image(new Texture(Gdx.files.internal("obstacle.png"))); // Placeholder for enemy icon
            Label nameLabel = new Label(enemy.getName() + " " + enemy.getId(), skin);
            Label hpLabel = new Label("HP: " + enemy.getHp(), skin);
            Label latestDamLabel = new Label("Latest Damage: " + enemy.getLatestDam(), skin);

            // Add input listener to the icon
            icon.addListener(new ClickListener() {
                boolean enlarged = false;

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("Icon Clicked", "Enemy: " + enemy.getName() + " ID: " + enemy.getId());
                    if (currentlyEnlargedIcon != null) {
                        currentlyEnlargedIcon.setSize(70, 70);
                    }

                    if (currentlyEnlargedIcon == icon) {
                        currentlyEnlargedIcon = null;
                        isIconEnlarged = false;
                    } else {
                        icon.setSize(100, 100);
                        currentlyEnlargedIcon = icon;
                        isIconEnlarged = true;
                    }
                    enlarged = !enlarged;
                }
            });

            enemyRow.add(icon).size(70, 70).pad(5);
            enemyRow.add(nameLabel).pad(5);
            enemyRow.add(hpLabel).pad(5);
            enemyRow.add(latestDamLabel).pad(5);

            rightEnemyTable.add(enemyRow).row();
        }
    }


    private void populateTurnOrderTable(List<EnemyRequest> turnOrder) {
        for (EnemyRequest entity : turnOrder) {
            Table entityRow = new Table();
            Image icon = new Image(new Texture(Gdx.files.internal("grass.png"))); // Placeholder for enemy icon
            Label nameLabel = new Label(entity.getName(), skin);

            entityRow.add(icon).size(32, 32).pad(5);
            entityRow.add(nameLabel).pad(5);

            turnOrderTable.add(entityRow).pad(10);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
