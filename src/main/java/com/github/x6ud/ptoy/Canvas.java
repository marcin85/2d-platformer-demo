package com.github.x6ud.ptoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import java.util.Stack;

public final class Canvas {

    public static final SpriteBatch batch = new SpriteBatch();

    private static final Stack<FrameBuffer> bufferStack = new Stack<FrameBuffer>();
    private static FrameBuffer currentBuffer;

    public static void beginCapture(FrameBuffer frameBuffer) {
        bufferStack.push(frameBuffer);
    }

    public static void endCapture() {
        bufferStack.pop();
        if (batch.isDrawing()) {
            batch.end();
        }
        if (currentBuffer != null) {
            currentBuffer.end();
            currentBuffer = null;
        }
    }

    public static void begin() {
        if (!bufferStack.empty() && currentBuffer == null) {
            if (batch.isDrawing()) {
                batch.end();
            }

            currentBuffer = bufferStack.peek();
            currentBuffer.begin();
            batch.begin();
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }
        if (!batch.isDrawing()) {
            batch.begin();
        }
    }

    public static void end() {
    }

}
