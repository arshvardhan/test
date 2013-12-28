package com.kelltontech.maxisgetit.service;

import java.util.List;

import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LocationFinderService extends Service {
	private LocationManager mLocationManager;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("maxis","LocationFinderService.onCreate()");
		handler.removeMessages(0);
		handler.sendEmptyMessage(1);
		AppSharedPreference.putFloat(AppSharedPreference.LONGITUDE, 0.0f, getApplication());
		AppSharedPreference.putFloat(AppSharedPreference.LATITUDE, 0.0f, getApplication());
		AppSharedPreference.putFloat(AppSharedPreference.LONGITUDE_N, 0.0f, getApplication());
		AppSharedPreference.putFloat(AppSharedPreference.LATITUDE_N, 0.0f, getApplication());
		setLocationListener();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_NOT_STICKY;
	}

	private void setLocationListener() {
		Log.i("maxis","LocationFinderService.setLocationListener()");
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsLocationListener);
		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkLocationListener);
		mLocationManager.addGpsStatusListener(mGPSListener);
		boolean isMessageSend = handler.sendEmptyMessageDelayed(0, 0);
	} 

	LocationListener gpsLocationListener = new LocationListener() {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
			
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onLocationChanged(Location location) {
			Log.i("maxis","GPS loc "+location.getLatitude()+" "+location.getLongitude());
			//Toast.makeText(LocationFinderService.this, "GPS loc "+location.getLatitude()+" "+location.getLongitude(), Toast.LENGTH_LONG).show();
			AppSharedPreference.putFloat(AppSharedPreference.LONGITUDE, (float) location.getLongitude(), getApplication());
			AppSharedPreference.putFloat(AppSharedPreference.LATITUDE, (float) location.getLatitude(), getApplication());
			/*unRegisterGpsLocationListener();
			unRegisterNetworkLocationListener();*/
			handler.removeMessages(0);
			Intent intent = new Intent("location");
			sendBroadcast(intent);
			//stopSelf();
		}
	};

	LocationListener networkLocationListener = new LocationListener() {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}
		
		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onLocationChanged(Location location) {
			//Toast.makeText(LocationFinderService.this, "N loc "+location.getLatitude()+" "+location.getLongitude(), Toast.LENGTH_LONG).show();
			Log.i("maxis","N loc "+location.getLatitude()+" "+location.getLongitude());
			AppSharedPreference.putFloat(AppSharedPreference.LONGITUDE_N, (float) location.getLongitude(), getApplication());
			AppSharedPreference.putFloat(AppSharedPreference.LATITUDE_N, (float) location.getLatitude(), getApplication());
			handler.removeMessages(0);
			Intent intent = new Intent("location");
			sendBroadcast(intent);
			//unRegisterNetworkLocationListener();
		}
	};
	Listener mGPSListener = new GpsStatus.Listener() {

	    @Override
	    public void onGpsStatusChanged(final int event) {
	        switch (event) {
	            case GpsStatus.GPS_EVENT_STARTED:
	            	Log.i("maxis", "GpsStatus.Listener");
	            	//Toast.makeText(LocationFinderService.this, "GpsStatus.Listener", Toast.LENGTH_LONG).show();
	    				if(mLocationManager != null){
	    					try {
	    						String provider = mLocationManager.getBestProvider(new Criteria(), true);
	    						if (null != provider) {
	    							Location locations = mLocationManager.getLastKnownLocation(provider);
	    							List<String> providerList = mLocationManager.getAllProviders();
	    							//if (null != locations && null != providerList && providerList.size() > 0) {
	    								double latitude = locations != null ? locations.getLatitude() : 0;
	    								double longitude = locations != null ? locations.getLongitude() : 0;
	    								if(latitude==0.0f||latitude==0.0f)
	    								{
	    									locations = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    									if(locations==null)
	    										locations = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	    									if(locations==null)
	    										locations = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
	    									if(locations!=null)
	    									{
	    										Log.i("SERVICE", "Last Known Location Found---"+locations.getProvider());
	    										AppSharedPreference.putFloat(AppSharedPreference.LONGITUDE, (float)longitude, getApplication());
		    		    						AppSharedPreference.putFloat(AppSharedPreference.LATITUDE, (float)latitude, getApplication());
		    		    						handler.removeMessages(0);
		    		    						Intent intent = new Intent("location");
		    		    						sendBroadcast(intent);
	    									}

	    								}
	    								Log.i("maxis","GPS LIstener "+locations.getLatitude()+" "+locations.getLongitude());
	    								/*if (latitude != 0 && longitude != 0) {
	    									AppSharedPreference.putFloat(AppSharedPreference.LONGITUDE, (float)longitude, getApplication());
	    		    						AppSharedPreference.putFloat(AppSharedPreference.LATITUDE, (float)latitude, getApplication());
	    		    						handler.removeMessages(0);
	    		    						Intent intent = new Intent("location");
	    		    						sendBroadcast(intent);
	    								}*/
	    							//}
	    						}
	    						
	    					} catch (Exception e) {
	    						AnalyticsHelper.onError(FlurryEventsConstants.GPS_LOCATION_FINDER_ERR, "LocationFinderService : " + AppConstants.GPS_LOCATION_FINDER_ERROR_MSG, e);
	    					}
	    					/*for(int i =0; i< 4; i++)
	    					{
	    						Log.e("maxis", "GpsStatus.Listener Attempt " + i);
		    					Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		    					if(location != null){
		    						AppSharedPreference.putFloat(AppSharedPreference.LONGITUDE, (float)location.getLongitude(), getApplication());
		    						AppSharedPreference.putFloat(AppSharedPreference.LATITUDE, (float)location.getLatitude(), getApplication());
		    						AppSharedPreference.putLong(AppSharedPreference.LOCATION_TIME, System.currentTimeMillis(), getApplication());
		    						handler.removeMessages(0);
		    						Intent intent = new Intent("location");
		    						sendBroadcast(intent);
		    						return;
		    						unRegisterGpsLocationListener();
		    						unRegisterNetworkLocationListener();
		    					}
	    					}*/
	    					setLocationListener();
	    			}
	                break;
	            case GpsStatus.GPS_EVENT_FIRST_FIX:
	                break;
	            case GpsStatus.GPS_EVENT_STOPPED:
	                break;
	            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
	                break;
	            default:
	                break;
	        }


	    }


	};
	private void unRegisterNetworkLocationListener() {
		Log.i("maxis", "unRegisterNetworkLocationListener");
		try {
			if (mLocationManager != null)
				mLocationManager.removeUpdates(networkLocationListener);
		} catch (Exception e) {
			AnalyticsHelper.onError(FlurryEventsConstants.UNREGISTER_NETWORK_LOCATION_ERR, "LocationFinderService : " + AppConstants.UNREGISTER_NETWORK_LOCATION_FINDER_ERROR_MSG, e);
		}
	}

	private void unRegisterGpsLocationListener() {
		Log.i("maxis", "unRegisterGpsLocationListener");
		try {
			if (mLocationManager != null)
				mLocationManager.removeUpdates(gpsLocationListener);
		} catch (Exception e) {
			AnalyticsHelper.onError(FlurryEventsConstants.UNREGISTER_GPS_LOCATION_ERR, "LocationFinderService : " + AppConstants.UNREGISTER_GPS_LOCATION_FINDER_ERROR_MSG, e);
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			/*unRegisterGpsLocationListener();
			unRegisterNetworkLocationListener();*/
			if (msg.what == 0) {
				Intent intent = new Intent("location");
				sendBroadcast(intent);
				//stopSelf();
			}
		}
	};

	@Override
	public void onDestroy() {
		Log.i("maxis","LocationFinderService.onDestroy()");
		unRegisterGpsLocationListener();
		unRegisterNetworkLocationListener();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
