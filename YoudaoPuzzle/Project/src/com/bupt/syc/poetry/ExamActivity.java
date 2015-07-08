package com.bupt.syc.poetry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ExamActivity extends MyActivity{
   
	public ToggleButton[] range = new ToggleButton[2];
	public ToggleButton[] dynastyBtns = new ToggleButton[7];
	public ToggleButton[] problemNum = new ToggleButton[4];
	public Button startExamBtn = null;
	public Spinner authorSpinner = null;
	public Spinner styleSpinner = null;
	
    public String[] authorItems = new String[300];
    public String[] styleItems = new String[300];
	
    Exam1 exam1 = new Exam1();
	HashSet<String> dynasity = new HashSet<String>();

	ExamCondition examCondition = new ExamCondition(null,null,10,false,null,null);
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.examview);
		changeActivity = false;
		//初始化界面控件相关信息
		authorItems = getResources().getStringArray(R.array.authorname);
		styleItems = getResources().getStringArray(R.array.styles);
		authorSpinner = (Spinner)findViewById(R.id.examAuthor); //改成动态生成 下拉项 
		initMySpinner(authorSpinner);
		styleSpinner = (Spinner)findViewById(R.id.examStyle);
		initMySpinner(styleSpinner);
		
		//按钮 
	    range[0] = (ToggleButton)findViewById(R.id.examstudied);
		range[1] = (ToggleButton)findViewById(R.id.examRandom);		
		dynastyBtns[0] = (ToggleButton)findViewById(R.id.btnBeforeTang);
		dynastyBtns[1] = (ToggleButton)findViewById(R.id.btnTang);
	    dynastyBtns[2] = (ToggleButton)findViewById(R.id.btnSong);
	    dynastyBtns[3] = (ToggleButton)findViewById(R.id.btnYuan);
	    dynastyBtns[4] = (ToggleButton)findViewById(R.id.btnMing);
	    dynastyBtns[5] = (ToggleButton)findViewById(R.id.btnQing);
	    dynastyBtns[6] = (ToggleButton)findViewById(R.id.btnAfterQing);
	    problemNum[0] = (ToggleButton)findViewById(R.id.exambtn10);
	    problemNum[1] = (ToggleButton)findViewById(R.id.exambtn20);
	    problemNum[2] = (ToggleButton)findViewById(R.id.exambtn30);
	    problemNum[3] = (ToggleButton)findViewById(R.id.exambtn50);
	    startExamBtn = (Button)findViewById(R.id.startExam);
	    
        //绑定监听器 
		range[0].setOnCheckedChangeListener(toggleBtnListener);
		range[1].setOnCheckedChangeListener(toggleBtnListener);
		range[0].setChecked(false);
		range[1].setChecked(true);
		
		examCondition.setMarked(false); //默认随机
		
	    for(int i=0; i<7; i++){
	    	dynastyBtns[i].setOnCheckedChangeListener(toggleBtnListener);
	    	dynastyBtns[i].setChecked(false);
	    }
	    for(int i=1; i<4; i++){
	       problemNum[i].setOnCheckedChangeListener(toggleBtnListener);
	       problemNum[i].setChecked(false);
	    }
	    problemNum[0].setOnCheckedChangeListener(toggleBtnListener);
	    problemNum[0].setChecked(true);
	    examCondition.setAmount(10);
		startExamBtn.setOnClickListener(Listener);
		
	}
	
	private void updateMySpinner(Spinner s){
		String lastSelection = (String) s.getSelectedItem();
		if(s.getId() == R.id.examAuthor){
			int f = 0;
			int i;
			for(i=0;i<authorItems.length;i++){
				if(authorItems[i].equals(lastSelection)){
					f = 1;
					break;
				}
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(ExamActivity.this, android.R.layout.simple_spinner_item,authorItems);//判断一下authoritems
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
			s.setAdapter(adapter);
			if(1 == f){
				s.setSelection(i,true);
			}else {
				s.setSelection(0,true);//调用一次
			}
		}
		if(s.getId() == R.id.examStyle){
			int f = 0;
			int i;
			for(i=0;i<styleItems.length;i++){
				if(styleItems[i].equals(lastSelection)){
					f = 1;
					break;
				}
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(ExamActivity.this, android.R.layout.simple_spinner_item,styleItems);//判断一下styleitems
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
			s.setAdapter(adapter);
			if(1 == f){
				s.setSelection(i,true);
			}else {
				s.setSelection(0,true);//调用一次y 
			}
		}
	}
	
	private void initMySpinner(Spinner s){
		if(s.getId() == R.id.examAuthor){	
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(ExamActivity.this, android.R.layout.simple_spinner_item,authorItems);//判断一下authoritems
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
			s.setAdapter(adapter);
			s.setPrompt("诗人");
			s.setSelection(0,true);//调用一次
			s.setOnItemSelectedListener((OnItemSelectedListener)new SpinnerOnSelectedListener());
		}
		if(s.getId() == R.id.examStyle){
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(ExamActivity.this, android.R.layout.simple_spinner_item,styleItems);//判断一下styleitems
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
			s.setAdapter(adapter);
			s.setPrompt("派别");
			s.setSelection(0,true);//调用一次
			s.setOnItemSelectedListener((OnItemSelectedListener)new SpinnerOnSelectedListener());
		}
	}
	
	class SpinnerOnSelectedListener implements OnItemSelectedListener{
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Spinner sp = (Spinner)arg0;
			String selected = arg0.getItemAtPosition(arg2).toString();
			
			if(arg0.getId() == authorSpinner.getId()){
				//Toast.makeText(ExamActivity.this, selected, Toast.LENGTH_LONG).show();
			   examCondition.setAuthor(selected);

			}
			if(arg0.getId() == styleSpinner.getId()){
				//Toast.makeText(ExamActivity.this, selected, Toast.LENGTH_LONG).show();
				examCondition.setStyle(selected);
			}
		}

		public void onNothingSelected(AdapterView<?> arg0){
			
		}

	}
	private OnCheckedChangeListener toggleBtnListener = new OnCheckedChangeListener(){

		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			ToggleButton tButton = (ToggleButton)buttonView;
			boolean conditionChanged = false;

			int bottom = tButton.getPaddingBottom();
		    int top = tButton.getPaddingTop();
		    int right = tButton.getPaddingRight();
		    int left = tButton.getPaddingLeft();
			switch (tButton.getId()) {
				case R.id.btnBeforeTang:
				case R.id.btnTang:
				case R.id.btnSong:
				case R.id.btnYuan:
				case R.id.btnMing:
				case R.id.btnQing:
				case R.id.btnAfterQing:
					conditionChanged = true;
					if(isChecked){
						tButton.setBackgroundResource(R.drawable.dynasty_btn_pressed);
						dynasity.add((String) tButton.getText());
						tButton.getBackground().setAlpha(64);
					}
					else{
						tButton.setBackgroundResource(R.drawable.dynasty_btn_normal);
						dynasity.remove((String) tButton.getText());
						tButton.getBackground().setAlpha(255);
					}
					break;
				case R.id.examstudied:
					conditionChanged = true;
				    if(isChecked){
				    	range[0].setChecked(true);
				    	range[1].setChecked(false);
				    	examCondition.setMarked(true);
				    	range[0].setBackgroundResource(R.drawable.dynasty_btn_pressed);
				    	range[1].setBackgroundResource(R.drawable.dynasty_btn_normal);
				    	range[0].setPadding(left, top, right, bottom);
				    	range[1].setPadding(left, top, right, bottom);
				    }
					break;
				case R.id.examRandom:
					conditionChanged = true;
					if(isChecked){
				    	range[0].setChecked(false);
				    	range[1].setChecked(true);
				    	examCondition.setMarked(false);
				    	range[0].setBackgroundResource(R.drawable.dynasty_btn_normal);
				    	range[1].setBackgroundResource(R.drawable.dynasty_btn_pressed);
				    	range[0].setPadding(left, top, right, bottom);
				    	range[1].setPadding(left, top, right, bottom);
				    }
					
					break;
	
				case R.id.exambtn10:
					
					if(isChecked){
						problemNum[0].setChecked(true);
						problemNum[0].setBackgroundResource(R.drawable.dynasty_btn_pressed);
						for(int i=1;i<4;i++){
							problemNum[i].setChecked(false);
							problemNum[i].setBackgroundResource(R.drawable.dynasty_btn_normal);
							problemNum[i].setPadding(left, top, right, bottom);
						}
						examCondition.setAmount(10);
					}
					break;
				case R.id.exambtn20:
					if(isChecked){
						for(int i=0;i<4;i++){
							if(i!=1){
								problemNum[i].setChecked(false);
								problemNum[i].setBackgroundResource(R.drawable.dynasty_btn_normal);
								problemNum[i].setPadding(left, top, right, bottom);
							}
							else {
								problemNum[1].setChecked(true);
								problemNum[i].setBackgroundResource(R.drawable.dynasty_btn_pressed);
							}
						}
						examCondition.setAmount(20);
					}
					break;
				case R.id.exambtn30:
					if(isChecked){
						for(int i=0;i<4;i++){
							if(i!=2){
								problemNum[i].setChecked(false);
								problemNum[i].setBackgroundResource(R.drawable.dynasty_btn_normal);
								problemNum[i].setPadding(left, top, right, bottom);
							}
							else{
								problemNum[i].setChecked(true);
								problemNum[i].setBackgroundResource(R.drawable.dynasty_btn_pressed);
							}
						}
						examCondition.setAmount(30);
					}
					break;
				case R.id.exambtn50:
					if(isChecked){
						for(int i=0;i<3;i++){
							problemNum[i].setChecked(false);
							problemNum[i].setBackgroundResource(R.drawable.dynasty_btn_normal);
							problemNum[i].setPadding(left, top, right, bottom);
						}
						problemNum[3].setChecked(true);
						problemNum[3].setBackgroundResource(R.drawable.dynasty_btn_pressed);
						examCondition.setAmount(50);
					}
					break;
	
				default:
					break;
			}
			tButton.setPadding(left, top, right, bottom);
			
			if(conditionChanged){
				//更新两个下拉列表
				int i=0;
				String[] str = new String[dynasity.size()+1];
				Iterator iterator = dynasity.iterator();
				while(iterator.hasNext()){
					
					str[i] = (String) iterator.next();
					//Toast.makeText(ExamActivity.this, str[i], Toast.LENGTH_LONG).show();
					i++;
					
				}
		    	examCondition.setDynasty(str);

		    	List<String> tmp;		    	
		    	tmp = exam1.getAuthorList(str,examCondition.isMarked); 
		    	tmp.add(0,"任意");
		    	authorItems = tmp.toArray(new String[0]);
		    	//cpy tmp to authorItems  + "renyi"
		    	tmp = exam1.getStyleList(str,examCondition.isMarked);
		    	tmp.add(0,"任意");
		    	styleItems = tmp.toArray(new String[0]);
		    	//cpy tmp to styleItems + "renyi"
		    	
		    	updateMySpinner(authorSpinner);
		    	updateMySpinner(styleSpinner);	
		    	conditionChanged = false;
			}
		}
	};
	private OnClickListener Listener = new OnClickListener(){
		public void onClick(View v) {
			Button btn = (Button)v;
			
			switch (btn.getId()) {
			case R.id.startExam:	
				//examCondition.setAuthor("李白");
				//ExamActivity.this.setTitle(examCondition.getAuthor());
				//UI
				int bottom = btn.getPaddingBottom();
			    int top = btn.getPaddingTop();
			    int right = btn.getPaddingRight();
			    int left = btn.getPaddingLeft();
				btn.setBackgroundResource(R.drawable.test_start_click);
				btn.setPadding(left, top, right, bottom);
				
				exam1.getPoemList(examCondition);
				Global.exam1 = exam1;
				
				changeActivity = true;
				Intent intent = new Intent();
				intent.setClass(ExamActivity.this, ExamProblemActivity.class);
				startActivity(intent);
				onDestroy();
				
				break;
			default:
				break;
			}
			
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//Music.stop(this);
		ExamActivity.this.finish();
	}
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
//		Music.stop(this);
//	}
}
