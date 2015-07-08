package com.bupt.syc.poetry;

import java.util.List;

import android.text.format.Time;


public class Game1 {
	public static final int RecordNum = 10;
	private static final int maxCount = 1000;
	private static final int scorePerQues = 20;
	private static final int ADDBOMBQues = 10;
	private static final int MAXBOMBNUM = 3;
	//private static final int addedTimePerQues = 20;
	//public static final int maxQusNum = 20;
	Game1Result glr;
	Game1PoemList[] questions;
	String[] answer;
	int bombNum;
	private int[] ques = new int[maxCount];
	private int totalCount = 0;
	private int doneQues = 0;
	private int amount;
	private int lastTimeRightAnsNum;
	private int bombAddNum = 0;
	int beginpri = 1;
	int endpri = 1;
	public Game1() {
		super();
		glr = new Game1Result();
		this.bombNum = 3;
	}
	
	public void getPoemList(Game1Condition ec){
		this.makeExamList(ec);
		this.answer = new String[ec.getAmount()];
	}
	
	public Game1PoemList getPoemList(int questionId){
		return this.questions[questionId];
	}
	
	public void setAnswer(int questionId, String ans){
		if(ans != null && !ans.equals(""))
			this.answer[questionId] = ans;
	}
	
	public int game1Judge(){
		this.lastTimeRightAnsNum = 0;
		for(int i = 0;i < this.amount;i++){
			//if(this.answer[i].compareTo(this.questions[i].getPoetrySentence()[this.questions[i].getOmit()]) == 0){
			if(!(this.answer[i] == null ) && this.questions[i].getPoetrySentence()[this.questions[i].getOmit()].compareTo(this.answer[i]) == 0){
				this.lastTimeRightAnsNum++;
				this.glr.score += scorePerQues;
				this.bombAddNum ++;
			}
			else{
				Game1PoemList temp = new Game1PoemList(this.questions[i]);
				this.glr.getRightAnswer().add(temp);
				this.glr.getWrongAnswer().add(this.answer[i]);
			}
		}
		if(this.bombAddNum >= this.ADDBOMBQues){
			if(this.bombNum < MAXBOMBNUM){
				this.bombNum ++;
				this.bombAddNum -= ADDBOMBQues;
			}
			else
				this.bombAddNum = ADDBOMBQues/2;
		}
		this.doneQues = this.totalCount;
		return this.lastTimeRightAnsNum;
	}
	
	public int game1TimeCnt(int level,int amount){
		return this.gettime(level) * amount;
	}
	
	public void game1TopRecord(String name, int score){
        Time time = new Time("GMT+8");    
        time.setToNow();		
        String curtime = this.changeTime(time);
        if(name == null || name.equals(""))
            name = "anonymous";
        DB db = new DB();
        db.insertRecord(name, score, curtime);
	}
	
	public  Game1Score[] game1ShowTop(int range){
		DB db = new DB();
		Game1Score[] game1Score = null;
		int size;
		List<Game1Score> topScore= db.getTopScore();
		if(0 == range){
			size = topScore.size() > RecordNum?RecordNum:topScore.size();
			game1Score = new Game1Score[size];
			for(int i = 0;i < size;i++){
				game1Score[i] = topScore.get(i);
			}
		}
		else{
			size = topScore.size() > RecordNum?topScore.size() - RecordNum:0;
			game1Score = new Game1Score[size];
			for(int i = 0;i < topScore.size() - RecordNum;i++){
				game1Score[i] = topScore.get(i + RecordNum);
			}
		}			
		return game1Score;
	}
	
