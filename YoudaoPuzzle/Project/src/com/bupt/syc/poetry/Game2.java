package com.bupt.syc.poetry;

import java.util.ArrayList;
import java.util.List;

public class Game2 {
	public static final int WIDTH = 12;
	public static final int HEIGHT = 12;
	private static final int NUM = 60;
	
    Game2Result g2r;    
    ArrayList<Mark> horizon;
    ArrayList<Mark> vertical;	
    int totalCount;
    int count;
    int correntnum;
    
    void generate(){
    	horizon = new ArrayList<Mark>();
    	vertical = new ArrayList<Mark>();
    	MakeTianzi makeTianzi= new MakeTianzi(Game2.HEIGHT,Game2.WIDTH,Game2.NUM);
    	makeTianzi.generate();
    	g2r = new Game2Result(HEIGHT,WIDTH);
    	
    	for(int i = 0;i < HEIGHT;i++){
    		for(int j = 0;j < WIDTH;j++){
    			g2r.init[i][j] = makeTianzi.flag[i][j];
    			g2r.matrix[i][j] = makeTianzi.matrix[i][j];
    			if(!g2r.init[i][j])
    			    g2r.result[i][j] = '　';
    			else
    				g2r.result[i][j] = '　';
    		}
    	}
    	
    	count = makeTianzi.count;
    	totalCount = makeTianzi.totalcount;
    	for(int i = 0;i < makeTianzi.hengpos.size();i++){
    		//if(makeTianzi.hengpos.get(i).getMarkpos() < makeTianzi.heng.size()){
	    		Mark temp = new Mark();
	    		Sentence x = makeTianzi.heng.get(makeTianzi.hengpos.get(i).getMarkpos());
	    		temp.setStr(x.getSen());
	    		temp.setStart_x(makeTianzi.hengpos.get(i).getX());
	    		temp.setStart_y(makeTianzi.hengpos.get(i).getY());
	    		temp.setLen(temp.getStr().length());
	    		temp.setPoint(makeTianzi.getPoint(temp.getStr()));
	    		this.horizon.add(temp);
    		//}
    	}
    	for(int i = 0;i < makeTianzi.shupos.size();i++){
    		//if(makeTianzi.shupos.get(i).getMarkpos() < makeTianzi.shu.size()){
    		Mark temp = new Mark();
    		temp.setStr(makeTianzi.shu.get(makeTianzi.shupos.get(i).getMarkpos()).getSen());
    		temp.setStart_x(makeTianzi.shupos.get(i).getX());
    		temp.setStart_y(makeTianzi.shupos.get(i).getY());
    		temp.setLen(temp.getStr().length());
    		temp.setPoint(makeTianzi.getPoint(temp.getStr()));
    		this.vertical.add(temp);
    		//}
    	}
    }
    
    public boolean[][] getMatrix(){
    	return this.g2r.init;
    }
    
    public String[] getPoint(int x, int y){
    	String[] result = new String[2];
    	result[0] = new String("");
    	result[1] = new String("");
    	for(int i =0;i < horizon.size();i++){
    		Mark mark = horizon.get(i);
    		if(mark.getStart_x() == x && mark.getStart_y() <= y && (mark.getStart_y() + mark.getLen()) > y){
    			result[0] =  mark.getPoint();
    			break;
    		}
    	}
    	for(int i =0;i < vertical.size();i++){
    		Mark mark = vertical.get(i);
    		if(mark.getStart_y() == y && mark.getStart_x() <= x && (mark.getStart_x() + mark.getLen()) > x){
    			result[1] =  mark.getPoint();
    			break;
    		}
    	}
    	return result;
    }
    
