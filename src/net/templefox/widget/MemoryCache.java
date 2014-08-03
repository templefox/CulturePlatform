package net.templefox.widget;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * �ڴ滺��
 */

public class MemoryCache {

	private final static String TAG = "MemoryCache";
	// ͼƬ���뻺���Ǹ�ͬ������
	// LinkedHashMap���췽�������һ������true�������map���Ԫ�ؽ��������ʹ�ô������ٵ�������
	// �����ĺô������Ҫ�������е�Ԫ���滻�����Ȼ��ڵ������б������������ʹ�õ�Ԫ�������Ч��

	private Map<String, Bitmap> cache = Collections
			.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));

	// ������ͼƬռ�õ��ֽڣ���ʼΪ0����ͨ���˱����ϸ���ƻ�����ռ�õĶ��ڴ�
	private long size = 0; // current allocated size

	// ������ռ�õ������ڴ�
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
		checkSize(); // ÿ����һ����ͼ�ͼ��һ���ڴ�ʹ�����������������ƾ�ɾ
	}

	/**
	 * �ϸ���ƶ��ڴ�ʹ���������������������滻���������ʹ�õ��Ǹ�ͼƬ����
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
	 * ͼƬռ�õ��ڴ�
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