package net.templefox.cultureplatform;

import java.io.FileNotFoundException;

import net.templefox.database.DatabaseConnector;
import net.templefox.database.MessageAdapter;
import net.templefox.database.data.User;

import com.example.cultureplatform.R;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CommentActivity extends Activity {
	private ImageView imageView;
	private String imagePath;
	private Button submit;
	private EditText content;
	private net.templefox.database.data.Activity currentActivity;
	private User currentUser;
	private ProgressDialog progressDialog;
	
	private OnClickListener onSubmit = new OnClickListener() {
		@Override
		public void onClick(View v) {
			MessageAdapter adapter = new MessageAdapter() {

				@Override
				public void onDone(String ret) {
					progressDialog.dismiss();
					Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_LONG).show();
					finish();
				}

				@Override
				public void onErrorOccur(String ret) {
					// TODO Auto-generated method stub
					super.onErrorOccur(ret);
				}

				@Override
				public void onTimeout() {
					// TODO Auto-generated method stub
					super.onTimeout();
				}
			};
			DatabaseConnector connector = new DatabaseConnector();
			connector.addParams(DatabaseConnector.UPLOAD, "SETCOMMENT");
			connector.addParams("user_id", currentUser.getId().toString());
			connector.addParams("activity_id", currentActivity.getId().toString());
			connector.addParams("content", content.getText().toString());
			connector.asyncUpload(BitmapFactory.decodeFile(imagePath), adapter);
			progressDialog = ProgressDialog.show(CommentActivity.this, "表单提交", "上传中，请稍后...");
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.ActionBar);
		setContentView(R.layout.activity_comment);
		
		currentActivity = (net.templefox.database.data.Activity) getIntent().getSerializableExtra(
				"activity");
		currentUser = ((ApplicationHelper)getApplication()).getCurrentUser();
		
		content = (EditText) findViewById(R.id.comment_editText);
		imageView = (ImageView) findViewById(R.id.comment_imageView);
		submit = (Button) findViewById(R.id.comment_submit);
		submit.setOnClickListener(onSubmit);
		imageView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				 CharSequence[] items = {"从相册选择", "拍摄新图片"};    
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
