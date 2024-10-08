package com.ta.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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

    private final TextButton attackButton;
    private final Label label;

    private DungeonService dungeonService;

    private CharacterRequest character;
    private List<EnemyRequest> enemies;

    private static Image currentlyEnlargedIcon;
    private Long enlargedEnemyId;

    private Integer enemyId;

    boolean hasChanges;

    public Timer timer;
    private static int countTime;
    private static int charMaxHp;
    private static int hpBar;

    public BattleCityScreen(ClientGDX game, CharacterRequest character, List<EnemyRequest> enemies, String token) {
        this.game = game;
        this.enemies = enemies;
        this.character = character;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        this.userService = new UserService(game);

        enemyId = Math.toIntExact(enemies.get(0).getId());
        hpBar = 100;
        // Pass this screen to the UserService
        dungeonService = new DungeonService(this, game);

        // Add attack button to the root table
        attackButton = new TextButton("Attack", skin);
        attackButton.setSize(200, 100);

        label = new Label("text", skin);
        label.setPosition((float) Gdx.graphics.getWidth() / 2 - label.getWidth() / 2, Gdx.graphics.getHeight() - 50);

        attackButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        // Call fight method with the current screen instance
                        dungeonService.fight(String.valueOf(enemyId), token);
                    }
                }
        );

        if (currentlyEnlargedIcon != null) {
            currentlyEnlargedIcon.setSize(100, 100);
        }

        stage.addActor(attackButton);
        stage.addActor(label);

        // Schedule a task to update the UI periodically
        timer = new Timer();
        timer.scheduleTask(new Task() {
            @Override
            public void run() {
                dungeonService.fight(String.valueOf(enemyId), token);
                if (countTime >= 0) {
                    label.setText("time: " + countTime);
                    countTime--;
                }
            }
        }, 1, 1); // Schedule the task to run every second

    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        updateCharacter(character, 0);
        updateEnemies(enemies);

    }

    public void updateCharacter(CharacterRequest newCharacter, int damage) {
        hasChanges = !character.getCharacterName().equals(newCharacter.getCharacterName())
                || character.getHp() != newCharacter.getHp()
                || character.getMana() != newCharacter.getMana()
                || character.getExp() != newCharacter.getExp();

        if (hasChanges) {
            countTime = 4;
        }
        if (charMaxHp < newCharacter.getHp()) {
            charMaxHp = newCharacter.getHp();
        }
        this.character = newCharacter;

        // Clear any previous actors before populating new ones
        stage.clear();

        populateLeftEnemyTable(newCharacter, damage);  // Add new data
        stage.addActor(attackButton);  // Add attack button back
        stage.addActor(label);  // Add label back
    }

    public void updateEnemies(List<EnemyRequest> newEnemies) {
        this.enemies = newEnemies;
        populateRightEnemyTable(newEnemies);  // add new data
    }


    private void populateLeftEnemyTable(CharacterRequest character, int damage) {
        Image icon = new Image(new Texture(Gdx.files.internal("obstacle.png"))); // Placeholder for enemy icon
        Label nameLabel = new Label(character.getCharacterName() + "  lvl " + character.getLvl(), skin);
        Label hpLabel = new Label("HP: " + character.getHp(), skin);

        icon.setSize(70, 70);
        icon.setPosition(50, Gdx.graphics.getHeight() - 100);  // Example position
        nameLabel.setPosition(150, Gdx.graphics.getHeight() - 50);  // Position next to the icon
        hpLabel.setPosition(150, Gdx.graphics.getHeight() - 80);  // Position below the name label

        // Add UI elements directly to the stage
        stage.addActor(icon);
        stage.addActor(nameLabel);
        stage.addActor(hpLabel);

        // Create HP bar background (grey or dark)
        Texture hpBarBgTexture = new Texture(Gdx.files.internal("hp_bar_bg.png"));
        Image hpBarBg = new Image(hpBarBgTexture);
        hpBarBg.setSize(100, 10); // Set size of the background bar
        hpBarBg.setPosition(150, Gdx.graphics.getHeight() - 110); // Position below the HP label
        stage.addActor(hpBarBg);

        // Create HP bar fill (red)
        Texture hpBarFillTexture = new Texture(Gdx.files.internal("hp_bar_fill_red.png"));
        Image hpBarFill = new Image(hpBarFillTexture);

        // Calculate width of the red bar based on percentage of remaining HP
        if (character.getHp() > 0) {
            float percentage = ((float) character.getHp() / charMaxHp) * 100;
            hpBar = (int) (100 * (percentage * 0.01));
            hpBarFill.setSize(hpBar, 8); // Adjusted size for the fill
            hpBarFill.setPosition(152, Gdx.graphics.getHeight() - 109); // Adjusted position to center within the background
            stage.addActor(hpBarFill);
        }

        // Handle damage animation
        Label damageLabel = new Label(" " + damage, skin);
        damageLabel.setColor(Color.RED);
        damageLabel.setFontScale(2.5f);
        damageLabel.setVisible(false);
        float hpLabelX = hpLabel.getX() + 30;
        float hpLabelY = hpLabel.getY();
        damageLabel.setPosition(hpLabelX, hpLabelY);

        // Add the damage label to the stage and animate it
        stage.addActor(damageLabel);


// Create Experience bar background (grey or dark)
        Texture expBarBgTexture = new Texture(Gdx.files.internal("exp_bar_bg.png"));
        Image expBarBg = new Image(expBarBgTexture);
        expBarBg.setSize(100, 10); // Set size of the background bar
        expBarBg.setPosition(150, Gdx.graphics.getHeight() - 130); // Position below the HP bar
        stage.addActor(expBarBg);

// Create Experience bar fill (blue)
        Texture expBarFillTexture = new Texture(Gdx.files.internal("exp_bar_fill_blue.png"));
        Image expBarFill = new Image(expBarFillTexture);

// Calculate width of the blue bar based on percentage of remaining experience
        float expPercentage = ((float) character.getExp() / (character.getLvl() * 50)) * 100;
        float expBarWidth = 100 * (expPercentage * 0.01f);
        expBarFill.setSize(expBarWidth, 8); // Adjusted size for the fill
        expBarFill.setPosition(152, Gdx.graphics.getHeight() - 129); // Adjusted position to center within the background
        stage.addActor(expBarFill);

        if (damage < 0) {
            damageLabel.setVisible(true);
            damageLabel.addAction(Actions.sequence(
                    Actions.parallel(
                            Actions.moveBy(0, 50, 1f),
                            Actions.fadeOut(1f)
                    ), Actions.removeActor()
            ));
        }

        // Add input listener to the icon
        icon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Icon Clicked", "Character: " + character.getCharacterName() + " ID: " + character.getId());
            }
        });
    }

    private void populateRightEnemyTable(List<EnemyRequest> enemies) {
        for (EnemyRequest enemy : enemies) {
            Image icon = new Image(new Texture(Gdx.files.internal("obstacle.png"))); // Placeholder for enemy icon
            Label nameLabel = new Label(enemy.getName() + " " + enemy.getId(), skin);
            Label hpLabel = new Label("HP: " + enemy.getHp(), skin);

            icon.setSize(70, 70);

            float initialX = Gdx.graphics.getWidth() - 120;
            float initialY = Gdx.graphics.getHeight() - 100 - (enemies.indexOf(enemy) * 120);

            icon.setPosition(initialX, initialY);
            nameLabel.setPosition(initialX - 100, initialY + 50);
            hpLabel.setPosition(initialX - 100, initialY + 20);

            // Add UI elements directly to the stage
            stage.addActor(icon);
            stage.addActor(nameLabel);
            stage.addActor(hpLabel);

            // Handle damage animation
            Label damageLabel = new Label(" " + enemy.getLatestDam(), skin);
            damageLabel.setColor(Color.RED);
            damageLabel.setFontScale(2.5f);
            damageLabel.setVisible(false);
            float hpLabelX = hpLabel.getX() + 30;
            float hpLabelY = hpLabel.getY();
            damageLabel.setPosition(hpLabelX, hpLabelY);

            // Add the damage label to the stage and animate it
            stage.addActor(damageLabel);

            if (enemy.getLatestDam() < 0 && hasChanges) {
                damageLabel.setVisible(true);
                damageLabel.addAction(Actions.sequence(
                        Actions.parallel(
                                Actions.moveBy(0, 50, 1f),
                                Actions.fadeOut(1f)
                        ), Actions.removeActor()
                ));
                enemy.setLatestDam(0);
            }

            // Check if this is the currently enlarged enemy
            if (enlargedEnemyId != null && enlargedEnemyId.equals(enemy.getId())) {
                icon.setSize(100, 100);
                currentlyEnlargedIcon = icon; // Keep reference to the enlarged icon
            } else {
                icon.setSize(70, 70);
            }


// Create HP bar background (grey or dark)
            Texture hpBarBgTexture = new Texture(Gdx.files.internal("hp_bar_bg.png"));
            Image hpBarBg = new Image(hpBarBgTexture);
            hpBarBg.setSize(100, 10); // Set size of the background bar
            hpBarBg.setPosition(initialX - 100, initialY - 10); // Position below the HP label
            stage.addActor(hpBarBg);

// Create HP bar fill (red)
            Texture hpBarFillTexture = new Texture(Gdx.files.internal("hp_bar_fill_red.png"));
            Image hpBarFill = new Image(hpBarFillTexture);

// Calculate width of the red bar based on percentage of remaining HP
            if (enemy.getHp() > 0) {
                float percentage = ((float) enemy.getHp() / enemy.getMaxHealth()) * 100;
                int hpBarWidth = (int) (100 * (percentage * 0.01));
                hpBarFill.setSize(hpBarWidth, 8); // Adjusted size for the fill
                hpBarFill.setPosition(initialX - 98, initialY - 9); // Adjusted position to center within the background
                stage.addActor(hpBarFill);
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
        }
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
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