package com.bupt.syc.poetry;

public class studyCondition {
	String dynasty[];
	String author;
	String style;
	boolean isMarked;
	String titleMatched;	
	String album;
	
	public studyCondition(String dynasty[], String author, String style,
			boolean isMarked, String titleMatched, String album) {
		super();
		this.dynasty = dynasty;
		this.author = author;
		this.style = style;
		this.isMarked = isMarked;
		this.titleMatched = titleMatched;
		this.album = album;
	}

	public studyCondition() {
		super();
		this.dynasty = null;
		this.author = null;
		this.style = null;
		this.isMarked = false;
		this.titleMatched = null;
		this.album = null;
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
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public boolean isMarked() {
		return isMarked;
	}
	public void setMarked(boolean isMarked) {
		this.isMarked = isMarked;
	}
	public String getTitleMatched() {
		return titleMatched;
	}
	public void setTitleMatched(String titleMatched) {
		this.titleMatched = titleMatched;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	
	
}