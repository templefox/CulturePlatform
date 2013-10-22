package com.example.iyangpu.data;

// Generated 2013-8-21 19:58:16 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * Comment generated by hbm2java
 */
public class Comment implements java.io.Serializable {

	private Integer id;
	private Activity activity;
	private User user;
	private String content;
	private Date datatime;
	private String pictureUrl;

	public Comment() {
	}

	public Comment(Date datatime) {
		this.datatime = datatime;
	}

	public Comment(Activity activity, User user, String content, Date datatime,
			String pictureUrl) {
		this.activity = activity;
		this.user = user;
		this.content = content;
		this.datatime = datatime;
		this.pictureUrl = pictureUrl;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Activity getActivity() {
		return this.activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDatatime() {
		return this.datatime;
	}

	public void setDatatime(Date datatime) {
		this.datatime = datatime;
	}

	public String getPictureUrl() {
		return this.pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

}