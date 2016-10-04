package com.oop.sonicboom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

	// basic GameScreen variables
	private OrthographicCamera gameCam;
	private Viewport gamePort;

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

	// Hud
	private Hud hud;

	// Background
	private Texture bg;
	private OrthographicCamera bgCam; // for static bg

	// Box2d parser variables
	public Box2DMapObjectParser parser;

	// Player
	Player player;

	// Game Object
	GameObject gameObject;

	public GameScreen(SonicBoom game) {
		this.game = game;

		// create cam used to follow player through cam world
		gameCam = new OrthographicCamera();

		// create a FitViewport to maintain virtual aspect ratio despite screen
		// size
		gamePort = new StretchViewport(SonicBoom.V_WIDTH / SonicBoom.PPM, SonicBoom.V_HEIGHT / SonicBoom.PPM, gameCam);

		// Load map and setup our map renderer
		maploader = new TmxMapLoader();
		map = maploader.load("Maps/testMap/testMap.tmx");
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
		worldContactListener = new WorldContactListener();
		world.setContactListener(worldContactListener);

		// allows for debug lines of box2d world.
		b2dr = new Box2DDebugRenderer();

		// create hud
		hud = new Hud();

		// create background
		bg = new Texture("Maps/testMap/bg.png");
		bgCam = new OrthographicCamera(SonicBoom.V_WIDTH, SonicBoom.V_HEIGHT);
		bgCam.position.set(SonicBoom.V_WIDTH / 2, SonicBoom.V_HEIGHT / 2, 0);

		// parse box2d object from map
		parser = new Box2DMapObjectParser(1 / SonicBoom.PPM);
		parser.load(world, map);
		debug = false;

		// create Sonic!
		player = new Sonic(world, this);

		// create item such ring
		gameObject = new GameObject(this);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		handleInput(delta);
		update(delta);
		renderWorld(delta);
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {

		map.dispose();
		renderer.dispose();
		world.dispose();
		b2dr.dispose();
		hud.dispose();
		bg.dispose();

	}

	public void handleInput(float delta) {

		// toggle debug mode
		if (Gdx.input.isKeyJustPressed(Keys.SLASH))
			toggleDebug();

		// Return to menu
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			game.setScreen(new MenuScreen(game));
		}

		// player input
		player.handleInput(delta);
	}

	public void update(float delta) {
		world.step(1 / 60f, 8, 3);

		player.update(delta);
		gameObject.update(delta);

		gameCam.position.x = player.body.getWorldCenter().x;
		gameCam.position.y = player.body.getWorldCenter().y;

		if (gameCam.position.x - SonicBoom.V_WIDTH / 2 / SonicBoom.PPM < 0)
			gameCam.position.x = SonicBoom.V_WIDTH / 2 / SonicBoom.PPM;

		if (gameCam.position.x + SonicBoom.V_WIDTH / 2 / SonicBoom.PPM > mapPixelWidth / SonicBoom.PPM)
			gameCam.position.x = mapPixelWidth / SonicBoom.PPM - SonicBoom.V_WIDTH / 2 / SonicBoom.PPM;

		if (gameCam.position.y - SonicBoom.V_HEIGHT / 2 / SonicBoom.PPM < 0)
			gameCam.position.y = SonicBoom.V_HEIGHT / 2 / SonicBoom.PPM;

		if (gameCam.position.y + SonicBoom.V_HEIGHT / 2 / SonicBoom.PPM > mapPixelHeight / SonicBoom.PPM)
			gameCam.position.y = mapPixelHeight / SonicBoom.PPM - SonicBoom.V_HEIGHT / 2 / SonicBoom.PPM;

		gameCam.update();
		bgCam.update();

		renderer.setView(gameCam);
	}

	public void renderWorld(float delta) {

		game.batch.setProjectionMatrix(bgCam.combined);
		game.batch.begin();
		game.batch.draw(bg, 0, 0, SonicBoom.V_WIDTH, SonicBoom.V_HEIGHT);
		game.batch.end();

		renderer.render(backLayer);

		game.batch.setProjectionMatrix(gameCam.combined);
		game.batch.begin();
		player.draw(game.batch);
		gameObject.draw(game.batch);
		game.batch.end();

		renderer.render(foreLayer);

		if (debug)
			b2dr.render(world, gameCam.combined);
		hud.render();
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

}
