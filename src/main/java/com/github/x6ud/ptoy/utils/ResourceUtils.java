package com.github.x6ud.ptoy.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public final class ResourceUtils {

    public static ShaderProgram compileShader(String vertPath, String fragPath) {
        String vertSrc = Gdx.files.internal(vertPath).readString();
        String fragSrc = Gdx.files.internal(fragPath).readString();
        ShaderProgram.pedantic = false;
        ShaderProgram shader = new ShaderProgram(vertSrc, fragSrc);
        if (!shader.isCompiled()) {
            throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        }
        return shader;
    }

    public static Texture loadTexture(String path) {
        Texture texture = new Texture(Gdx.files.internal(path));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        return texture;
    }

}
