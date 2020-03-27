package com.github.x6ud.ptoy;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.github.x6ud.ptoy.sprite.BaseSprite;
import com.github.x6ud.ptoy.testcase.Testcase1;

public final class Director {

    public static final OrthographicCamera camera = new OrthographicCamera(
            Global.VIEWPORT_WIDTH / Global.PIXELS_PER_METER,
            Global.VIEWPORT_HEIGHT / Global.PIXELS_PER_METER
    );

    public static BaseSprite stage = new Testcase1();

}
