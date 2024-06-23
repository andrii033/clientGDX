package com.ta.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Character {
    private Texture texture;
    private Vector2 position;
    private float speed;

    public Character(Texture texture, Vector2 position, float speed) {
        this.texture = texture;
        this.position = position;
        this.speed = speed;
    }

    public void update(float deltaTime, Vector2 direction) {
        if (direction.len() > 0) {
            position.add(direction.scl(speed * deltaTime));
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public Vector2 getPosition() {
        return position;
    }
}