    public char[][] fillBlanks(String words, int x, int y){
    	int i;
    	Mark mark = null;
    	for(i =0;i < horizon.size();i++){
    		mark = horizon.get(i);
    		if(mark.getStart_x() == x && mark.getStart_y() <= y && (mark.getStart_y() + mark.getLen()) > y){
    			break;
    		}
    	}    	
    	if(i < horizon.size()){
    		int cx = mark.getStart_x();
    		int cy = mark.getStart_y();
    		for(i = 0;i < mark.getLen();i++){
    			if(i < words.length()){
    				this.g2r.result[cx][cy+i] = words.charAt(i);
    				if(this.g2r.result[cx][cy+i]== ' '){
    					this.g2r.result[cx][cy+i] = '　';
    				}
    			}
    			else
    				this.g2r.result[cx][cy+i] = '　';
    		}
    		return g2r.result;
    	}
    	for(i =0;i < vertical.size();i++){
    		mark = vertical.get(i);
    		if(mark.getStart_y() == y && mark.getStart_x() <= x && (mark.getStart_x() + mark.getLen()) > x){
    			break;
    		}
    	}    	
    	if(i < vertical.size()){
    		int cx = mark.getStart_x();
    		int cy = mark.getStart_y();
    		for(i = 0;i < mark.getLen();i++){
    			if(i < words.length()){
    				this.g2r.result[cx+i][cy] = words.charAt(i);
    				if(this.g2r.result[cx+i][cy] == ' '){
    					this.g2r.result[cx+i][cy] = '　';
    				}
    			}
    			else
    				this.g2r.result[cx+i][cy] = '　';
    		}
    		return g2r.result;
    	}
    	return g2r.result;
    }
    
    public int game2Judge(){
    	correntnum = 0;
    	for(int i =0;i < HEIGHT;i++){
    		for(int j = 0;j < WIDTH;j++){
    			if(g2r.init[i][j]){
    				if(g2r.result[i][j] == g2r.matrix[i][j]){
    					correntnum++;
    					g2r.mark[i][j] = Game2Result.FILL_CORRECT;
    				} 
    				else if(g2r.result[i][j] == '　'){
    					g2r.mark[i][j] = Game2Result.NOT_FILL;
    				}
    				else
    					g2r.mark[i][j] = Game2Result.FILL_WRONG;
    			}
    		}
    	}
    	return (int)((double)correntnum/(double)count);
    }
    
    public Game2Result game2ShowAns(){
    	return this.g2r;
    }
    
    public String getCurResult(int x,int y){
    	String result = "";
    	char[] res;
    	boolean isFind = false;
    	for(int i =0;i < horizon.size();i++){
    		Mark mark = horizon.get(i);
    		if(mark.getStart_x() == x && mark.getStart_y() <= y && (mark.getStart_y() + mark.getLen()) > y){
    			res = new char[mark.getLen()];
    			for(int j = mark.getStart_y();j < mark.getStart_y() + mark.getLen();j++){
    				res[j-mark.getStart_y()] = this.g2r.result[mark.getStart_x()][j];
    			}
    			result = String.copyValueOf(res);
    			isFind = true;
    			break;
    		}
    	}
    	for(int i =0;i < vertical.size();i++){
    		Mark mark = vertical.get(i);
    		if(mark.getStart_y() == y && mark.getStart_x() <= x && (mark.getStart_x() + mark.getLen()) > x){
    			if(!isFind){
        			res = new char[mark.getLen()];
        			for(int j = mark.getStart_x();j < mark.getStart_x() + mark.getLen();j++){
        				res[j-mark.getStart_x()] = this.g2r.result[j][mark.getStart_y()];
        			}    
        			result = String.copyValueOf(res);
    			}
    			else
    				result = "";
    			break;
    		}
    	}
    	int i;
    	for(i = 0;i < result.length();i++){
    		if(result.charAt(i) != '　'){
    			break;
    		}
    	}
    	if(i == result.length())
    		result = "";
    	return result;
    }
}

class MakeTianzi{
	int height;
	int width;
	int num;
	char matrix[][];
	List<Sentence> heng;
	List<Sentence> shu;
	boolean flag[][];
	boolean check[][];
	int modes[][];
	List<Pos> posList;
	int count;
	int totalcount;
	List<Pos> hengpos;
	List<Pos> shupos;	
	DB db;
	Storage storage;
	private static int MAXBEYONDTHREENUM = 2;
	int currentBeyondThreeNum;
	
