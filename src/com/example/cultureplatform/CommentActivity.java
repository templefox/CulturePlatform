package com.example.cultureplatform;

import java.io.FileNotFoundException;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class CommentActivity extends Activity {
	private ImageView imageView;
	private String imagePath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.ActionBar);
		setContentView(R.layout.activity_comment);
		
		imageView = (ImageView) findViewById(R.id.comment_imageView);
		imageView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				 CharSequence[] items = {"相册", "相机"};    
				   new AlertDialog.Builder(CommentActivity.this)  
				    .setTitle("选择图片来源")  
				    .setItems(items, new DialogInterface.OnClickListener() {  
				        public void onClick(DialogInterface dialog, int which) {  
				            if( which == 0 ){  
				                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
				                intent.addCategory(Intent.CATEGORY_OPENABLE);  
				                intent.setType("image/*");  
				                startActivityForResult(Intent.createChooser(intent, "选择图片"), 0);   
				            }else{  
				                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);    
				                startActivityForResult(intent, 1);    
				            }  
				        }  
				    })  
				    .create().show();   
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comment, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Bitmap bmp = null;
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
	        //选择图片  
			Uri uri = data.getData();   
	        String[] filePathColumn = { MediaStore.Images.Media.DATA };

	        Cursor cursor = getContentResolver().query(uri,
	                filePathColumn, null, null, null);
	        cursor.moveToFirst();

	        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	        imagePath = cursor.getString(columnIndex);
	        cursor.close();   
			
	        imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
		}
	}

}
