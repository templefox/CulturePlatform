package com.example.database.data;

// Generated 2013-8-21 19:58:16 by Hibernate Tools 3.4.0.CR1

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * User generated by hbm2java
 */
public class User implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4497016747707135215L;
	private Integer id;
	private Location location;
	private String name;
	private Date regTime;
	private String phoneNum;
	private String EMail;
	private int authority;
	private String password;

	public User() {
	}

	public User(String nickname, Date regTime,
			int authority, String password) {
		this.name = nickname;
		this.regTime = regTime;
		this.authority = authority;
		this.password = password;
	}

	public User(Location location, String nickname,
			Date regTime, String phoneNum, String EMail, int authority,
			String password) {
		this.location = location;
		this.name = nickname;
		this.regTime = regTime;
		this.phoneNum = phoneNum;
		this.EMail = EMail;
		this.authority = authority;
		this.password = password;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}



	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getRegTime() {
		return this.regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public String getPhoneNum() {
		return this.phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getEMail() {
		return this.EMail;
	}

	public void setEMail(String EMail) {
		this.EMail = EMail;
	}

	public int isAuthority() {
		return this.authority;
	}

	public void setAuthority(int authority) {
		this.authority = authority;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User transJSON(JSONObject obj)
			throws JSONException, ParseException {
		User user = this;
		user.setId(obj.getInt("id"));
		user.setName(obj.getString("name"));
		user.setEMail(obj.getString("E_mail"));
		user.setPhoneNum(obj.getString("phone_num"));
		user.setRegTime(DateFormat.getDateInstance().parse(obj.getString("reg_time")));
		user.setAuthority(obj.getInt("authority"));
		return user;
	}

	
}
