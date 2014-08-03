package net.templefox.database;

import net.templefox.cultureplatform.R;

import junit.framework.Assert;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.AndroidTestCase;

public class AsyncUploadTest extends AndroidTestCase {
	DatabaseConnector connector;
	MessageAdapter adapter;
	Bitmap picture;
	
	@Override
	protected void setUp() throws Exception {
		connector = new DatabaseConnector();
		connector.addParams(DatabaseConnector.UPLOAD, "TEST");
		picture = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.init);
	}

	public void testAsyncUpload() {
		adapter = new MessageAdapter(){

			@Override
			public void onDone(String ret) {
				// TODO Auto-generated method stub
				Assert.assertEquals("TEST", ret);
			}
			
		};
		
		try{
			connector.asyncUpload(picture, adapter);
		}catch(Exception e){
			fail("Something wrong!");
		}
	}

}
