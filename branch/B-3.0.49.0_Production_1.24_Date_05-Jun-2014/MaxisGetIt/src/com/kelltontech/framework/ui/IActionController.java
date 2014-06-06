/**
 *
 */
package com.kelltontech.framework.ui;

import android.app.Activity;

/**
 * This interface is for managing Controller actions on UI.
 * 
 * @author prabhoo.saxena Since 1.0
 */
public interface IActionController {
	/**
	 * for set screen data.
	 */
	public void setScreenData(Object screenData, int event, long time);

	/**
	 * for getting the Application (for DB) for getting the Context SD cast
	 * etc..
	 * 
	 * @return
	 */
	public Activity getMyActivityReference();
}
