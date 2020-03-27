package com.github.x6ud.ptoy.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.HashMap;

public class ContactObserver {

    private HashMap<Body, BodyContactListener> listenerMap = new HashMap<Body, BodyContactListener>();

    private static final int BEGIN_CONTACT = 0;
    private static final int END_CONTACT = 1;
    private static final int PRE_SOLVE = 2;
    private static final int POST_SOLVE = 3;

    public ContactObserver(World world) {
        world.setContactListener(new ContactListener() {
            public void beginContact(Contact contact) {
                notifyContact(BEGIN_CONTACT, contact, null, null);
            }

            public void endContact(Contact contact) {
                notifyContact(END_CONTACT, contact, null, null);
            }

            public void preSolve(Contact contact, Manifold oldManifold) {
                notifyContact(PRE_SOLVE, contact, oldManifold, null);
            }

            public void postSolve(Contact contact, ContactImpulse impulse) {
                notifyContact(POST_SOLVE, contact, null, impulse);
            }
        });
    }

    private void notifyContact(int type, Contact contact, Manifold oldManifold, ContactImpulse impulse) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        Body bodyA = fixtureA.getBody();
        Body bodyB = fixtureB.getBody();
        Vector2 normal = contact.getWorldManifold().getNormal();

        if (listenerMap.containsKey(bodyA)) {
            BodyContactListener listener = listenerMap.get(bodyA);
            switch (type) {
                case BEGIN_CONTACT:
                    listener.onContactBegin(fixtureA, fixtureB, normal, contact);
                    break;
                case END_CONTACT:
                    listener.onContactEnd(fixtureA, fixtureB, contact);
                    break;
                case PRE_SOLVE:
                    listener.onPreSolve(fixtureA, fixtureB, normal, contact, oldManifold);
                    break;
                case POST_SOLVE:
                    listener.onPostSolve(fixtureA, fixtureB, normal, contact, impulse);
                    break;
            }
        }
        if (listenerMap.containsKey(bodyB)) {
            BodyContactListener listener = listenerMap.get(bodyB);

            switch (type) {
                case BEGIN_CONTACT:
                    listener.onContactBegin(fixtureB, fixtureA, new Vector2(-normal.x, -normal.y), contact);
                    break;
                case END_CONTACT:
                    listener.onContactEnd(fixtureB, fixtureA, contact);
                    break;
                case PRE_SOLVE:
                    listener.onPreSolve(fixtureB, fixtureA, new Vector2(-normal.x, -normal.y), contact, oldManifold);
                    break;
                case POST_SOLVE:
                    listener.onPostSolve(fixtureB, fixtureA, new Vector2(-normal.x, -normal.y), contact, impulse);
                    break;
            }
        }
    }

    public void addListener(Body body, BodyContactListener listener) {
        if (listenerMap.containsKey(body)) {
            throw new RuntimeException("Body has already added a contact listener.");
        }
        listenerMap.put(body, listener);
    }

    public void removeListener(Body body) {
        listenerMap.remove(body);
    }

}
