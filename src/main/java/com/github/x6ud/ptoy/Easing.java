package com.github.x6ud.ptoy;

public class Easing {

    // https://gist.github.com/gre/1650294

    // no easing, no acceleration
    public static float linear(float t) {
        return t;
    }

    // accelerating from zero velocity
    public static float easeInQuad(float t) {
        return t * t;
    }

    // decelerating to zero velocity
    public static float easeOutQuad(float t) {
        return t * (2f - t);
    }

    // acceleration until halfway, then deceleration
    public static float easeInOutQuad(float t) {
        return t < .5f ? 2f * t * t : -1f + (4f - 2f * t) * t;
    }

    // accelerating from zero velocity
    public static float easeInCubic(float t) {
        return t * t * t;
    }

    // decelerating to zero velocity
    public static float easeOutCubic(float t) {
        return (--t) * t * t + 1f;
    }

    // acceleration until halfway, then deceleration
    public static float easeInOutCubic(float t) {
        return t < .5f ? 4f * t * t * t : (t - 1f) * (2f * t - 2f) * (2f * t - 2f) + 1f;
    }

    // accelerating from zero velocity
    public static float easeInQuart(float t) {
        return t * t * t * t;
    }

    // decelerating to zero velocity
    public static float easeOutQuart(float t) {
        return 1f - (--t) * t * t * t;
    }

    // acceleration until halfway, then deceleration
    public static float easeInOutQuart(float t) {
        return t < .5f ? 8f * t * t * t * t : 1f - 8f * (--t) * t * t * t;
    }

    // accelerating from zero velocity
    public static float easeInQuint(float t) {
        return t * t * t * t * t;
    }

    // decelerating to zero velocity
    public static float easeOutQuint(float t) {
        return 1f + (--t) * t * t * t * t;
    }

    // acceleration until halfway, then deceleration
    public static float easeInOutQuint(float t) {
        return t < .5f ? 16f * t * t * t * t * t : 1f + 16f * (--t) * t * t * t * t;
    }

}
