package com.oop.sonicboom;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class OnScreenController {

	private GameScreen game;
	private Player player;

	private Viewport viewport;
	private Stage stage;
	private OrthographicCamera cam;

	private Texture btn;

	public OnScreenController(GameScreen game) {
		this.game = game;
		this.player = game.player;

		cam = new OrthographicCamera();
		viewport = new StretchViewport(800, 600, cam);
		stage = new Stage(viewport, game.batch);
		// stage.setDebugAll(true);

		Table table = new Table();
		table.setFillParent(true);
		table.left().bottom();

		// arrow up
		btn = game.manager.get("Controller/up.png", Texture.class);
		btn.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Image upImg = new Image(btn);
		upImg.setSize(50, 50);
		upImg.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (player.preSpin && (player.onGround || player.loop && player.body.getLinearVelocity().y <= 0)) {
					player.spinCharged = true;
				} else {
					player.spinJump();
				}
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

			}
		});

		// arrow down
		btn = game.manager.get("Controller/down.png", Texture.class);
		btn.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Image downImg = new Image(btn);
		downImg.setSize(50, 50);
		downImg.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if ((player.moveRight || player.moveLeft) && player.onGround) {
					player.spinning = true;
				}
				if (!player.spinning) {
					player.preSpin = true;
					player.crouch = true;
				}

				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				player.crouch = false;
				if (player.spinCharged) {
					player.spinCharged = false;
					player.spinning = true;
					player.dash();
				}
				player.preSpin = false;
			}
		});

		// arrow right
		btn = game.manager.get("Controller/right.png", Texture.class);
		btn.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Image rightImg = new Image(btn);
		rightImg.setSize(50, 50);
		rightImg.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				player.crouch = false;
				if (player.preSpin && player.onGround) {
					player.spinning = true;
				}
				player.moveRight = true;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				player.moveRight = false;
			}
		});

		// arrow left
		btn = game.manager.get("Controller/left.png", Texture.class);
		btn.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Image leftImg = new Image(btn);
		leftImg.setSize(50, 50);
		leftImg.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				player.crouch = false;
				if (player.preSpin && player.onGround) {
					player.spinning = true;
				}
				player.moveLeft = true;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				player.moveLeft = false;
			}
		});

		table.padBottom(20);
		table.padLeft(20);
		table.padRight(20);

		table.add(upImg).padRight(10);
		table.add().expandX();
		table.add(downImg).padLeft(10);
		table.add(leftImg).padLeft(10);
		table.add(rightImg).padLeft(10);

		stage.addActor(table);
	}

	public void draw() {
		stage.draw();
	}

	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	public Stage getInputProcessor() {
		return stage;
	}

}
