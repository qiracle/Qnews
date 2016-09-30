package qq.qiracle.qnews.utils.bitmap;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class NetCacheUtils {
	LocalCacheUtils mLocalCacheUtils;
	MemoryCacheUtils mMemoryCacheUtils;

	public NetCacheUtils(LocalCacheUtils localCacheUtils,
	MemoryCacheUtils memoryCacheUtils) {
		mLocalCacheUtils = localCacheUtils;
		mMemoryCacheUtils=memoryCacheUtils;
	}

	
	public void getBitmapFromNet(ImageView ivpic, String url) {
		new BitmapTask().execute(ivpic, url);

	}

	/**
	 * Handler和线程池的封装
	 * 
	 * 第一个泛型: 参数类型 第二个泛型: 更新进度的泛型, 第三个泛型是onPostExecute的返回结果
	 * 
	 * 
	 */
	class BitmapTask extends AsyncTask<Object, Void, Bitmap> {

		private ImageView ivPic;
		private String url;

		@Override
		protected Bitmap doInBackground(Object... params) {
			ivPic = (ImageView) params[0];
			url = (String) params[1];
			ivPic.setTag(url);// 将url和imageview绑定
			return downloadBitmap(url);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				String bindUrl = (String) ivPic.getTag();

				if (url.equals(bindUrl)) {// 确保图片设定给了正确的imageview
					ivPic.setImageBitmap(result);
					mLocalCacheUtils.setBitmaptoLocal(url, result);// 将图片保存在本地
					mMemoryCacheUtils.setBitmapToMemory(url, result);// 将图片保存在内存
					
					System.out.println("从网络缓存读取图片啦...");
				}
			}
		}

	}

	public Bitmap downloadBitmap(String url) {
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.setRequestMethod("GET");
			conn.connect();

			int responseCode = conn.getResponseCode();

			if (responseCode == 200) {
				InputStream inputStream = conn.getInputStream();
			

				BitmapFactory.Options option = new BitmapFactory.Options();

				// option.inSampleSize =2;//宽高都压缩为原来的二分之一, 此参数需要根据图片要展示的大小来确定
				option.inPreferredConfig = Bitmap.Config.RGB_565;
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, option);

				return bitmap;

			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// public static int calculateInSampleSize(BitmapFactory.Options options,
	// int reqWidth, int reqHeight) {
	// // 源图片的高度和宽度
	// final int height = options.outHeight;
	// final int width = options.outWidth;
	// int inSampleSize = 1;
	// if (height > reqHeight || width > reqWidth) {
	// // 计算出实际宽高和目标宽高的比率
	// final int heightRatio = Math.round((float) height / (float) reqHeight);
	// final int widthRatio = Math.round((float) width / (float) reqWidth);
	// // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
	// // 一定都会大于等于目标的宽和高。
	// inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	// }
	// return inSampleSize;
	// }
}
