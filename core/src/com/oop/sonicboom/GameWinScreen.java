package com.oop.sonicboom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

public class GameWinScreen implements Screen {

	final GameScreen game;

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

	public GameWinScreen(final GameScreen game) {
		this.game = game;

		stage = new Stage();
		//stage.setDebugAll(true);

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
										game.game.setScreen(new HomeScreen(game.game));
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

				System.out.println(txfPlayerName.getText());
			}
		});

		// Player Name input text field
		txfPlayerName = new TextField("", skin);
		txfPlayerName.setMaxLength(15);

		// top score label
		Label highScoreLabel = new Label("High Scores", new Label.LabelStyle(font, Color.WHITE));

		name1 = new Label(String.format("1  %-15s   ", GameScorer.name[0]), new Label.LabelStyle(font, Color.WHITE));
		name2 = new Label(String.format("2  %-15s   ", GameScorer.name[1]), new Label.LabelStyle(font, Color.WHITE));
		name3 = new Label(String.format("3  %-15s   ", GameScorer.name[2]), new Label.LabelStyle(font, Color.WHITE));
		name4 = new Label(String.format("4  %-15s   ", GameScorer.name[3]), new Label.LabelStyle(font, Color.WHITE));
		name5 = new Label(String.format("5  %-15s   ", GameScorer.name[4]), new Label.LabelStyle(font, Color.WHITE));

		score1 = new Label(String.format("%d   ", GameScorer.highScores[0]), new Label.LabelStyle(font, Color.WHITE));
		score2 = new Label(String.format("%d   ", GameScorer.highScores[1]), new Label.LabelStyle(font, Color.WHITE));
		score3 = new Label(String.format("%d   ", GameScorer.highScores[2]), new Label.LabelStyle(font, Color.WHITE));
		score4 = new Label(String.format("%d   ", GameScorer.highScores[3]), new Label.LabelStyle(font, Color.WHITE));
		score5 = new Label(String.format("%d   ", GameScorer.highScores[4]), new Label.LabelStyle(font, Color.WHITE));

		time1 = new Label(String.format("%d", GameScorer.time[0]), new Label.LabelStyle(font, Color.WHITE));
		time2 = new Label(String.format("%d", GameScorer.time[1]), new Label.LabelStyle(font, Color.WHITE));
		time3 = new Label(String.format("%d", GameScorer.time[2]), new Label.LabelStyle(font, Color.WHITE));
		time4 = new Label(String.format("%d", GameScorer.time[3]), new Label.LabelStyle(font, Color.WHITE));
		time5 = new Label(String.format("%d", GameScorer.time[4]), new Label.LabelStyle(font, Color.WHITE));

		// putting stuff together
		// win pic
		table.add(win).expandX().padTop(50).row();

		// score table
		highScoreTable.add().width(150).padBottom(10);
		highScoreTable.add(highScoreLabel).padBottom(10);
		highScoreTable.add().width(150).padBottom(10).row();;
		
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
		buttonTable.padRight(90);
		buttonTable.add(txfPlayerName).padRight(15);
		buttonTable.add(buttonHome).padRight(15);
		buttonTable.add(buttonSubmit);

		table.add(buttonTable).padTop(40);

		// creating animations
		tweenManager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());

		// heading color animation
		Timeline.createSequence().beginSequence().start(tweenManager);

		// heading and buttons fade-in
		Timeline.createSequence().beginSequence().push(Tween.set(buttonHome, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(buttonHome, ActorAccessor.ALPHA).target(0))
				.push(Tween.from(win, ActorAccessor.ALPHA, .25f).target(0))
				.push(Tween.to(buttonHome, ActorAccessor.ALPHA, .25f).target(1)).end().start(tweenManager);

		// table fade-in
		Tween.from(table, ActorAccessor.ALPHA, .75f).target(0).start(tweenManager);
		Tween.from(table, ActorAccessor.Y, .75f).target(Gdx.graphics.getHeight() / 8).start(tweenManager);

		tweenManager.update(Gdx.graphics.getDeltaTime());

		// txfPlayerName.setVisible(false);
		// buttonSubmit.setVisible(false);
		// buttonHome.setVisible(false);

	}

	@Override
	public void render(float delta) {
		Gdx.input.setInputProcessor(stage);

		stage.act(delta);
		stage.draw();

		tweenManager.update(delta);
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
		table.invalidateHierarchy();
	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {
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
	}

}