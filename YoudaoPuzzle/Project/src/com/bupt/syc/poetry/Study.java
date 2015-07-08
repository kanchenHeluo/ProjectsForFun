package com.bupt.syc.poetry;

import java.util.ArrayList;
import java.util.List;

public class Study {
	StudyPoem editedPoem;
	Poem originalPoem;
	static DB db;
	

	
	public Study() {
		super();
		db = new DB();
	}

	public static StudyPoem anylyzePoem(Poem originalPoem){
		StudyPoem tempPoem = new StudyPoem();
		tempPoem.setPoems(originalPoem.getFulltext().split("¡£|£¿"));
		tempPoem.setAppreciation(originalPoem.getAppreation());
		return tempPoem;
	}
	
	public StudyPoem getpoem(int id){
		this.originalPoem = db.getPoem(id);
		this.editedPoem = new StudyPoem();
		this.editedPoem.setPoems(this.originalPoem.getFulltext().split("¡£|£¿"));
		this.editedPoem.setAppreciation(this.originalPoem.getAppreation());
		this.editedPoem.setAuthor(this.originalPoem.getAuthor());
		this.editedPoem.setTitle(this.originalPoem.getName());
		return this.editedPoem;
	}
	
	public List<StudyPoemList> getPoemList(studyCondition sc){
		return db.getPoemList(sc);
	}
	
	public List<StudyPoemList> getPoemList(){
		List<StudyPoemList> poemlist = new ArrayList<StudyPoemList>();
		List<StudyPoemList> tlist = db.getPoemList(1, 1);
		for(int i = 0;i < 5;i++){
			int radnum = (int) (Math.random() * 3331) % tlist.size();
			poemlist.add(tlist.get(radnum));
			tlist.remove(radnum);
		}
		return poemlist;
	}
	
	public List<StudyPoemList> getPoemList(studyCondition sc,int numbers){
		List<StudyPoemList> poemlist = new ArrayList<StudyPoemList>();
		List<StudyPoemList> tlist = db.getPoemList(sc);
		int totalnum = tlist.size();
		for(int i = 0;i < numbers && i < totalnum;i++){
			int radnum = (int) (Math.random() * 3331) % tlist.size();
			poemlist.add(tlist.get(radnum));
			tlist.remove(radnum);
		}
		return poemlist;
	}

	
	public void mark(int pId,String albumName){
		db.insertAlbum(pId, albumName);
		db.setMarked(pId);
	}
	
	public static boolean isLeaned(int pid){
		return db.isLeard(pid);
	}
	
	public void unmark(int pId){
		db.delLeard(pId);
	}
	 public List<String> getAuthorList(String[] dynasty){
		 DB db = new DB();
		 return db.getAuthorList(dynasty);
	 }
	 
	 public List<String> getStyleList(){
		 DB db = new DB();
		 return db.getStyleList();
	 }
	 
	 public List<String> getStyleList(String[] dynasty){
		 DB db = new DB();
		 return db.getStyleList(dynasty,false);
	 }
	 
}

