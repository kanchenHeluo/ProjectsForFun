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
		//��ȡ������pid
		Bundle bundle = getIntent().getExtras();
		pid=bundle.getInt("pid");
		//��Layout�����ListView
        ListView list = (ListView) findViewById(R.id.studyPoemContentLV);
        //���ɶ�̬���飬��������
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
        //������������Item�Ͷ�̬�����Ӧ��Ԫ��
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//����Դ 
            R.layout.studypoemlistview,//ListItem��XMLʵ��
            //��̬������ImageItem��Ӧ������        
            new String[] {"Content"}, 
            //ImageItem��XML�ļ������һ��ImageView,����TextView ID
            new int[] {R.id.Content}
        );
        //��Ŀ����
        LinearLayout headerView = (LinearLayout)getLayoutInflater().inflate(R.layout.studypoemlist_header, null);
        list.addHeaderView(headerView);
        TextView txtT=(TextView)findViewById(R.id.studyPoemNameTV);
        txtT.setText(Global.studypoem.getTitle());
        TextView txtP=(TextView)findViewById(R.id.studyPoemAuthorTV);
        txtP.setText(Global.studypoem.getAuthor());

        //����text
        LinearLayout footerView = (LinearLayout)getLayoutInflater().inflate(R.layout.studypoemlist_footer, null);
        list.addFooterView(footerView);
        TextView txtA=(TextView)findViewById(R.id.appreciationTV);
        txtA.setText("��������"+Global.studypoem.getAppreciation());
        
        //��Ӳ�����ʾ
        list.setAdapter(listItemAdapter);
        
        //buttons
        Button BackButton = (Button)findViewById(R.id.back);
        //BackButton.getBackground().setAlpha(156);
        BackButton.setOnClickListener(Listener);
        Button MarkButton = (Button)findViewById(R.id.AddToAlbum);
        //MarkButton.getBackground().setAlpha(156);
        if(Study.isLeaned(pid))
        	MarkButton.setText("����ѧȡ��");
        else
        	MarkButton.setText("��ӵ���ѧ");
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
					//setTitle("�����");
					study.mark(pid, "Marked");
					//setTitle("��ӳɹ�");
					Toast.makeText(StudyPoemActivity.this, "��ӳɹ�", Toast.LENGTH_LONG).show();
					btn.setText("����ѧȡ��");
				}
				else{
					//setTitle("ȡ����");
					study.unmark(pid);
					//setTitle("ȡ���ɹ�");
					Toast.makeText(StudyPoemActivity.this, "ȡ���ɹ�", Toast.LENGTH_LONG).show();
					btn.setText("��ӵ���ѧ");
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
