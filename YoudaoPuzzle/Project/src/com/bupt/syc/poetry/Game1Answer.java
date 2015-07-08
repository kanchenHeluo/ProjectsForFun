package com.bupt.syc.poetry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Game1Answer extends MyActivity{
	TextView score;
	Button backBtn = null;
	
	ListView lView = null;
	Context context;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamefirstanswer);
		changeActivity = false;
		//Music.play(this);
		
		context = this.getApplicationContext();
		score = (TextView)findViewById(R.id.game1ScoreTV);
		Global.score = Global.game1.glr.getScore();
	    score.setText(String.valueOf(Global.score));  
	    
	    backBtn = (Button)findViewById(R.id.game1ansbackbtn);
	    backBtn.setOnClickListener(Listener);
	    //
	    lView = (ListView)findViewById(R.id.game1AnswerLV);
    	List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
    	for(int i = 0;i < Global.game1.glr.getRightAnswer().size();i++){  //������ 
			Map<String, Object> item = new HashMap<String, Object>();
			Game1PoemList temp = Global.game1.glr.getRightAnswer().get(i);
			String sen = "" + temp.seq +": ";
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
			from.put("sentence", "���ԣ� " + Global.game1.glr.getRightAnswer().get(i).getAuthor() + "��" +Global.game1.glr.getRightAnswer().get(i).getName() +"��");
			data.add(from);	
			
			Map<String, Object> itemAns = new HashMap<String, Object>();
			sen = Global.game1.glr.getWrongAnswer().get(i); //ȡ��
			if(sen != null && !sen.equals(""))
				itemAns.put("sentence", "���Ĵ𰸣� " + sen);
			else
				itemAns.put("sentence", "���Ĵ𰸣� "  + "��û����д����Ĵ�");
			data.add(itemAns);	
			
			Map<String, Object> itemExAns = new HashMap<String, Object>();
			//sen = "��ȷ�𰸣� " + Global.game1.glr.  .getPoetrySentence()[Global.er.getExamPoemList()[i].getOmit()]; 
			sen = "��ȷ�𰸣� " + Global.game1.glr.getRightAnswer().get(i).getPoetrySentence()[Global.game1.glr.getRightAnswer().get(i).omit];//ȡ��ȷ��
			itemExAns.put("sentence", sen);
			data.add(itemExAns);		
    	}	
    	ListAdapter adapter = new SimpleAdapter(context,data,R.layout.item1,new String[]{"sentence"},new int[]{R.id.sentence});
		lView.setAdapter(adapter);	
	}
	
	private OnClickListener Listener = new OnClickListener(){

		public void onClick(View v) {
			Button btn = (Button)v;
			switch (btn.getId()) {
			case R.id.game1ansbackbtn:
				//�������Ƿ�洢����
				final EditText inputServer = new EditText(Game1Answer.this);
		        AlertDialog.Builder builder = new AlertDialog.Builder(Game1Answer.this);
		        builder.setTitle("�����¼�������������").setIcon(R.drawable.exam1hh).setView(inputServer)
		                .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int which) {
				            	changeActivity = true;
//				            	Intent intent = new Intent();
//								intent.setClass(Game1Answer.this, MenuActivity.class);
//								startActivity(intent);
								onDestroy();
				             }
				        });
		        
		        builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

		            public void onClick(DialogInterface dialog, int which) {
		              
		             changeActivity = true;
		               Intent intent = new Intent();
					   intent.setClass(Game1Answer.this, Game1TopActivity.class);
					   String name = inputServer.getText().toString();
					   
					   Global.recordName = name;
					   
					   startActivity(intent);
					   onDestroy();
		             }
		        });
		        builder.show();			
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
		Game1Answer.this.finish();
	}
	
	
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
//		Music.stop(this);
//	}
	
}
