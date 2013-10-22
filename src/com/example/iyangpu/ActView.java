package com.example.iyangpu;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.iyangpu.data.Activity;
import com.example.iyangpu.fragments.DetailsFrag;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class ActView extends RelativeLayout implements OnClickListener,OnTouchListener,OnLongClickListener{
	ImageView image;
	TextView name;
	TextView time;
	TextView address;
	TextView location;
	TextView type;
	TextView votes;
	com.example.iyangpu.data.Activity activity;
	public ActView(Context context, Activity activity) {
		super(context);
		this.activity = activity;
		this.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 200));
		
		image = new ImageView(getContext());
		image.setImageDrawable(getResources().getDrawable(R.drawable.no_loc_pic));
		image.setPadding(10, 10, 10, 10);	
		image.setId(1);


		
		RelativeLayout.LayoutParams para = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		para.topMargin = 50;
		para.bottomMargin = 50;
		para.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM|RelativeLayout.ALIGN_PARENT_LEFT|RelativeLayout.ALIGN_PARENT_TOP);
		
		ActView.this.addView(image, para);
		
		
		name = new TextView(getContext());
		name.setText(activity.getName());
		RelativeLayout.LayoutParams para2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		
		para2.leftMargin = 50;
		para2.addRule(RelativeLayout.RIGHT_OF,1);
		para2.addRule(RelativeLayout.ALIGN_TOP,1);
		addView(name,para2);
		
		this.setOnClickListener(this);
		this.setOnTouchListener(this);
		this.setOnLongClickListener(this);
		
		new addImageTask().execute(activity.getPictureUrl());

		
	}
	
	
	
	class addImageTask extends AsyncTask<String,Integer,Integer>{
		Bitmap bitmap;
		@Override
		protected Integer doInBackground(String... arg0) {
			if(arg0[0]!="null"&&arg0[0]!=null)
			{
				try {
					URL url = new URL(DBUtil.target_url+"/"+arg0[0]);
					HttpURLConnection conn  = (HttpURLConnection)url.openConnection();
					conn.setDoInput(true);
					conn.connect(); 
					InputStream inputStream=conn.getInputStream();
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream); 
                	this.bitmap = bitmap;
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("e", e.getMessage());
				}
			}
			else {
				//image.setImageDrawable(getResources().getDrawable(R.drawable.no_loc_pic));
			}
			return null;

		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub	
			if(bitmap != null)
				image.setImageBitmap(bitmap);
			else 
				image.setImageDrawable(getResources().getDrawable(R.drawable.no_loc_pic));
		}
		
	}



	@Override
	public void onClick(View v) {
		
		MainActivity mainActivity = (MainActivity) getContext();
		FragmentManager fragmentManager = mainActivity.getFragmentManager();
		FragmentTransaction fTransaction = fragmentManager.beginTransaction();
		
		Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_to_be_changed_middle);
		fTransaction.hide(fragment).add(R.id.fragment_to_be_changed_middle, new DetailsFrag().setActivity(activity));
		fTransaction.addToBackStack(null);
		fTransaction.commit();
		fragmentManager.executePendingTransactions();
		
		mainActivity.reload_detail_comments(activity,false);
		
		
	}



	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			this.setAlpha((float) 0.5);
			return false;
		}
		else if(event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP){
			this.setAlpha(1);
			return false;
		}
		else {
			return false;
		}
	}



	@Override
	public boolean onLongClick(View v) {
	    new AlertDialog.Builder(getContext())  
	    .setTitle("请选择")  
	    .setIcon(android.R.drawable.ic_dialog_info)                  
	    .setItems(new CharSequence[] {"添加到我的活动","评分"}, 	      
	    		new DialogInterface.OnClickListener(){  
	    	public void onClick(DialogInterface dialog, int which) {  
	    		if(which == 0){
	    			//TODO
	    			MainActivity a = (MainActivity) getContext();
	    			a.upload_attention(activity.getId());
	    		}
	    		else if (which == 1) {
	    			if(activity.getIsRating().equals(0)){
	    				MainActivity a = (MainActivity) getContext();
	    				a.start_rating(activity);
	    			}
	    			else {
	    				Toast.makeText(getContext(), "您已经评过分了", Toast.LENGTH_SHORT).show();
					}
				}
	    	}  
	    	}
	    )
	    .setNegativeButton("取消", null)  
	    .show();  
		return true;
	}
}
