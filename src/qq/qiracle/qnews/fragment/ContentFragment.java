package qq.qiracle.qnews.fragment;

import java.util.ArrayList;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import qq.qiracle.qnews.R;
import qq.qiracle.qnews.base.BasePager;
import qq.qiracle.qnews.base.impl.LiveServicePager;
import qq.qiracle.qnews.base.impl.HomePager;
import qq.qiracle.qnews.base.impl.NewsCenterPager;
import qq.qiracle.qnews.base.impl.SettingPager;
import qq.qiracle.qnews.base.impl.HappyTimePager;

public class ContentFragment extends Basefragment{
	@ViewInject(R.id.rg_group)
	private RadioGroup rgGroup;

	@ViewInject(R.id.vp_content)
	private ViewPager mViewPager;

	private ArrayList<BasePager> mPagerList;
	@Override
	public View initViews() {
		View view =View.inflate(mActivity, R.layout.fragment_content, null);
		ViewUtils.inject(this, view); // 注入view和事件
		return view;
	}
	@Override
	public void initData() {
		rgGroup.check(R.id.rb_home);
		mPagerList = new ArrayList<BasePager>();
		mPagerList.add(new HomePager(mActivity));
		mPagerList.add(new NewsCenterPager(mActivity));
		mPagerList.add(new HappyTimePager(mActivity));
		mPagerList.add(new LiveServicePager(mActivity));
		mPagerList.add(new SettingPager(mActivity));
		
		mViewPager.setAdapter(new ContentAdapter());
		
		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
				case R.id.rb_home:
					mViewPager.setCurrentItem(0, false);
					break;
				case R.id.rb_news:
					mViewPager.setCurrentItem(1, false);
					break;
				case R.id.rb_smart:
					mViewPager.setCurrentItem(2, false);
					
					break;
				case R.id.rb_gov:
					mViewPager.setCurrentItem(3, false);
					break;
				case R.id.rb_setting:
					mViewPager.setCurrentItem(4, false);
					
					break;
					
				default:
					break;
				}
				
			}
		});
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				mPagerList.get(position).initData();// 获取当前被选中的页面, 初始化该页面数据
				
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
		

		mPagerList.get(0).initData();// 初始化首页数据
	}
	class ContentAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
		
			return view==object;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = mPagerList.get(position);
			container.addView(pager.mRootView);
			// pager.initData();// 初始化数据.... 不要放在此处初始化数据, 否则会预加载下一个页面
			return pager.mRootView;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
	}
	
	/**
	 * 获取新闻中心页面
	 * 
	 * @return
	 */
	public NewsCenterPager getNewsCenterPager() {
		return (NewsCenterPager) mPagerList.get(1);
	}

}
