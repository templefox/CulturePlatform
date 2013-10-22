package com.example.iyangpu.data;

// Generated 2013-8-21 19:58:16 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * User generated by hbm2java
 */
public class User implements java.io.Serializable {

	private Integer id;
	private Location location;
	private String loginName;
	private String nickname;
	private Date regTime;
	private String phoneNum;
	private String EMail;
	private int authority;
	private String password;
	private Set attentions = new HashSet(0);
	private Set comments = new HashSet(0);
	private Set activities = new HashSet(0);

	public User() {
	}

	public User(String loginName, String nickname, Date regTime,
			int authority, String password) {
		this.loginName = loginName;
		this.nickname = nickname;
		this.regTime = regTime;
		this.authority = authority;
		this.password = password;
	}

	public User(Location location, String loginName, String nickname,
			Date regTime, String phoneNum, String EMail, int authority,
			String password, Set attentions, Set comments, Set activities) {
		this.location = location;
		this.loginName = loginName;
		this.nickname = nickname;
		this.regTime = regTime;
		this.phoneNum = phoneNum;
		this.EMail = EMail;
		this.authority = authority;
		this.password = password;
		this.attentions = attentions;
		this.comments = comments;
		this.activities = activities;
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

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
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

	public Set getAttentions() {
		return this.attentions;
	}

	public void setAttentions(Set attentions) {
		this.attentions = attentions;
	}

	public Set getComments() {
		return this.comments;
	}

	public void setComments(Set comments) {
		this.comments = comments;
	}

	public Set<Activity> getActivities() {
		return this.activities;
	}

	public void setActivities(Set activities) {
		this.activities = activities;
	}

}