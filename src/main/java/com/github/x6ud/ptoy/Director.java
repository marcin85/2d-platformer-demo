package com.github.x6ud.ptoy;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.github.x6ud.ptoy.sprite.BaseSprite;
import com.github.x6ud.ptoy.vfx.CopyFilter;
import com.github.x6ud.ptoy.vfx.Downscale3xFilter;
import com.github.x6ud.testcase.Testcase1;

public final class Director {

    public static final OrthographicCamera camera = new OrthographicCamera(
            Config.VIEWPORT_WIDTH / Config.PIXELS_PER_METER,
            Config.VIEWPORT_HEIGHT / Config.PIXELS_PER_METER
    );

    public static final BaseSprite stage = BaseSprite.chain(
            new CopyFilter(), // 2x magnification
            new Downscale3xFilter(), // rot-sprite
            new Testcase1()
    );

}
