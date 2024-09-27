package com.ta.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ta.ClientGDX;
import com.ta.auth.UserService;
import com.ta.data.CharacterRequest;
import com.ta.data.EnemyRequest;
import com.ta.game.DungeonService;
import lombok.var;

import java.util.List;
import java.util.Objects;

public class BattleCityScreen extends InputAdapter implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final ClientGDX game;
    private final UserService userService;
    private final Table rootTable;
    private final Table leftEnemyTable;
    private final Table rightEnemyTable;
    private final Table turnOrderTable;
    private final TextButton attackButton;
    private final Label label;

    private DungeonService dungeonService;

    private CharacterRequest character;
    private List<EnemyRequest> enemies;

    private static Image currentlyEnlargedIcon ;
    private Long enlargedEnemyId;
    private static boolean isIconEnlarged = false;

    private Integer enemyId;

    public Timer timer;
    private static int countTime;

    public BattleCityScreen(ClientGDX game, CharacterRequest character, List<EnemyRequest> enemies, String token) {
        this.game = game;
        this.enemies = enemies;
        this.character = character;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        this.userService = new UserService(game);


        enemyId = Math.toIntExact(enemies.get(0).getId());

        // Pass this screen to the UserService
        dungeonService = new DungeonService( this,game);

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

        // Add attack button to the root table
        attackButton = new TextButton("Attack", skin);
        attackButton.setSize(200, 100);
        rootTable.row(); // Move to the next row
        rootTable.add(attackButton).expand().center().colspan(2).pad(10);

        label = new Label("text", skin);
        rootTable.row();
        rootTable.add(label).expand().center().colspan(2).pad(10);

        attackButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        // Call fight method with the current screen instance
                        dungeonService.fight(String.valueOf(enemyId),token);
                    }
                }
        );

        if (currentlyEnlargedIcon != null) {
            currentlyEnlargedIcon.setSize(100, 100);
            isIconEnlarged = true;
        }

        stage.addActor(rootTable);

        // Schedule a task to update the UI periodically
        timer = new Timer();
        timer.scheduleTask(new Task() {
            @Override
            public void run() {
                dungeonService.fight(String.valueOf(enemyId),token);
                if(countTime>=0) {
                    label.setText("time: " + countTime);
                    countTime--;
                }
            }
        }, 1, 1); // Schedule the task to run every second

    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        updateCharacter(character);
        updateEnemies(enemies);

    }
    public void updateCharacter(CharacterRequest newCharacter) {
        boolean hasChanges = !character.getCharacterName().equals(newCharacter.getCharacterName())
                || character.getHp() != newCharacter.getHp()
                || character.getMana() != newCharacter.getMana()
                || character.getExp() != newCharacter.getExp();
        if(hasChanges){
            countTime=5;
        }
        this.character = newCharacter;
        leftEnemyTable.clear();  // Очистка текущих данных
        populateLeftEnemyTable(newCharacter);  // Добавление новых данных

    }

    public void updateEnemies(List<EnemyRequest> newEnemies) {
        this.enemies = newEnemies;
        rightEnemyTable.clear();  // clear data
        populateRightEnemyTable(newEnemies);  // add new data
    }


    private void populateLeftEnemyTable(CharacterRequest character) {
        Table enemyRow = new Table();
        Image icon = new Image(new Texture(Gdx.files.internal("obstacle.png"))); // Placeholder for enemy icon
        Label nameLabel = new Label(character.getCharacterName() + " " + character.getId(), skin);
        Label hpLabel = new Label("HP: " + character.getHp(), skin);

        // Add input listener to the icon
        icon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Icon Clicked", "Character: " + character.getCharacterName() + " ID: " + character.getId());
            }
        });

        enemyRow.add(icon).size(70, 70).pad(5);
        enemyRow.add(nameLabel).pad(5);
        enemyRow.add(hpLabel).pad(5);

        leftEnemyTable.add(enemyRow).row();
    }

    private void populateRightEnemyTable(List<EnemyRequest> enemies) {
        for (EnemyRequest enemy : enemies) {
            Table enemyRow = new Table();
            Image icon = new Image(new Texture(Gdx.files.internal("obstacle.png"))); // Placeholder for enemy icon
            Label nameLabel = new Label(enemy.getName() + " " + enemy.getId(), skin);
            Label hpLabel = new Label("HP: " + enemy.getHp(), skin);
            Label latestDamLabel = new Label("Latest Damage: " + enemy.getLatestDam(), skin);


            // Check if this is the currently enlarged enemy
            if (enlargedEnemyId != null && enlargedEnemyId.equals(enemy.getId())) {
                icon.setSize(100, 100);
                currentlyEnlargedIcon = icon; // Keep reference to the enlarged icon
            } else {
                icon.setSize(70, 70);
            }

            // Add input listener to the icon
            icon.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("Icon Clicked", "Enemy: " + enemy.getName() + " ID: " + enemy.getId());
                    enemyId = Math.toIntExact(enemy.getId());


                    if (currentlyEnlargedIcon != null) {
                        currentlyEnlargedIcon.setSize(70, 70);
                    }

                    if (currentlyEnlargedIcon == icon) {
                        currentlyEnlargedIcon = null;
                        enlargedEnemyId = null;
                    } else {
                        icon.setSize(100, 100);
                        currentlyEnlargedIcon = icon;
                        enlargedEnemyId = enemy.getId();
                    }
                }
            });

            enemyRow.add(icon).size(icon.getWidth(), icon.getHeight()).pad(5); // Use icon size
            enemyRow.add(nameLabel).pad(5);
            enemyRow.add(hpLabel).pad(5);
            enemyRow.add(latestDamLabel).pad(5);

            rightEnemyTable.add(enemyRow).row();
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
        if (timer != null) {
            timer.stop();
        }
        currentlyEnlargedIcon = null;
        enlargedEnemyId = null;

    }

}
