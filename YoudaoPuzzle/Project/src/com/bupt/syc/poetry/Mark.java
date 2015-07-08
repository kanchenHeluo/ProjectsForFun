package com.bupt.syc.poetry;

public class Mark {
	   String point;
	   String str;
	   int start_x;
	   int start_y;
	   int len;
	   
	   
	public Mark() {
		super();
	}
	public Mark(String point, String str, int start_x, int start_y, int len) {
		super();
		this.point = point;
		this.str = str;
		this.start_x = start_x;
		this.start_y = start_y;
		this.len = len;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public int getStart_x() {
		return start_x;
	}
	public void setStart_x(int start_x) {
		this.start_x = start_x;
	}
	public int getStart_y() {
		return start_y;
	}
	public void setStart_y(int start_y) {
		this.start_y = start_y;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}	
	   
	   
}
