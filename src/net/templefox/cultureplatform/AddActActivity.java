package net.templefox.cultureplatform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.async.http.libcore.RawHeaders;
import com.koushikdutta.ion.Ion;

import net.templefox.database.DatabaseConnector;
import net.templefox.database.IonRemoteLoader;
import net.templefox.database.MessageAdapter;
import net.templefox.database.MessageHandler;
import net.templefox.database.SQLiteWorker;
import net.templefox.misc.Encoder;

import net.templefox.cultureplatform.R;

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
import android.content.Context;
import android.content.CursorLoader;
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

@EActivity(R.layout.activity_add_act)
@OptionsMenu(R.menu.add_act)
public class AddActActivity extends Activity {
	@ViewById(R.id.add_picture)
	ImageView picture;

	@ViewById(R.id.add_time)
	EditText time;
	private String picture_path;

	@ViewById(R.id.add_type)
	EditText type;

	@ViewById(R.id.add_name)
	EditText name;

	@ViewById(R.id.add_intro)
	EditText intro;

	@ViewById(R.id.add_rout)
	EditText rout;

	@ViewById(R.id.add_address)
	EditText address;

	@ViewById(R.id.add_theme)
	EditText theme;

	@ViewById(R.id.add_reporter)
	EditText reporter;

	@ViewById(R.id.add_submit)
	Button submit;

