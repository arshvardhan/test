package com.kelltontech.maxisgetit.ui.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.RootCategoryAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.CombindListingController;
import com.kelltontech.maxisgetit.controllers.SubCategoryController;
import com.kelltontech.maxisgetit.controllers.TypeByCategoryController;
import com.kelltontech.maxisgetit.dao.CategoryGroup;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.db.CityTable;
import com.kelltontech.maxisgetit.requests.CombinedListRequest;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.response.SubCategoryResponse;
import com.kelltontech.maxisgetit.service.AppSharedPreference;
import com.kelltontech.maxisgetit.service.LocationFinderService;
import com.kelltontech.maxisgetit.ui.widgets.CustomGallery;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class HomeActivity extends MaxisMainActivity {
	private ListView mListView;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private RootCategoryAdapter mListAdapter;
	private ImageView mHomeIconView, mFacebookIconView, mTwitterIconView, mProfileIconView, mLogo;
	private CustomGallery mImgGallery;
	private Timer mGalleryFlipperTimer;
	private Intent mLocationServiceIntent;
	private static final String GROUP_TYPE_DEAL = "Deal";
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

	ImageView box;
	LinearLayout followus;
	View seprator;
	boolean footerVisible = false;

	public static boolean fromHomeClick = false;
	public String countryName = "";

	@Override
	protected void onNewIntent(Intent intent) {
		Log.d("maxis", "HomeActivity.onNewIntent()");
		if (intent.getBooleanExtra(AppConstants.RESET, false)) {
			finish();
			Intent nInt = new Intent(HomeActivity.this, SplashActivity.class);
			startActivity(nInt);
		}
		super.onNewIntent(intent);
	}

	private void startFlipping() {
		if (mGalleryFlipperTimer != null)
			mGalleryFlipperTimer.cancel();
		mGalleryFlipperTimer = new Timer();
		new Thread(new Runnable() {
			@Override
			public void run() {
				mGalleryFlipperTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						Message m = new Message();
						m.arg2 = Events.FLIP_GALLERY_ITEM;
						handler.sendMessage(m);
					}
				}, 1000, 1500);
			}
		}).start();
	}

	private void stopFlipping() {
		if (mGalleryFlipperTimer != null)
			mGalleryFlipperTimer.cancel();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("maxis", "HomeActivity.onCreate()");
		AnalyticsHelper.logEvent(FlurryEventsConstants.APPLICATION_HOME);
		setContentView(R.layout.activity_home);
		UiUtils.hideKeyboardOnTappingOutside(
				findViewById(R.id.home_root_layout), this);
		mLocationServiceIntent = new Intent(HomeActivity.this,
				LocationFinderService.class);
		startService(mLocationServiceIntent);
		IntentFilter filterSend = new IntentFilter();
		filterSend.addAction("location");
		registerReceiver(locationReceiver, filterSend);

		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mFacebookIconView = (ImageView) findViewById(R.id.footer_facebook_icon);
		mFacebookIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mTwitterIconView = (ImageView) findViewById(R.id.footer_twitterIcon);
		mTwitterIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(HomeActivity.this);
		mLogo = (ImageView) findViewById(R.id.logo);
		mLogo.setOnClickListener(HomeActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mListView = (ListView) findViewById(R.id.home_category_list);
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
		
		mSearchEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS); 

		// uncomment for displaying banner also uncomment start flipping in
		// onResume as well set visibility on in layout
		// imgGallery = (CustomGallery) findViewById(R.id.home_gallery);
		// imgGallery.setAdapter(new ImageAdapter(HomeActivity.this));
		Bundle bundle = getIntent().getExtras();
		ArrayList<CategoryGroup> categories = bundle
				.getParcelableArrayList(AppConstants.DATA_CAT_LIST);
		CategoryGroup catPhotoContestApp = new CategoryGroup();
		catPhotoContestApp.setCategoryId(AppConstants.PHOTO_CONTEST_CAT_ID);
		categories.add(catPhotoContestApp);

		mListAdapter = new RootCategoryAdapter(HomeActivity.this);
		mListAdapter.setData(categories);
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				gotoScreen(position);
			}
		});
		// showDialogWithTitle(getResources().getString(R.string.disclaimer),
		// getResources().getString(R.string.disclaimer_text));

