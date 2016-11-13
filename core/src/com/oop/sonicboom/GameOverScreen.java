package com.oop.sonicboom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameOverScreen implements Screen {

	private OrthographicCamera cam;
	private Stage stage;
	private Skin skin;
	private SonicBoom game;
	private TextureAtlas buttonAtlas;
	private BitmapFont font;
	private TextButton button_ex;
	private TextButtonStyle textButtonStyle;
	private Viewport viewport;
	private Texture img;

	public GameOverScreen(final SonicBoom game) {
		this.game = game;

		viewport = new FitViewport(SonicBoom.V_WIDTH, SonicBoom.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, game.batch);

		cam = new OrthographicCamera();

		cam.setToOrtho(false, 800, 600);

		img = game.manager.get("OverScreen/gameover.png", Texture.class);
		img.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		stage = new Stage(new StretchViewport(1920, 1024));
		Gdx.input.setInputProcessor(stage);
		font = game.manager.get("UIskin/junegull.ttf", BitmapFont.class);

		skin = new Skin();
		buttonAtlas = game.manager.get("TutorialScreen/button.pack", TextureAtlas.class);
		skin.addRegions(buttonAtlas);

		textButtonStyle = new TextButtonStyle();

		textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.up = skin.getDrawable("home pic");
		textButtonStyle.down = skin.getDrawable("home");
		textButtonStyle.checked = skin.getDrawable("home");
		button_ex = new TextButton("", textButtonStyle);
		stage.addActor(button_ex);
		button_ex.setWidth(120);
		button_ex.setHeight(120);
		button_ex.setPosition(1750, 10);// home
		button_ex.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.setScreen(new HomeScreen(game));
			}
		});

	}

	@Override
	public void show() {
		game.manager.get("Sound/Bosslaugh.wav", Sound.class).play();

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.setProjectionMatrix(cam.combined);
		game.batch.begin();
		game.batch.draw(img, 0, 0, 800, 600);
		game.batch.end();

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {

		stage.getViewport().update(width, height, true);

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
		stage.dispose();
		skin.dispose();
		img.dispose();
	}
}
