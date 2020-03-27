package com.github.x6ud.ptoy.platformer.player;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.github.x6ud.ptoy.Easing;
import com.github.x6ud.ptoy.input.KeyMap;
import com.github.x6ud.ptoy.enums.HorizontalDirection;
import com.github.x6ud.ptoy.physics.BodyContactListener;
import com.github.x6ud.ptoy.physics.Physics;
import com.github.x6ud.ptoy.sprite.BaseSprite;

public class Player extends BaseSprite implements BodyContactListener {

    static final float EPSILON = 1E-4f;

    static final float BODY_RADIUS = 0.5f * (24f / 32f);
    static final float MAX_GROUND_SLOPE_DEG = 75f;

    static final float MOVING_MAX_SPEED = 12f;
    static final float MOVING_ACCELERATION = MOVING_MAX_SPEED * 5f;
    static final float MOVING_ACCELERATION_OFF_GROUND = MOVING_ACCELERATION * 0.3f;
    static final float FRICTION_DECELERATION = MOVING_ACCELERATION * 0.35f;

    static final float JUMPING_TAKEOFF_SPEED = MOVING_MAX_SPEED * 0.65f;
    static final float JUMPING_HOLD_RESIST_GRAVITY_SCALAR = 0.8f;
    static final float JUMPING_HOLD_MAX_SECS = 0.5f;

    static final float WALL_JUMP_ANGLE_DEG = 45f;
    static final float CLIMBING_RESIST_GRAVITY_SCALAR = 0.8f;

    static final float DASHING_TAKEOFF_SPEED = MOVING_MAX_SPEED * 2f;
    static final float DASHING_DURATION_SECS = 0.2f;
    static final float DASHING_SLOWDOWN_SPEED_SCALAR = 0.75f;
    static final float DASHING_COOL_DOWN_SECS = 0.5f;

    PlayerAnimation animation = new PlayerAnimation();

    Body body;
    Fixture bodyFixture;

    Fixture contactingGroundFixture;
    Vector2 contactingGroundNormal;
    Fixture contactingWallFixture;
    HorizontalDirection contactingWallDirection;

    boolean moveKeyPressed;
    Vector2 moveNormal;
    HorizontalDirection facingDirection = HorizontalDirection.RIGHT;

    boolean jumpPressed;
    float jumpingHoldSecs;
    boolean jumpJustPressed;
    Vector2 jumpTakeoffNormal;

    Fixture climbingWallFixture;
    boolean lockFacingDirectionForAnimation;
    HorizontalDirection lastWallJumpDirection;

    boolean dashPressed;
    boolean dashing;
    HorizontalDirection dashDirection;
    Vector2 dashNormal;
    float dashDuration;
    float dashCooling;

    public Player() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = Physics.world.createBody(bodyDef);
        body.setFixedRotation(true);

        FixtureDef bodyFixtureDef = new FixtureDef();
        CircleShape bodyShape = new CircleShape();
        bodyShape.setRadius(BODY_RADIUS);
        bodyFixtureDef.shape = bodyShape;
        bodyFixtureDef.restitution = 0;
        bodyFixtureDef.friction = 0;
        bodyFixtureDef.density = 1;
        bodyFixture = body.createFixture(bodyFixtureDef);