	public MakeTianzi(int height2, int width2, int num2) {
		super();
		this.height = height2;
		this.width = width2;
		this.num = num2;
		this.matrix= new char[height][width];
		this.flag = new boolean[height][width];
		this.check = new boolean[height][width];
		this.modes = new int[height][width];
		for(int i = 0;i < height;i++)
		{
			for(int j = 0;j < width;j++)
			{
				matrix[i][j] = '　';
				flag[i][j] = false;
				modes[i][j] = 0;
				check[i][j] = false;
			}
		}
		heng = new ArrayList<Sentence>();
		shu = new ArrayList<Sentence>();
		posList = new ArrayList<Pos>(width * height);
		hengpos = new ArrayList<Pos>();
		shupos = new ArrayList<Pos>();
		count = 0;
		db = new DB();
		storage = new Storage();
	}	
	
	public void clear()
	{
		for(int i = 0;i < height;i++)
		{
			for(int j = 0;j < width;j++)
			{
				matrix[i][j] = '　';
				flag[i][j] = false;
				modes[i][j] = 0;
				check[i][j] = false;
			}
		}	
		count = 0;
		totalcount = 0;
		heng.clear();
		shu.clear();
		hengpos.clear();
		shupos.clear();
		currentBeyondThreeNum = 0;
	}
	
	public void generate(){
		int trytimes = 0;
		curBest curbest = new curBest(height,width);
		do{
			this.clear();
			this.makematrix();
			if(trytimes == 0 || this.heng.size() + this.shu.size() - (this.totalcount - this.count) < curbest.heng.size() + curbest.shu.size() - (curbest.totalcount - curbest.count) || (this.heng.size() + this.shu.size() - (this.totalcount - this.count) == curbest.heng.size() + curbest.shu.size() - (curbest.totalcount - curbest.count)) && this.count > curbest.count){
		    	for(int i = 0;i < height;i++){
		    		for(int j = 0;j < width;j++){
		    			curbest.matrix[i][j] = this.matrix[i][j];
		    			curbest.flag[i][j] = this.flag[i][j];
		    		}
		    	}		
			}
			//trytimes++;
		}while((this.count < 55 || this.shu.size() < 3 || this.heng.size() + this.shu.size() - (this.totalcount - this.count) > 3));	
	}
	
	
	public void makematrix()
	{
		Pos cpos;
		int randnum;
		int randx,randy;
		int trytimes = 0;
		int trytime;
		make(0,0,modes[0][0]);
		
		while(trytimes < 100 && count < num)
		{
			trytimes ++;
			while(!posList.isEmpty())
			{
//				if(count >= num)
//					break;
				randnum = (int) (Math.random() * 20000) % posList.size();
				cpos = posList.get(randnum);
				posList.remove(randnum);
				make(cpos.getX(),cpos.getY(),modes[cpos.getX()][cpos.getY()]);
			}
			if(count < num)
			{
				trytime = 0;
				while(true)
				{
					randx = (int) (Math.random() * 20000) % height;
					randy = (int) (Math.random() * 20000) % width;	
					if(!flag[randx][randy] && !check[randx][randy])
					{
						//check[randx][randy] = true;
						int temp = (int) (Math.random() * 20000) % 2 + 1;
						if(make(randx,randy,temp))
							break;
						if(make(randx,randy, (temp) % 2 + 1));
							break;
					}
					//trytime++;
				}
			}
		}
		
	}	
	private boolean checkPri(int pri){
		boolean result = true;
		if(pri > 2){
			if(currentBeyondThreeNum > MAXBEYONDTHREENUM)
				result = false;
		}
		return result;
	}
	
