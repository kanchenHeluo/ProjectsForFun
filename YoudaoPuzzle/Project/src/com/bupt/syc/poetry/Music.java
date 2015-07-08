package com.bupt.syc.poetry;

import android.content.Context;
import android.media.MediaPlayer;

public class Music {
	private static MediaPlayer mp =null;
	private static int position = 0;
	public static boolean status = false;
	public static int musicNum = R.raw.bgmusic;
	static int music[] = {R.raw.bgmusic,R.raw.bgmusic2};
	public static void create(){
		int radnum = (int) (Math.random() * 10) % 2;
		musicNum = music[radnum];
	}
	
	public static void play(Context context){
		stop(context);
		mp = MediaPlayer.create(context, musicNum);
		mp.setLooping(true);
		mp.seekTo(position);
		mp.start();
		status = true;
	}
	
	public static void stop(Context context) {
		if(mp!=null){
			position = mp.getCurrentPosition();
			mp.stop();
			mp.release();
			mp = null;
			status = false;
		}
	}
	
	public static void pause() {
		if(status)
			mp.pause();
	}
	
	public static void resume() {
		if(status)
			mp.start();
	}
}
