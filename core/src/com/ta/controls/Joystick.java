package com.ta.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Joystick {
    private Texture baseTexture;
    private Texture knobTexture;
    private Vector2 position;
    private Vector2 knobPosition;
    private Vector2 initialKnobPosition;
    private float radius;
    private boolean isTouched;

    public Joystick(Texture baseTexture, Texture knobTexture, float radius) {
        this.baseTexture = baseTexture;
        this.knobTexture = knobTexture;
        this.radius = radius;
        this.isTouched = false;
        this.position = new Vector2();
        this.knobPosition = new Vector2();
        this.initialKnobPosition = new Vector2();
    }

    public void setPosition(Vector2 position) {
        this.position.set(position);
        this.knobPosition.set(position);
        this.initialKnobPosition.set(position);
    }

    public void update() {
        if (Gdx.input.isTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            if (isTouched || position.dst(touchPos) < radius) {
                isTouched = true;
                knobPosition.set(touchPos);
                if (knobPosition.dst(position) > radius) {
                    knobPosition.sub(position).nor().scl(radius).add(position);
                }
            }
        } else {
            isTouched = false;
            knobPosition.set(initialKnobPosition);
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(baseTexture, position.x - baseTexture.getWidth() / 2, position.y - baseTexture.getHeight() / 2);
        batch.draw(knobTexture, knobPosition.x - knobTexture.getWidth() / 2, knobPosition.y - knobTexture.getHeight() / 2);
    }

    public Vector2 getDirection() {
        return new Vector2(knobPosition).sub(position).nor();
    }

    public boolean isTouched() {
        return isTouched;
    }
}
