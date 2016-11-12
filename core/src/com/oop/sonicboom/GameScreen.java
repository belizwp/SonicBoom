package com.oop.sonicboom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser;

public class GameScreen implements Screen {
	// Reference to our Game, used to set Screens
	final SonicBoom game;

	public AssetManager manager;

	// Game State
	public static final int GAME_READY = 0;
	public static final int GAME_RUNNING = 1;
	public static final int GAME_PAUSED = 2;
	public static final int GAME_OVER = 4;
	public static final int GAME_END_LEVEL = 5;
	public static final int GAME_WIN = 6;

	private static int state;

	private final int MAX_MAP_NUMBER = 3;

	private boolean setToChangeMap;

	// screen that relate to GameScreen
	PauseMenuScreen pauseMenu;

	// batch for draw
	Batch batch;

	// basic GameScreen variables
	private OrthographicCamera gameCam;
	private Viewport gamePort;
	private boolean updateCam;

	// Tiled map variables
	private TmxMapLoader maploader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private MapProperties mapProperties;
	private int mapWidth;
	private int mapHeight;
	private int tilePixelWidth;
	private int tilePixelHeight;
	private int mapPixelWidth;
	private int mapPixelHeight;
	private int[] backLayer = { 0, 1 };
	private int[] foreLayer = { 2 };

	// Box2d variables
	private World world;
	public WorldContactListener worldContactListener;
	private Box2DDebugRenderer b2dr;
	private boolean debug;
	private ShapeRenderer shapeRenderer;

	// Hud
	private Hud hud;

	// Background
	private Texture bg;
	private OrthographicCamera bgCam; // for static bg

	// Box2d parser variables
	public Box2DMapObjectParser parser;

	// Player
	public Player player;

	// Game Object
	public GameObjects gameObjects;

	// Enemies
	private Enemies enemies;

	private Music music;

	public GameScreen(SonicBoom game) {
		this.game = game;
		this.batch = game.batch;
		this.manager = game.manager;

		pauseMenu = new PauseMenuScreen(this);

		// create hud
		hud = new Hud(this);

		// create cam used to follow player through cam world
		gameCam = new OrthographicCamera();
		updateCam = true;

		// create background cam
		bgCam = new OrthographicCamera(SonicBoom.V_WIDTH, SonicBoom.V_HEIGHT);
		bgCam.position.set(SonicBoom.V_WIDTH / 2, SonicBoom.V_HEIGHT / 2, 0);

		// create a StretchViewport
		gamePort = new StretchViewport(SonicBoom.V_WIDTH / SonicBoom.PPM, SonicBoom.V_HEIGHT / SonicBoom.PPM, gameCam);

		// Load map and setup our map renderer
		maploader = new TmxMapLoader();

		if (GameScorer.getCurrentMap() == 1) {
			map = maploader.load("Maps/Neo_Green_Hill_1.tmx");
			hud.setMapName("Beach");
			bg = game.manager.get("Maps/bg_new.png", Texture.class);
			music = game.manager.get("Sound/Backgroundgame_1.mp3", Music.class);
		} else if (GameScorer.getCurrentMap() == 2) {
			map = maploader.load("Maps/Hot_Crater_Act_2.tmx");
			hud.setMapName("Volcano");
			bg = game.manager.get("Maps/bg_map2.png", Texture.class);
			music = game.manager.get("Sound/Backgroundgame_2.mp3", Music.class);
		} else if (GameScorer.getCurrentMap() == 3) {
			map = maploader.load("Maps/final_map.tmx");
			hud.setMapName("Boss");
			bg = game.manager.get("Maps/bg_sky.png", Texture.class);
			music = game.manager.get("Sound/Background_Bossfight.mp3", Music.class);
		} else {
			map = maploader.load("Maps/testMap.tmx");
			hud.setMapName("TEST ROOM");
			bg = game.manager.get("Maps/bg3.png", Texture.class);
			music = game.manager.get("Sound/Backgroundgame_1.mp3", Music.class);
		}

		bg.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		music.setLooping(true);

		renderer = new OrthogonalTiledMapRenderer(map, 1 / SonicBoom.PPM);
		mapProperties = map.getProperties();
		mapWidth = mapProperties.get("width", Integer.class);
		mapHeight = mapProperties.get("height", Integer.class);
		tilePixelWidth = mapProperties.get("tilewidth", Integer.class);
		tilePixelHeight = mapProperties.get("tileheight", Integer.class);
		mapPixelWidth = mapWidth * tilePixelWidth;
		mapPixelHeight = mapHeight * tilePixelHeight;
		gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

		// create Box2D world, setting no gravity in X, -10 gravity in Y, and
		// allow bodies to sleep
		world = new World(new Vector2(0, -9.81f), true);
		worldContactListener = new WorldContactListener(this);
		world.setContactListener(worldContactListener);

		// allows for debug lines of box2d world.
		b2dr = new Box2DDebugRenderer();
		shapeRenderer = new ShapeRenderer();

		// parse box2d object from map
		parser = new Box2DMapObjectParser(1 / SonicBoom.PPM);
		parser.load(world, map);
		debug = false;

		// create Sonic!
		player = new Sonic(this);

		// create game object
		gameObjects = new GameObjects(this);

		// create enemies
		enemies = new Enemies(this);

		if (GameScorer.getCurrentMap() == 1) {
			GameScorer.start();
		}

		state = GAME_RUNNING;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(player.getInputProcessor());
		music.play();

	}

