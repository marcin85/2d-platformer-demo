package com.github.x6ud.ptoy.platformer.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.github.x6ud.ptoy.physics.BodyContactListener;
import com.github.x6ud.ptoy.physics.Physics;
import com.github.x6ud.ptoy.sprite.BaseSprite;

import java.util.HashSet;

public class WallOneWay extends BaseSprite implements BodyContactListener {

    private Body body;
    private Vector2 passableDirection;
    private HashSet<Contact> passThroughContacts = new HashSet<Contact>();

    public WallOneWay(float x0, float y0, float x1, float y1) {
        passableDirection = new Vector2(x1 - x0, y1 - y0).rotate90(1);

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

    public void onRemoved() {
        Physics.contact.removeListener(body);
        Physics.world.destroyBody(body);
    }

    public void onContactBegin(Fixture self, Fixture other, Vector2 normal, Contact contact) {
        if (normal.dot(passableDirection) < 0) {
            passThroughContacts.add(contact);
        }
    }

    public void onContactEnd(Fixture self, Fixture other, Contact contact) {
        passThroughContacts.remove(contact);
        contact.setEnabled(true);
    }

    public void onPreSolve(Fixture self, Fixture other, Vector2 normal, Contact contact, Manifold oldManifold) {
        if (passThroughContacts.contains(contact)) {
            contact.setEnabled(false);
        }
    }

    public void onPostSolve(Fixture self, Fixture other, Vector2 normal, Contact contact, ContactImpulse impulse) {
    }

    public void onUpdate(float secs) {
    }

    public void onRender(Batch batch) {
    }

}
