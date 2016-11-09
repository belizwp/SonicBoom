package com.oop.sonicboom;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

public abstract class Enemy implements Disposable {

	protected GameScreen game;
	protected World world;
	protected MapObject object;

	protected Body body;
	protected Fixture fixture;

	protected TextureMapObject textureObject;
	protected TextureRegion textureRegion;

	protected float rotation;
	protected float x;
	protected float y;
	protected float width;
	protected float height;

	protected boolean toDestroy;
	protected boolean destroyed;

	protected float destroyedTime;

	public Enemy(GameScreen game, MapObject object) {
		this.game = game;
		this.world = game.getWorld();
		this.object = object;

		this.textureObject = (TextureMapObject) object;
		this.textureRegion = textureObject.getTextureRegion();

		defineEnemy();
		customizeEnemy();
	}

	private void defineEnemy() {
		TextureMapObject textureMapObject = (TextureMapObject) object;

		rotation = textureMapObject.getRotation();

		x = textureMapObject.getX() / SonicBoom.PPM;
		y = textureMapObject.getY() / SonicBoom.PPM;

		width = textureMapObject.getProperties().get("width", Float.class).floatValue() / SonicBoom.PPM;
		height = textureMapObject.getProperties().get("height", Float.class).floatValue() / SonicBoom.PPM;

		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();

		body = world.createBody(bdef);

		applyTiledLocationToBody(body, x, y, width, height, rotation);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2, height / 2);
		fdef.shape = shape;

		fdef.filter.categoryBits = SonicBoom.ENEMY_BIT;

		fixture = body.createFixture(fdef);
		fixture.setUserData(this);
	}

	private void applyTiledLocationToBody(Body body, float x, float y, float width, float height, float rotation) {
		// set body position taking into consideration the center position
		body.setTransform(x + width / 2, y + height / 2, 0);

		// bottom left position in local coordinates
		Vector2 localPosition = new Vector2(-width / 2, -height / 2);

		// save world position before rotation
		Vector2 positionBefore = body.getWorldPoint(localPosition).cpy();

		// calculate angle in radians
		float angle = -rotation * MathUtils.degreesToRadians;

		// set new angle
		body.setTransform(body.getPosition(), angle);

		// save world position after rotation
		Vector2 positionAfter = body.getWorldPoint(localPosition).cpy();

		// adjust position with the difference (before - after)
		// so that the bottom left position remains unchanged
		Vector2 newPosition = body.getPosition().add(positionBefore).sub(positionAfter);
		body.setTransform(newPosition, angle);
	}

	public void draw(Batch batch) {
		if (!destroyed) {
			batch.draw(textureRegion, x, y, textureObject.getOriginX() / SonicBoom.PPM,
					textureObject.getOriginY() / SonicBoom.PPM,
					textureObject.getTextureRegion().getRegionWidth() / SonicBoom.PPM,
					textureObject.getTextureRegion().getRegionHeight() / SonicBoom.PPM, textureObject.getScaleX(),
					textureObject.getScaleY(), -textureObject.getRotation());
		}

	}

	public void setRegion(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}

	private void updatePosition() {
		x = body.getPosition().x - width / 2;
		y = body.getPosition().y - height / 2;
	}

	public void update(float delta) {
		// destroy item
		if (toDestroy && !destroyed) {
			world.destroyBody(body);
			destroyed = true;
		}

		// dead time
		if (destroyed) {
			destroyedTime += delta;
		}

		updatePosition();
	}

	public void destroy() {
		toDestroy = true;
	}

	public float getDestroyedTime() {
		return destroyedTime;
	}

	public abstract void customizeEnemy();

	public abstract void hit();

}
