package com.github.x6ud.ptoy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.github.x6ud.ptoy.physics.Physics;

public class Game extends ApplicationAdapter {

    private Box2DDebugRenderer box2DDebugRenderer;

    @Override
    public void create() {
        box2DDebugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float deltaSecs = Gdx.graphics.getDeltaTime();

        Physics.world.step(deltaSecs, 6, 3);
        Director.stage.update(deltaSecs);

        Matrix4 originalMatrix = Canvas.batch.getProjectionMatrix().cpy();
        Canvas.batch.setProjectionMatrix(Director.camera.combined);
        Canvas.batch.begin();
        Director.stage.draw();
        if (Canvas.batch.isDrawing()) {
            Canvas.batch.end();
        }
        Canvas.batch.setProjectionMatrix(originalMatrix);

        if (Config.RENDER_DEBUG) {
            box2DDebugRenderer.render(Physics.world, Director.camera.combined);
        }
    }

    @Override
    public void dispose() {
        box2DDebugRenderer.dispose();
    }

}
