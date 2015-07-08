package com.bupt.syc.poetry;

public class StudyPoem {
	 String[] poems;
	 String author;
	 String title;
	 String Appreciation;
	 
	 
	public StudyPoem() {
		super();
	}
	public StudyPoem(String[] poems, String author, String title,
			String appreciation) {
		super();
		this.poems = poems;
		this.author = author;
		this.title = title;
		Appreciation = appreciation;
	}
	public String[] getPoems() {
		return poems;
	}
	public void setPoems(String[] poems) {
		this.poems = poems;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAppreciation() {
		return Appreciation;
	}
	public void setAppreciation(String appreciation) {
		Appreciation = "";
		if(appreciation != null && !appreciation.equals("")){
			if(appreciation.indexOf("\n\r") >= 0){
				String[] temp = appreciation.split("\n\r");
				for(String x:temp){
					Appreciation += (x + "\n");
				}
			}
			else if(appreciation.indexOf("\r\n") >= 0){
				String[] temp = appreciation.split("\r\n");
				for(String x:temp){
					Appreciation += (x + "\n");
				}
			}
			else if(appreciation.indexOf("\\n") >= 0){
				String[] temp = appreciation.split("\\n");
				for(String x:temp){
					Appreciation += (x + "\n");
				}
			}
			else if(appreciation.indexOf("\n") >= 0){
				String[] temp = appreciation.split("\n");
				for(String x:temp){
					Appreciation += (x + "\n");
				}
			}
			else if(appreciation.indexOf("/n/r") >= 0){
				String[] temp = appreciation.split("/n/r");
				for(String x:temp){
					Appreciation += (x + "\n");
				}
			}
			else if(appreciation.indexOf("/r/n") >= 0){
				String[] temp = appreciation.split("/r/n");
				for(String x:temp){
					Appreciation += (x + "\n");
				}
			}
			else if(appreciation.indexOf("/n") >= 0){
				String[] temp = appreciation.split("/n");
				for(String x:temp){
					Appreciation += (x + "\n");
				}
			}
			else
				Appreciation = appreciation;
		}
		else
			Appreciation = "тщнчимнЖ";
	}
	 
	 
	 
}
