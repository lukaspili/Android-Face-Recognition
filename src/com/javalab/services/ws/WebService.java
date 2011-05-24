package com.javalab.services.ws;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.javalab.activities.HttpService;

public class WebService {

	public static final String HOST = "http://kizebi.net:9000/application/";
	public static final String UPLOAD = HOST + "uploadImg";
	public static final String GET = HOST + "saveImg";

	private Activity activity;

	public WebService(Activity activity) {
		this.activity = activity;
	}

	public void sendPictureToServer(Uri uri) {

		Intent uploadIntent = new Intent();
		uploadIntent.setClassName(HttpService.PACKAGE, HttpService.CLASS);
		uploadIntent.setData(uri);
		activity.startService(uploadIntent);
	}
}
