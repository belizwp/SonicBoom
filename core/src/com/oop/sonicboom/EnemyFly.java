package com.oop.sonicboom;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

public class EnemyFly extends Enemy {

	private static Texture sprite, sprite1, sprite2, sprite3;
	private static Animation animation;
	private static Animation deadAnimation;
	private float stateTime;

	private float distance;
	private float limitDistance;
	private boolean go, dead;
	
	private float deadTime = 0.35f;

	public EnemyFly(GameScreen game, MapObject object) {
		super(game, object);

		defineAnimation();

		body.setType(BodyType.DynamicBody);

		Filter filter = new Filter();
		filter.categoryBits = SonicBoom.BOSS_BIT;
		fixture.setFilterData(filter);
		fixture.setSensor(false);
		
		limitDistance = 5;
	}

	private void defineAnimation() {
		sprite = new Texture("Sprites/enemyfly1.png");
		sprite1 = new Texture("Sprites/enemyfly2.png");
		sprite2 = new Texture("Sprites/enemyfly3.png");
		sprite3 = new Texture("Sprites/enemyfly4.png");

		
		Array<TextureRegion> frames = new Array<TextureRegion>();
		frames.add(new TextureRegion(sprite, 0, 0, 90, 140));
		frames.add(new TextureRegion(sprite1, 0, 0, 90, 140));
		frames.add(new TextureRegion(sprite2, 0, 0, 90, 140));

		animation = new Animation(0.2f, frames);
		frames.clear();
		
		frames.add(new TextureRegion(sprite3, 0, 0, 90, 140));
		
		
		deadAnimation = new Animation(0.2f, frames);
		frames.clear();
	}

	private void moveAround(float delta) {
		if (distance < 0) {
			go = true;
		} else if (distance > limitDistance) {
			go = false;
		}

		if (go) {
			body.setLinearVelocity(0.7f, 0);
			distance += delta;
		} else {
			body.setLinearVelocity(-0.7f, 0);
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
		if (!dead) {
			setRegion(animation.getKeyFrame(stateTime, true));

			// make it moving around
			moveAround(delta);

		} else {
			setRegion(deadAnimation.getKeyFrame(stateTime, true));
			deadTime -= delta;

			if (deadTime < 0) {
				destroy();
			}
		}
		stateTime += delta;

		// flip picture
		flip();
	}

	@Override
	public void hit() {
		// do some thing
		if (game.player.spinning || game.player.spinJump) {
			dead = true;
		} else {
			pushBack(game.player, 0.125f, 0.2f);
			game.player.hurt(1);
			game.player.loseRing(GameScorer.clearScore());
		}
	}

	private void pushBack(Player player, float vx, float vy) {
		Vector2 velocity = player.body.getLinearVelocity();
		Vector2 forceBack = new Vector2(velocity.x <= 0 ? vx : -vx, vy);
		player.body.setLinearVelocity(0, 0);
		player.body.applyLinearImpulse(forceBack, player.body.getWorldCenter(), true);
	}

	@Override
	public void dispose() {
		sprite.dispose();
		sprite1.dispose();
		sprite2.dispose();
	}
}
