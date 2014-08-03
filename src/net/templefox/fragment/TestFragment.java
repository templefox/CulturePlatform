package net.templefox.fragment;

import org.androidannotations.annotations.EFragment;

import net.templefox.cultureplatform.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@EFragment(R.layout.frag_test)
public class TestFragment extends Fragment {
	int a;
	public void setA(int a) {
		this.a =a;
		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag_test, container,false);
		TextView textView = (TextView) view.findViewById(R.id.item_comment_namew);
		textView.setText("The "+a);
		return view;
	}


	@Override
	public String toString(){
		return "test";
	}

	
}
