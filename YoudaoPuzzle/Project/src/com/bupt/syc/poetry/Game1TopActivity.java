package com.bupt.syc.poetry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Game1TopActivity extends MyActivity{

	Game1Score[] gScores = null;
	Button moreButton = null;
	Button BackButton = null;
	TextView gamefirstscoreTV = null;
	 ListView list = null;
	 int isMore = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamefirsttop);
		changeActivity = false;
		//Music.play(this);
		//排名，取出前10名
		Global.game1.game1TopRecord(Global.recordName, Global.score);
		gScores =  Global.game1.game1ShowTop(0);
		
		//绑定Layout里面的ListView
        list = (ListView) findViewById(R.id.gamefirsttopLV); 
        gamefirstscoreTV = (TextView)findViewById(R.id.gamefirstscoreTV);
        gamefirstscoreTV.setText(Global.game1.glr.getScore() + "分");
        //buttons
        BackButton = (Button)findViewById(R.id.gamefirstBackBTN);
        //BackButton.getBackground().setAlpha(156);
        BackButton.setOnClickListener(Listener);
        moreButton = (Button)findViewById(R.id.game1moreBTN);
        //moreButton.getBackground().setAlpha(156);
        moreButton.setOnClickListener(Listener);
        //ShowOffButton.getBackground().setAlpha(156);
        //ShowOffButton.setOnClickListener(Listener);
        
        if(gScores!=null)
           showTop();
	}
	
	private void showTop(){
		//生成动态数组，加入数据
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
 
        for(int i=0;i<10 && i<gScores.length;i++)
        {
        	HashMap<String, Object> map = new HashMap<String, Object>();
        	switch(i){
        	case 0:
        		if(isMore == 0)
        		   map.put("gamefirsttoplistimage", R.drawable.game1rank1);//图像资源的ID
        		else 
        			map.put("gamefirsttoplistimage", R.drawable.game1rank4to10);//图像资源的ID
				
        		break;
        	case 1:
        		if(isMore == 0)
        			map.put("gamefirsttoplistimage", R.drawable.game1rank2);//图像资源的ID
        		else 
        			map.put("gamefirsttoplistimage", R.drawable.game1rank4to10);//图像资源的ID
				
        		break;
        	case 2:
        		if(isMore == 0)
        			map.put("gamefirsttoplistimage", R.drawable.game1rank3);//图像资源的ID
        		else
        			map.put("gamefirsttoplistimage", R.drawable.game1rank4to10);//图像资源的ID
        		break;
        	default:
        		map.put("gamefirsttoplistimage", R.drawable.game1rank4to10);//图像资源的ID
        		break;
        	}
        	//取出对应名次的名字和分数
        	map.put("gamefirsttoplistname", gScores[i].name);
        	map.put("gamefirsttoplistscore", String.valueOf(gScores[i].score));
        	listItem.add(map);
        }
        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源 
            R.layout.gamefirsttoplist,//ListItem的XML实现
            //动态数组与ImageItem对应的子项        
            new String[] {"gamefirsttoplistimage","gamefirsttoplistname", "gamefirsttoplistscore"}, 
            //ImageItem的XML文件里面的一个ImageView,两个TextView ID
            new int[] {R.id.gamefirsttoplistimage,R.id.gamefirsttoplistname,R.id.gamefirsttoplistscore}
        );
       
        //添加并且显示
        list.setAdapter(listItemAdapter);
	}
	
	private OnClickListener Listener = new OnClickListener(){

		public void onClick(View v) {
			
			Button btn = (Button)v;
			switch (btn.getId()) {
			case R.id.gamefirstBackBTN:
				//Intent intent = new Intent();
				//intent.setClass(Game1TopActivity.this, MenuActivity.class);
				//startActivity(intent);
				onDestroy();
				break;
				
			case R.id.game1moreBTN:
				gScores =  Global.game1.game1ShowTop(1);
				isMore = 1;
				showTop();
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
		Game1TopActivity.this.finish();
	}
	
	
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
//		Music.stop(this);
//	}	

}
