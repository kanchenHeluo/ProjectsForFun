package com.bupt.syc.poetry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Game1Activity extends MyActivity{
    public static final int INTERVAL = 20;
    public static final int amount = 11;
	Game1PoemList game1PoemList = new Game1PoemList();
	Button nextBtn = null, lastBtn=null, upHandBtn=null,bombBtn = null;
	Button[] words = new Button[16];
	TextView seqTV = null;
	TextView processTextView = null;
	ProgressBar processProgressBar = null;
	
	
	TextView exTV1, exTV2, exTV3;
	EditText exET1, exET2, exET3;
	GridView gView1,gView2,gView3;
	TextView timerShow = null;
	GridView gView;
	ImageView iView;
	Timer timer = new Timer();
	
	int proNum=10;
	int lastNum = proNum;
	int currentPage = 0;
	int round = 1;
	int omit;
	int secLeft;
	int buttonid[][] = new int[proNum][16];
	int buttonNum[] = new int[proNum];
	int Bombednum[] = new int[proNum];
	int BombedId[][] = new int[proNum][16];
	int pos[][] = new int[proNum][2];
	int numImage[] = new int[]{R.drawable.game1_zero,R.drawable.game1_one,R.drawable.game1_two,R.drawable.game1_three};
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamefirstview);
		changeActivity = false;
		//Music.play(this);
		//得到题目数 ，起始题目号 
		seqTV = (TextView)findViewById(R.id.seqTV);
		processTextView = (TextView)findViewById(R.id.game1tv3);
		exTV1 = (TextView)findViewById(R.id.game1tvFir);
		exTV2 = (TextView)findViewById(R.id.game1tvSec);
		exTV3 = (TextView)findViewById(R.id.game1tvThi);
		//exET1 = (EditText)findViewById(R.id.game1etFir);
		//exET2 = (EditText)findViewById(R.id.game1etSec);
		//exET3 = (EditText)findViewById(R.id.game1etThi);
		gView1 = (GridView)findViewById(R.id.game1GVFir);
		gView2 = (GridView)findViewById(R.id.game1GVSec);
		gView3 = (GridView)findViewById(R.id.game1GVThi);
		iView = (ImageView)findViewById(R.id.imageView1);
		processProgressBar = (ProgressBar)findViewById(R.id.game1ProgressBar);
		
		nextBtn = (Button)findViewById(R.id.btnGame1Next);
		lastBtn = (Button)findViewById(R.id.btnGame1Last);
		upHandBtn = (Button)findViewById(R.id.btnGame1Handup);
		bombBtn = (Button)findViewById(R.id.btnGame1Bomb);
		
		processProgressBar.setIndeterminate(false);
		processProgressBar.setVisibility(View.VISIBLE);
		
		words[0] = (Button)findViewById(R.id.gab11);
		words[1] = (Button)findViewById(R.id.gab12);
		words[2] = (Button)findViewById(R.id.gab13);
		words[3] = (Button)findViewById(R.id.gab14);
		words[4] = (Button)findViewById(R.id.gab21);
		words[5] = (Button)findViewById(R.id.gab22);
		words[6] = (Button)findViewById(R.id.gab23);
		words[7] = (Button)findViewById(R.id.gab24);
		words[8] = (Button)findViewById(R.id.gab31);
		words[9] = (Button)findViewById(R.id.gab32);
		words[10] = (Button)findViewById(R.id.gab33);
		words[11] = (Button)findViewById(R.id.gab34);
		words[12] = (Button)findViewById(R.id.gab41);
		words[13] = (Button)findViewById(R.id.gab42);
		words[14] = (Button)findViewById(R.id.gab43);
		words[15] = (Button)findViewById(R.id.gab44);
		
		
		nextBtn.setOnClickListener(Listener);
		lastBtn.setOnClickListener(Listener);	
		upHandBtn.setOnClickListener(Listener);
		bombBtn.setOnClickListener(Listener);
		
		for(int i=0; i<16; i++)
		   words[i].setOnClickListener(Listener);
		
		Game1Condition gCondition = new Game1Condition(round,proNum);
		Global.game1 = new Game1();
		Global.game1.getPoemList(gCondition);
		showProblem(currentPage);	//获取第一轮的题s
		
		processProgressBar.setMax(Global.game1.getTotalcount());
		processProgressBar.setProgress(Global.game1.getDoneQues());
		
		// timeTask
		//secLeft = gCondition.amount * INTERVAL;
		secLeft = Global.game1.game1TimeCnt(round, gCondition.amount);
		timerShow = (TextView)findViewById(R.id.game1lefttime);
		timer.schedule(timer_task, 1000, 1000);		//启动计时，后面两个参数？？？
	}
	
	TimerTask timer_task = new TimerTask() { //计时线程
		@Override
		public void run() {
			runOnUiThread(new Runnable() {		// UI thread
				public void run() {
					secLeft--;
					timerShow.setText(""+sec2time(secLeft));
					if(secLeft <= 0){ //到时间了 
						
						//触发一个提交动作
						upHandBtn.performClick();
						timerShow.setText("00:00");
						//timer_task.cancel();
						//timer.cancel();
						//Toast.makeText(Game1Activity.this, "thread running", Toast.LENGTH_LONG).show();
					}
				}
			});
		}
	};
	private String sec2time(int secLeft){
		String res=null;
		res=""+((secLeft/60)>59?"59:59":((secLeft/60>9?secLeft/60:"0"+secLeft/60)+":"+(secLeft%60>9?secLeft%60:"0"+secLeft%60)));
		return res;
	}

	private void showProblem(int currentPage){
		game1PoemList = Global.game1.getPoemList(currentPage);
		if(Bombednum[currentPage] > 0){
			bombBtn.setBackgroundResource(R.drawable.game1_bomb_click);
			bombBtn.setEnabled(false);
		}
		else{
			bombBtn.setBackgroundResource(R.drawable.game1_bomb);
			bombBtn.setEnabled(true);
		}
		//bombBtn.setText(Integer.toString(Global.game1.getBombNum()));
		//iView.setImageDrawable();
		iView.setImageResource(numImage[Global.game1.getBombNum()]);
		
		//ombBtn.setGravity(View.FOCUS_BACKWARD)
		seqTV.setText(String.valueOf(game1PoemList.seq));
		//添加进度条更新信息，如2/11
		processTextView.setText( Global.game1.getDoneQues() + "/" + Global.game1.getTotalcount());
		
		int length = game1PoemList.getPoetrySentence()[game1PoemList.getOmit()].length();
		pos[currentPage][0] = (amount - length)/2;
		pos[currentPage][1] = pos[currentPage][0] + length;
		
		int len = game1PoemList.poetrySentence.length;
		exTV1.setText(game1PoemList.poetrySentence[0]);
		exTV2.setText(game1PoemList.poetrySentence[1]);
		if(3 == len){
			exTV3.setText(game1PoemList.poetrySentence[2]);
		}else
			exTV3.setText("");
		
		
		gView1.setVisibility(View.INVISIBLE);
		gView2.setVisibility(View.INVISIBLE);
		gView3.setVisibility(View.INVISIBLE);
		setNullGV(gView1);
		setNullGV(gView2);
		setNullGV(gView3);
		omit = game1PoemList.omit;
		//show words
		for(int i=0;i<16;i++){
			   words[i].setText(String.valueOf(game1PoemList.getWords()[i]));
			   words[i].setEnabled(true);
			   words[i].setVisibility(View.VISIBLE);
		}
		
	    for(int i=0;i<buttonNum[currentPage];i++)   
	    {  
	    	if( buttonid[currentPage][i] != 0){
		        Button btn = (Button)findViewById(buttonid[currentPage][i]);
		        btn.setEnabled(false);
		        btn.setVisibility(View.INVISIBLE);
	    	}
	    }  
	    
	    for(int i = 0;i < Bombednum[currentPage];i++){
	        Button btn = (Button)findViewById(BombedId[currentPage][i]);
	        btn.setEnabled(false);
	        btn.setVisibility(View.INVISIBLE);
	    }
		if(omit == 0){
			exTV1.setText("");
			gView1.setVisibility(View.VISIBLE);
			gView = gView1;
			//exET1.setText(Global.game1.getViewAnswer(currentPage));
			setGV();
		}
		else if(game1PoemList.omit == 1){
			exTV2.setText("");
			gView2.setVisibility(View.VISIBLE);
			//exET2.setText(Global.game1.getViewAnswer(currentPage));
			gView = gView2;
			setGV();
		}
		else {
			exTV3.setText("");
			gView3.setVisibility(View.VISIBLE);
			//exET3.setText(Global.game1.getViewAnswer(currentPage));
			gView = gView3;
			setGV();
		}
	
	}
	
	private void setGV(){
		  //生成动态数组，并且转入数据  
	      ArrayList<HashMap<String, Object>> lstItem = new ArrayList<HashMap<String, Object>>();  
	      for(int i=0;i<pos[currentPage][0];i++)   
	      {  
	        HashMap<String, Object> map = new HashMap<String, Object>();
	        map.put("n1", "　"); // white
	        lstItem.add(map);  
	      }  	      
	      for(int i=0;i<buttonNum[currentPage];i++)   
	      {  
	        HashMap<String, Object> map = new HashMap<String, Object>();
	        if(buttonid[currentPage][i] != 0){
		        Button btn = (Button)findViewById(buttonid[currentPage][i]);
		        map.put("n1", String.valueOf(btn.getText())); 
	        }
	        else
	        	map.put("n1",'　');
	        lstItem.add(map);  
	      }  
	      if(lstItem.size() < pos[currentPage][1]){
	    	  for(int i = lstItem.size();i < pos[currentPage][1];i++){
		      HashMap<String, Object> map = new HashMap<String, Object>();
		      map.put("n1", "　"); // white
		      lstItem.add(map); 
	    	  }
	      }
	      //生成适配器 <====> 动态数组的元素 
	      SimpleAdapter saItems = new SpecialAdapter(this, 
          lstItem,//数据来源   
          R.layout.itemgame1,//item的XML实现  
     
          //动态数组与ImageItem对应的子项          
          new String[] {"n1"},   
            
          //ImageItem的XML文件里面的一个ImageView,两个TextView ID  
          new int[] {R.id.col1});  
	      
	      //添加并且显示  
	      if(gView != null){
		      gView.setAdapter(saItems);  
		      //添加消息处理  
		      gView.setOnItemClickListener(new ItemClickListener());
	      }
	      
	} 	

	private void setNullGV(GridView gView){
		  //生成动态数组，并且转入数据  
	      ArrayList<HashMap<String, Object>> lstItem = new ArrayList<HashMap<String, Object>>();  
	      for(int i=0;i<1;i++)   
	      {  
	        HashMap<String, Object> map = new HashMap<String, Object>();
	        map.put("n1", "　"); // white
	        lstItem.add(map);  
	      }  	      
	      //生成适配器 <====> 动态数组的元素 
	      SimpleAdapter saItems = new SimpleAdapter(this, 
        lstItem,//数据来源   
        R.layout.itemgame1,//item的XML实现  
   
        //动态数组与ImageItem对应的子项          
        new String[] {"n1"},   
          
        //ImageItem的XML文件里面的一个ImageView,两个TextView ID  
        new int[] {R.id.col1});  
	      
	      //添加并且显示  
	      gView.setAdapter(saItems);  
	      //添加消息处理  
	      //gView.setOnItemClickListener(new ItemClickListener());
	      
	} 	
	private String getAns(){
		  String ans = "";
		  boolean isAllSpace = true;
	      for(int i=0;i<buttonNum[currentPage];i++)   
	      { 
	    	if(buttonid[currentPage][i] != 0){
		        Button btn = (Button)findViewById(buttonid[currentPage][i]);
		        ans += String.valueOf(btn.getText());
		        isAllSpace =false;
	    	}
	    	else
	    		ans += '　';
	      } 	
	     if(!isAllSpace)
	    	 return ans;
	     else
	    	 return "";
	}	
	private OnClickListener Listener = new OnClickListener(){
		public void onClick(View v) {
			Button btn = (Button)v;
			switch (btn.getId()) {
			case R.id.btnGame1Next: //存储本页答案，进入下一页
				String s1 = getAns();
//				if(omit==0)
//					s1 = exET1.getText().toString();
//				else if(omit==1)
//					s1 = exET2.getText().toString();
//				else
//					s1 = exET3.getText().toString();
				Global.game1.setAnswer(currentPage, s1);
				
				if(currentPage != Global.game1.getAmount()-1)
					currentPage ++;
			    
				showProblem(currentPage);
				processProgressBar.setProgress(Global.game1.getDoneQues());
				break;
				
			case R.id.btnGame1Last:
				String s = getAns();
//				if(omit==0)
//					s = exET1.getText().toString();
//				else if(omit==1)
//					s = exET2.getText().toString();
//				else
//					s = exET3.getText().toString();
				Global.game1.setAnswer(currentPage, s);
				
                if(currentPage != 0)
                	currentPage --;
                showProblem(currentPage);
                processProgressBar.setProgress(Global.game1.getDoneQues());
				break;
				
			case R.id.btnGame1Handup:
				//存储本页答案
				String s11 = getAns();
//				if(omit==0)
//					s11 = exET1.getText().toString();
//				else if(omit==1)
//					s11 = exET2.getText().toString();
//				else
//					s11 = exET3.getText().toString();
				Global.game1.setAnswer(currentPage, s11);
				//进入继续加题环节 ,把当前页设置为下一轮第一页，继续调showProblem
				round++;
				int lastBombNum = Global.game1.getBombNum();
				proNum = Global.game1.game1Judge();
				if(proNum!=0){
					Game1Condition gCondition = new Game1Condition(round,proNum);
					Global.game1.getPoemList(gCondition);
					currentPage = 0;
					for(int i = 0;i < proNum;i++){
						buttonNum[i] = 0;	
						Bombednum[i] = 0;
					}
					String str = "上轮共" + lastNum + "道题目，您答对了" + gCondition.amount + "道题,追加" + gCondition.amount + "道题,加时" + Global.game1.game1TimeCnt(round, gCondition.amount) +   "s";
					if(Global.game1.getBombNum() > lastBombNum)
						str += ",并且奖励1颗炸弹，请珍惜使用哦~~";
					Toast.makeText(Game1Activity.this, str , Toast.LENGTH_LONG).show();
					lastNum =  gCondition.amount;
					showProblem(currentPage);
					//更新倒计时
					secLeft += Global.game1.game1TimeCnt(round, gCondition.amount);
					
					//更新进度条
					processProgressBar.setMax(Global.game1.getTotalcount());
					processProgressBar.setProgress(Global.game1.getDoneQues());				
	
				}else{
					timer_task.cancel();
					timer.cancel();
					timer.purge(); 
					timer = null; 
					changeActivity = true;
					Intent intent = new Intent();
					intent.setClass(Game1Activity.this, Game1Answer.class);
					startActivity(intent);
					onDestroy();
				}
				
				break;
			
			case R.id.btnGame1Bomb:
				if(Global.game1.useBomb()){
					btn.setBackgroundResource(R.drawable.game1_bomb_click);
					btn.setEnabled(false);
					String ans = game1PoemList.getPoetrySentence()[game1PoemList.getOmit()];
					char[] ansChar = ans.toCharArray();
					int len = ansChar.length;
					//Toast.makeText(Game1Activity.this, ans, Toast.LENGTH_LONG).show();
				      for(int i=0;i<buttonNum[currentPage];i++){   
				    	if(buttonid[currentPage][i] != 0){
					        Button btn1 = (Button)findViewById(buttonid[currentPage][i]);
					        char temp = String.valueOf(btn1.getText()).charAt(0);
					        int j;
					        for(j = 0;j < len;j++){
					        	if(ansChar[j] == temp){
				        			ansChar[j] = ansChar[len-1];
				        			//len--;
				        			break;
					        	}
					        }	
					        if(j == len){
					        	buttonid[currentPage][i] = 0;
					        }
					        else
					        	len--;
				    	}
				     } 
				   //   Toast.makeText(Game1Activity.this, ans, Toast.LENGTH_LONG).show();
					for(int i=0;i<16;i++){
					   if(words[i].isEnabled()){
					        char temp = String.valueOf(words[i].getText()).charAt(0);
					        int j;
					        for(j = 0;j < len;j++){
					        	if(ansChar[j] == temp){
				        			ansChar[j] = ansChar[len-1];
				        			break;
					        	}
					        }
					        if(j == len){
					        	words[i].setEnabled(false);
					        	words[i].setVisibility(View.INVISIBLE);
					        	BombedId[currentPage][Bombednum[currentPage]++] = words[i].getId();
					        }
					        else
					        	len--;
					   }
					}	
					//bombBtn.setText(Integer.toString(Global.game1.getBombNum()));
					iView.setImageResource(numImage[Global.game1.getBombNum()]);
					setGV();
				}
				break;
			case R.id.gab11:
			case R.id.gab12:
			case R.id.gab13:
			case R.id.gab14:
			case R.id.gab21:
			case R.id.gab22:
			case R.id.gab23:
			case R.id.gab24:
			case R.id.gab31:
			case R.id.gab32:
			case R.id.gab33:
			case R.id.gab34:
			case R.id.gab41:
			case R.id.gab42:
			case R.id.gab43:
			case R.id.gab44:
			    //Toast.makeText(ExamProblemActivity.this, "press", Toast.LENGTH_LONG).show();
				int i = 0;
				for(i = 0;i < buttonNum[currentPage];i++){
					if(buttonid[currentPage][i] == 0){
						buttonid[currentPage][i] = btn.getId();
						btn.setVisibility(View.INVISIBLE);
						btn.setEnabled(false);
						break;
					}
				}
				if(i == buttonNum[currentPage] && buttonNum[currentPage] < pos[currentPage][1] - pos[currentPage][0]){
					buttonid[currentPage][buttonNum[currentPage]++] = btn.getId();
//					if(omit == 0){
//	                   //insertText(exET1, btn.getText());
//						//buttonid[currentPage][buttonNum[currentPage]++] = btn.getId();
//						setGV();
//					}
//					else if(game1PoemList.omit == 1){
//						//insertText(exET2, btn.getText());
//						//buttonid[currentPage][buttonNum[currentPage]++] = btn.getId();
//						setGV();
//					}
//					else {
//						//insertText(exET3, btn.getText());
//						//buttonid[currentPage][buttonNum[currentPage]++] = btn.getId();
//						setGV();
//					}
					btn.setVisibility(View.INVISIBLE);
					btn.setEnabled(false);
				}
				setGV();
				break;
			default:
				break;
			}
		}

	};
	
	private int getEditTextCursorIndex(EditText mEditText){
		return mEditText.getSelectionStart();
	}
    private void insertText(EditText mEditText, CharSequence charSequence){
    	mEditText.getText().insert(getEditTextCursorIndex(mEditText), charSequence);
    }
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//Music.play(this);
		Game1Activity.this.finish();
		if(timer!=null){
			timer_task.cancel();
			timer.cancel();
			timer.purge(); 
			timer = null; 
		}
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
        switch (item.getItemId()) {
        case Menu.FIRST + 4:
        	AlertDialog.Builder builder = new AlertDialog.Builder(this); 
	        builder.setIcon(R.drawable.menu4);  
	        builder.setTitle("您确定要退出七步成诗，回到主菜单吗？");  
	        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
	            public void onClick(DialogInterface dialog, int whichButton) {  
	                //这里添加点击确定后的逻辑  
	            	changeActivity = true;
	            	onDestroy();
	            }  
	        });  
	        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {  
	            public void onClick(DialogInterface dialog, int whichButton) {  
	                //这里添加点击确定后的逻辑  
	            }  
	        });  
	        builder.create().show();     
	        break;
        default:
        	return super.onOptionsItemSelected(item);
        }
        	
        return false;
		
	//	return super.onOptionsItemSelected(item);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
        	AlertDialog.Builder builder = new AlertDialog.Builder(this); 
	        builder.setIcon(R.drawable.menu4);  
	        builder.setTitle("您确定要退出七步成诗，回到主菜单吗？");  
	        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
	            public void onClick(DialogInterface dialog, int whichButton) {  
	                //这里添加点击确定后的逻辑  
	            	changeActivity = true;
	            	onDestroy();
	            }  
	        });  
	        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {  
	            public void onClick(DialogInterface dialog, int whichButton) {  
	                //这里添加点击确定后的逻辑  
	            }  
	        });  
	        builder.create().show();     
			return false;
		}
		else
			return super.onKeyDown(keyCode, event);
	}
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
//		Music.stop(this);
//	}
	class  ItemClickListener implements OnItemClickListener  
	{  
		public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened   
		                                View arg1,//The view within the AdapterView that was clicked  
		                                int arg2,//The position of the view in the adapter  
		                                long arg3//The row id of the item that was clicked  
		                                ) {  
			if(arg2 >= pos[currentPage][0] && arg2 < buttonNum[currentPage] + pos[currentPage][0]){
				if(buttonid[currentPage][arg2 - pos[currentPage][0]] != 0){
					Button btn = (Button)findViewById(buttonid[currentPage][arg2 - pos[currentPage][0]]);
					btn.setVisibility(View.VISIBLE);
					btn.setEnabled(true);
//					for(int i = arg2 - pos[currentPage][0];i < buttonNum[currentPage] - 1;i++){
//						buttonid[currentPage][i] = buttonid[currentPage][i+1];
//					}
//					buttonNum[currentPage]--;
					buttonid[currentPage][arg2 - pos[currentPage][0]] = 0;
				}
				setGV();
			}			
		  }		  
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
			  if(position >= pos[currentPage][0] && position < pos[currentPage][1]){
				  tvn.setBackgroundResource(R.drawable.a9);
			  }
			  //tvn.setBackgroundColor(Color.WHITE);
			  //tvn.setTextSize(16);        
	        return view;  
	    }  
	} 
}
