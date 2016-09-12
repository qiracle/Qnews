package qq.qiracle.qnews.utils.bitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import qq.qiracle.qnews.utils.MD5Encoder;

public class LocalCacheUtils {
	public static final String CACHE_PATH = "/data/data/qq.qiracle.qnews/qnews_cache";

	public Bitmap getBitmapFromLocal(String url) {
		try {
			String filename = MD5Encoder.encode(url);
			File file = new File(CACHE_PATH, filename);

			if (file.exists()) {
				Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
				return bitmap;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void setBitmaptoLocal(String url, Bitmap bitmap) {
		try {
			String fileName = MD5Encoder.encode(url);

			File file = new File(CACHE_PATH, fileName);

			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {// 如果文件夹不存在, 创建文件夹
				parentFile.mkdirs();
			}

			// 将图片保存在本地
			bitmap.compress(CompressFormat.JPEG, 100,
					new FileOutputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}