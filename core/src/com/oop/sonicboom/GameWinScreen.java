package com.oop.sonicboom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

public class GameWinScreen implements Screen {

	final SonicBoom game;

	OrthographicCamera camera;

	private Stage stage;
	private Skin skin;
	private BitmapFont font;

	private Table table;
	private Table highScoreTable;
	private Table buttonTable;

	private TweenManager tweenManager;

	private TextButton buttonHome;
	private TextButton buttonSubmit;
	private TextField txfPlayerName;

	private Label name1;
	private Label name2;
	private Label name3;
	private Label name4;
	private Label name5;

	private Label score1;
	private Label score2;
	private Label score3;
	private Label score4;
	private Label score5;

	private Label time1;
	private Label time2;
	private Label time3;
	private Label time4;
	private Label time5;

	private boolean newHighScore;

	private Image newHighScoreTag;

	private Texture bg;

	public GameWinScreen(final SonicBoom game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 600);

		stage = new Stage(new StretchViewport(800, 600));
		// stage.setDebugAll(true);

		skin = game.manager.get("UIskin/uiskin.json", Skin.class);

		for (Texture tex : skin.getAtlas().getTextures()) {
			tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		font = game.manager.get("UIskin/junegull.ttf", BitmapFont.class);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// root table
		table = new Table(skin);
		table.center().top();
		table.setFillParent(true);

		stage.addActor(table);

		// sub table
		highScoreTable = new Table();
		buttonTable = new Table();

		// Win Picture
		Texture winPic = game.manager.get("WinScreen/youWin.png", Texture.class);
		winPic.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Image win = new Image(winPic);

		Texture newHighScorePic = game.manager.get("WinScreen/highScore.png", Texture.class);
		newHighScorePic.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		newHighScoreTag = new Image(newHighScorePic);

		// Home Button
		buttonHome = new TextButton("HOME", skin);
		buttonHome.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.manager.get("Sound/ChangeMap.wav", Sound.class).play();

				Timeline.createParallel().beginParallel().push(Tween.to(table, ActorAccessor.ALPHA, .75f).target(0))
						.push(Tween.to(table, ActorAccessor.Y, .75f).target(table.getY() - 50)
								.setCallback(new TweenCallback() {

									@Override
									public void onEvent(int type, BaseTween<?> source) {
										game.setScreen(new HomeScreen(game));
									}
								}))
						.end().start(tweenManager);
			}
		});
		buttonHome.pad(10);

		// Home Button
		buttonSubmit = new TextButton("SUBMIT", skin);
		buttonSubmit.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.manager.get("Sound/ChangeMap.wav", Sound.class).play();

				if (!txfPlayerName.getText().contains(" ") && !txfPlayerName.getText().equals("")) {
					GameScorer.addNewHighScore(txfPlayerName.getText());

					GameScorer.save();

					updateLabel();

					txfPlayerName.setVisible(false);
					buttonSubmit.setVisible(false);
					buttonHome.setVisible(true);
					newHighScoreTag.setVisible(false);
				}

				System.out.println(txfPlayerName.getText());
			}
		});

		// Player Name input text field
		txfPlayerName = new TextField("", skin);
		txfPlayerName.setMessageText("enter your name.");
		txfPlayerName.setAlignment(Align.center);
		txfPlayerName.setMaxLength(5);

		// top score label
		Label highScoreLabel = new Label("High Scores", new Label.LabelStyle(font, Color.WHITE));

		name1 = new Label("1", new Label.LabelStyle(font, Color.WHITE));
		name2 = new Label("2", new Label.LabelStyle(font, Color.WHITE));
		name3 = new Label("3", new Label.LabelStyle(font, Color.WHITE));
		name4 = new Label("4", new Label.LabelStyle(font, Color.WHITE));
		name5 = new Label("5", new Label.LabelStyle(font, Color.WHITE));

		score1 = new Label("", new Label.LabelStyle(font, Color.WHITE));
		score2 = new Label("", new Label.LabelStyle(font, Color.WHITE));
		score3 = new Label("", new Label.LabelStyle(font, Color.WHITE));
		score4 = new Label("", new Label.LabelStyle(font, Color.WHITE));
		score5 = new Label("", new Label.LabelStyle(font, Color.WHITE));

		time1 = new Label("", new Label.LabelStyle(font, Color.WHITE));
		time2 = new Label("", new Label.LabelStyle(font, Color.WHITE));
		time3 = new Label("", new Label.LabelStyle(font, Color.WHITE));
		time4 = new Label("", new Label.LabelStyle(font, Color.WHITE));
		time5 = new Label("", new Label.LabelStyle(font, Color.WHITE));

		// putting stuff together
		// win pic
		table.add(win).expandX().padTop(50).row();

		// score table
		highScoreTable.add().width(150).padBottom(10);
		highScoreTable.add(highScoreLabel).padBottom(10);
		highScoreTable.add().width(150).padBottom(10).row();

		highScoreTable.add(name1).width(150);
		highScoreTable.add(score1).width(150);
		highScoreTable.add(time1).width(150).row();

		highScoreTable.add(name2).width(150);
		highScoreTable.add(score2).width(150);
		highScoreTable.add(time2).width(150).row();

		highScoreTable.add(name3).width(150);
		highScoreTable.add(score3).width(150);
		highScoreTable.add(time3).width(150).row();

		highScoreTable.add(name4).width(150);
		highScoreTable.add(score4).width(150);
		highScoreTable.add(time4).width(150).row();

		highScoreTable.add(name5).width(150);
		highScoreTable.add(score5).width(150);
		highScoreTable.add(time5).width(150).row();

		table.add(highScoreTable).padTop(20).row();

		// button table
		buttonTable.padRight(180);
		buttonTable.add(newHighScoreTag).width(80).height(80).padRight(20);
		buttonTable.add(txfPlayerName).padRight(15);
		buttonTable.add(buttonHome).padRight(15);
		buttonTable.add(buttonSubmit);

		table.add(buttonTable).padTop(20);

		// creating animations
		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());

		// heading color animation
		Timeline.createSequence().beginSequence()
				.push(Tween.to(newHighScoreTag, ActorAccessor.RGB, .5f).target(0, 0, 1))
				.push(Tween.to(newHighScoreTag, ActorAccessor.RGB, .5f).target(0, 1, 0))
				.push(Tween.to(newHighScoreTag, ActorAccessor.RGB, .5f).target(1, 0, 0))
				.push(Tween.to(newHighScoreTag, ActorAccessor.RGB, .5f).target(1, 1, 0))
				.push(Tween.to(newHighScoreTag, ActorAccessor.RGB, .5f).target(0, 1, 1))
				.push(Tween.to(newHighScoreTag, ActorAccessor.RGB, .5f).target(1, 0, 1))
				.push(Tween.to(newHighScoreTag, ActorAccessor.RGB, .5f).target(1, 1, 1)).end().repeat(Tween.INFINITY, 0)
				.start(tweenManager);

		// heading and buttons fade-in
		Timeline.createSequence().beginSequence().push(Tween.set(buttonHome, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(buttonHome, ActorAccessor.ALPHA).target(0))
				.push(Tween.from(win, ActorAccessor.ALPHA, .25f).target(0))
				.push(Tween.to(buttonHome, ActorAccessor.ALPHA, .25f).target(1)).end().start(tweenManager);

		// table fade-in
		Tween.from(table, ActorAccessor.ALPHA, .75f).target(0).start(tweenManager);
		Tween.from(table, ActorAccessor.Y, .75f).target(Gdx.graphics.getHeight() / 8).start(tweenManager);

		tweenManager.update(Gdx.graphics.getDeltaTime());

		updateLabel();

		newHighScore = GameScorer.isNewHighScore();

		if (newHighScore) {
			txfPlayerName.setVisible(true);
			buttonSubmit.setVisible(true);
			buttonHome.setVisible(false);
			newHighScoreTag.setVisible(true);
		} else {
			txfPlayerName.setVisible(false);
			buttonSubmit.setVisible(false);
			buttonHome.setVisible(true);
			newHighScoreTag.setVisible(false);
		}

		generateBlurBG();

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.batch.draw(bg, 0, 0, 800, 600);
		game.batch.end();

		stage.act(delta);
		stage.draw();

		tweenManager.update(delta);
	}

	private void updateLabel() {
		name1.setText(String.format("1  %-15s", nameFormat(GameScorer.profiles.get(0).getName())));
		name2.setText(String.format("2  %-15s", nameFormat(GameScorer.profiles.get(1).getName())));
		name3.setText(String.format("3  %-15s", nameFormat(GameScorer.profiles.get(2).getName())));
		name4.setText(String.format("4  %-15s", nameFormat(GameScorer.profiles.get(3).getName())));
		name5.setText(String.format("5  %-15s", nameFormat(GameScorer.profiles.get(4).getName())));

		score1.setText(scoreFormat(GameScorer.profiles.get(0).getScore()));
		score2.setText(scoreFormat(GameScorer.profiles.get(1).getScore()));
		score3.setText(scoreFormat(GameScorer.profiles.get(2).getScore()));
		score4.setText(scoreFormat(GameScorer.profiles.get(3).getScore()));
		score5.setText(scoreFormat(GameScorer.profiles.get(4).getScore()));

		time1.setText(timeFormat(GameScorer.profiles.get(0).getTime()));
		time2.setText(timeFormat(GameScorer.profiles.get(1).getTime()));
		time3.setText(timeFormat(GameScorer.profiles.get(2).getTime()));
		time4.setText(timeFormat(GameScorer.profiles.get(3).getTime()));
		time5.setText(timeFormat(GameScorer.profiles.get(4).getTime()));
	}

	private String nameFormat(String name) {
		if (name.equals("")) {
			return "------";
		}
		return name;
	}

	private String scoreFormat(int score) {
		if (score == 0) {
			return "------";
		}
		return score + "";
	}

	private String timeFormat(long time) {
		if (time == 0) {
			return "--:--:--";
		}

		int millis = (int) (time % 1000) / 10;
		int seconds = (int) (time / 1000);
		int minutes = seconds / 60;
		seconds -= minutes * 60;

		return String.format("%02d", minutes) + ':' + String.format("%02d", seconds) + ':'
				+ String.format("%02d", millis);
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
		table.invalidateHierarchy();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		game.manager.get("Sound/Itsshowtime.mp3", Music.class).play();

	}

	@Override
	public void hide() {
		game.manager.get("Sound/Itsshowtime.mp3", Music.class).stop();
		dispose();
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
		bg.dispose();
	}

	public void generateBlurBG() {
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