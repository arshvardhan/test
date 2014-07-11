package com.kelltontech.maxisgetit.service;

import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

public class ContestLocationFinderService extends Service implements LocationListener{
	private LocationManager                        mLocationManager;

	@Override
	public void onCreate() {
		super.onCreate();
		handler.removeMessages(0);
		handler.sendEmptyMessage(1);
		setLocationListener();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return START_NOT_STICKY;
	}

	/**
	 * This method is used for location updates.
	 */
	private void setLocationListener()
	{
		mLocationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		if(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
			mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		
			if(mLocationManager != null){
				Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				if(location != null){
					AppSharedPreference.putFloat(AppSharedPreference.LONGITUDE_N, (float)location.getLongitude(), getApplication());
					AppSharedPreference.putFloat(AppSharedPreference.LATITUDE_N, (float)location.getLatitude(), getApplication());
					AppSharedPreference.putLong(AppSharedPreference.LOCATION_TIME, System.currentTimeMillis(), getApplication());
					unRegisterGpsLocationListener();
					unRegisterNetworkLocationListener();
				}
			}
		}
		
		if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
			if(mLocationManager != null){
				Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if(location != null){
					AppSharedPreference.putFloat(AppSharedPreference.LONGITUDE, (float)location.getLongitude(), getApplication());
					AppSharedPreference.putFloat(AppSharedPreference.LATITUDE, (float)location.getLatitude(), getApplication());
					AppSharedPreference.putLong(AppSharedPreference.LOCATION_TIME, System.currentTimeMillis(), getApplication());
					unRegisterGpsLocationListener();
					unRegisterNetworkLocationListener();
				}
			}
		}
		
		handler.sendEmptyMessageDelayed(0, 3*1000);
	}

	
	/**
	 * To unregister network location listener
	 */
	private void unRegisterNetworkLocationListener()
	{
		try{
			if(mLocationManager!=null)
				mLocationManager.removeUpdates(this);
		}catch (Exception e) {
			AnalyticsHelper.onError(FlurryEventsConstants.UNREGISTER_NETWORK_LOCATION_ERR, "ContestLocationFinderService : " + AppConstants.UNREGISTER_NETWORK_LOCATION_FINDER_ERROR_MSG, e);
		}
	}

	/**
	 * To unregister GPS location listener
	 */
	private void unRegisterGpsLocationListener()
	{
		try{
			if(mLocationManager!=null)
				mLocationManager.removeUpdates(/*gpsLocationListener*/this);
		}catch (Exception e) {
			AnalyticsHelper.onError(FlurryEventsConstants.UNREGISTER_GPS_LOCATION_ERR, "ContestLocationFinderService : " + AppConstants.UNREGISTER_GPS_LOCATION_FINDER_ERROR_MSG, e);
		}
	}

	private Handler handler = new Handler()
	{
		
		public void handleMessage(android.os.Message msg) 
		{
			
			unRegisterGpsLocationListener();
			unRegisterNetworkLocationListener();
			if(msg.what==0)
			{
				Intent intent=new Intent("location");
				sendBroadcast(intent);
				stopSelf();
			}

		}
	};

	@Override
	public void onDestroy() {
		unRegisterGpsLocationListener();
		unRegisterNetworkLocationListener();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	
	
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