	@Override
	public void render(float delta) {
		update(delta);
		draw(delta);
		handleInput(delta);

		if (setToChangeMap) {
			game.manager.get("Sound/ChangeMap.wav", Sound.class).play();

			changeMap(GameScorer.getCurrentMap() + 1);
		}
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
		pauseMenu.resize(width, height);
	}

	@Override
	public void pause() {
		if (state == GAME_RUNNING) {
			state = GAME_PAUSED;
			GameScorer.pause();
			pauseMenu.show();
			music.pause();
			game.manager.get("Sound/ManuSelect.mp3", Sound.class).play();
		}

	}

	@Override
	public void resume() {
		if (state == GAME_PAUSED) {
			state = GAME_RUNNING;
			GameScorer.reseume();
			pauseMenu.hide();
			music.play();
			show();
		}

	}

	@Override
	public void hide() {
		music.stop();

	}

	@Override
	public void dispose() {

		renderer.dispose();
		b2dr.dispose();
		world.dispose();
		hud.dispose();
		enemies.dispose();
		pauseMenu.dispose();

	}

	private void handleInput(float delta) {

		// toggle debug mode
		if (Gdx.input.isKeyJustPressed(Keys.SLASH))
			toggleDebug();

		// pause / resume
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			if (state == GAME_RUNNING) {
				pause();
			} else {
				resume();
			}
		}

		// Test ring spawning for now
		if (Gdx.input.isKeyPressed(Keys.C)) {
			gameObjects.spawnRing(player.body.getWorldCenter().add(-8 / SonicBoom.PPM, 50 / SonicBoom.PPM), 0, 3);
		}

		// Test kill player
		if (Gdx.input.isKeyJustPressed(Keys.K)) {
			forceGameOver();
		}

		// Test reset score
		if (Gdx.input.isKeyJustPressed(Keys.R)) {
			GameScorer.reset();
		}

