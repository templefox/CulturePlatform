package com.example.cultureplatform;

import java.util.ArrayList;
import java.util.List;

import com.example.database.data.Entity;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

public class AddActActivity extends Activity {
	private ImageView picture;
	private EditText time;
	private String picture_path;
	private EditText type;
	private int year = 2014;
	private int month = 0;
	private int day = 1;
	private int hour = 18;
	private int minute = 30;
	private int second = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.ActionBar);
		setContentView(R.layout.activity_add_act);

		time = (EditText) findViewById(R.id.add_time);
		picture = (ImageView) findViewById(R.id.add_picture);
		type = (EditText) findViewById(R.id.add_type);

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
				v.performClick();
			}

		});
		picture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CharSequence[] items = { "¥”œ‡≤·—°‘Ò", "≈ƒ…„–¬Õº∆¨" };
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
											.createChooser(intent, "—°‘ÒÕº∆¨"), 0);
								} else {
									Intent intent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									startActivityForResult(intent, 1);
								}
							}
						}).create().show();
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
			// —°‘ÒÕº∆¨
			Uri uri = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(uri, filePathColumn,
					null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picture_path = cursor.getString(columnIndex);
			cursor.close();

			picture.setImageBitmap(BitmapFactory.decodeFile(picture_path));
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