	private void addPri(int pri){
		if(pri > 2){
			currentBeyondThreeNum++;
		}		
	}
	public boolean make(int x,int y,int mode)
	{
		boolean result = false;
		int beginpos;
		Sentence[] temp;
		if(flag[x][y])
		{
			//temp = find(matrix[x][y]);
			temp = storage.find(matrix[x][y]);
		}
		else
		{
			//temp = init();
			temp = storage.begin();
		}
		for(Sentence m:temp)
		{
			if(m == null)
				break;
			if(checkPri(m.getPri()) && checkexist(m.getSen()))
			{
				if(mode != 1)
				{
					result = checkheng(x,y - m.getPos(),m);
					if(result)
					{
						heng.add(m);
						mode = 1;
						beginpos = y - m.getPos();
						this.totalcount += m.getLength();
						Pos tpos = new Pos(heng.size() - 1,x,beginpos);
						hengpos.add(tpos);
						addPri(m.getPri());
						for(int i = 0;i < m.getLength();i++)
						{
							if(!flag[x][beginpos +i])
							{
								flag[x][beginpos +i] = true;
								matrix[x][beginpos +i] = m.getSen().charAt(i);
								count++;
								posList.add(new Pos(x,beginpos +i));
								modes[x][y] = mode;
							}
						}
						break;
					}
				}
				else if(mode != 2)
				{
					result = checklie(x - m.getPos(),y,m);
					if(result)
					{
						shu.add(m);
						beginpos = x - m.getPos();
						mode = 2;
						this.totalcount += m.getLength();
						Pos tpos = new Pos(shu.size() - 1,beginpos,y);
						shupos.add(tpos);
						addPri(m.getPri());
						for(int i = 0;i < m.getLength();i++)
						{
							if(!flag[beginpos + i][y])
							{
								flag[beginpos + i][y] = true;
								matrix[beginpos + i][y] = m.getSen().charAt(i);
								count++;
								posList.add(new Pos(beginpos + i,y));
								modes[x][y] = mode;
							}
						}
						break;
					}					
				}
			}
		}
		return result;
	}		

		public boolean checkheng(int x,int y,Sentence key)
		{
			boolean result = true;
			if(y < 0 || y > 0 && flag[x][y-1] || y + key.getLength() > width)
				return false;
			for(int i = 0;i < key.getLength();i++)
			{
				if(flag[x][y+i] && matrix[x][y+i] != key.getSen().charAt(i) || !flag[x][y+i] && (x > 0 && flag[x-1][y+i] || x < height -1  && flag[x+1][y+i]))
				    return false;
			}
			if(y + key.getLength() < width && flag[x][y+key.getLength()])
				return false;
			return result;		
		}
		
		public boolean checklie(int x,int y,Sentence key)
		{
			boolean result = true;
			if(x < 0 || x > 0 && flag[x-1][y] || x + key.getLength() > height)
				return false;
			for(int i = 0;i < key.getLength();i++)
			{
				if(flag[x+i][y] && matrix[x+i][y] != key.getSen().charAt(i) || !flag[x+i][y] && (y > 0 && flag[x+i][y-1] || y < width -1  && flag[x+i][y+1]))
				    return false;
			}
			if(x + key.getLength() < height && flag[x+key.getLength()][y])
				return false;
			return result;		
		}
		
