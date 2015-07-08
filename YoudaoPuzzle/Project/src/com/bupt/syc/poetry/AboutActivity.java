package com.bupt.syc.poetry;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends MyActivity{
    TextView t = null;
    String s = "关于我们\n\n\n"+
    		"版本：V1.10\n\n" +
    		"作者：杨杰/苏玉林/陈侃\n\n" +
    		"   若您对该游戏有任何的意见和建议，欢迎联系yangjie@bupt.edu.cn.";
    
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
