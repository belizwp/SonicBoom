package com.oop.sonicboom;

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

public class Homescreen implements Screen {

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

	public Homescreen(final SonicBoom game) {
		this.game = game;

		viewport = new FitViewport(SonicBoom.V_WIDTH, SonicBoom.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, game.batch);

		cam = new OrthographicCamera();

		cam.setToOrtho(false, 900, 671);

		img = new Texture("home screen.png");// choose img

		stage = new Stage(new StretchViewport(1920, 1024));// 1920, 1024
		Gdx.input.setInputProcessor(stage);
		font = new BitmapFont();

		skin = new Skin();
		buttonAtlas = new TextureAtlas(Gdx.files.internal("homescreen/homebut.pack"));// choose
																						// img
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
				game.setScreen(new GameScreen((SonicBoom) game));
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
				game.setScreen(new MenuScreen(game));
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

		Label forestLabel = new Label("START", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		forestLabel.setFontScale((float) 4.0);
		forestLabel.setPosition(420, 580);
		stage.addActor(forestLabel);

		Label forestLabel1 = new Label("TUTORAIL", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		forestLabel1.setFontScale((float) 4.0);
		forestLabel1.setPosition(380, 380);
		stage.addActor(forestLabel1);

		Label forestLabel2 = new Label("EXIT", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		forestLabel2.setFontScale((float) 4.0);
		forestLabel2.setPosition(440, 180);
		stage.addActor(forestLabel2);

		//
		// // skin.addRegions(buttonAtlas);
		// textButtonStyle = new TextButtonStyle();
		// textButtonStyle.font = font;
		// textButtonStyle.up = skin.getDrawable("Button1");
		// textButtonStyle.down = skin.getDrawable("Button2");
		// textButtonStyle.checked = skin.getDrawable("Button3");
		// button_tul = new TextButton("", textButtonStyle);
		// stage.addActor(button_tul);
		// button_tul.setPosition(700, 425);
		// button_tul.addListener(new ClickListener() {
		// @Override
		// public void clicked(InputEvent event, float x, float y) {
		// super.clicked(event, x, y);
		// Gdx.app.exit();
		// }
		// });
		//
		//
		// Label menuLabel = new Label("MENU", new Label.LabelStyle(new
		// BitmapFont(), Color.YELLOW));
		// menuLabel.setFontScale((float)3.5);
		// menuLabel.setPosition(825, 935);
		// stage.addActor(menuLabel);
		//
		// Label startLabel = new Label("START", new Label.LabelStyle(new
		// BitmapFont(), Color.WHITE));
		// startLabel.setFontScale((float)3);
		// startLabel.setPosition(840, 750);
		// stage.addActor(startLabel);
		//
		// Label tulLabel = new Label("TUTORIAL", new Label.LabelStyle(new
		// BitmapFont(), Color.WHITE));
		// tulLabel.setFontScale((float)3);
		// tulLabel.setPosition(805, 530);
		// stage.addActor(tulLabel);
		//
		// Label exitLabel = new Label("EXIT", new Label.LabelStyle(new
		// BitmapFont(), Color.WHITE));
		// exitLabel.setFontScale((float)3);
		//
		// exitLabel.setPosition(860, 320);
		// stage.addActor(exitLabel);

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