		public boolean checkexist(String key)
		{
			boolean result = true;
			for(Sentence x:heng)
			{
				if(key.compareTo(x.getSen()) == 0)
					return false;
			}
			for(Sentence x:shu)
			{
				if(key.compareTo(x.getSen()) == 0)
					return false;
			}
			return result;
		}
		
//		Sentence[] find(char key){
//			Sentence[] result = new Sentence[MAXDESNUM];
//			int[] choosedPoemIds = new int[MAXDESNUM];
//			int count = 0;
//			int radnum = 0;
//			List<StudyPoemList> plist = db.getPoemList(1,2,key);
//			if(plist.size() > 0){
//				radnum = (int)(Math.random() * 20101) % plist.size();
//				if(plist.size() < SHIJUNUM){
//					for(StudyPoemList x:plist){
//						result[count] = new Sentence();
//						Poem poem = db.getPoem(x.getpId());
//						result[count].generate(poem,key);
//						//ansList[count] = examPoemList[count].getPoetrySentence()[examPoemList[count].getOmit()];
//						choosedPoemIds[count] = x.getpId();
//						count++;
//					}
//				}
//				else{
//					for(int i = count;i < SHIJUNUM;i++){
//						do{
//							radnum = (int)(Math.random() * 20101) % plist.size();
//						}while(this.findExist(plist.get(radnum).getpId(),choosedPoemIds,i));
//						result[i] = new Sentence();
//						Poem poem = db.getPoem(plist.get(radnum).getpId());
//						result[i].generate(poem,key);
//						//ansList[i] = examPoemList[i].getPoetrySentence()[examPoemList[i].getOmit()];
//						choosedPoemIds[i] = plist.get(radnum).getpId();		
//						count++;
//					}			
//				}			
//			}
//			List<String> alist = db.getAuthorList(key);
//			for(int i = 0;i < alist.size() && i < AUTHORNUM ;i++){
//				radnum = (int)(Math.random() * 20101) % alist.size();
//				String strTemp = alist.get(radnum);
//				alist.remove(radnum);
//				result[count] = new Sentence();
//				result[count].setSen(strTemp);
//				result[count].setLength(strTemp.length());
//				result[count].setPos(strTemp.indexOf(key));
//				result[count].setType(Sentence.AUTHOR);
//				count ++;
//			}
//			List<String> nlist = db.getAuthorList(key);
//			for(int i = 0;i < nlist.size() && i < NAMENUM ;i++){
//				radnum = (int)(Math.random() * 20101) % nlist.size();
//				String strTemp = nlist.get(radnum);
//				nlist.remove(radnum);
//				result[count] = new Sentence();
//				result[count].setSen(strTemp);
//				result[count].setLength(strTemp.length());
//				result[count].setPos(strTemp.indexOf(key));
//				result[count].setType(Sentence.AUTHOR);
//				count ++;
//			}
//			return result;
//		}
//		
//		Sentence[] init(){
//			Sentence[] result = new Sentence[MAXDESNUM];
//			List<StudyPoemList> plist = db.getPoemList(1,1);
//			int radnunm = (int)(Math.random() * 20101) % plist.size();
//			int senRadnum;
//			Poem poem;
//			String[] poemSens;
//			String[] senParts;
//			for(int i = 0;i < MAXDESNUM;i++){
//				Sentence temp = new Sentence();
//				int curpos = (i + radnunm) % plist.size();
//				poem = db.getPoem(plist.get(curpos).getpId());
//				poemSens = poem.getFulltext().split("。");
//				senRadnum = (int)(Math.random() * 20101) %(poemSens.length);
//				senParts = poemSens[senRadnum].split("，");
//				senRadnum = (int)(Math.random() * 20101) % (senParts.length);
//				temp.setSen(senParts[senRadnum]);
//				temp.setLength(senParts[senRadnum].length());
//				temp.setPos(0);
//				temp.setType(Sentence.SHIJU);
//				result[i] = temp;
//			}
//			return result;
//		}
		
