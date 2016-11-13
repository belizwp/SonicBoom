package com.oop.sonicboom;

public class Profile {

	private String name;
	private int score;
	private long time;

	public Profile() {
		this.name = "";
		this.score = 0;
		this.time = 0;
	}

	public Profile(String name, int score, long time) {
		this.name = name;
		this.score = score;
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return name + " " + score + " " + time;
	}

}
