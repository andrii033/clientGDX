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
import com.ta.data.LvlUpRequest;

public class LvlUpScreen extends InputAdapter implements Screen {
    private Stage stage;
    private Skin skin;
    private final ClientGDX game;
    private final LvlUpRequest request;
    private final UserService userService;

    private Label pointsCountLabel;

    int pointsMain;
    int str;
    int agi;
    int inte;

    private int unallocatedStrPoints;

    private int physicalHarm;
    private int armorPiercing;
    private int reduceBlockDam;
    private int maxHealth;

    private int unallocatedAgiPoints;
    
    private int critChance;
    private int attackSpeed;
    private int avoidance;
    private int blockChance;

    private int unallocatedIntePoints;

    private int magicDam;
    private int magicCritChance;
    private int manaRegen;
    private int maxMana;

    public LvlUpScreen(ClientGDX game, LvlUpRequest lvlUpRequest) {
        this.game = game;
        this.request = lvlUpRequest;
        this.userService = new UserService(game);
        pointsMain = request.getUnallocatedMainPoints();
        str = request.getStr();
        agi = request.getAgi();
        inte = request.getInte();

        unallocatedStrPoints = request.getUnallocatedStrPoints();
        physicalHarm = request.getPhysicalHarm();
        armorPiercing = request.getArmorPiercing();
        reduceBlockDam = request.getReduceBlockDam();
        maxHealth = request.getMaxHealth();

        unallocatedAgiPoints = request.getUnallocatedAgiPoints();

        critChance = request.getCritChance();
        attackSpeed = request.getAttackSpeed();
        avoidance = request.getAvoidance();
        blockChance = request.getBlockChance();

        unallocatedIntePoints = request.getUnallocatedIntePoints();

        magicDam = request.getMagicDam();
        magicCritChance = request.getMagicCritChance();
        manaRegen = request.getManaRegen();
        maxMana = request.getMaxMana();
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));


        Table rootTable = new Table();
        rootTable.setFillParent(true);

        Table table1 = new Table();
        table1.left();
        rootTable.add(table1).expand();
        Table table2 = new Table();
        table2.right();
        rootTable.add(table2).expand();


        Label pointsLabel = new Label("Main Points ", skin);
        table1.add(pointsLabel).pad(10);
        pointsCountLabel = new Label(String.valueOf(pointsMain), skin);
        table1.add(pointsCountLabel).pad(10);
        table1.row();
//str
        Label strLabel = new Label("Str ", skin);
        table1.add(strLabel).pad(10);
        Label strengthLabel = new Label(String.valueOf(request.getStr()), skin); // Set initial value from request
        table1.add(strengthLabel).pad(10);
        TextButton strengthButton = new TextButton("+", skin);
        table1.add(strengthButton).pad(10);
        table1.row();
//agi
        Label agiLabel = new Label("Agi", skin);
        table1.add(agiLabel).pad(10);
        Label agilityLabel = new Label(String.valueOf(request.getAgi()), skin); // Set initial value from request
        table1.add(agilityLabel).pad(10).left();
        TextButton agiButton = new TextButton("+", skin);
        table1.add(agiButton).pad(10);
        table1.row();
//int
        Label inteLabel = new Label("Int", skin);
        table1.add(inteLabel).pad(10);
        Label intelligenceLabel = new Label(String.valueOf(request.getInte()), skin); // Set initial value from request
        table1.add(intelligenceLabel).pad(10);
        TextButton inteButton = new TextButton("+", skin);
        table1.add(inteButton).pad(10);
        table1.row();

// Unallocated Strength Points
        Label unallocatedStrLabel = new Label("Unallocated Str Points", skin);
        table1.add(unallocatedStrLabel).pad(10);
        Label unallocatedStrPointsLabel = new Label(String.valueOf(request.getUnallocatedStrPoints()), skin); // Set initial value from request
        table1.add(unallocatedStrPointsLabel).pad(10);
        table1.row();

// Physical Harm
        Label physicalHarmLabel = new Label("Physical Harm", skin);
        table1.add(physicalHarmLabel).pad(10);
        Label physicalHarmValueLabel = new Label(String.valueOf(request.getPhysicalHarm()), skin); // Set initial value from request
        table1.add(physicalHarmValueLabel).pad(10);
        TextButton physicalHarmButton = new TextButton("+", skin);
        table1.add(physicalHarmButton).pad(10);
        table1.row();

// Armor Piercing
        Label armorPiercingLabel = new Label("Armor Piercing", skin);
        table1.add(armorPiercingLabel).pad(10);
        Label armorPiercingValueLabel = new Label(String.valueOf(request.getArmorPiercing()), skin); // Set initial value from request
        table1.add(armorPiercingValueLabel).pad(10);
        TextButton armorPiercingButton = new TextButton("+", skin);
        table1.add(armorPiercingButton).pad(10);
        table1.row();

