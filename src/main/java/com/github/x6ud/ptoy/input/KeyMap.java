package com.github.x6ud.ptoy.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public final class KeyMap {

    public static boolean isLeftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.LEFT);
    }

    public static boolean isRightPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.RIGHT);
    }

    public static boolean isJumpPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.SPACE)
                || Gdx.input.isKeyPressed(Input.Keys.Z);
    }

    public static boolean isDashPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.X);
    }

}
