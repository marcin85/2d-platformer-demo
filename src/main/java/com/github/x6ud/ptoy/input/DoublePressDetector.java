package com.github.x6ud.ptoy.input;

public class DoublePressDetector {

    private static final float PRESSED_INTERVAL_SECS = 0.2f;

    private int pressCount = 0;
    private boolean keyPressed = false;
    private float pressedDuration = 0;
    private float releaseDuration = 0;

    public void update(boolean keyPressedThisFrame, float secs) {
        if (keyPressedThisFrame) {
            if (!keyPressed) {
                keyPressed = true;
                pressedDuration = 0;
                releaseDuration = 0;
            } else {
                if (pressedDuration <= PRESSED_INTERVAL_SECS) {
                    pressedDuration += secs;
                }
            }
        } else {
            if (keyPressed) {
                keyPressed = false;
                releaseDuration = 0;
                if (pressedDuration <= PRESSED_INTERVAL_SECS) {
                    pressCount += 1;
                } else {
                    pressCount = 0;
                }
                pressedDuration = 0;
            } else {
                if (releaseDuration <= PRESSED_INTERVAL_SECS) {
                    releaseDuration += secs;
                }
                if (releaseDuration > PRESSED_INTERVAL_SECS) {
                    pressCount = 0;
                }
            }
        }
    }

    public boolean isKeyDoublePressed() {
        return !keyPressed && pressCount >= 2;
    }

}