        Physics.contact.addListener(body, this);
    }

    public void onRemoved() {
        Physics.contact.removeListener(body);
        Physics.world.destroyBody(body);
    }

    public void onContactBegin(Fixture self, Fixture other, Vector2 normal, Contact contact) {
    }

    public void onContactEnd(Fixture self, Fixture other, Contact contact) {
        if (self == bodyFixture) {
            if (other == contactingGroundFixture) {
                contactingGroundFixture = null;
                contactingGroundNormal = null;
            } else if (other == contactingWallFixture) {
                contactingWallFixture = null;
                contactingWallDirection = null;
            }
        }
    }

    public void onPreSolve(Fixture self, Fixture other, Vector2 normal, Contact contact, Manifold oldManifold) {
        if (self == bodyFixture) {
            float normalAngle = normal.angle();
            if (normalAngle >= 180 + (90 - MAX_GROUND_SLOPE_DEG) && normalAngle <= 360 - (90 - MAX_GROUND_SLOPE_DEG)) {
                contactingGroundFixture = other;
                contactingGroundNormal = normal;
                if (contactingWallFixture == other) {
                    contactingWallFixture = null;
                    contactingWallDirection = null;
                }
            } else if (normalAngle > 360 - (90 - MAX_GROUND_SLOPE_DEG) && normalAngle <= 360 || normalAngle < EPSILON) {
                contactingWallFixture = other;
                contactingWallDirection = HorizontalDirection.RIGHT;
                if (contactingGroundFixture == other) {
                    contactingGroundFixture = null;
                    contactingGroundNormal = null;
                }
            } else if (normalAngle >= 180 - EPSILON && normalAngle < 180 + (90 - MAX_GROUND_SLOPE_DEG)) {
                contactingWallFixture = other;
                contactingWallDirection = HorizontalDirection.LEFT;
                if (contactingGroundFixture == other) {
                    contactingGroundFixture = null;
                    contactingGroundNormal = null;
                }
            }
        }
    }

    public void onPostSolve(Fixture self, Fixture other, Vector2 normal, Contact contact, ContactImpulse impulse) {
    }

    public void setPosition(float x, float y) {
        body.setTransform(x, y, 0);
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public void onUpdate(float secs) {
        boolean isOnGround = contactingGroundFixture != null;

        moveKeyPressed = false;
        if (KeyMap.isLeftPressed()) {
            if (!KeyMap.isRightPressed()) {
                facingDirection = HorizontalDirection.LEFT;
                moveKeyPressed = true;
            }
        } else if (KeyMap.isRightPressed()) {
            facingDirection = HorizontalDirection.RIGHT;
            moveKeyPressed = true;
        }

        moveNormal = new Vector2();
        if (isOnGround) {
            if (facingDirection == HorizontalDirection.LEFT) {
                moveNormal.x = +contactingGroundNormal.y;
                moveNormal.y = -contactingGroundNormal.x;
            } else {
                moveNormal.x = -contactingGroundNormal.y;
                moveNormal.y = +contactingGroundNormal.x;
            }
        } else {
            moveNormal.x = facingDirection.value();
        }

        Vector2 velocity = body.getLinearVelocity().cpy();

        // move
        if (moveKeyPressed) {
            // move
            float v0 = velocity.dot(moveNormal);
            float detV = (isOnGround ? MOVING_ACCELERATION : MOVING_ACCELERATION_OFF_GROUND) * secs;
            if (v0 + detV > MOVING_MAX_SPEED) {
                detV = Math.max(MOVING_MAX_SPEED - v0, 0);
            }
            velocity.mulAdd(moveNormal, detV);
        } else if (!dashing && isOnGround) {
            // friction
            float v0 = velocity.dot(moveNormal);
            if (Math.abs(v0) > 0) {
                float direction = Math.signum(v0);
                float detV = FRICTION_DECELERATION * secs * -direction;
                if ((v0 + detV) * v0 < 0) {
                    detV = -v0;
                }
                velocity.mulAdd(moveNormal, detV);
            }
        }

        // dash
        if (dashing) {
            dashDuration += secs;
            // dash stop
            if (dashDuration >= DASHING_DURATION_SECS || (moveKeyPressed && facingDirection != dashDirection)) {
                dashCooling = DASHING_COOL_DOWN_SECS;
                dashing = false;
                dashDuration = 0;
                Vector2 normal = moveNormal;
                if (normal.dot(dashNormal) < 0) {
                    normal = new Vector2(-normal.x, -normal.y);
                }
                float v0 = velocity.dot(normal);
                if (v0 > 0) {
                    float detV = DASHING_TAKEOFF_SPEED * DASHING_SLOWDOWN_SPEED_SCALAR;
                    if (v0 - detV < 0) {
                        detV = v0;
                    }
                    velocity.mulAdd(normal, -detV);
                }
            }
        }
        if (dashCooling > 0) {
            dashCooling -= secs;
        }
        if (KeyMap.isLeftDoublePressed() || KeyMap.isRightDoublePressed()) {
            // dash start
            if (!dashPressed && !dashing && dashCooling <= 0) {
                dashPressed = true;
                dashing = true;
                dashDuration = 0;
                dashDirection = facingDirection;
                dashNormal = moveNormal;
                float detV = DASHING_TAKEOFF_SPEED;
                float v0 = velocity.dot(moveNormal);
                if (v0 < 0) {
                    detV -= v0;
                }
                velocity.mulAdd(moveNormal, detV);
            }
        } else {
            dashPressed = false;
        }

        boolean isTouchingWall = contactingWallFixture != null;

        // climb wall
        if (moveKeyPressed && isTouchingWall && contactingWallDirection == facingDirection) {
            climbingWallFixture = contactingWallFixture;
        }
        boolean isClimbingWall = isTouchingWall && climbingWallFixture == contactingWallFixture;
        if (isClimbingWall) {
            float speed = velocity.y;
            if (speed < 0) {
                float detV = -Physics.world.getGravity().y * secs * CLIMBING_RESIST_GRAVITY_SCALAR;
                if ((speed + detV) * speed < 0) {
                    detV = -speed;
                }
                velocity.y += detV;
            }
        } else {
            climbingWallFixture = null;
        }

        // jump
        jumpJustPressed = false;
        if (KeyMap.isJumpPressed()) {
            if (!jumpPressed) {
                if (isOnGround) {
                    // jump on ground
                    jumpPressed = true;
                    jumpJustPressed = true;
                    jumpingHoldSecs = 0;
                    jumpTakeoffNormal = new Vector2(-contactingGroundNormal.x, -contactingGroundNormal.y);
                    velocity.mulAdd(jumpTakeoffNormal, JUMPING_TAKEOFF_SPEED);
                } else if (isTouchingWall) {
                    // wall jump
                    jumpPressed = true;
                    jumpJustPressed = true;
                    jumpingHoldSecs = 0;
                    jumpTakeoffNormal = new Vector2(0, 1).rotate(WALL_JUMP_ANGLE_DEG * contactingWallDirection.value());
                    velocity.mulAdd(jumpTakeoffNormal, JUMPING_TAKEOFF_SPEED);

                    lockFacingDirectionForAnimation = true;
                    lastWallJumpDirection = contactingWallDirection == HorizontalDirection.LEFT ? HorizontalDirection.RIGHT : HorizontalDirection.LEFT;
                }
            }

            // hold jump to jump higher
            if (!isOnGround && jumpingHoldSecs <= JUMPING_HOLD_MAX_SECS && !isClimbingWall) {
                jumpingHoldSecs += secs;
                float dt = secs;
                if (jumpingHoldSecs > JUMPING_HOLD_MAX_SECS) {
                    dt = JUMPING_HOLD_MAX_SECS - jumpingHoldSecs;
                }
                velocity.mulAdd(
                        Physics.world.getGravity(),
                        -JUMPING_HOLD_RESIST_GRAVITY_SCALAR * dt
                                * Easing.easeOutQuart(Math.min(jumpingHoldSecs, JUMPING_HOLD_MAX_SECS) / JUMPING_HOLD_MAX_SECS)
                );
            }
        } else {
            jumpPressed = false;
        }

        if (jumpJustPressed && !moveKeyPressed && jumpTakeoffNormal.x != 0) {
            facingDirection = jumpTakeoffNormal.x > 0 ? HorizontalDirection.RIGHT : HorizontalDirection.LEFT;
        }

        if (lockFacingDirectionForAnimation && (isOnGround || lastWallJumpDirection.value() * velocity.x < 0)) {
            lockFacingDirectionForAnimation = false;
            lastWallJumpDirection = null;
        }

        body.setLinearVelocity(velocity);

        animation.update(this, secs);
    }

    public void onRender(Batch batch) {
        animation.draw(batch);
    }

}
