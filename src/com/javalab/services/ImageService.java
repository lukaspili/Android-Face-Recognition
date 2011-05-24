package com.javalab.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageService {

	private static final String TAG = "Image Service";

	private Context context;
	private WindowManager windowManager;

	public ImageService(Context context, WindowManager windowManager) {
		this.context = context;
		this.windowManager = windowManager;
	}

	public ImageView getSmallImageView(Drawable drawable) {

		Integer width = windowManager.getDefaultDisplay().getWidth() / 3 - 2;

		int orgWidth = drawable.getIntrinsicWidth();
		int orgHeight = drawable.getIntrinsicHeight();
		int height = (int) Math.floor((orgHeight * width) / orgWidth);

		ImageView image = new ImageView(context);
		image.setImageDrawable(drawable);
		image.setAdjustViewBounds(true);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
		params.setMargins(1, 0, 1, 0);
		image.setLayoutParams(params);
		image.setScaleType(ImageView.ScaleType.CENTER_CROP);

		return image;
	}

	public ImageView getSmallPictureFromLocal(Uri uri) {

		Drawable drawable = getDrawableFromLocal(uri);
		return getSmallImageView(drawable);
	}

	public ImageView getSmallPictureFromRemote(Uri uri) {

		Integer width = windowManager.getDefaultDisplay().getWidth() / 3 - 2;
		Drawable drawable = null;

		try {
			drawable = getPictureFromRemote(uri, width);
		} catch (IOException e) {
			Log.e(TAG, "Cannot get picture from uri : " + uri);
		}

		if (null == drawable) {
			return null;
		}

		return getSmallImageView(drawable);
	}

	public Drawable getDrawableFromLocal(Uri uri) {

		Drawable drawable = null;

		try {
			Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
			drawable = new BitmapDrawable(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return drawable;
	}

	public Drawable getPictureFromRemote(Uri uri, Integer width) throws IOException {

		Bitmap b = null;
		InputStream is = null;

		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;

		URL url = null;

		try {
			url = new URL(uri.toString());
		} catch (MalformedURLException e) {
			Log.e(TAG, "Image url error : " + uri);
		}

		try {
			is = (InputStream) url.getContent();
			BitmapFactory.decodeStream(is, null, o);
		} finally {
			if (null != is) {
				is.close();
			}
		}

		int newHeight = (int) Math.floor((o.outHeight * width) / o.outWidth);

		int scale = 1;
		if (o.outHeight > newHeight || o.outWidth > newHeight) {
			scale = (int) Math.pow(2,
					(int) Math.round(Math.log(newHeight / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
		}

		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;

		try {
			is = (InputStream) url.getContent();
			b = BitmapFactory.decodeStream(is, null, o2);

		} finally {
			if (null != is) {
				is.close();
			}
		}

		return new BitmapDrawable(b);
	}
}
