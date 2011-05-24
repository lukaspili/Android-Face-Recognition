package com.javalab.services;

import java.io.File;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.javalab.activities.RecognitionResultActivity;
import com.javalab.models.Picture;
import com.javalab.services.ws.WebService;

public class CameraService {

	public static final int CAMERA_ACTIVITY_ID = 2;
	public static final int RECOGNITION_RESULT_ACTIVITY_ID = 3;

	private Activity activity;
	private Uri picture;

	public CameraService(Activity activity) {
		this.activity = activity;
	}

	public void startCamera() {

		String path = Environment.getExternalStorageDirectory() + File.separator + new Date().getTime() + ".jpg";
		File file = new File(path);
		picture = Uri.fromFile(file);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, picture);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		activity.startActivityForResult(intent, CAMERA_ACTIVITY_ID);
	}

	public void startCameraAsLocal(int resource) {

		picture = Uri.parse("android.resource://com.javalab.activities/" + resource);
		getCameraResult();
	}

	public void getCameraResult() {

		Picture pic = new Picture();
		pic.setUri(picture.toString());
		pic.setDate(new Date());

		Intent recognitionResult = new Intent(activity, RecognitionResultActivity.class);
		recognitionResult.putExtra("picture", pic);

		activity.startActivityForResult(recognitionResult, RECOGNITION_RESULT_ACTIVITY_ID);
	}

	public void getCameraResultWithSending() {
		
		WebService webService = new WebService(activity);
		webService.sendPictureToServer(picture);

		getCameraResult();
	}
}
