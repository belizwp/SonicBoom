package com.oop.sonicboom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScorer {

	private static long startTime;
	private static long startPauseTime;
	private static long pauseTime;
	private static long timeCount;

	private static int score;

	public static int[] highScores = new int[] { 100, 80, 50, 30, 10 };
	public static String[] name = new String[] { "BELL", "FOOK", "NHAM", "TONG", "SEA" };
	public static long[] time = new long[] { 10, 30, 50, 80, 100 };
	public final static String file = ".oop";

	public static void load() {
		try {
			FileHandle filehandle = Gdx.files.external(file);

			String[] strings = filehandle.readString().split("\n");

			for (int i = 0; i < 5; i++) {
				highScores[i] = Integer.parseInt(strings[i + 1]);
			}
		} catch (Throwable e) {
			// :( It's ok we have defaults
		}
	}

	public static void save() {
		try {
			FileHandle filehandle = Gdx.files.external(file);

			for (int i = 0; i < 5; i++) {
				filehandle.writeString(Integer.toString(highScores[i]) + "\n", true);
			}
		} catch (Throwable e) {
		}
	}

	public static void start() {
		reset();
		startTime = TimeUtils.millis();

	}

	public static void pause() {
		startPauseTime = TimeUtils.millis();
	}

	public static void reseume() {
		pauseTime += TimeUtils.timeSinceMillis(startPauseTime);
	}

	public static void endGame() {
		GameScreen.forceGameOver();
	}

	public static void reset() {
		startTime = TimeUtils.millis();
		pauseTime = 0;
		timeCount = 0;
		score = 0;
	}

	public static void addScore(int value) {
		if (!GameScreen.isGameOver() && !GameScreen.isGameWin()) {
			score += value;
		}
	}

	public static int clearScore() {
		if (!GameScreen.isGameOver() && !GameScreen.isGameWin()) {
			if (score == 0) {
				endGame();
				return 0;
			}

			int tempScore = score;
			score = 0;
			return tempScore;
		}
		return 0;

	}

	public static int getScore() {
		return score;
	}

	public static long getTimeCount() {
		if (!GameScreen.isGameOver() && !GameScreen.isGameWin()) {
			timeCount = TimeUtils.timeSinceMillis(startTime) - pauseTime;
		}
		return timeCount;
	}

}
