package com.github.x6ud.ptoy.vfx;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.github.x6ud.ptoy.Canvas;
import com.github.x6ud.ptoy.Config;
import com.github.x6ud.ptoy.utils.ResourceUtils;

public class Downscale3xFilter extends VfxFilter {

    private static ShaderProgram shader = ResourceUtils.compileShader("shaders/full.vert", "shaders/downscale3x.frag");

    public Downscale3xFilter() {
        super(new FrameBuffer(
                Pixmap.Format.RGBA8888,
                Config.VIEWPORT_WIDTH * 3,
                Config.VIEWPORT_HEIGHT * 3,
                false
        ));
    }

    protected void applyFilter(TextureRegion textureRegion) {
        Canvas.batch.setShader(shader);
        shader.setUniformi("u_inputSize", textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        Canvas.batch.draw(textureRegion, -1, -1, 2, 2);
        Canvas.batch.setShader(null);
    }

}
