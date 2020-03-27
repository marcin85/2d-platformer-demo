package com.github.x6ud.ptoy.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public final class KeyMap {

    private static final DoublePressDetector leftDoublePressDetector = new DoublePressDetector();
    private static final DoublePressDetector rightDoublePressDetector = new DoublePressDetector();

    public static void update(float secs) {
        leftDoublePressDetector.update(isLeftPressed(), secs);
        rightDoublePressDetector.update(isRightPressed(), secs);
    }

    // ===================================================

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

    public static boolean isLeftDoublePressed() {
        return leftDoublePressDetector.isKeyDoublePressed();
    }

    public static boolean isRightDoublePressed() {
        return rightDoublePressDetector.isKeyDoublePressed();
    }

}
