package com.kelltontech.maxisgetit.ui.activities;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.service.AppSharedPreference;
import com.kelltontech.maxisgetit.service.LocationFinderService;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.ui.widgets.MyCompassView;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class CompassDirectionActivity extends MaxisMainActivity implements SensorEventListener{
	private ImageView mProfileIconView;
    private MyCompassView compassView;
    private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private TextView mHeaderTitle;
    // record the compass picture angle turned
    private float currentDegree = 0f;
    private LinearLayout compassHolder;
    // device sensor manager
    private SensorManager mSensorManager;
    private CompanyDetail mCompanyDetail;
    private float mBearingFromLocation;
	private ImageView mHeaderBackButton;
	private ImageView mHomeIconView;
	private BroadcastReceiver mLocationReceiver;
	private String mDistanceFromMyLocation;
	private float mLastAzimuth;
	private EditText mSearchEditText;
	private ImageView mSearchBtn;
	private Sensor mOrientationSensor;
	private Sensor mAccelerometerSensor;
	private Intent mLocationServiceIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compass_direction);
		if(getIntent().getExtras().containsKey(AppConstants.COMP_DETAIL_DATA))
		{
			mCompanyDetail = getIntent().getParcelableExtra(AppConstants.COMP_DETAIL_DATA);
			
		}
		
		if(mCompanyDetail == null)
		{
			Toast.makeText(this, "Some error occured.", Toast.LENGTH_LONG).show();
			return;
		}
		
		((TextView)findViewById(R.id.compass_comp_name)).setText(mCompanyDetail.getTitle());
		((TextView)findViewById(R.id.compass_location)).setText(mCompanyDetail.getLocality() + ", " + mCompanyDetail.getCity() + ", " + mCompanyDetail.getPincode());
		
		if(mCompanyDetail.getRecordType().equalsIgnoreCase(AppConstants.COMP_TYPE_DEAL))
		{
			findViewById(R.id.compass_rated_user_count).setVisibility(View.GONE);
			findViewById(R.id.compass_rating_comp).setVisibility(View.GONE);
		}
		else
		{
			((TextView)findViewById(R.id.compass_rated_user_count)).setText("( " + String.valueOf(mCompanyDetail.getRatedUserCount()) + " )");
			((RatingBar)findViewById(R.id.compass_rating_comp)).setRating(mCompanyDetail.getRating());
		}
		calculateBearing();
		calculateDistance();
		compassHolder=(LinearLayout) findViewById(R.id.compass_layout);
		compassView = new MyCompassView(this);
		compassHolder.addView(compassView);
	    mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    mSearchContainer=(LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler=(ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);
		mHeaderTitle=(TextView) findViewById(R.id.header_title);
		mHeaderTitle.setText(getResources().getString(R.string.cd_header_title));
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(CompassDirectionActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		findViewById(R.id.txv_icon_show_map).setOnClickListener(this);
		
		String searchKeyword =  getIntent().getExtras().getString(AppConstants.GLOBAL_SEARCH_KEYWORD);
		
		if(!StringUtil.isNullOrEmpty(searchKeyword))
			mSearchEditText.setText(searchKeyword);
		
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mLocationServiceIntent = new Intent(CompassDirectionActivity.this, LocationFinderService.class);
		startService(mLocationServiceIntent);
		mLocationReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				locationUpdate();
			}
		};
		
		IntentFilter filterSend = new IntentFilter();
		filterSend.addAction("location");
		registerReceiver(mLocationReceiver, filterSend);
		
		compassView.updateData(mLastAzimuth, mBearingFromLocation, mDistanceFromMyLocation);
	}

	private void calculateBearing() {
		Location userLocation = new Location("");
		userLocation.setLatitude(GPS_Data.getLatitude());
		userLocation.setLongitude(GPS_Data.getLongitude());
		Location compLocation = new Location("");
		compLocation.setLatitude(mCompanyDetail.getLatitude());
		compLocation.setLongitude(mCompanyDetail.getLongitude());
		
		mBearingFromLocation = userLocation.bearingTo(compLocation);
		
	}

	protected void locationUpdate() {
		/*try {
			unregisterReceiver(mLocationReceiver);
		} catch (Exception e) {
		}*/
		double mLongitude = AppSharedPreference.getFloat(AppSharedPreference.LONGITUDE, 0.0f, getApplication());
		double mLattitude = AppSharedPreference.getFloat(AppSharedPreference.LATITUDE, 0.0f, getApplication());
		if (mLongitude == 0.0f && mLattitude == 0.0f) {
			mLongitude = AppSharedPreference.getFloat(AppSharedPreference.LONGITUDE_N, 0.0f, getApplication());
			mLattitude = AppSharedPreference.getFloat(AppSharedPreference.LATITUDE_N, 0.0f, getApplication());
		}
		Log.i("maxis", mLattitude + " , " + mLongitude);
		if (mLongitude != 0.0f && mLattitude != 0.0f) {
			GPS_Data.setLatitude(mLattitude);
			GPS_Data.setLongitude(mLongitude);
		}
		calculateBearing();
		calculateDistance();
		compassView.updateData(mLastAzimuth, mBearingFromLocation, mDistanceFromMyLocation);
	}

	private void calculateDistance() {
		Location userLocation = new Location("");
		userLocation.setLatitude(GPS_Data.getLatitude());
		userLocation.setLongitude(GPS_Data.getLongitude());
		Location compLocation = new Location("");
		compLocation.setLatitude(mCompanyDetail.getLatitude());
		compLocation.setLongitude(mCompanyDetail.getLongitude());
		
		float distance = userLocation.distanceTo(compLocation)/1000;
		float[] results = new float[3];
		/*Location.distanceBetween(GPS_Data.getLatitude(), GPS_Data.getLongitude(), mCompanyDetail.getLatitude(), mCompanyDetail.getLongitude(), results);
		Log.d("maxis", "Distance : " + results[0]);*/
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		mDistanceFromMyLocation = df.format(distance) + " KM";
		
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}
	 @Override
	    protected void onResume() {
	        super.onResume();
	        
	        // for the system's orientation sensor registered listeners
	        mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	        if(mOrientationSensor != null)
	        {
	        	mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
	                SensorManager.SENSOR_DELAY_GAME);
	        }
	        else 
	        {
	        	showAlertDialog(getResources().getString(R.string.cd_error_sensor_not_found));
	        	/*mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	        	mSensorManager.registerListener(this, mAccelerometerSensor,
		                SensorManager.SENSOR_DELAY_GAME);
	        	mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);*/
	        }
	    }

	    @Override
	    protected void onPause() {
	        super.onPause();
	        
	        // to stop the listener and save battery
	        mSensorManager.unregisterListener(this);
	    }
	    
	    @Override
	    public void onSensorChanged(SensorEvent event) {
	    	
	    	if(event.sensor == mOrientationSensor)
	    	{
	    		mLastAzimuth = event.values[0];
	    	}
	    	
	    	calculateBearing();
	        compassView.updateData(mLastAzimuth, mBearingFromLocation, mDistanceFromMyLocation);

	    }

	    @Override
	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	        // not in use
	    }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intentHome = new Intent(CompassDirectionActivity.this, HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		case R.id.show_profile_icon:
			onProfileClick();
			break;
		case R.id.search_toggler:
			AnalyticsHelper.logEvent(FlurryEventsConstants.HOME_SEARCH_CLICK);
			if(mSearchContainer.getVisibility()==View.VISIBLE){
				mSearchContainer.setVisibility(View.GONE);
			}else{
				mSearchContainer.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.header_btn_back:
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			this.finish();
			break;
		case R.id.txv_icon_show_map:
			if(isDialogToBeShown()) {
				showConfirmationDialog(CustomDialog.DATA_USAGE_DIALOG,getResources().getString(R.string.cd_msg_data_usage));
			} else {
				showFullMapActivity();
			}
			break;
		case R.id.search_icon_button: 
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			performSearch(mSearchEditText.getText().toString());
			break;
		default:
			break;
		}

	}

	@Override
	public void onPositiveDialogButton(int id) {
		if(id == CustomDialog.DATA_USAGE_DIALOG){
			showFullMapActivity();
		} else {
			super.onPositiveDialogButton(id);
		}
	}
	
	private void showFullMapActivity() {
		if(isLocationAvailable()){
			Intent intent = new Intent(CompassDirectionActivity.this, FullMapActivity.class);
			intent.putExtra(AppConstants.COMP_DETAIL_DATA, mCompanyDetail);
			//intent.putExtra(AppConstants.THUMB_URL, mCategoryThumbUrl);
			intent.putExtra(AppConstants.IS_DEAL_LIST, getIntent().getExtras().getBoolean(AppConstants.IS_DEAL_LIST));
			startActivity(intent);
		}
	}
	
	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE||msg.arg2==Events.USER_DETAIL) {
			super.updateUI(msg);
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE||event==Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		}
	}
	
	@Override
	protected void onDestroy() {
		if(mLocationReceiver != null)
		{
			try{
				unregisterReceiver(mLocationReceiver);
			}catch(Exception e)	{
				//AnalyticsHelper.onError(FlurryEventsConstants.LOCATION_RECIEVER_UNREGISTER_ERR, "CompassDirectionActivity : " + AppConstants.LOCATION_RECEIVER_UNREGISTER_ERROR_MSG, e);
			}
			mLocationReceiver = null;
		}
		if(mLocationServiceIntent != null){
			try{
				stopService(mLocationServiceIntent);
			}catch(Exception e)	{
				AnalyticsHelper.onError(FlurryEventsConstants.LOCATION_SERVICE_STOP_ERR, "CompassDirectionActivity : " + AppConstants.LOCATION_SERVICE_NOT_STOP_ERROR_MSG, e);
			}
		}
		
		super.onDestroy();
	}
	
	/*
	 * Can be used later
	 * float[] grav = new float[3];
	float[] mag = new float[3];
	float[] temp = new float[9];
	float[] rotation = new float[9];
	float[] apr = new float[3];
    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
        grav[0] = event.values[0];
        grav[1] = event.values[1];
        grav[2] = event.values[2];
    } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
        mag[0] = event.values[0];
        mag[1] = event.values[1];
        mag[2] = event.values[2];
    }

    //Get the rotation matrix and remap the coordinates to compensate for landscape orientation.

    //Get rotation matrix given the gravity and geomagnetic matrices
    SensorManager.getRotationMatrix(temp, null, grav, mag);

    //Translate the rotation matrices from Y and -X (landscape)
    SensorManager.remapCoordinateSystem(temp, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, rotation);

    //Get the azimuth, pitch, and roll then convert to degrees and save for use on the radar:

    //Get the azimuth, pitch, roll
    SensorManager.getOrientation(rotation,apr);
    mLastAzimuth = 360 -(float)Math.toDegrees(apr[0]);
   // if (mLastAzimuth<0) mLastAzimuth+=360;  //Convert from -180->180 to 0->359
*/}
