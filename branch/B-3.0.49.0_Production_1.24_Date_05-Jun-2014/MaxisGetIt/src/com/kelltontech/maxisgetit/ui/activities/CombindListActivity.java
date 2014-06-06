package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.BannerViewAdapter;
import com.kelltontech.maxisgetit.adapters.CompanyListAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.CombindListingController;
import com.kelltontech.maxisgetit.controllers.CompanyDetailController;
import com.kelltontech.maxisgetit.controllers.RefineAttributeController;
import com.kelltontech.maxisgetit.controllers.SubCategoryController;
import com.kelltontech.maxisgetit.dao.CategoryGroup;
import com.kelltontech.maxisgetit.dao.CategoryRefine;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.CompanyDesc;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.SelectorDAO;
import com.kelltontech.maxisgetit.db.CityTable;
import com.kelltontech.maxisgetit.requests.CombinedListRequest;
import com.kelltontech.maxisgetit.requests.DetailRequest;
import com.kelltontech.maxisgetit.requests.RefineSearchRequest;
import com.kelltontech.maxisgetit.response.CompanyListResponse;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.response.RefineCategoryResponse;
import com.kelltontech.maxisgetit.response.RefineSelectorResponse;
import com.kelltontech.maxisgetit.response.SubCategoryResponse;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class CombindListActivity extends MaxisMainActivity {
	private ListView mCompanyList;
	private CompanyListAdapter mCompListAdapter;
	private TextView mRecordsFoundView;
	private TextView mRefineSearchView;
	// private TextView mMoreLinkView;
	private TextView mDealBtnView;
	private TextView mViewAllOnMap;
	// private Button[] mPaginationButtons;
	// private int[] mNavigationButtonIds = { R.id.col_page_btn_1,
	// R.id.col_page_btn_2, R.id.col_page_btn_3, R.id.col_page_btn_4,
	// R.id.col_page_btn_5 };

	private String mCategoryThumbUrl;

	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private TextView mHeaderTitle;

	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private CompanyListResponse mClResponse;
	private CompanyListResponse mDealResponse;
	private CombinedListRequest mClRequest;

	private CombinedListRequest mdealRequest;

	private ImageView mProfileIconView;
	private ImageView mHeaderBackButton;
	// private TextView mCategoryTitle;
	private RefineSelectorResponse mSelctorResp;
	private RefineCategoryResponse mCatResponse;
	private boolean mIsFreshSearch = true;
	private SelectorDAO mLocalitySelectorDao;
	private boolean loadingNextPageData;
	private boolean isModifySearchDialogOpen;
	private ImageView mHomeIconView;

	private LinearLayout mDealsFooter;
	private LinearLayout mListFooter;
	private boolean mScrollUp;

	private boolean isAdvanceSearchLayoutOpen = false;
	private LinearLayout advanceSearchLayout;
	private TextView currentCity, currentLocality;
	private ImageView upArrow;
	private ArrayList<String> cityListString = new ArrayList<String>();
	private ArrayList<String> localityItems;
	ArrayList<CityOrLocality> cityList;
	private String selectedCityforHeader = "Entire Malaysia";
	private int city_id = -1;

	private ArrayList<String> selectedLocalityItemsforHeader = new ArrayList<String>();
	
	private String selectedCity = "Entire Malaysia";
	private ArrayList<String> selectedLocalityItems = new ArrayList<String>();
	ArrayList<CityOrLocality> localityList;
	ArrayList<String> ids = new ArrayList<String>();
	TextView mainSearchButton;
	ArrayList<String> selectedLocalityindex;
	LinearLayout wholeSearchBoxContainer;
	private int mPreviousSelectedIndex;
	private String mPreviousDistance;

	@SuppressWarnings("unchecked")
	ArrayList<String> filter = new ArrayList(Arrays.asList("1 km", "2 km",
			"3 km", "4 km", "5 km", "10 km", "15 km", "20 km", "All"));
	private ArrayAdapter<String> mDistanceFilterAdapter;
	private Spinner mDistanceChooser;
	private TextView nearMe;
	boolean isFirstTime = false;
	boolean isSuccessfull = false;
	boolean isFromNearMe = false;
	boolean isFromSearch = false;
	private ViewPager bannerView;
	private ArrayList<String> bannerList;
	private LinearLayout bannerViewLayout;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				mIsFreshSearch = false;
				// mRefineSearchView.setImageDrawable(getResources().getDrawable(R.drawable.modify_search));
				mRefineSearchView.setText(getResources().getString(
						R.string.cl_modify_search));
				Bundle bundle = data.getExtras();
				mClResponse = bundle.getParcelable(AppConstants.COMP_LIST_DATA);
				mLocalitySelectorDao = bundle
						.getParcelable(AppConstants.LOCALITY_DAO_DATA);
				mClRequest = bundle
						.getParcelable(AppConstants.DATA_LIST_REQUEST);
				mSelctorResp = bundle
						.getParcelable(AppConstants.REFINE_ATTR_RESPONSE);
				mCatResponse = bundle
						.getParcelable(AppConstants.REFINE_CAT_RESPONSE);
				mRecordsFoundView
						.setText(mClResponse.getTotalrecordFound()
								+ " "
								+ getResources().getString(
										R.string.record_found));
				// initNavigationButton(mClResponse.getPagesCount());
				addAnEmptyRow();
				ArrayList<CompanyDesc> compListData = mClResponse
						.getCompanyArrayList();
				mCompListAdapter.setData(compListData);
				mCompListAdapter.notifyDataSetChanged();

				if (MaxisMainActivity.isCitySelected) {

					mDistanceChooser.setVisibility(View.GONE);
					nearMe.setVisibility(View.VISIBLE);
				} else {
					nearMe.setVisibility(View.GONE);
					mDistanceChooser.setVisibility(View.VISIBLE);
					isSuccessfull = true;
					String search_distance = mClResponse.getSearch_distance();
					int index = setDistanceFilter(search_distance);
					mDistanceChooser.setSelection(index);
				}

			}
		} else if (resultCode == RESULT_OK
				&& requestCode == AppConstants.CITY_REQUEST) {
			if (!selectedCityforHeader
					.equalsIgnoreCase(data.getStringExtra("CITY_NAME"))) {
				localityItems = null;
				ids = null;
				selectedLocalityindex = null;
				currentLocality.setText("Choose your Area");
			}
			selectedCityforHeader = data.getStringExtra("CITY_NAME");
			selectedCity = data.getStringExtra("CITY_NAME");
			currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCityforHeader
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

			selectedLocalityItemsforHeader = data
					.getStringArrayListExtra("SELECTED_LOCALITIES");
			selectedLocalityItems = selectedLocalityItemsforHeader;

			selectedLocalityindex = data
					.getStringArrayListExtra("SELECTED_LOCALITIES_INDEX");
			if (selectedLocalityItemsforHeader != null
					&& selectedLocalityItemsforHeader.size() > 0) {
				for (int i = 0; i < selectedLocalityItemsforHeader.size(); i++) {

					if (i == selectedLocalityItemsforHeader.size() - 1) {
						locality += selectedLocalityItemsforHeader.get(i);
					} else {
						locality += selectedLocalityItemsforHeader.get(i) + ",";
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

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_combind_list);
		AnalyticsHelper.logEvent(
				FlurryEventsConstants.APPLICATION_COMBINED_LIST, true);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.cl_root_layout),
				this);

		mHeaderTitle = (TextView) findViewById(R.id.header_title);
		ImageLoader.initialize(CombindListActivity.this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mViewAllOnMap = (TextView) findViewById(R.id.col_view_on_map);
		mViewAllOnMap.setOnClickListener(this);

		findViewById(R.id.col_view_on_map1).setOnClickListener(this);

		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(CombindListActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mDealsFooter = (LinearLayout) findViewById(R.id.cl_deals_footer);
		mListFooter = (LinearLayout) findViewById(R.id.cl_company_list_footer);

		Bundle bundle = getIntent().getExtras();
		mClRequest = bundle.getParcelable(AppConstants.DATA_LIST_REQUEST);
		if (mClRequest != null
				&& !StringUtil.isNullOrEmpty(mClRequest.getCategoryTitle())) {
			mHeaderTitle.setText(Html.fromHtml(mClRequest.getCategoryTitle()));
			mCategoryThumbUrl = mClRequest.getParentThumbUrl();
		}
		mRefineSearchView = (TextView) findViewById(R.id.col_refine_search);
		mRefineSearchView.setOnClickListener(this);

		findViewById(R.id.col_refine_search1).setOnClickListener(this);

		advanceSearchLayout = (LinearLayout) findViewById(R.id.advanceSearch);
		advanceSearchLayout.setVisibility(View.GONE);

		upArrow = (ImageView) findViewById(R.id.upArrow);
		upArrow.setOnClickListener(this);

		currentCity = (TextView) findViewById(R.id.currentCity);
		currentLocality = (TextView) findViewById(R.id.currentLocality);
		currentCity.setText(Html
				.fromHtml("in " + "<b>" + selectedCityforHeader + "</b>"));

		currentCity.setOnClickListener(this);
		currentLocality.setOnClickListener(this);

		mainSearchButton = (TextView) findViewById(R.id.mainSearchButton);
		mainSearchButton.setOnClickListener(this);

		wholeSearchBoxContainer = (LinearLayout) findViewById(R.id.whole_search_box_container);

		String jsonForSearch = mClRequest.getPostJsonPayload();
		if (!StringUtil.isNullOrEmpty(jsonForSearch)) {
			try {
				JSONObject jsonObject = new JSONObject(jsonForSearch);

				JSONObject jObject = jsonObject.getJSONObject("city");
				int cityId = jObject.getInt("city_id");
				String city_name = jObject.getString("city_name");

				selectedCityforHeader = city_name;
				selectedCity = city_name;
				city_id = cityId;

				currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCityforHeader
						+ "</b>"));

				JSONArray jArray = jsonObject.getJSONArray("locality");
				ids.clear();
				selectedLocalityItemsforHeader.clear();
				for (int i = 0; i < jArray.length(); i++) {
					try {
						JSONObject object = jArray.getJSONObject(i);
						// Pulling items from the array
						int locality_id = object.getInt("locality_id");
						String locality_name = object
								.getString("locality_name");
						ids.add(locality_id + "");
						selectedLocalityItemsforHeader.add(locality_name);
						selectedLocalityItems.add(locality_name);

					} catch (JSONException e) {
					}
				}
				String locality = "";
				if (selectedLocalityItemsforHeader != null
						&& selectedLocalityItemsforHeader.size() > 0) {
					for (int i = 0; i < selectedLocalityItemsforHeader.size(); i++) {

						if (i == selectedLocalityItemsforHeader.size() - 1) {
							locality += selectedLocalityItemsforHeader.get(i);
						} else {
							locality += selectedLocalityItemsforHeader.get(i) + ",";
						}
					}
					currentLocality.setText(Html.fromHtml("Your Selected Area "
							+ "<b>" + locality + "</b>"));
				} else {
					currentLocality.setText("Choose your Area");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		mClResponse = bundle.getParcelable(AppConstants.COMP_LIST_DATA);
		if (mClRequest.isBySearch()) {
			if (mIsFreshSearch) {
				if (mClResponse.getCategoryList() != null
						&& mClResponse.getCategoryList().size() == 1) {
					mIsFreshSearch = false;
					CategoryRefine tempCatref = mClResponse.getCategoryList()
							.get(0);
					mClRequest.setSelectedCategoryBySearch(
							tempCatref.getCategoryId(),
							tempCatref.getCategoryTitle());
				} else
					mRefineSearchView.setText(getResources().getString(
							R.string.cl_filter_by));
				// mRefineSearchView.setImageDrawable(getResources().getDrawable(R.drawable.filter_by));
			}
			if (mClRequest != null
					&& !StringUtil.isNullOrEmpty(mClRequest
							.getKeywordOrCategoryId())) {
				mHeaderTitle.setText(Html.fromHtml(mClRequest
						.getKeywordOrCategoryId()));
				mSearchEditText.setText(Html.fromHtml(mClRequest
						.getKeywordOrCategoryId()));
			}
		} else if ((mClRequest
				.getGroupActionType()
				.trim()
				.equalsIgnoreCase(
						AppConstants.GROUP_ACTION_TYPE_CATEGORY_LIST_FOR_GROUP) && (mClRequest
				.getGroupType().trim()
				.equalsIgnoreCase(AppConstants.GROUP_TYPE_CATEGORY)))) {
			if (mIsFreshSearch) {
				if (mClResponse.getCategoryList() != null
						&& mClResponse.getCategoryList().size() == 1) {
					mIsFreshSearch = false;
					CategoryRefine tempCatref = mClResponse.getCategoryList()
							.get(0);
					mClRequest.setSelectedCategoryBySearch(
							tempCatref.getCategoryId(),
							tempCatref.getCategoryTitle());
				} else
					mRefineSearchView.setText(getResources().getString(
							R.string.cl_filter_by));
				// mRefineSearchView.setImageDrawable(getResources().getDrawable(R.drawable.filter_by));
			}
		} else {
			mHeaderTitle.setVisibility(View.VISIBLE);
		}

		mRecordsFoundView = (TextView) findViewById(R.id.col_records_found);
		// mMoreLinkView = (TextView) findViewById(R.id.col_more_option);
		// mMoreLinkView.setOnClickListener(this);
		mDealBtnView = (TextView) findViewById(R.id.col_deal_btn);
		mDealBtnView.setOnClickListener(this);
		if (mClRequest.isCompanyListing()) {
			// mDealBtnView.setOnClickListener(this);
			mDealsFooter.setVisibility(View.GONE);
			mListFooter.setVisibility(View.VISIBLE);
		} else {
			// mDealBtnView.setVisibility(View.GONE);
			mDealsFooter.setVisibility(View.VISIBLE);
			mListFooter.setVisibility(View.GONE);
		}

		mRecordsFoundView.setText(mClResponse.getTotalrecordFound() + " "
				+ getResources().getString(R.string.record_found));
		// initNavigationButton(mClResponse.getPagesCount());
		ArrayList<CompanyDesc> compListData = mClResponse.getCompanyArrayList();
		mCompanyList = (ListView) findViewById(R.id.col_company_list);
		mCompListAdapter = new CompanyListAdapter(CombindListActivity.this,
				mClRequest.isCompanyListing());
		mCompListAdapter.setData(compListData);
		mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler = (ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mCompanyList.setAdapter(mCompListAdapter);
		mCompanyList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if (arg2 == (mCompListAdapter.getCount() - 1) && arg2 != 0
						&& mClResponse.getTotalrecordFound() > 10) {
					// do nothing
				} else {
					CompanyDetailController controller = new CompanyDetailController(
							CombindListActivity.this, Events.COMPANY_DETAIL);
					String id = ((CompanyDesc) mCompListAdapter.getItem(arg2))
							.getCompId();
					DetailRequest detailRequest = new DetailRequest(
							CombindListActivity.this, id, !mClRequest
									.isCompanyListing(),
							((CompanyDesc) mCompListAdapter.getItem(arg2))
									.getCat_id());

					startSppiner();
					controller.requestService(detailRequest);
				}
			}
		});
		mCompanyList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView listView,
					int scrollState) {
			}

			@Override
			public void onScroll(AbsListView listView, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				Log.w("", "firstVisibleItem " + firstVisibleItem
						+ " visibleItemCount " + visibleItemCount
						+ " totalItemCount " + totalItemCount);
				int number = firstVisibleItem + visibleItemCount;
				if (mClResponse != null) {
					if ((number) % mClResponse.getRecordsPerPage() == 0
							&& number > 0
							&& number == totalItemCount
							&& totalItemCount < mClResponse
									.getTotalrecordFound())// mNewsList.size()>totalItemCount
					{
						Log.d("maxis", "list detail before next page"
								+ mClResponse.getPageNumber() + "  "
								+ totalItemCount + " "
								+ mClResponse.getCompanyArrayList().size());
						if (loadingNextPageData)
							return;
						loadingNextPageData = true;
						if (mClResponse.getPageNumber() < AppConstants.MAX_RECORD_COUNT / 10) {
							loadPageData(mClResponse.getPageNumber() + 1);
						}
					} else if (number >= AppConstants.MAX_RECORD_COUNT
							&& !isModifySearchDialogOpen && mScrollUp
							&& mClResponse.getTotalrecordFound() > 100) {
						showConfirmationDialog(
								CustomDialog.CONFIRMATION_DIALOG,
								getResources().getString(
										R.string.modify_to_filter));
						isModifySearchDialogOpen = true;
						AnalyticsHelper
								.logEvent(FlurryEventsConstants.COMBINED_LIST_VISITED_ITEMS_EXCEEDED_70);
					}
				}
			}
		});

		mCompanyList.setOnTouchListener(new OnTouchListener() {
			private float mInitialX;
			private float mInitialY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mInitialX = event.getX();
					mInitialY = event.getY();
					mScrollUp = false;
					// mScrollDown = true;
					return true;
				case MotionEvent.ACTION_MOVE:
					final float x = event.getX();
					final float y = event.getY();
					final float yDiff = y - mInitialY;
					if (yDiff > 0.0) {
						Log.d("maxis", "SCROLL DOWN");
						mScrollUp = false;
						break;

					} else if (yDiff < 0.0) {
						Log.d("maxis", "SCROLL up");
						mScrollUp = true;
						break;
					}
					break;
				}
				return false;
			}

		});

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
/*		 if (mClResponse.getRecordsPerPage() <= 10 &&
		 mClResponse.getTotalrecordFound()>5) {
		 // Add one blank record.
		 CompanyDesc desc = new CompanyDesc();
		 desc.setCompId("-1");
		 mClResponse.getCompanyArrayList().add(desc);
		 }*/

		addAnEmptyRow();
		
		
		mDistanceChooser = (Spinner) findViewById(R.id.distance_filter_chooser);
		nearMe = (TextView) findViewById(R.id.nearMe);
		nearMe.setOnClickListener(this);

		// TO Be CHANGE

		if (MaxisMainActivity.isCitySelected
				|| mClRequest.getLongitude() == 0.0
				|| mClRequest.getLatitude() == 0.0) {

			mDistanceChooser.setVisibility(View.GONE);
			nearMe.setVisibility(View.VISIBLE);
		} else {
			nearMe.setVisibility(View.GONE);
			mDistanceChooser.setVisibility(View.VISIBLE);
		}

		mDistanceFilterAdapter = new ArrayAdapter<String>(
				CombindListActivity.this, R.layout.spinner_item, filter);
		mDistanceChooser.setAdapter(mDistanceFilterAdapter);
		String search_distance = mClResponse.getSearch_distance();
		mDistanceChooser.setSelection(setDistanceFilter(search_distance));

		mDistanceChooser
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					String distance = "";

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {
						if (filter.get(pos).equalsIgnoreCase("All")) {
							distance = "1000000";

						} else {
							distance = filter.get(pos).replace(" km", "");
						}

						if (isFirstTime) {

							if (mPreviousSelectedIndex != pos && !isFromNearMe) {
								mClRequest.setSearch_distance(distance);
								mClRequest.setPageNumber(1);
								CombindListingController listingController = new CombindListingController(
										CombindListActivity.this,
										Events.COMBIND_LISTING_NEW_LISTING_PAGE);
								startSppiner();
								listingController.requestService(mClRequest);

							} else {
								// Do NOthing
							}
						} else {
							isFirstTime = true;
							mClRequest.setSearch_distance(distance);
						}
						// if (isSuccessfull) {
						// mPreviousSelectedIndex = pos;
						// mPreviousDistance = distance;
						// isSuccessfull = false;
						// }

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
	bannerViewLayout = (LinearLayout) findViewById(R.id.bannerView_LnrLayout);
	bannerView = (ViewPager) findViewById(R.id.subcategory_banner);
		
		if (mClResponse != null && mClResponse.getBanner() != null && mClResponse.getBanner().size() > 0) {
			bannerList = new ArrayList<String>();
			bannerList = mClResponse.getBanner();
			bannerViewLayout.setVisibility(View.VISIBLE);
			BannerViewAdapter bannerViewAdapter = new BannerViewAdapter(getSupportFragmentManager(), bannerList, this, "Category List");
//			if (bannerList.size() > 1) {
//				addImage();
//				circleIndicator.setVisibility(View.VISIBLE);
//			} else {
//				circleIndicator.setVisibility(View.GONE);
//			}
			bannerView.setAdapter(bannerViewAdapter);
		} else {
			bannerViewLayout.setVisibility(View.GONE);
//			circleIndicator.setVisibility(View.GONE);
		}

//		comDetailGallery.setOnPageChangeListener(new OnPageChangeListener() {
//
//			@Override
//			public void onPageSelected(int position) {
//				indicatorchange(position);
//				flipperVisibleItemPosition = position;
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//				comDetailGallery.getParent()
//						.requestDisallowInterceptTouchEvent(true);
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//
//			}
//		});

	}

	private void addAnEmptyRow() {
		 if (mClResponse.getTotalrecordFound() <= mClResponse.getRecordsPerPage() && mClResponse.getTotalrecordFound() > 5) {
		 // Add one blank record.
		 CompanyDesc desc = new CompanyDesc();
		 desc.setCompId("-1");
		 mClResponse.getCompanyArrayList().add(desc);
		 }
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		AnalyticsHelper.trackSession(CombindListActivity.this, AppConstants.Company_listing);
	}
	
	/*
	 * private void initNavigationButton(int pages) { if (pages < 1) return; if
	 * (mPaginationButtons != null) { for (int i = 0; i <
	 * mPaginationButtons.length; i++) { if (mPaginationButtons[i] != null)
	 * mPaginationButtons[i].setVisibility(View.GONE); }
	 * mMoreLinkView.setVisibility(View.GONE); } if (pages > 5)
	 * mMoreLinkView.setVisibility(View.VISIBLE); mPaginationButtons = new
	 * Button[pages]; for (int i = 0; i < pages && i < 5; i++) {
	 * mPaginationButtons[i] = (Button) findViewById(mNavigationButtonIds[i]);
	 * mPaginationButtons[i].setVisibility(View.VISIBLE);
	 * mPaginationButtons[i].setOnClickListener(this); if (i == 0) {
	 * mPaginationButtons[i].setSelected(true);
	 * mPaginationButtons[i].setTextColor
	 * (getResources().getColor(R.color.white)); } }
	 * changeNavigationButtonState(pages, 1); }
	 */

	/*
	 * private void changeNavigationButtonState(int pages, int currentPage) {
	 * for (int i = 0; i < pages && i < 5; i++) { if (i == currentPage - 1) {
	 * mPaginationButtons[i].setSelected(true);
	 * mPaginationButtons[i].setTextColor
	 * (getResources().getColor(R.color.white));
	 * mPaginationButtons[i].setClickable(false); } else {
	 * mPaginationButtons[i].setSelected(false);
	 * mPaginationButtons[i].setTextColor
	 * (getResources().getColor(R.color.black));
	 * mPaginationButtons[i].setClickable(true); } } }
	 */
	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.REFINE_ATTRIBUTES) {
			Message catRefine = (Message) screenData;
			handler.sendMessage(catRefine);
			return;
		} else if (event == Events.CITY_LISTING
				|| event == Events.LOCALITY_LISTING) {
			Message message = (Message) screenData;
			handler.sendMessage(message);
		} else if (event == Events.DEALCATEGORY_EVENT) {
			Message message = (Message) screenData;
			handler.sendMessage(message);
		}else if(event == Events.COMBIND_LISTING_NEW_LISTING_PAGE && isFromSearch)
		{
			super.setScreenData(screenData, event, time);
			return;
		}

		else {
			Response response = (Response) screenData;
			Message message = new Message();
			message.arg2 = event;
			message.arg1 = 1;
			if (response.isError()) {
				message.obj = response.getErrorText();
			} else {
				if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE
						|| event == Events.COMBIND_LISTING_PAGINATION) {
					if (response.getPayload() instanceof CompanyListResponse) {
						CompanyListResponse compListResponse = (CompanyListResponse) response
								.getPayload();
						if (compListResponse.getErrorCode() != 0) {
							message.obj = getResources().getString(
									R.string.communication_failure);
						} else {
							if (compListResponse.getCompanyArrayList().size() < 1) {
								message.obj = new String(getResources()
										.getString(R.string.no_result_found));
							} else {
								message.arg1 = 0;
								message.obj = compListResponse;
							}
						}
					} else {
						message.obj = new String(getResources().getString(
								R.string.communication_failure));
					}
				} else if (event == Events.COMBIND_DEAL_LISTING_NEW_LISTING_PAGE) {
					if (response.getPayload() instanceof CompanyListResponse) {
						mDealResponse = (CompanyListResponse) response
								.getPayload();
						if (mDealResponse.getErrorCode() != 0) {
							message.obj = getResources().getString(
									R.string.communication_failure);
							// clResponse.getServerMessage() + " " +
							// clResponse.getErrorCode();
						} else {
							if (mDealResponse.getCompanyArrayList().size() < 1) {
								message.obj = new String("No Result Found");
							} else {
								message.arg1 = 0;
								message.obj = mDealResponse;
							}
						}
					} else {
						message.obj = new String("Internal Error");
					}
				}

				else {
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
			}
			handler.sendMessage(message);
		}
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.COMPANY_DETAIL) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				CompanyDetail compListResp = (CompanyDetail) msg.obj;
				if (!StringUtil.isNullOrEmpty(compListResp.getId())) {
					Intent intent = new Intent(CombindListActivity.this,
							CompanyDetailActivity.class);
					if (mClRequest.isBySearch())
						intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD,
								mClRequest.getKeywordOrCategoryId());
					intent.putExtra(AppConstants.COMP_DETAIL_DATA, compListResp);
					intent.putExtra(AppConstants.THUMB_URL, mCategoryThumbUrl);
					intent.putExtra(AppConstants.IS_DEAL_LIST,
							!mClRequest.isCompanyListing());
					startActivity(intent);
				} else {
					showInfoDialog(getResources().getString(
							R.string.no_result_found));
				}
			}
			stopSppiner();
		} else if (msg.arg2 == Events.COMBIND_LISTING_PAGINATION) {
			loadingNextPageData = false;
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				CompanyListResponse oldResponse = mClResponse;
				mClResponse = (CompanyListResponse) msg.obj;
				oldResponse.appendCompListAtEnd(
						mClResponse.getCompanyArrayList(), false);
				mClResponse.setCompanyList(oldResponse.getCompanyArrayList());
				// changeNavigationButtonState(mClResponse.getPagesCount(),
				// mClResponse.getPageNumber());
				if (mClResponse.getPageNumber() == 10
						|| mClResponse.getTotalrecordFound() == mClResponse
								.getCompanyArrayList().size()) {
					// Add one blank record.
					CompanyDesc desc = new CompanyDesc();
					desc.setCompId("-1");
					mClResponse.getCompanyArrayList().add(desc);
				}

				mCompListAdapter.setData(mClResponse.getCompanyArrayList());
				mCompListAdapter.notifyDataSetChanged();
			}
			stopSppiner();
		} else if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE && !isFromSearch) {
			isFromNearMe = false;
			
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
				isSuccessfull = false;
				mDistanceChooser.setSelection(mPreviousSelectedIndex);
				mClRequest.setSearch_distance(mPreviousDistance);
			} else {
				isSuccessfull = true;
				// CompanyListResponse oldResponse = new CompanyListResponse();
				mClResponse = (CompanyListResponse) msg.obj;
				String search_distance = mClResponse.getSearch_distance();
				int index = setDistanceFilter(search_distance);
				mDistanceChooser.setSelection(index);
				mClRequest.setSearch_distance(search_distance);

				if (MaxisMainActivity.isCitySelected) {
					mDistanceChooser.setVisibility(View.GONE);
					nearMe.setVisibility(View.VISIBLE);
				} else {
					nearMe.setVisibility(View.GONE);
					mDistanceChooser.setVisibility(View.VISIBLE);
				}
				if (mClResponse.getPageNumber() == 10
						|| mClResponse.getTotalrecordFound() == mClResponse
								.getCompanyArrayList().size()) {
				}
				mRecordsFoundView
						.setText(mClResponse.getTotalrecordFound()
								+ " "
								+ getResources().getString(
										R.string.record_found));
				
				mCompListAdapter.setData(mClResponse.getCompanyArrayList());
				mCompListAdapter.notifyDataSetChanged();
				mCompanyList.setAdapter(mCompListAdapter);
				System.out.println(mClResponse.getPageNumber());
				if (mClRequest != null
						&& !StringUtil.isNullOrEmpty(mClRequest.getCategoryTitle())) {
					mHeaderTitle.setText(Html.fromHtml(mClRequest.getCategoryTitle()));
					mCategoryThumbUrl = mClRequest.getParentThumbUrl();
				}
			}
			stopSppiner();
		} else if (msg.arg2 == Events.REFINE_ATTRIBUTES) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				if (mClRequest.isBySearch())
					displayRefineWithAttributeSpinnersPreloaded(
							(RefineSelectorResponse) msg.obj,
							RefineSearchActivity.ATTR_SELECTION_BY_SEARCH);
				else
					displayRefineWithAttributeSpinnersPreloaded(
							(RefineSelectorResponse) msg.obj,
							RefineSearchActivity.ATTR_SELECTION);
			}
			stopSppiner();
		} else if (msg.arg2 == Events.COMBIND_DEAL_LISTING_NEW_LISTING_PAGE) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				// mRecordsFoundView.setText(mClResponse.getTotalrecordFound()
				// + " " + getResources().getString(R.string.record_found));
				// initNavigationButton(mClResponse.getPagesCount());
				ArrayList<CompanyDesc> compListData = mDealResponse.getCompanyArrayList();

				Intent intent = new Intent(CombindListActivity.this,
						DealsActivity.class);

				intent.putExtra(AppConstants.DATA_LIST_REQUEST, mdealRequest);
				intent.putExtra(AppConstants.COMP_LIST_DATA, mDealResponse);

				startActivity(intent);

				// mCompListDealAdapter = new CompanyListDealAdapter(
				// DealsActivity.this, false);
				// mCompListDealAdapter.setData(compListData);
				// mCompanyList.setAdapter(mCompListDealAdapter);
			}
			stopSppiner();

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
				Intent intent = new Intent(CombindListActivity.this,
						AdvanceSelectCity.class);
				for (CityOrLocality cityOrLocality : cityList) {

					cityListString.add(cityOrLocality.getName());
				}
				localityItems = null;
				ids = null;
				selectedLocalityindex = null;
				currentLocality.setText("Choose your Area");
				intent.putExtra("CITY_LIST", cityListString);
				intent.putExtra("SELECTED_CITY", selectedCityforHeader);
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
				Intent intent = new Intent(CombindListActivity.this,
						AdvanceSelectLocalityActivity.class);
				localityItems = new ArrayList<String>();
				for (CityOrLocality dealCityOrLoc : localityList) {
					localityItems.add(dealCityOrLoc.getName());
				}
				intent.putExtra("LOCALITY_LIST", localityItems);
				intent.putStringArrayListExtra("LOCALITY_INDEX",
						selectedLocalityindex);
				intent.putExtra("SELECTED_LOCALITIES", selectedLocalityItemsforHeader);
				startActivityForResult(intent, AppConstants.LOCALITY_REQUEST);

			}
		} else if (msg.arg2 == Events.DEALCATEGORY_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				SubCategoryResponse categoriesResp = (SubCategoryResponse) msg.obj;
				Intent intent = new Intent(CombindListActivity.this,
						DealsActivity.class);
				intent.putExtra(AppConstants.DATA_SUBCAT_RESPONSE,
						categoriesResp);
				startActivity(intent);
			}
			stopSppiner();
		}else if(msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE && isFromSearch)
		{
			isFromSearch = false;
			super.updateUI(msg);
		}

	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	private void loadPageData(int pageNumber) {
		CombindListingController listingController = new CombindListingController(
				CombindListActivity.this, Events.COMBIND_LISTING_PAGINATION);
		mClRequest.setPageNumber(pageNumber);
		startSppiner();
		listingController.requestService(mClRequest);
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
			MaxisMainActivity.isCitySelected = false;
			this.finish();
			break;
		case R.id.col_refine_search:
		case R.id.col_refine_search1:
			refineSearch();
			AnalyticsHelper.logEvent(FlurryEventsConstants.MODIFY_SEARCH_CLICK);
			break;
		// case R.id.col_more_option:
		// showConfirmationDialog(CustomDialog.CONFIRMATION_DIALOG,
		// getResources().getString(R.string.modify_to_filter));
		// break;
		case R.id.col_deal_btn:

			CategoryGroup cat = new CategoryGroup();
			cat.setmGroupActionType(AppConstants.GROUP_ACTION_TYPE_DEAL);
			cat.setMgroupType(AppConstants.GROUP_TYPE_CATEGORY);
			cat.setCategoryId("");
			cat.setCategoryTitle("");
			showDealcategories(cat);
			// showDealListing(mClRequest.getKeywordOrCategoryId());
			// CombindListingController listingController = new
			// CombindListingController(CombindListActivity.this,
			// Events.COMBIND_LISTING_NEW_LISTING_PAGE);
			// CombinedListRequest newListReq = new
			// CombinedListRequest(CombindListActivity.this);
			// newListReq.setBySearch(mClRequest.isBySearch());//
			// newListReq.setCompanyListing(false);
			// newListReq.setKeywordOrCategoryId(mClRequest.getKeywordOrCategoryId());
			// newListReq.setLatitude(GPS_Data.getLatitude());
			// newListReq.setLongitude(GPS_Data.getLongitude());
			// newListReq.setGroupActionType(AppConstants.GROUP_ACTION_TYPE_DEAL);
			// newListReq.setGroupType(AppConstants.GROUP_TYPE_SUB_CATEGORY);
			// setRequest(newListReq);
			// startSppiner();
			// listingController.requestService(newListReq);
			AnalyticsHelper.logEvent(FlurryEventsConstants.HOT_DEALS_CLICK);
			break;
		case R.id.mainSearchButton:
			mSearchEditText
					.setText(mSearchEditText.getText().toString().trim());

			String JSON_EXTRA = jsonForSearch();
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA);
			mClRequest.setKeywordOrCategoryId(mSearchEditText.getText()
					.toString());
			mClRequest.setCategoryTitle(mSearchEditText.getText()
					.toString());
			isFromSearch = true;
			
			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			showHomeScreen();
			MaxisMainActivity.isCitySelected = false;
			break;
		case R.id.show_profile_icon:
			onProfileClick();
			break;
		case R.id.col_view_on_map:
		case R.id.col_view_on_map1:
			AnalyticsHelper.logEvent(FlurryEventsConstants.VIEW_ON_MAP_CLICK);
			if (isDialogToBeShown())
				showConfirmationDialog(CustomDialog.DATA_USAGE_DIALOG,
						getResources().getString(R.string.cd_msg_data_usage));
			else
				showMap();
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
				Intent cityIntent = new Intent(CombindListActivity.this,
						AdvanceSelectCity.class);
				cityIntent.putExtra("CITY_LIST", cityListString);
				cityIntent.putExtra("SELECTED_CITY", selectedCityforHeader);
				startActivityForResult(cityIntent, AppConstants.CITY_REQUEST);
			} else {
				setSearchCity();
			}
			break;

		case R.id.currentLocality:
			if (localityItems != null && localityItems.size() > 0) {
				Intent localityIntent = new Intent(CombindListActivity.this,
						AdvanceSelectLocalityActivity.class);
				localityIntent.putExtra("LOCALITY_LIST", localityItems);
				localityIntent.putExtra("SELECTED_LOCALITIES",
						selectedLocalityItemsforHeader);
				localityIntent.putStringArrayListExtra("LOCALITY_INDEX",
						selectedLocalityindex);
				startActivityForResult(localityIntent,
						AppConstants.LOCALITY_REQUEST);
			} else {
				setSearchLocality(city_id);
			}
			break;

		case R.id.nearMe:

			if (mClRequest.getLongitude() == 0.0
					|| mClRequest.getLatitude() == 0.0) {
				showAlertDialog(getResources().getString(
						R.string.location_unavailable_switch_gps));
			} else {
				mSelctorResp = null;
				
				 selectedCity = null;
				 selectedLocalityItems = null;

				mDistanceChooser.setVisibility(View.VISIBLE);
				nearMe.setVisibility(View.GONE);
				mClRequest.setSearch_distance("");

				mClRequest.setPostJsonPayload("");
				MaxisMainActivity.isCitySelected = false;
				isFromNearMe = true;
				CombindListingController listingController = new CombindListingController(
						CombindListActivity.this,
						Events.COMBIND_LISTING_NEW_LISTING_PAGE);
				startSppiner();
				listingController.requestService(mClRequest);
			}
			break;
		}
	}

	private void refineSearch() {
		if (mClRequest.isBySearch()
				|| (mClRequest
						.getGroupActionType()
						.trim()
						.equalsIgnoreCase(
								AppConstants.GROUP_ACTION_TYPE_CATEGORY_LIST_FOR_GROUP) && (mClRequest
						.getGroupType().trim()
						.equalsIgnoreCase(AppConstants.GROUP_TYPE_CATEGORY)))) {
			if (mIsFreshSearch) {
				mCatResponse = new RefineCategoryResponse();
				if (mClResponse.getCategoryList() == null
						|| mClResponse.getCategoryList().size() < 1) {
					showAlertDialog(getResources().getString(
							R.string.category_list_not_found));
					return;
				}
				mCatResponse.setCategories(mClResponse.getCategoryList());
				Intent intent = new Intent(CombindListActivity.this,
						RefineSearchCategoryActivity.class);
				intent.putExtra(AppConstants.REFINE_CAT_RESPONSE, mCatResponse);
				intent.putExtra(AppConstants.DATA_LIST_REQUEST, mClRequest);
				intent.putExtra("SELECTED_CITY", selectedCity);
				intent.putExtra("SELECTED_LOCALITY", selectedLocalityItems);
				intent.putExtra("SELECTED_CITY_FOR_HEADERS", selectedCityforHeader);
				intent.putExtra("SELECTED_LOCALITY_FOR_HEADERS", selectedLocalityItemsforHeader);
				startActivityForResult(intent, 1);
			} else {
				if (mSelctorResp != null) {
					displayRefineWithAttributeSpinnersPreloaded(mSelctorResp,
							RefineSearchActivity.ATTR_SELECTION_BY_SEARCH);
				} else {
					fetchRefineAttribute(mClRequest
							.getSelectedCategoryBySearch());
				}
			}
		} else {
			if (mSelctorResp != null) {
				displayRefineWithAttributeSpinnersPreloaded(mSelctorResp,
						RefineSearchActivity.ATTR_SELECTION);
			} else
				fetchRefineAttribute(mClRequest.getKeywordOrCategoryId());
		}
	}

	private void fetchRefineAttribute(String categoryId) {
		RefineAttributeController refineController = new RefineAttributeController(
				CombindListActivity.this, Events.REFINE_ATTRIBUTES);
		startSppiner();
		RefineSearchRequest refineSearchRequest = new RefineSearchRequest();
		refineSearchRequest.setCategoryId(categoryId);
		if(mClRequest.isBySearch())
		{
			refineSearchRequest.setSearchKeyword(mClRequest.getKeywordOrCategoryId());
		}else
		{
			refineSearchRequest.setSearchKeyword("");	
		}
		refineSearchRequest.setDeal(!mClRequest.isCompanyListing());
		refineController.requestService(refineSearchRequest);
	}

	private void displayRefineWithAttributeSpinnersPreloaded(
			RefineSelectorResponse selectorRes, int selectionMode) {
		Intent intent = new Intent(CombindListActivity.this,
				RefineSearchActivity.class);
		intent.putExtra(AppConstants.REFINE_ATTR_RESPONSE, selectorRes);
		intent.putExtra(RefineSearchActivity.SELECTOR_MODE, selectionMode);
		intent.putExtra(AppConstants.DATA_LIST_REQUEST, mClRequest);
		intent.putExtra(AppConstants.THUMB_URL, mCategoryThumbUrl);
		intent.putExtra(AppConstants.LOCALITY_DAO_DATA, mLocalitySelectorDao);
		if(mClResponse.getCategoryList().size()>0)
		{
		intent.putExtra("categoryId", mClResponse.getCategoryList().get(0)
				.getCategoryId());
		}
		startActivityForResult(intent, 1);
	}

	@Override
	public void onPositiveDialogButton(int id) {
		if (id == CustomDialog.CONFIRMATION_DIALOG) {
			refineSearch();
			isModifySearchDialogOpen = false;
		} else if (id == CustomDialog.DATA_USAGE_DIALOG) {
			showMap();
		} else {
			super.onPositiveDialogButton(id);
		}
	}

	private void showMap() {
		if (isLocationAvailable()) {
			Intent intent = new Intent(CombindListActivity.this,
					ViewAllOnMapActivity.class);
			intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD,
					mClRequest.getKeywordOrCategoryId());
			intent.putExtra(AppConstants.MAP_ALL_TITLE, mHeaderTitle.getText()
					.toString().trim());
			intent.putExtra(AppConstants.IS_SEARCH_KEYWORD,
					mClRequest.isBySearch());
			intent.putExtra(AppConstants.IS_DEAL_LIST,
					!mClRequest.isCompanyListing());
			intent.putParcelableArrayListExtra(AppConstants.COMP_DETAIL_LIST,
					mClResponse.getCompanyArrayList());
			startActivity(intent);
		}
	}

	@Override
	public void onNegativeDialogbutton(int id) {
		loadingNextPageData = false;
		isModifySearchDialogOpen = false;
		super.onNegativeDialogbutton(id);
	}

	// private void showDealListing(String cat) {
	// CombindListingController listingController = new
	// CombindListingController(
	// CombindListActivity.this,
	// Events.COMBIND_DEAL_LISTING_NEW_LISTING_PAGE);
	// // mClRequest = new CombinedListRequest(CombindListActivity.this);
	// mdealRequest = new CombinedListRequest(CombindListActivity.this);
	// mdealRequest.setCompanyListing(false);
	// // mClRequest.setKeywordOrCategoryId("-1");
	//
	// // if (StringUtil.isNullOrEmpty(mClResponse.g)) {
	// mdealRequest.setKeywordOrCategoryId(cat);
	// // } else {
	// // mdealRequest.setKeywordOrCategoryId(mClResponse.getCategoryList()
	// // .get(0).getCategoryId());
	// // }
	//
	// mdealRequest.setLatitude(GPS_Data.getLatitude());
	// mdealRequest.setLongitude(GPS_Data.getLongitude());
	// // mClRequest.setCategoryTitle(cat.getCategoryTitle());
	// // mClRequest.setParentThumbUrl(cat.getThumbUrl());
	//
	// mdealRequest.setGroupActionType(AppConstants.GROUP_ACTION_TYPE_DEAL);
	// mdealRequest.setGroupType(AppConstants.GROUP_TYPE_SUB_CATEGORY);
	// if (mClRequest.isBySearch()) {
	// mdealRequest.setBySearch(true);
	// } else {
	// mdealRequest.setBySearch(false);
	// }
	// setRequest(mdealRequest);
	// startSppiner();
	// listingController.requestService(mdealRequest);
	//
	// }

	public String jsonForSearch() {

		// {"city":{"city_id":5,"city_name":"adyui"},"locality":[{"locality_id":5,"locality_name":"adyui"},{"locality_id":5,"locality_name":"adyui"}]}
		JSONObject jArray = new JSONObject();
		try {

			if (city_id != -1) {
				JSONObject array = new JSONObject();
				array.put("city_id", city_id + "");
				array.put("city_name", selectedCityforHeader);

				jArray.put("city", array);

				if (ids != null && ids.size() > 0) {
					JSONArray jsonArray = new JSONArray();
					for (int i = 0; i < selectedLocalityItemsforHeader.size(); i++) {
						JSONObject localityArray = new JSONObject();
						localityArray.put("locality_id", ids.get(i));
						localityArray.put("locality_name",
								selectedLocalityItemsforHeader.get(i));
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
			e.printStackTrace();
			return null;
		}

	}

	protected void showDealcategories(CategoryGroup cat) {
		SubCategoryController controller = new SubCategoryController(
				CombindListActivity.this, Events.DEALCATEGORY_EVENT);
		controller.requestService(cat);
		controller.isForDeal = true;
		startSppiner();
	}

	@SuppressLint("NewApi")
	public int setDistanceFilter(String search_distance) {
		// String search_distance = mClResponse.getSearch_distance();
		int index = 0;
		if (search_distance == null || Integer.valueOf(search_distance) >= 21
				|| search_distance.isEmpty()) {

			index = 8;
			mPreviousSelectedIndex = 8;
			mPreviousDistance = "100000";

		} else {
			search_distance = search_distance + " km";
			index = filter.indexOf(search_distance);
			mPreviousSelectedIndex = index;
			mPreviousDistance = search_distance;

		}
		return index;
	}

	@Override
	public void onBackPressed() {
		MaxisMainActivity.isCitySelected = false;
		super.onBackPressed();
	}

}