// Reduce Block Damage
        Label reduceBlockDamLabel = new Label("Reduce Block Damage", skin);
        table1.add(reduceBlockDamLabel).pad(10);
        Label reduceBlockDamValueLabel = new Label(String.valueOf(request.getReduceBlockDam()), skin); // Set initial value from request
        table1.add(reduceBlockDamValueLabel).pad(10);
        TextButton reduceBlockDamButton = new TextButton("+", skin);
        table1.add(reduceBlockDamButton).pad(10);
        table1.row();

// Max Health
        Label maxHealthLabel = new Label("Max Health", skin);
        table1.add(maxHealthLabel).pad(10);
        Label maxHealthValueLabel = new Label(String.valueOf(request.getMaxHealth()), skin); // Set initial value from request
        table1.add(maxHealthValueLabel).pad(10);
        TextButton maxHealthButton = new TextButton("+", skin);
        table1.add(maxHealthButton).pad(10);
        table1.row();

// Unallocated Agility Points
        Label unallocatedAgiLabel = new Label("Unallocated Agi Points", skin);
        table2.add(unallocatedAgiLabel).pad(10);
        Label unallocatedAgiPointsLabel = new Label(String.valueOf(request.getUnallocatedAgiPoints()), skin); // Set initial value from request
        table2.add(unallocatedAgiPointsLabel).pad(10);
        table2.row();

// Crit Chance
        Label critChanceLabel = new Label("Crit Chance", skin);
        table2.add(critChanceLabel).pad(10);
        Label critChanceValueLabel = new Label(String.valueOf(request.getCritChance()), skin); // Set initial value from request
        table2.add(critChanceValueLabel).pad(10);
        TextButton critChanceButton = new TextButton("+", skin);
        table2.add(critChanceButton).pad(10);
        table2.row();

// Attack Speed
        Label attackSpeedLabel = new Label("Attack Speed", skin);
        table2.add(attackSpeedLabel).pad(10);
        Label attackSpeedValueLabel = new Label(String.valueOf(request.getAttackSpeed()), skin); // Set initial value from request
        table2.add(attackSpeedValueLabel).pad(10);
        TextButton attackSpeedButton = new TextButton("+", skin);
        table2.add(attackSpeedButton).pad(10);
        table2.row();

// Avoidance
        Label avoidanceLabel = new Label("Avoidance", skin);
        table2.add(avoidanceLabel).pad(10);
        Label avoidanceValueLabel = new Label(String.valueOf(request.getAvoidance()), skin); // Set initial value from request
        table2.add(avoidanceValueLabel).pad(10);
        TextButton avoidanceButton = new TextButton("+", skin);
        table2.add(avoidanceButton).pad(10);
        table2.row();

// Block Chance
        Label blockChanceLabel = new Label("Block Chance", skin);
        table2.add(blockChanceLabel).pad(10);
        Label blockChanceValueLabel = new Label(String.valueOf(request.getBlockChance()), skin); // Set initial value from request
        table2.add(blockChanceValueLabel).pad(10);
        TextButton blockChanceButton = new TextButton("+", skin);
        table2.add(blockChanceButton).pad(10);
        table2.row();

// Unallocated Intelligence Points
        Label unallocatedInteLabel = new Label("Unallocated Inte Points", skin);
        table2.add(unallocatedInteLabel).pad(10);
        Label unallocatedIntePointsLabel = new Label(String.valueOf(request.getUnallocatedIntePoints()), skin); // Set initial value from request
        table2.add(unallocatedIntePointsLabel).pad(10);
        table2.row();

// Magic Damage
        Label magicDamLabel = new Label("Magic Damage", skin);
        table2.add(magicDamLabel).pad(10);
        Label magicDamValueLabel = new Label(String.valueOf(request.getMagicDam()), skin); // Set initial value from request
        table2.add(magicDamValueLabel).pad(10);
        TextButton magicDamButton = new TextButton("+", skin);
        table2.add(magicDamButton).pad(10);
        table2.row();

// Magic Crit Chance
        Label magicCritChanceLabel = new Label("Magic Crit Chance", skin);
        table2.add(magicCritChanceLabel).pad(10);
        Label magicCritChanceValueLabel = new Label(String.valueOf(request.getMagicCritChance()), skin); // Set initial value from request
        table2.add(magicCritChanceValueLabel).pad(10);
        TextButton magicCritChanceButton = new TextButton("+", skin);
        table2.add(magicCritChanceButton).pad(10);
        table2.row();

// Mana Regen
        Label manaRegenLabel = new Label("Mana Regen", skin);
        table2.add(manaRegenLabel).pad(10);
        Label manaRegenValueLabel = new Label(String.valueOf(request.getManaRegen()), skin); // Set initial value from request
        table2.add(manaRegenValueLabel).pad(10);
        TextButton manaRegenButton = new TextButton("+", skin);
        table2.add(manaRegenButton).pad(10);
        table2.row();

