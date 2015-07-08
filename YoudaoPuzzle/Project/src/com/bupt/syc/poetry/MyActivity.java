package com.bupt.syc.poetry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MyActivity extends Activity{

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
//		if(Music.status)
//			menu.add(Menu.NONE, Menu.FIRST + 1, 1,"关闭背景音乐").setIcon(R.drawable.menu1_1);
//		else
//			menu.add(Menu.NONE, Menu.FIRST + 1, 1,"打开背景音乐").setIcon(R.drawable.menu1);
//		menu.add(Menu.NONE, Menu.FIRST + 2, 2,"关于").setIcon(R.drawable.menu2);
//		menu.add(Menu.NONE, Menu.FIRST + 3, 3,"帮助").setIcon(R.drawable.menu3);
//		menu.add(Menu.NONE, Menu.FIRST + 4, 4,"返回").setIcon(R.drawable.menu5);
//		menu.add(Menu.NONE, Menu.FIRST + 5, 5,"退出").setIcon(R.drawable.menu4);
//		menu.add(Menu.NONE, Menu.FIRST + 6, 6,"赞美作者").setIcon(R.drawable.menu6);
//		
		return super.onCreateOptionsMenu(menu);
//		return true;
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
        switch (item.getItemId()) {

        case Menu.FIRST + 1:
        	if(Music.status)
        		Music.stop(this);
        	else
        		Music.play(this);
            break;
        case Menu.FIRST + 2:
        	intent.setClass(this, AboutActivity.class);
        	startActivity(intent);
            break;
        case Menu.FIRST + 3:
        	intent.setClass(this, HelpActivity.class);
        	startActivity(intent);
            break;
        case Menu.FIRST + 4:
        	changeActivity = true;
        	this.onDestroy();
            break;
        case Menu.FIRST + 5:
        	//this.finish();
        	AlertDialog.Builder builder = new AlertDialog.Builder(this); 
	        builder.setIcon(R.drawable.menu4);  
	        builder.setTitle("你确定要离开吗？");  
	        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
	            public void onClick(DialogInterface dialog, int whichButton) {  
	                //这里添加点击确定后的逻辑  
	    			Intent startMain = new Intent(Intent.ACTION_MAIN);
	    			startMain.addCategory(Intent.CATEGORY_HOME);
	    			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    			//this.startActivity(startMain);
	    			MyActivity.this.startActivity(startMain);
	    			System.exit(0);
	            }  
	        });  
	        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {  
	            public void onClick(DialogInterface dialog, int whichButton) {  
	                //这里添加点击确定后的逻辑  
	            }  
	        });  
	        builder.create().show();          

            break;
        case Menu.FIRST + 6:
        	Toast.makeText(this,"谢谢您赞美作者，我们也赞美你哦~，感谢您使用本应用", Toast.LENGTH_LONG).show();
            break;
        }

        return false;
		
	//	return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.clear();
		if(Music.status)
			menu.add(Menu.NONE, Menu.FIRST + 1, 1,"关闭背景音乐").setIcon(R.drawable.menu1_1);
		else
			menu.add(Menu.NONE, Menu.FIRST + 1, 1,"打开背景音乐").setIcon(R.drawable.menu1);
		menu.add(Menu.NONE, Menu.FIRST + 2, 2,"关于").setIcon(R.drawable.menu2);
		menu.add(Menu.NONE, Menu.FIRST + 3, 3,"帮助").setIcon(R.drawable.menu3);
		menu.add(Menu.NONE, Menu.FIRST + 4, 4,"返回").setIcon(R.drawable.menu5);
		menu.add(Menu.NONE, Menu.FIRST + 5, 5,"退出").setIcon(R.drawable.menu4);
		menu.add(Menu.NONE, Menu.FIRST + 6, 6,"赞美作者").setIcon(R.drawable.menu6);
		
		//return super.onCreateOptionsMenu(menu);
		return true;
		//return super.onPrepareOptionsMenu(menu);
	}

	boolean changeActivity;
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if(!changeActivity)
			Music.pause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(!changeActivity)
			Music.resume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//System.exit(0);
	}
//	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			changeActivity = true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
