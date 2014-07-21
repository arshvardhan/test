package com.kelltontech.maxisgetit.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

public class BitmapCalculation {
	private static final String LOG_TAG = "BitmapCalculation";

	public static Bitmap decodeSampledBitmapFromPath(String res,int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    options.inPurgeable = true;
	    BitmapFactory.decodeFile(res, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	   
	    return  BitmapFactory.decodeFile(res, options);
	}
	
	public static int getMaxSize(Context context) {
		int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
		int maxSize = Math.max((screenWidth - 75), ((int) (screenWidth * 0.7f)));
		return maxSize;
	}
	
	public static Bitmap getScaledBitmap(Bitmap pSourceBmp, int pNewWidth, int pNewHeight, boolean pIsUniform) {
		if (pSourceBmp == null || pNewWidth <= 0 || pNewHeight <= 0) {
			return null;
		}
		int actualWidth = pSourceBmp.getWidth();
		int actualHeight = pSourceBmp.getHeight();

		/**
		 * Check if scaling is not required.
		 */
		if (actualWidth == pNewWidth && actualHeight == pNewHeight) {
			return pSourceBmp;
		}

		int scalingToWidth, scalingToHeight;

		/**
		 * If uniform scaling is required, then scaled Bitmap might not be of
		 * pNewWidth and pNewHeight. Any one dimension will be as per respective
		 * new dimension and other will be >= respective new dimension
		 */
		if (pIsUniform) {
			// If scaling as per pNewWidth will be enough
			scalingToWidth = pNewWidth;
			scalingToHeight = actualHeight * scalingToWidth / actualWidth;
			
			// If scaling as per pNewHeight will be enough
			if (scalingToHeight < pNewHeight) {
				scalingToHeight = pNewHeight;
				scalingToWidth = actualWidth * scalingToHeight / actualHeight;
			}
		} else {
			scalingToWidth = pNewWidth;
			scalingToHeight = pNewHeight;
		}

		try {
			return Bitmap.createScaledBitmap(pSourceBmp, scalingToWidth, scalingToHeight, true);
		} catch (Throwable tr) {
			Log.e(LOG_TAG, "Error in scaling upto required width and/or height", tr);
			return null;
		}
	}
	
	public static Bitmap getScaledBitmap(File f, int maxImageSize) {
		System.gc();

		FileInputStream fstream = null;
		FileDescriptor fd = null;
		try {
			fstream = new FileInputStream(f);
			fd = fstream.getFD();
			if (fd != null) {
				return decodeSampledBitmapFromDescriptor(fd, maxImageSize);
			} else {
				return decodeSampledBitmapFromStream(fstream, maxImageSize);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fstream != null) {
				try {
					fstream.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
	
	/**
	 * Decode and sample down a bitmap from a file input stream to the requested width and height.
	 * 
	 * @param fileDescriptor
	 *            The file descriptor to read from
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @param cache
	 *            The ImageCache used to find candidate bitmaps for use with inBitmap
	 * @return A bitmap sampled down from the original with the same aspect ratio and dimensions that are equal to or
	 *         greater than the requested width and height
	 */
	public static Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fileDescriptor, int maxFileSize) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inDither = false; //Disable Dithering mode
		options.inPurgeable = true; //Tell to gc that whether it needs free memory, the Bitmap can be cleared
		options.inInputShareable = true; //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
		options.inTempStorage = new byte[32 * 1024];
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, maxFileSize);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		// If we're running on Honeycomb or newer, try to use inBitmap
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			addInBitmapOptions(options);
		}

		return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int maxImageSize) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		inSampleSize = (int) Math.pow(2.0,
				(int) Math.round(Math.log(maxImageSize / (double) Math.max(height, width)) / Math.log(0.5)));

		return inSampleSize;
	}

	/**
	 * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding bitmaps using the
	 * decode* methods from {@link BitmapFactory}. This implementation calculates the closest inSampleSize that is a
	 * power of 2 and will result in the final decoded bitmap having a width and height equal to or larger than the
	 * requested width and height.
	 * 
	 * @param options
	 *            An options object with out* params already populated (run through a decode* method with
	 *            inJustDecodeBounds==true
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @return The value to be used for inSampleSize
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}

			// This offers some additional logic in case the image has a strange
			// aspect ratio. For example, a panorama may have a much larger
			// width than height. In these cases the total pixels might still
			// end up being too large to fit comfortably in memory, so we should
			// be more aggressive with sample down the image (=larger inSampleSize).
			long totalPixels = halfHeight * halfWidth / inSampleSize;

			// Anything more than 2x the requested pixels we'll sample down further
			final long totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels > totalReqPixelsCap) {
				inSampleSize *= 2;
				totalPixels /= 2;
			}
		}
		return inSampleSize;
	}
	
	public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] arr=baos.toByteArray();
        String result=Base64.encodeToString(arr, Base64.DEFAULT);
        return result;
  }
	
	/**
	 * Decode and sample down a bitmap from a file to the requested width and height.
	 * 
	 * @param filename
	 *            The full path of the file to decode
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @param cache
	 *            The ImageCache used to find candidate bitmaps for use with inBitmap
	 * @return A bitmap sampled down from the original with the same aspect ratio and dimensions that are equal to or
	 *         greater than the requested width and height
	 */
	public static Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// If we're running on Honeycomb or newer, try to use inBitmap
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			addInBitmapOptions(options);
		}

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filename, options);
	}

	/**
	 * Decode and sample down a bitmap from a file to the requested width and height.
	 * 
	 * @param filename
	 *            The full path of the file to decode
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @param cache
	 *            The ImageCache used to find candidate bitmaps for use with inBitmap
	 * @return A bitmap sampled down from the original with the same aspect ratio and dimensions that are equal to or
	 *         greater than the requested width and height
	 */
	public static Bitmap decodeSampledBitmapFromStream(InputStream stream, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inDither = false; //Disable Dithering mode
		options.inPurgeable = true; //Tell to gc that whether it needs free memory, the Bitmap can be cleared
		options.inInputShareable = true; //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
		options.inTempStorage = new byte[32 * 1024];
		BitmapFactory.decodeStream(stream, null, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// If we're running on Honeycomb or newer, try to use inBitmap
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			addInBitmapOptions(options);
		}

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(stream, null, options);
	}

	/**
	 * Decode and sample down a bitmap from a file to the requested width and height.
	 * 
	 * @param filename
	 *            The full path of the file to decode
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @param cache
	 *            The ImageCache used to find candidate bitmaps for use with inBitmap
	 * @return A bitmap sampled down from the original with the same aspect ratio and dimensions that are equal to or
	 *         greater than the requested width and height
	 */
	public static Bitmap decodeSampledBitmapFromStream(InputStream stream, int maxImageSize) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inDither = false; //Disable Dithering mode
		options.inPurgeable = true; //Tell to gc that whether it needs free memory, the Bitmap can be cleared
		options.inInputShareable = true; //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
		options.inTempStorage = new byte[32 * 1024];
		BitmapFactory.decodeStream(stream, null, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, maxImageSize);

		// If we're running on Honeycomb or newer, try to use inBitmap
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			addInBitmapOptions(options);
		}

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(stream, null, options);
	}


	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private static void addInBitmapOptions(BitmapFactory.Options options) {
		// inBitmap only works with mutable bitmaps so force the decoder to
		// return mutable bitmaps.
		options.inMutable = true;
	}
}
