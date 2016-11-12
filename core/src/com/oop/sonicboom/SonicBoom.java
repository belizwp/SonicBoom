package com.oop.sonicboom;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SonicBoom extends Game {

	// Virtual size for game
	public static final int V_WIDTH = 600;
	public static final int V_HEIGHT = 450;

	// Pixel per Meter
	public static final float PPM = 100;

	// Fixture Mask BIT
	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short RING_BIT = 4;
	public static final short ENEMY_BIT = 8;
	public static final short OBJECT_BIT = 16;
	public static final short PLATFORM_BIT = 32;
	public static final short LOOP_SWITCH_BIT = 64;
	public static final short LOOP_R_BIT = 128;
	public static final short LOOP_R_SENSOR_BIT = 256;
	public static final short LOOP_L_BIT = 512;
	public static final short LOOP_L_SENSOR_BIT = 1024;
	public static final short WARP_BIT = 2048;
	public static final short BOSS_BIT = 4096;

	// used by all screens
	public SpriteBatch batch;
	public AssetManager manager;

	@Override
	public void create() {
		batch = new SpriteBatch();
		manager = new AssetManager();

		setScreen(new LoadingScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
		manager.dispose();
	}
}
