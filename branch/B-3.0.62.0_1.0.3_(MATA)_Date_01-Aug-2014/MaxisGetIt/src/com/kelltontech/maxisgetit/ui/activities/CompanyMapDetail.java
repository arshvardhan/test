package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import org.w3c.dom.Document;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.GMapV2Direction;
import com.kelltontech.maxisgetit.controllers.MapDirectionController;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.response.RouteDetailResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class CompanyMapDetail extends MaxisFragmentBaseActivity {
	private GoogleMap mMap;
	private CompanyDetail mCompanyDetail;
	private String mCategoryThumbUrl;
	private Drawable mThumbLoading;
	private Drawable mThumbError;
	private ImageView mHomeIconView;
	private ImageView mProfileIconView;
	private View mSearchBtn;
	private EditText mSearchEditText;
	private ImageView mHeaderIconView;
	private TextView mTitleView;
	private TextView mCallBtnView;
	private TextView mEmailBtnView;
	private ImageView mReferFB, mReferTwitter;
	private GMapV2Direction mMapDirection;
	private RouteDetailResponse routeDetail;
	private Marker sourceMarker;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_map);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.cm_root_layout), this);
		mReferFB = (ImageView) findViewById(R.id.cd_facebook_icon);
		mReferFB.setOnClickListener(this);
		mReferTwitter = (ImageView) findViewById(R.id.cd_twitterIcon);
		mReferTwitter.setOnClickListener(this);
		Bundle bundle = getIntent().getExtras();
		mCompanyDetail = bundle.getParcelable(AppConstants.COMP_DETAIL_DATA);
		setUpMapIfNeeded();
		ImageLoader.initialize(CompanyMapDetail.this);
		mCategoryThumbUrl = bundle.getString(AppConstants.THUMB_URL);
		mHeaderIconView = (ImageView) findViewById(R.id.cm_category_icon);
		mThumbLoading = getResources().getDrawable(R.drawable.group_load);
		mThumbError = getResources().getDrawable(R.drawable.group_cross);
		ImageLoader.start(mCategoryThumbUrl, mHeaderIconView, mThumbLoading, mThumbError);
		mTitleView = (TextView) findViewById(R.id.cm_comp_title);
		mTitleView.setText(mCompanyDetail.getTitle());
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mCallBtnView = (TextView) findViewById(R.id.cm_call_btn);
		mCallBtnView.setOnClickListener(this);
		mEmailBtnView = (TextView) findViewById(R.id.cm_mail_btn);
		mEmailBtnView.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		AnalyticsHelper.trackSession(CompanyMapDetail.this, AppConstants.Screen_CompanyDetailMap);
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
		mMapDirection = new GMapV2Direction();
		mMap.setMyLocationEnabled(true);
//		LatLng fromPosition=new LatLng(6.419371,99.810822);
		LatLng fromPosition = new LatLng(GPS_Data.getLatitude(), GPS_Data.getLongitude());
		LatLng toPosition = new LatLng(mCompanyDetail.getLatitude(), mCompanyDetail.getLongitude());
		final CameraPosition cameraPosition = new CameraPosition.Builder().target(toPosition).zoom(13) // Sets
																										// the
																										// zoom
				.bearing(90) // Sets the orientation of the camera to east
				.tilt(30) // Sets the tilt of the camera to 30 degrees
				.build(); //
		sourceMarker= mMap.addMarker(new MarkerOptions().position(fromPosition).title("You are here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
		mMap.addMarker(new MarkerOptions().position(toPosition).title(mCompanyDetail.getLocality()).snippet(mCompanyDetail.getCity()));
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		mMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
			@Override
			public boolean onMyLocationButtonClick() {
				mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
				return true;
			}
		});
		mMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
			@Override
			public void onMyLocationChange(Location location) {
				if(location.getLatitude()!=0 && location.getLongitude()!=0){
//					GPS_Data.setLatitude(location.getLatitude());
//					GPS_Data.setLongitude(location.getLongitude());
					sourceMarker.setPosition(new LatLng(GPS_Data.getLatitude(),GPS_Data.getLongitude()));
				}
			}
		});
		mMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng arg0) {
				Log.d("maxis", "click detected");
				Intent intentFullMap = new Intent(CompanyMapDetail.this, FullMapActivity.class);
				intentFullMap.putExtra(AppConstants.COMP_DETAIL_DATA, mCompanyDetail);
				intentFullMap.putExtra(AppConstants.ROUTE_DETAIL, routeDetail);
				startActivity(intentFullMap);
			}
		});
		if (fromPosition.latitude != 0 && fromPosition.longitude != 0) {
			loadRoute(fromPosition, toPosition, GMapV2Direction.MODE_DRIVING);
		}
	}

	private void loadRoute(LatLng start, LatLng end, String mode) {
		MapDirectionController routeController = new MapDirectionController(CompanyMapDetail.this, Events.MAP_ROUTE);
		startSppiner();
		routeController.requestService(start, end, mode);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.cm_map_btn:
			NativeHelper.sendSms(CompanyMapDetail.this, mCompanyDetail.getSmsNumber());
			// startActivity(new
			// Intent(this,SendSmsActivity.class).putExtra(AppConstants.COMP_DETAIL_DATA,
			// companyDetail).putExtra(AppConstants.THUMB_URL,
			// categoryThumbUrl));
			// Toast.makeText(CompanyMapDetail.this,
			// getResources().getString(R.string.under_implement),
			// Toast.LENGTH_SHORT).show();
			break;
		case R.id.cm_call_btn:
			ArrayList<String> contacts = mCompanyDetail.getContacts();
			if (contacts.size() > 0)
				NativeHelper.makeCall(CompanyMapDetail.this, contacts.get(0));
			else
				Toast.makeText(CompanyMapDetail.this, getResources().getString(R.string.contact_unavailable), Toast.LENGTH_SHORT).show();
			break;
		case R.id.cm_mail_btn:
			NativeHelper.sendMail(CompanyMapDetail.this, mCompanyDetail.getMailId());
			break;
		case R.id.search_icon_button:
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			performSearch(mSearchEditText.getText().toString());
			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intentHome = new Intent(CompanyMapDetail.this, HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			HomeActivity.fromHomeClick = true;
			break;
		case R.id.show_profile_icon:
			onProfileClick();
			break;
		case R.id.cd_facebook_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.FACEBOOK_CLICK);
			showAlertDialog(getResources().getString(R.string.under_implement));
			break;
		case R.id.cd_twitterIcon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.TWITTER_CLICK);
			showAlertDialog(getResources().getString(R.string.under_implement));
			break;
		}

	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE || event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.MAP_ROUTE) {
			handler.sendMessage((Message) screenData);
		}
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE || msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.MAP_ROUTE) {
			if (msg.arg1 == 0 && msg.obj instanceof RouteDetailResponse) {
				routeDetail = (RouteDetailResponse) msg.obj;
				drawRouteonMap();
			}
			stopSppiner();
		}
	}

	private void drawRouteonMap() {
		if (routeDetail != null && mMap != null) {
			ArrayList<LatLng> directionPoint = routeDetail.getRouteLatlngList();
			PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.RED);
			for (int i = 0; i < directionPoint.size(); i++) {
				rectLine.add(directionPoint.get(i));
			}
			mMap.addPolyline(rectLine);
		}
	}

	@Override
	public Activity getMyActivityReference() {
		// TODO Auto-generated method stub
		return null;
	}

}
