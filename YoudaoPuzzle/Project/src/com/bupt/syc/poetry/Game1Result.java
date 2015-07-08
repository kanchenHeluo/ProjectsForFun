package com.bupt.syc.poetry;

import java.util.ArrayList;

public class Game1Result {
	int score;
	ArrayList<Game1PoemList> rightAnswer;
	ArrayList<String> wrongAnswer;
	
	public Game1Result() {
		super();
		rightAnswer = new ArrayList<Game1PoemList>();
		wrongAnswer = new ArrayList<String>();
		score = 0;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public ArrayList<Game1PoemList> getRightAnswer() {
		return rightAnswer;
	}

	public void setRightAnswer(ArrayList<Game1PoemList> rightAnswer) {
		this.rightAnswer = rightAnswer;
	}

	public ArrayList<String> getWrongAnswer() {
		return wrongAnswer;
	}

	public void setWrongAnswer(ArrayList<String> wrongAnswer) {
		this.wrongAnswer = wrongAnswer;
	}
	
}	
