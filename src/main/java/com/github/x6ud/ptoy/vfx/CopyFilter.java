package com.github.x6ud.ptoy.vfx;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.github.x6ud.ptoy.Canvas;
import com.github.x6ud.ptoy.Config;
import com.github.x6ud.ptoy.utils.ResourceUtils;

public class CopyFilter extends VfxFilter {

    private static ShaderProgram shader = ResourceUtils.compileShader("shaders/full.vert", "shaders/copy.frag");

    public CopyFilter() {
        this(Config.VIEWPORT_WIDTH, Config.VIEWPORT_HEIGHT);
    }

    public CopyFilter(int width, int height) {
        super(new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false));
    }

    protected void applyFilter(TextureRegion textureRegion) {
        Canvas.batch.setShader(shader);
        Canvas.batch.draw(textureRegion, -1, -1, 2, 2);
        Canvas.batch.setShader(null);
    }

}
