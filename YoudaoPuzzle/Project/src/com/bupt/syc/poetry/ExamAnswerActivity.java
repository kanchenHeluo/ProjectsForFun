package com.bupt.syc.poetry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bupt.syc.poetry.R.layout;

public class ExamAnswerActivity extends MyActivity{

	TextView score;
	Button backBtn = null;
	ListView lView = null;
	Context context;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.examanswer);
		changeActivity = false;
		//Music.play(this);
		context = this.getApplicationContext();
		score = (TextView)findViewById(R.id.examScoreTV);
	    score.setText(String.valueOf(Global.score));
	    
	    //btn
	    backBtn = (Button)findViewById(R.id.examansbackbtn);
	    //backBtn.getBackground().setAlpha(156);
	    backBtn.setOnClickListener(Listener);
	    //
	    lView = (ListView)findViewById(R.id.examAnswerLV);
    	List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
    	for(int i = 0;i < Global.exam1.getAmount();i++){ //data
			Map<String, Object> item = new HashMap<String, Object>();
			ExamPoemList temp = Global.er.getExamPoemList()[i];
			String sen = "" + (i+1) +": ";
			for(int j = 0;j < temp.getPoetrySentence().length;j++){ //item
				if(temp.getOmit() != j){
					sen += (" " + temp.getPoetrySentence()[j]);
				}
				else
					sen += ("______________");
			}
			item.put("sentence", sen);
			data.add(item);		
			
			Map<String, Object> from = new HashMap<String, Object>();
			from.put("sentence", "出自： " + Global.er.getExamPoemList()[i].getAuthor() + "《" +Global.er.getExamPoemList()[i].getTitle() +"》");
			data.add(from);		
			
			Map<String, Object> itemAns = new HashMap<String, Object>();
			if(Global.er.getAnsList()[i] == null){
			
			    sen = "你的答案： " + " " + " 未填！";
			}else{
				sen = "你的答案： " + Global.er.getAnsList()[i];
				if(Global.er.getIsCorrect()[i] == true){
					sen+=" 正确!";
				}else
					sen+=" 错误!";
			}
			itemAns.put("sentence", sen);
			data.add(itemAns);	
			
			Map<String, Object> itemExAns = new HashMap<String, Object>();
			sen = "正确答案： " + Global.er.getExamPoemList()[i].getPoetrySentence()[Global.er.getExamPoemList()[i].getOmit()];
			itemExAns.put("sentence", sen);
			data.add(itemExAns);		
    	}	
    	ListAdapter adapter = new SpecialAdapter(context,data,R.layout.item1,new String[]{"sentence"},new int[]{R.id.sentence});
		lView.setAdapter(adapter);	
	    
	}
	
	private OnClickListener Listener = new OnClickListener(){

		public void onClick(View v) {
			Button btn = (Button)v;
			switch (btn.getId()) {
			case R.id.examansbackbtn:
				changeActivity = true;
				Intent intent = new Intent();
				intent.setClass(ExamAnswerActivity.this, MenuActivity.class);
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
		ExamAnswerActivity.this.finish();
	}
	
	class SpecialAdapter extends SimpleAdapter{  
	    public SpecialAdapter(Context context, List<? extends Map<String, ?>> data,  
	            int resource, String[] from, int[] to) {  
	        super(context, data, resource, from, to);  
	        // TODO Auto-generated constructor stub  
	    }  
	    
	    @Override  
	    public View getView(int position, View convertView, ViewGroup parent) {  
	        // TODO Auto-generated method stub  
	        View view = super.getView(position, convertView, parent);  
	        //int colorPos = position%colors.length;  
			  LinearLayout ll = (LinearLayout)view;
			  TextView tvn = (TextView)ll.getChildAt(0);
		        int pos = position/4;
		        int seq = position%4;
				if(Global.er.getAnsList()[pos] == null){
					
				    tvn.setTextColor(Color.BLUE);
				}else{
					if(Global.er.getIsCorrect()[pos] == true){
						tvn.setTextColor(Color.BLACK);
					}else
						tvn.setTextColor(Color.RED);
				}

	        
	        return view;  
	    }  
	} 
	
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
//		Music.stop(this);
//	}
}
