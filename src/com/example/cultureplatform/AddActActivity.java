package com.example.cultureplatform;

import java.util.ArrayList;
import java.util.List;

import com.example.database.DatabaseConnector;
import com.example.database.MessageAdapter;
import com.example.database.data.Entity;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddActActivity extends Activity {
	private ImageView picture;
	private EditText time;
	private String picture_path;
	private EditText type;
	private EditText name;
	private EditText intro;
	private EditText rout;
	private EditText address;
	private EditText theme;
	private EditText reporter;
	private Button submit;
	private int year = 2014;
	private int month = 0;
	private int day = 1;
	private int hour = 18;
	private int minute = 30;
	private boolean pictureReady = false;
	private Bitmap pictureBitmap;
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.ActionBar);
		setContentView(R.layout.activity_add_act);

		time = (EditText) findViewById(R.id.add_time);
		picture = (ImageView) findViewById(R.id.add_picture);
		type = (EditText) findViewById(R.id.add_type);
		name = (EditText) findViewById(R.id.add_name);
		intro = (EditText) findViewById(R.id.add_intro);
		rout = (EditText) findViewById(R.id.add_rout);
		address = (EditText) findViewById(R.id.add_address);
		theme = (EditText) findViewById(R.id.add_theme);
		submit = (Button) findViewById(R.id.add_submit);
		reporter = (EditText) findViewById(R.id.add_reporter);
		
		time.setInputType(InputType.TYPE_NULL);
		time.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(AddActActivity.this,
						new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								AddActActivity.this.year = year;
								AddActActivity.this.month = monthOfYear;
								AddActActivity.this.day = dayOfMonth;
								new TimePickerDialog(AddActActivity.this,
										new OnTimeSetListener() {

											@Override
											public void onTimeSet(
													TimePicker view,
													int hourOfDay, int minute) {
												AddActActivity.this.hour = hourOfDay;
												AddActActivity.this.minute = minute;
												AddActActivity.this.setTime();
											}
										}, hour, minute, true).show();
							}
						}, year, month, day).show();
			}

		});
		time.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (arg1)
					arg0.performClick();
			}

		});
		type.setInputType(InputType.TYPE_NULL);
		type.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				List<ContentValues> cvs = Entity.selectFromSQLite("type",
						new String[] { "name" }, AddActActivity.this);
				final List<CharSequence> items = new ArrayList<CharSequence>();
				for (ContentValues cv : cvs) {
					items.add(cv.getAsString("name"));
				}

				new AlertDialog.Builder(AddActActivity.this)
						.setItems(items.toArray(new String[items.size()]),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										type.setText(items.get(which));
									}

								}).create().show();
			}

		});
		type.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					v.performClick();
			}

		});
		picture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CharSequence[] items = { "从相册选择", "拍摄新图片" };
				new AlertDialog.Builder(AddActActivity.this)
						.setItems(items, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) {
									Intent intent = new Intent(
											Intent.ACTION_GET_CONTENT);
									intent.addCategory(Intent.CATEGORY_OPENABLE);
									intent.setType("image/*");
									startActivityForResult(Intent
											.createChooser(intent, "选择图片"), 0);
								} else {
									Intent intent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									startActivityForResult(intent, 1);
								}
							}
						}).create().show();
			}

		});
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MessageAdapter adapter = new MessageAdapter() {

					@Override
					public void onDone(String ret) {
						// TODO Auto-generated method stub
						Log.v("a", ret);
						progressDialog.dismiss();
						Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_LONG).show();
						finish();
					}

					@Override
					public void onErrorOccur(String ret) {
						// TODO Auto-generated method stub
						Log.w("a", ret);
						Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onTimeout() {
						Log.w("handler", "timeout");
						Toast.makeText(getApplicationContext(), "上传失败,超时", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onFinish() {
						progressDialog.dismiss();
						finish();
						pictureBitmap.recycle();
					}
				};
			
				boolean cancel = false;
				View focuseView = null;
				
				String nameString = name.getText().toString();
				String addresString = address.getText().toString();
				String introString = intro.getText().toString();
				String typeString = type.getText().toString();
				String themeString = theme.getText().toString();
				String datetime = time.getText().toString();
				String routString = rout.getText().toString();
				String reporterString = reporter.getText().toString();
				
				if(TextUtils.isEmpty(routString)){
					rout.setError(getString(R.string.error_field_required));
					focuseView = rout;
					cancel = true;
				}					
				if(TextUtils.isEmpty(introString)){
					intro.setError(getString(R.string.error_field_required));
					focuseView = intro;
					cancel = true;
				}			
				if(TextUtils.isEmpty(themeString)){
					theme.setError(getString(R.string.error_field_required));
					focuseView = theme;
					cancel = true;
				}
				if(TextUtils.isEmpty(reporterString)){
					reporter.setError(getString(R.string.error_field_required));
					focuseView = reporter;
					cancel = true;
				}				
				if(TextUtils.isEmpty(typeString)){
					type.setError(getString(R.string.error_field_required));
					focuseView = type;
					cancel = true;
				}
				if(TextUtils.isEmpty(addresString)){
					address.setError(getString(R.string.error_field_required));
					focuseView = address;
					cancel = true;
				}
				if(TextUtils.isEmpty(datetime)){
					time.setError(getString(R.string.error_field_required));
					focuseView = time;
					cancel = true;
				}
				if(TextUtils.isEmpty(nameString)){
					name.setError(getString(R.string.error_field_required));
					focuseView = name;
					cancel = true;
				}
				
				if(cancel){
					focuseView.requestFocus();
					return;
				}
								
				if(!pictureReady){
					Toast.makeText(AddActActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
					return;
				}
				DatabaseConnector connector = new DatabaseConnector();
				progressDialog = ProgressDialog.show(AddActActivity.this, "上传中", "图片文件较大，请耐心等待");
				connector.addParams(DatabaseConnector.UPLOAD, "SETACTIVITY");
				connector.addParams("name", nameString);
				connector.addParams("address", addresString);
				connector.addParams("content", introString);
				connector.addParams("type", typeString);
				connector.addParams("theme", themeString);
				connector.addParams("date", datetime.substring(0, datetime.indexOf(' ')));
				connector.addParams("time", datetime.substring(datetime.indexOf(' ')+1));
				connector.addParams("procedure", routString);
				connector.addParams("reporter_info", reporterString);
				connector.asyncUpload(pictureBitmap, adapter);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_act, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Bitmap bmp = null;
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			// 选择图片
			Uri uri = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(uri, filePathColumn,
					null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picture_path = cursor.getString(columnIndex);
			cursor.close();

			if(pictureBitmap!=null)
				pictureBitmap.recycle();
			pictureBitmap =BitmapFactory.decodeFile(picture_path);
			if(pictureBitmap.getWidth()<512&&pictureBitmap.getHeight()<512)
			{
				picture.setImageBitmap(pictureBitmap);
				pictureReady =true;
			}
			else
			{
				pictureBitmap.recycle();
				pictureReady =false;
				Toast.makeText(AddActActivity.this, "图片不能超过512*512大小", Toast.LENGTH_LONG).show();
			}
		}
	}

	public void setTime() {
		time.setText(new StringBuilder().append(year).append('.')
				.append(String.format("%02d", month + 1)).append('.')
				.append(String.format("%02d", day)).append(' ')
				.append(String.format("%02d", hour)).append(':')
				.append(String.format("%02d", minute)));
	}
}
