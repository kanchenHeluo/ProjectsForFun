package com.bupt.syc.poetry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class PoetryActivity extends MyActivity {
	
	Timer timer = new Timer();  
    TimerTask taskcc = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        changeActivity = false;
        DB.DATABASE_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/Poem";
        DB.DATABASR_NAME = "shici.db";
        copyDB();
        DB db = new DB();
        db.setVersion();
        db.db.close();
        Music.create();
        Music.play(this);
        spark();
    }
    public boolean onTouchEvent(MotionEvent event) {//屏幕监听方法
		if(event.getAction() == MotionEvent.ACTION_DOWN){//屏幕被按下事件
			changeActivity = true;
			Intent intent = new Intent();
			intent.setClass(PoetryActivity.this, MenuActivity.class);
			startActivity(intent);
			//PoetryActivity.this.finish();
			taskcc.cancel();
			timer.cancel();
			timer.purge(); 
			timer = null;
			onDestroy();
		}
		return super.onTouchEvent(event);
    }
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Music.stop(this);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
        switch (item.getItemId()) {
        case Menu.FIRST + 4:
        	Music.stop(this);
        	break;
        }
        	
        return super.onOptionsItemSelected(item);
	}
	
    private int clo = 0;  
    public void spark() {  
        final TextView touchScreen = (TextView) findViewById(R.id.welcomeText);// 获取页面textview对象  
        //Timer timer = new Timer();  
        taskcc = new TimerTask(){  
            public void run() {  
                runOnUiThread(new Runnable() {  
                    public void run() {  
                        if (clo == 0) {  
                            clo = 1;  
                            touchScreen.setTextColor(Color.LTGRAY); // 透明  
                        } else {  
                            if (clo == 1) {  
                                clo = 2;  
                                touchScreen.setTextColor(Color.GRAY);  
                            } else {  
                                clo = 0;  
                                touchScreen.setTextColor(Color.DKGRAY);  
                            }  
                        }  
                    }  
                });  
            }  
        };  
        timer.schedule(taskcc, 1, 300); // 参数分别是delay（多长时间后执行），duration（执行间隔）  
    }  
    boolean copyDB()
    {
		try {
			//String DatebaseFilename = DATABASE_PATH + "/" + DATABASE_NAME;
			String DatebaseFilename = DB.DATABASE_PATH + "/" + DB.DATABASR_NAME;
			File Dir = new File(DB.DATABASE_PATH);
			if(!Dir.exists())
			{
				Dir.mkdir();
			}
			File a = new File(DatebaseFilename);
			if((a.exists())){
				DB db = new DB();
				if(db.db.getVersion() < DB.version){
					this.setTitle(db.db.getVersion() + " " +  DB.version);
					db.db.close();
					a.delete();
					
				}
			}
			if(!(new File(DatebaseFilename).exists())){
			InputStream is = getResources().openRawResource(R.raw.shici);
			FileOutputStream fos = new FileOutputStream(DatebaseFilename);
			byte[] buffer = new byte[8192];
			int count = 0;
			while((count = is.read(buffer)) > 0)
			{
				fos.write(buffer, 0, count);
			}
			
			is.close();
			fos.close();	
			}
		}
	    catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true; 	
    }
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		super.onDestroy();
		PoetryActivity.this.finish();
	}
    
	
}