// Max Mana
        Label maxManaLabel = new Label("Max Mana", skin);
        table2.add(maxManaLabel).pad(10);
        Label maxManaValueLabel = new Label(String.valueOf(request.getMaxMana()), skin); // Set initial value from request
        table2.add(maxManaValueLabel).pad(10);
        TextButton maxManaButton = new TextButton("+", skin);
        table2.add(maxManaButton).pad(10);
        table2.row();


        TextButton applyButton = new TextButton("Apply", skin);
        table1.add(applyButton).size(150,50).pad(10);

        applyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //
            }
        });

        strengthButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (pointsMain > 0) {
                    pointsMain--;
                    pointsCountLabel.setText(pointsMain);
                    str++;
                    strengthLabel.setText(str);
                }

            }
        });

        agiButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (pointsMain > 0) {
                    pointsMain--;
                    pointsCountLabel.setText(pointsMain);
                    agi++;
                    agilityLabel.setText(agi);
                }
            }
        });

        inteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (pointsMain > 0) {
                    pointsMain--;
                    pointsCountLabel.setText(pointsMain);
                    inte++;
                    intelligenceLabel.setText(inte);
                }
            }
        });


// Physical Harm Button Listener
        physicalHarmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (unallocatedStrPoints > 0) {
                    physicalHarm++;
                    unallocatedStrPoints--;
                    unallocatedStrPointsLabel.setText(unallocatedStrPoints);
                    physicalHarmValueLabel.setText(physicalHarm);
                }
            }
        });

// Armor Piercing Button Listener
        armorPiercingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (unallocatedStrPoints > 0) {
                    armorPiercing++;
                    unallocatedStrPoints--;
                    unallocatedStrPointsLabel.setText(unallocatedStrPoints);
                    armorPiercingValueLabel.setText(armorPiercing);
                }
            }
        });

// Reduce Block Damage Button Listener
        reduceBlockDamButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (unallocatedStrPoints > 0) {
                    reduceBlockDam++;
                    unallocatedStrPoints--;
                    unallocatedStrPointsLabel.setText(unallocatedStrPoints);
                    reduceBlockDamValueLabel.setText(reduceBlockDam);
                }
            }
        });

// Max Health Button Listener
        maxHealthButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (unallocatedStrPoints > 0) {
                    maxHealth++;
                    unallocatedStrPoints--;
                    unallocatedStrPointsLabel.setText(unallocatedStrPoints);
                    maxHealthValueLabel.setText(maxHealth);
                }
            }
        });


// Crit Chance Button Listener
        critChanceButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (unallocatedAgiPoints > 0) {
                    critChance++;
                    unallocatedAgiPoints--;
                    critChanceValueLabel.setText(critChance);
                    unallocatedAgiPointsLabel.setText(unallocatedAgiPoints);
                }
            }
        });

// Attack Speed Button Listener
        attackSpeedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (unallocatedAgiPoints > 0) {
                    attackSpeed++;
                    unallocatedAgiPoints--;
                    attackSpeedValueLabel.setText(attackSpeed);
                    unallocatedAgiPointsLabel.setText(unallocatedAgiPoints);
                }
            }
        });

// Avoidance Button Listener
        avoidanceButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (unallocatedAgiPoints > 0) {
                    avoidance++;
                    unallocatedAgiPoints--;
                    avoidanceValueLabel.setText(avoidance);
                    unallocatedAgiPointsLabel.setText(unallocatedAgiPoints);
                }
            }
        });

// Block Chance Button Listener
        blockChanceButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (unallocatedAgiPoints > 0) {
                    blockChance++;
                    unallocatedAgiPoints--;
                    blockChanceValueLabel.setText(blockChance);
                    unallocatedAgiPointsLabel.setText(unallocatedAgiPoints);
                }
            }
        });

// Magic Damage Button Listener
        magicDamButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (unallocatedIntePoints > 0) {
                    magicDam++;
                    unallocatedIntePoints--;
                    magicDamValueLabel.setText(magicDam);
                    unallocatedIntePointsLabel.setText(unallocatedIntePoints);
                }
            }
        });

// Magic Crit Chance Button Listener
        magicCritChanceButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (unallocatedIntePoints > 0) {
                    magicCritChance++;
                    unallocatedIntePoints--;
                    magicCritChanceValueLabel.setText(magicCritChance);
                    unallocatedIntePointsLabel.setText(unallocatedIntePoints);
                }
            }
        });

// Mana Regen Button Listener
        manaRegenButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (unallocatedIntePoints > 0) {
                    manaRegen++;
                    unallocatedIntePoints--;
                    manaRegenValueLabel.setText(manaRegen);
                    unallocatedIntePointsLabel.setText(unallocatedIntePoints);
                }
            }
        });

// Max Mana Button Listener
        maxManaButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (unallocatedIntePoints > 0) {
                    maxMana++;
                    unallocatedIntePoints--;
                    maxManaValueLabel.setText(maxMana);
                    unallocatedIntePointsLabel.setText(unallocatedIntePoints);
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

        stage.addActor(rootTable);
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
