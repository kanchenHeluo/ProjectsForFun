package com.bupt.syc.poetry;

import java.util.HashSet;
import java.util.List;

public class Exam1 {
	ExamResult er;
	private static final int totalScore = 100;
	private int scorePerQues;
	private static DB db;
	public Exam1() {
		super();
		db = new DB();
		er = new ExamResult();
	}


	public void getPoemList(ExamCondition ec){
		er.makeExamList(ec,Exam1.db);
		scorePerQues = totalScore/er.getAmount();
	}
	
	public int getAmount(){
		return this.er.getAmount();
	}
	
	public ExamPoemList getPoemList(int questionId){
		return er.getExamPoemList()[questionId];
	}
	
	public void setViewAnswer(int questionId, String ans){
		if(ans != null && !ans.equals(""))
			er.getAnsList()[questionId] = ans;
	}

	public String getViewAnswer(int questionId){
		return er.getAnsList()[questionId];
	}
	 public int ExamJudge(){ //еп╬М 
		 er.judge(scorePerQues);
		 return er.score;
	 }
	 
	 public ExamResult returnAnser(){
		 return this.er;
	 }
	 
	 public List<String> getAuthorList(String[] dynasty){
		 return db.getAuthorList(dynasty);
	 }

	 public List<String> getAuthorList(String[] dynasty,boolean isMarked){
		 return db.getAuthorList(dynasty,isMarked);
	 }
	 
	 public List<String> getStyleList(){
		 return db.getStyleList();
	 }
	 
	 public List<String> getStyleList(String[] dynasty,boolean isMarked){
		 return db.getStyleList(dynasty,isMarked);
	 }
	 
	 public List<String> getStyleList(HashSet<String> hashSet,boolean isMarked){
		 String[] dynasty = hashSet.toArray(new String[0]);
		 return getStyleList(dynasty,isMarked);
	 }
	 
	 public List<String> getAuthorList(HashSet<String> hashSet,boolean isMarked){
		 String[] dynasty = hashSet.toArray(new String[0]);
		 return getAuthorList(dynasty,isMarked);
	 }
}