		private boolean findExist(int pId,int[] existPid,int curnum){
			boolean result = false;
			for(int i = 0;i < curnum;i++){
				if(pId == existPid[i])
					return true;
			}
			return result;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getNum() {
			return num;
		}

		public void setNum(int num) {
			this.num = num;
		}
		
		String getPoint(String sen){
			List<String> desList = db.getDescribe(sen);
			Poem poem;
			if(desList.size() > 0){
				int radnum = (int)(Math.random() * 20101) %(desList.size());
				return desList.get(radnum);
			}
			shiren author = db.getShiren(sen);
			if(author != null){
				return genDescribe(author);
			}
			List<StudyPoemList> alist = db.getPoemList(sen);
			if(alist.size() > 0){
				return genDescribe(alist);
			}
			poem = db.getPoemByName(sen);
			if(poem != null){
				return genDescribe(poem);
			}
			poem = db.getPoemByPeomsen(sen);
			int i,j = 0;
			boolean isFind = false;
			String senTemp[];
			String senParts[] = null;
			if(poem != null){
				senTemp = poem.getFulltext().split("。");
				for(i = 0;!isFind && i < senTemp.length;i++){
					if(senTemp[i].indexOf(sen) > -1){
						senParts = senTemp[i].split("，|？|、");
						for(j = 0;j < senParts.length;j++){
							if(senParts[j].compareTo(sen) == 0){
								isFind = true;
								break;
							}
						}
					}
				}
				return genDescribe(poem.getName(),poem.getAuthor(),senTemp,senParts,--i,j);
			}

			return "^_^ 没有提示，自己更具前后文判断吧~~";
		}

		private String genDescribe(Poem poem) {
			// TODO Auto-generated method stub
			String sentence[] = poem.getFulltext().split("。");
			String str = "^_^ 没有提示，自己更具前后文判断吧~~";
			int senNum = (int)(Math.random() * 20101) %(sentence.length);
			int radnum = (int)(Math.random() * 20101) % 3;
			if(radnum < 2){
				if(radnum == 0){
				str = poem.getDynasty() + "代诗人" + poem.getAuthor() + "的作品";
				}
				else{
					str = poem.getDynasty() + "代诗人" + poem.getAuthor() + "的一首" + poem.getType() + "诗歌";
				}
				str += "，包含诗句“" +sentence[senNum] +"”";
			}
			else
			{
				str = "诗歌名，包含诗句“" + sentence[senNum] +"”";
			}
			return str;
		}

		private String genDescribe(List<StudyPoemList> alist) {
			// TODO Auto-generated method stub
			int num = 0;
			int radnum;
			String str = "^_^ 没有提示，自己更具前后文判断吧~~";
			str = db.getPoem(alist.get(0).pId).getDynasty() + "朝诗人，著有作品:";
			while(num < 3 && alist.size() > 0){
				radnum = (int)(Math.random() * 20101) % alist.size();
				StudyPoemList x = alist.get(radnum);
				str += "《" + x.getTitle() + "》、";
				alist.remove(radnum);
			}
			return str;
		}

		private String genDescribe(shiren author) {
			// TODO Auto-generated method stub
			String str = author.getChaodai() + "朝诗人,字"+ author.getZi();
			if(author.getHao() != null || !author.equals("")){
				str += ",号" + author.getHao();
			}
			return str;
		}

		private String genDescribe(String name, String author, String[] sentence,
				String[] sentenceParts, int pos1, int pos2) {
			// TODO Auto-generated method stub
			String str = "^_^ 没有提示，自己更具前后文判断吧~~";
			int radnum = (int)(Math.random() * 20101) % 30;
			if(radnum < 22){
				if(sentenceParts.length == 2){
					if(pos2 == 0){
						if(radnum < 12)
							str = "诗句“" + sentenceParts[1] + "”的上句";
						else
							str = "“" + sentenceParts[1] + "”前面是什么";
					}
					else{
						if(radnum < 12)
							str = "诗句“" + sentenceParts[0] + "”的下句";
						else
							str = "“" + sentenceParts[0] + "”接下来是什么";
					}
					return str;
				}
				else if(sentenceParts.length == 3){
					if(pos2 == 0){
						if(radnum < 12)
							str = "“" + sentenceParts[1] + "，" + sentenceParts[2]   + "”" + "的前一句"; 
						else
							str = "后续的诗句为" + "“" + sentenceParts[1] + "，" + sentenceParts[2]   + "”";
					}
					else if(pos2 == 1){
						if(radnum < 11)
							str = "在“" + sentenceParts[0] + "”和“" + sentenceParts[2]   + "”" + "中间的诗句"; 
						else
							str = "承接“" + sentenceParts[0] + "”和引出“" + sentenceParts[2]   + "”" + "诗句";
					}
					else{
						if(radnum < 12)
							str = "“" + sentenceParts[0] + "，" + sentenceParts[1]   + "”" + "的后一句"; 
						else
							str = "前面的诗句为" + "“" + sentenceParts[0] + "，" + sentenceParts[1]   + "”";					
					}
					return str;
				}
				else
					radnum = (int)(Math.random() * 20101) % 20;
			}
			if(radnum < 14){
				str = "出自"+author+"的《" + name + "》，";
				if(radnum  > 1 && pos1 < sentence.length - 1)
					str += "其下一句诗为:“" + sentence[pos1 + 1] + "”";
				else if(radnum < 8 && pos1 > 1)
					str += "其上一句诗为:“" + sentence[pos1 - 1] + "”";
				return str;
			}
			int i = 0;
			if(sentence.length == 4){
				for(i = 0;i < 4;i++){
					if(sentence[i].split("，").length != 2)
						break;
					else{
						String[] temp1 = sentence[i].split("，");
						if(temp1[0].length() != temp1[1].length() || temp1[0].length() != (5|7))
							break;
					}
				}
			}
			int posRad = (int)(Math.random() * 20101) % sentence.length;
			while(posRad == pos1)
				posRad = (int)(Math.random() * 20101) % sentence.length;
			if(i == 4){
				String[] pos = new String[]{"首","颔","颈","尾"};
				str = author + "《" + name + "》中的" + pos[(pos1)] + "联";
				str += "，该诗的"+ pos[posRad] + "联是:" + "“" +sentence[posRad] + "”";
			}
			else{
				str = author + "《" + name + "》中第" + (pos1 + 1) + "句";
				str += "，该诗第"+ (posRad+1) + "句是:" + "“" +sentence[posRad] + "”";
			}
			return str;
		}
}

class curBest{
	boolean[][] flag;
	char[][] matrix;
	int count;
	int totalcount;
	List<Sentence> heng;
	List<Sentence> shu;
	List<Pos> hengpos;
	List<Pos> shupos;
	
