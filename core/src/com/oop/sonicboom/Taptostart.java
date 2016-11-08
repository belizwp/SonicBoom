package com.oop.sonicboom;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Taptostart implements Screen {

	private OrthographicCamera cam;
	private Stage stage;
	private Skin skin;
	private SonicBoom game;
	private TextureAtlas buttonAtlas;
	private BitmapFont font;
	private TextButton button;
	private TextButton button_ex;
	private TextButtonStyle textButtonStyle;
	private TextButton button_tul;
	private TextButton screenbut;
	private TextButton screenbut2;
	private Viewport viewport;
	private Texture img;

	public Taptostart(final SonicBoom game) {
		this.game = game;

		viewport = new FitViewport(SonicBoom.V_WIDTH, SonicBoom.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, game.batch);

		cam = new OrthographicCamera();

		cam.setToOrtho(false, 1920, 1050);

		img = new Texture("tap to start.jpg");// choose img

		stage = new Stage(new StretchViewport(1920, 1024));
		Gdx.input.setInputProcessor(stage);
		font = new BitmapFont();

		skin = new Skin();
		buttonAtlas = new TextureAtlas(Gdx.files.internal("tutorials/button.pack"));// choose
																					// img
		skin.addRegions(buttonAtlas);

		textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.up = skin.getDrawable("home pic");
		textButtonStyle.down = skin.getDrawable("home");
		textButtonStyle.checked = skin.getDrawable("home");
		screenbut2 = new TextButton("", textButtonStyle);
		stage.addActor(screenbut2);
		screenbut2.setPosition(300, 600);
		screenbut2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.setScreen(new Homescreen(game));
			}
		});

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.setProjectionMatrix(cam.combined);
		game.batch.begin();
		game.batch.draw(img, 0, 0);
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