	private void makeExamList(Game1Condition condition){
		this.amount = condition.getAmount();
		this.questions = new Game1PoemList[amount];
		this.answer = new String[amount];
		int radnum;
		int count = 0;
		this.setpri(condition.getLevel());
		DB db = new DB();
		List<StudyPoemList> plist = db.getPoemList(beginpri,endpri);
		count = 0;
		if(plist.size() < amount){
			for(StudyPoemList x:plist){
				if(!this.findExist(x.getpId(),ques,totalCount)){
					questions[count] = new Game1PoemList();
					Poem poem = db.getPoem(x.getpId());
					if(questions[count].generate(poem, 16)){
						//answer[count] = questions[count].getPoetrySentence()[questions[count].getOmit()];
						ques[totalCount++] = x.getpId();
						questions[count].setSeq(totalCount);
						count++;
					}
				}
			}
		}
		else{
			while(count < amount && plist.size() > 0){
				radnum = (int)(Math.random() * 20101) % plist.size();
				questions[count] = new Game1PoemList();
				Poem poem = db.getPoem(plist.get(radnum).getpId());
				plist.remove(radnum);
				if(questions[count].generate(poem, 16)){
					//answer[count] = questions[count].getPoetrySentence()[questions[count].getOmit()];
					ques[totalCount++] = poem.getPid();
					questions[count].setSeq(totalCount);
					count++;
				}
			}			
		}
		if(count < amount){
			plist = db.getPoemList(1,5);
			while(count < amount){
				radnum = (int)(Math.random() * 20101) % plist.size();
				questions[count] = new Game1PoemList();
				Poem poem = db.getPoem(plist.get(radnum).getpId());
				if(!this.findExist(plist.get(radnum).getpId(),ques,totalCount)){
					if(questions[count].generate(poem, 16)){
						//answer[count] = questions[count].getPoetrySentence()[questions[count].getOmit()];
						ques[totalCount++] = poem.getPid();
						questions[count].setSeq(totalCount);
						count++;
					}		
				}
			}
		}
		
	}
	
	private boolean findExist(int pId,int[] existPid,int curnum){
		boolean result = false;
		for(int i = 0;i < curnum;i++){
			if(pId == existPid[i])
				return true;
		}
		return result;
	}
	
	private void setpri(int level){
		switch(level){
		case 1:
			this.beginpri = 1;
			this.endpri = 1;
			break;
		case 2:
			this.beginpri = 1;
			this.endpri = 2;
			break;
		case 3:
			this.beginpri = 1;
			this.endpri = 3;
			break;
		case 4:
			this.beginpri = 2;
			this.endpri = 2;
			break;
		case 5:
			this.beginpri = 2;
			this.endpri = 3;
			break;
		case 6:
			this.beginpri = 3;
			this.endpri = 3;
			break;
		case 7:
			this.beginpri = 2;
			this.endpri = 4;
			break;
		default:
			this.beginpri = 3;
			this.endpri = 5;
			break;			
		}
	}

	private int gettime(int level){
		switch(level){
		case 0:
		case 1:
			return 20;
		case 2:
			return 17;
		case 3:
			return 15;
		case 4:
			return 13;
		case 5:
			return 10;
		case 6:
			return 9;
		case 7:
			return 8;
		default:
			return 7;		
		}
	}
	
	private String changeTime(Time time){
		String result = "";
		result = time.year + "-" + (time.month + 1)+ "-" + time.monthDay + "  ";
		result += ((time.hour + 8) % 24) + ":" + time.minute + ":" + time.second;
		return result;
	}

	public int getAmount() {
		// TODO Auto-generated method stub
		return this.amount;
	}

	public String getViewAnswer(int questionId) {
		// TODO Auto-generated method stub
		return this.answer[questionId];
	}
	
	public int getTotalcount(){
		return this.totalCount;
	}
	
	public int getDoneQues(){
		int doneQues = this.totalCount;
		for(String x:answer){
			if(x == null || x.equals(""))
				doneQues --;
		}
		return doneQues;
	}
	
	public int getBombNum(){
		return this.bombNum;
	}
	
	public boolean useBomb(){
		if(this.bombNum > 0){
			this.bombNum --;
			return true;
		}
		else
			return false;
	}
}
