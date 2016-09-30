package qq.qiracle.qnews;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import qq.qiracle.qnews.utils.PrefUtils;

public class SplashActivity extends Activity {
	RelativeLayout rlRoot;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		 rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
	startAnim();
	
	
	}
	private void startAnim() {
		AnimationSet set = new AnimationSet(false);
		//旋转
		RotateAnimation rotate = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		rotate.setDuration(1000);
		rotate.setFillAfter(true);
		
		ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scale.setDuration(1000);
		scale.setFillAfter(true);
		
	
		AlphaAnimation alpha = new AlphaAnimation(0, 1);
		alpha.setDuration(2000);
		alpha.setFillAfter(true);
		
		set.addAnimation(rotate);
		set.addAnimation(scale);
		set.addAnimation(alpha);
		
		set.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				jumpNextPage();
				
			}

			
		});
		rlRoot.startAnimation(set);
		
		
	}
	private void jumpNextPage() {

		
		boolean userGuide = PrefUtils.getBoolean(this,"is_user_guide_showed",
				false);
		
		if (!userGuide) {
			// 跳转到新手引导页
			startActivity(new Intent(SplashActivity.this, GuideActivity.class));
		} else {
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
		}

		finish();
		
	}

}
