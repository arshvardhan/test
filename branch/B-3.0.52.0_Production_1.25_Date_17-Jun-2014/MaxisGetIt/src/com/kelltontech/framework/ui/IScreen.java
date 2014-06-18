/**
 *
 */
package com.kelltontech.framework.ui;

import android.os.Message;

/**
 * This interface is for ui level communication.
 * 
 * @author prabhoo.saxena Since 1.0
 */
public interface IScreen extends IActionController {
	/**
	 * this method used to load the UI.
	 */
	// public void onLoad();
	/**
	 * this method start spinning the refresh button. if not added then add
	 * first then start.
	 */
	/*
	 * public void startSppiner();
	 *//**
	 * this method stop spinning...
	 */
	/*
	 * public void stopSppiner();
	 */
	/*
	 * Update the UI
	 */
	public void updateUI(Message o);
}
