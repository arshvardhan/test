package com.kelltontech.framework.imageloader;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.kelltontech.framework.imageloader.ImageDownloaderGoogle.FlushedInputStream;

/**
 * @author rupesh.rastogi use this for image scaling if you want to display
 *         image of size 250x250 whereas the actual image dimesion is 2048x2367
 *         the decodeSampled() will return image of required size
 */
public class ScaledDownBitmapLoader {
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		int height_tmp = options.outHeight;
		int width_tmp = options.outWidth;
		int inSampleSize = 1;

		while (true) {
			if (width_tmp / 2 < reqWidth || height_tmp / 2 < reqHeight)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			inSampleSize *= 2;
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static Bitmap decodeSampledByteArray(byte[] res, int offset, int length, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(res, offset, length, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(res, offset, length, options);
	}

	public static Bitmap decodeSampledImageUrl(String imageUrl, int reqWidth, int reqHeight) throws IOException {
		URL url = new URL(imageUrl);
		InputStream inputStream = new FlushedInputStream((InputStream) url.getContent());
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(inputStream, null, options);
		inputStream.close();
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		options.inJustDecodeBounds = false;
		// Decode bitmap with inSampleSize set
		url = new URL(imageUrl);
		inputStream = new FlushedInputStream((InputStream) url.getContent());

		Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
		if (bitmap == null) {
			try {
				HttpGet httpRequest = new HttpGet();
				httpRequest.setURI(new URI(imageUrl));
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
				HttpEntity entity = response.getEntity();
				BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
				bitmap = BitmapFactory.decodeStream(bufHttpEntity.getContent(), null, options);
				httpRequest.abort();
			} catch (Exception e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
			}
		}
		inputStream.close();
		return bitmap;
	}

	public static byte[] getByteArrayFromBitmap(Bitmap bmp) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}

	public static void getBitmapFromFilePath(String fileAbsolutePath, int requiredWidth, int requiredHeight) throws IOException {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		FileInputStream fis = new FileInputStream(fileAbsolutePath);
		BitmapFactory.decodeStream(fis, null, o);
		fis.close();
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < requiredWidth || height_tmp / 2 < requiredHeight)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inSampleSize = scale;
		fis = new FileInputStream(fileAbsolutePath);
		Bitmap bitmap = BitmapFactory.decodeStream(fis, null, op);
		fis.close();
	}
}
