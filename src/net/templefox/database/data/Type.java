package net.templefox.database.data;

import java.io.Serializable;
import java.text.ParseException;
import org.json.JSONException;
import org.json.JSONObject;



import android.content.ContentValues;

public class Type extends Entity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6017693500517684612L;
	private Integer id;
	private String name;
	public Type() {
	}
	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public ContentValues getContentValues() {
		ContentValues value = new ContentValues();
		value.put("name", name);
		value.put("id", id);
		return value;
	}
	
	@Override
	public String getTableName() {
		return "type";
	}
	
	@Override
	public Entity resolveJSON(JSONObject obj) throws JSONException,
			ParseException {
		this.setId(obj.getInt("id"));
		this.setName(obj.getString("name"));
		return this;
	}
}
