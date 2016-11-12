package com.oop.sonicboom;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Spring extends GameObject {

	private Vector2 force;

	public Spring(GameScreen game, MapObject object) {
		super(game, object);

		force = new Vector2(0.37f, 0).rotate(-rotation);
	}

	@Override
	public void customizeObject() {
		// create bump surface
		FixtureDef fdef = new FixtureDef();
		EdgeShape eShape = new EdgeShape();

		Vector2 v1 = new Vector2(width / 2, height / 2 - 2 / SonicBoom.PPM);
		Vector2 v2 = new Vector2(width / 2, -height / 2 + 2 / SonicBoom.PPM);

		eShape.set(v1, v2);
		fdef.shape = eShape;
		fdef.isSensor = true;
		fdef.filter.categoryBits = SonicBoom.OBJECT_BIT;

		body.createFixture(fdef).setUserData(this);

		fixture.setSensor(true);
	}

	@Override
	public void hit() {
		// bump it!
		game.player.body.setLinearVelocity(0, 0);
		game.player.body.applyLinearImpulse(force, game.player.body.getWorldCenter(), true);

		game.manager.get("Sound/Item box break.wav", Sound.class).play();
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub

	}

}
