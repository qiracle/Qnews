package qq.qiracle.qnews.base;

import qq.qiracle.qnews.R;
import qq.qiracle.qnews.domain.NewsData.NewsTabData;
import qq.qiracle.qnews.domain.TabData;
import qq.qiracle.qnews.domain.TabData.TabNewsData;
import qq.qiracle.qnews.domain.TabData.TopNewsData;
import qq.qiracle.qnews.global.GlobalContants;
import qq.qiracle.qnews.view.RefreshListView;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.Activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 页签详情页
 * 
 * @author Kevin
 * 
 */
public class TabDetailPager extends BaseMenuDetailPager implements
OnPageChangeListener{

	NewsTabData mTabData;
	private TabData mTabDetailData;
	private String murl;
	

	@ViewInject(R.id.vp_news)
	private ViewPager mViewPager;

	@ViewInject(R.id.tv_title)
	private TextView tvTitle;// 头条新闻的标题
	private ArrayList<TopNewsData> mTopNewsList;// 头条新闻数据集合

	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator;// 头条新闻位置指示器

	@ViewInject(R.id.lv_list)
	private RefreshListView lvList;// 新闻列表
	private ArrayList<TabNewsData> mNewsList; // 新闻数据集合
	private NewsAdapter mNewsAdapter;

	public TabDetailPager(Activity activity, NewsTabData newsTabData) {
		super(activity);
		mTabData = newsTabData;
		murl = GlobalContants.SERVER_URL + mTabData.url;

	}

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
		// 加载头布局
		View headerView = View.inflate(mActivity, R.layout.list_header_topnews,
				null);

		ViewUtils.inject(this, view);
		ViewUtils.inject(this, headerView);

		// 将头条新闻以头布局的形式加给listview
		lvList.addHeaderView(headerView);
		return view;
	}

	@Override
	public void initData() {
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, murl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = (String) responseInfo.result;
				System.out.println("页签详情页返回结果:" + result);

				parseData(result);
				
			}

			

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace(); 
				
			}
		});
		
	}

	protected void parseData(String result) {
		Gson gson = new Gson();
		mTabDetailData = gson.fromJson(result, TabData.class);
		System.out.println("页签详情解析:" + mTabDetailData);
		mTopNewsList = mTabDetailData.data.topnews;

		mNewsList = mTabDetailData.data.news;

		if (mTopNewsList != null) {
			mViewPager.setAdapter(new TopNewsAdapter());
			mIndicator.setViewPager(mViewPager);
			mIndicator.setSnap(true);// 支持快照显示
			mIndicator.setOnPageChangeListener(this);

			mIndicator.onPageSelected(0);// 让指示器重新定位到第一个点

			tvTitle.setText(mTopNewsList.get(0).title);
		}

		if (mNewsList != null) {
			mNewsAdapter = new NewsAdapter();
			lvList.setAdapter(mNewsAdapter);
		}
		
	}
	
	/**
	 * 头条新闻适配器
	 * 
	 * @author Kevin
	 * 
	 */
	class TopNewsAdapter extends PagerAdapter {

		private BitmapUtils utils;

		public TopNewsAdapter() {
			utils = new BitmapUtils(mActivity);
			utils.configDefaultLoadingImage(R.drawable.topnews_item_default);// 设置默认图片
		}

		@Override
		public int getCount() {
			return mTabDetailData.data.topnews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView image = new ImageView(mActivity);
		//	image.setImageResource(R.drawable.topnews_item_default);
			image.setScaleType(ScaleType.FIT_XY);// 基于控件大小填充图片

	TopNewsData topNewsData = mTopNewsList.get(position);
			//TopNewsData topNewsData = mTabDetailData.data.topnews.get(position);
		utils.display(image, topNewsData.topimage);// 传递imagView对象和图片地址
	

			container.addView(image);
			
			//System.out.println("instantiateItem....." + position);
			return image;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	
	/**
	 * 新闻列表的适配器
	 * 
	 * @author Kevin
	 * 
	 */
	class NewsAdapter extends BaseAdapter {

		private BitmapUtils utils;

		public NewsAdapter() {
			utils = new BitmapUtils(mActivity);
			utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}

		@Override
		public int getCount() {
			return mNewsList.size();
		}

		@Override
		public TabNewsData getItem(int position) {
			return mNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_news_item,
						null);
				holder = new ViewHolder();
				holder.ivPic = (ImageView) convertView
						.findViewById(R.id.iv_pic);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.tv_date);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			TabNewsData item = getItem(position);

			holder.tvTitle.setText(item.title);
			holder.tvDate.setText(item.pubdate);

			utils.display(holder.ivPic, item.listimage);

			return convertView;
		}

	}

	static class ViewHolder {
		public TextView tvTitle;
		public TextView tvDate;
		public ImageView ivPic;
	}
	
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		TopNewsData topNewsData = mTopNewsList.get(position);
		tvTitle.setText(topNewsData.title);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub
		
	}

}
