package com.kelltontech.maxisgetit.ui.activities.matta;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.matta.MattaViewPagerAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.constants.matta.MattaConstants;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.model.matta.packages.detail.MattaPackageDetailResponse;
import com.kelltontech.maxisgetit.requests.matta.MattaPackageDetailRequest;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.ui.activities.AdvanceSelectCity;
import com.kelltontech.maxisgetit.ui.activities.AdvanceSelectLocalityActivity;
import com.kelltontech.maxisgetit.ui.activities.MaxisMainActivity;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class MattaPackageDetailActivity extends MaxisMainActivity implements OnClickListener {

	private ViewPager dealGallery;
	private MattaPackageDetailResponse mMattaPackageDetailResponse;
	private MattaPackageDetailRequest mMattaPackageDetailRequest;
	private LinearLayout circleIndicator;
	private int flipperVisibleItemPosition = 0;
	private ArrayList<String> imgPathList;

	private TextView mHeaderText;
	private TextView mDealTitle;
	private TextView highlightsTab;
	private TextView itineraryTab;
	private TextView contactsTab;
	private WebView highlightsDesc;
	private WebView itineraryDesc;
	private LinearLayout mSearchContainer, contactContainer;
	private EditText mSearchEditText;
	private ImageView mSearchBtn;
	private ImageView mProfileIconView;
	private ImageView mHeaderBackButton;
	private ImageView mHomeIconView;
	private ImageView mSearchToggler;

	private boolean isNearestOutlet = false;
	LinearLayout outLetsName;
	boolean viewAdded = false;
	//	private MaxisStore store;
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
	private boolean onTapNearestOutletEnable = false;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK
				&& requestCode == AppConstants.CITY_REQUEST) {
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

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AnalyticsHelper.logEvent(FlurryEventsConstants.APPLICATION_DEAL_DETAIL);
		setContentView(R.layout.activity_matta_package_detail);

		circleIndicator = (LinearLayout) findViewById(R.id.indicatorlinearlayout);
		Bundle bundle = getIntent().getExtras();
		mMattaPackageDetailResponse = (MattaPackageDetailResponse) bundle.getSerializable(MattaConstants.DATA_MATTA_PACKAGE_DETAIL_RESPONSE);
		mMattaPackageDetailRequest = (MattaPackageDetailRequest) bundle.getSerializable(MattaConstants.DATA_MATTA_PACKAGE_DETAIL_REQUEST);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		highlightsDesc = (WebView) findViewById(R.id.highlights_desc);
		WebSettings settings = highlightsDesc.getSettings();
		settings.setJavaScriptEnabled(true);
		highlightsDesc.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		highlightsDesc.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return overrideWebViewUrlLoading(url);
			}
			public void onPageFinished(WebView view, String url) { }
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) { }
		});

		itineraryDesc = (WebView) findViewById(R.id.itinerary_desc);
		WebSettings ItinerarySettings = itineraryDesc.getSettings();
		ItinerarySettings.setJavaScriptEnabled(true);
		itineraryDesc.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		itineraryDesc.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return overrideWebViewUrlLoading(url);
			}
			public void onPageFinished(WebView view, String url) { }
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) { }
		});
		itineraryDesc.setVisibility(View.GONE);

		//		nearestOutletLayout = (RelativeLayout) findViewById(R.id.nearestLayout);
		//		nearestOutLetTitle = (TextView) nearestOutletLayout.findViewById(R.id.outletName);
		//		nearestOutLetTitle.setTag(-1);
		//		nearestOutLetTitle.setOnClickListener(this);
		//		nearestOutLetAddress = (TextView) nearestOutletLayout.findViewById(R.id.outletAddress);
		//		nearestOutLetAddress.setTag(-1);
		//		nearestOutLetAddress.setOnClickListener(this);
		//		ImageView imageView = (ImageView) nearestOutletLayout.findViewById(R.id.imageIcon);
		//		imageView.setVisibility(View.VISIBLE);

		//		mapLayout = (LinearLayout) findViewById(R.id.mapLayout);
		//		mapLayout.setVisibility(View.VISIBLE);
		//		mapLayout.setOnClickListener(this);

		//		mapLabel = (TextView) findViewById(R.id.mapLabel);
		//		mapLabel.setVisibility(View.VISIBLE);
		//		view_map = (TextView) findViewById(R.id.viewOnMap_view);
		//		view_map.setOnClickListener(this);

		//		ImageView mapIcon = (ImageView) findViewById(R.id.mapIcon);
		//		mapIcon.setTag(-1);
		//		mapIcon.setOnClickListener(this);

		//		FrameLayout layout = (FrameLayout) nearestOutletLayout.findViewById(R.id.outletCountLayout);
		//		layout.setVisibility(View.GONE);

		//		outLetsList = (ListView) findViewById(R.id.outlets_list);

		//		outletCount = (TextView) findViewById(R.id.outlets_count);
		//		crossBtn = (ImageView) findViewById(R.id.cross_btn);
		//		viewOnMap = (TextView) findViewById(R.id.view_on_map);
		//		leftInfoLayout = (RelativeLayout) findViewById(R.id.leftOutletRltLayout);
		//		leftInfoLayout.setVisibility(View.GONE);

		//		rightLayout = (LinearLayout) findViewById(R.id.rightlayout);
		//		rightLayout.setVisibility(View.GONE);

		//		crossBtn.setOnClickListener(this);
		//		viewOnMap.setOnClickListener(this);

		//		if (mMattaPackageDetailResponse == null) {
		//			try {
		//				comp_id = bundle.getString(AppConstants.COMP_ID);
		//				String mCategoryid = getIntent().getStringExtra(
		//						AppConstants.CATEGORY_ID_KEY);
		//				CompanyDetailController controller = new CompanyDetailController(
		//						MattaPackageDetailActivity.this, Events.DEAL_DETAIL);
		//				DetailRequest detailRequest = new DetailRequest(
		//						MattaPackageDetailActivity.this, comp_id, getIntent()
		//						.getExtras().getBoolean(
		//								AppConstants.IS_DEAL_LIST), "");
		//
		//				startSppiner();
		//				controller.requestService(detailRequest);
		//			} catch (Exception e) {
		//				e.printStackTrace();
		//			}
		//		} else {
		//		comp_id = mMattaPackageDetailResponse.getCid();
		//		deal_id = mMattaPackageDetailResponse.getId();
		//		l3cat_id = mMattaPackageDetailResponse.getCatId();
		setdata();
		//		getOutLets();
		//		}

		advanceSearchLayout = (LinearLayout) findViewById(R.id.advanceSearch);
		advanceSearchLayout.setVisibility(View.GONE);

		upArrow = (ImageView) findViewById(R.id.upArrow);
		upArrow.setOnClickListener(this);

		currentCity = (TextView) findViewById(R.id.currentCity);
		currentLocality = (TextView) findViewById(R.id.currentLocality);
		currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCity + "</b>"));

		currentCity.setOnClickListener(this);
		currentLocality.setOnClickListener(this);

		mainSearchButton = (TextView) findViewById(R.id.mainSearchButton);
		mainSearchButton.setOnClickListener(this);

		wholeSearchBoxContainer = (LinearLayout) findViewById(R.id.whole_search_box_container);

		mSearchEditText.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (!isAdvanceSearchLayoutOpen) {
					isAdvanceSearchLayoutOpen = true;
					advanceSearchLayout.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});

		//		highlightsDesc.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View v) {
		//
		//				if (onTapNearestOutletEnable) {
		//					String catId = outLets.get(0).getCatid();
		//					String comp_id = outLets.get(0).getId();
		//
		//					Intent intent = new Intent(DealDetailActivity.this,
		//							CompanyDetailActivity.class);
		//
		//					Bundle bundle = new Bundle();
		//					bundle.putString(AppConstants.COMP_ID, comp_id);
		//					bundle.putString(AppConstants.GLOBAL_SEARCH_KEYWORD,
		//							mSearchKeyword);
		//					bundle.putBoolean(AppConstants.IS_DEAL_LIST, true);
		//					intent.putExtra(AppConstants.CATEGORY_ID_KEY, catId);
		//					intent.putExtras(bundle);
		//					startActivity(intent);
		//				}
		//			}
		//		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		AnalyticsHelper.trackSession(MattaPackageDetailActivity.this, MattaConstants.Matta_Package_Detail);
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.highlights:
			if (mMattaPackageDetailResponse.getResults().getPackage().getHighlights() != null 
			&& !StringUtil.isNullOrEmpty(mMattaPackageDetailResponse.getResults().getPackage().getHighlights().getValue())) {
				isNearestOutlet = false;
				onTapNearestOutletEnable = false;
				highlightsTab.setBackgroundColor(getResources().getColor(R.color.green));
				highlightsTab.setTextColor(Color.WHITE);
				itineraryTab.setBackgroundColor(Color.WHITE);
				itineraryTab.setTextColor(Color.BLACK);
				contactsTab.setBackgroundColor(Color.WHITE);
				contactsTab.setTextColor(Color.BLACK);
				highlightsDesc.setVisibility(View.VISIBLE);
				highlightsDesc.loadDataWithBaseURL("", mMattaPackageDetailResponse.getResults().getPackage().getHighlights().getValue(), "text/html", "UTF-8","");
				itineraryDesc.setVisibility(View.GONE);
			}  else {
				showInfoDialog(getResources().getString(R.string.no_result_found));
			}

			break;
		case R.id.itinerary:
			if (mMattaPackageDetailResponse.getResults().getPackage().getItinerary() != null 
			&& !StringUtil.isNullOrEmpty(mMattaPackageDetailResponse.getResults().getPackage().getItinerary().getValue())) {
				isNearestOutlet = true;
				highlightsTab.setBackgroundColor(Color.WHITE);
				highlightsTab.setTextColor(Color.BLACK);
				itineraryTab.setBackgroundColor(getResources().getColor(R.color.green));
				itineraryTab.setTextColor(Color.WHITE);
				contactsTab.setBackgroundColor(Color.WHITE);
				contactsTab.setTextColor(Color.BLACK);
				highlightsDesc.setVisibility(View.GONE);
				itineraryDesc.loadDataWithBaseURL("", mMattaPackageDetailResponse.getResults().getPackage().getItinerary().getValue(), "text/html", "UTF-8","");
				itineraryDesc.setVisibility(View.VISIBLE);
				onTapNearestOutletEnable = true;
			} else {
				onTapNearestOutletEnable = false;
				showInfoDialog(getResources().getString(R.string.no_result_found));
			}
			break;
		case R.id.contacts:
			onViewAllOutletClick();
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
			AnalyticsHelper
			.endTimedEvent(FlurryEventsConstants.APPLICATION_COMBINED_LIST);
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
				Intent cityIntent = new Intent(MattaPackageDetailActivity.this,
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
				Intent localityIntent = new Intent(MattaPackageDetailActivity.this,
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
		case R.id.call_us_btn:
			break;
		case R.id.locate_us_btn:
			break;
		default:
			break;
		}

	}

	private void onViewAllOutletClick() {
		isNearestOutlet = false;
		highlightsTab.setBackgroundColor(Color.WHITE);
		highlightsTab.setTextColor(Color.BLACK);
		itineraryTab.setBackgroundColor(Color.WHITE);
		itineraryTab.setTextColor(Color.BLACK);
		contactsTab.setBackgroundColor(getResources().getColor(R.color.green));
		contactsTab.setTextColor(Color.WHITE);
		// highlightsDesc.setText(termsNdcond);
		highlightsDesc.setVisibility(View.GONE);
		itineraryDesc.setVisibility(View.GONE);
		onTapNearestOutletEnable = false;
	}

	private void indicatorchange(int pos) {
		for (int i = 0; i < imgPathList.size(); i++) {
			circleIndicator.getChildAt(i).setBackgroundResource(R.drawable.circle_white);
		}
		circleIndicator.getChildAt(pos).setBackgroundResource(R.drawable.circle_blue);
	}

	private void addImage() {
		for (int i = 0; i < imgPathList.size(); i++) {
			ImageView image = new ImageView(this);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(0, 0, (int) (5 * getResources().getDisplayMetrics().density), 0);
			int padding = (int) (3 * getResources().getDisplayMetrics().density);
			image.setPadding(padding, padding, padding, padding);
			image.setLayoutParams(layoutParams);
			circleIndicator.addView(image, i);
		}
		indicatorchange(flipperVisibleItemPosition);
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE || event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
		} else if (event == Events.TYPE_BY_CATEGORY_EVENT || event == Events.SUBCATEGORY_EVENT) {
			handler.sendMessage((Message) screenData);
		} else if (event == Events.CITY_LISTING || event == Events.LOCALITY_LISTING) {
			Message message = (Message) screenData;
			handler.sendMessage(message);
			return;
		} else {
			System.out.println(screenData);
			Response response = (Response) screenData;
			Message message = new Message();
			message.arg2 = event;
			message.arg1 = 1;
			if (response.isError()) {
				message.obj = response.getErrorText();
			}
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
				Intent intent = new Intent(MattaPackageDetailActivity.this, AdvanceSelectCity.class);
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
				Intent intent = new Intent(MattaPackageDetailActivity.this,
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

	public void setdata() {
		contactContainer = (LinearLayout) findViewById(R.id.contacts_container);
		contactContainer.setVisibility(View.GONE);

		((TextView) findViewById(R.id.call_us_btn)).setOnClickListener(this);
		((TextView) findViewById(R.id.locate_us_btn)).setOnClickListener(this);

		mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);

		mHeaderText = (TextView) findViewById(R.id.header_title);
		mDealTitle = (TextView) findViewById(R.id.txt_deal_name);
		highlightsTab = (TextView) findViewById(R.id.highlights);
		if (!StringUtil.isNullOrEmpty(mMattaPackageDetailResponse.getResults().getPackage().getHighlights().getLabel())) {
			highlightsTab.setVisibility(View.VISIBLE);
			highlightsTab.setText(mMattaPackageDetailResponse.getResults().getPackage().getHighlights().getLabel());
		} else {
			highlightsTab.setVisibility(View.GONE);
		}


		itineraryTab = (TextView) findViewById(R.id.itinerary);
		contactsTab = (TextView) findViewById(R.id.contacts);

		if (!StringUtil.isNullOrEmpty(mMattaPackageDetailResponse.getResults().getPackage().getTitle())) {
			mHeaderText.setText(mMattaPackageDetailResponse.getResults().getPackage().getTitle());
			mDealTitle.setText(mMattaPackageDetailResponse.getResults().getPackage().getTitle());
		}

		if (!StringUtil.isNullOrEmpty(mMattaPackageDetailResponse.getResults().getPackage().getHighlights().getValue()))
			highlightsDesc.loadDataWithBaseURL("", mMattaPackageDetailResponse.getResults().getPackage().getHighlights().getValue(), "text/html", "UTF-8","");
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

		highlightsTab.setOnClickListener(this);
		itineraryTab.setOnClickListener(this);
		contactsTab.setOnClickListener(this);
		mSearchContainer.setOnClickListener(this);
		mSearchEditText.setOnClickListener(this);
		mSearchBtn.setOnClickListener(this);

		imgPathList = mMattaPackageDetailResponse.getResults().getPackage().getImages().getImage();

		dealGallery = (ViewPager) findViewById(R.id.dealtopbanner);

		dealGallery.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				dealGallery.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		if (imgPathList != null && imgPathList.size() > 0) {
			dealGallery.setVisibility(View.VISIBLE);
			MattaViewPagerAdapter pagerAdapter = new MattaViewPagerAdapter(getSupportFragmentManager(), imgPathList, this, MattaConstants.FLOW_FROM_MATTA_PACKAGE_DETAIL);
			if (imgPathList.size() > 1) {
				addImage();
				circleIndicator.setVisibility(View.VISIBLE);
			} else {
				circleIndicator.setVisibility(View.GONE);
			}
			dealGallery.setAdapter(pagerAdapter);
		} else {
			dealGallery.setVisibility(View.GONE);
			circleIndicator.setVisibility(View.GONE);
		}

		dealGallery.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				indicatorchange(position);
				flipperVisibleItemPosition = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				dealGallery.getParent()
				.requestDisallowInterceptTouchEvent(true);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

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
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void onPositiveDialogButton(int id) {
		if (id == CustomDialog.DATA_USAGE_DIALOG) {
		} else {
			super.onPositiveDialogButton(id);
		}
	}

	@Override
	public void onNegativeDialogbutton(int id) {
		if (id == CustomDialog.LOGIN_CONFIRMATION_DIALOG) {
			stopSppiner();
		} else if (id == CustomDialog.DELETE_CONFIRMATION_DIALOG) {
			stopSppiner();
		} else {
			super.onNegativeDialogbutton(id);
		}
	}

	public void viewFlipperTapped() {
		Intent intents = new Intent(MattaPackageDetailActivity.this, MattaPhotoSlideActivity.class);
		intents.putStringArrayListExtra("list", imgPathList);
		intents.putExtra("position", flipperVisibleItemPosition);
		startActivity(intents);
	}
}