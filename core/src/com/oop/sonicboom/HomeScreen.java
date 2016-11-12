package com.oop.sonicboom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
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

public class HomeScreen implements Screen {

	private OrthographicCamera cam;
	private Stage stage;
	private Skin skin;
	private SonicBoom game;
	private TextureAtlas buttonAtlas;
	private BitmapFont font;
	private TextButton button_ex;
	private TextButtonStyle textButtonStyle;
	private TextButton screenbut;
	private TextButton screenbut2;
	private Viewport viewport;
	private Texture img;

	private Music music;

	public HomeScreen(final SonicBoom game) {
		this.game = game;

		viewport = new FitViewport(SonicBoom.V_WIDTH, SonicBoom.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, game.batch);

		cam = new OrthographicCamera();

		cam.setToOrtho(false, 900, 671);

		img = game.manager.get("HomeScreen/home screen.png");
		img.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		stage = new Stage(new StretchViewport(1920, 1024));// 1920, 1024
		Gdx.input.setInputProcessor(stage);
		font = game.manager.get("UIskin/junegull.ttf", BitmapFont.class);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		skin = new Skin();
		buttonAtlas = game.manager.get("HomeScreen/homebut.pack");
		skin.addRegions(buttonAtlas);

		textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.up = skin.getDrawable("pic1");
		textButtonStyle.down = skin.getDrawable("pic2");
		textButtonStyle.checked = skin.getDrawable("pic2");
		screenbut2 = new TextButton("", textButtonStyle);
		stage.addActor(screenbut2);
		screenbut2.setPosition(300, 500);
		screenbut2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.manager.get("Sound/ChangeMap.wav", Sound.class).play();

				GameScorer.setMap(1);
				game.setScreen(new GameScreen(game));
			}
		});

		textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.up = skin.getDrawable("pic1");
		textButtonStyle.down = skin.getDrawable("pic2");
		textButtonStyle.checked = skin.getDrawable("pic2");
		screenbut = new TextButton("", textButtonStyle);
		stage.addActor(screenbut);
		screenbut.setPosition(300, 300);
		screenbut.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.manager.get("Sound/ChangeMap.wav", Sound.class).play();
				game.setScreen(new TutorialScreen(game));
			}
		});

		textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.up = skin.getDrawable("pic1");
		textButtonStyle.down = skin.getDrawable("pic2");
		textButtonStyle.checked = skin.getDrawable("pic2");
		button_ex = new TextButton("", textButtonStyle);
		stage.addActor(button_ex);
		button_ex.setPosition(300, 100);
		button_ex.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				Gdx.app.exit();
			}
		});

		Label forestLabel = new Label("START", new Label.LabelStyle(font, Color.WHITE));
		forestLabel.setFontScale(3);
		forestLabel.setPosition(410, 580);
		stage.addActor(forestLabel);

		Label forestLabel1 = new Label("TUTORIAL", new Label.LabelStyle(font, Color.WHITE));
		forestLabel1.setFontScale(3);
		forestLabel1.setPosition(350, 380);
		stage.addActor(forestLabel1);

		Label forestLabel2 = new Label("EXIT", new Label.LabelStyle(font, Color.WHITE));
		forestLabel2.setFontScale(3);
		forestLabel2.setPosition(440, 180);
		stage.addActor(forestLabel2);

		music = game.manager.get("Sound/Introduction.mp3", Music.class);
		music.setLooping(true);
	}

	@Override
	public void show() {
		music.play();
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
		music.stop();
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}
}
