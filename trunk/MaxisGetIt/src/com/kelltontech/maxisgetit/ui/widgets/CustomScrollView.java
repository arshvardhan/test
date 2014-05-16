package com.kelltontech.maxisgetit.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Custom scrollView to suppress horizontal scroll of gallery
 * 
 * @author kapil.vij 10/03/2013
 * 
 */
public class CustomScrollView extends ScrollView {
	private GestureDetector mGestureDetector;
	View.OnTouchListener mGestureListener;
	private float xDistance, yDistance, lastX, lastY;

	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(context, new YScrollDetector());
		setFadingEdgeLength(0);
	}

	// @Override
	// public boolean onInterceptTouchEvent(MotionEvent ev) {
	// return super.onInterceptTouchEvent(ev) &&
	// mGestureDetector.onTouchEvent(ev);
	// }
	//
	//
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			Log.i("VerticalScrollview",
					"onInterceptTouchEvent: DOWN super false");
			xDistance = yDistance = 0f;
			lastX = ev.getX();
			lastY = ev.getY();
			super.onTouchEvent(ev);
			break;

		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();
			xDistance += Math.abs(curX - lastX);
			yDistance += Math.abs(curY - lastY);
			lastX = curX;
			lastY = curY;
			if (xDistance > yDistance)
				return false; // redirect MotionEvents to ourself

		case MotionEvent.ACTION_CANCEL:
			Log.i("VerticalScrollview",
					"onInterceptTouchEvent: CANCEL super false");
			super.onTouchEvent(ev);
			break;

		case MotionEvent.ACTION_UP:
			Log.i("VerticalScrollview", "onInterceptTouchEvent: UP super false");
			return false;

		default:
			Log.i("VerticalScrollview", "onInterceptTouchEvent: " + action);
			break;
		}

		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		super.onTouchEvent(ev);
		Log.i("VerticalScrollview", "onTouchEvent. action: " + ev.getAction());
		return true;
	}
}

// Return false if we're scrolling in the x direction
class YScrollDetector extends SimpleOnGestureListener {
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if (Math.abs(distanceY) > Math.abs(distanceX)) {
			return true;
		}
		return false;
	}

}
