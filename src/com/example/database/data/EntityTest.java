package com.example.database.data;

import java.util.List;

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
		Entity.insertIntoSQLite(activity, this.getContext());
		
		List<ContentValues> cvs = 
				Entity.selectFromSQLite("activity", new String[]{"name"}, 
						"name = ?", 
				new String[]{"TEST"}, getContext());
		
		Assert.assertEquals(1, cvs.size());
		for(ContentValues cv : cvs){
			Assert.assertEquals("TEST",cv.get("name"));
		}
	}
}
