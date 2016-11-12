package com.oop.sonicboom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
	private Table table;
	private TweenManager tweenManager;

	private Texture winPic;

	public GameWinScreen(final GameScreen game) {
		this.game = game;

		stage = new Stage();
		// stage.setDebugAll(true);

		skin = game.manager.get("UIskin/uiskin.json", Skin.class);

		table = new Table(skin);
		table.top();
		table.setFillParent(true);
		// table.setBounds(0, 0, stage.getWidth(), stage.getHeight());

		winPic = game.manager.get("WinScreen/youWin.png", Texture.class);
		winPic.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		Image win = new Image(winPic);
		stage.addActor(win);

		TextButton buttonHome = new TextButton("HOME", skin);
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
		buttonHome.pad(15);

		// putting stuff together
		table.add(win).padTop(75).row();
		table.add(buttonHome).padTop(100);

		stage.addActor(table);

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