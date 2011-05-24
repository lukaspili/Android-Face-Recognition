package com.javalab.activities;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.javalab.services.ws.WebService;

public class HttpService extends Service {

	public static final String PACKAGE = "com.javalab.activities";
	public static final String CLASS = PACKAGE + ".HttpService";

	private static final String TAG = "Http Service";

	private Intent mInvokeIntent;
	private volatile Looper mUploadLooper;
	private volatile ServiceHandler mUploadHandler;

	private volatile String result = "UNDEFINED";

	private final class ServiceHandler extends Handler {

		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message message) {

			Log.i(TAG, "Handle new message");
			Uri image = (Uri) message.obj;

			Log.i(TAG, "Image to upload : " + image);

			doHttpUpload(image);

			Log.i(getClass().getSimpleName(), "Message: " + message);
			Log.i(getClass().getSimpleName(), "Done with #" + message.arg1);
			stopSelf(message.arg1);
		}
	};

	public void onCreate() {
		Log.i(TAG, "Service creation");

		mInvokeIntent = new Intent();
		mInvokeIntent.setClassName(PACKAGE, CLASS);

		HandlerThread thread = new HandlerThread("HttpUploader");
		thread.start();

		mUploadLooper = thread.getLooper();
		mUploadHandler = new ServiceHandler(mUploadLooper);
	}

	public void onStart(Intent uploadintent, int startId) {

		Message msg = mUploadHandler.obtainMessage();
		msg.arg1 = startId;

		msg.obj = uploadintent.getData();
		mUploadHandler.sendMessage(msg);
		Log.d(getClass().getSimpleName(), "Sending: " + msg);

	}

	public void doHttpUpload(Uri myImage) {

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		String photofile = null;
		String httpResponse;
		String filename = null;

		String urlString = WebService.UPLOAD;
		HttpURLConnection conn = null;

		InputStream fis = null;
		Bitmap mBitmap = null;
		String pathfile;

		if (myImage != null) {

			filename = myImage.toString();

			try {
				fis = getContentResolver().openInputStream(myImage);
				mBitmap = BitmapFactory.decodeStream(fis);

				try {
					int bytesAvailable = fis.available();
				} catch (IOException e) {
					e.printStackTrace();
					Log.i(getClass().getSimpleName(), "échec de lecture de la photo");
					stopSelf();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				Toast.makeText(HttpService.this, "échec de lecture de la photo ", Toast.LENGTH_SHORT).show();
				Log.i(getClass().getSimpleName(), "échec de lecture de la photo");
				stopSelf();
			}

		} else
			Log.i(getClass().getSimpleName(), "myImage is null");

		try {

			URL site = new URL(urlString);
			conn = (HttpURLConnection) site.openConnection();

			conn.setDoOutput(true);
			conn.setDoInput(true);

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			Log.i(getClass().getSimpleName(), "Display name : " + photofile);
			Log.i(getClass().getSimpleName(), "Filename : " + filename);
			dos.writeBytes("Content-Disposition: form-data; name=\"picture\";filename=\"" + filename + "\"" + lineEnd);
			dos.writeBytes(lineEnd);

			Log.i(getClass().getSimpleName(), "Headers are written");

			mBitmap.compress(CompressFormat.JPEG, 75, dos);

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			fis.close();
			dos.flush();
			dos.close();
			Log.e("fileUpload", "File is written on the queue");

		} catch (MalformedURLException e) {
			e.printStackTrace();
			Toast.makeText(HttpService.this, "échec de connexion au site web ", Toast.LENGTH_SHORT).show();
			Log.i(getClass().getSimpleName(), "échec de connexion au site web 1");
		} catch (IOException e) {
			e.printStackTrace();

			Toast.makeText(HttpService.this, "échec de connexion au site web ", Toast.LENGTH_SHORT).show();

			Log.i(getClass().getSimpleName(), "échec de connexion au site web 2");
		}

		try {
			BufferedReader rdOk = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			Log.i(getClass().getSimpleName(), "HTTP reponse OK");
			while ((httpResponse = rdOk.readLine()) != null) {

				result = httpResponse;

				Log.i(getClass().getSimpleName(), "HTTP reponse= " + httpResponse);

			}
			rdOk.close();

			System.out.println(result);

		} catch (IOException ioex) {
			Log.e("HttpUploader", "error: " + ioex.getMessage(), ioex);
			ioex.printStackTrace();
			Toast.makeText(HttpService.this, "échec de lecture de la réponse du site web ", Toast.LENGTH_SHORT).show();

			Log.i(getClass().getSimpleName(), "échec de lecture de la réponse du site web");
		}
	}

	public void onDestroy() {
		mUploadLooper.quit();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}