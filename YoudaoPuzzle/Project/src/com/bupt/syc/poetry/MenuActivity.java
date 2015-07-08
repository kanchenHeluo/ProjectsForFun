package com.bupt.syc.poetry;


import java.text.BreakIterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends MyActivity{

	Button studyButton = null;
	Button examButton = null;
	Button game1Button = null;
	Button game2Button = null;
	Button helpButton = null;
	Button aboutButton = null;
	boolean isPressBackKey = false;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuview);	
		isPressBackKey = false;
		changeActivity = false;
		//Music.play(this);
		studyButton = (Button)findViewById(R.id.study_button);
		examButton = (Button)findViewById(R.id.exam_button);
		game1Button = (Button)findViewById(R.id.game1_button);
		game2Button = (Button)findViewById(R.id.game2_button);
		helpButton = (Button)findViewById(R.id.help_button);
		aboutButton = (Button)findViewById(R.id.about_button);
		
		studyButton.setOnClickListener(Listener);
		examButton.setOnClickListener(Listener);
		game1Button.setOnClickListener(Listener);
		game2Button.setOnClickListener(Listener);
		helpButton.setOnClickListener(Listener);
		aboutButton.setOnClickListener(Listener);
	}
	
	private OnClickListener Listener = new OnClickListener(){

		public void onClick(View v) {
			Button btn = (Button)v;
			isPressBackKey = false;
			Intent intent = new Intent();
			switch (btn.getId()) {
			case R.id.study_button:
				//Toast.makeText(MenuActivity.this, "press study", Toast.LENGTH_LONG).show();
				intent.setClass(MenuActivity.this, StudyActivity.class);
				//startActivity(intent);
				break;
			case R.id.exam_button:
				//Toast.makeText(MenuActivity.this, "press exam", Toast.LENGTH_LONG).show();
				intent.setClass(MenuActivity.this, ExamActivity.class);
				//startActivity(intent);
				break;
			case R.id.game1_button:
				//Toast.makeText(MenuActivity.this, "press game", Toast.LENGTH_LONG).show();
				intent.setClass(MenuActivity.this, Game1Activity.class);
				//startActivity(intent);
				break;
			case R.id.game2_button:
				intent.setClass(MenuActivity.this, Game2Activity.class);
				break;
			case R.id.about_button:
				intent.setClass(MenuActivity.this, AboutActivity.class);
				break;
			case R.id.help_button:
				intent.setClass(MenuActivity.this, HelpActivity.class);
				break;
				
			default:
				break;
			}
			changeActivity = true;
			startActivity(intent);
			
		}

	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Music.stop(this);
		DB.close();
		MenuActivity.this.finish();
		System.exit(0);
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(isPressBackKey){
				onDestroy();
			}
			else{
				isPressBackKey = true;
				Toast.makeText(this,"再按一次回退键退出程序", Toast.LENGTH_LONG).show();
			}
			return false;
		}
		else
			return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
        switch (item.getItemId()) {
        case Menu.FIRST + 4:
			if(isPressBackKey){
				onDestroy();
			}
			else{
				isPressBackKey = true;
				Toast.makeText(this,"再按一次返回键退出程序", Toast.LENGTH_LONG).show();
			}
            break;
        default:
        	return super.onOptionsItemSelected(item);
        }
        	
        return false;
		
	//	return super.onOptionsItemSelected(item);
	}
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
//		Music.stop(this);
//	}
//	
//	@Override
//	protected void onRestart() {
//		// TODO Auto-generated method stub
//		super.onRestart();
//		Music.play(this);
//	}	
	
}
