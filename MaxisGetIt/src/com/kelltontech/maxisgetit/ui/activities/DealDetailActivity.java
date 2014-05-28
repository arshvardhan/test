package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.DealMapInfoWindowAdapter;
import com.kelltontech.maxisgetit.adapters.DealOutletsAdapter;
import com.kelltontech.maxisgetit.adapters.ViewPagerAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.CompanyDetailController;
import com.kelltontech.maxisgetit.controllers.DownloadDealController;
import com.kelltontech.maxisgetit.controllers.OutLetDetailtController;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.dao.IconUrl;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.dao.OutLet;
import com.kelltontech.maxisgetit.dao.OutLetDetails;
import com.kelltontech.maxisgetit.db.CityTable;
import com.kelltontech.maxisgetit.requests.CombinedListRequest;
import com.kelltontech.maxisgetit.requests.DetailRequest;
import com.kelltontech.maxisgetit.requests.DownloadDealReq;
import com.kelltontech.maxisgetit.requests.OutLetDetailRequest;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.ui.widgets.EllipsizingTextView;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class DealDetailActivity extends MaxisMainActivity implements
OnGlobalLayoutListener, OnClickListener {

	private ViewPager dealGallery;
	private CompanyDetail compDetailResponse;
	private LinearLayout circleIndicator;
	private int flipperVisibleItemPosition = 0;
	private ArrayList<IconUrl> imgPathList;

	private TextView mHeaderText;
	private TextView mDealTitle;
	private EllipsizingTextView validIn;
	private TextView validDate;
	private TextView aboutUs;
	private TextView nearOutLets;
	private TextView tNc;
	private TextView dealDesc;
	private TextView downloadDeal;
	private TextView viewAllOutlets;
	private LinearLayout mSearchContainer;
	private EditText mSearchEditText;
	private ImageView mSearchBtn;
	private ImageView mProfileIconView;
	private ImageView mHeaderBackButton;
	private ImageView mHomeIconView;
	private ImageView mSearchToggler;
	private CombinedListRequest mClRequest;
	// ArrayList<NearOutLets> outLets;
	private DealMapInfoWindowAdapter mMapInfoWindowAdapter;

	private GoogleMap mMap;
	// private Marker sourceMarker;
	private String comp_id;
	private String deal_id;
	private String l3cat_id;
	private OutLetDetails outLetResponse;
	ArrayList<OutLet> outLets = new ArrayList<OutLet>();
	private String termsNdcond;
	private boolean isNearestOutlet = false;
	LinearLayout outLetsName;
	private final int textId = 1104;
	private int tagId = 0;
	boolean viewAdded = false;
	private MaxisStore store;
	String userNo;
	String userName;

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

	private View seprator_outlet;

	private RelativeLayout nearestOutletLayout;
	private ListView outLetsList;
	private TextView nearestOutLetTitle;
	private TextView nearestOutLetAddress;
	private int index;

	private TextView outletCount;
	private ImageView crossBtn;
	private TextView viewOnMap;
	private RelativeLayout leftInfoLayout;
	private LinearLayout rightLayout;
	private OutLetDetailRequest detailRequest;
	private LinearLayout mapLayout;
	private TextView view_map;
	private TextView mapLabel;
	private TextView mMoreDesc;
	//	private boolean mIsCollapsedView = true;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				userName = bundle.getString("name");
				userNo = bundle.getString("phoneNo");
				dealDownload();
			}
		} else if (resultCode == RESULT_OK
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AnalyticsHelper.logEvent(FlurryEventsConstants.APPLICATION_DEAL_DETAIL);
		setContentView(R.layout.activity_deal_detail);

		circleIndicator = (LinearLayout) findViewById(R.id.indicatorlinearlayout);
		store = MaxisStore.getStore(this);
		outLetsName = (LinearLayout) findViewById(R.id.outlets_names);
		Bundle bundle = getIntent().getExtras();
		compDetailResponse = bundle
				.getParcelable(AppConstants.COMP_DETAIL_DATA);
		mClRequest = bundle.getParcelable(AppConstants.DATA_LIST_REQUEST);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		seprator_outlet = (View) findViewById(R.id.outlet_seprator);
		dealDesc = (TextView) findViewById(R.id.deal_desc);
		nearestOutletLayout = (RelativeLayout) findViewById(R.id.nearestLayout);
		nearestOutLetTitle = (TextView) nearestOutletLayout
				.findViewById(R.id.outletName);
		nearestOutLetTitle.setTag(-1);
		nearestOutLetTitle.setOnClickListener(this);
		nearestOutLetAddress = (TextView) nearestOutletLayout
				.findViewById(R.id.outletAddress);
		nearestOutLetAddress.setTag(-1);
		nearestOutLetAddress.setOnClickListener(this);
		ImageView imageView = (ImageView) nearestOutletLayout
				.findViewById(R.id.imageIcon);
		imageView.setVisibility(View.VISIBLE);

		mapLayout = (LinearLayout) findViewById(R.id.mapLayout);
		mapLayout.setVisibility(View.VISIBLE);
		mapLayout.setOnClickListener(this);

		mapLabel = (TextView) findViewById(R.id.mapLabel);
		mapLabel.setVisibility(View.VISIBLE);
		view_map = (TextView) findViewById(R.id.viewOnMap_view);
		view_map.setOnClickListener(this);

		ImageView mapIcon = (ImageView) findViewById(R.id.mapIcon);
		mapIcon.setTag(-1);
		mapIcon.setOnClickListener(this);

		FrameLayout layout = (FrameLayout) nearestOutletLayout
				.findViewById(R.id.outletCountLayout);
		layout.setVisibility(View.GONE);

		outLetsList = (ListView) findViewById(R.id.outlets_list);

		outletCount = (TextView) findViewById(R.id.outlets_count);
		crossBtn = (ImageView) findViewById(R.id.cross_btn);
		viewOnMap = (TextView) findViewById(R.id.view_on_map);
		leftInfoLayout = (RelativeLayout) findViewById(R.id.leftOutletRltLayout);
		leftInfoLayout.setVisibility(View.GONE);

		rightLayout = (LinearLayout) findViewById(R.id.rightlayout);
		rightLayout.setVisibility(View.GONE);

		crossBtn.setOnClickListener(this);
		viewOnMap.setOnClickListener(this);

		if (compDetailResponse == null) {
			try {
				comp_id = bundle.getString(AppConstants.COMP_ID);
				String mCategoryid = getIntent().getStringExtra(
						AppConstants.CATEGORY_ID_KEY);
				CompanyDetailController controller = new CompanyDetailController(
						DealDetailActivity.this, Events.DEAL_DETAIL);
				DetailRequest detailRequest = new DetailRequest(
						DealDetailActivity.this, comp_id, getIntent()
						.getExtras().getBoolean(
								AppConstants.IS_DEAL_LIST), "");

				startSppiner();
				controller.requestService(detailRequest);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			comp_id = compDetailResponse.getCid();
			deal_id = compDetailResponse.getId();
			l3cat_id = compDetailResponse.getCatId();
			setdata();
			getOutLets();
		}

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

		dealDesc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (onTapNearestOutletEnable) {
					String catId = outLets.get(0).getCatid();
					String comp_id = outLets.get(0).getId();

					Intent intent = new Intent(DealDetailActivity.this,
							CompanyDetailActivity.class);

					Bundle bundle = new Bundle();
					bundle.putString(AppConstants.COMP_ID, comp_id);
					bundle.putString(AppConstants.GLOBAL_SEARCH_KEYWORD,
							mSearchKeyword);
					bundle.putBoolean(AppConstants.IS_DEAL_LIST, true);
					intent.putExtra(AppConstants.CATEGORY_ID_KEY, catId);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});

		final com.kelltontech.maxisgetit.ui.widgets.CustomScrollView mainScrollView = (com.kelltontech.maxisgetit.ui.widgets.CustomScrollView) findViewById(R.id.scroll_view);
		// View transparentImageView = (View) findViewById(R.id.trans_img);
		//
		// transparentImageView.setOnTouchListener(new View.OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// int action = event.getAction();
		// switch (action) {
		// case MotionEvent.ACTION_DOWN:
		// // Disallow ScrollView to intercept touch events.
		// mainScrollView.requestDisallowInterceptTouchEvent(true);
		// // Disable touch on transparent view
		// return false;
		//
		// case MotionEvent.ACTION_UP:
		// // Allow ScrollView to intercept touch events.
		// mainScrollView.requestDisallowInterceptTouchEvent(false);
		// return true;
		//
		// case MotionEvent.ACTION_MOVE:
		// mainScrollView.requestDisallowInterceptTouchEvent(true);
		// return false;
		//
		// default:
		// return true;
		// }
		// }
		// });

		outLetsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {

				String catId = outLets.get(pos).getCatid();
				String comp_id = outLets.get(pos).getId();

				Intent intent = new Intent(DealDetailActivity.this,
						CompanyDetailActivity.class);

				Bundle bundle = new Bundle();
				bundle.putString(AppConstants.COMP_ID, comp_id);
				bundle.putString(AppConstants.GLOBAL_SEARCH_KEYWORD,
						mSearchKeyword);
				bundle.putBoolean(AppConstants.IS_DEAL_LIST, true);
				intent.putExtra(AppConstants.CATEGORY_ID_KEY, catId);
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});

		dealGallery.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				dealGallery.getParent()
				.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		AnalyticsHelper.trackSession(DealDetailActivity.this, AppConstants.Deal_Detail);
	}

	@Override
	public Activity getMyActivityReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about_deal:
			isNearestOutlet = false;
			onTapNearestOutletEnable = false;
			aboutUs.setBackgroundColor(getResources().getColor(R.color.green));
			aboutUs.setTextColor(Color.WHITE);
			nearOutLets.setBackgroundColor(Color.WHITE);
			nearOutLets.setTextColor(Color.BLACK);
			viewAllOutlets.setBackgroundColor(Color.WHITE);
			viewAllOutlets.setTextColor(Color.BLACK);
			outLetsName.setVisibility(View.GONE);
			seprator_outlet.setVisibility(View.GONE);
			nearestOutletLayout.setVisibility(View.GONE);
			dealDesc.setVisibility(View.VISIBLE);
			mapLayout.setVisibility(View.VISIBLE);

			leftInfoLayout.setVisibility(View.GONE);
			rightLayout.setVisibility(View.GONE);
			mapLabel.setVisibility(View.VISIBLE);

			if (!StringUtil.isNullOrEmpty(compDetailResponse.getDescription()))
				dealDesc.setText(Html.fromHtml(compDetailResponse
						.getDescription()));

			break;
		case R.id.nearest_outlet:
			isNearestOutlet = true;
			outLetsName.setVisibility(View.GONE);
			aboutUs.setBackgroundColor(Color.WHITE);
			aboutUs.setTextColor(Color.BLACK);
			nearOutLets.setBackgroundColor(getResources().getColor(
					R.color.green));
			nearOutLets.setTextColor(Color.WHITE);
			viewAllOutlets.setBackgroundColor(Color.WHITE);
			viewAllOutlets.setTextColor(Color.BLACK);
			seprator_outlet.setVisibility(View.GONE);
			nearestOutletLayout.setVisibility(View.VISIBLE);
			nearestOutletLayout.setBackgroundColor(getResources().getColor(
					R.color.light_green));
			dealDesc.setVisibility(View.GONE);
			mapLayout.setVisibility(View.VISIBLE);

			leftInfoLayout.setVisibility(View.GONE);
			rightLayout.setVisibility(View.GONE);
			mapLabel.setVisibility(View.VISIBLE);
			// setUpMapIfNeeded();
			if (outLets != null && outLets.size() > 0) {
				if (!StringUtil.isNullOrEmpty(outLetResponse.getOutlet().get(0)
						.getAddress())) {
					dealDesc.setText(outLets.get(0).getTitle() + ", "
							+ outLets.get(0).getAddress());
					onTapNearestOutletEnable = true;
				}
			} else {
				dealDesc.setText("Nearest OutLet Address not available .");
				onTapNearestOutletEnable = false;
			}
			break;

		case R.id.deal_all_outlet:
			onViewAllOutletClick();
			//			isNearestOutlet = false;
			//			outLetsName.setVisibility(View.VISIBLE);
			//			aboutUs.setBackgroundColor(Color.WHITE);
			//			aboutUs.setTextColor(Color.BLACK);
			//			nearOutLets.setBackgroundColor(Color.WHITE);
			//			nearOutLets.setTextColor(Color.BLACK);
			//			viewAllOutlets.setBackgroundColor(getResources().getColor(
			//					R.color.green));
			//			viewAllOutlets.setTextColor(Color.WHITE);
			//
			//			if (outLetResponse !=null) {
			//				if (Integer.parseInt(outLetResponse.getTotal_records()) > 10)
			//					leftInfoLayout.setVisibility(View.VISIBLE);
			//				else
			//					leftInfoLayout.setVisibility(View.GONE);
			//			}
			//			rightLayout.setVisibility(View.VISIBLE);
			//			// dealDesc.setText(termsNdcond);
			//			dealDesc.setVisibility(View.GONE);
			//			mapLayout.setVisibility(View.GONE);
			//			nearestOutletLayout.setVisibility(View.GONE);
			//			mapLabel.setVisibility(View.GONE);
			//			// nearestOutletLayout.setBackgroundColor(getResources().getColor(
			//			// R.color.light_green));
			//
			//			onTapNearestOutletEnable = false;
			//			seprator_outlet.setVisibility(View.GONE);
			//			try {
			//				// setUpMapIfNeeded();
			//				addOutLets();
			//			} catch (Exception e) {
			//				e.printStackTrace();
			//			}

			break;

		case R.id.tnc:
			// TODO
			Intent tncintent = new Intent(DealDetailActivity.this,
					TermsAndConditionActivity.class);
			tncintent.putExtra(AppConstants.TNC_FROM,
					AppConstants.TNC_FROM_DEAL);
			tncintent.putExtra("TnC_Data", termsNdcond);
			startActivity(tncintent);

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

		case R.id.deal_download:
			// TODO
			AnalyticsHelper.logEvent(FlurryEventsConstants.GET_INFO_CLICK);
			getDownloadDetails();
			break;

		case R.id.outletName:
		case R.id.outletAddress:
			index = Integer.parseInt(v.getTag().toString());

			if (index == -1) {
				index = 0;
			}

			String catId = outLets.get(index).getCatid();
			String comp_id = outLets.get(index).getId();

			Intent intent = new Intent(DealDetailActivity.this,
					CompanyDetailActivity.class);

			Bundle bundle = new Bundle();
			bundle.putString(AppConstants.COMP_ID, comp_id);
			bundle.putString(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
			bundle.putBoolean(AppConstants.IS_DEAL_LIST, true);
			intent.putExtra(AppConstants.CATEGORY_ID_KEY, catId);
			intent.putExtras(bundle);
			startActivity(intent);

			break;
		case R.id.mapIcon:
			index = Integer.parseInt(v.getTag().toString());
			if (index == -1) {
				index = 0;
			}
			showMap(index);

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
				Intent cityIntent = new Intent(DealDetailActivity.this,
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
				Intent localityIntent = new Intent(DealDetailActivity.this,
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

		case R.id.cross_btn:
			leftInfoLayout.setVisibility(View.GONE);
			break;

		case R.id.view_on_map:
			index = -1;
			showMap(index);
			break;
		case R.id.mapLayout:
		case R.id.viewOnMap_view:

			index = 0;
			showMap(index);


			break;
		case R.id.more:
			onViewAllOutletClick();
			break;
		default:
			break;
		}

	}

	private void onViewAllOutletClick() {
		isNearestOutlet = false;
		outLetsName.setVisibility(View.VISIBLE);
		aboutUs.setBackgroundColor(Color.WHITE);
		aboutUs.setTextColor(Color.BLACK);
		nearOutLets.setBackgroundColor(Color.WHITE);
		nearOutLets.setTextColor(Color.BLACK);
		viewAllOutlets.setBackgroundColor(getResources().getColor(
				R.color.green));
		viewAllOutlets.setTextColor(Color.WHITE);

		if (outLetResponse !=null) {
			if (Integer.parseInt(outLetResponse.getTotal_records()) > 10)
				leftInfoLayout.setVisibility(View.VISIBLE);
			else
				leftInfoLayout.setVisibility(View.GONE);
		}
		rightLayout.setVisibility(View.VISIBLE);
		// dealDesc.setText(termsNdcond);
		dealDesc.setVisibility(View.GONE);
		mapLayout.setVisibility(View.GONE);
		nearestOutletLayout.setVisibility(View.GONE);
		mapLabel.setVisibility(View.GONE);
		// nearestOutletLayout.setBackgroundColor(getResources().getColor(
		// R.color.light_green));

		onTapNearestOutletEnable = false;
		seprator_outlet.setVisibility(View.GONE);
		try {
			// setUpMapIfNeeded();
			addOutLets();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void indicatorchange(int pos) {
		for (int i = 0; i < imgPathList.size(); i++) {
			circleIndicator.getChildAt(i).setBackgroundResource(
					R.drawable.circle_white);
		}
		circleIndicator.getChildAt(pos).setBackgroundResource(
				R.drawable.circle_blue);
	}

	private void addImage() {
		Log.e("manish", "inside add");

		// circleIndicator = (LinearLayout)
		// findViewById(R.id.indicatorlinearlayout);

		for (int i = 0; i < imgPathList.size(); i++) {
			ImageView image = new ImageView(this);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(0, 0, (int) (5 * getResources()
					.getDisplayMetrics().density), 0);
			int padding = (int) (3 * getResources().getDisplayMetrics().density);
			image.setPadding(padding, padding, padding, padding);
			image.setLayoutParams(layoutParams);

			circleIndicator.addView(image, i);

		}
		indicatorchange(flipperVisibleItemPosition);
	}

	@Override
	public void onGlobalLayout() {
		// TODO Auto-generated method stub
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
			for (int i = 0; i < outLets.size(); i++) {

				if (StringUtil.isNullOrEmpty(outLets.get(i).getLat())
						|| StringUtil.isNullOrEmpty(outLets.get(i).getLat())) {

				} else {
					toPosition = new LatLng(Double.parseDouble(outLets.get(i)
							.getLat()), Double.parseDouble(outLets.get(i)
									.getLongt()));
					mMap.addMarker(new MarkerOptions()
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.map_company_marker))
							.position(toPosition)
							.title(outLets.get(i).getTitle())
							.snippet(getSnippet(outLets.get(i))));
					builder.include(toPosition);
				}
			}

			nearestPosition = new LatLng(Double.parseDouble(outLets.get(0)
					.getLat()), Double.parseDouble(outLets.get(0).getLongt()));
		}
		if (nearestPosition == null) {
			nearestPosition = fromPosition;
		}
		LatLngBounds bounds = builder.build();
		int padding = 50; // offset from edges of the map in pixels
		// CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,
		// padding);
		CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(nearestPosition,
				14.0f);

		mMap.animateCamera(cu);

		final CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(nearestPosition) // Sets the center of the map to
		// Mountain
		// View
		.zoom(14) // Sets the zoom
		.bearing(90) // Sets the orientation of the camera to east
		.tilt(30) // Sets the tilt of the camera to 30 degrees
		.build();
		// mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		if (outLets != null && outLets.size() > 0) {
			if (isNearestOutlet) {
				mMapInfoWindowAdapter = new DealMapInfoWindowAdapter(this);
				ArrayList<OutLet> nearestOutlet = new ArrayList<OutLet>();

				nearestOutlet.add(outLets.get(0));
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

					mCurrentCompId = marker.getSnippet().split(
							AppConstants.SPLIT_STRING)[0];
					OutLet outLet = mMapInfoWindowAdapter
							.getValue(mCurrentCompId);
					Intent intent = new Intent(DealDetailActivity.this,
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

	public void getOutLets() {
		try {

			OutLetDetailtController detailtController = new OutLetDetailtController(
					DealDetailActivity.this, Events.OUTLET_DETAIL);
			detailRequest = new OutLetDetailRequest();

			detailRequest.setComp_id(comp_id);
			detailRequest.setDeal_id(deal_id);
			detailRequest.setL3cat_id(l3cat_id);
			startSppiner();
			detailtController.requestService(detailRequest);
		} catch (Exception e) {
			// TODO: handle exception
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
		if (msg.arg2 == Events.OUTLET_DETAIL) {
			try {
				if (msg.arg1 == 1) {
					showInfoDialog((String) msg.obj);
				} else {
					outLetResponse = (OutLetDetails) msg.obj;
					outLets = outLetResponse.getOutlet();
					if (outLets != null && outLets.size() > 0) {
						nearestOutLetTitle.setText(outLets.get(0).getTitle());
						nearestOutLetAddress.setText(outLets.get(0)
								.getAddress());
						// leftInfoLayout.setVisibility(View.VISIBLE);
						// TODO
						outletCount.setText(outLetResponse.getTotal_records() + " " + "Outlets");
					}
					// setUpMapIfNeeded();
				}
				stopSppiner();
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else if (msg.arg2 == Events.DOWNLOAD_DEAL) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else if (msg.arg1 == 0) {
				MaxisResponse genResp = (MaxisResponse) msg.obj;
				if (compDetailResponse.getSource().equalsIgnoreCase("GROUPON")) {
					String url = compDetailResponse.getDealDetailUrl();

					Intent browserIntent = new Intent(Intent.ACTION_VIEW,
							Uri.parse(url));
					startActivity(browserIntent);
				} else {
					showInfoDialog(getString(R.string.download_deal));
				}
			}
			stopSppiner();
		} else if (msg.arg2 == Events.DEAL_DETAIL) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				compDetailResponse = (CompanyDetail) msg.obj;
				if (!StringUtil.isNullOrEmpty(compDetailResponse.getId())) {
					deal_id = compDetailResponse.getId();
					comp_id = compDetailResponse.getCid();
					l3cat_id = compDetailResponse.getCatId();
					setdata();
					getOutLets();
					// intent.putExtra(AppConstants.THUMB_URL,
					// mCategoryThumbUrl);
					// intent.putExtra(AppConstants.IS_DEAL_LIST,
					// !mClRequest.isCompanyListing());
				} else {
					showInfoDialog(getResources().getString(
							R.string.no_result_found));
				}
			}
			// stopSppiner();
		} else if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
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
				Intent intent = new Intent(DealDetailActivity.this,
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
				Intent intent = new Intent(DealDetailActivity.this,
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

	public void getDownloadDetails() {

		if (store.isLoogedInUser()) {
			userNo = "60" + store.getUserMobileNumberToDispaly();
			userName = store.getUserName();
			dealDownload();

		} else {
			userNo = store.getAuthMobileNumber();
			userName = "";
			dealDownload();
			// Intent intent = new Intent(getApplicationContext(),
			// DealForm.class);
			// startActivityForResult(intent, 2);

		}

	}

	public void setdata() {
		termsNdcond = compDetailResponse.getTermsNdCondition();
		// outLets = compDetailResponse.getNearoutlets();

		mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);

		mHeaderText = (TextView) findViewById(R.id.header_title);
		mDealTitle = (TextView) findViewById(R.id.txt_deal_name);
		validIn = (EllipsizingTextView) findViewById(R.id.validin);
		validIn.setMaxLines(1);
		validDate = (TextView) findViewById(R.id.validdate);
		aboutUs = (TextView) findViewById(R.id.about_deal);
		nearOutLets = (TextView) findViewById(R.id.nearest_outlet);
		tNc = (TextView) findViewById(R.id.tnc);

		// dealDownload = (Button)findViewById(R.id.);
		downloadDeal = (TextView) findViewById(R.id.deal_download);
		viewAllOutlets = (TextView) findViewById(R.id.deal_all_outlet);

		if (compDetailResponse.getSource().equalsIgnoreCase("GROUPON")) {
			downloadDeal.setText("Buy Now");
		}
		downloadDeal.setOnClickListener(this);
		viewAllOutlets.setOnClickListener(this);

		if (!StringUtil.isNullOrEmpty(compDetailResponse.getTitle()))
			mHeaderText.setText(compDetailResponse.getTitle());

		if (!StringUtil.isNullOrEmpty(compDetailResponse.getTitle()))
			mDealTitle.setText(compDetailResponse.getTitle());

		if (!StringUtil.isNullOrEmpty(compDetailResponse.getValidIn()))
			validIn.setText(compDetailResponse.getValidIn());
		mMoreDesc = (TextView) findViewById(R.id.more);
		mMoreDesc.setText(Html.fromHtml("<u>" + getResources().getString(R.string.more) + "</u>"));
		mMoreDesc.setOnClickListener(this);
//		if (validIn.isEllipsized()) {
//			mMoreDesc.setVisibility(View.VISIBLE);
//		} else {
//			mMoreDesc.setVisibility(View.INVISIBLE);
//		}
		if (compDetailResponse.getValidIn().length() > 25) {
			mMoreDesc.setVisibility(View.VISIBLE);
		} else {
			mMoreDesc.setVisibility(View.INVISIBLE);
		}
		if (!StringUtil.isNullOrEmpty(compDetailResponse.getValidDate()))
			validDate.setText(compDetailResponse.getValidDate());

		if (!StringUtil.isNullOrEmpty(compDetailResponse.getDescription()))
			dealDesc.setText(Html.fromHtml(compDetailResponse.getDescription()));

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

		aboutUs.setOnClickListener(this);
		tNc.setOnClickListener(this);
		nearOutLets.setOnClickListener(this);
		mSearchContainer.setOnClickListener(this);
		mSearchEditText.setOnClickListener(this);
		mSearchBtn.setOnClickListener(this);

		imgPathList = compDetailResponse.getIconUrl();

		dealGallery = (ViewPager) findViewById(R.id.dealtopbanner);
		if (imgPathList != null && imgPathList.size() > 0) {
			dealGallery.setVisibility(View.VISIBLE);
			ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(
					getSupportFragmentManager(), imgPathList, this,
					"DealDetail");
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

		// getOutLets();
	}

	public void addOutLets() {

		if (outLets.size() > 0) {
			// OutLetsName
			// LayoutInflater inflater = (LayoutInflater)
			// getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// View view = inflater.inflate(
			// R.layout.offerlistheader, null, true);
			seprator_outlet.setVisibility(View.GONE);
			if (!viewAdded) {
				viewAdded = true;

				for (OutLet outlets : outLets) {

					/*
					 * if (tagId <= 4) {
					 * 
					 * LayoutInflater inflater = (LayoutInflater) this
					 * .getSystemService(Context.LAYOUT_INFLATER_SERVICE); View
					 * outletRow = inflater.inflate( R.layout.outlet_list_item,
					 * null);
					 * 
					 * // TextView name = new TextView(this); //
					 * LinearLayout.LayoutParams layoutParams = //
					 * (LinearLayout.LayoutParams) name // .getLayoutParams();
					 * // // name.setPadding(10, 10, 10, 10); // //
					 * name.setTextSize(, size) // //
					 * name.setCompoundDrawablesWithIntrinsicBounds( // //
					 * R.drawable.circle_blue, 0, 0, 0); // int count = tagId +
					 * 1; //
					 * name.setText(Html.fromHtml("<b><font color='black'>" // +
					 * count + "</font></b>" + "  " // + "<font color='blue'>" +
					 * title.getTitle() // + "</font>")); // //
					 * name.setTag(tagId); // name.setOnClickListener(this); //
					 * name.setId(textId);
					 * 
					 * TextView dealTitle = (TextView) outletRow
					 * .findViewById(R.id.outletName); TextView dealAddress =
					 * (TextView) outletRow .findViewById(R.id.outletAddress);
					 * ImageView mapIcon = (ImageView) outletRow
					 * .findViewById(R.id.mapIcon); TextView outletCount =
					 * (TextView) outletRow .findViewById(R.id.outletCount);
					 * 
					 * dealTitle.setText(outlets.getTitle());
					 * dealAddress.setText(outlets.getAddress());
					 * outletCount.setText(tagId + 1 + "");
					 * outLetsName.addView(outletRow); dealTitle.setTag(tagId);
					 * dealAddress.setTag(tagId); mapIcon.setTag(tagId);
					 * tagId++;
					 * 
					 * mapIcon.setOnClickListener(this);
					 * dealTitle.setOnClickListener(this);
					 * dealAddress.setOnClickListener(this); } else { break; } }
					 */
					Log.e("manish",
							":"
									+ Integer.parseInt(outLetResponse
											.getTotal_records()));

					DealOutletsAdapter adapter = new DealOutletsAdapter(this,
							true, Integer.parseInt(outLetResponse
									.getTotal_records()));
					adapter.setData(outLets);
					outLetsList.setAdapter(adapter);
					if (outLets.size() > 1) {
						android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) outLetsName
								.getLayoutParams();
						lp.height = (getWindowManager().getDefaultDisplay()
								.getHeight() / 2);
						outLetsName.setLayoutParams(lp);
					} else {
						android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) outLetsName
								.getLayoutParams();
						lp.height = (220);
						outLetsName.setLayoutParams(lp);
					}
				}

				outLetsName.setVisibility(View.VISIBLE);
			} else {
				outLetsName.setVisibility(View.VISIBLE);
			}

		}
	}

	public void dealDownload() {
		DownloadDealReq dealReq = new DownloadDealReq();
		dealReq.setName(userName);
		dealReq.setPhoneNo(userNo);
		dealReq.setDeal_id(deal_id);

		DownloadDealController downloadDealController = new DownloadDealController(
				DealDetailActivity.this, Events.DOWNLOAD_DEAL);
		downloadDealController.fromDeal = false;
		startSppiner();
		downloadDealController.requestService(dealReq);
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

	public void showMap(int index) {

		if (isDialogToBeShown()) {
			showConfirmationDialog(CustomDialog.DATA_USAGE_DIALOG,
					getResources().getString(R.string.cd_msg_data_usage));
		} else {
			if (isLocationAvailable()) {
//				if(index == -1) {
					redirectToMap();
//				} else {
//					showMapActivity();
//				}
			}
		}
	}

	public void viewAllOutlets() {
		// TODO
		Intent intent = new Intent(DealDetailActivity.this,
				ViewAllOutletsActivity.class);
		intent.putExtra(AppConstants.OUTLET_DETAIL_DATA, outLetResponse);
		intent.putExtra("totalCount",
				Integer.parseInt(outLetResponse.getTotal_records()));
		intent.putExtra("OutletRequest", detailRequest);
		intent.putExtra("deal_title", compDetailResponse.getTitle());
		startActivity(intent);

	}

	@Override
	public void onPositiveDialogButton(int id) {
		if (id == CustomDialog.DATA_USAGE_DIALOG) {

			if (isLocationAvailable()) {
//				if(index == -1) {
					redirectToMap();
//				} else {
//					showMapActivity();
//				}
			}
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

	public void redirectToMap() {
		Intent intent = new Intent(DealDetailActivity.this,
				ViewDealMapActivity.class);

		intent.putParcelableArrayListExtra(AppConstants.OUTLET_DATA, outLets);
		intent.putExtra("index", index);
		if (!StringUtil.isNullOrEmpty(compDetailResponse.getTitle()))
			intent.putExtra("DEAL_TITLE", compDetailResponse.getTitle());
		startActivity(intent);
	}

/*	private void showMapActivity() {
		if (isLocationAvailable()) {
			String url = "http://maps.google.com/maps?saddr="
					+ GPS_Data.getLatitude() + "," + GPS_Data.getLongitude()
					+ "&daddr=" + outLets.get(index).getLat() + ","
					+ outLets.get(index).getLongt() + "&mode=driving";
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse(url));
			intent.setClassName("com.google.android.apps.maps",
					"com.google.android.maps.MapsActivity");
			startActivity(intent);
		}
	}*/
	
	public void viewFlipperTapped() {
		Intent intents = new Intent(DealDetailActivity.this,
				PhotoSlideActivity.class);
		intents.putParcelableArrayListExtra("list", imgPathList);
		intents.putExtra("position", flipperVisibleItemPosition);
		/*intents.putExtra("ImageURL",
				imgPathList.get(flipperVisibleItemPosition).getDealIconUrl());*/
		startActivity(intents);
	}

	/*public void viewFlipperTapped() {
		Intent intents = new Intent(DealDetailActivity.this,
				CompanyDetailImageViewActivity.class);
		
		intents.putExtra("ImageURL",
				imgPathList.get(flipperVisibleItemPosition).getDealIconUrl());
		startActivity(intents);
	}*/
}
