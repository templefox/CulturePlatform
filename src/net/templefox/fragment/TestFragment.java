package net.templefox.fragment;

import java.util.Arrays;
import java.util.LinkedList;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import net.templefox.cultureplatform.R;
import net.templefox.database.FakeLoadDataListener;
import net.templefox.database.LoadDataListener;
import net.templefox.fragment.item.ClassifyItemAdapter;
import net.templefox.widget.pullrefresh.PullToRefreshBase;
import net.templefox.widget.pullrefresh.PullToRefreshBase.OnRefreshListener2;
import net.templefox.widget.pullrefresh.PullToRefreshListView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

@EFragment(R.layout.frag_test)
public class TestFragment extends Fragment {
	@ViewById(R.id.ptr)
	PullToRefreshListView listView;
	
	@Bean
	ClassifyItemAdapter adapter;
	
	int a;
	public void setA(int a) {
		this.a =a;
		
	}
	
	@AfterInject
	protected void loadDate(){
		//adapter.loadRemoteDate(FakeLoadDataListener.getFake());
		//adapter.loadLocalData(FakeLoadDataListener.getFake());
	}
	
	@AfterViews
	protected void AfterViews(){	
		listView.setAdapter(adapter);	
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(final PullToRefreshBase refreshView) {
				/*adapter.loadRemoteDate(false,new LoadDataListener() {
					
					@Override
					public void onDone() {
						adapter.notifyDataSetChanged();
						refreshView.onRefreshComplete();
					}
				});*/
			}
			
			@Override
			public void onPullUpToRefresh(final PullToRefreshBase refreshView) {
				/*adapter.loadRemoteDate(true,new LoadDataListener() {
					
					@Override
					public void onDone() {
						adapter.notifyDataSetChanged();
						refreshView.onRefreshComplete();
					}
				});*/
			}
		});
	}


	@Override
	public String toString(){
		return "test";
	}

	
}
