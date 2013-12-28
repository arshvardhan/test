package com.kelltontech.maxisgetit.ui.activities;

import com.kelltontech.maxisgetit.service.AppSharedPreference;

/**
 * BaseActivity for 5 ContestAppActivities, code is taken from Contest App
 */
public abstract class ContestBaseActivity extends MaxisMainActivity {

	protected float			mLongitude;
	protected float			mLattitude;
	protected float			mLongitudeN;
	protected float			mLattitudeN;
	
	/**
	 * This method is used to get location
	 */
	protected final void getLocation() {
		mLongitude = AppSharedPreference.getFloat(AppSharedPreference.LONGITUDE, 0.0f, getApplication());
		mLattitude = AppSharedPreference.getFloat(AppSharedPreference.LATITUDE, 0.0f, getApplication());
		if( mLongitude == 0.0f && mLattitude == 0.0f ) {
			mLongitudeN =AppSharedPreference.getFloat(AppSharedPreference.LONGITUDE_N, 0.0f, getApplication());
			mLattitudeN =AppSharedPreference.getFloat(AppSharedPreference.LATITUDE_N, 0.0f, getApplication());
			mLongitude = mLongitudeN;
			mLattitude = mLattitudeN;
		}
	}
}