//		View btnPhotoContest = findViewById(R.id.btn_photo_contest);
//		btnPhotoContest.setOnClickListener(this);
		// btnPhotoContest.setVisibility(View.GONE);

		if (isLocationAware()) {
			setCurrrentLocation();
			Log.i("maxis",
					"stored " + GPS_Data.getLatitude() + " "
							+ GPS_Data.getLongitude());
			if (GPS_Data.getLatitude() == 0 && GPS_Data.getLongitude() == 0) {
				showAlertDialog(getResources().getString(
						R.string.location_unavailable_switch_gps));
			}
		} else {
			GPS_Data.resetCordinates();
		}

		// TODO:: for Advance search visibility.

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

		// advanceSearchLayout.setOnTouchListener(new OnTouchListener() {
		// private float mInitialX;
		// private float mInitialY;
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		//
		// switch (event.getAction()) {
		// case MotionEvent.ACTION_DOWN:
		// mInitialX = event.getX();
		// mInitialY = event.getY();
		// // mScrollDown = true;
		// return true;
		// case MotionEvent.ACTION_MOVE:
		// final float x = event.getX();
		// final float y = event.getY();
		// final float yDiff = y - mInitialY;
		// if (yDiff > 0.0) {
		// Log.d("maxis", "SCROLL DOWN");
		// // mScrollUp = false;
		// break;
		//
		// } else if (yDiff < 0.0) {
		// Log.d("maxis", "SCROLL up");
		// // mScrollUp = true;q
		// if (isAdvanceSearchLayoutOpen) {
		// isAdvanceSearchLayoutOpen = false;
		// advanceSearchLayout.setVisibility(View.GONE);
		// }
		//
		// break;
		// }
		// break;
		// }
		// return false;
		// }
		// });

		box = (ImageView) findViewById(R.id.box);
		box.setOnClickListener(this);
		followus = (LinearLayout) findViewById(R.id.footer_followus);
		seprator = (View) findViewById(R.id.seprator);

		if (!StringUtil.isNullOrEmpty(countryName)) {
			if (!countryName.equalsIgnoreCase("Malaysia")) {
				// TODO
				// Toast.makeText(getApplicationContext(),
				// "You are outside the country", Toast.LENGTH_SHORT).show();
			}
		}
	}

	protected void gotoScreen(int position) {
		CategoryGroup cat = (CategoryGroup) mListAdapter.getItem(position);
		if (!StringUtil.isNullOrEmpty(cat.getCategoryTitle().trim())
				&& !StringUtil.isNullOrEmpty(cat.getCategoryId().trim())) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(FlurryEventsConstants.Category_Title, cat
					.getCategoryTitle().trim());
			map.put(FlurryEventsConstants.Category_Id, cat.getCategoryId()
					.trim());
			AnalyticsHelper.logEvent(FlurryEventsConstants.CATEGORY, map);
		}
		/*
		 * if (cat.getGroupType().trim().equalsIgnoreCase(AppConstants.
		 * GROUP_ACTION_TYPE_DEAL)) showDealListing(cat); else
		 * showSubcategories(cat);
		 */
		if (cat.getCategoryId().equalsIgnoreCase(
				AppConstants.PHOTO_CONTEST_CAT_ID)) {
			return;
		}
		if (cat.getMgroupType().trim()
				.equalsIgnoreCase(AppConstants.GROUP_TYPE_CATEGORY)) {
			if (cat.getmGroupActionType().trim()
					.equalsIgnoreCase(AppConstants.GROUP_ACTION_TYPE_LIST)) {
				// showDealListing(cat);
				showSubcategories(cat);
			} else if (cat.getmGroupActionType().trim()
					.equalsIgnoreCase(AppConstants.GROUP_ACTION_TYPE_DEAL)) {
				// TODO : for Hot Deals.
				showDealcategories(cat);
			} else if (cat
					.getmGroupActionType()
					.trim()
					.equalsIgnoreCase(
							AppConstants.GROUP_ACTION_TYPE_CATEGORY_LIST_FOR_GROUP)) {
				showCompanyDealListing(cat.getCategoryId(),
						cat.getCategoryTitle(), cat.getThumbUrl(), true,
						cat.getMgroupType(), cat.getmGroupActionType(), Events.COMBIND_LISTING_NEW_LISTING_PAGE);
			}
			/*
			 * else
			 * if(cat.getmGroupActionType().trim().equalsIgnoreCase(AppConstants
			 * .GROUP_ACTION_TYPE_LIST)){ showSubcategories(cat); }
			 */

		} else {
			if (cat.getmGroupActionType().trim()
					.equalsIgnoreCase(AppConstants.GROUP_ACTION_TYPE_DEAL)) {
				showDealListing(cat);
			} else if (cat.getmGroupActionType().trim()
					.equalsIgnoreCase(AppConstants.GROUP_ACTION_TYPE_LIST)) {
				showCompanyDealListing(cat.getCategoryId(),
						cat.getCategoryTitle(), cat.getThumbUrl(), true,
						cat.getMgroupType(), cat.getmGroupActionType(), Events.COMBIND_LISTING_NEW_LISTING_PAGE);
			} else if (cat.getmGroupActionType().trim()
					.equalsIgnoreCase(AppConstants.GROUP_ACTION_TYPE_ATTRIBUTE)) {
				showTypeByCategoryScreen(cat.getCategoryId());
			}
		}
	}

	@Override
	protected void onResume() {
		Log.d("maxis", "HomeActivity.onResume()");
		if (fromHomeClick) {
			selectedCity = "Entire Malaysia";
			mSearchEditText.setText("");
			currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCity
					+ "</b>"));
			currentLocality.setText("Choose your Area");

			cityListString = new ArrayList<String>();
			city_id = -1;
			localityItems = null;
		}

		// for banner fliping
		// startFlipping();
		if (isLocationAware()) {
			setCurrrentLocation();
			Log.i("maxis",
					"stored " + GPS_Data.getLatitude() + " "
							+ GPS_Data.getLongitude());
			if (GPS_Data.getLatitude() == 0 && GPS_Data.getLongitude() == 0) {
				// showAlertDialog(getResources().getString(R.string.location_unavailable_switch_gps));
			}
		} else {
			GPS_Data.resetCordinates();
		}
		super.onResume();
		AnalyticsHelper.trackSession(HomeActivity.this, AppConstants.Home_Screen);
	}

	@Override
	protected void onPause() {
		Log.d("maxis", "HomeActivity.onPause()");
		stopFlipping();
		fromHomeClick = false;
		super.onPause();
	}

	BroadcastReceiver locationReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			locationUpdate();
		}
	};

	protected void onStop() {
		Log.i("maxis", "HomeActivity.onStop()");
		/*
		 * if(locationReceiver != null) { try{
		 * unregisterReceiver(locationReceiver); Log.e("maxis",
		 * "Unregistered locaiton data"); }catch(Exception e) {
		 * e.printStackTrace(); } locationReceiver = null; }
		 * if(mLocationServiceIntent != null){ try{
		 * stopService(mLocationServiceIntent); }catch(Exception e) {
		 * e.printStackTrace(); } }
		 */
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.i("maxis", "onDestroy");
		if (locationReceiver != null) {
			try {
				unregisterReceiver(locationReceiver);
				Log.i("maxis", "Unregistered locaiton data");
			} catch (Exception e) {
				// AnalyticsHelper.onError(FlurryEventsConstants.LOCATION_RECIEVER_UNREGISTER_ERR,
				// "HomeActivity : " +
				// AppConstants.LOCATION_RECEIVER_UNREGISTER_ERROR_MSG, e);
			}
			locationReceiver = null;
		}
		if (mLocationServiceIntent != null) {
			try {
				stopService(mLocationServiceIntent);
			} catch (Exception e) {
				AnalyticsHelper
						.onError(
								FlurryEventsConstants.LOCATION_SERVICE_STOP_ERR,
								"HomeActivity : "
										+ AppConstants.LOCATION_SERVICE_NOT_STOP_ERROR_MSG,
								e);
			}
		}
		super.onDestroy();
	}

	private void locationUpdate() {
		/*
		 * try { unregisterReceiver(locationReceiver); } catch (Exception e) { }
		 */

		double mLongitude = AppSharedPreference.getFloat(
				AppSharedPreference.LONGITUDE, 0.0f, getApplication());
		double mLattitude = AppSharedPreference.getFloat(
				AppSharedPreference.LATITUDE, 0.0f, getApplication());
		if (mLongitude == 0.0f && mLattitude == 0.0f) {
			mLongitude = AppSharedPreference.getFloat(
					AppSharedPreference.LONGITUDE_N, 0.0f, getApplication());
			mLattitude = AppSharedPreference.getFloat(
					AppSharedPreference.LATITUDE_N, 0.0f, getApplication());
		}
		Log.i("maxis", "HOME SCREEN: " + mLattitude + " , " + mLongitude);
		if (mLongitude != 0.0f && mLattitude != 0.0f) {
			GPS_Data.setLatitude(mLattitude);
			GPS_Data.setLongitude(mLongitude);
		}
		// Toast.makeText(HomeActivity.this, "location updated on screen " +
		// mLattitude + " , " + mLongitude, Toast.LENGTH_LONG).show();
	}

	private void setCurrrentLocation() {
		try {
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			String provider = locationManager.getBestProvider(new Criteria(),
					true);
			if (null != provider) {
				Location locations = locationManager
						.getLastKnownLocation(provider);
				List<String> providerList = locationManager.getAllProviders();
				if (null != locations && null != providerList
						&& providerList.size() > 0) {
					double latitude = locations.getLatitude();
					double longitude = locations.getLongitude();
					if (latitude != 0 && longitude != 0) {
						GPS_Data.setLatitude(latitude);
						GPS_Data.setLongitude(longitude);
						Geocoder geoCoder = new Geocoder(getBaseContext(),
								Locale.getDefault());
						try {
							List<Address> addresses = geoCoder.getFromLocation(
									GPS_Data.getLatitude(),
									GPS_Data.getLongitude(), 1);

							if (addresses.size() > 0) {
								System.out.println(addresses.get(0)
										.getLocality());
								System.out.println(addresses.get(0)
										.getCountryName());
								countryName = addresses.get(0).getCountryName();

							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
			if (GPS_Data.getLatitude() == 0 && GPS_Data.getLongitude() == 0) {
				if (null != provider) {
					Location locations = locationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					List<String> providerList = locationManager
							.getAllProviders();
					if (null != locations && null != providerList
							&& providerList.size() > 0) {
						double latitude = locations.getLatitude();
						double longitude = locations.getLongitude();
						if (latitude != 0 && longitude != 0) {
							GPS_Data.setLatitude(latitude);
							GPS_Data.setLongitude(longitude);
						}
					}
				}

			}

		} catch (Exception e) {
			AnalyticsHelper.onError(
					FlurryEventsConstants.CURRENT_LOCATION_NOT_SET_ERR,
					"HomeActivity : "
							+ AppConstants.CURRENT_LOCATION_NOT_SET_ERROR_MSG,
					e);
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		}
		Message message = (Message) screenData;
		handler.sendMessage(message);
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.SUBCATEGORY_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				SubCategoryResponse categoriesResp = (SubCategoryResponse) msg.obj;

				Intent intent = new Intent(HomeActivity.this,
						SubCategoryListActivity.class);
				intent.putExtra(AppConstants.DATA_SUBCAT_RESPONSE,
						categoriesResp);
				startActivity(intent);
			}
			stopSppiner();
		} else if (msg.arg2 == Events.DEALCATEGORY_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				SubCategoryResponse categoriesResp = (SubCategoryResponse) msg.obj;

				// TODO TEMPORARY :: change for hot deals
				Intent intent = new Intent(HomeActivity.this,
						DealsActivity.class);
				intent.putExtra(AppConstants.DATA_SUBCAT_RESPONSE,
						categoriesResp);
				startActivity(intent);
			}
			stopSppiner();
		} else if (msg.arg2 == Events.FLIP_GALLERY_ITEM) {
			int index = mImgGallery.getSelectedItemPosition() + 1;
			index = index % (mImgGallery.getCount());
			Log.d("maxis", "gallery item " + index);
			if (index == 0)
				mImgGallery.setSelection(index, true);
			else
				mImgGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
		} else if (msg.arg2 == Events.CITY_LISTING) {
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
				Intent intent = new Intent(HomeActivity.this,
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
				Intent intent = new Intent(HomeActivity.this,
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
		}
	}

	@Override
	public Activity getMyActivityReference() {
		// TODO Auto-generated method stub
		return null;
	}

	private void showDealListing(CategoryGroup cat) {
		CombindListingController listingController = new CombindListingController(
				HomeActivity.this, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
		CombinedListRequest newListReq = new CombinedListRequest(
				HomeActivity.this);
		newListReq.setBySearch(false);
		newListReq.setCompanyListing(false);
		newListReq.setKeywordOrCategoryId("-1");
		newListReq.setLatitude(GPS_Data.getLatitude());
		newListReq.setLongitude(GPS_Data.getLongitude());
		newListReq.setCategoryTitle(cat.getCategoryTitle());
		newListReq.setParentThumbUrl(cat.getThumbUrl());
		newListReq.setGroupActionType(cat.getmGroupActionType());
		newListReq.setGroupType(cat.getMgroupType());
		setRequest(newListReq);
		startSppiner();
		listingController.requestService(newListReq);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mainSearchButton:
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			String JSON_EXTRA = jsonForSearch();
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			break;
		case R.id.show_profile_icon:
			onProfileClick();
			break;
		case R.id.footer_facebook_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.FACEBOOK_CLICK);
			checkPreferenceAndOpenBrowser(AppConstants.FB_PAGE_URL);
			break;
		case R.id.footer_twitterIcon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.TWITTER_CLICK);
			checkPreferenceAndOpenBrowser(AppConstants.TWITTER_PAGE_URL);
			break;
/*		case R.id.btn_photo_contest:
			AnalyticsHelper.logEvent(FlurryEventsConstants.PHOTO_CONTEST_CLICK);
			startActivity(new Intent(HomeActivity.this,
					ContestHomeActivity.class));
			break;*/

		case R.id.logo:
			startActivity(new Intent(HomeActivity.this, GetItInfoActivity.class));
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
				Intent cityIntent = new Intent(HomeActivity.this,
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
				Intent localityIntent = new Intent(HomeActivity.this,
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

		case R.id.box:
			if (!footerVisible) {
				box.setImageResource(R.drawable.box_down);
				seprator.setVisibility(View.VISIBLE);
				followus.setVisibility(View.VISIBLE);
				footerVisible = true;
			} else {
				box.setImageResource(R.drawable.upbox);
				seprator.setVisibility(View.GONE);
				followus.setVisibility(View.GONE);
				footerVisible = false;
			}
			break;

		default:
			break;
		}
	}

	protected void showTypeByCategoryScreen(String categoryId) {
		TypeByCategoryController tBCController = new TypeByCategoryController(
				HomeActivity.this, Events.TYPE_BY_CATEGORY_EVENT);
		startSppiner();
		tBCController.requestService(categoryId);
	}

	protected void showSubcategories(CategoryGroup cat) {
		SubCategoryController controller = new SubCategoryController(
				HomeActivity.this, Events.SUBCATEGORY_EVENT);
		controller.isForDeal = false;
		controller.requestService(cat);
		startSppiner();
	}

	protected void showDealcategories(CategoryGroup cat) {
		SubCategoryController controller = new SubCategoryController(
				HomeActivity.this, Events.DEALCATEGORY_EVENT);
		controller.isForDeal = true;
		controller.requestService(cat);
		startSppiner();
	}

	@Override
	protected void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK && reqCode == AppConstants.CITY_REQUEST) {
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
				&& reqCode == AppConstants.LOCALITY_REQUEST) {
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

			}

			else {
				return null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}
