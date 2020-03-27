package com.github.x6ud.ptoy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.github.x6ud.ptoy.input.KeyMap;
import com.github.x6ud.ptoy.physics.Physics;

public class Game extends ApplicationAdapter {

    private SpriteBatch batch;
    private Box2DDebugRenderer box2DDebugRenderer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        box2DDebugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float deltaSecs = Gdx.graphics.getDeltaTime();

        KeyMap.update(deltaSecs);
        Physics.world.step(deltaSecs, 6, 3);

        batch.begin();
        if (Director.stage != null) {
            Director.stage.update(deltaSecs);

            Matrix4 originalMatrix = batch.getProjectionMatrix().cpy();
            batch.setProjectionMatrix(Director.camera.combined);
            Director.stage.draw(batch);
            batch.setProjectionMatrix(originalMatrix);
        }
        batch.end();

        if (Global.RENDER_DEBUG) {
            box2DDebugRenderer.render(Physics.world, Director.camera.combined);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        box2DDebugRenderer.dispose();
    }

}
