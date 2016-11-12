package com.oop.sonicboom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class LoadingScreen implements Screen {

	private SonicBoom game;

	private Stage stage;

	private Image loadingFrame;
	private Image loadingBarHidden;
	private Image screenBg;
	private Image loadingBg;

	private float startX, endX;
	private float percent;

	private Actor loadingBar;

	public LoadingScreen(SonicBoom game) {
		this.game = game;
	}

	@Override
	public void show() {
		// Tell the manager to load assets for the loading screen
		game.manager.load("LoadingScreen/loading.pack", TextureAtlas.class);
		// Wait until they are finished loading
		game.manager.finishLoading();

		// Initialize the stage where we will place everything
		stage = new Stage();

		// Get our textureatlas from the manager
		TextureAtlas atlas = game.manager.get("LoadingScreen/loading.pack", TextureAtlas.class);

		// Grab the regions from the atlas and create some images
		loadingFrame = new Image(atlas.findRegion("loading-frame"));
		loadingBarHidden = new Image(atlas.findRegion("loading-bar-hidden"));
		screenBg = new Image(atlas.findRegion("screen-bg"));
		loadingBg = new Image(atlas.findRegion("loading-frame-bg"));
		loadingBar = new Image(atlas.findRegion("loading-bar1"));

		// Add all the actors to the stage
		stage.addActor(screenBg);
		stage.addActor(loadingBar);
		stage.addActor(loadingBg);
		stage.addActor(loadingBarHidden);
		stage.addActor(loadingFrame);

		// Add everything to be loaded, for instance:
		loadAssets();
	}

	@Override
	public void resize(int width, int height) {
		// Set our screen to always be XXX x 480 in size
		stage.getViewport().update(width, height);

		// Make the background fill the screen
		screenBg.setSize(width, height);

		// Place the loading frame in the middle of the screen
		loadingFrame.setX((stage.getWidth() - loadingFrame.getWidth()) / 2);
		loadingFrame.setY((stage.getHeight() - loadingFrame.getHeight()) / 2);

		// Place the loading bar at the same spot as the frame, adjusted a few
		// px
		loadingBar.setX(loadingFrame.getX() + 15);
		loadingBar.setY(loadingFrame.getY() + 5);

		// Place the image that will hide the bar on top of the bar, adjusted a
		// few px
		loadingBarHidden.setX(loadingBar.getX() + 35);
		loadingBarHidden.setY(loadingBar.getY() - 3);
		// The start position and how far to move the hidden loading bar
		startX = loadingBarHidden.getX();
		endX = 440;

		// The rest of the hidden bar
		loadingBg.setSize(450, 50);
		loadingBg.setX(loadingBarHidden.getX() + 30);
		loadingBg.setY(loadingBarHidden.getY() + 3);
	}

	@Override
	public void render(float delta) {
		// Clear the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Load some, will return true if done loading
		if (game.manager.update()) {
			game.setScreen(new HomeScreen(game));

		}

		// Interpolate the percentage to make it more smooth
		percent = Interpolation.linear.apply(percent, game.manager.getProgress(), 0.1f);

		// Update positions (and size) to match the percentage
		loadingBarHidden.setX(startX + endX * percent);
		loadingBg.setX(loadingBarHidden.getX() + 30);
		loadingBg.setWidth(450 - 450 * percent);
		loadingBg.invalidate();

		// Show the loading screen
		stage.act();
		stage.draw();
	}

	private void loadAssets() {
		game.manager.load("Maps/bg_new.png", Texture.class);
		game.manager.load("Maps/bg_map2.png", Texture.class);
		game.manager.load("Maps/bg_sky.png", Texture.class);
		game.manager.load("Maps/bg3.png", Texture.class);

		game.manager.load("Sprites/boss.png", Texture.class);

		// Home Screen
		game.manager.load("HomeScreen/home screen.png", Texture.class);
		game.manager.load("HomeScreen/homebut.pack", TextureAtlas.class);

		// Pause Screen
		game.manager.load("UIskin/uiskin.json", Skin.class);

		// Tutorial Screen
		game.manager.load("TutorialScreen/Tutorialbg.jpg", Texture.class);
		game.manager.load("TutorialScreen/button.pack", TextureAtlas.class);

		// Win Screen
		game.manager.load("WinScreen/youWin.png", Texture.class);

		// Ring
		game.manager.load("Sprites/ring.gif", Texture.class);

		// Enemies
		game.manager.load("Sprites/sampleEnemySprite.png", Texture.class);

		// music
		game.manager.load("Sound/Background_Bossfight.mp3", Music.class);
		game.manager.load("Sound/Backgroundgame_1.mp3", Music.class);
		game.manager.load("Sound/Backgroundgame_2.mp3", Music.class);
		game.manager.load("Sound/Endgame.mp3", Music.class);
		game.manager.load("Sound/Introduction.mp3", Music.class);

		// sound
		game.manager.load("Sound/Bosslaugh.wav", Sound.class);
		game.manager.load("Sound/ChangeMap.wav", Sound.class);
		game.manager.load("Sound/collectCoin.wav", Sound.class);
		game.manager.load("Sound/Dash.wav", Sound.class);
		game.manager.load("Sound/Death.wav", Sound.class);
		game.manager.load("Sound/getReady.wav", Sound.class);
		game.manager.load("Sound/Go.wav", Sound.class);
		game.manager.load("Sound/Item box break.wav", Sound.class);
		game.manager.load("Sound/jump.wav", Sound.class);
		game.manager.load("Sound/Lose Rings.wav", Sound.class);
		game.manager.load("Sound/ManuSelect.mp3", Sound.class);
		game.manager.load("Sound/Ring (mono).wav", Sound.class);
		game.manager.load("Sound/Run out of air.wav", Sound.class);
		game.manager.load("Sound/Spin.wav", Sound.class);
		game.manager.load("Sound/wrong action.wav", Sound.class);

		// font
		FileHandleResolver resolver = new InternalFileHandleResolver();
		game.manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		game.manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		FreeTypeFontLoaderParameter hudFont = new FreeTypeFontLoaderParameter();
		hudFont.fontFileName = "UIskin/junegull.ttf";
		hudFont.fontParameters.size = 26;
		game.manager.load("UIskin/junegull.ttf", BitmapFont.class, hudFont);

		game.manager.finishLoading();
	}

	@Override
	public void hide() {
		// Dispose the loading assets as we no longer need them
		game.manager.unload("LoadingScreen/loading.pack");
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
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
