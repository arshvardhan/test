package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.provider.MediaStore;

import com.kelltontech.maxisgetit.constants.AppConstants;
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
	
	public boolean hasImageCaptureBug() {

	    // list of known devices that have the bug
	    ArrayList<String> devices = new ArrayList<String>();
	    devices.add("android-devphone1/dream_devphone/dream");
	    devices.add("generic/sdk/generic");
	    devices.add("vodafone/vfpioneer/sapphire");
	    devices.add("tmobile/kila/dream");
	    devices.add("verizon/voles/sholes");
	    devices.add("google_ion/google_ion/sapphire");

	    return devices.contains(android.os.Build.BRAND + "/" + android.os.Build.PRODUCT + "/"
	            + android.os.Build.DEVICE);

	}
	
	protected void takePhoto() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, AppConstants.CAMERA_REQUEST);
	}
	
	protected void openGalery() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, AppConstants.GALLERY_REQUEST);
	}
}

