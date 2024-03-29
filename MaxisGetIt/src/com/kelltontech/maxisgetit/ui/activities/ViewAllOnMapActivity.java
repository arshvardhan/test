package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
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
import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.MapInfoWindowAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.CompanyDesc;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.requests.CombinedListRequest;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class ViewAllOnMapActivity extends MaxisMainActivity implements
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

	private boolean isAdvanceSearchLayoutOpen = false;
	private LinearLayout advanceSearchLayout;
	private TextView currentCity, currentLocality;
	private ImageView upArrow;
	private ArrayList<String> cityListString = new ArrayList<String>();
	private ArrayList<String> localityItems;
	ArrayList<CityOrLocality> cityList;
	private String selectedCity = "Entire Malaysia";
	private int city_id = -1;

	private ArrayList<String> selectedLocalityItems;
	ArrayList<CityOrLocality> localityList;
	ArrayList<String> ids = new ArrayList<String>();
	TextView mainSearchButton;
	ArrayList<String> selectedLocalityindex;
	LinearLayout wholeSearchBoxContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_full);
		Bundle bundle = getIntent().getExtras();
		mCompanyDetailList = bundle
				.getParcelableArrayList(AppConstants.COMP_DETAIL_LIST);
		mClRequest = bundle.getParcelable(AppConstants.DATA_LIST_REQUEST);
		String headerTitle = bundle.getString(AppConstants.MAP_ALL_TITLE);
		boolean isSearchKeyword = bundle.getBoolean(AppConstants.IS_SEARCH_KEYWORD);
		//		compDetailResponse = null;
		compDetailResponse = bundle.getParcelable(AppConstants.COMP_DETAIL_DATA);
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

		advanceSearchLayout = (LinearLayout) findViewById(R.id.advanceSearch);
		advanceSearchLayout.setVisibility(View.GONE);

		upArrow = (ImageView) findViewById(R.id.upArrow);
		upArrow.setOnClickListener(this);

		currentCity = (TextView) findViewById(R.id.currentCity);
		currentLocality = (TextView) findViewById(R.id.currentLocality);
		currentCity.setText(Html
				.fromHtml("in " + "<b>" + selectedCity + "</b>"));

		currentCity.setOnClickListener(this);
		currentLocality.setOnClickListener(this);

		mainSearchButton = (TextView) findViewById(R.id.mainSearchButton);
		mainSearchButton.setOnClickListener(this);

		wholeSearchBoxContainer = (LinearLayout) findViewById(R.id.whole_search_box_container);

		mSearchEditText.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				if (!isAdvanceSearchLayoutOpen) {
					isAdvanceSearchLayoutOpen = true;
					advanceSearchLayout.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		//		AnalyticsHelper.trackSession(ViewAllOnMapActivity.this, AppConstants.MapDirection);
		AnalyticsHelper.trackSession(ViewAllOnMapActivity.this, AppConstants.ViewOnMap);
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
		LatLng nearestPosition = null;
		for (int i = 0; i < mCompanyDetailList.size(); i++) {
			String lat = String
					.valueOf(mCompanyDetailList.get(i).getLatitude());
			String longt = String.valueOf(mCompanyDetailList.get(i)
					.getLongitude());
			if (!StringUtil.isNullOrEmpty(lat)
					&& !StringUtil.isNullOrEmpty(longt) && !("0.0".equals(lat)) && !("0.0".equals(longt))) {
				Log.e("Lat-Long: true",  "" +i);
				toPosition = new LatLng(
						mCompanyDetailList.get(i).getLatitude(),
						mCompanyDetailList.get(i).getLongitude());
				mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.map_company_marker))
						.position(toPosition)
						.title(mCompanyDetailList.get(i).getTitle())
						.snippet(getSnippet(mCompanyDetailList.get(i))));
				builder.include(toPosition);
			} else {
				Log.e("Lat-Long: 0.0",  "" +i);
			}

		}

		if (mCompanyDetailList != null && mCompanyDetailList.size() > 0) {
			String nearestLat = String.valueOf(mCompanyDetailList.get(0).getLatitude());
			String nearestLongt = String.valueOf(mCompanyDetailList.get(0).getLongitude());
			if (!StringUtil.isNullOrEmpty(nearestLat) && !StringUtil.isNullOrEmpty(nearestLongt) && !("0.0".equals(nearestLat)) && !("0.0".equals(nearestLongt))) {
				nearestPosition = new LatLng(mCompanyDetailList.get(0).getLatitude(), mCompanyDetailList.get(0).getLongitude());
			} else {
				nearestPosition = fromPosition;	
			}
		}

		//		LatLngBounds bounds = builder.build();
		//		int padding = 100; // offset from edges of the map in pixels
		//		CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
		//		CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(fromPosition, padding);
		CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(nearestPosition, 10.0f);
		mMap.animateCamera(cu);
		final CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(fromPosition) // Sets the center of the map to Mountain
		// View
		.zoom(14) // Sets the zoom
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

					//					mCurrentCompId = marker.getSnippet().split(
					//							AppConstants.SPLIT_STRING)[0];
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
			if (wholeSearchBoxContainer.getVisibility() == View.VISIBLE) {
				wholeSearchBoxContainer.setVisibility(View.GONE);
			} else {
				wholeSearchBoxContainer.setVisibility(View.VISIBLE);
			}
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
		case R.id.mainSearchButton:
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			String JSON_EXTRA = jsonForSearch();
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			showHomeScreen();
			break;
		case R.id.upArrow:
			if (isAdvanceSearchLayoutOpen) {
				isAdvanceSearchLayoutOpen = false;
				advanceSearchLayout.setVisibility(View.GONE);
			}
			break;
		case R.id.currentCity:
			if (cityListString != null && cityListString.size() > 0) {
				localityItems = null;
				selectedLocalityindex = null;
				Intent cityIntent = new Intent(ViewAllOnMapActivity.this, AdvanceSelectCity.class);
				cityIntent.putExtra("CITY_LIST", cityListString);
				cityIntent.putExtra("SELECTED_CITY", selectedCity);
				startActivityForResult(cityIntent, AppConstants.CITY_REQUEST);
			} else {
				setSearchCity();
			}
			break;

		case R.id.currentLocality:
			if (localityItems != null && localityItems.size() > 0) {
				Intent localityIntent = new Intent(ViewAllOnMapActivity.this, AdvanceSelectLocalityActivity.class);
				localityIntent.putExtra("LOCALITY_LIST", localityItems);
				localityIntent.putStringArrayListExtra("LOCALITY_INDEX", selectedLocalityindex);
				startActivityForResult(localityIntent, AppConstants.LOCALITY_REQUEST);
			} else {
				setSearchLocality(city_id);
			}
			break;
		default:
			break;

		}

	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.CITY_LISTING
				|| event == Events.LOCALITY_LISTING) {
			Message message = (Message) screenData;
			handler.sendMessage(message);
		}
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.CITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				cityList = glistRes.getCityOrLocalityList();
				Intent intent = new Intent(ViewAllOnMapActivity.this, AdvanceSelectCity.class);
				for (CityOrLocality cityOrLocality : cityList) {
					cityListString.add(cityOrLocality.getName());
				}
				localityItems = null;
				ids = null;
				selectedLocalityindex = null;
				currentLocality.setText("Choose your Area");
				intent.putExtra("CITY_LIST", cityListString);
				intent.putExtra("SELECTED_CITY", selectedCity);
				startActivityForResult(intent, AppConstants.CITY_REQUEST);
			}
			stopSppiner();
		} else if (msg.arg2 == Events.LOCALITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				localityList = glistRes.getCityOrLocalityList();
				Intent intent = new Intent(ViewAllOnMapActivity.this, AdvanceSelectLocalityActivity.class);
				localityItems = new ArrayList<String>();
				for (CityOrLocality dealCityOrLoc : localityList) {
					localityItems.add(dealCityOrLoc.getName());
				}
				intent.putExtra("LOCALITY_LIST", localityItems);
				intent.putStringArrayListExtra("LOCALITY_INDEX", selectedLocalityindex);
				startActivityForResult(intent, AppConstants.LOCALITY_REQUEST);

			}
		}
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	@Override
	public void onPositiveDialogButton(int id) {
		Intent intent = null;
		Bundle bundle = new Bundle();
		CompanyDetail companyDetail = getCompanyDetails();

		switch (id) {
		case R.id.dialog_map_details:
			Log.e("FINDIT", "ViewAllOnMapActivity : mCurrentCompId = " + mCurrentCompId);
			if (!StringUtil.isNullOrEmpty(mCurrentCompId)) {
				intent = new Intent(ViewAllOnMapActivity.this, CompanyDetailActivity.class);
				bundle.putString(AppConstants.COMP_ID, mCurrentCompId);
				bundle.putBoolean(AppConstants.IS_DEAL_LIST, getIntent().getExtras().getBoolean(AppConstants.IS_DEAL_LIST));
				intent.putExtras(bundle);
			}
			break;
		case R.id.dialog_map_driving: {
			if (companyDetail == null)
				return;
			intent = new Intent(ViewAllOnMapActivity.this, FullMapActivity.class);
			bundle.putParcelable(AppConstants.COMP_DETAIL_DATA, companyDetail);
			bundle.putInt(AppConstants.MAP_MODE, AppConstants.MAP_DRIVING_MODE);
			break;
		}
		case R.id.dialog_map_walking: {
			if (companyDetail == null)
				return;
			intent = new Intent(ViewAllOnMapActivity.this, FullMapActivity.class);
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
				compDetail.setLongitude(mCompanyDetailList.get(i).getLongitude());
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

	public String jsonForSearch() {
		JSONObject jArray = new JSONObject();
		try {
			if (city_id != -1) {
				JSONObject array = new JSONObject();
				array.put("city_id", city_id + "");
				array.put("city_name", selectedCity);
				jArray.put("city", array);

				if (ids != null && ids.size() > 0) {
					JSONArray jsonArray = new JSONArray();
					for (int i = 0; i < selectedLocalityItems.size(); i++) {
						JSONObject localityArray = new JSONObject();
						localityArray.put("locality_id", ids.get(i));
						localityArray.put("locality_name", selectedLocalityItems.get(i));
						jsonArray.put(localityArray);
					}
					jArray.put("locality", jsonArray);
				}
				return jArray.toString();
			} else {
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == AppConstants.AR_REPORT_ERROR_SUCCESS) {
			showInfoDialog(getResources().getString(R.string.are_error_reported));
		} else if (resultCode == AppConstants.AR_REPORT_ERROR_FAILURE) {
			showInfoDialog(getResources().getString(R.string.are_error_occured));
		} else if (resultCode == RESULT_OK && requestCode == AppConstants.CITY_REQUEST) {
			if (!selectedCity.equalsIgnoreCase(data.getStringExtra("CITY_NAME"))) {
				localityItems = null;
				ids = null;
				selectedLocalityindex = null;
				currentLocality.setText("Choose your Area");
			}
			selectedCity = data.getStringExtra("CITY_NAME");
			currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCity + "</b>"));
			int index = data.getIntExtra("CITY_INDEX", 0);
			if(index==-1)
				city_id =-1;
			else
				city_id = cityList.get(index).getId();

		} else if (resultCode == RESULT_OK && requestCode == AppConstants.LOCALITY_REQUEST) {
			String locality = "";
			selectedLocalityItems = data.getStringArrayListExtra("SELECTED_LOCALITIES");
			selectedLocalityindex = data.getStringArrayListExtra("SELECTED_LOCALITIES_INDEX");
			if (selectedLocalityItems != null && selectedLocalityItems.size() > 0) {
				for (int i = 0; i < selectedLocalityItems.size(); i++) {
					if (i == selectedLocalityItems.size() - 1) {
						locality += selectedLocalityItems.get(i);
					} else {
						locality += selectedLocalityItems.get(i) + ",";
					}
				}
				currentLocality.setText(Html.fromHtml("Your Selected Area "	+ "<b>" + locality + "</b>"));
			} else {
				currentLocality.setText("Choose your Area");
			}
			ids = new ArrayList<String>();
			if (selectedLocalityindex != null && selectedLocalityindex.size() > 0) {
				for (int i = 0; i < selectedLocalityindex.size(); i++) {
					ids.add(String.valueOf(localityList.get(Integer.parseInt(selectedLocalityindex.get(i))).getId()));
				}
			}
		}
	}
}
