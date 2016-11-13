package com.oop.sonicboom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScorer {

	private static long startTime;
	private static long startPauseTime;
	private static long pauseTime;
	private static long timeCount;

	private static int score;

	public static ArrayList<Profile> profiles = new ArrayList<Profile>();

	private static int newPlaceIndex = -1;

	public final static String file = ".sonicboom";

	public static void load() {
		try {
			FileHandle filehandle = Gdx.files.external(file);

			for (int i = 0; i < 5; i++) {
				profiles.add(new Profile());
			}

			String[] strings = filehandle.readString().split("\n");
			String[] format; // [NAME, HIGHSCORE, TIME]

			for (int i = 0; i < 5; i++) {
				format = strings[i].split("\\s+");

				profiles.get(i).setName(format[0]);
				profiles.get(i).setScore(Integer.parseInt(format[1]));
				profiles.get(i).setTime(Long.parseLong(format[2]));
			}

			sortScore();
		} catch (Throwable e) {
			// :( It's ok we have defaults
		}
	}

	public static void save() {
		try {
			FileHandle filehandle = Gdx.files.external(file);

			sortScore();

			String string = "";

			for (int i = 0; i < 5; i++) {
				string += profiles.get(i).toString() + "\n";
			}

			filehandle.writeString(string, false);

		} catch (Throwable e) {
		}
	}

	public static void sortScore() {
		Collections.sort(profiles, new Comparator<Profile>() {
			@Override
			public int compare(Profile o1, Profile o2) {
				if (o1.getScore() - o2.getScore() == 0) {
					return (int) (o1.getTime() - o2.getTime());
				} else {
					return -(o1.getScore() - o2.getScore());
				}

			}
		});
	}

	public static boolean isNewHighScore() {
		for (int i = 0; i < 5; i++) {
			if (score > profiles.get(i).getScore()) {
				newPlaceIndex = i;
				return true;
			}
		}
		return false;
	}

	public static void addNewHighScore(String name) {
		profiles.add(newPlaceIndex, new Profile(name, getScore(), getTimeCount()));
	}

	public static int getNewPlace() {
		return newPlaceIndex;
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
