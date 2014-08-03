package net.templefox.widget;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * 内存缓存
 */

public class MemoryCache {

	private final static String TAG = "MemoryCache";
	// 图片放入缓存是个同步操作
	// LinkedHashMap构造方法的最后一个参数true代表这个map里的元素将按照最近使用次数由少到多排列
	// 这样的好处是如果要将缓存中的元素替换，则先会在迭代器中遍历出最近最少使用的元素以提高效率

	private Map<String, Bitmap> cache = Collections
			.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));

	// 缓存中图片占用的字节，初始为0，将通过此变量严格控制缓存所占用的堆内存
	private long size = 0; // current allocated size

	// 缓存能占用的最大堆内存
	private long limit = 1048575; // max memory in bytes

	public MemoryCache() {
		// use 25% of available heap size
		setLimit(Runtime.getRuntime().maxMemory() / 4);
	}

	public void setLimit(long new_limit) {
		limit = new_limit;
		Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");

	}

	public Bitmap get(String id) {
		if (!cache.containsKey(id)) {
			return null;
		}
		return cache.get(id);
	}

	public void put(String id, Bitmap bitmap) {
		if (cache.containsKey(id)) {
			size -= getSizeInBytes(cache.get(id));
			}

		cache.put(id, bitmap);
		size += getSizeInBytes(bitmap);
		checkSize(); // 每放入一个新图就检查一下内存使用情况，如果超了限制就删
	}

	/**
	 * 严格控制堆内存使用情况，如果超过将首先替换掉最近最少使用的那个图片缓存
	 */
	public void checkSize() {

		Log.i(TAG, "cache size = " + size + " length = " + cache.size());

		if (size > limit) {
			Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Bitmap> entry = iter.next();
				size -= getSizeInBytes(entry.getValue());
				iter.remove();

				if (size <= limit) {
					break;
				}

			}
			Log.i(TAG, "Clean cache! New size = " + cache.size());

		}

	}

	/**
	 * 图片占用的内存
	 */

	public long getSizeInBytes(Bitmap bitmap) {
		if (bitmap == null) {
			return 0;
		}

		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	public void clear() {
		cache.clear();
	}

}