package com.kelltontech.maxisgetit.utils;

import java.io.ByteArrayOutputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;


public class UiUtils {

	public static Bitmap getBitmap( byte[] stream) {
		Bitmap bitmap = null;
		if ( stream != null ) {
			bitmap = BitmapFactory.decodeByteArray(stream, 0, stream.length);
		}
		return bitmap;
	}


	public static Bitmap createReflectedImages(Context context, Bitmap originalImage)
	{

		//		//The gap we want between the reflection and the original image
		//		final int reflectionGap = 4;

		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		//This will not scale but will flip on the Y axis
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		//Create a Bitmap with the flip matrix applied to it.
		//We only want the bottom half of the image
		/////Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height/2, width, height/2, matrix, false);


		//Create a new bitmap with same width but taller to fit reflection
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width
				, (height + height/4), Config.ARGB_8888);

		//Create a new Canvas with the bitmap that's big enough for
		//the image plus gap plus reflection
		Canvas canvas = new Canvas(bitmapWithReflection);
		//Draw in the original image
		canvas.drawBitmap(originalImage, 0, 0, null);
		//Draw in the gap
		Paint deafaultPaint = new Paint();
		deafaultPaint.setColor(Color.BLACK);
		canvas.drawRect(0, height, width, height + bitmapWithReflection.getHeight(), deafaultPaint);
		//Draw in the reflection
		// canvas.drawBitmap(reflectionImage,0, height + reflectionGap, null);
		//Create a shader that is a linear gradient that covers the reflection
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0,
				bitmapWithReflection.getHeight() , 0x70000000, 0x00ffffff,TileMode.CLAMP);
		//Set the paint to use this shader (linear gradient)
		paint.setShader(shader);
		//Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		//Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width,bitmapWithReflection.getHeight(), paint);

		return bitmapWithReflection;
	}


	public static Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight)
	{
		if(null != bitmap)
		{
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// create the new Bitmap object
			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,height, matrix, false);
			return resizedBitmap;
		}
		return null;
	}

	public static String getDensity ( Activity activity ) {
		String retVal = "";
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		switch(metrics.densityDpi){
		case DisplayMetrics.DENSITY_LOW:
			retVal = "ldpi";
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			retVal = "mdpi";
			break;
		case DisplayMetrics.DENSITY_HIGH:
			retVal = "hdpi";
			break;
		}
		return retVal;
	}
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
	public static String getResolution ( Activity activity ) {
		DisplayMetrics _Displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(_Displaymetrics);
		int _ScreenWidth = _Displaymetrics.widthPixels;
		int _ScreenHeight = _Displaymetrics.heightPixels;

		if ( _ScreenWidth <= 240 ) {
			_ScreenWidth = 240;
			_ScreenHeight = 432;
		}
		else if ( _ScreenWidth < 400 ) {
			_ScreenWidth = 320;
			_ScreenHeight = 480;
		}
		else if ( _ScreenWidth > 400 && _ScreenWidth <= 600 ){
			_ScreenWidth = 480;
			_ScreenHeight = 854;
		}
		else{
			_ScreenWidth = 720;
			_ScreenHeight = 1184;
		}
		String res = _ScreenWidth + "*" + _ScreenHeight;


		return  res;
	}

	public static float getResolution ( Context activity ) {
		DisplayMetrics _Displaymetrics = activity.getResources().getDisplayMetrics();
		int _ScreenWidth = _Displaymetrics.widthPixels;
		int _ScreenHeight = _Displaymetrics.heightPixels;
		float res=1;
		if ( _ScreenWidth <= 240 ) {
			/*_ScreenWidth = 240;
			_ScreenHeight = 432;*/
			res=1;
		}
		else if ( _ScreenWidth < 400 ) {
			/*_ScreenWidth = 320;
			_ScreenHeight = 480;
			*/
			res=1;
		}
		else if ( _ScreenWidth > 400 && _ScreenWidth <= 600 ){
			/*_ScreenWidth = 480;
			_ScreenHeight = 854;*/
			res=1.5f;
		}
		else{
			/*_ScreenWidth = 720;
			_ScreenHeight = 1184;*/
			res=2;
		}

		return  res;
	}

	public static void startActivityCleraingOthers(Activity activity, Class<?> cls) {
		Intent intent = new Intent(activity, cls);
		intent .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		activity.startActivity(intent);
		activity.finish();

	}
public static int getDeviceWidth(Activity activity)
{
	DisplayMetrics _Displaymetrics = new DisplayMetrics();
	activity.getWindowManager().getDefaultDisplay().getMetrics(_Displaymetrics);
	return _Displaymetrics.widthPixels;
}
public static float getDeviceDensity(Activity activity)
{
	/*DisplayMetrics _Displaymetrics = new DisplayMetrics();
	activity.getWindowManager().getDefaultDisplay().getMetrics(_Displaymetrics);*/
	return activity.getResources().getDisplayMetrics().density;
}
	public static int convertPixelsToDp(float px,Context context){
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return (int)dp;

	}

	public static int convertDpTOPixels(float dp,Context context){
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();

		float px=dp*(metrics.densityDpi / 160f);
		return (int)px;

	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public static void setListViewHeightBasedOnChildren(ListView listView,float height,int margin) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return ;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = (int) Math.min(totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)),height );
		params.height=params.height+margin;
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public static void sendScrollToBottom(final ScrollView scrollview){
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {Thread.sleep(200);} catch (InterruptedException e) {}
				handler.post(new Runnable() {
					@Override
					public void run() {
						scrollview.fullScroll(View.FOCUS_DOWN);
					}
				});
			}
		}).start();
	}
	
	public static void hideKeyboardOnTappingOutside(View view,final Activity activity) {
	    //Set up touch listener for non-text box views to hide keyboard.
	    if(!(view instanceof EditText)) {
	        view.setOnTouchListener(new OnTouchListener() {
	            public boolean onTouch(View v, MotionEvent event) {
	                hideSoftKeyboard(activity);
	                return false;
	            }
	        });
	    }

	    //If a layout container, iterate over children and seed recursion.
	    if (view instanceof ViewGroup) {
	        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
	            View innerView = ((ViewGroup) view).getChildAt(i);
	            hideKeyboardOnTappingOutside(innerView,activity);
	        }
	    }
	}
	public static void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
	
	/**
	 * THis method will immediately show soft keyboard
	 * @param activity
	 */
	public static void showSoftKeyboard(final Activity activity) {
    	Timer timer = new Timer();
    	timer.schedule(new TimerTask() {
    		@Override
    		public void run() {
    			InputMethodManager m = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    			if(m != null) {
    				m.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
    			}
    		}
    	}, 100);
    }
	
	public static String getBase64Image(Bitmap mBitmap)throws OutOfMemoryError{
		ByteArrayOutputStream full_stream = new ByteArrayOutputStream();
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, full_stream);
		try{
			byte[] full_bytes = full_stream.toByteArray();
			String img_full = Base64.encodeToString(full_bytes, Base64.DEFAULT);
			return img_full;
		}catch(OutOfMemoryError err){
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, full_stream);
			byte[] full_bytes = full_stream.toByteArray();
			String img_full = Base64.encodeToString(full_bytes, Base64.DEFAULT);
			return img_full;
		}
	}
}
