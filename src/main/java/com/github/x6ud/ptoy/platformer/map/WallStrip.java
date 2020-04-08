package com.github.x6ud.ptoy.platformer.map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.github.x6ud.ptoy.physics.Physics;
import com.github.x6ud.ptoy.sprite.BaseSprite;

public class WallStrip extends BaseSprite {

    private Body body;

    public WallStrip(Vector2 from, Vector2 to, float thickness) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        body = Physics.world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        Vector2 tangent = to.cpy().sub(from).rotate90(1).nor();
        Vector2 p1 = from.cpy().mulAdd(tangent, thickness / 2);
        Vector2 p2 = from.cpy().mulAdd(tangent, -thickness / 2);
        Vector2 p3 = to.cpy().mulAdd(tangent, -thickness / 2);
        Vector2 p4 = to.cpy().mulAdd(tangent, thickness / 2);
        shape.set(new Vector2[]{p1, p2, p3, p4});

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.restitution = 0;
        fixtureDef.friction = 1;
        fixtureDef.density = 10;
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);
    }

    @Override
    public void onRemoved() {
        Physics.world.destroyBody(body);
    }

}
