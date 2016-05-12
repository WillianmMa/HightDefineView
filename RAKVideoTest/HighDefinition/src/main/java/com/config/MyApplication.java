package com.config;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by t00284576 on 2015/10/27.
 */
public class MyApplication extends Application {
	private List<Activity> mActivities = new ArrayList<Activity>();

	private static MyApplication sInstance = null;

	public List<Activity> getActivityList() {
		return mActivities;
	}

	public MyApplication() {
		sInstance = this;
	}

	public static MyApplication getInstance() {
		return sInstance;
	}

	public static Context getContext() {
		return getInstance().getApplicationContext();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// 加载加密数据库的库
		//SQLiteDatabase.loadLibs(this);
		// 初始化Preference
		//PreferenceUtils.init(this);
		initImageLoader();
	}

	public void initImageLoader() {
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
				.threadPriority(Thread.NORM_PRIORITY - 2).threadPoolSize(3)
				// 线程池内加载的数量
				.tasksProcessingOrder(QueueProcessingType.LIFO).denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				// .writeDebugLogs() // Remove for release app
				// 50Mb sd卡(本地)缓存的最大值
				.memoryCache(new LruMemoryCache(cacheSize)).diskCacheSize(50 * 1024 * 1024).build();
		ImageLoader.getInstance().init(config);
	}

	public void addActivity(Activity activity) {
		if (!mActivities.contains(activity)) {
			mActivities.add(activity);
		}
	}

	public void exitAllActicity() {
		if (mActivities != null && mActivities.size() > 0) {
			Log.i("info", "activitys.size=" + mActivities.size());
			for (Activity activity : mActivities) {
				if (activity != null) {
					activity.finish();
				}
			}
			mActivities.clear();
		}
	}
}
