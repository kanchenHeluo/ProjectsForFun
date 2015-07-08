package com.bupt.syc.poetry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ExamProblemActivity extends MyActivity{
	ExamPoemList examPoemList = new ExamPoemList();
	Button nextBtn = null, lastBtn=null, upHandBtn=null, hintBtn=null;
	Button[] words = new Button[16];
	
	TextView exTV1, exTV2, exTV3;
	EditText exET1, exET2, exET3;
	int currentPage = 0;
	int omit;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.examproblemview);
		changeActivity = false;
		//Music.play(this);
		
		exTV1 = (TextView)findViewById(R.id.examtv1);
		exTV2 = (TextView)findViewById(R.id.examtv2);
		exTV3 = (TextView)findViewById(R.id.examtv3);
		exET1 = (EditText)findViewById(R.id.examET1);
		exET2 = (EditText)findViewById(R.id.examET2);
		exET3 = (EditText)findViewById(R.id.examET3);
		
		nextBtn = (Button)findViewById(R.id.btnNext);
		lastBtn = (Button)findViewById(R.id.btnLast);
		upHandBtn = (Button)findViewById(R.id.btnTijiao);
		hintBtn = (Button)findViewById(R.id.btnTishi);
		
		words[0] = (Button)findViewById(R.id.eab11);
		words[1] = (Button)findViewById(R.id.eab12);
		words[2] = (Button)findViewById(R.id.eab13);
		words[3] = (Button)findViewById(R.id.eab14);
		words[4] = (Button)findViewById(R.id.eab21);
		words[5] = (Button)findViewById(R.id.eab22);
		words[6] = (Button)findViewById(R.id.eab23);
		words[7] = (Button)findViewById(R.id.eab24);
		words[8] = (Button)findViewById(R.id.eab31);
		words[9] = (Button)findViewById(R.id.eab32);
		words[10] = (Button)findViewById(R.id.eab33);
		words[11] = (Button)findViewById(R.id.eab34);
		words[12] = (Button)findViewById(R.id.eab41);
		words[13] = (Button)findViewById(R.id.eab42);
		words[14] = (Button)findViewById(R.id.eab43);
		words[15] = (Button)findViewById(R.id.eab44);
		
		
		nextBtn.setOnClickListener(Listener);
		lastBtn.setOnClickListener(Listener);	
		upHandBtn.setOnClickListener(Listener);
		hintBtn.setOnClickListener(Listener);
		
		for(int i=0; i<16; i++)
		   words[i].setOnClickListener(Listener);
		
		
		showProblem(currentPage);
	}
	
	private void showProblem(int currentPage){
		examPoemList = Global.exam1.getPoemList(currentPage);
		int len = examPoemList.poetrySentence.length;
		exTV1.setText(examPoemList.poetrySentence[0]);
		exTV2.setText(examPoemList.poetrySentence[1]);
		if(3 == len){
			exTV3.setText(examPoemList.poetrySentence[2]);
		}else
			exTV3.setText("");
		
		
		exET1.setVisibility(View.INVISIBLE);
		exET2.setVisibility(View.INVISIBLE);
		exET3.setVisibility(View.INVISIBLE);
		omit = examPoemList.omit;
		if(omit == 0){
			exTV1.setText("");
			exET1.setVisibility(View.VISIBLE);
			exET1.setText(Global.exam1.getViewAnswer(currentPage));
		}
		else if(examPoemList.omit == 1){
			exTV2.setText("");
			exET2.setVisibility(View.VISIBLE);
			exET2.setText(Global.exam1.getViewAnswer(currentPage));
		}
		else {
			exTV3.setText("");
			exET3.setVisibility(View.VISIBLE);
			exET3.setText(Global.exam1.getViewAnswer(currentPage));
		}
		//show words
		for(int i=0;i<16;i++)
		   words[i].setText(String.valueOf(examPoemList.getWords()[i]));
	}
	
	private OnClickListener Listener = new OnClickListener(){

		public void onClick(View v) {
			Button btn = (Button)v;
			int bottom = btn.getPaddingBottom();
		    int top = btn.getPaddingTop();
		    int right = btn.getPaddingRight();
		    int left = btn.getPaddingLeft();
			switch (btn.getId()) {
			case R.id.btnNext: //存储本页答案，进入下一页
				String s1 = null;
				if(omit==0)
					s1 = exET1.getText().toString();
				else if(omit==1)
					s1 = exET2.getText().toString();
				else
					s1 = exET3.getText().toString();
				Global.exam1.setViewAnswer(currentPage, s1);
				
				if(currentPage != Global.exam1.getAmount()-1)
					currentPage ++;
				showProblem(currentPage);
				break;
				
			case R.id.btnLast:
				String s = null;
				if(omit==0)
					s = exET1.getText().toString();
				else if(omit==1)
					s = exET2.getText().toString();
				else
					s = exET3.getText().toString();
				Global.exam1.setViewAnswer(currentPage, s);
				
                if(currentPage != 0)
                	currentPage --;
                showProblem(currentPage);
				break;
				
			case R.id.btnTijiao:
				String s11 = null;
				if(omit==0)
					s11 = exET1.getText().toString();
				else if(omit==1)
					s11 = exET2.getText().toString();
				else
					s11 = exET3.getText().toString();
				Global.exam1.setViewAnswer(currentPage, s11);
				
				Global.score = Global.exam1.ExamJudge();
				Global.er = Global.exam1.returnAnser();
				//UI
				btn.setBackgroundResource(R.drawable.test_submit_btn_click);
				//切换 
				changeActivity = true;
				Intent intent = new Intent();
				intent.setClass(ExamProblemActivity.this, ExamAnswerActivity.class);
				startActivity(intent);
				onDestroy();
				break;
			
			case R.id.btnTishi:
				if(words[0].getVisibility() == View.VISIBLE){
				   for(int i=0; i<16; i++ ){
					   words[i].setVisibility(View.INVISIBLE);
				   }
				   //UI
				   btn.setBackgroundResource(R.drawable.test_hint_btn);
				}else{
					for(int i=0; i<16; i++ ){
						   words[i].setVisibility(View.VISIBLE);
					   }
					//UI
					btn.setBackgroundResource(R.drawable.test_hint_btn_click);
				}
				break;
				
			case R.id.eab11:
			case R.id.eab12:
			case R.id.eab13:
			case R.id.eab14:
			case R.id.eab21:
			case R.id.eab22:
			case R.id.eab23:
			case R.id.eab24:
			case R.id.eab31:
			case R.id.eab32:
			case R.id.eab33:
			case R.id.eab34:
			case R.id.eab41:
			case R.id.eab42:
			case R.id.eab43:
			case R.id.eab44:
			    //Toast.makeText(ExamProblemActivity.this, "press", Toast.LENGTH_LONG).show();
				if(omit == 0){
                   insertText(exET1, btn.getText());
				}
				else if(examPoemList.omit == 1){
					insertText(exET2, btn.getText());

				}
				else {
					insertText(exET3, btn.getText());
				}
				break;
			default:
				break;
			}
			btn.setPadding(left, top, right, bottom);
		}

	};
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//Music.play(this);
		ExamProblemActivity.this.finish();
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
        switch (item.getItemId()) {
        case Menu.FIRST + 4:
        	AlertDialog.Builder builder = new AlertDialog.Builder(this); 
	        builder.setIcon(R.drawable.menu4);  
	        builder.setTitle("您确定要退出小试牛刀，回到主菜单吗？");  
	        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
	            public void onClick(DialogInterface dialog, int whichButton) {  
	                //这里添加点击确定后的逻辑  
	            	changeActivity = true;
	            	onDestroy();
	            }  
	        });  
	        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {  
	            public void onClick(DialogInterface dialog, int whichButton) {  
	                //这里添加点击确定后的逻辑  
	            }  
	        });  
	        builder.create().show();     
	        break;
        default:
        	return super.onOptionsItemSelected(item);
        }
        	
        return false;
		
	//	return super.onOptionsItemSelected(item);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
        	AlertDialog.Builder builder = new AlertDialog.Builder(this); 
	        builder.setIcon(R.drawable.menu4);  
	        builder.setTitle("您确定要退出小试牛刀，回到主菜单吗？？");  
	        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
	            public void onClick(DialogInterface dialog, int whichButton) {  
	                //这里添加点击确定后的逻辑  
	            	changeActivity = true;
	            	onDestroy();
	            }  
	        });  
	        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {  
	            public void onClick(DialogInterface dialog, int whichButton) {  
	                //这里添加点击确定后的逻辑  
	            }  
	        });  
	        builder.create().show();     
			return false;
		}
		else
			return super.onKeyDown(keyCode, event);
	}
	
	private int getEditTextCursorIndex(EditText mEditText){
		return mEditText.getSelectionStart();
	}
    private void insertText(EditText mEditText, CharSequence charSequence){
    	mEditText.getText().insert(getEditTextCursorIndex(mEditText), charSequence);
    }
}
