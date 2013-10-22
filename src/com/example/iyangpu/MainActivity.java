package com.example.iyangpu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.iyangpu.data.Comment;
import com.example.iyangpu.data.User;
import com.example.iyangpu.data.Location;
import com.example.iyangpu.fragments.CalendarFrag;
import com.example.iyangpu.fragments.DetailsFrag;
import com.example.iyangpu.fragments.RatingFrag;
import com.example.iyangpu.fragments.TheActivitiesViewFrag;
import com.example.iyangpu.fragments.TopToolFragA;
import com.example.iyangpu.fragments.TopToolFragB;


import android.R.anim;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

@SuppressLint({ "NewApi", "HandlerLeak" })
public class MainActivity extends Activity {
	//private List<com.example.iyangpu.data.Activity> activities;
	private User currentUser = new User();
	private Set<com.example.iyangpu.data.Activity> allActivities;
	private Set<Location> allLocations = new HashSet<Location>();
	public ProgressDialog progressDialog;
	private List<String> list = new ArrayList<String>();
	private ArrayAdapter<String> adapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FragmentTransaction fTransaction = this.getFragmentManager().beginTransaction();
		fTransaction.replace(R.id.fragment_to_be_changed_top, new TopToolFragA());
		fTransaction.replace(R.id.fragment_to_be_changed_middle, new TheActivitiesViewFrag());
		//fTransaction.replace(R.id.fragment_to_be_changed_middle, new CalendarFrag());
		fTransaction.commit();
		
