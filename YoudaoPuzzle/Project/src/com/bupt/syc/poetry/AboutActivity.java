package com.bupt.syc.poetry;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends MyActivity{
    TextView t = null;
    String s = "��������\n\n\n"+
    		"�汾��V1.10\n\n" +
    		"���ߣ����/������/��٩\n\n" +
    		"   �����Ը���Ϸ���κε�����ͽ��飬��ӭ��ϵyangjie@bupt.edu.cn.";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		changeActivity = false;
		setContentView(R.layout.aboutview);	
		t = (TextView)findViewById(R.id.helptxt);
		t.setText(s);	
		
		
		
		t.setTextColor(Color.BLACK);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		finish();
		
		//Music.stop(this);
	}
}
