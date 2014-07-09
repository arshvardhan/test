package com.kelltontech.maxisgetit.ui.widgets;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class CustomGallery extends Gallery{
	private Activity mActivity;
	public CustomGallery(Context context) {
		super(context);
	}
	public CustomGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		mActivity=(Activity) context;
	}


//	@Override
//	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {        
//		return false;
//	}
//	@Override
//	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
//			float distanceY) {
////		int kEvent;
////		if(isScrollingLeft(e1, e2)){ //Check if scrolling left
////			kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
////			Log.w("","left");
////		}
////		else{ 
////			//Otherwise scrolling right
////			kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
////			Log.w("","Right");
////		}
////		onKeyDown(kEvent, null);
//
//		return true;  
//		//return super.onScroll(e1, e2, distanceX, distanceY);
//	}
	/*@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {  
		if(isScrollingLeft(e1, e2)){
			return false;
		}
		return super.onFling(e1, e2, velocityX, velocityY);
	}*/
	/*@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
		int kEvent;
		if(isScrollingLeft(e1, e2)){ //Check if scrolling left
			kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
			Log.w("","left");
//			return super.onScroll(e1, e2, distanceX, distanceY);
		}
		
		else{ 
			//Otherwise scrolling right
			kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
			Log.w("","Right");
			return super.onScroll(e1, e2, distanceX, distanceY);
		}
		onKeyDown(kEvent, null);
		if(mActivity instanceof HomeActivity)
		{
			if(kEvent == KeyEvent.KEYCODE_DPAD_LEFT)
				((HomeActivity)mActivity).scrollGalleryLeft();
			else ((HomeActivity)mActivity).scrollGalleryRight();
		}

		return true;  
//		return super.onScroll(e1, e2, distanceX, distanceY);
	}*/
	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2){ 
		return e2.getX() > e1.getX(); 
	}
}