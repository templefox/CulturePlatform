package net.templefox.fragment;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

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
import net.templefox.widget.staggeredGrid.StaggeredGridView;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

@EFragment(R.layout.frag_test)
public class TestFragment extends Fragment {
	@ViewById(R.id.staggeredGridView1)
	StaggeredGridView gridView;
	
	@ViewById(android.R.id.empty)
	View empty;
	
	@Bean
	ClassifyItemAdapter adapter;
	
	Ad ad = new Ad();
	int a;
	public void setA(int a) {
		this.a =a;
		
	}
	
	@AfterInject
	protected void loadDate(){
	}
	
	@AfterViews
	protected void AfterViews(){	
		gridView.setEmptyView(empty);
		gridView.setAdapter(ad);
		ad.notifyDataSetChanged();
	}


	@Override
	public String toString(){
		return "test";
	}

	class Ad extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 20;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		Random random = new Random();
		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (position%2==0) {
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_recommend, parent, false);				
			}else {
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_calendar, parent, false);
			}
			return convertView;
		}}
}
