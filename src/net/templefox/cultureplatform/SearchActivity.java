package net.templefox.cultureplatform;

import java.util.ArrayList;
import java.util.List;

import net.templefox.database.SQLiteWorker;
import net.templefox.fragment.item.ClassifyItemAdapter;

import net.templefox.cultureplatform.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

//TODO 设置自己的itemAdapter， 更多的搜索方法，搜索中的环形进度条，搜索第一界面的热门关键字(或用户常用关键字)


public class SearchActivity extends Activity {
	private SearchView searchView;
	private ListView listView;
	private ClassifyItemAdapter adapter = new ClassifyItemAdapter();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.ActionBar);
		setContentView(R.layout.activity_search);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		listView = (ListView)findViewById(R.id.list_search);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		searchView.setIconifiedByDefault(false);
		searchView.requestFocus();
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				List<net.templefox.database.data.Activity> activities = new ArrayList<net.templefox.database.data.Activity>();
				List<ContentValues> list = SQLiteWorker.selectFromSQLite("activity", new String[]{"id","name"},
						"content like ?",new String[]{"%"+query+"%"}, SearchActivity.this);
				for(ContentValues contentValue: list){
					net.templefox.database.data.Activity activity = new net.templefox.database.data.Activity();
					activity.setId(contentValue.getAsInteger("id"));
					activity.setName(contentValue.getAsString("name"));
					activities.add(activity);
				}
				adapter.setActivities(activities);
				listView.setAdapter(adapter);
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
