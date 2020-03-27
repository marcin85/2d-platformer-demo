package com.github.x6ud.ptoy;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Global.VIEWPORT_WIDTH;
        config.height = Global.VIEWPORT_HEIGHT;
        config.resizable = false;
        new LwjglApplication(new Game(), config);
    }

}
