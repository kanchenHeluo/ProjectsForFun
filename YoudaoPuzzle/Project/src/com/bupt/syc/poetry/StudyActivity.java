package com.bupt.syc.poetry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class StudyActivity extends MyActivity{
	private Study study;
	private studyCondition sc;
	private ArrayAdapter<String> styleAdapter;
	HashSet<String> dynastyHash = new HashSet<String>();
	
	ToggleButton BeforeTangButton = null;
	ToggleButton TangButton = null;
	ToggleButton SongButton = null;
	ToggleButton YuanButton = null;
	ToggleButton MingButton = null;
	ToggleButton QingButton = null;
	ToggleButton AfterQingButton = null;
	Spinner styleSpinner = null;
	Spinner poetSpinner = null;
	EditText inputBox = null;
	boolean ArrayFlag[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.studyview);
		setContentView(R.layout.studyview);
		
		//
		study=new Study();
		sc = new studyCondition();
		List<StudyPoemList> poemList;
		poemList = study.getPoemList(sc);
		SetListItems(poemList);
		changeActivity = false;
       // Music.play(this);
        
        //Dynasty button Action
        ArrayFlag=new boolean[7];
        BeforeTangButton = (ToggleButton)findViewById(R.id.btnBeforeTang);
		TangButton = (ToggleButton)findViewById(R.id.btnTang);
		SongButton = (ToggleButton)findViewById(R.id.btnSong);
		YuanButton = (ToggleButton)findViewById(R.id.btnYuan);
		MingButton = (ToggleButton)findViewById(R.id.btnMing);
		QingButton = (ToggleButton)findViewById(R.id.btnQing);
		AfterQingButton = (ToggleButton)findViewById(R.id.btnAfterQing);
		
		BeforeTangButton.setOnClickListener(DynastyListener);
		TangButton.setOnClickListener(DynastyListener);
		SongButton.setOnClickListener(DynastyListener);
		YuanButton.setOnClickListener(DynastyListener);
		MingButton.setOnClickListener(DynastyListener);
		QingButton.setOnClickListener(DynastyListener);
		AfterQingButton.setOnClickListener(DynastyListener);
		
		//spinner Action
		poetSpinner = (Spinner)findViewById(R.id.studyAuthor);
		List<String> poetList = study.getAuthorList(sc.getDynasty());
		SetPoetItems(poetList);
		styleSpinner = (Spinner)findViewById(R.id.studyStyle);
		List<String> styleList = study.getStyleList(sc.getDynasty());
		SetStyleItems(styleList);
		
		//Input box Action
		inputBox = (EditText) findViewById(R.id.StudyPoemNameEdit);
		inputBox.addTextChangedListener(InputBoxListener);
		
		//Button Action
		Button GetMarkedButton = (Button)findViewById(R.id.studiedList);
		//GetMarkedButton.getBackground().setAlpha(156);
		GetMarkedButton.setOnClickListener(Listener);
		Button RandSelectButton = (Button)findViewById(R.id.randomChoose);
		//RandSelectButton.getBackground().setAlpha(156);
		RandSelectButton.setOnClickListener(Listener);
	}
	private TextWatcher InputBoxListener = new TextWatcher() {
        public void afterTextChanged(Editable s) {     
            // TODO Auto-generated method stub
        }
        public void beforeTextChanged(CharSequence s, int start, int count,  
                int after) {  
            // TODO Auto-generated method stub  
        }    
        public void onTextChanged(CharSequence s, int start, int before,     
                int count){
        	sc.setTitleMatched(inputBox.getText().toString());
        	List<StudyPoemList> poemList;
    		poemList = study.getPoemList(sc);
    		SetListItems(poemList);
        }
	};
	private void SetPoetItems(final List<String> tmp){
		//准备数据
		final List<String> poetList = new ArrayList<String>();
		poetList.add("任意");
		for(String iPoet:tmp){
			poetList.add(iPoet);
		}
		ArrayAdapter<String> poetAdapter;
		String lastSelection = (String) poetSpinner.getSelectedItem();//记录之前选中选项
		poetAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, poetList);
		poetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		//更新数据
		poetSpinner.setAdapter(poetAdapter);
		poetSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                /* 将mySpinner 显示*/
                arg0.setVisibility(View.VISIBLE);
                //设置选中诗人
                sc.setAuthor(arg2==0?null:poetList.get(arg2));
                //更新诗词列表
        		List<StudyPoemList> poemList;
        		poemList = study.getPoemList(sc);
        		SetListItems(poemList);
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                arg0.setVisibility(View.VISIBLE);
            }
        });
		//设置之前的选中数据
		if(poetList.indexOf(lastSelection)>=0){
			poetSpinner.setSelection(poetList.indexOf(lastSelection),true);
		}
		else{
			sc.setAuthor(null);
		}
	}
	private void SetStyleItems(final List<String> tmp){
		//准备数据
		final List<String> styleList = new ArrayList<String>();
		styleList.add("任意");
		for(String iStyle:tmp){
			styleList.add(iStyle);
		}
		ArrayAdapter<String> styleAdapter;
		String lastSelection = (String) styleSpinner.getSelectedItem();//记录之前选中选项
		styleAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, styleList);
		styleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		//更新数据
		styleSpinner.setAdapter(styleAdapter);
		styleSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                /* 将mySpinner 显示*/
                arg0.setVisibility(View.VISIBLE);
                //设置选中风格
                sc.setStyle(arg2==0?null:styleList.get(arg2));
                //更新列表
        		List<StudyPoemList> poemList;
        		poemList = study.getPoemList(sc);
        		SetListItems(poemList);
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                arg0.setVisibility(View.VISIBLE);
            }
        });
		//设置之前的选中数据
		if(styleList.indexOf(lastSelection)>=0){
			styleSpinner.setSelection(styleList.indexOf(lastSelection),true);
		}
		else{
			sc.setStyle(null);
		}
	}
	private void SetListItems(final List<StudyPoemList> poemList){
		
		//绑定Layout里面的ListView
        ListView list = (ListView) findViewById(R.id.studiedListView);
        //生成动态数组，加入数据
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        for(StudyPoemList iPoem:poemList)
        {
        	HashMap<String, Object> map = new HashMap<String, Object>();
        	map.put("ItemTitle", iPoem.getTitle());
        	map.put("ItemAuthor", iPoem.getAuthor());
        	listItem.add(map);
        }
        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源 
            R.layout.studylistview,//ListItem的XML实现
            //动态数组与ImageItem对应的子项        
            new String[] {"ItemTitle", "ItemAuthor"}, 
            //ImageItem的XML文件里面的一个ImageView,两个TextView ID
            new int[] {R.id.ItemTitle,R.id.ItemAuthor}
        );
       
        //添加并且显示
        list.setAdapter(listItemAdapter);
        
        //添加点击
        list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				setTitle("正在打开");
				changeActivity = true;
				Global.studypoem = study.getpoem(poemList.get(arg2).getpId());
				Intent intent = new Intent();
				Bundle mBundle = new Bundle();
				mBundle.putInt("pid", poemList.get(arg2).getpId());
				intent.putExtras(mBundle); 
				intent.setClass(StudyActivity.this, StudyPoemActivity.class);
				startActivity(intent);
			}
		});
	}
	private OnClickListener DynastyListener = new OnClickListener(){

		public void onClick(View v) {
			
			ToggleButton btn = (ToggleButton)v;
			switch (btn.getId()) {
			case R.id.btnBeforeTang:
				ArrayFlag[0]=ArrayFlag[0]==true?false:true;
				break;
			case R.id.btnTang:
				ArrayFlag[1]=ArrayFlag[1]==true?false:true;
				break;
			case R.id.btnSong:
				ArrayFlag[2]=ArrayFlag[2]==true?false:true;
				break;
			case R.id.btnYuan:
				ArrayFlag[3]=ArrayFlag[3]==true?false:true;
				break;
			case R.id.btnMing:
				ArrayFlag[4]=ArrayFlag[4]==true?false:true;
				break;
			case R.id.btnQing:
				ArrayFlag[5]=ArrayFlag[5]==true?false:true;
				break;
			case R.id.btnAfterQing:
				ArrayFlag[6]=ArrayFlag[6]==true?false:true;
				break;
			default:
				break;
			}
			int bottom = btn.getPaddingBottom();
		    int top = btn.getPaddingTop();
		    int right = btn.getPaddingRight();
		    int left = btn.getPaddingLeft();
			if(!btn.isChecked()){
				btn.setBackgroundResource(R.drawable.dynasty_btn_normal);
				dynastyHash.remove((String) btn.getText());
				btn.getBackground().setAlpha(255);
			}
			else{
				btn.setBackgroundResource(R.drawable.dynasty_btn_pressed);
				dynastyHash.add((String) btn.getText());
				btn.getBackground().setAlpha(64);
			}
			btn.setPadding(left, top, right, bottom);
			sc.setDynasty(dynastyHash.toArray(new String[dynastyHash.size()]));
			List<StudyPoemList> poemList;
			List<String> poetList = study.getAuthorList(sc.getDynasty());
			SetPoetItems(poetList);
			List<String> styleList = study.getStyleList(sc.getDynasty());
			SetStyleItems(styleList);
			poemList = study.getPoemList(sc);
			SetListItems(poemList);
		}

	};
	private OnClickListener Listener = new OnClickListener(){

		public void onClick(View v) {
			
			Button btn = (Button)v;
			switch (btn.getId()) {
			case R.id.studiedList:
				resetButtons();
				studyCondition scMarked = new studyCondition();
				scMarked.setMarked(true);
				scMarked.setAlbum("Marked");
				List<StudyPoemList> poemList;
				poemList = study.getPoemList(scMarked);
				SetListItems(poemList);
				break;
			case R.id.randomChoose:
				List<StudyPoemList> poemListR;
				poemListR = study.getPoemList(sc,5);
				SetListItems(poemListR);
				break;
			default:
				break;
			}
			//startActivity(intent);
		}

	};
	private void resetButtons(){
		BeforeTangButton.setChecked(false);
		TangButton.setChecked(false);
		SongButton.setChecked(false);
		YuanButton.setChecked(false);
		MingButton.setChecked(false);
		QingButton.setChecked(false);
		AfterQingButton.setChecked(false);
		int bottom = BeforeTangButton.getPaddingBottom();
	    int top = BeforeTangButton.getPaddingTop();
	    int right = BeforeTangButton.getPaddingRight();
	    int left = BeforeTangButton.getPaddingLeft();
		BeforeTangButton.setBackgroundResource(R.drawable.dynasty_btn_normal);
		BeforeTangButton.setPadding(left, top, right, bottom);
		TangButton.setBackgroundResource(R.drawable.dynasty_btn_normal);
		TangButton.setPadding(left, top, right, bottom);
		SongButton.setBackgroundResource(R.drawable.dynasty_btn_normal);
		SongButton.setPadding(left, top, right, bottom);
		YuanButton.setBackgroundResource(R.drawable.dynasty_btn_normal);
		YuanButton.setPadding(left, top, right, bottom);
		MingButton.setBackgroundResource(R.drawable.dynasty_btn_normal);
		MingButton.setPadding(left, top, right, bottom);
		QingButton.setBackgroundResource(R.drawable.dynasty_btn_normal);
		QingButton.setPadding(left, top, right, bottom);
		AfterQingButton.setBackgroundResource(R.drawable.dynasty_btn_normal);
		AfterQingButton.setPadding(left, top, right, bottom);
		inputBox.setText("");
		if(poetSpinner.getSelectedItemPosition()!=0) poetSpinner.setSelection(0);
		if(styleSpinner.getSelectedItemPosition()!=0) styleSpinner.setSelection(0);
		sc = new studyCondition();
		List<String> poetList = study.getAuthorList(sc.getDynasty());
		SetPoetItems(poetList);
		List<String> styleList = study.getStyleList(sc.getDynasty());
		SetStyleItems(styleList);
		
	}
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
//		Music.stop(this);
//	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		StudyActivity.this.finish();
		//Music.stop(this);
	}
//	@Override
//	protected void onRestart() {
//		// TODO Auto-generated method stub
//		super.onRestart();
//		Music.play(this);
//	}
	
	
}
