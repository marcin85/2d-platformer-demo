package com.github.x6ud.testcase;

import com.badlogic.gdx.math.Vector2;
import com.github.x6ud.ptoy.Director;
import com.github.x6ud.ptoy.platformer.map.Trampoline;
import com.github.x6ud.ptoy.platformer.map.WallOneWay;
import com.github.x6ud.ptoy.platformer.map.WallStrip;
import com.github.x6ud.ptoy.platformer.player.Player;
import com.github.x6ud.ptoy.sprite.BaseSprite;

public class Testcase1 extends BaseSprite {

    private Player player = new Player();

    public Testcase1() {
        this.addToTop(player);
        player.setPosition(-1, 1);

        Vector2[] list = new Vector2[]{
                new Vector2(1, 0).rotate(0),
                new Vector2(1, 0).rotate(30),
                new Vector2(1, 0).rotate(45),
                new Vector2(1, 0).rotate(60),
                new Vector2(1, 0).rotate(90)
        };
        Vector2 v0 = new Vector2(-5, 0);
        for (Vector2 dv : list) {
            Vector2 v1 = v0.cpy().mulAdd(dv, 10);
            this.addToBottom(new WallStrip(v0, v1, 0.5f));
            v0 = v1;
        }

        this.addToBottom(new WallStrip(new Vector2(-5, 0), new Vector2(-5, 50), 0.5f));

        this.addToBottom(new WallOneWay(-3, 3, 0, 3));
        this.addToBottom(new Trampoline(3, 3, 6, 3, 0.5f, 5.0f));
    }

    @Override
    public void onUpdate(float secs) {
        Vector2 from = new Vector2(Director.camera.position.x, Director.camera.position.y);
        Vector2 delta = player.getPosition().cpy().sub(from);
        Director.camera.position.set(from.mulAdd(delta, Math.min(1, secs / 0.05f)), 0);
        Director.camera.update();
    }

}
