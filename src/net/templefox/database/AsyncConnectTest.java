package net.templefox.database;

import junit.framework.Assert;
import android.test.AndroidTestCase;

public class AsyncConnectTest extends AndroidTestCase {
	DatabaseConnector connector;
	MessageAdapter adapter;
	
	@Override
	protected void setUp() throws Exception {
		connector = new DatabaseConnector();
		connector.addParams(DatabaseConnector.METHOD, "TEST");
		
	}

	public void testAsyncConnect() {
		adapter = new MessageAdapter(){

			@Override
			public void onDone(String ret) {
				// TODO Auto-generated method stub
				Assert.assertEquals("TEST", ret);
			}
			
		};
		connector.asyncConnect(adapter);
	}

}
