package com.example.database.data;

// Generated 2013-8-21 19:58:16 by Hibernate Tools 3.4.0.CR1

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ContentValues;

/**
 * Activity generated by hbm2java
 */
@SuppressLint("SimpleDateFormat")
public class Activity extends Entity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3286174095424688071L;
	private Integer id;
	private Integer user;
	private Integer location;
	private String name;
	private String address;
	private String content;
	private String type;
	private String theme;
	private Date date;
	private Date time;
	private String procedure;
	private String pictureUrl;
	private String reporterInfo;
	private Integer isAttention = 0;
	private Integer isRating = 0;
	private Float reporterRating = 0f;
	private Float staffRating = 0f;
	private Float contentRating = 0f;
	private Float environmentRating = 0f;
	private int temperature;

	public Activity() {
	}

	public Activity(String name, String address, String type, int temperature) {
		this.name = name;
		this.address = address;
		this.type = type;
		this.temperature = temperature;
	}

	public Activity(int user, Integer location, String name, String address,
			String content, String type, String theme, Date data, Date time,
			String procedure, String pictureUrl, String reporterInfo,
			int temperature) {
		this.user = user;
		this.location = location;
		this.name = name;
		this.address = address;
		this.content = content;
		this.type = type;
		this.theme = theme;
		this.date = data;
		this.time = time;
		this.procedure = procedure;
		this.pictureUrl = pictureUrl;
		this.reporterInfo = reporterInfo;
		this.temperature = temperature;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getUser() {
		return this.user;
	}

	public void setUser(int user) {
		this.user = user;
	}

	public Integer getLocation() {
		return this.location;
	}

	public void setLocation(Integer location) {
		this.location = location;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTheme() {
		return this.theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date data) {
		this.date = data;
	}

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getProcedure() {
		return this.procedure;
	}

	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}

	public String getPictureUrl() {
		return this.pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getReporterInfo() {
		return this.reporterInfo;
	}

	public void setReporterInfo(String reporterInfo) {
		this.reporterInfo = reporterInfo;
	}

	public int getTemperature() {
		return this.temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public Integer getisAttention() {
		return this.isAttention;
	}
	
	public void setisAttention(int i) {
		isAttention = i;
	}
	public Float getReporterRating() {
		return this.reporterRating;
	}

	public void setReporterRating(Float reporterRating) {
		this.reporterRating = reporterRating;
	}

	public Float getStaffRating() {
		return this.staffRating;
	}

	public void setStaffRating(Float staffRating) {
		this.staffRating = staffRating;
	}

	public Float getContentRating() {
		return this.contentRating;
	}

	public void setContentRating(Float contentRating) {
		this.contentRating = contentRating;
	}

	public Float getEnvironmentRating() {
		return this.environmentRating;
	}

	public void setEnvironmentRating(Float environmentRating) {
		this.environmentRating = environmentRating;
	}
	
	
	public Integer getIsRating() {
		return this.isRating;
	}

	public void setIsRating(Integer isRating) {
		this.isRating = isRating;
	}
	
	@Override
	public boolean equals(Object o) {
		Activity be = (Activity) o;
		return be.id == this.id;
	}

	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public Activity transJSON(JSONObject obj)
			throws JSONException, ParseException {
		Activity activity = this;
		activity.setId(obj.getInt("id"));
		activity.setAddress(obj.getString("address"));
		activity.setContent(obj.getString("content"));
		activity.setName(obj.getString("name"));
		activity.setType(obj.getString("type"));
		activity.setPictureUrl(obj.getString("picture_url"));
		activity.setisAttention(0);
		try {
			activity.setTime(new SimpleDateFormat("HH:mm:ss").parse(obj.getString("time")));
			activity.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(obj.getString("date")));
			activity.setLocation(obj.getInt("locationID"));
			activity.setUser(obj.getInt("organiserID"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return activity;
	}
	

	@Override
	protected ContentValues getContentValues() {
		ContentValues value = new ContentValues();
		value.put("id", id);
		value.put("address", address);
		value.put("content", content);
		value.put("type", type);
		value.put("name", name);
		value.put("locationID", location);
		value.put("organiserID", user);
		value.put("picture_url", pictureUrl);
		try {
			value.put("time", new SimpleDateFormat("HH:mm:ss").format(time));
			value.put("date", new SimpleDateFormat("yyyy-MM-dd").format(date));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "activity";
	}

}
