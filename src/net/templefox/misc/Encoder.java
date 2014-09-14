package net.templefox.misc;

import android.util.Base64;



public abstract class Encoder {
	public static String encodeString(String format,Object...args){
		String f = String.format(format, args);
		byte[] bytes = Base64.encode(Base64.encode(f.getBytes(), Base64.DEFAULT),Base64.DEFAULT);
		return new String(bytes);
	}
	
	public static String encodeString(String format){
		return encodeString(format, new Object[]{});
	}
}
