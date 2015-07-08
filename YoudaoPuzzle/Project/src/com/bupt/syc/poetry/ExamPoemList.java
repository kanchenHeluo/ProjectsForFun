package com.bupt.syc.poetry;


public class ExamPoemList {
	String[] poetrySentence;
	int omit;
	char[] words;
	String title;
	String author;
	private String openUse = "天上无云何时春日花相见水中有月一夜山风人不来圆绿秋悲欢思别鸟夜声鹊香空去中上时白尽心愁明红又关黑城闺楼女儿";
	private static final int charl = 100;
	private static final int charin = 12;
	public ExamPoemList() {
		super();
	}
	public String[] getPoetrySentence() {
		return poetrySentence;
	}
	public void setPoetrySentence(String[] poetrySentence) {
		this.poetrySentence = poetrySentence;
	}
	public int getOmit() {
		return omit;
	}
	public void setOmit(int omit) {
		this.omit = omit;
	}
	public char[] getWords() {
		return words;
	}
	public void setWords(char[] words) {
		this.words = words;
	}
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public boolean generate(Poem poem,int charnum){
		this.words = new char[charnum];
		String sentence[] = poem.getFulltext().split("。");
		int length = sentence.length;
		int senPos = (int) (Math.random() * 3331) % length;
		boolean result = true;
		int trytimes = 0;
		String temp[];
		do{
			trytimes ++;
			if(trytimes > 10)
				return false;
			senPos = (int) (Math.random() * 3331) % length;
			temp = sentence[senPos].split("，|？|、");
		}while(!(temp.length > 1 && temp.length < 5));
		
		if(temp.length < 4)
			this.poetrySentence = sentence[senPos].split("，|？|、").clone();
		else{
			int min = 1000,minpos = 0;
			for(int i = 0;i < temp.length-1;i++){
				if(temp[i].length() + temp[i+1].length() < min){
					min = temp[i].length() + temp[i+1].length();
					minpos = i;
				}
			}
			this.poetrySentence = new String[temp.length - 1];
			for(int i = 0,j = 0;i < temp.length;i++,j++){
				if(i != minpos){
					this.poetrySentence[j] = temp[i];
				}
				else{
					this.poetrySentence[j] = temp[i] + "，" + temp[i+1];
					i++;
				}
			}
		}		
		//this.poetrySentence = sentence[senPos].split("，");
		this.omit = (int) (Math.random() * 3331) % this.poetrySentence.length;
		this.author = poem.getAuthor();
		this.title = poem.getName();
		
		boolean ischoose[] = new boolean[length];
		ischoose[senPos] = true;
		int count = 0;
		boolean flag[] = new boolean[charl];
		char character[] = new char[charl];
		int radPos;
		for(int i = 0;i < this.poetrySentence[this.omit].length();i++){
			char tmp = this.poetrySentence[this.omit].charAt(i);
			do{
				radPos = (int)(Math.random() * 3331) % charl;
			}while(flag[radPos]);
			flag[radPos] = true;
			character[radPos] = tmp;
			count++;
		}
		
		//while(count < charnum){
			int radSen;
			if(length > 1){
				do{
					radSen = (int) (Math.random() * 3331) % length;
				}while(ischoose[radSen]);
				temp = sentence[radSen].split("，");
				int senBeginPos = (int) (Math.random() * 3331) % temp.length;
				for(int i = 0;count < charin && i < temp.length;i+=2){
					int curSenpos = (senBeginPos + i) % temp.length;
					int charBeginPos = (int) (Math.random() * 3331) % temp[curSenpos].length();
					for(int j = 0;count < charin && j < temp[curSenpos].length();j+=2){
						int curCharPos = (j + charBeginPos)% temp[curSenpos].length();
						char tempChar = temp[curSenpos].charAt(curCharPos);
						do{
							radPos = (int)(Math.random() * 3331) % charl;
						}while(flag[radPos]);
						flag[radPos] = true;
						character[radPos] = tempChar;
						count++;					
					}
				}
			}
			while(count < charnum){
				do{
					radPos = (int)(Math.random() * 3331) % charl;
				}while(flag[radPos]);
				radSen = (int) (Math.random() * 3331) % this.openUse.length();
				flag[radPos] = true;
				character[radPos] = this.openUse.charAt(radSen);
				count++;
			}
		//}
		int i = 0;
		while(i < charnum){
			for(int j = 0;j < charl;j++){
				if(flag[j]){
					this.words[i++] = character[j];
				if(i == charnum)
					break;
				}
			}
		}
		return result;
	}
}
