package com.bupt.syc.poetry;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class StudyPoemActivity extends MyActivity {
	int pid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.studypoemview);
		changeActivity = false;
		//Music.play(this);
		//获取传到的pid
		Bundle bundle = getIntent().getExtras();
		pid=bundle.getInt("pid");
		//绑定Layout里面的ListView
        ListView list = (ListView) findViewById(R.id.studyPoemContentLV);
        //生成动态数组，加入数据
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    	HashMap<String, Object> map = new HashMap<String, Object>();
    	//produce data
    	String poemContent="";
    	String[] poemSentences=Global.studypoem.getPoems();
    	for(String iSentence:poemSentences){
    		poemContent+=iSentence+"\n";
    	}
    	
    	map.put("Content", poemContent);
    	listItem.add(map);
        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源 
            R.layout.studypoemlistview,//ListItem的XML实现
            //动态数组与ImageItem对应的子项        
            new String[] {"Content"}, 
            //ImageItem的XML文件里面的一个ImageView,两个TextView ID
            new int[] {R.id.Content}
        );
        //题目作者
        LinearLayout headerView = (LinearLayout)getLayoutInflater().inflate(R.layout.studypoemlist_header, null);
        list.addHeaderView(headerView);
        TextView txtT=(TextView)findViewById(R.id.studyPoemNameTV);
        txtT.setText(Global.studypoem.getTitle());
        TextView txtP=(TextView)findViewById(R.id.studyPoemAuthorTV);
        txtP.setText(Global.studypoem.getAuthor());

        //赏析text
        LinearLayout footerView = (LinearLayout)getLayoutInflater().inflate(R.layout.studypoemlist_footer, null);
        list.addFooterView(footerView);
        TextView txtA=(TextView)findViewById(R.id.appreciationTV);
        txtA.setText("【赏析】"+Global.studypoem.getAppreciation());
        
        //添加并且显示
        list.setAdapter(listItemAdapter);
        
        //buttons
        Button BackButton = (Button)findViewById(R.id.back);
        //BackButton.getBackground().setAlpha(156);
        BackButton.setOnClickListener(Listener);
        Button MarkButton = (Button)findViewById(R.id.AddToAlbum);
        //MarkButton.getBackground().setAlpha(156);
        if(Study.isLeaned(pid))
        	MarkButton.setText("从已学取消");
        else
        	MarkButton.setText("添加到已学");
        MarkButton.setOnClickListener(Listener);
        
	}
	private OnClickListener Listener = new OnClickListener(){

		public void onClick(View v) {
			
			Button btn = (Button)v;
			switch (btn.getId()) {
			case R.id.back:
				changeActivity = true;
				finish();
				break;
			case R.id.AddToAlbum:
				Study study = new Study();
				if(!Study.isLeaned(pid)){
					//setTitle("添加中");
					study.mark(pid, "Marked");
					//setTitle("添加成功");
					Toast.makeText(StudyPoemActivity.this, "添加成功", Toast.LENGTH_LONG).show();
					btn.setText("从已学取消");
				}
				else{
					//setTitle("取消中");
					study.unmark(pid);
					//setTitle("取消成功");
					Toast.makeText(StudyPoemActivity.this, "取消成功", Toast.LENGTH_LONG).show();
					btn.setText("添加到已学");
				}				
				break;
			default:
				break;
			}
			//startActivity(intent);
		}

	};
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		StudyPoemActivity.this.finish();
		//Music.stop(this);
	}
	
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
//		Music.stop(this);
//	}
}
