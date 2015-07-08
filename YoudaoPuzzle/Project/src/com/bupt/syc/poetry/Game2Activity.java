package com.bupt.syc.poetry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Game2Activity extends MyActivity{
	public static final int NUM=144;
	public static final int ROW = 12;
	public static final int STATE_FINISH = 1;
	public static final int PROGRESS_DIALOG = 2;
	
	
	Game2 game2 = null;
	int curRPos = 0;
	int curCPos = 0;
   
	boolean isStarted = false;
	TextView hengTextView = null;
	TextView zongTextView = null;
	
	Button backButton = null;
	Button handUpButton = null;
	Button fillButton = null;
	Button nextButton = null;
	
	GridView gView = null;
	
	boolean init[][];
	char matrix[][];
	int mark[][];
	String[] hint = new String[2];
	String fillInWords = new String();
	
	ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamescdview);
		//Music.play(this);
		hengTextView = (TextView)findViewById(R.id.game2TishiHengTV);
		zongTextView = (TextView)findViewById(R.id.game2TishiZongTV);
		hengTextView.setClickable(true);
		zongTextView.setClickable(true);

		
		hengTextView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				hengTextView.setFocusableInTouchMode(true);
				zongTextView.setFocusableInTouchMode(false);
				hengTextView.requestFocus();
			}
		});
		zongTextView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				zongTextView.setFocusableInTouchMode(true);
				hengTextView.setFocusableInTouchMode(false);
				zongTextView.requestFocus();
				
			}
		});
		
		backButton = (Button)findViewById(R.id.game2BackBTN);
		backButton.setOnClickListener(Listener);
		handUpButton = (Button)findViewById(R.id.game2HandupBTN);
		handUpButton.setOnClickListener(Listener);
		fillButton = (Button)findViewById(R.id.game2fillBTN);
		fillButton.setOnClickListener(Listener);
		nextButton = (Button)findViewById(R.id.game2NextBTN);
		nextButton.setOnClickListener(Listener);
		
		gView = (GridView)findViewById(R.id.game2GV);
		
		init();
	}
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case PROGRESS_DIALOG:
			progressDialog = new ProgressDialog(this);  
			progressDialog.setIcon(R.drawable.progress);
			progressDialog.setTitle("�ݺ�ʫ��");
			progressDialog.setMessage("�������ɣ������ĵȴ�����");
			progressDialog.show();
			return progressDialog;
			
		default:
			return null;
		}
	}