	public curBest(int height,int width){
		super();
		flag = new boolean[height][width];
		matrix = new char[height][width];
		heng = new ArrayList<Sentence>();
		shu = new ArrayList<Sentence>();
		hengpos = new ArrayList<Pos>();
		shupos = new ArrayList<Pos>();
	}
	
	
}
class Storage{
	public static final int MAXSIZE = 5000;
	public static final int MAXPERTIMES = 30;
	public static final int BEGINNUM = 10;
	int num;
	sens[] senList;
	
	public Storage() {
		super();
		senList = new sens[MAXSIZE];
		num = 0;
		init();
	}
	
	
	private void init() {
		// TODO Auto-generated method stub
		DB db = new DB();
		List<StudyPoemList> pList= db.getPoemList(1, 5);
		String[] senTemp;
		String[] part;
		int pId;
		int pri;
		Poem poem;
		for(StudyPoemList x:pList){
			pId = x.getpId();
			poem = db.getPoem(pId);
			pri = poem.getPriority();
			senTemp = poem.getFulltext().split("。");
			for(String y:senTemp){
				part = y.split("，|？|、");
				for(String z:part){
					if(z.length() > 3 && z.indexOf("，|、|？") == -1){
						sens temp = new sens(z,pId,pri);
						senList[num++] = temp;
					}
				}
			}
			if(poem.getName().indexOf('，') == -1 && poem.getName().indexOf('。') == -1 && poem.getName().indexOf(' ') == -1 && poem.getName().indexOf('·') == -1 && poem.getName().indexOf('　') == -1){
			sens temp = new sens(poem.getName(),pId,pri);
			senList[num++] = temp;
			}
			sens temp1 = new sens(poem.getAuthor(),pId,pri);
			senList[num++] = temp1;
		}		
	}
	
