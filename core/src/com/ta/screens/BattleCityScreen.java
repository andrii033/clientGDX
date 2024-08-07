package com.ta.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ta.ClientGDX;
import com.ta.auth.UserService;
import com.ta.data.CharacterRequest;
import com.ta.data.EnemyRequest;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class BattleCityScreen extends InputAdapter implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final ClientGDX game;
    private final Table rootTable;
    private final Table leftEnemyTable;
    private final Table rightEnemyTable;
    private final Table turnOrderTable;
    private TextButton attackButton;
    private Label timeLeftLabel;

    private UserService userService;

    private CharacterRequest character;
    private List<EnemyRequest> enemies;

    private static Image currentlyEnlargedIcon = null;
    private static boolean isIconEnlarged = false;

    private Timer timer;

    public BattleCityScreen(ClientGDX game, List<EnemyRequest> enemies, CharacterRequest character) {
        this.game = game;
        this.enemies = enemies;
        this.character = character;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        userService = new UserService(game);

        // Initialize tables
        rootTable = new Table();
        rootTable.setFillParent(true);

        leftEnemyTable = new Table();
        rightEnemyTable = new Table();
        turnOrderTable = new Table();
        timeLeftLabel = new Label("Time Left: ", skin);

        // Set table alignments
        leftEnemyTable.top().left();
        rightEnemyTable.top().right();
        turnOrderTable.bottom();

        // Add enemy tables to the root table
        rootTable.add(leftEnemyTable).expand().top().left().pad(10);
        rootTable.add(rightEnemyTable).expand().top().right().pad(10).row();
        rootTable.add(turnOrderTable).expandX().bottom().center().pad(10).colspan(2);

        // Add time left label
        rootTable.row(); // Move to the next row
        rootTable.add(timeLeftLabel).expand().center().colspan(2).pad(10);

        // Add attack button to the root table
        attackButton = new TextButton("Attack", skin);
        attackButton.setSize(200, 100);
        rootTable.row(); // Move to the next row
        rootTable.add(attackButton).expand().center().colspan(2).pad(10);
        attackButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        userService.fight(String.valueOf(1));
                        userService.party();            //временно добавлено потом изменить
                    }
                }
        );

        stage.addActor(rootTable);

        // Schedule a task to update the UI periodically
        timer = new Timer();
        timer.scheduleTask(new Task() {
            @Override
            public void run() {
                // Perform periodic updates here
                updateTimeLeft();
            }
        }, 1, 1); // Schedule the task to run every second
    }

    private void updateTimeLeft() {
        // Fetch the time left from the server (this could be done through WebSocket or HTTP request)
        // For this example, let's just simulate it
        long timeLeft = getTimeLeftFromServer(); // Replace with actual server call

        Gdx.app.postRunnable(() -> {
            timeLeftLabel.setText("Time Left: " + timeLeft + "ms");
        });
    }

    private long getTimeLeftFromServer() {
        // Simulate server call - replace this with actual logic to get time left from the server
        return 10000 - (System.currentTimeMillis() % 10000);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        updateCharacter(character);
        updateEnemies(enemies);
    }

    public void updateCharacter(CharacterRequest newCharacter) {
        this.character = newCharacter;
        leftEnemyTable.clear();  // Очистка текущих данных
        populateLeftEnemyTable(newCharacter);  // Добавление новых данных
    }

    public void updateEnemies(List<EnemyRequest> newEnemies) {
        this.enemies = newEnemies;
        rightEnemyTable.clear();  // Очистка текущих данных
        populateRightEnemyTable(newEnemies);  // Добавление новых данных
        updateTurnOrder(newEnemies);  // Обновление порядка хода
    }

    private void updateTurnOrder(List<EnemyRequest> turnOrder) {
        turnOrderTable.clear();  // Очистка текущих данных
        populateTurnOrderTable(turnOrder);  // Добавление новых данных
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
                Gdx.app.log("Icon Clicked", "Enemy: " + character.getCharacterName() + " ID: " + character.getId());
                // Add your logic here for what happens when the icon is clicked
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
        if (timer != null) {
            timer.clear();
        }
    }
}
