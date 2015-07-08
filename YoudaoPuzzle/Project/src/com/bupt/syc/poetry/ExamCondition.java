package com.bupt.syc.poetry;


public class ExamCondition {
    String dynasty[];
    String author;
    int amount; //XX题的卷面 
    boolean isMarked; //是否标记 
    String albumName;//考试选择的专辑的名字	
    String style;
    
    
	public ExamCondition() {
		super();
		this.dynasty = null;
		this.author = null;
		this.amount = 0;
		this.isMarked = false;
		this.albumName = null;
		this.style = null;
	}


	public ExamCondition(String[] dynasty, String author, int amount,
			boolean isMarked, String albumName, String style) {
		super();
		this.dynasty = dynasty;
		this.author = author;
		this.amount = amount;
		this.isMarked = isMarked;
		this.albumName = albumName;
		this.style = style;
	}


	public String[] getDynasty() {
		return dynasty;
	}


	public void setDynasty(String[] dynasty) {
		this.dynasty = dynasty;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public int getAmount() {
		return amount;
	}


	public void setAmount(int amount) {
		this.amount = amount;
	}


	public boolean isMarked() {
		return isMarked;
	}


	public void setMarked(boolean isMarked) {
		this.isMarked = isMarked;
	}


	public String getAlbumName() {
		return albumName;
	}


	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}


	public String getStyle() {
		return style;
	}


	public void setStyle(String style) {
		this.style = style;
	}
	

    
    
}
