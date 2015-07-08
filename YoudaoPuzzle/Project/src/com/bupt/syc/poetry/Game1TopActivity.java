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
		//������ȡ��ǰ10��
		Global.game1.game1TopRecord(Global.recordName, Global.score);
		gScores =  Global.game1.game1ShowTop(0);
		
		//��Layout�����ListView
        list = (ListView) findViewById(R.id.gamefirsttopLV); 
        gamefirstscoreTV = (TextView)findViewById(R.id.gamefirstscoreTV);
        gamefirstscoreTV.setText(Global.game1.glr.getScore() + "��");
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
		//���ɶ�̬���飬��������
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
 
        for(int i=0;i<10 && i<gScores.length;i++)
        {
        	HashMap<String, Object> map = new HashMap<String, Object>();
        	switch(i){
        	case 0:
        		if(isMore == 0)
        		   map.put("gamefirsttoplistimage", R.drawable.game1rank1);//ͼ����Դ��ID
        		else 
        			map.put("gamefirsttoplistimage", R.drawable.game1rank4to10);//ͼ����Դ��ID
				
        		break;
        	case 1:
        		if(isMore == 0)
        			map.put("gamefirsttoplistimage", R.drawable.game1rank2);//ͼ����Դ��ID
        		else 
        			map.put("gamefirsttoplistimage", R.drawable.game1rank4to10);//ͼ����Դ��ID
				
        		break;
        	case 2:
        		if(isMore == 0)
        			map.put("gamefirsttoplistimage", R.drawable.game1rank3);//ͼ����Դ��ID
        		else
        			map.put("gamefirsttoplistimage", R.drawable.game1rank4to10);//ͼ����Դ��ID
        		break;
        	default:
        		map.put("gamefirsttoplistimage", R.drawable.game1rank4to10);//ͼ����Դ��ID
        		break;
        	}
        	//ȡ����Ӧ���ε����ֺͷ���
        	map.put("gamefirsttoplistname", gScores[i].name);
        	map.put("gamefirsttoplistscore", String.valueOf(gScores[i].score));
        	listItem.add(map);
        }
        //������������Item�Ͷ�̬�����Ӧ��Ԫ��
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//����Դ 
            R.layout.gamefirsttoplist,//ListItem��XMLʵ��
            //��̬������ImageItem��Ӧ������        
            new String[] {"gamefirsttoplistimage","gamefirsttoplistname", "gamefirsttoplistscore"}, 
            //ImageItem��XML�ļ������һ��ImageView,����TextView ID
            new int[] {R.id.gamefirsttoplistimage,R.id.gamefirsttoplistname,R.id.gamefirsttoplistscore}
        );
       
        //��Ӳ�����ʾ
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
