package com.bupt.syc.poetry;


public class Game2Result {
	public static final int NOT_FILL = 1;
	public static final int FILL_CORRECT = 2;
	public static final int FILL_WRONG = 3;
    boolean[][] init;
    char[][] result;
    int[][] mark;
	char[][] matrix;
    
	public Game2Result(int height,int width) {
		super();
		init = new boolean[height][width];
		result = new char[height][width];
		matrix = new char[height][width];
		mark = new int[height][width];
	}

	public boolean[][] getInit() {
		return init;
	}

	public void setInit(boolean[][] init) {
		this.init = init;
	}

	public char[][] getResult() {
		return result;
	}

	public void setResult(char[][] result) {
		this.result = result;
	}

	public int[][] getMark() {
		return mark;
	}

	public void setMark(int[][] mark) {
		this.mark = mark;
	}

	public char[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(char[][] matrix) {
		this.matrix = matrix;
	}
    
    
}
