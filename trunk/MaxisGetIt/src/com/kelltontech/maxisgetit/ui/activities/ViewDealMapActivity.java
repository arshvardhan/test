package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.DealMapInfoWindowAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.dao.OutLet;
import com.kelltontech.maxisgetit.dao.OutLetDetails;
import com.kelltontech.maxisgetit.db.CityTable;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class ViewDealMapActivity extends MaxisMainActivity implements
OnGlobalLayoutListener, OnClickListener {

	private GoogleMap mMap;
	ArrayList<OutLet> outLets = new ArrayList<OutLet>();
	private DealMapInfoWindowAdapter mMapInfoWindowAdapter;

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
	private ArrayList<CityOrLocality> localityList;
	private ArrayList<String> ids = new ArrayList<String>();
	private TextView mainSearchButton;
	private ArrayList<String> selectedLocalityindex;
	private LinearLayout wholeSearchBoxContainer;

	private LinearLayout mSearchContainer;
	private EditText mSearchEditText;
	private ImageView mSearchBtn;
	private ImageView mProfileIconView;
	private ImageView mHeaderBackButton;
	private ImageView mHomeIconView;
	private ImageView mSearchToggler;
	private TextView mHeaderText;

	private int indexOfOutlet;
	private String title = "";
	private TextView showRoute;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == AppConstants.CITY_REQUEST) {
			if (!selectedCity
					.equalsIgnoreCase(data.getStringExtra("CITY_NAME"))) {
				localityItems = null;
				ids = null;
				selectedLocalityindex = null;
				currentLocality.setText("Choose your Area");
			}
			selectedCity = data.getStringExtra("CITY_NAME");
			currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCity
					+ "</b>"));
			int index = data.getIntExtra("CITY_INDEX", 0);
			if (index == -1) {
				city_id = -1;
			} else {
				city_id = cityList.get(index).getId();
			}

		} else if (resultCode == RESULT_OK
				&& requestCode == AppConstants.LOCALITY_REQUEST) {
			String locality = "";

			selectedLocalityItems = data
					.getStringArrayListExtra("SELECTED_LOCALITIES");

			selectedLocalityindex = data
					.getStringArrayListExtra("SELECTED_LOCALITIES_INDEX");
			if (selectedLocalityItems != null
					&& selectedLocalityItems.size() > 0) {
				for (int i = 0; i < selectedLocalityItems.size(); i++) {

					if (i == selectedLocalityItems.size() - 1) {
						locality += selectedLocalityItems.get(i);
					} else {
						locality += selectedLocalityItems.get(i) + ",";
					}
				}
				currentLocality.setText(Html.fromHtml("Your Selected Area "
						+ "<b>" + locality + "</b>"));
			} else {
				currentLocality.setText("Choose your Area");
			}

			ids = new ArrayList<String>();

			if (selectedLocalityindex != null
					&& selectedLocalityindex.size() > 0) {
				for (int i = 0; i < selectedLocalityindex.size(); i++) {

					ids.add(String.valueOf(localityList.get(
							Integer.parseInt(selectedLocalityindex.get(i)))
							.getId()));
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_deal_map);

		Bundle bundle = getIntent().getExtras();
		outLets = bundle.getParcelableArrayList(AppConstants.OUTLET_DATA);
		indexOfOutlet = bundle.getInt("index");
		title = getIntent().getStringExtra("DEAL_TITLE");

		mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);

		mHeaderText = (TextView) findViewById(R.id.header_title);

		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mSearchToggler = (ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mSearchContainer.setOnClickListener(this);
		mSearchEditText.setOnClickListener(this);
		mSearchBtn.setOnClickListener(this);

		if (!StringUtil.isNullOrEmpty(title)) {
			mHeaderText.setText(title);
		}

		mSearchEditText = (EditText) findViewById(R.id.search_box);
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

		setUpMapIfNeeded(indexOfOutlet);

		showRoute = (TextView) findViewById(R.id.txv_icon_show_route);
		showRoute.setOnClickListener(this);
		if(indexOfOutlet == -1) {
			((LinearLayout) findViewById(R.id.footer_compass_screen)).setVisibility(View.GONE);
		} else {
			((LinearLayout) findViewById(R.id.footer_compass_screen)).setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		AnalyticsHelper.trackSession(ViewDealMapActivity.this, AppConstants.ViewOnMap);
	}

	@Override
	public Activity getMyActivityReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

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
			AnalyticsHelper
			.endTimedEvent(FlurryEventsConstants.APPLICATION_COMBINED_LIST);
			this.finish();
			break;
		case R.id.col_refine_search:
		case R.id.col_refine_search1:
			AnalyticsHelper.logEvent(FlurryEventsConstants.MODIFY_SEARCH_CLICK);
			break;
		case R.id.mainSearchButton:
			mSearchEditText
			.setText(mSearchEditText.getText().toString().trim());

			String JSON_EXTRA = jsonForSearch();
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA);
			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			showHomeScreen();
			break;
		case R.id.show_profile_icon:
			onProfileClick();
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
				Intent cityIntent = new Intent(ViewDealMapActivity.this,
						AdvanceSelectCity.class);
				cityIntent.putExtra("CITY_LIST", cityListString);
				cityIntent.putExtra("SELECTED_CITY", selectedCity);
				startActivityForResult(cityIntent, AppConstants.CITY_REQUEST);
			} else {
				setSearchCity();
			}
			break;

		case R.id.currentLocality:
			if (localityItems != null && localityItems.size() > 0) {
				Intent localityIntent = new Intent(ViewDealMapActivity.this,
						AdvanceSelectLocalityActivity.class);
				localityIntent.putExtra("LOCALITY_LIST", localityItems);
				localityIntent.putStringArrayListExtra("LOCALITY_INDEX",
						selectedLocalityindex);
				startActivityForResult(localityIntent,
						AppConstants.LOCALITY_REQUEST);
			} else {
				setSearchLocality(city_id);
			}
			break;
		case R.id.txv_icon_show_route:
			if (isLocationAvailable()) {
				showMapActivity();
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onPositiveDialogButton(int id) {
		if (id == CustomDialog.DATA_USAGE_DIALOG) {
			if (isLocationAvailable()) {
				showMapActivity();
			}
		}
	}

	private void showMapActivity() {
		if (isLocationAvailable()) {
			String url = "http://maps.google.com/maps?saddr="
					+ GPS_Data.getLatitude() + "," + GPS_Data.getLongitude()
					+ "&daddr=" + outLets.get(indexOfOutlet).getLat() + ","
					+ outLets.get(indexOfOutlet).getLongt() + "&mode=driving";
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse(url));
			intent.setClassName("com.google.android.apps.maps",
					"com.google.android.maps.MapsActivity");
			startActivity(intent);
		}
	}

	@Override
	public void onGlobalLayout() {
		// TODO Auto-generated method stub

	}

	private void setUpMapIfNeeded(int index) {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap(index);
			}
		}
	}

	private void setUpMap(int index) {
		mMap.setMyLocationEnabled(true);
		// Location loc = mMap.getMyLocation();
		// loc.getLatitude()!=0&&loc.getLongitude()!=0
		LatLng fromPosition = new LatLng(GPS_Data.getLatitude(),
				GPS_Data.getLongitude());
		// LatLng fromPosition=new LatLng(6.419371,99.810822);
		// 28.589345,77.040825

		// LatLng toPosition=new LatLng(28.393213,77.248878);
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		// builder.include(fromPosition);
		// sourceMarker = mMap.addMarker(new MarkerOptions()
		// .position(fromPosition)
		// .title("You are here")
		// .icon(BitmapDescriptorFactory
		// .fromResource(R.drawable.map_user_marker)));
		// builder.include(sourceMarker.getPosition());
		LatLng toPosition = null;
		LatLng nearestPosition = null;
		if (outLets != null) {

			if (index == -1) {
				for (int i = 0; i < outLets.size(); i++) {

					if (!StringUtil.isNullOrEmpty(outLets.get(i).getLat())
							&& !StringUtil.isNullOrEmpty(outLets.get(i)
									.getLat())) {
						toPosition = new LatLng(Double.parseDouble(outLets.get(
								i).getLat()), Double.parseDouble(outLets.get(i)
										.getLongt()));
						mMap.addMarker(new MarkerOptions()
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.map_company_marker))
								.position(toPosition)
								.title(outLets.get(i).getTitle())
								.snippet(getSnippet(outLets.get(i))));
						builder.include(toPosition);
					}
					nearestPosition = new LatLng(Double.parseDouble(outLets.get(0)
							.getLat()), Double.parseDouble(outLets.get(0).getLongt()));
				}
			} else {

				if (!StringUtil.isNullOrEmpty(outLets.get(index).getLat())
						&& !StringUtil.isNullOrEmpty(outLets.get(index)
								.getLat())) {
					toPosition = new LatLng(Double.parseDouble(outLets.get(
							index).getLat()), Double.parseDouble(outLets.get(
									index).getLongt()));
					mMap.addMarker(new MarkerOptions()
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.map_company_marker))
							.position(toPosition)
							.title(outLets.get(index).getTitle())
							.snippet(getSnippet(outLets.get(index))));
					builder.include(toPosition);
				}
				nearestPosition = new LatLng(Double.parseDouble(outLets.get(index)
						.getLat()), Double.parseDouble(outLets.get(index).getLongt()));
			}
			
		}
		if (nearestPosition == null) {
			nearestPosition = fromPosition;
		}
