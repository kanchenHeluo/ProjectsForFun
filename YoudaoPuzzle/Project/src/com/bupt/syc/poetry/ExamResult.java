package com.bupt.syc.poetry;

import java.util.List;


public class ExamResult {
	int score;
	int amount;
	ExamPoemList[] examPoemList;
	String[] ansList;
	boolean isCorrect[];

 
	public ExamResult() {
		super();
		this.score = 0;
	}

	public void makeExamList(ExamCondition condition,DB db){
		if(0 != condition.getAmount())
		    this.amount = condition.getAmount();
		else 
			this.amount = 10;
		examPoemList = new ExamPoemList[amount];
		ansList = new String[amount];
		isCorrect = new boolean[amount];
		int[] choosedPoemIds = new int[amount];
		int radnum;
		int count = 0;
		List<StudyPoemList> plist = db.getPoemList(condition);
		//db.close();
		count = 0;
		if(plist.size() < amount){
			for(StudyPoemList x:plist){
				examPoemList[count] = new ExamPoemList();
				Poem poem = db.getPoem(x.getpId());
				if(examPoemList[count].generate(poem, 16)){
					//ansList[count] = examPoemList[count].getPoetrySentence()[examPoemList[count].getOmit()];
					choosedPoemIds[count] = x.getpId();
					count++;
				}
			}
		}
		else{
			while(count < amount && plist.size() > 0){
				radnum = (int)(Math.random() * 20101) % plist.size();
				examPoemList[count] = new ExamPoemList();
				Poem poem = db.getPoem(plist.get(radnum).getpId());
				plist.remove(radnum);
				if(examPoemList[count].generate(poem, 16)){
					//ansList[i] = examPoemList[i].getPoetrySentence()[examPoemList[i].getOmit()];
					choosedPoemIds[count] = poem.getPid();		
					count++;
				}
			}			
		}
		if(count < amount){
			//db = new DB();
			//studyCondition condition1 = new studyCondition(null, null, null, false, null, null);
			plist = db.getPoemList(1,3);
			while(count < amount){
				//do{
					radnum = (int)(Math.random() * 20101) % plist.size();
					
				//}while(this.findExist(plist.get(radnum).getpId(),choosedPoemIds,count));
				Poem poem = db.getPoem(plist.get(radnum).getpId());
				plist.remove(radnum);
				if(!this.findExist(poem.getPid(),choosedPoemIds,count)){
					examPoemList[count] = new ExamPoemList();
					if(examPoemList[count].generate(poem, 16)){
						//ansList[i] = examPoemList[i].getPoetrySentence()[examPoemList[i].getOmit()];
						choosedPoemIds[count] = poem.getPid();		
						count++;
					}
				}
			}
		}
		db.close();
	}
	
	public int judge(int scorePerQues){
		this.score = 0;
		for(int i = 0;i < this.amount;i++){
			if(ansList[i] != null){
				if(this.ansList[i].compareTo(this.getExamPoemList()[i].getPoetrySentence()[this.getExamPoemList()[i].getOmit()]) == 0){
					this.score += scorePerQues;
					isCorrect[i] = true;
				}
				else
					isCorrect[i] = false;
			}
			else
				isCorrect[i] = false;
		}	
		return this.score;
	}
	
	private boolean findExist(int pId,int[] existPid,int curnum){
		boolean result = false;
		for(int i = 0;i < curnum;i++){
			if(pId == existPid[i])
				return true;
		}
		return result;
	}

	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public ExamPoemList[] getExamPoemList() {
		return examPoemList;
	}

	public void setExamPoemList(ExamPoemList[] examPoemList) {
		this.examPoemList = examPoemList;
	}

	public String[] getAnsList() {
		return ansList;
	}

	public void setAnsList(String[] ansList) {
		this.ansList = ansList;
	}

	public boolean[] getIsCorrect() {
		return isCorrect;
	}
		
}
