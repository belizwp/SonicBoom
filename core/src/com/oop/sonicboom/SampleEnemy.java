package com.oop.sonicboom;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

public class SampleEnemy extends Enemy {

	private static Texture sprite;
	private static Animation animation;
	private float stateTime;

	private float distance;
	private float limitDistance;
	private boolean go;

	public SampleEnemy(GameScreen game, MapObject object) {
		super(game, object);

		defineAnimation();

		limitDistance = 3;
	}

	private void defineAnimation() {
		sprite = new Texture("Sprites/sampleEnemySprite.png");

		Array<TextureRegion> frames = new Array<TextureRegion>();
		frames.add(new TextureRegion(sprite, 0, 0, 32, 32));
		frames.add(new TextureRegion(sprite, 32, 0, 32, 32));
		frames.add(new TextureRegion(sprite, 64, 0, 32, 32));
		frames.add(new TextureRegion(sprite, 96, 0, 32, 32));

		animation = new Animation(0.2f, frames);
	}

	private void moveAround(float delta) {
		if (distance < 0) {
			go = true;
		} else if (distance > limitDistance) {
			go = false;
		}

		if (go) {
			body.setLinearVelocity(0.4f, 0);
			distance += delta;
		} else {
			body.setLinearVelocity(-0.4f, 0);
			distance -= delta;
		}
	}

	private void flip() {
		if (go && !textureRegion.isFlipX()) {
			textureRegion.flip(true, false);
		} else if (!go && textureRegion.isFlipX()) {
			textureRegion.flip(true, false);
		}
	}

	@Override
	public void customizeEnemy() {
		body.setType(BodyType.KinematicBody);
		fixture.setSensor(true);
	}

	@Override
	public void update(float delta) {
		super.update(delta);

		// set current picture of animation
		setRegion(animation.getKeyFrame(stateTime, true));
		stateTime += delta;

		// flip picture
		flip();

		// make it moving around
		moveAround(delta);
	}

	@Override
	public void hit() {
		// do some thing
		if (game.player.spinning || game.player.spinJump) {
			destroy();
		} else {
			pushBack(game.player, 0.125f, 0.2f);
			game.player.hurt(1);
			game.player.loseRing(GameScorer.clearScore());
		}

	}

	private void pushBack(Player player, float vx, float vy) {
		Vector2 velocity = player.body.getLinearVelocity();
		Vector2 forceBack = new Vector2(velocity.x <= 0 ? vx : -vx, vy);
		player.body.applyLinearImpulse(forceBack, player.body.getWorldCenter(), true);
	}

	@Override
	public void dispose() {
		sprite.dispose();
	}

}
