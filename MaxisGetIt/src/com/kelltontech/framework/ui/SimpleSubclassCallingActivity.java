package com.kelltontech.framework.ui;

import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;

import com.kelltontech.framework.ui.utils.AnimUtils;

/**
 * SimpleActivity is a subclass of Activity that makes it trivial to create a
 * sub-Activity, and handle it's results (ok or cancel). No need to deal with
 * requestCodes, since this class handles creating correlationIds automatically.
 * 
 */
public class SimpleSubclassCallingActivity extends Activity {

	/** holds the map of callbacks */
	protected HashMap<Integer, ResultCallbackIF> _callbackMap = new HashMap<Integer, ResultCallbackIF>();

	/**
	 * use this method to launch the sub-Activity, and provide a functor to
	 * handle the result - ok or cancel
	 */
	public void launchSubActivity(Class<? extends Activity> subActivityClass, ResultCallbackIF callback) {

		Intent i = new Intent(this, subActivityClass);

		Random rand = new Random();
		int correlationId = rand.nextInt();

		_callbackMap.put(correlationId, callback);

		startActivityForResult(i, correlationId);

	}

	/**
	 * this is the underlying implementation of the onActivityResult method that
	 * handles auto generation of correlationIds and adding/removing callback
	 * functors to handle the result
	 */
	@Override
	protected void onActivityResult(int correlationId, int resultCode, Intent data) {

		try {
			ResultCallbackIF callback = _callbackMap.get(correlationId);

			switch (resultCode) {
			case Activity.RESULT_CANCELED:
				Log.i("Info", this.getClass().getSimpleName() + " - Activity returned result [CANCEL]:" + data.toString());
				callback.resultCancel(data);
				_callbackMap.remove(correlationId);
				break;
			case Activity.RESULT_OK:
				Log.i("Info", this.getClass().getSimpleName() + " - Activity returned result [OK]:" + data.toString());
				callback.resultOk(data);
				_callbackMap.remove(correlationId);
				break;
			default:
				Log.e("Info", "Couldn't find callback handler for correlationId");
			}
		} catch (Exception e) {
			Log.e("Info", "Problem processing result from sub-activity", e);
		}

	}

	/**
	 * ResultCallbackIF is a simple interface that you have to implement to
	 * handle results - ok or cancel from a sub-Activity.
	 * 
	 * @version 1.0
	 * @since Jul 3, 2008, 12:11:31 PM
	 */
	public static interface ResultCallbackIF {

		public void resultOk(Intent data);

		public void resultCancel(Intent data);

	}// end interface ResultCallbackIF

	@Override
	public boolean onKeyDown(int i, KeyEvent event) {

		// only intercept back button press
		if (i == KeyEvent.KEYCODE_BACK) {
			if (rootView != null) {
				runFadeOutAnimAndFinish(i, event);
				return true; // consume this keyevent
			} else {
				super.onKeyDown(i, event);
				return true;
			}
		}

		return false; // propagate this keyevent
	}

	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	// fade out animation support when activity is finished
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	protected View rootView;

	public void setRootViewToRunFinishAnimOn(View root) {
		rootView = root;
	}

	/**
	 * simply runs a fadeout anim on the {@link #rootView}, and then call the
	 * super class's implementation of the back button press eventhandler.
	 */
	public void runFadeOutAnimAndFinish(final int i, final KeyEvent event) {

		if (rootView == null)
			throw new IllegalArgumentException("rootView can not be null!");

		// run an animation to fade this out...
		Animation animation = AnimUtils.runFadeOutAnimationOn(this, rootView);

		animation.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {

				if (i == 0 && event == null)
					finish();
				else
					SimpleSubclassCallingActivity.super.onKeyDown(i, event);
			}

			public void onAnimationRepeat(Animation animation) {
			}
		});

	}

	/**
	 * simply calls the {@link #runFadeOutAnimAndFinish(int, KeyEvent)} with
	 * some params that will cause it not to call the super class's
	 * implpementation of KEYCODE_BACK pressed
	 */
	public void runFadeOutAnimationAndFinish() {
		if (rootView == null)
			throw new IllegalArgumentException("rootView can not be null!");
		runFadeOutAnimAndFinish(0, null);
	}

}// end class SimpleActivity
