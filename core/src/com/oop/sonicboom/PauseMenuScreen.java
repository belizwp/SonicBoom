package com.oop.sonicboom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class PauseMenuScreen implements Screen {

	final GameScreen game;

	OrthographicCamera camera;

	private Stage stage;
	private Skin skin;

	private Texture bg;

	public PauseMenuScreen(final GameScreen game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 600);

		stage = new Stage(new StretchViewport(800, 600));

		skin = game.manager.get("UIskin/uiskin.json", Skin.class);

		for (Texture tex : skin.getAtlas().getTextures()) {
			tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		TextButton btnResume = new TextButton("RESUME", skin);
		btnResume.setWidth(200);
		btnResume.setHeight(50);
		btnResume.setPosition(800 / 2 - 200 / 2, 300);

		btnResume.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.manager.get("Sound/ChangeMap.wav", Sound.class).play();
				game.resume();
			}
		});

		TextButton btnQuit = new TextButton("QUIT", skin);
		btnQuit.setWidth(200);
		btnQuit.setHeight(50);
		btnQuit.setPosition(800 / 2 - 200 / 2, 256);

		btnQuit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.game.setScreen(new HomeScreen(game.game));
			}
		});

		stage.addActor(btnQuit);
		stage.addActor(btnResume);

	}

	public void update(float delta) {
		stage.act(delta);

	}

	@Override
	public void render(float delta) {
		try {
			game.batch.setProjectionMatrix(camera.combined);
			game.batch.begin();
			game.batch.draw(bg, 0, 0, 800, 600);
			game.batch.end();
		} catch (Exception e) {
			System.out.println("fail to draw pause bg");
		}

		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		generateBG();
	}

	@Override
	public void hide() {
		bg.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	public void generateBG() {
		// load original pixmap
		byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(),
				Gdx.graphics.getBackBufferHeight(), true);

		Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(),
				Pixmap.Format.RGBA8888);
		BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);

		// Blur the original pixmap with a radius of 4 px
		// The blur is applied over 2 iterations for better quality
		// We specify "disposePixmap=true" to destroy the original pixmap
		Pixmap blurred = BlurUtils.blur(pixmap, 8, 2, true);

		// we then create a GL texture with the blurred pixmap
		bg = new Texture(blurred);

		// dispose our blurred data now that it resides on the GPU
		blurred.dispose();
	}
}