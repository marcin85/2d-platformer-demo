package com.github.x6ud.ptoy.vfx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.github.x6ud.ptoy.Canvas;
import com.github.x6ud.ptoy.sprite.BaseSprite;

public abstract class VfxFilter extends BaseSprite {

    private FrameBuffer frameBuffer;
    private TextureRegion textureRegion;

    protected VfxFilter(FrameBuffer frameBuffer, Texture.TextureFilter minFilter, Texture.TextureFilter magFilter) {
        this.frameBuffer = frameBuffer;
        Texture texture = frameBuffer.getColorBufferTexture();
        texture.setFilter(minFilter, magFilter);
        textureRegion = new TextureRegion(texture);
        textureRegion.flip(false, true);
    }

    protected VfxFilter(FrameBuffer frameBuffer) {
        this(frameBuffer, Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    @Override
    public final void draw() {
        Canvas.beginCapture(frameBuffer);
        super.draw();
        Canvas.endCapture();

        Canvas.begin();
        applyFilter(textureRegion);
        Canvas.end();
    }

    protected abstract void applyFilter(TextureRegion textureRegion);

}
