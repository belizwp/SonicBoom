package com.oop.sonicboom;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud implements Disposable {

	// Scene2D.ui Stage and its own Viewport for HUD
	public Stage stage;
	private Viewport viewport;
	private String worldTimer;
	private long timeCount;
	private int minutes;
	private int seconds;
	private int millis;

	private Label countdownLabel;
	private static Label scoreLabel;
	private Label timeLabel;
	private Label levelLabel;
	private Label worldLabel;
	private Label sonicLabel;

	BitmapFont textFont;

	public Hud(GameScreen game) {
		textFont = game.manager.get("UIskin/junegull.ttf", BitmapFont.class);
		textFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// setup the HUD viewport using a new camera seperate from our gamecam
		// define our stage using that viewport and our games spritebatch
		viewport = new FitViewport(SonicBoom.V_WIDTH, SonicBoom.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport);

		Table table = new Table();
		table.top();
		table.setFillParent(true);

		countdownLabel = new Label(String.format("%06d", worldTimer), new Label.LabelStyle(textFont, Color.WHITE));
		scoreLabel = new Label(String.format("%06d", 0), new Label.LabelStyle(textFont, Color.WHITE));
		timeLabel = new Label("Time", new Label.LabelStyle(textFont, Color.WHITE));
		levelLabel = new Label("Sea", new Label.LabelStyle(textFont, Color.WHITE));
		worldLabel = new Label("MAP", new Label.LabelStyle(textFont, Color.WHITE));
		sonicLabel = new Label("Score", new Label.LabelStyle(textFont, Color.WHITE));

		// define our labels using the String, and a Label style consisting of a
		// font and color

		table.add(sonicLabel).expandX().padTop(10);
		table.add(worldLabel).expandX().padTop(10);
		table.add(timeLabel).expandX().padTop(10);
		table.row();
		table.add(scoreLabel).expandX();
		table.add(levelLabel).expandX();
		table.add(countdownLabel).expandX().width(100);

		stage.addActor(table);

	}

	private void updateWorldTimer() {
		timeCount = GameScorer.getTimeCount();

		millis = (int) (timeCount % 1000) / 10;
		seconds = (int) (timeCount / 1000);
		minutes = seconds / 60;
		seconds -= minutes * 60;

		worldTimer = String.format("%02d", minutes) + ':' + String.format("%02d", seconds) + ':'
				+ String.format("%02d", millis);
		countdownLabel.setText(worldTimer);
	}

	public void setMapName(String name) {
		levelLabel.setText(name);
	}

	public void update(float delta) {
		updateWorldTimer();
		scoreLabel.setText(GameScorer.getScore() + "");

	}

	public void render() {
		stage.draw();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
