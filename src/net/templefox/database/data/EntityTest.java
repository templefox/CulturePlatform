package net.templefox.database.data;

import java.util.List;

import net.templefox.database.SQLiteWorker;

import junit.framework.Assert;

import android.content.ContentValues;
import android.test.AndroidTestCase;

public class EntityTest extends AndroidTestCase {
	Activity activity = new Activity();
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * 
	 */
	public void testInsert()
	{
		activity.setId(99);
		activity.setName("TEST");
		SQLiteWorker.insertIntoSQLite(activity, this.getContext());
		
		List<ContentValues> cvs = 
				SQLiteWorker.selectFromSQLite("activity", new String[]{"name"}, 
						"name = ?", 
				new String[]{"TEST"}, getContext());
		
		Assert.assertEquals(1, cvs.size());
		for(ContentValues cv : cvs){
			Assert.assertEquals("TEST",cv.get("name"));
		}
	}
}
