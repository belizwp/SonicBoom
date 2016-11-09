package com.oop.sonicboom;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Disposable;

public abstract class Player extends Sprite implements Disposable {

	protected GameScreen game;

	protected Body body;
	protected Fixture fixture;

	protected Body wrapperBody;

	protected float rotation;
	protected Vector2 contactPoint;
	protected float contactAngle;

	protected enum State {
		IDLE, WALKING, RUNNING, SPINNING, SPINCHARGE, SPINJUMP, CROUCHING, HURTING, DYING
	};

	// player game state
	protected boolean faceRight;
	protected boolean onGround;
	protected boolean loop;
	protected boolean loseRing;
	protected boolean hurt;
	protected boolean dead;
	protected boolean onLoop;

	protected int tempRing;

	// player motion state
	protected boolean moveRight;
	protected boolean moveLeft;
	protected boolean preSpin;
	protected boolean spinCharged;
	protected boolean spinning;
	protected boolean spinJump;
	protected boolean crouch;
	protected boolean falling;

	private PlayerInputProcessor playerInputProcessor;

	public Player(GameScreen game) {
		this.game = game;

		createPlayer();

		// create PlayerInputProcessor
		playerInputProcessor = new PlayerInputProcessor(this);

		contactPoint = new Vector2();
	}

	public void switchLoop() {
		loop = loop ? false : true;
	}

	private void createPlayer() {
		// load position from map
		Vector2 position = game.parser.getBodies().get("player").getWorldCenter();
		game.getWorld().destroyBody(game.parser.getBodies().get("player"));

		// ************************ MAIN BODY ************************ //
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();

		bdef.type = BodyType.DynamicBody;
		bdef.position.set(position);
		bdef.angularDamping = 90;

		body = game.getWorld().createBody(bdef);

		CircleShape shape = new CircleShape();
		shape.setRadius(13 / SonicBoom.PPM);
		fdef.shape = shape;
		fdef.density = 1;

		fdef.filter.categoryBits = SonicBoom.PLAYER_BIT;
		fdef.filter.maskBits = SonicBoom.GROUND_BIT | SonicBoom.PLATFORM_BIT | SonicBoom.LOOP_SWITCH_BIT
				| SonicBoom.LOOP_R_BIT | SonicBoom.LOOP_R_SENSOR_BIT | SonicBoom.LOOP_L_BIT
				| SonicBoom.LOOP_L_SENSOR_BIT | SonicBoom.OBJECT_BIT;

		fixture = body.createFixture(fdef);
		fixture.setUserData(this);

		// ************************ WRAPPER BODY ************************ //
		bdef.type = BodyType.DynamicBody;
		bdef.position.set(position);
		bdef.angularDamping = 0;

		wrapperBody = game.getWorld().createBody(bdef);

		PolygonShape wShape = new PolygonShape();
		wShape.setAsBox(26 / 2 / SonicBoom.PPM, 45 / 2 / SonicBoom.PPM, new Vector2(0, 45 / 2 / SonicBoom.PPM), 0);
		fdef.shape = wShape;
		fdef.density = 0;
		fdef.isSensor = true;

		fdef.filter.categoryBits = SonicBoom.PLAYER_BIT;
		fdef.filter.maskBits = SonicBoom.ENEMY_BIT | SonicBoom.RING_BIT;

		wrapperBody.createFixture(fdef).setUserData(this);
	}

	abstract public State getState();

	abstract public void update(float delta);

	abstract public void spinJump();

	abstract public void dash();

	abstract public void loseRing(int n);

	abstract public void hurt(float time);

	abstract public void kill();

	public InputProcessor getInputProcessor() {
		return playerInputProcessor;
	}

}
