package com.github.x6ud.ptoy.platformer.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.github.x6ud.ptoy.Canvas;
import com.github.x6ud.ptoy.Config;
import com.github.x6ud.ptoy.enums.HorizontalDirection;
import com.github.x6ud.ptoy.utils.ResourceUtils;

class PlayerAnimation {

    private enum State {
        STAND(0),
        RUN(1),
        JUMP(2),
        FALL(3),
        LAND(4),
        DASH(5),
        CLIMB(6);

        private int index;

        State(int index) {
            this.index = index;
        }
    }

    private static final int FRAME_WIDTH = 32;
    private static final int FRAME_HEIGHT = 32;
    private static final float OFFSET_X = 4f / Config.PIXELS_PER_METER;
    private static final float OFFSET_Y = 4f / Config.PIXELS_PER_METER;

    private static final float ROTATION_TRANSITION_DURATION_SECS = 0.05f;
    private static final float RUNNING_SPEED_MIN_RATE = 0.5f;
    private static final float RUNNING_SPEED_MAX_RATE = 2.5f;

    private Texture texture;
    private Animation<TextureRegion>[] animations;

    private State state = State.STAND;
    private float stateTime;
    private HorizontalDirection direction;
    private float x;
    private float y;
    private float rotation;

    @SuppressWarnings("unchecked")
    PlayerAnimation() {
        texture = ResourceUtils.loadTexture("textures/sprites/player.png");

        animations = (Animation<TextureRegion>[]) new Animation[7];

        animations[State.STAND.index] = new Animation<TextureRegion>(
                0.7f,
                getFrame(0, 0),
                getFrame(1, 0)
        );
        animations[State.STAND.index].setPlayMode(Animation.PlayMode.LOOP);

        animations[State.RUN.index] = new Animation<TextureRegion>(
                0.06f,
                getFrame(0, 1),
                getFrame(1, 1),
                getFrame(2, 1),
                getFrame(3, 1),
                getFrame(4, 1),
                getFrame(5, 1)
        );
        animations[State.RUN.index].setPlayMode(Animation.PlayMode.LOOP);

        animations[State.JUMP.index] = new Animation<TextureRegion>(
                0.06f,
                getFrame(0, 2)
        );

        animations[State.FALL.index] = new Animation<TextureRegion>(
                0.1f,
                getFrame(0, 3),
                getFrame(1, 3),
                getFrame(2, 3),
                getFrame(3, 3),
                getFrame(4, 3)
        );

        animations[State.LAND.index] = new Animation<TextureRegion>(
                0.05f,
                getFrame(0, 4),
                getFrame(1, 4),
                getFrame(2, 4),
                getFrame(3, 4),
                getFrame(4, 4),
                getFrame(5, 4)
        );

        animations[State.DASH.index] = new Animation<TextureRegion>(
                1f,
                getFrame(0, 5)
        );

        animations[State.CLIMB.index] = new Animation<TextureRegion>(
                1f,
                getFrame(1, 5)
        );
    }

    private TextureRegion getFrame(int x, int y) {
        return new TextureRegion(texture, x * FRAME_WIDTH, y * FRAME_HEIGHT, FRAME_WIDTH, FRAME_HEIGHT);
    }

    void update(Player player, float secs) {
        Vector2 velocity = player.body.getLinearVelocity();
        boolean isOnGround = player.contactingGroundFixture != null;

        // state
        if (player.dashing) {
            pushState(State.DASH);
        } else if (
                player.climbingWallFixture != null
                        && player.contactingWallFixture == player.climbingWallFixture
                        && player.contactingGroundFixture == null
        ) {
            pushState(State.CLIMB);
        } else if (isOnGround) {
            float speed = player.moveNormal.dot(velocity);
            if (speed > 0.2f || player.moveKeyPressed) {
                pushState(State.RUN);
            } else {
                pushState(State.STAND);
            }
        } else {
            if (velocity.y > 0) {
                pushState(State.JUMP);
            } else {
                pushState(State.FALL);
            }
        }

        // dt
        float rate = 1.0f;
        if (state == State.RUN) {
            float speed = velocity.len();
            rate = Math.min(Math.max(speed / Player.MOVING_MAX_SPEED, RUNNING_SPEED_MIN_RATE), RUNNING_SPEED_MAX_RATE);
        }
        stateTime += secs * rate;

        HorizontalDirection facingDirection = player.lockFacingDirectionForAnimation ? player.lastWallJumpDirection : player.facingDirection;
        Vector2 moveNormal = player.lockFacingDirectionForAnimation ? new Vector2(facingDirection.value(), 0) : player.moveNormal;

        // direction
        boolean flipped = direction != facingDirection;
        direction = facingDirection;

        // rotation
        float newRotation = moveNormal.angle();
        if (state == State.JUMP && velocity.dot(new Vector2(facingDirection.value(), 0)) > 0) {
            float velocityAngle = velocity.angle();
            if (velocityAngle > 0 && velocityAngle < 180) {
                if (velocityAngle > 90) {
                    velocityAngle = Math.max(90 + 45, velocityAngle);
                } else {
                    velocityAngle = Math.min(45, velocityAngle);
                }
                if (velocityAngle < 90 && velocityAngle < rotation || velocityAngle > 90 && velocityAngle > rotation) {
                    newRotation = velocityAngle;
                }
            }
        }
        if (!flipped && (rotation < 90 && newRotation < 90 || rotation >= 90 && newRotation >= 90)) {
            rotation = rotation + (newRotation - rotation) * Math.min(1, secs / ROTATION_TRANSITION_DURATION_SECS);
        } else {
            rotation = newRotation;
        }

        // position
        Vector2 position = player.getPosition();
        x = position.x;
        y = position.y;
    }

    private void pushState(State newState) {
        if (state == State.FALL) {
            if (newState == State.STAND) {
                newState = State.LAND;
            }
        } else if (state == State.LAND && newState == State.STAND) {
            if (!animations[State.LAND.index].isAnimationFinished(stateTime)) {
                newState = State.LAND;
            }
        }
        if (state != newState) {
            state = newState;
            stateTime = 0;
        }
    }

    void draw() {
        Animation<TextureRegion> animation = animations[state.index];

        TextureRegion textureRegion = animation.getKeyFrame(stateTime);
        boolean flip = direction == HorizontalDirection.LEFT;

        Canvas.begin();
        Canvas.batch.draw(
                textureRegion.getTexture(),
                x - Player.BODY_RADIUS - OFFSET_X,
                y - Player.BODY_RADIUS - OFFSET_Y,
                Player.BODY_RADIUS + OFFSET_X,
                Player.BODY_RADIUS + OFFSET_Y,
                1,
                1,
                1,
                1,
                flip ? rotation - 180 : rotation,
                textureRegion.getRegionX(),
                textureRegion.getRegionY(),
                textureRegion.getRegionWidth(),
                textureRegion.getRegionHeight(),
                flip,
                false
        );
        Canvas.end();
    }

}