		// Test change map
		if (Gdx.input.isKeyJustPressed(Keys.NUM_1)) {
			changeMap(1);
		}
		if (Gdx.input.isKeyJustPressed(Keys.NUM_2)) {
			changeMap(2);
		}
		if (Gdx.input.isKeyJustPressed(Keys.NUM_3)) {
			changeMap(3);
		}
		if (Gdx.input.isKeyJustPressed(Keys.NUM_0)) {
			changeMap(0);
		}
	}

	private void update(float delta) {
		switch (state) {
		case GAME_READY:
			state = GAME_RUNNING;
			break;
		case GAME_RUNNING:
			updateRunning(delta);
			break;
		case GAME_PAUSED:
			updatePaused(delta);
			break;
		case GAME_OVER:
			updateGameOver(delta);
			break;
		}
	}

	private void updateRunning(float delta) {
		// world callback time step
		world.step(1 / 60f, 8, 3);

		// update player, enemies and game object
		player.update(delta);
		enemies.update(delta);
		gameObjects.update(delta);

		// update cam position
		if (updateCam) {
			gameCam.position.x = player.body.getWorldCenter().x;
			gameCam.position.y = player.body.getWorldCenter().y;
			// if cam out of map
			if (gameCam.position.x - SonicBoom.V_WIDTH / 2 / SonicBoom.PPM < 0)
				gameCam.position.x = SonicBoom.V_WIDTH / 2 / SonicBoom.PPM;

			if (gameCam.position.x + SonicBoom.V_WIDTH / 2 / SonicBoom.PPM > mapPixelWidth / SonicBoom.PPM)
				gameCam.position.x = mapPixelWidth / SonicBoom.PPM - SonicBoom.V_WIDTH / 2 / SonicBoom.PPM;

			if (gameCam.position.y - SonicBoom.V_HEIGHT / 2 / SonicBoom.PPM < 0)
				gameCam.position.y = SonicBoom.V_HEIGHT / 2 / SonicBoom.PPM;

			if (gameCam.position.y + SonicBoom.V_HEIGHT / 2 / SonicBoom.PPM > mapPixelHeight / SonicBoom.PPM)
				gameCam.position.y = mapPixelHeight / SonicBoom.PPM - SonicBoom.V_HEIGHT / 2 / SonicBoom.PPM;
		}

		gameCam.update();
		bgCam.update();

		renderer.setView(gameCam);

		// update HUD
		hud.update(delta);
	}

	private void updatePaused(float delta) {
		pauseMenu.update(delta);
	}

	private void updateGameOver(float delta) {
		updateRunning(delta);
		player.kill();

		if (music.isPlaying()) {
			music.stop();
		}
	}

	private void draw(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		switch (state) {
		case GAME_RUNNING:
			presentRunning(delta);
			break;
		case GAME_PAUSED:
			presentPaused(delta);
			break;
		case GAME_OVER:
			presentGameOver(delta);
			break;
		}
	}

	private void presentRunning(float delta) {
		// draw static background
		batch.setProjectionMatrix(bgCam.combined);
		batch.begin();
		batch.draw(bg, 0, 0, SonicBoom.V_WIDTH, SonicBoom.V_HEIGHT);
		batch.end();

		// darw back layer of map
		renderer.render(backLayer);

		// draw player, enemies and game object
		batch.setProjectionMatrix(gameCam.combined);
		batch.begin();
		gameObjects.draw(game.batch);
		enemies.draw(game.batch);
		player.draw(game.batch);
		batch.end();

		// draw foregraound of map
		renderer.render(foreLayer);

		// debug
		if (debug) {
			b2dr.render(world, gameCam.combined);

			gameCam.update();
			shapeRenderer.setProjectionMatrix(gameCam.combined);
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(1, 0, 0, 1);
			shapeRenderer.line(player.contactPoint.x, player.contactPoint.y, player.body.getWorldCenter().x,
					player.body.getWorldCenter().y);
			shapeRenderer.end();
		}

		// draw HUD
		hud.render();
	}

	private void presentPaused(float delta) {
		// render pause menu screen
		pauseMenu.render(delta);
	}

	private void presentGameOver(float delta) {
		// overlay running
		presentRunning(delta);
		// render game over screen
	}

	public TiledMap getMap() {
		return map;
	}

	public World getWorld() {
		return world;
	}

	public void toggleDebug() {
		debug = debug ? false : true;
	}

	public void setUpdateCam(boolean bool) {
		updateCam = bool;
	}

	public static void forceGameOver() {
		state = GAME_OVER;
	}

	public static boolean isGameOver() {
		return state == GAME_OVER;
	}

	private void changeMap(int level) {
		if (level >= 1 && level <= MAX_MAP_NUMBER) {
			dispose();
			GameScorer.setMap(level);
			game.setScreen(new GameScreen(game));
		} else {
			dispose();
			GameScorer.setMap(0);
			game.setScreen(new GameScreen(game));
		}
	}

	public void changeMap() {
		setToChangeMap = true;
	}

}
