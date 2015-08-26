package com.lorentzos.swipecards;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MyActivity extends Activity implements View.OnClickListener {

	private List<String> data;

	private SwipeFlingAdapterView flingContainer;
	private TextView left;
	private TextView right;

	 private MyAdapter myAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		initImageLoader();
		getData();
		initView();
		myAdapter = new MyAdapter();
		flingContainer.setAdapter(myAdapter);
		flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
					@Override
					public void removeFirstObjectInAdapter() {
						Log.d("LIST", "removed object!");
						if (data.size() > 0) {
							data.remove(0);
							myAdapter.notifyDataSetChanged();
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
					public void onItemClicked(int itemPosition,
							Object dataObject) {
						makeToast(MyActivity.this, "Clicked!");
					}
				});

	}

	private void initView() {
		flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
		left = (TextView) findViewById(R.id.left);
		right = (TextView) findViewById(R.id.right);
		left.setOnClickListener(this);
		right.setOnClickListener(this);
	}

	private void getData() {
		data = new ArrayList<String>();
		
		data.add("http://a.hiphotos.baidu.com/baike/c0%3Dbaike92%2C5%2C5%2C92%2C30/sign=8172ae52d31373f0e13267cdc566209e/d52a2834349b033b0b84caf317ce36d3d539bd8e.jpg");
		data.add("http://a.hiphotos.baidu.com/baike/c0%3Dbaike92%2C5%2C5%2C92%2C30/sign=8172ae52d31373f0e13267cdc566209e/d52a2834349b033b0b84caf317ce36d3d539bd8e.jpg");
		data.add("http://a.hiphotos.baidu.com/baike/c0%3Dbaike92%2C5%2C5%2C92%2C30/sign=8172ae52d31373f0e13267cdc566209e/d52a2834349b033b0b84caf317ce36d3d539bd8e.jpg");
		data.add("http://a.hiphotos.baidu.com/baike/c0%3Dbaike92%2C5%2C5%2C92%2C30/sign=8172ae52d31373f0e13267cdc566209e/d52a2834349b033b0b84caf317ce36d3d539bd8e.jpg");
	}

	private void initImageLoader() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
				// 如Bitmap.Config.ARGB_8888
				.showImageOnLoading(R.drawable.ic_launcher) // 默认图片
				.showImageForEmptyUri(R.drawable.ic_launcher) // url爲空會显示该图片，自己放在drawable里面的
				.showImageOnFail(R.drawable.ic_launcher)// 加载失败显示的图片
				.displayer(new RoundedBitmapDisplayer(5)) // 圆角，不需要请删除
				.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).memoryCacheExtraOptions(480, 800)
				// 缓存在内存的图片的宽和高度
				.discCacheExtraOptions(480, 800, CompressFormat.PNG, 70, null)
				// CompressFormat.PNG类型，70质量（0-100）
				.memoryCache(new WeakMemoryCache())
				.memoryCacheSize(2 * 1024 * 1024) // 缓存到内存的最大数据
				.discCacheSize(50 * 1024 * 1024). // 缓存到文件的最大数据
				discCacheFileCount(1000) // 文件数量
				.defaultDisplayImageOptions(options). // 上面的options对象，一些属性配置
				build();
		ImageLoader.getInstance().init(config); // 初始化
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

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//(int resource, ViewGroup root, boolean attachToRoot) 
			View view  = getLayoutInflater().inflate(R.layout.item, parent,false);
			ImageView imageView = (ImageView) view.findViewById(R.id.iv_hello);
			ImageLoader.getInstance().displayImage(data.get(position), imageView);
			return view;
		}
	}

}
