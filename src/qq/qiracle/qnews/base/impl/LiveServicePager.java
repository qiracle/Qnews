package qq.qiracle.qnews.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import qq.qiracle.qnews.base.BasePager;


public class LiveServicePager extends BasePager {

	public LiveServicePager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		System.out.println("初始化....");
		
		tvTitle.setText("生活服务");
		setSlidingMenuEnable(true);// 打开侧边栏

		TextView text = new TextView(mActivity);
		text.setText("生活服务");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);

		// 向FrameLayout中动态添加布局
		flContent.addView(text);
	}

}