//		LatLngBounds bounds = builder.build();
//		int padding = 50; // offset from edges of the map in pixels
		// CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,
		// padding);
		CameraUpdate cu = (index == -1) ? CameraUpdateFactory.newLatLngZoom(nearestPosition,10.0f) : CameraUpdateFactory.newLatLngZoom(nearestPosition,14.0f) ;
		mMap.animateCamera(cu);

		final CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(fromPosition) // Sets the center of the map to
		// Mountain
		// View
		.zoom(14) // Sets the zoom
		.bearing(90) // Sets the orientation of the camera to east
		.tilt(30) // Sets the tilt of the camera to 30 degrees
		.build();
		// mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		if (outLets != null && outLets.size() > 0) {
			if (index != -1) {
				mMapInfoWindowAdapter = new DealMapInfoWindowAdapter(this);
				ArrayList<OutLet> nearestOutlet = new ArrayList<OutLet>();

				nearestOutlet.add(outLets.get(index));
				mMapInfoWindowAdapter.setData(nearestOutlet);
				mMap.setInfoWindowAdapter(mMapInfoWindowAdapter);

			} else {
				mMapInfoWindowAdapter = new DealMapInfoWindowAdapter(this);
				mMapInfoWindowAdapter.setData(outLets);
				mMap.setInfoWindowAdapter(mMapInfoWindowAdapter);
			}
		}
		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				if (!StringUtil.isNullOrEmpty(marker.getSnippet())) {
					String mCurrentCompId;
					mCurrentCompId = marker.getSnippet().split(
							AppConstants.SPLIT_STRING)[0];

					//					mCurrentCompId = marker.getSnippet().split(
					//							AppConstants.SPLIT_STRING)[0];
					OutLet outLet = mMapInfoWindowAdapter
							.getValue(mCurrentCompId);
					Intent intent = new Intent(ViewDealMapActivity.this,
							CompanyDetailActivity.class);

					Bundle bundle = new Bundle();
					bundle.putString(AppConstants.COMP_ID, mCurrentCompId);
					bundle.putString(AppConstants.GLOBAL_SEARCH_KEYWORD,
							mSearchKeyword);
					bundle.putBoolean(AppConstants.IS_DEAL_LIST, true);
					intent.putExtra(AppConstants.CATEGORY_ID_KEY,
							outLet.getCatid());
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
					// sourceMarker.setPosition(new
					// LatLng(GPS_Data.getLatitude(),
					// GPS_Data.getLongitude()));
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

	private String getSnippet(OutLet outLet) {
		// id + location + image + distance
		String adddress = "";
		adddress = outLet.getAddress();
		String snippet = outLet.getId() + AppConstants.SPLIT_STRING + adddress
				+ AppConstants.SPLIT_STRING + outLet.getIcon_url()
				+ AppConstants.SPLIT_STRING + outLet.getPhone_no();
		return snippet;
	}

	public String jsonForSearch() {

		// {"city":{"city_id":5,"city_name":"adyui"},"locality":[{"locality_id":5,"locality_name":"adyui"},{"locality_id":5,"locality_name":"adyui"}]}
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
						localityArray.put("locality_name",
								selectedLocalityItems.get(i));
						jsonArray.put(localityArray);

					}
					jArray.put("locality", jsonArray);
				}
				return jArray.toString();
			} else {
				return null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.DOWNLOAD_DEAL) {
			Message message = (Message) screenData;
			handler.sendMessage(message);
		} else if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
		} else if (event == Events.TYPE_BY_CATEGORY_EVENT
				|| event == Events.SUBCATEGORY_EVENT) {
			handler.sendMessage((Message) screenData);
		} else if (event == Events.CITY_LISTING
				|| event == Events.LOCALITY_LISTING) {
			Message message = (Message) screenData;
			handler.sendMessage(message);
		} else {
			System.out.println(screenData);
			Response response = (Response) screenData;
			Message message = new Message();
			message.arg2 = event;
			message.arg1 = 1;
			if (response.isError()) {
				message.obj = response.getErrorText();
			}

			else if (event == Events.OUTLET_DETAIL) {

				try {
					if (response.getPayload() instanceof OutLetDetails) {
						OutLetDetails outLetDetails = (OutLetDetails) response
								.getPayload();
						if (outLetDetails.getErrorCode() != 0) {
							message.obj = getResources().getString(
									R.string.communication_failure);
						} else {
							if (outLetDetails.getOutlet().size() < 1) {
								message.obj = new String(getResources()
										.getString(R.string.no_result_found));
							} else {
								message.arg1 = 0;
								message.obj = outLetDetails;
							}
						}
					} else {
						message.obj = new String(getResources().getString(
								R.string.communication_failure));
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (event == Events.DEAL_DETAIL) {
				if (response.getPayload() instanceof CompanyDetail) {
					CompanyDetail compDetail = (CompanyDetail) response
							.getPayload();
					if (compDetail.getErrorCode() != 0) {
						message.obj = getResources().getString(
								R.string.communication_failure);
					} else {
						message.arg1 = 0;
						message.obj = compDetail;
					}
				} else {
					message.obj = new String(getResources().getString(
							R.string.communication_failure));
				}
			}
			handler.sendMessage(message);
		}
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.CITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				CityTable cityTable = new CityTable(
						(MyApplication) getApplication());
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				// cityTable.addCityList(glistRes.getCityOrLocalityList());
				cityList = glistRes.getCityOrLocalityList();
				// inflateCityList(cityList);
				Intent intent = new Intent(ViewDealMapActivity.this,
						AdvanceSelectCity.class);
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
				Intent intent = new Intent(ViewDealMapActivity.this,
						AdvanceSelectLocalityActivity.class);
				localityItems = new ArrayList<String>();
				for (CityOrLocality dealCityOrLoc : localityList) {
					localityItems.add(dealCityOrLoc.getName());
				}
				intent.putExtra("LOCALITY_LIST", localityItems);
				intent.putStringArrayListExtra("LOCALITY_INDEX",
						selectedLocalityindex);
				startActivityForResult(intent, AppConstants.LOCALITY_REQUEST);

			}
		} else if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		}
	}
}
