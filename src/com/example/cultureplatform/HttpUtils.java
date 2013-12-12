package com.example.cultureplatform;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpUtils {

	private final static String URL_PATH = "http://templefox.xicp.net:998/pic/act/tj.png";
	public HttpUtils() {

	}

	public static InputStream getImageViewInputStream() throws IOException {
		InputStream inputStream = null;

		URL url = new URL(URL_PATH);
		
		if (url != null) {
			
			HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url
					.openConnection();
			httpsURLConnection.setConnectTimeout(3000);
			httpsURLConnection.setRequestMethod("GET");
			httpsURLConnection.setDoInput(true);
			int response_code = httpsURLConnection.getResponseCode();
			System.out.println("response code is "+ response_code);
			if (response_code == 200) {
				inputStream = httpsURLConnection.getInputStream();
				
			}

		}

		return inputStream;
	}

}
