package com.oop.sonicboom;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Sonic extends Player {

	public final float MAX_NORM_SPD = 4f;
	public final float MAX_SPIN_SPD = 5.75f;

	private State currentState;
	private State previousState;

	private float stateTimer;
	private float lastSpd;

	private Animation idle;
	private Animation walk;
	private Animation run;
	private Animation spin;
	private Animation charge;
	private Animation jump;
	private Animation crouching;

	private Texture texture;

	public Sonic(GameScreen game) {
		super(game);

		currentState = State.IDLE;
		previousState = State.IDLE;
		stateTimer = 0;

		faceRight = true;

		texture = new Texture("Sprites/boss.png");

		Array<TextureRegion> frames = new Array<TextureRegion>();

		// get idle animation frames and add them to sonic Animation
		for (int i = 1; i < 5; i++)
			frames.add(new TextureRegion(texture, 110 + 47 * i, 133, 45, 72));
		idle = new Animation(0.2f, frames);
		frames.clear();

		// get walk and run animation frames and add them to sonic Animation
		for (int i = 1; i < 5; i++)
			frames.add(new TextureRegion(texture, 110 + 47 * i, 214, 45, 72));
		walk = new Animation(0.2f, frames);
		run = new Animation(0.1f, frames);
		frames.clear();

		// get spin animation frames and add them to sonic Animation
		for (int i = 1; i < 4; i++)
			frames.add(new TextureRegion(texture, 525 + 51 * i, 619, 45, 74));
		spin = new Animation(0.125f, frames);
		frames.clear();

		// get spin charge animation frames and add them to sonic Animation
		frames.add(new TextureRegion(texture, 527, 620, 45, 74));
		charge = new Animation(0, frames);
		frames.clear();

		// get jump animation frames and add them to sonic Animation
		for (int i = 2; i < 5; i++)
			frames.add(new TextureRegion(texture, 110 + 47 * i, 322, 45, 72));
		jump = new Animation(0.2f, frames);
		frames.clear();

		// get crouch animation frames and add them to sonic Animation
		for (int i = 7; i < 9; i++)
			frames.add(new TextureRegion(texture, 110 + 45 * i, 322, 45, 72));
		crouching = new Animation(0.2f, frames);
		frames.clear();

		// set area of sprite
		setBounds(0, 0, 50 / SonicBoom.PPM, 74 / SonicBoom.PPM);
	}

	@Override
	public void update(float delta) {
		// update motion of player
		updateMotion();

		// set position of sprite and update frames
		setPosition(body.getWorldCenter().x - getWidth() / 2, body.getWorldCenter().y - 18 / SonicBoom.PPM);
		setRegion(getFrame(delta));
		stateTimer += delta;

		// wait another part for update ring losing here
	}

	public TextureRegion getFrame(float delta) {
		currentState = getState();

		TextureRegion region;

		switch (currentState) {
		case CROUCHING:
			region = crouching.getKeyFrame(stateTimer, true);
			break;
		case JUMPPING:
			region = jump.getKeyFrame(stateTimer);
			break;
		case SPINCHARGE:
			region = charge.getKeyFrame(stateTimer, true);
			break;
		case SPINNING:
			region = spin.getKeyFrame(stateTimer, true);
			break;
		case RUNNING:
			region = run.getKeyFrame(stateTimer, true);
			break;
		case WALKING:
			region = walk.getKeyFrame(stateTimer, true);
			break;
		case IDLE:
		default:
			region = idle.getKeyFrame(stateTimer, true);
			break;
		}

		if (!faceRight && !region.isFlipX()) {
			region.flip(true, false);
		} else if (faceRight && region.isFlipX()) {
			region.flip(true, false);
		}

		stateTimer = currentState == previousState ? stateTimer + delta : 0;
		previousState = currentState;

		return region;
	}

	public State getState() {
		if (spinJump) {
			return State.JUMPPING;
		} else if (spinCharged) {
			return State.SPINCHARGE;
		} else if (spinning) {
			return State.SPINNING;
		} else if (crouch) {
			return State.CROUCHING;
		} else if (body.getLinearVelocity().x > 2f || body.getLinearVelocity().x < -2f) {
			return State.RUNNING;
		} else if (body.getLinearVelocity().x > 0.1f || body.getLinearVelocity().x < -0.1f) {
			return State.WALKING;
		} else {
			return State.IDLE;
		}
	}

	private void updateMotion() {
		// update jump state
		if (onGround) {
			spinJump = false;
		}

		Vector2 spd = body.getLinearVelocity();
		isSpdDown(spd.x);
		if (isSpdDown(spd.x) && spinning && (spd.x <= 1f && spd.x >= -2f) && (spd.y <= 2f && spd.y >= -2f)) {
			spinning = false;
		}

		if (crouch) {
			body.setLinearDamping(1);
		} else {
			body.setLinearDamping(0);
		}

		if (spinCharged) {
			body.setActive(false);
		}

		if (moveRight && (spd.x <= MAX_SPIN_SPD && spinning || spd.x <= MAX_NORM_SPD)) {
			body.applyLinearImpulse(new Vector2(0.002f, 0), body.getWorldCenter(), true);
			body.applyTorque(-5f, true);

			faceRight = true;
		}
		if (moveLeft && (spd.x >= -MAX_SPIN_SPD && spinning || spd.x >= -MAX_NORM_SPD)) {
			body.applyLinearImpulse(new Vector2(-0.002f, 0), body.getWorldCenter(), true);
			body.applyTorque(5f, true);

			faceRight = false;
		}
	}

	// is speed down
	private boolean isSpdDown(float newSpd) {
		if (newSpd <= lastSpd) {
			lastSpd = newSpd;
			return true;
		}
		lastSpd = newSpd;
		return false;
	}

	@Override
	public void jump() {
		if (onGround) {
			body.applyForce(new Vector2(0, 13f), body.getWorldCenter(), true);
			onGround = false;
		}
	}

	@Override
	public void dash() {
		body.setActive(true);

		if (faceRight) {
			body.applyLinearImpulse(new Vector2(0.4f, 0), body.getWorldCenter(), true);
		} else {
			body.applyLinearImpulse(new Vector2(-0.4f, 0), body.getWorldCenter(), true);
		}
	}

	@Override
	public void dispose() {
		texture.dispose();
	}

	@Override
	public void loseRing() {
		loseRing = true;
	}
}
