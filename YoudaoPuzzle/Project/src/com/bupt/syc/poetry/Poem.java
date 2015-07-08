package com.bupt.syc.poetry;

public class Poem {
	int pid;
	String name;
	String author;
	String fulltext;
	String appreation;
	int priority;
	int ismarked;
	String type;
	String dynasty;
	
	public Poem() {
		super();
	}

	public Poem(int pid, String name, String author, String fulltext,
			String appreation, int priority, int ismarked, String type,
			String dynasty) {
		super();
		this.pid = pid;
		this.name = name;
		this.author = author;
		this.fulltext = fulltext;
		this.appreation = appreation;
		this.priority = priority;
		this.ismarked = ismarked;
		this.type = type;
		this.dynasty = dynasty;
	}

	public String getDynasty() {
		return dynasty;
	}

	public void setDynasty(String dynasty) {
		this.dynasty = dynasty;
	}

	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getFulltext() {
		return fulltext;
	}
	public void setFulltext(String fulltext) {
		this.fulltext = fulltext;
	}
	public String getAppreation() {
		return appreation;
	}
	public void setAppreation(String appreation) {
		this.appreation = appreation;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getIsmarked() {
		return ismarked;
	}
	public void setIsmarked(int ismarked) {
		this.ismarked = ismarked;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
