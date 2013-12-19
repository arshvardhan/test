package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.CompDetailMapWindowAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.GMapV2Direction;
import com.kelltontech.maxisgetit.controllers.MapDirectionController;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.response.RouteDetailResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class FullMapActivity extends MaxisFragmentBaseActivity implements OnGlobalLayoutListener{
	private GoogleMap mMap;
	private CompanyDetail mCompanyDetail;
	private ImageView mProfileIconView;
	private GMapV2Direction mMapDirection;
	private RouteDetailResponse mDrivingRouteDetail;
	private RouteDetailResponse mWalkingRouteDetail;
	private Marker sourceMarker;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private TextView mHeaderTitle;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private ImageView mHeaderBackButton;
	private TextView mSwitchView;
	private int mMapMode;
	private LatLng mFromPosition;
	private LatLng mToPosition;
	private CompDetailMapWindowAdapter mMapInfoWindowAdapter;
	private ImageView mHomeIconView;
	private String mSearchKeyword;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_full);
		AnalyticsHelper.logEvent(FlurryEventsConstants.APPLICATION_COMPANY_DETAIL_MAP);
		Bundle bundle = getIntent().getExtras();
		mCompanyDetail = bundle.getParcelable(AppConstants.COMP_DETAIL_DATA);
		mDrivingRouteDetail=bundle.getParcelable(AppConstants.ROUTE_DETAIL);
		mMapMode = bundle.getInt(AppConstants.MAP_MODE, 0);
		mSearchKeyword =  bundle.getString(AppConstants.GLOBAL_SEARCH_KEYWORD);
		
		mSwitchView = (TextView) findViewById(R.id.fm_switch_view);
		mSwitchView.setOnClickListener(this);
		setUpMapIfNeeded();
		ImageLoader.initialize(FullMapActivity.this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchContainer=(LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler=(ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);
		
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		
		mHeaderTitle=(TextView) findViewById(R.id.header_title);
		mHeaderTitle.setText(mCompanyDetail.getTitle());
		
		if(!StringUtil.isNullOrEmpty(mSearchKeyword))
			mSearchEditText.setText(mSearchKeyword);
		
		findViewById(R.id.map_driving).setOnClickListener(this);
		findViewById(R.id.map_walking).setOnClickListener(this);
		((RelativeLayout)findViewById(R.id.map_base_layout)).getViewTreeObserver().addOnGlobalLayoutListener(this);
	}


	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		mMapDirection=new GMapV2Direction();
		mMap.setMyLocationEnabled(true);
		Location loc = mMap.getMyLocation();
//		loc.getLatitude()!=0&&loc.getLongitude()!=0
		mFromPosition = new LatLng(GPS_Data.getLatitude(),GPS_Data.getLongitude());
//		LatLng fromPosition=new LatLng(6.419371,99.810822);
		//28.589345,77.040825
		//28.50821,77.062141
		
		//mToPosition = new LatLng(28.50821,77.062141);
		mToPosition = new LatLng(mCompanyDetail.getLatitude(), mCompanyDetail.getLongitude());
		final CameraPosition cameraPosition = new CameraPosition.Builder()
	    .target(mFromPosition)      // Sets the center of the map to Mountain View
	    .zoom(13)                   // Sets the zoom
	    .bearing(90)                // Sets the orientation of the camera to east
	    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
	    .build();
		
		//String snippet = mCompanyDetail.getPincode();//mCompanyDetail.getCity().trim() + "," + 
		sourceMarker= mMap.addMarker(new MarkerOptions().position(mFromPosition).title("You are here").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_user_marker)));
		mMap.addMarker(new MarkerOptions().position(mToPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_company_marker)).title(mCompanyDetail.getLocality()).snippet(getSnippet()));
		//mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		mMapInfoWindowAdapter = new CompDetailMapWindowAdapter(this);
		mMapInfoWindowAdapter.setData(mCompanyDetail);
		mMap.setInfoWindowAdapter(mMapInfoWindowAdapter);
		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker marker) {
				if(!StringUtil.isNullOrEmpty(marker.getSnippet()))
				{
					
				}
			}
		});
		
		mMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
			@Override
			public void onMyLocationChange(Location location) {
				if(location.getLatitude()!=0 && location.getLongitude()!=0){
					GPS_Data.setLatitude(location.getLatitude());
					GPS_Data.setLongitude(location.getLongitude());
					sourceMarker.setPosition(new LatLng(GPS_Data.getLatitude(),GPS_Data.getLongitude()));
				}
			}
		});
		mMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
			@Override
			public boolean onMyLocationButtonClick() {
				mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
				return true;
			}
		});
		toggleMapViews();
	}
	private void loadRoute(LatLng start, LatLng end, String mode) {
		MapDirectionController routeController = new MapDirectionController(FullMapActivity.this, Events.MAP_ROUTE);
		startSppiner();
		routeController.requestService(start, end, mode);
	}
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
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
		case R.id.search_icon_button:
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			performSearch(mSearchEditText.getText().toString());
			break;
		case R.id.fm_switch_view:
			mMapMode = 1 - mMapMode;
			toggleMapViews();
			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intentHome = new Intent(FullMapActivity.this, HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
			
		case R.id.map_driving:
			mMapMode = AppConstants.MAP_DRIVING_MODE;
			toggleMapViews();
			break;
		case R.id.map_walking:
			mMapMode = AppConstants.MAP_WALKING_MODE;
			toggleMapViews();
			break;
		}

	}
	private void toggleMapViews() {
		switch (mMapMode) {
		case AppConstants.MAP_DRIVING_MODE:
			mSwitchView.setText(getResources().getString(R.string.map_driving_mode));
			if(mDrivingRouteDetail!=null)
				drawRouteonMap();
			else{
				if (mFromPosition.latitude != 0 && mFromPosition.longitude != 0) {
					loadRoute(mFromPosition, mToPosition, GMapV2Direction.MODE_DRIVING);
				}
			}
			break;
		case AppConstants.MAP_WALKING_MODE:
			mSwitchView.setText(getResources().getString(R.string.map_walking_mode));
			if(mWalkingRouteDetail!=null)
				drawRouteonMap();
			else{
				if (mFromPosition.latitude != 0 && mFromPosition.longitude != 0) {
					loadRoute(mFromPosition, mToPosition, GMapV2Direction.MODE_WALKING);
				}
			}
			break;
		}
		
		
	}


	private void drawRouteonMap() {
		if (mMap != null) {
			mMap.clear();
			mMap.addMarker(new MarkerOptions().position(mFromPosition).title("You are here").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_user_marker)));//defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
			mMap.addMarker(new MarkerOptions().position(mToPosition).title(mCompanyDetail.getLocality()).snippet(getSnippet()).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_company_marker)));//mCompanyDetail.getCity()+", " +
			//mMapInfoWindowAdapter.notify();
			
			ArrayList<LatLng> directionPoint = null;
			if(mDrivingRouteDetail != null && mMapMode == AppConstants.MAP_DRIVING_MODE)
			{
				directionPoint = mDrivingRouteDetail.getRouteLatlngList();
			}
			else if(mWalkingRouteDetail != null && mMapMode == AppConstants.MAP_WALKING_MODE )
			{
				directionPoint = mWalkingRouteDetail.getRouteLatlngList();
			}
			else
				return;
			if(directionPoint != null && directionPoint.size() >0)
			{
				PolylineOptions rectLine = new PolylineOptions().width(9).color(getResources().getColor(R.color.transparent_blue_map_route));
				for (int i = 0; i < directionPoint.size(); i++) {
					rectLine.add(directionPoint.get(i));
				}
				mMap.addPolyline(rectLine);
				
			}
		}
	}
	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if(event==Events.COMBIND_LISTING_NEW_LISTING_PAGE||event==Events.USER_DETAIL){
			super.setScreenData(screenData, event, time);
			return;
		}else if (event == Events.MAP_ROUTE) {
			handler.sendMessage((Message) screenData);
		}
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE||msg.arg2==Events.USER_DETAIL) {
			super.updateUI(msg);
		}else if (msg.arg2 == Events.MAP_ROUTE) {
			if (msg.arg1 == 0 && msg.obj instanceof RouteDetailResponse) {
				if(mMapMode == AppConstants.MAP_DRIVING_MODE)
					mDrivingRouteDetail = (RouteDetailResponse) msg.obj;
				else if(mMapMode == AppConstants.MAP_WALKING_MODE)
					mWalkingRouteDetail = (RouteDetailResponse) msg.obj;
				drawRouteonMap();
			}
			stopSppiner();
		}
	}

	@Override
	public Activity getMyActivityReference() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String getSnippet() {
		//id + adddress + image + distance
		String adddress = "";
		adddress += mCompanyDetail.getLocality() + ", ";
		//adddress += mCompanyDetail.getCity() + ", ";
		//adddress += mCompanyDetail.getState() + " ";
		adddress += mCompanyDetail.getPincode();
		String snippet = mCompanyDetail.getId() + AppConstants.SPLIT_STRING + adddress + AppConstants.SPLIT_STRING + mCompanyDetail.getImageUrl() + AppConstants.SPLIT_STRING + mCompanyDetail.getDistance(); 
		return snippet;
	}


	@Override
	public void onGlobalLayout() {
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		builder.include(mFromPosition);
		builder.include(mToPosition);
		LatLngBounds bounds = builder.build();
		int padding = 100; // offset from edges of the map in pixels
		CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
		mMap.animateCamera(cu);
		
	}
}
