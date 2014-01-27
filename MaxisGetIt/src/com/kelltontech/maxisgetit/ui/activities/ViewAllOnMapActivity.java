package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.MapInfoWindowAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.dao.CompanyDesc;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.requests.CombinedListRequest;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class ViewAllOnMapActivity extends MaxisFragmentBaseActivity implements
		OnGlobalLayoutListener {
	private GoogleMap mMap;
	private ArrayList<CompanyDesc> mCompanyDetailList;
	private ImageView mProfileIconView;
	private Marker sourceMarker;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private TextView mHeaderTitle;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private ImageView mHeaderBackButton;
	private MapInfoWindowAdapter mMapInfoWindowAdapter;
	private String mCurrentCompId;
	private ImageView mHomeIconView;
	private String mSearchKeyword;
	CompanyDetail compDetailResponse;
	private CombinedListRequest mClRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_full);
		Bundle bundle = getIntent().getExtras();
		mCompanyDetailList = bundle
				.getParcelableArrayList(AppConstants.COMP_DETAIL_LIST);
		mClRequest = bundle.getParcelable(AppConstants.DATA_LIST_REQUEST);
		String headerTitle = bundle.getString(AppConstants.MAP_ALL_TITLE);
		boolean isSearchKeyword = bundle
				.getBoolean(AppConstants.IS_SEARCH_KEYWORD);
		compDetailResponse = null;
		// setUpMapIfNeeded();
		ImageLoader.initialize(ViewAllOnMapActivity.this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler = (ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);

		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);

		mHeaderTitle = (TextView) findViewById(R.id.header_title);
		mHeaderTitle.setText(headerTitle);

		if (isSearchKeyword) {
			mSearchEditText.setText(headerTitle);

			mSearchKeyword = bundle
					.getString(AppConstants.GLOBAL_SEARCH_KEYWORD);

			if (!StringUtil.isNullOrEmpty(mSearchKeyword.trim()))

				mSearchEditText.setText(mSearchKeyword.trim());
		} else {
			mSearchKeyword = "";
		}

		findViewById(R.id.fm_switch_view).setVisibility(View.INVISIBLE);
		findViewById(R.id.footerLayout).setVisibility(View.GONE);
		((RelativeLayout) findViewById(R.id.map_base_layout))
				.getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}

	}

	private void setUpMap() {
		mMap.setMyLocationEnabled(true);
		Location loc = mMap.getMyLocation();
		// loc.getLatitude()!=0&&loc.getLongitude()!=0
		LatLng fromPosition = new LatLng(GPS_Data.getLatitude(),
				GPS_Data.getLongitude());
		// LatLng fromPosition=new LatLng(6.419371,99.810822);
		// 28.589345,77.040825

		// LatLng toPosition=new LatLng(28.393213,77.248878);
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		builder.include(fromPosition);
		sourceMarker = mMap.addMarker(new MarkerOptions()
				.position(fromPosition)
				.title("You are here")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.map_user_marker)));
		// builder.include(sourceMarker.getPosition());
		LatLng toPosition = null;
		for (int i = 0; i < mCompanyDetailList.size(); i++) {
			toPosition = new LatLng(mCompanyDetailList.get(i).getLatitude(),
					mCompanyDetailList.get(i).getLongitude());
			mMap.addMarker(new MarkerOptions()
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.map_company_marker))
					.position(toPosition)
					.title(mCompanyDetailList.get(i).getTitle())
					.snippet(getSnippet(mCompanyDetailList.get(i))));
			builder.include(toPosition);
		}
		LatLngBounds bounds = builder.build();
		int padding = 100; // offset from edges of the map in pixels
		CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
		mMap.animateCamera(cu);
		final CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(fromPosition) // Sets the center of the map to Mountain
										// View
				.zoom(15) // Sets the zoom
				.bearing(90) // Sets the orientation of the camera to east
				.tilt(30) // Sets the tilt of the camera to 30 degrees
				.build();
		// mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		mMapInfoWindowAdapter = new MapInfoWindowAdapter(this);
		mMapInfoWindowAdapter.setData(mCompanyDetailList);
		mMap.setInfoWindowAdapter(mMapInfoWindowAdapter);

		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				if (!StringUtil.isNullOrEmpty(marker.getSnippet())) {
					mCurrentCompId = marker.getSnippet().split(
							AppConstants.SPLIT_STRING)[0];

					mCurrentCompId = marker.getSnippet().split(
							AppConstants.SPLIT_STRING)[0];
					CompanyDesc compDesc = mMapInfoWindowAdapter
							.getValue(mCurrentCompId);
					Intent intent;
					if (getIntent().getExtras().getBoolean(
							AppConstants.IS_DEAL_LIST)) {
						intent = new Intent(ViewAllOnMapActivity.this,
								DealDetailActivity.class);
						
						intent.putExtra(AppConstants.DATA_LIST_REQUEST,
								mClRequest);
						intent.putExtra(AppConstants.COMP_DETAIL_DATA,
								compDetailResponse);
					} else {
						intent = new Intent(ViewAllOnMapActivity.this,
								CompanyDetailActivity.class);
					}

					Bundle bundle = new Bundle();
					bundle.putString(AppConstants.COMP_ID, mCurrentCompId);
					bundle.putString(AppConstants.GLOBAL_SEARCH_KEYWORD,
							mSearchKeyword);
					bundle.putBoolean(AppConstants.IS_DEAL_LIST, getIntent()
							.getExtras().getBoolean(AppConstants.IS_DEAL_LIST));
					intent.putExtra(AppConstants.CATEGORY_ID_KEY,
							compDesc.getCat_id());

					
					
					intent.putExtras(bundle);
					startActivity(intent);

					/*
					 * Dialog dialog =
					 * CustomDialog.CreateCustomDialog(ViewAllOnMapActivity
					 * .this, CustomDialog.MAP_DIALOG, marker.getTitle());
					 * dialog.show();
					 */
				}
			}
		});
		mMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
			@Override
			public void onMyLocationChange(Location location) {
				if (location.getLatitude() != 0 && location.getLongitude() != 0) {
					GPS_Data.setLatitude(location.getLatitude());
					GPS_Data.setLongitude(location.getLongitude());
					sourceMarker.setPosition(new LatLng(GPS_Data.getLatitude(),
							GPS_Data.getLongitude()));
				}
			}
		});
		mMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
			@Override
			public boolean onMyLocationButtonClick() {
				mMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));
				return true;
			}
		});

	}

	private String getSnippet(CompanyDesc companyDesc) {
		// id + location + image + distance
		String adddress = "";
		adddress += companyDesc.getLocality() + ", ";
		// adddress += companyDesc.getCity() + ", ";
		// adddress += companyDesc.getState() + " ";
		adddress += companyDesc.getPincode();
		String snippet = companyDesc.getCompId() + AppConstants.SPLIT_STRING
				+ adddress + AppConstants.SPLIT_STRING
				+ companyDesc.getIconUrl() + AppConstants.SPLIT_STRING
				+ companyDesc.getDistance() + AppConstants.SPLIT_STRING
				+ companyDesc.getContactNo();
		return snippet;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.show_profile_icon:
			onProfileClick();
			break;
		case R.id.search_toggler:
			AnalyticsHelper.logEvent(FlurryEventsConstants.HOME_SEARCH_CLICK);
			if (mSearchContainer.getVisibility() == View.VISIBLE) {
				mSearchContainer.setVisibility(View.GONE);
			} else {
				mSearchContainer.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.header_btn_back:
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			this.finish();
			break;
		case R.id.search_icon_button:
			mSearchEditText
					.setText(mSearchEditText.getText().toString().trim());
			performSearch(mSearchEditText.getText().toString());
			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intentHome = new Intent(ViewAllOnMapActivity.this,
					HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		}

	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		}
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		}
	}

	@Override
	public Activity getMyActivityReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onPositiveDialogButton(int id) {
		Intent intent = null;
		Bundle bundle = new Bundle();
		CompanyDetail companyDetail = getCompanyDetails();

		switch (id) {
		case R.id.dialog_map_details:
			Log.e("FINDIT", "ViewAllOnMapActivity : mCurrentCompId = "
					+ mCurrentCompId);
			if (!StringUtil.isNullOrEmpty(mCurrentCompId)) {
				intent = new Intent(ViewAllOnMapActivity.this,
						CompanyDetailActivity.class);
				bundle.putString(AppConstants.COMP_ID, mCurrentCompId);
				bundle.putBoolean(AppConstants.IS_DEAL_LIST, getIntent()
						.getExtras().getBoolean(AppConstants.IS_DEAL_LIST));
				intent.putExtras(bundle);
			}
			break;
		case R.id.dialog_map_driving: {
			if (companyDetail == null)
				return;
			intent = new Intent(ViewAllOnMapActivity.this,
					FullMapActivity.class);
			bundle.putParcelable(AppConstants.COMP_DETAIL_DATA, companyDetail);
			bundle.putInt(AppConstants.MAP_MODE, AppConstants.MAP_DRIVING_MODE);
			break;
		}
		case R.id.dialog_map_walking: {
			if (companyDetail == null)
				return;
			intent = new Intent(ViewAllOnMapActivity.this,
					FullMapActivity.class);
			bundle.putParcelable(AppConstants.COMP_DETAIL_DATA, companyDetail);
			bundle.putInt(AppConstants.MAP_MODE, AppConstants.MAP_WALKING_MODE);
			break;
		}
		}
		intent.putExtras(bundle);
		startActivity(intent);
		super.onPositiveDialogButton(id);
	}

	private CompanyDetail getCompanyDetails() {
		CompanyDetail compDetail = null;
		for (int i = 0; i < mCompanyDetailList.size(); i++) {
			if (mCompanyDetailList.get(i).getCompId().equals(mCurrentCompId)) {
				compDetail = new CompanyDetail();
				compDetail.setTitle(mCompanyDetailList.get(i).getTitle());
				compDetail.setCity(mCompanyDetailList.get(i).getCity());
				compDetail.setState(mCompanyDetailList.get(i).getState());
				compDetail.setLocality("");
				compDetail.setLatitude(mCompanyDetailList.get(i).getLatitude());
				compDetail.setLongitude(mCompanyDetailList.get(i)
						.getLongitude());
				compDetail.setDistance(mCompanyDetailList.get(i).getDistance());
				compDetail.setPincode("");
				return compDetail;
			}
		}
		return compDetail;
	}

	@Override
	public void onGlobalLayout() {
		setUpMapIfNeeded();

	}

}
