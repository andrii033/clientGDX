package com.ta.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ta.ClientGDX;
import com.ta.game.Character;
import com.ta.controls.Joystick;

public class CharScreen extends InputAdapter implements Screen {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Character character;
    private Joystick joystick;
    private boolean joystickInitialized;

    public CharScreen(ClientGDX game, String token) {
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        // Load your character texture
        Texture characterTexture = new Texture("badlogic.jpg");
        Vector2 startPosition = new Vector2(400, 240);
        float speed = 100; // Adjust speed as necessary

        character = new Character(characterTexture, startPosition, speed);

        // Load joystick textures and create joystick
        Texture joystickBase = new Texture("joystick_base.png");
        Texture joystickKnob = new Texture("joystick_knob.png");
        float joystickRadius = 200; // Radius of the joystick

        joystick = new Joystick(joystickBase, joystickKnob, joystickRadius);
        joystickInitialized = false;

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        if (Gdx.input.justTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            // Check if the touch is in the left part of the screen
            if (touchPos.x < Gdx.graphics.getWidth() / 2) {
                joystick.setPosition(touchPos);
                joystickInitialized = true;
            }
        }

        if (joystickInitialized) {
            joystick.update();
            character.update(delta, joystick.getDirection());
        }

        batch.begin();
        character.render(batch);
        if (joystickInitialized) {
            joystick.render(batch);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
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
        batch.dispose();
    }
}
