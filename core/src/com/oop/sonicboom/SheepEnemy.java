package com.oop.sonicboom;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

public class SheepEnemy extends Enemy {

	private static Texture sprite1, sprite2, sprite3, sprite4, sprite5, sprite6;
	private static Animation animation;
	private float stateTime;
	
	private float distance;
	private float limitDistance;
	private boolean go, dead=true;

	public SheepEnemy(GameScreen game, MapObject object) {
		super(game, object);

		defineAnimation();

		limitDistance = 4;
	}

	private void defineAnimation() {
		sprite1 = new Texture("Sprites/sheep1.png");
		sprite2 = new Texture("Sprites/sheep2.png");
		sprite3 = new Texture("Sprites/sheep3.png");
		sprite4 = new Texture("Sprites/sheep4.png");
		sprite5 = new Texture("Sprites/sheep5.png");
		sprite6 = new Texture("Sprites/sheep6.png");

		Array<TextureRegion> frames = new Array<TextureRegion>();
		if(dead){
			frames.add(new TextureRegion(sprite1, 0, 0, 96, 65));
			frames.add(new TextureRegion(sprite2, 0, 0, 96, 65));
			frames.add(new TextureRegion(sprite3, 0, 0, 96, 65));
			frames.add(new TextureRegion(sprite4, 0, 0, 96, 65));
			frames.add(new TextureRegion(sprite5, 0, 0, 96, 65));
		} else{
			frames.add(new TextureRegion(sprite6, 0, 0, 96, 65));
		}

		animation = new Animation(0.2f, frames);
		frames.clear();

	}

	private void moveAround(float delta) {
		if (distance < 0) {
			go = true;
		} else if (distance > limitDistance) {
			go = false;
		}

		if (go) {
			body.setLinearVelocity(0.9f, 0);
			distance += delta;
		} else {
			body.setLinearVelocity(-0.9f, 0);
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
		body.setType(BodyType.DynamicBody);
		fixture.setSensor(false);
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
			dead = false;
			destroy();
			
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
		sprite1.dispose();
	}
}
