package com.github.x6ud.ptoy.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.github.x6ud.ptoy.Config;

public final class Physics {

    public static final World world = new World(new Vector2(0, -Config.GRAVITY), true);

    public static final ContactObserver contact = new ContactObserver(world);

}
