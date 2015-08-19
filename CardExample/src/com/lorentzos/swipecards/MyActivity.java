package com.lorentzos.swipecards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

public class MyActivity extends Activity implements View.OnClickListener {

	private List<Map<String, Object>> data;

	private SwipeFlingAdapterView flingContainer;
	private TextView left;
	private TextView right;
	private SimpleAdapter simpleAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getData();
		setContentView(R.layout.activity_my);
		flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
		left = (TextView) findViewById(R.id.left);
		right = (TextView) findViewById(R.id.right);
		left.setOnClickListener(this);
		right.setOnClickListener(this);

		simpleAdapter = new SimpleAdapter(this, data, R.layout.item,
				new String[] { "img" }, new int[] { R.id.iv_hello });
		flingContainer.setAdapter(simpleAdapter);

		flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
					@Override
					public void removeFirstObjectInAdapter() {
						Log.d("LIST", "removed object!");
						if(data.size()>0){
							data.remove(0);
							simpleAdapter.notifyDataSetChanged();
						}
					}

					@Override
					public void onLeftCardExit(Object dataObject) {
						makeToast(MyActivity.this, "Left!");
					}

					@Override
					public void onRightCardExit(Object dataObject) {
						makeToast(MyActivity.this, "Right!");
					}

					@Override
					public void onAdapterAboutToEmpty(int itemsInAdapter) {

					}

					@SuppressLint("NewApi")
					@Override
					public void onScroll(float scrollProgressPercent) {
						View view = flingContainer.getSelectedView();
						view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent: 0);
						view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent: 0);
					}
				});

			flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
					@Override
					public void onItemClicked(int itemPosition,Object dataObject) {
						makeToast(MyActivity.this, "Clicked!");
					}
				});

	}

	
	private void getData() {
		// map.put(参数名字,参数值)
		data = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("title", "img1");
		map.put("img", R.drawable.img1);
		data.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "img2");
		map.put("img", R.drawable.img2);
		data.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "img3");
		map.put("img", R.drawable.img3);
		data.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "img4");
		map.put("img", R.drawable.img4);
		data.add(map);
	}

	static void makeToast(Context ctx, String s) {
		Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.left:
			left();
			break;

		case R.id.right:
			right();
			break;
		default:
			break;
		}
	}

	public void left() {
		flingContainer.getTopCardListener().selectLeft();
	}

	public void right() {
		flingContainer.getTopCardListener().selectRight();
	}

}