//	protected Dialog onCreateDialog(int id) {
//		switch (id) {
//		case PROGRESS_DIALOG:
//			progressDialog = new ProgressDialog(this);   
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);   
//            progressDialog.setMessage("Loading...");   
//            progressDialog.show();  
//            return progressDialog;   
//
//		default:
//			return null;
//		}
//	}

	private class ProgressTask extends AsyncTask<Game2, Void, Integer>{

		@Override
		protected Integer doInBackground(Game2... params) {
			Game2 g2 = params[0];
			g2.generate();
			return STATE_FINISH;
		}

		@Override
		protected void onPostExecute(Integer result) {
			int state = result.intValue();
			switch(state){
			
			case STATE_FINISH:
				dismissDialog(PROGRESS_DIALOG);
				Toast.makeText(getApplicationContext(), "�������!", Toast.LENGTH_LONG).show();
				setGV();
			}
			
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			//��ʾdialog 
			showDialog(PROGRESS_DIALOG);
			super.onPreExecute();
		}
		
	}
	
	private void init(){
		//get init matrix
		isStarted = false;
		game2 = new Game2();
        new ProgressTask().execute(game2);
	}
	
	private void setGV(){
		init = game2.getMatrix();
		matrix = new char[ROW][ROW];
		mark = new int[ROW][ROW];
		for(int i=0;i<ROW;i++){
			for(int j=0;j<ROW;j++){
				matrix[i][j] = '��';
		        	mark[i][j] = Game2Result.FILL_CORRECT;
				}
		}
		matrix = game2.g2r.result;

		//���ɶ�̬���飬����ת������  
		ArrayList<HashMap<String, Object>> lstItem = new ArrayList<HashMap<String, Object>>();  
		for(int i=0;i<NUM;i++)   
		{  
		    HashMap<String, Object> map = new HashMap<String, Object>();
		    int r = i/ROW;
		    int c = i%ROW;
		    if(init[r][c]==true)
		    	map.put("n1", String.valueOf(matrix[r][c])); 
			else
				map.put("n1", String.valueOf(matrix[r][c])); 
			 
			lstItem.add(map);  
		}  
		//���������� <====> ��̬�����Ԫ�� 
		SpecialAdapter saItems = new SpecialAdapter(this, lstItem,//������Դ   
													R.layout.itemgame2,//item��XMLʵ��  
													//��̬������ImageItem��Ӧ������          
													new String[] {"n1"},   
													//ImageItem��XML�ļ������һ��ImageView,����TextView ID  
													new int[] {R.id.col1});  
		//��Ӳ�����ʾ  
		gView.setAdapter(saItems);  
		//�����Ϣ����  
		gView.setOnItemClickListener(new ItemClickListener());
			      
	} 
	private int getEditTextCursorIndex(EditText mEditText){
		return mEditText.getSelectionStart();
	}
    private void insertText(EditText mEditText, CharSequence charSequence){
    	mEditText.getText().insert(getEditTextCursorIndex(mEditText), charSequence);
    }
	protected void onDestroy() {
		super.onDestroy();
		//Music.stop(this);
		Game2Activity.this.finish();
	}
	
	
	private void showAnswer(){
		//�Ȳ�����ɫ�ж�
		matrix = game2.g2r.matrix;
		mark = game2.g2r.mark;
		  //���ɶ�̬���飬����ת������  
	      ArrayList<HashMap<String, Object>> lstItem = new ArrayList<HashMap<String, Object>>();  
	      for(int i=0;i<NUM;i++)   
	      {  
	        HashMap<String, Object> map = new HashMap<String, Object>();
	        int r = i/ROW;
	        int c = i%ROW;
	        if(init[r][c]==true)
	        	map.put("n1", String.valueOf(matrix[r][c])); // white
	        else
	        	map.put("n1", String.valueOf(matrix[r][c])); //gray
	        lstItem.add(map);  
	      }  
	      SpecialAdapter saItems = new SpecialAdapter(this, 
                lstItem,//������Դ   
                R.layout.itemgame2,//item��XMLʵ��  
           
                //��̬������ImageItem��Ӧ������          
                new String[] {"n1"},   
                  
                //ImageItem��XML�ļ������һ��ImageView,����TextView ID  
                new int[] {R.id.col1});  
	      //��Ӳ�����ʾ  
	      gView.setAdapter(saItems);  
	}
	private OnClickListener Listener = new OnClickListener(){
		public void onClick(View v) {
			Button btn = (Button)v;
			switch (btn.getId()) {
			case R.id.game2BackBTN: //�洢��ҳ�𰸣�������һҳ
				Intent intent = new Intent();
				intent.setClass(Game2Activity.this, MenuActivity.class);
				startActivity(intent);
				onDestroy();
				break;	
			case R.id.game2HandupBTN:
				game2.game2Judge();
				matrix = game2.g2r.matrix;
				showAnswer();
				fillButton.setEnabled(false);
				break;
				
			case R.id.game2fillBTN:
				if(isStarted == true){
					if(init[curRPos][curCPos]==true && !((!hengTextView.getText().equals("")) && (!zongTextView.getText().equals("")))){
						//�������Ƿ�洢����
						final EditText inputServer = new EditText(Game2Activity.this);
						inputServer.setText((String)game2.getCurResult(curRPos, curCPos));
				        AlertDialog.Builder builder = new AlertDialog.Builder(Game2Activity.this);
				        String title = new String();
				        if(hengTextView.getText().equals(""))
				        	title = (String)zongTextView.getText();
				        else
				        	title = (String)hengTextView.getText();
				        
				        builder.setMessage(title).setIcon(R.drawable.fillindialog).setView(inputServer)
				                .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
						            public void onClick(DialogInterface dialog, int which) {
						            	//ȡ���Ի���
						             }
						        });
				        
				        builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int which) {
							    fillInWords = inputServer.getText().toString();
							    fillInWords = ConvertStringToWide(fillInWords);
							    game2.fillBlanks(fillInWords, curRPos, curCPos);
								setGV();//���½���
				             }
				        });
				        builder.show();	
					}
					else if((!hengTextView.getText().equals("")) && (!zongTextView.getText().equals("")))
						Toast.makeText(getApplicationContext(), "�˴�Ϊ����㣬�޷�ȷ��Ҫ���ʫ�䣬������ѡ�񷽸����д!", Toast.LENGTH_LONG).show();
				}
				break;	

			case R.id.game2NextBTN:
				curRPos = 0;
				curCPos = 0;
				hengTextView.setText("");
				zongTextView.setText("");
				fillButton.setEnabled(true);
				init();
				break;
				
			default:
				break;
			}
		}

	};
	
	 public static String ConvertStringToWide(String source)
	    {
	        String result = "";
	        for (int i = 0; i < source.length(); i++)
	        {
	            if (source.charAt(i) == 32)
	            {
	                result +='��';
	            }
	            else if (source.charAt(i) == 46)
	            {
	                result += '��';
	            }
	            else if (source.charAt(i) >= 33 && source.charAt(i) <= 126)
	            {
	                result += ((char)(source.charAt(i) + 65248));
	            }
	            else
	            {
	                result += source.charAt(i);
	            }
	        }
	        return result;
	    }

	
	//��AdapterView������(���������߼���)���򷵻ص�Item�����¼�  
	class  ItemClickListener implements OnItemClickListener  
	{  
		public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened   
		                                View arg1,//The view within the AdapterView that was clicked  
		                                int arg2,//The position of the view in the adapter  
		                                long arg3//The row id of the item that was clicked  
		                                ) {  
		  //�ڱ�����arg2=arg3  
		  HashMap<String, Object> item=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);  
		  isStarted = true;
		  //Toast.makeText(Game2Activity.this,(String)item.get("n1"), Toast.LENGTH_LONG).show();
		 // Toast.makeText(Game2Activity.this,String.valueOf(arg3), Toast.LENGTH_LONG).show();
		  
		  //��ȡ�кź��к�  �����ݸ�game2�෽������ȡ string hint[2]
		  int r = arg2/ROW;
		  int c = arg2%ROW;
		  if(init[r][c] == true){
			  hint = game2.getPoint(r, c);
			  curRPos = r;
			  curCPos = c;
			  
			  //��ʾhint[0]���ᣬ��ʾhint[1]����
			  hengTextView.setText(hint[0]);
			  zongTextView.setText(hint[1]);
			  
		  }else{
			  Toast.makeText(Game2Activity.this,"�ô�Ϊ��Ч����", Toast.LENGTH_LONG).show();
		  }
		  
//		  LinearLayout ll = (LinearLayout)arg1;
//		  TextView tvn = (TextView)ll.getChildAt(0);
//		  tvn.setBackgroundColor(R.color.white);
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
	        int r = position/ROW;
	        int c = position%ROW;

	        if(init[r][c]==true)
	        {
	        	tvn.setBackgroundColor(Color.WHITE); // white	 
	        	if(mark[r][c] == 1)
	        	   tvn.setTextColor(Color.BLUE);//not fill	       
	        	else if(mark[r][c] == 2)
	        		tvn.setTextColor(Color.BLACK);//right
	        	else {
					tvn.setTextColor(Color.RED); //wrong
				}
	        }
	        else
	        	tvn.setBackgroundColor(Color.GRAY); //gray
	        //view.setBackgroundColor(colors[colorPos]);  
	        
	        return view;  
	    }  
	} 
	
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
//		Music.stop(this);
//	}
}


