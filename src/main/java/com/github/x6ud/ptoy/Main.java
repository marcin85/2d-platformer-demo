package com.github.x6ud.ptoy;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Config.WINDOW_WIDTH;
        config.height = Config.WINDOW_HEIGHT;
        config.resizable = false;
        new LwjglApplication(new Game(), config);
    }

}
