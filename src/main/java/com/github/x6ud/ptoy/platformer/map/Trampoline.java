package com.github.x6ud.ptoy.platformer.map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.github.x6ud.ptoy.physics.BodyContactListener;
import com.github.x6ud.ptoy.physics.Physics;
import com.github.x6ud.ptoy.sprite.BaseSprite;

public class Trampoline extends BaseSprite implements BodyContactListener {

    private Body body;
    private Vector2 direction;
    private float speed;
    private float coefficient;

    public Trampoline(float x0, float y0, float x1, float y1, float bounceCoefficient, float bounceSpeed) {
        direction = new Vector2(x1 - x0, y1 - y0).rotate90(1).nor();
        coefficient = bounceCoefficient;
        speed = bounceSpeed;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        body = Physics.world.createBody(bodyDef);
        EdgeShape shape = new EdgeShape();
        shape.set(x0, y0, x1, y1);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.restitution = 0;
        fixtureDef.friction = 1;
        fixtureDef.density = 10;
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);
        Physics.contact.addListener(body, this);
    }

    @Override
    public void onRemoved() {
        Physics.contact.removeListener(body);
        Physics.world.destroyBody(body);
    }

    public void onContactBegin(Fixture self, Fixture other, Vector2 normal, Contact contact) {
        if (other.getBody().getType() == BodyDef.BodyType.DynamicBody) {
            Vector2 velocity = other.getBody().getLinearVelocity();
            float v0 = velocity.dot(direction);
            if (v0 < 0) {
                velocity.mulAdd(direction, speed - v0 * (1 + coefficient));
                other.getBody().setLinearVelocity(velocity);
            }
        }
    }

    public void onContactEnd(Fixture self, Fixture other, Contact contact) {
    }

    public void onPreSolve(Fixture self, Fixture other, Vector2 normal, Contact contact, Manifold oldManifold) {
    }

    public void onPostSolve(Fixture self, Fixture other, Vector2 normal, Contact contact, ContactImpulse impulse) {
    }

}
