package com.github.x6ud.ptoy.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public interface BodyContactListener {

    void onContactBegin(Fixture self, Fixture other, Vector2 normal, Contact contact);

    void onContactEnd(Fixture self, Fixture other, Contact contact);

    void onPreSolve(Fixture self, Fixture other, Vector2 normal, Contact contact, Manifold oldManifold);

    void onPostSolve(Fixture self, Fixture other, Vector2 normal, Contact contact, ContactImpulse impulse);

}
