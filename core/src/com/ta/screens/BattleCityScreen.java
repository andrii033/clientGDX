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
import com.ta.data.CharacterRequest;
import com.ta.data.EnemyRequest;
import com.ta.data.TurnOrderEntry;
import com.ta.game.DungeonService;

import java.util.*;
import java.util.List;

public class BattleCityScreen extends InputAdapter implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final ClientGDX game;
    private final Table rootTable;
    private final Table leftEnemyTable;
    private final Table rightEnemyTable;
    private final Table turnOrderTable;
    private TextButton attackButton;

    private DungeonService dungeonService;

    private CharacterRequest character;
    private List<EnemyRequest> enemies;

    private static Image currentlyEnlargedIcon ;
    private Long enlargedEnemyId;
    private static boolean isIconEnlarged = false;

    private Integer enemyId;

    private Timer timer;

    private long lastFightRequestTime = 0;
    private static final long REQUEST_INTERVAL = 5000; // 5 seconds


    public BattleCityScreen(ClientGDX game, CharacterRequest character, List<EnemyRequest> enemies, String token) {
        this.game = game;
        this.enemies = enemies;
        this.character = character;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        enemyId = Math.toIntExact(enemies.get(0).getId());


        // Pass this screen to the UserService
        dungeonService = new DungeonService( this);

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

//        // Add time left label
//        rootTable.row(); // Move to the next row
//        rootTable.add(timeLeftLabel).expand().center().colspan(2).pad(10);

        // Add attack button to the root table
        attackButton = new TextButton("Attack", skin);
        attackButton.setSize(200, 100);
        rootTable.row(); // Move to the next row
        rootTable.add(attackButton).expand().center().colspan(2).pad(10);
        attackButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        // Call fight method with the current screen instance
                        //userService.fight(String.valueOf(enemyId));
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
                // Perform periodic updates here
                updateTimeLeft();
                dungeonService.fight(String.valueOf(enemyId),token);
            }
        }, 1, 3); // Schedule the task to run every second
    }



    private void updateTimeLeft() {
        long timeLeft = getTimeLeftFromServer();

        Gdx.app.postRunnable(() -> {

            // Get the current time
            long currentTime = System.currentTimeMillis();

            // Check if enough time has passed since the last request
            if (currentTime - lastFightRequestTime >= REQUEST_INTERVAL) {
                // Update the time of the last request
                lastFightRequestTime = currentTime;
            }
        });
    }


    private long getTimeLeftFromServer() {
        // Simulate server call - replace this with actual logic to get time left from the server
        long timeLeftMillis = 10000 - (System.currentTimeMillis() % 10000);
        return timeLeftMillis / 1000; // Convert milliseconds to seconds
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        updateCharacter(character);
        updateEnemies(enemies);

//        if (currentlyEnlargedIcon != null) {
//            currentlyEnlargedIcon.setSize(100, 100);
//            isIconEnlarged = true;
//        }
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


    private void populateTurnOrderTable(List<EnemyRequest> turnOrder) {
        List<TurnOrderEntry> turnOrderList = new ArrayList<>();

        for (EnemyRequest entity : turnOrder) {
            turnOrderList.add(new TurnOrderEntry(entity.getInitiative(), entity.getName()));
        }

        // Sort the list by initiative using Collections.sort
        Collections.sort(turnOrderList, new Comparator<TurnOrderEntry>() {
            @Override
            public int compare(TurnOrderEntry entry1, TurnOrderEntry entry2) {
                return Integer.compare(entry1.getInitiative(), entry2.getInitiative());
            }
        });

        // Populate the UI table
        for (TurnOrderEntry entry : turnOrderList) {
            Table entityRow = new Table();
            Image icon = new Image(new Texture(Gdx.files.internal("grass.png"))); // Placeholder for enemy icon
            Label nameLabel = new Label(entry.getName(), skin);

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
        currentlyEnlargedIcon = null;
        enlargedEnemyId = null;
    }

}