	private int year = 2014;
	private int month = 0;
	private int day = 1;
	private int hour = 18;
	private int minute = 30;
	private boolean pictureReady = false;
	private Bitmap pictureBitmap;
	private ProgressDialog progressDialog;
	private File imageFile;
	private String fileName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.ActionBar);
	}

	@AfterViews
	protected void AfterViews() {
		initTimeAndTypeInputter();
		initPictureSelector();
	}

	private void initPictureSelector() {
		picture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CharSequence[] items = { "从相册选择", "拍摄新图片" };
				new AlertDialog.Builder(AddActActivity.this).setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
							intent.addCategory(Intent.CATEGORY_OPENABLE);
							intent.setType("image/*");
							startActivityForResult(Intent.createChooser(intent, "选择图片"), 0);
						} else {
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(intent, 1);
						}
					}
				}).create().show();
			}

		});
	}

	private void initTimeAndTypeInputter() {
		time.setInputType(InputType.TYPE_NULL);
		time.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(AddActActivity.this, new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						AddActActivity.this.year = year;
						AddActActivity.this.month = monthOfYear;
						AddActActivity.this.day = dayOfMonth;
						new TimePickerDialog(AddActActivity.this, new OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
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
				List<ContentValues> cvs = SQLiteWorker.selectFromSQLite("type", new String[] { "name" }, AddActActivity.this);
				final List<CharSequence> items = new ArrayList<CharSequence>();
				for (ContentValues cv : cvs) {
					items.add(cv.getAsString("name"));
				}

				new AlertDialog.Builder(AddActActivity.this)
						.setItems(items.toArray(new String[items.size()]), new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								type.setText(items.get(which));
							}

						}).create().show();
			}

		});
		type.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					v.performClick();
			}

		});
	}

	@Click(R.id.add_submit)
	protected void doSubmit() {
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
				Log.w("handler", MessageHandler.TIME_OUT_STR);
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

		if (TextUtils.isEmpty(routString)) {
			rout.setError(getString(R.string.error_field_required));
			focuseView = rout;
			cancel = true;
		}
		if (TextUtils.isEmpty(introString)) {
			intro.setError(getString(R.string.error_field_required));
			focuseView = intro;
			cancel = true;
		}
		if (TextUtils.isEmpty(themeString)) {
			theme.setError(getString(R.string.error_field_required));
			focuseView = theme;
			cancel = true;
		}
		if (TextUtils.isEmpty(reporterString)) {
			reporter.setError(getString(R.string.error_field_required));
			focuseView = reporter;
			cancel = true;
		}
		if (TextUtils.isEmpty(typeString)) {
			type.setError(getString(R.string.error_field_required));
			focuseView = type;
			cancel = true;
		}
		if (TextUtils.isEmpty(addresString)) {
			address.setError(getString(R.string.error_field_required));
			focuseView = address;
			cancel = true;
		}
		if (TextUtils.isEmpty(datetime)) {
			time.setError(getString(R.string.error_field_required));
			focuseView = time;
			cancel = true;
		}
		if (TextUtils.isEmpty(nameString)) {
			name.setError(getString(R.string.error_field_required));
			focuseView = name;
			cancel = true;
		}

		if (cancel) {
			focuseView.requestFocus();
			return;
		}

		if (!pictureReady) {
			Toast.makeText(AddActActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
			return;
		}
		/*
		 * DatabaseConnector connector = new DatabaseConnector(); progressDialog
		 * = ProgressDialog.show(AddActActivity.this, "上传中", "图片文件较大，请耐心等待");
		 * connector.addParams(DatabaseConnector.UPLOAD, "SETACTIVITY");
		 * connector.addParams("name", nameString);
		 * connector.addParams("address", addresString);
		 * connector.addParams("content", introString);
		 * connector.addParams("type", typeString); connector.addParams("theme",
		 * themeString); connector.addParams("date", datetime.substring(0,
		 * datetime.indexOf(' '))); connector.addParams("time",
		 * datetime.substring(datetime.indexOf(' ')+1));
		 * connector.addParams("procedure", routString);
		 * connector.addParams("reporter_info", reporterString);
		 * connector.asyncUpload(pictureBitmap, adapter);
		 */
		String query = Encoder
				.encodeString(
						"insert into activity(`name`, `detail_address`, `content`, `type`, `theme`, `procedure`, `picture_urls`, `reporter`, `organiser_id`, `location`) "
								+ "select '%s', '%s', '%s', (select id_type from type where name = '%s'), '%s', '%s', '%s', '%s', null, null",
						nameString, addresString, introString, typeString, themeString, routString, "/pictures/activity/" + fileName,
						reporterString);
		Ion.with(this).load(DatabaseConnector.url).setMultipartParameter(DatabaseConnector.UPLOAD, "activity")
				.setMultipartFile("file", imageFile).setMultipartParameter("file_name", fileName)
				.setMultipartParameter(DatabaseConnector.QUERY, query).asString().setCallback(new FutureCallback<String>() {

					@Override
					public void onCompleted(Exception arg0, String arg1) {
						Toast.makeText(AddActActivity.this, arg1, 10000000).show();
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		processImage(resultCode, data);
	}

	@UiThread
	protected void processImage(int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();

			fileName = uri.getPath().substring(uri.getPath().lastIndexOf(":") + 1) + ".png";

			try {
				InputStream pictureInputStream = getContentResolver().openInputStream(uri);
				if (pictureBitmap != null)
					pictureBitmap.recycle();
				BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
				bmpFactoryOptions.inSampleSize = 8;

				pictureBitmap = BitmapFactory.decodeStream(pictureInputStream, null, bmpFactoryOptions);

				makeTempImageFile(uri);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			picture.setImageBitmap(pictureBitmap);
			pictureReady = true;
		}
	}

	@Background
	protected void makeTempImageFile(Uri uri) {
		try {
			InputStream pictureInputStream = getContentResolver().openInputStream(uri);

			if (imageFile != null && imageFile.exists()) {
				OutputStream os2 = new FileOutputStream(imageFile);
				os2.write(new byte[] {}, 0, 0);
				os2.close();
			} else {
				imageFile = new File(getExternalFilesDir("tmp") + "/tmp.jpg");
				imageFile.createNewFile();
			}

			OutputStream os = new FileOutputStream(imageFile);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = pictureInputStream.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			pictureInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setTime() {
		time.setText(new StringBuilder().append(year).append('.').append(String.format("%02d", month + 1)).append('.')
				.append(String.format("%02d", day)).append(' ').append(String.format("%02d", hour)).append(':')
				.append(String.format("%02d", minute)));
	}
}