	Sentence[] find(char key){
		Sentence[] result = new Sentence[MAXPERTIMES];
		int pos;
		int count = 0;
		int beginpos = (int) (Math.random() * 2000) % num;
		for(int i = 0;i < num;i++)
		{
			sens x = senList[(i + beginpos) % num];
			pos = x.getSen().indexOf(key);
			if(pos > -1)
			{
				Sentence temp = new Sentence();
				temp.setpID(x.getpId());
				temp.setSen(x.getSen());
				temp.setLength(temp.getSen().length());
				temp.setPos(pos);
				temp.setPri(x.getPri());
				result[count++] = temp;
			}
			if(count >= MAXPERTIMES)
				break;	
		}		
		return result;	
	}
	
	Sentence[] begin(){
		Sentence[] result = new Sentence[BEGINNUM];
		int pos;
		int count = 0;
		for(int i = 0;i < BEGINNUM;i++)
		{
			pos = (int) (Math.random() * 2000) % num;
			sens x = senList[pos];
			if(x.sen.length() > 3){
				Sentence temp = new Sentence();
				temp.setpID(x.getpId());
				temp.setSen(x.getSen());
				temp.setLength(temp.getSen().length());
				temp.setPos(0);
				temp.setPri(x.getPri());
				result[count++] = temp;		
			}
		}
		return result;		
	}
	
	public sens[] getSenList() {
		return senList;
	}
	public void setSenList(sens[] senList) {
		this.senList = senList;
	}
	
	
}

class sens{
	String sen;
	int pId;
	int pri;
//	public sens(String sen, int pId) {
//		super();
//		this.sen = sen;
//		this.pId = pId;
//	}

	public sens(String sen, int pId,int pri) {
		super();
		this.sen = sen;
		this.pId = pId;
		this.pri = pri;
	}
	public sens() {
		super();
	}
	public String getSen() {
		return sen;
	}
	public void setSen(String sen) {
		this.sen = sen;
	}
	public int getpId() {
		return pId;
	}
	public void setpId(int pId) {
		this.pId = pId;
	}
	public int getPri() {
		return pri;
	}
	public void setPri(int pri) {
		this.pri = pri;
	}
	
	
}

class Sentence{
	String sen;
	int length;
	int pos;
	int pID;
	int pri;

	public Sentence() {
		super();
	}
//	public void generate(Poem poem,char key) {
//		// TODO Auto-generated method stub
//		String[] poemSens = poem.getFulltext().split("。");
//		int radnum = (int)(Math.random() * 20101) % poemSens.length;
//		int curpos = 0;
//		for(int i = 0;i < poemSens.length;i++){	
//			curpos = (i + radnum) % poemSens.length;
//			if(poemSens[curpos].indexOf(key) >= 0)
//				break;
//		}
//		String[] senParts = poemSens[curpos].split("，");
//		radnum = (int)(Math.random() * 20101) % senParts.length;
//		for(int i = 0;i < senParts.length;i++){	
//			curpos = (i + radnum) % senParts.length;
//			if(senParts[curpos].indexOf(key) >= 0)
//				break;
//		}		
//		this.setSen(senParts[curpos]);
//		this.setLength(this.getSen().length());
//		this.setpID(Sentence.SHIJU);
//		this.setPos(senParts[curpos].indexOf(key));
//	}
	
	public String getSen() {
		return sen;
	}
	public void setSen(String sen) {
		this.sen = sen;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public int getpID() {
		return pID;
	}
	public void setpID(int pID) {
		this.pID = pID;
	}

	public int getPri() {
		return pri;
	}

	public void setPri(int pri) {
		this.pri = pri;
	}

	
}
class Pos{
	int markpos;
	int x;
	int y;
	
	
	public Pos(int markpos, int x, int y) {
		super();
		this.markpos = markpos;
		this.x = x;
		this.y = y;
	}
	
	public Pos(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getMarkpos() {
		return markpos;
	}
	public void setMarkpos(int markpos) {
		this.markpos = markpos;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	
}