		setContentView(R.layout.main_screen);
		//Button myActivity = (Button)findViewById(R.id.button_myActivity);
		//lo= (LinearLayout)findViewById(R.id.inner);
		progressDialog = ProgressDialog.show(this, "正在连接", "请等待",true,true);
		loadCurrenUser();
		
	
		//myActivity.setOnClickListener(new myActivityOnClickListener());
		//myActivity.callOnClick();
	}

	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {

		menu.clear();
		MenuItem item = menu.add("注销");
		

        //TODO
        return super.onPrepareOptionsMenu(menu);
    }
	
	public ArrayAdapter<String> getAdapter() {
		if(adapter != null){
			return adapter;
		}
		else {
			adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, list);
			return adapter;			
		}

	}
	
	class GetMyActivity extends Handler{
		@Override
		public void handleMessage(Message msg) {
		
			Bundle b = msg.getData();
			String get = b.getString(null);
			if(get == null || get.isEmpty()){
				//Toast.makeText(getApplicationContext(), "你还没有关注活动", Toast.LENGTH_SHORT).show();
			}				
			else if(get.equals("CONERROR")){
				Toast.makeText(getApplicationContext(), "连接错误，请检查网络连接和服务器状态", Toast.LENGTH_SHORT).show();
			}
			else
			{
				try {
					JSONArray array = new JSONArray(get);
					Set<com.example.iyangpu.data.Activity> activities = new HashSet<com.example.iyangpu.data.Activity>();
					for(int i=0 ; i<array.length();i++)
					{
						JSONObject obj = array.getJSONObject(i);
						com.example.iyangpu.data.Activity activity = new com.example.iyangpu.data.Activity(
								obj.getInt("organiserID"), obj.getInt("locationID"), obj.getString("name"), obj.getString("address"),obj.getString("content")
								, obj.getString("type"), obj.getString("theme"), SimpleDateFormat.getDateInstance().parse(obj.getString("date"))
								, new SimpleDateFormat("HH:mm:ss").parse(obj.getString("time")),obj.getString("procedure"), obj.getString("picture_url")
								, obj.getString("reporter_info"), obj.getInt("temperature"), null, null);
						activity.setId(obj.getInt("id"));
						activity.setIsRating(obj.getInt("isRating"));
						activity.setisAttention(1);
						activities.add(activity);
						
					}
					currentUser.setActivities(activities);
					allActivities = activities;
					loadAllActivities();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	
	}

	class GetCurrentUser extends Handler{
		@Override
		public void handleMessage(Message msg){
			Bundle b = msg.getData();
			String get = b.getString(null);
			if(get == null || get.isEmpty()){
				Toast.makeText(getApplicationContext(), "用户不存在", Toast.LENGTH_SHORT).show();
				Intent tolog = new Intent(getApplication(),LoginActivity.class);
				startActivity(tolog);
				MainActivity.this.finish(); //或许可以不结束
			}
			else if(get.equals("CONERROR")){
				Toast.makeText(getApplicationContext(), "连接错误，请检查网络连接和服务器状态", Toast.LENGTH_SHORT).show();
				Intent tolog = new Intent(getApplication(),LoginActivity.class);
				startActivity(tolog);
				MainActivity.this.finish();
			}
			else
			{
					
				try {
					JSONArray array = new JSONArray(get);
					for(int i=0 ; i<array.length();i++)
					{
						JSONObject obj = array.getJSONObject(i);
						String name = obj.getString("reg_time");
						currentUser.setId(obj.getInt("id"));
						currentUser.setNickname(obj.getString("nickname"));
						currentUser.setEMail(obj.getString("E_mail"));
						currentUser.setPhoneNum(obj.getString("phone_num"));
						currentUser.setLoginName(obj.getString("login_name"));
						currentUser.setRegTime(SimpleDateFormat.getDateInstance().parse(obj.getString("reg_time")));
						currentUser.setAuthority(obj.getInt("authority"));
						Toast.makeText(getApplicationContext(), name+"OK", Toast.LENGTH_SHORT).show();
						
						loadMyActivities();
						
						
					}
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(), e.getMessage(),
						     Toast.LENGTH_SHORT).show();
				} catch (ParseException e) {
					Toast.makeText(getApplicationContext(), e.getMessage(),
						     Toast.LENGTH_SHORT).show();
				} finally{
				}
			}
		}
	}

	class GetAllActivity extends Handler{

		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String get = b.getString(null);
			if(get == null || get.isEmpty()){
				Toast.makeText(getApplicationContext(), "暂无活动", Toast.LENGTH_SHORT).show();
			}				
			else if(get.equals("CONERROR")){
				Toast.makeText(getApplicationContext(), "连接错误，请检查网络连接和服务器状态", Toast.LENGTH_SHORT).show();
			}
			else
			{
				try {
					JSONArray array = new JSONArray(get);
					Set<com.example.iyangpu.data.Activity> activities = new HashSet<com.example.iyangpu.data.Activity>();
					for(int i=0 ; i<array.length();i++)
					{
						JSONObject obj = array.getJSONObject(i);
						com.example.iyangpu.data.Activity activity = new com.example.iyangpu.data.Activity(
								obj.getInt("organiserID"), obj.getInt("locationID"), obj.getString("name"), obj.getString("address"),obj.getString("content")
								, obj.getString("type"), obj.getString("theme"), SimpleDateFormat.getDateInstance().parse(obj.getString("date"))
								, new SimpleDateFormat("HH:mm:ss").parse(obj.getString("time")),obj.getString("procedure"), obj.getString("picture_url")
								, obj.getString("reporter_info"), obj.getInt("temperature"), null, null);
						activity.setId(obj.getInt("id"));
						activities.add(activity);
					}
					allActivities.addAll(activities);
					loadAllLocation();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (UnsupportedOperationException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	class GetAllLocation extends Handler{
		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String get = b.getString(null);
			if(get == null || get.isEmpty()){
				Toast.makeText(getApplicationContext(), "暂无数据，请联系数据库管理员", Toast.LENGTH_SHORT).show();
			}				
			else if(get.equals("CONERROR")){
				Toast.makeText(getApplicationContext(), "连接错误，请检查网络连接和服务器状态", Toast.LENGTH_SHORT).show();
			}
			else
			{
				try {
					JSONArray array = new JSONArray(get);
					Set<Location> locations = new HashSet<Location>();
						for(int i=0 ; i<array.length();i++)
						{
							JSONObject obj = array.getJSONObject(i);
							Location location = new Location(obj.getString("name"), obj.getString("detail"), obj.getString("picture_url"), obj.getString("address")
									, null, null);
							locations.add(location);
						}
						
						allLocations = locations;
						
						reload_top_spinners(false);
				} catch (JSONException e) {
					e.printStackTrace();
				}  catch (UnsupportedOperationException e) {
					e.printStackTrace();
				} finally {
					if(progressDialog.isShowing())
						progressDialog.dismiss();
				}
			}
		}
	}
	
	class GetActivity extends Handler{
		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String get = b.getString(null);
			if(get == null || get.isEmpty() || get.equals("-")){
				refresh__activities(new HashSet<com.example.iyangpu.data.Activity>(),"暂无活动");
				Toast.makeText(getApplicationContext(), "暂无活动", Toast.LENGTH_SHORT).show();
			}				
			else if(get.equals("CONERROR")){
				Toast.makeText(getApplicationContext(), "连接错误，请检查网络连接和服务器状态", Toast.LENGTH_SHORT).show();
			}
			else
			{
				try {
					JSONArray array = new JSONArray(get);
					Set<com.example.iyangpu.data.Activity> activities = new HashSet<com.example.iyangpu.data.Activity>();
					for(int i=0 ; i<array.length();i++)
					{
						JSONObject obj = array.getJSONObject(i);
						com.example.iyangpu.data.Activity activity = new com.example.iyangpu.data.Activity(
								obj.getInt("organiserID"), obj.getInt("locationID"), obj.getString("name"), obj.getString("address"),obj.getString("content")
								, obj.getString("type"), obj.getString("theme"), SimpleDateFormat.getDateInstance().parse(obj.getString("date"))
								, new SimpleDateFormat("HH:mm:ss").parse(obj.getString("time")),obj.getString("procedure"), obj.getString("picture_url")
								, obj.getString("reporter_info"), obj.getInt("temperature"), null, null);
						activity.setId(obj.getInt("id"));
						activities.add(activity);
						if(!allActivities.contains(activity)){
							allActivities.add(activity);
						}
					}
					
					refresh__activities(activities,"筛选活动");
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (UnsupportedOperationException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class GetActivitySearch extends Handler{
		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String get = b.getString(null);
			if(get == null || get.isEmpty() || get.equals("-")){
				refresh__activities(new HashSet<com.example.iyangpu.data.Activity>(),"没有相关活动");
				Toast.makeText(getApplicationContext(), "没有相关活动", Toast.LENGTH_SHORT).show();
			}				
			else if(get.equals("CONERROR")){
				Toast.makeText(getApplicationContext(), "连接错误，请检查网络连接和服务器状态", Toast.LENGTH_SHORT).show();
			}
			else
			{
				try {
					JSONArray array = new JSONArray(get);
					Set<com.example.iyangpu.data.Activity> activities = new HashSet<com.example.iyangpu.data.Activity>();
					for(int i=0 ; i<array.length();i++)
					{
						JSONObject obj = array.getJSONObject(i);
						com.example.iyangpu.data.Activity activity = new com.example.iyangpu.data.Activity(
								obj.getInt("organiserID"), obj.getInt("locationID"), obj.getString("name"), obj.getString("address"),obj.getString("content")
								, obj.getString("type"), obj.getString("theme"), SimpleDateFormat.getDateInstance().parse(obj.getString("date"))
								, new SimpleDateFormat("HH:mm:ss").parse(obj.getString("time")),obj.getString("procedure"), obj.getString("picture_url")
								, obj.getString("reporter_info"), obj.getInt("temperature"), null, null);
						activity.setId(obj.getInt("id"));
						activities.add(activity);
						if(!allActivities.contains(activity)){
							allActivities.add(activity);
						}
					}
					
					refresh__activities(activities,"查找活动");
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (UnsupportedOperationException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class SetAttention extends Handler{
		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String get = b.getString(null);
			if(get == null || get.isEmpty() || get.equals("-")){
				Toast.makeText(getApplicationContext(), "发生错误", Toast.LENGTH_SHORT).show();
			}				
			else if(get.equals("CONERROR")){
				Toast.makeText(getApplicationContext(), "连接错误，请检查网络连接和服务器状态", Toast.LENGTH_SHORT).show();
			}
			else if(get.equals("ERROR"))
			{
				Toast.makeText(getApplicationContext(), "数据库操作失败", Toast.LENGTH_SHORT).show();
			}
			else if(get.startsWith("DONE"))
			{
				
				int activity_id = Integer.parseInt(get.substring(4));
				
				for (com.example.iyangpu.data.Activity activity : allActivities) {
					if(activity.getId() == activity_id){
						currentUser.getActivities().add(activity);
						break;
					}
				}
				
				Toast.makeText(getApplicationContext(), "关注成功", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	class SetRating extends Handler{
		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String get = b.getString(null);
			if(get == null || get.isEmpty() || get.equals("-")){
				Toast.makeText(getApplicationContext(), "发生错误", Toast.LENGTH_SHORT).show();
			}				
			else if(get.equals("CONERROR")){
				Toast.makeText(getApplicationContext(), "连接错误，请检查网络连接和服务器状态", Toast.LENGTH_SHORT).show();
			}
			else if(get.equals("ERROR"))
			{
				Toast.makeText(getApplicationContext(), "数据库操作失败", Toast.LENGTH_SHORT).show();
			}
			else if(get.startsWith("DONE"))
			{
				
				int activity_id = Integer.parseInt(get.substring(4));
				
				for (com.example.iyangpu.data.Activity activity : allActivities) {
					if(activity.getId() == activity_id){
						activity.setIsRating(1);
						break;
					}
				}
				
				Toast.makeText(getApplicationContext(), "评论成功", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	class GetComment extends Handler{
		com.example.iyangpu.data.Activity targetActivity;
		
		public GetComment setActivity(com.example.iyangpu.data.Activity activity) {
			this.targetActivity = activity;
			return this;
		}
		
		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String get = b.getString(null);
			if(get == null || get.isEmpty() || get.equals("-")){
				Toast.makeText(getApplicationContext(), "没人评论", Toast.LENGTH_SHORT).show();
			}				
			else if(get.equals("CONERROR")){
				Toast.makeText(getApplicationContext(), "连接错误，请检查网络连接和服务器状态", Toast.LENGTH_SHORT).show();
			}
			else
			{
				try {
					JSONArray array = new JSONArray(get);
					Set<Comment> comments = new HashSet<Comment>();
					for(int i=0 ; i<array.length();i++)
					{
						JSONObject obj = array.getJSONObject(i);
						Comment comment = new Comment(null, null, obj.getString("Content"), 
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(obj.getString("datetime")), obj.getString("picture_url"));
						comment.setId(obj.getInt("id"));
						
						User user = new User(null,obj.getString("login_name"),obj.getString("nickname"),
								new SimpleDateFormat("yyyy-MM-dd").parse(obj.getString("reg_time")), obj.getString("phone_num"), obj.getString("E_mail")
								, obj.getInt("authority"), null, null, null, null);
						
						comment.setUser(user);
						
						comments.add(comment);
					}
					targetActivity.setComments(comments);
					refresh__detail_comments(targetActivity.getComments());
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (UnsupportedOperationException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void loadCurrenUser()
	{	
		Map<String,String> para = new HashMap<String, String>();
		para.put("name", getIntent().getExtras().getString("name"));
		para.put("password", getIntent().getExtras().getString("password"));
		para.put("METHOD", "GETUSER");
		
		
		DBUtil.asyncConnect(DBUtil.target_url.toString(), para, "POST", new GetCurrentUser());

	}

	private void loadAllActivities()
	{
		Map<String,String> para = new HashMap<String, String>();
		para.put("METHOD", "GETACTIVITY");
		DBUtil.asyncConnect(DBUtil.target_url, para, "POST", new GetAllActivity());
	}
	
	private void loadMyActivities() {
		Map<String,String> para = new HashMap<String, String>();
		para.put("METHOD", "GETACTIVITY_USER");
		para.put("name", currentUser.getLoginName());
		DBUtil.asyncConnect(DBUtil.target_url, para, "POST", new GetMyActivity());
	}
	
	private void loadActivities_search(String words) {
		Map<String,String> para = new HashMap<String, String>();
		para.put("METHOD", "SEARCH");
		para.put("words", words);
		DBUtil.asyncConnect(DBUtil.target_url, para, "POST", new GetActivitySearch());
	}
	
	private void loadActivities(String order,String locationString, String type){
		Map<String,String> para = new HashMap<String, String>();
		para.put("METHOD", "GETACTIVITY");
		para.put("address", locationString);
		para.put("type", type);
		para.put("order", order);
		DBUtil.asyncConnect(DBUtil.target_url, para, "POST", new GetActivity());
	}
	
	private void loadAllLocation() {
		Map<String, String> para = new HashMap<String, String>();
		para.put("METHOD", "GETLOCATION");
		DBUtil.asyncConnect(DBUtil.target_url, para, "POST", new GetAllLocation());
	}
	
	public void loadComments(com.example.iyangpu.data.Activity target) {
		Map<String, String> para = new HashMap<String, String>();
		para.put("METHOD", "GETCOMMENT");
		para.put("activity_id", target.getId().toString());
		DBUtil.asyncConnect(DBUtil.target_url, para, "POST", new GetComment().setActivity(target));
	}
	
	private void refresh__activitiesInDateView(Set<com.example.iyangpu.data.Activity> activities) {
		 CalendarFrag _calendar = (CalendarFrag) getFragmentManager().findFragmentById(R.id.fragment_to_be_changed_middle);
		 _calendar.refresh_frag_calendar(activities);
	}
	
	private void refresh__activities(Set<com.example.iyangpu.data.Activity> activities, String title){
		TheActivitiesViewFrag _mid = (TheActivitiesViewFrag) getFragmentManager().findFragmentById(R.id.fragment_to_be_changed_middle);
		_mid.refresh_frag_my_activities(activities,title);
	}
	
	private void refresh__spinners(List<String> loc , List<String> type){
		TopToolFragA a = (TopToolFragA) getFragmentManager().findFragmentById(R.id.fragment_to_be_changed_top);
		a.setSpinnerContent(loc, type);
	}
	
	private void refresh__detail_comments(Set<Comment> comments) {
		DetailsFrag frag = (DetailsFrag) getFragmentManager().findFragmentById(R.id.fragment_to_be_changed_middle);
		frag.refresh_details_comments(comments);
	}
	
	public void reload_middle_all_activities(boolean refresh){
		if(!refresh){
			refresh__activities(allActivities,"已有活动");
		}else{
			loadAllActivities();
			//reload_middle_all_activities(false);
		}
	}
	
	public void reload_middle_my_activities(boolean refresh) {
		if(!refresh){
			refresh__activities(currentUser.getActivities(),"我的活动");
		}
		else{
			loadMyActivities();
			//reload_middle_my_activities(false);
		}
	}

	public void reload_middle_activities_with_conditions(String order,String location, String type){
		if(location.equals("全部地点")&&type.equals("全部类型")){
			refresh__activities(allActivities,"已有活动");
		}
		else {
			if(location.equals("全部地点")) location = "";
			if(type.equals("全部类型")) type = "";
			loadActivities(order, location, type);
		}
	}
	
	public void reload_top_spinners(boolean refresh){
		if(!refresh){
			if(allActivities == null || allLocations == null);
			else{
				Set<String> locSet = new HashSet<String>();
				Set<String> typeSet	= new HashSet<String>();
			
				for(Location l:allLocations){
					locSet.add(l.getName());
				}
				for (com.example.iyangpu.data.Activity activity : allActivities) {
					typeSet.add(activity.getType());
				}
				refresh__spinners(new ArrayList<String>(locSet), new ArrayList<String>(typeSet));
			}
		}else{
			
		}
	}

	public void reload_activities_with_date(int year, int month , int day){
		StringBuilder sb = new StringBuilder();
		sb.append(year).append('-').append(month).append('-').append(day);
		Set<com.example.iyangpu.data.Activity> ret = new HashSet<com.example.iyangpu.data.Activity>();
		Set<com.example.iyangpu.data.Activity> activities = currentUser.getActivities();
		GregorianCalendar calendar = new GregorianCalendar(year, month, day);
		for (com.example.iyangpu.data.Activity activity : activities) {
			if (activity.getDate().compareTo(calendar.getTime())==0) {
				ret.add(activity);
			}
		}

		
		refresh__activitiesInDateView(ret);
	}
	
	public void reload_activities_to_rating() {
		Date date = new Date();
		Set<com.example.iyangpu.data.Activity> ret = new HashSet<com.example.iyangpu.data.Activity>();
		Set<com.example.iyangpu.data.Activity> activities = currentUser.getActivities();
		for(com.example.iyangpu.data.Activity activity:activities){
			if(activity.getDate().compareTo(date)<0 && activity.getIsRating().equals(0)){
				ret.add(activity);
			}
		}
		
		refresh__activities(ret,"已完成活动");
	}
	
	public void reload_activities_with_search(String words){
		loadActivities_search(words);
	}
	
	public void reload_detail_comments(com.example.iyangpu.data.Activity target,boolean refresh) {
		if(!refresh){
			if(target.getComments() == null) refresh = true;
		}
		if(refresh) {
			loadComments(target);
		}
		
	}
	
	public void upload_attention(Integer activity_id) {
		Map<String,String> para = new HashMap<String, String>();
		para.put("METHOD", "SETATTENTION");
		para.put("user_id", currentUser.getId().toString());
		para.put("activity_id", activity_id.toString());
		DBUtil.asyncConnect(DBUtil.target_url, para, "POST", new SetAttention());
	}
	
	
	public void start_rating(com.example.iyangpu.data.Activity activity) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_to_be_changed_middle);
		transaction.hide(fragment).add(R.id.fragment_to_be_changed_middle, new RatingFrag().setActivity(activity));
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	public void upload_rating(Integer content,Integer reporter,Integer staff,Integer environment,com.example.iyangpu.data.Activity activity) {
		Map<String,String> para = new HashMap<String, String>();
		para.put("METHOD", "SETRATING");
		para.put("content", content.toString());
		para.put("reporter", reporter.toString());
		para.put("environment", environment.toString());
		para.put("staff", staff.toString());
		para.put("user_id", currentUser.getId().toString());
		para.put("activity_id", activity.getId().toString());
		DBUtil.asyncConnect(DBUtil.target_url, para, "POST", new SetRating());
	}
}
