package com.bupt.syc.poetry;

import java.io.UnsupportedEncodingException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DB {
	 public static String DATABASE_PATH = null;
	 public static String DATABASR_NAME = null;
	 public static int version = 169;
	 public static SQLiteDatabase db ;
	 
	 
	 
	 public DB(String dATABASE_PATH, String dATABASR_NAME) {
		super();	
		DATABASE_PATH = dATABASE_PATH;
		DATABASR_NAME = dATABASR_NAME;
		if(db != null)
			this.OpenDatabase();
	}

	 public DB() {
		super();	
		this.OpenDatabase();
	}
	private void OpenDatabase()
	 {
		String DatebaseFilename = DATABASE_PATH + "/" + DATABASR_NAME;
		db = SQLiteDatabase.openOrCreateDatabase(DatebaseFilename,null);
	 }
	 
	shiren getShiren(String name)
	 {
		shiren author = null;
		if(db != null)
		{
			Cursor cursor = db.rawQuery("select * from author where name = ?", new String[]{"%" + name + "%"});
			while(cursor.moveToNext())
			{
				shiren temp = new shiren();
				temp.setName(cursor.getString(cursor.getColumnIndex("name")));
				temp.setChaodai(cursor.getString(cursor.getColumnIndex("dynasty")));
				temp.setZi(cursor.getString(cursor.getColumnIndex("zi")));
				temp.setShiziji(cursor.getString(cursor.getColumnIndex("poemset")));
				temp.setHao(cursor.getString(cursor.getColumnIndex("hao")));
				author = temp;
			}		
		}
		return author;		 
	 }
	 
	 List<StudyPoemList> getPoemList(studyCondition condition)
	 {
		List<StudyPoemList> poemlist = new ArrayList<StudyPoemList>();
		String sql = "";
		boolean isbegin = false;
		if(condition.getAlbum() != null){
			sql = "select * from poem,album where poem.pid = album.pid";
			isbegin = true;
		}
		else{
			sql = "select * from poem";
		}
		if(condition.getAuthor() != null){
			if(!isbegin){
				sql += (" where poem.authorname = '" + condition.getAuthor() + "'");
				isbegin = true;
			}
			else
				sql += (" and poem.authorname = '" + condition.getAuthor() + "'");
		}
		if(condition.getDynasty() != null && condition.getDynasty().length != 0 && condition.getDynasty()[0] != null){
			if(!isbegin){
				sql += (" where" + this.genDynastySQL(condition.getDynasty()));
				isbegin = true;
			}
			else
				sql += (" and" + this.genDynastySQL(condition.getDynasty()));
		}
		if(condition.getStyle() != null){
			if(!isbegin){
				sql += (" where poem.type = '" + condition.getStyle() + "'");
				isbegin = true;
			}
			else
				sql += (" and poem.type = '" + condition.getStyle() + "'");
		}
		if(condition.getAlbum() != null)
			sql += (" and album.albumname = '" + condition.getAlbum() + "'");
		if(condition.getTitleMatched() != null){
			if(!isbegin){
				sql += (" where poem.name like '%" + condition.getTitleMatched() + "%'");
				isbegin = true;
			}
			else
				sql += (" and poem.name like '%" + condition.getTitleMatched() + "%'");	
		}
		sql += " limit 30";
		if(db != null){
			Cursor cursor = db.rawQuery(sql, new String[]{});
			while(cursor.moveToNext())
			{
				StudyPoemList temp = new StudyPoemList();
				temp.setAuthor(cursor.getString(cursor.getColumnIndex("authorname")));
				temp.setpId(cursor.getInt(cursor.getColumnIndex("pid")));
				temp.setTitle(cursor.getString(cursor.getColumnIndex("name")));
				poemlist.add(temp);
			}			
		}
		return poemlist;		 
	 }
	 
	 List<StudyPoemList> getPoemList(ExamCondition condition)
	 {
		List<StudyPoemList> poemlist = new ArrayList<StudyPoemList>();
		String sql = "";
		boolean isbegin = false;
		if(condition.getAlbumName() != null){
			sql = "select * from poem,album where poem.pid = album.pid";
			isbegin = true;
		}
		else{
			sql = "select * from poem";
		}
		if(condition.getAuthor() != null && !condition.getAuthor().equals("任意")){
			if(!isbegin){
				sql += (" where poem.authorname = '" + condition.getAuthor() + "'");
				isbegin = true;
			}
			else
				sql += (" and poem.authorname = '" + condition.getAuthor() + "'");
		}
		if(condition.getDynasty() != null && condition.getDynasty().length != 0 && condition.getDynasty()[0] != null){
			if(!isbegin){
				sql += (" where" + this.genDynastySQL(condition.getDynasty()));
				isbegin = true;
			}
			else
				sql += (" and" + this.genDynastySQL(condition.getDynasty()));
		}
		if(condition.isMarked()){
			if(!isbegin){
				sql += (" where poem.ismarked = 1");
				isbegin = true;
			}
			else
				sql += ("and poem.ismarked = 1");
		}
		if(condition.getStyle() != null && !condition.getStyle().equals("任意")){
			if(!isbegin){
				sql += (" where poem.type = '" + condition.getStyle() + "'");
				isbegin = true;
			}
			else
				sql += (" and poem.type = '" + condition.getStyle() + "'");
		}
		if(condition.getAlbumName() != null)
			sql += (" and album.albumname = '" + condition.getAlbumName() + "'");
		if(db != null){
			Cursor cursor = db.rawQuery(sql, new String[]{});
			while(cursor.moveToNext())
			{
				StudyPoemList temp = new StudyPoemList();
				temp.setAuthor(cursor.getString(cursor.getColumnIndex("authorname")));
				temp.setpId(cursor.getInt(cursor.getColumnIndex("pid")));
				temp.setTitle(cursor.getString(cursor.getColumnIndex("name")));
				poemlist.add(temp);
			}			
		}
		return poemlist;		 
	 }
	 
	 String genDynastySQL(String[] dynastys){
		 String result = "(poem.dynasty = '" + dynastys[0] + "'";
		 for(int i = 1;i < dynastys.length;i++){
			result += " or poem.dynasty = '" + dynastys[i] + "'";
		 }
		 result += ")";
		 return result;
	 }
	 
	 List<StudyPoemList> getPoemList(int beginPri,int endPri)
	 {
		List<StudyPoemList> poemlist = new ArrayList<StudyPoemList>();
		String sql = "select * from poem where priority >= ? and priority <= ?";
		if(db != null){
			Cursor cursor = db.rawQuery(sql, new String[]{Integer.toString(beginPri),Integer.toString(endPri)});
			while(cursor.moveToNext())
			{
				StudyPoemList temp = new StudyPoemList();
				temp.setAuthor(cursor.getString(cursor.getColumnIndex("authorname")));
				temp.setpId(cursor.getInt(cursor.getColumnIndex("pid")));
				temp.setTitle(cursor.getString(cursor.getColumnIndex("name")));
				poemlist.add(temp);
			}			
		}
		return poemlist;		 
	 }
	 
	 List<StudyPoemList> getPoemList(int beginPri,int endPri,char key)
	 {
		List<StudyPoemList> poemlist = new ArrayList<StudyPoemList>();
		String sql = "select * from poem where priority >= ? and priority <= ? and fulltext like ?";
		if(db != null){
			Cursor cursor = db.rawQuery(sql, new String[]{Integer.toString(beginPri),Integer.toString(endPri),"%" + key + "%"});
			while(cursor.moveToNext())
			{
				StudyPoemList temp = new StudyPoemList();
				temp.setAuthor(cursor.getString(cursor.getColumnIndex("authorname")));
				temp.setpId(cursor.getInt(cursor.getColumnIndex("pid")));
				temp.setTitle(cursor.getString(cursor.getColumnIndex("name")));
				poemlist.add(temp);
			}			
		}
		return poemlist;		 
	 }
	 List<StudyPoemList> getPoemList(String authorName)
	 {
		List<StudyPoemList> poemlist = new ArrayList<StudyPoemList>();
		String sql = "select * from poem where authorname = ?";
		if(db != null){
			Cursor cursor = db.rawQuery(sql, new String[]{authorName});
			while(cursor.moveToNext())
			{
				StudyPoemList temp = new StudyPoemList();
				temp.setAuthor(cursor.getString(cursor.getColumnIndex("authorname")));
				temp.setpId(cursor.getInt(cursor.getColumnIndex("pid")));
				temp.setTitle(cursor.getString(cursor.getColumnIndex("name")));
				poemlist.add(temp);
			}			
		}
		return poemlist;		 
	 }
	 List<String> getAuthorList(char key)
	 {
		List<String> authorlist = new ArrayList<String>();
		String sql = "select distinct(authorname) from poem where authorname like ?";
		if(db != null){
			Cursor cursor = db.rawQuery(sql, new String[]{"%" + key + "%"});
			while(cursor.moveToNext())
			{
				String temp = (cursor.getString(cursor.getColumnIndex("authorname")));
				authorlist.add(temp);
			}			
		}
		return authorlist;		 
	 }
	 
	 List<String> getAuthorList(String[] dynasty){
		 List<String> authorlist = new ArrayList<String>();
		 String sql;
		 if(dynasty != null && dynasty.length != 0 && dynasty[0] != null)
			 sql = "select authorname,count(*) from poem where " + genDynastySQL(dynasty) + " group by authorname order by count(*) desc";
		 else
			 sql = "select authorname,count(*) from poem group by authorname order by count(*) desc";
			if(db != null){
				Cursor cursor = db.rawQuery(sql, new String[]{});
				while(cursor.moveToNext())
				{
					String temp = (cursor.getString(cursor.getColumnIndex("authorname")));
					authorlist.add(temp);
				}			
			}
			Collections.sort(authorlist, new SortComparator());
		return authorlist;	
	 }

	 List<String> getAuthorList(String[] dynasty,boolean isMarked){
		 List<String> authorlist = new ArrayList<String>();
		 String sql;
		 boolean isbegin = false;
		 if(dynasty != null && dynasty.length != 0 && dynasty[0] != null){
			 sql = "select authorname,count(*) from poem where " + genDynastySQL(dynasty);
			 isbegin = true;
		 }
		 else
			 sql = "select authorname,count(*) from poem";
		if(isMarked){
			if(!isbegin){
				sql += (" where poem.ismarked = '1'");
				isbegin = true;
			}
			else
				sql += (" and poem.ismarked = '1'");
		}
		 sql += " group by authorname order by count(*) desc";
			if(db != null){
				Cursor cursor = db.rawQuery(sql, new String[]{});
				while(cursor.moveToNext())
				{
					String temp = (cursor.getString(cursor.getColumnIndex("authorname")));
					authorlist.add(temp);
				}			
			}
		Collections.sort(authorlist, new SortComparator());
		return authorlist;	
	 }
	 List<String> getStyleList(){
		 List<String> typelist = new ArrayList<String>();
		 String sql = "select distinct(type) from poem";
			if(db != null){
				Cursor cursor = db.rawQuery(sql, new String[]{});
				while(cursor.moveToNext())
				{
					String temp = (cursor.getString(cursor.getColumnIndex("type")));
					typelist.add(temp);
				}			
			}
		return typelist;	
	 }
	 
	 List<String> getStyleList(String[] dynasty,boolean isMarked){
		 List<String> typelist = new ArrayList<String>();
		 boolean isbegin = false;
		 String sql;
		 if(dynasty != null && dynasty.length != 0 && dynasty[0] != null){
			 sql = "select type,count(*) from poem where " + genDynastySQL(dynasty);
			 isbegin = true;
		 }
		 else
			 sql = "select type,count(*) from poem";
		if(isMarked){
			if(!isbegin){
				sql += (" where ismarked = 1");
				isbegin = true;
			}
			else
				sql += ("and ismarked = 1");
		}
		 sql += " group by type order by count(*) desc";
		if(db != null){
			Cursor cursor = db.rawQuery(sql, new String[]{});
			while(cursor.moveToNext())
			{
				String temp = (cursor.getString(cursor.getColumnIndex("type")));
				typelist.add(temp);
			}			
		}
		return typelist;	
	 }
	 List<String> getNameList(char key)
	 {
		List<String> namelist = new ArrayList<String>();
		String sql = "select distinct(name) from poem where authorname like ?";
		if(db != null){
			Cursor cursor = db.rawQuery(sql, new String[]{"%" + key + "%"});
			while(cursor.moveToNext())
			{
				String temp = (cursor.getString(cursor.getColumnIndex("name")));
				namelist.add(temp);
			}			
		}
		return namelist;		 
	 }
	 
	 Poem getPoem(int pId)
	 {
		 Poem result = new Poem();
			if(db != null)
			{
				Cursor cursor = db.rawQuery("select * from poem where pid = ?", new String[]{Integer.toString(pId)});
				while(cursor.moveToNext())
				{
					result.setPid(pId);
					result.setName(cursor.getString(cursor.getColumnIndex("name")));
					result.setAuthor(cursor.getString(cursor.getColumnIndex("authorname")));
					result.setAppreation(cursor.getString(cursor.getColumnIndex("appreciation")));
					result.setFulltext(cursor.getString(cursor.getColumnIndex("fulltext")));
					result.setIsmarked(cursor.getInt(cursor.getColumnIndex("ismarked")));
					result.setPriority(cursor.getInt(cursor.getColumnIndex("priority")));
					result.setType(cursor.getString(cursor.getColumnIndex("type")));
					result.setDynasty(cursor.getString(cursor.getColumnIndex("dynasty")));
					//temp.setHao("dfdf");
				}		
			}		 
		 return result;
	 }

	 void insertRecord(String name,int score,String time){
		 db.execSQL("insert into topscore(time,name,score) values(?,?,?)",new Object[]{time,name,score});
	 }
	 
	 List<Game1Score> getTopScore(){
			List<Game1Score> recordlist = new ArrayList<Game1Score>();
			String sql = "select * from topscore order by score desc";
			if(db != null){
				Cursor cursor = db.rawQuery(sql, new String[]{});
				while(cursor.moveToNext())
				{
					Game1Score temp = new Game1Score();
					temp.setName(cursor.getString(cursor.getColumnIndex("name")));
					temp.setScore(cursor.getInt(cursor.getColumnIndex("score")));
					temp.setTime(cursor.getString(cursor.getColumnIndex("time")));
					recordlist.add(temp);
				}			
			}
			return recordlist;			 
	 }

	 void insertAlbum(int pId,String albumName){
		 Cursor cursor = db.rawQuery("select * from album where pid = ? and albumname = ?", new String[]{Integer.toString(pId),albumName});
		 if(!cursor.moveToNext())
			 db.execSQL("insert into album(pid,albumname) values(?,?)",new Object[]{pId,albumName});
	 }
	 
	 boolean isLeard(int pId){
		 Cursor cursor = db.rawQuery("select * from album where pid = ?", new String[]{Integer.toString(pId)});
		 if(!cursor.moveToNext())
			 return false;
		 else
			 return true;
	 }
	 
	 void delLeard(int pId){
		 db.execSQL("update poem set ismarked = 0 where pid = ?",new Object[]{pId});
		 if(this.isLeard(pId))
			 db.execSQL("delete from album where pid = ?",new Object[]{pId});
	 }
	 
	 void setMarked(int pId){
		 db.execSQL("update poem set ismarked = 1 where pid = ?",new Object[]{pId});
	 }

	 List<String> getDescribe(String sen){
		 List<String> describes = new ArrayList<String>();
			if(db != null){
				 Cursor cursor = db.rawQuery("select * from describe where words = ?", new String[]{sen});
				while(cursor.moveToNext())
				{
					String temp = cursor.getString(cursor.getColumnIndex("description"));
					describes.add(temp);
				}			
			}
		return describes;
	 }
	 
	 Poem getPoemByPeomsen(String text){
		 Poem result = null;
			if(db != null)
			{
				Cursor cursor = db.rawQuery("select * from poem where fulltext like ?", new String[]{"%" + text + "%"});
				while(cursor.moveToNext())
				{
					result = new Poem();
					result.setPid(cursor.getInt(cursor.getColumnIndex("pid")));
					result.setName(cursor.getString(cursor.getColumnIndex("name")));
					result.setAuthor(cursor.getString(cursor.getColumnIndex("authorname")));
					result.setAppreation(cursor.getString(cursor.getColumnIndex("appreciation")));
					result.setFulltext(cursor.getString(cursor.getColumnIndex("fulltext")));
					result.setIsmarked(cursor.getInt(cursor.getColumnIndex("ismarked")));
					result.setPriority(cursor.getInt(cursor.getColumnIndex("priority")));
					result.setType(cursor.getString(cursor.getColumnIndex("type")));
					result.setDynasty(cursor.getString(cursor.getColumnIndex("dynasty")));
					//temp.setHao("dfdf");
				}		
			}		 
		 return result;		 
	 }
	 
	 Poem getPoemByName(String name){
		 Poem result = null;
			if(db != null)
			{
				Cursor cursor = db.rawQuery("select * from poem where name = ?", new String[]{name});
				while(cursor.moveToNext())
				{
					result = new Poem();
					result.setPid(cursor.getInt(cursor.getColumnIndex("pid")));
					result.setName(cursor.getString(cursor.getColumnIndex("name")));
					result.setAuthor(cursor.getString(cursor.getColumnIndex("authorname")));
					result.setAppreation(cursor.getString(cursor.getColumnIndex("appreciation")));
					result.setFulltext(cursor.getString(cursor.getColumnIndex("fulltext")));
					result.setIsmarked(cursor.getInt(cursor.getColumnIndex("ismarked")));
					result.setPriority(cursor.getInt(cursor.getColumnIndex("priority")));
					result.setType(cursor.getString(cursor.getColumnIndex("type")));
					result.setDynasty(cursor.getString(cursor.getColumnIndex("dynasty")));
					//temp.setHao("dfdf");
				}		
			}		 
		 return result;		 
	 }
	 
	 void setVersion(){
			DB.db.setVersion(version);
	 }
	 
	 static void close(){
		 if(DB.db != null)
			 DB.db.close();
	 }
	 
	 public class SortComparator implements Comparator{ 
		 public int compare(Object o1,Object o2) { 
			 return Collator.getInstance(Locale.CHINESE).compare(o1, o2); 
		 } 
	 } 
	 
}


