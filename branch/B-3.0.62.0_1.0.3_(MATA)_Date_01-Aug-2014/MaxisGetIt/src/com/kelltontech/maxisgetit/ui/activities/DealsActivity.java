package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
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
import android.widget.Toast;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.CompanyListDealAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.CombindListingController;
import com.kelltontech.maxisgetit.controllers.CompanyDetailController;
import com.kelltontech.maxisgetit.controllers.DownloadDealController;
import com.kelltontech.maxisgetit.controllers.RefineAttributeController;
import com.kelltontech.maxisgetit.dao.CategoryRefine;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.CompanyDesc;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.dao.SelectorDAO;
import com.kelltontech.maxisgetit.dao.SubCategory;
import com.kelltontech.maxisgetit.requests.CombinedListRequest;
import com.kelltontech.maxisgetit.requests.DetailRequest;
import com.kelltontech.maxisgetit.requests.DownloadDealReq;
import com.kelltontech.maxisgetit.requests.RefineSearchRequest;
import com.kelltontech.maxisgetit.response.CompanyListResponse;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.response.RefineCategoryResponse;
import com.kelltontech.maxisgetit.response.RefineSelectorResponse;
import com.kelltontech.maxisgetit.response.SubCategoryResponse;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class DealsActivity extends MaxisMainActivity {

	private SubCategoryResponse mSubcatResponse;
	private ImageView mProfileIconView;
	private ImageView mHeaderBackButton;
	private ImageView mHomeIconView;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private TextView mHeaderTitle;
	private SubCategory mSelectdCategory;
	private TextView mRecordsFoundView;
	private TextView mViewAllOnMap;
	private TextView mRefineSearchView;
	private TextView mRefineSearchView1;
	private ListView mCompanyList;
	private CompanyListDealAdapter mCompListDealAdapter;
	private CompanyListResponse mClResponse;
	private CombinedListRequest mClRequest;
	private SelectorDAO mLocalitySelectorDao;
	private RefineSelectorResponse mSelctorResp;
	private RefineCategoryResponse mCatResponse;

	private boolean loadingNextPageData;
	private boolean isModifySearchDialogOpen;
	private boolean mScrollUp;
	private boolean mIsFreshSearch = true;
	private Spinner mCatchooser;
	private ArrayAdapter<String> mCategoryAdapter;
	static String dealID;
	CompanyDetail compListResp;
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
	ArrayList<CityOrLocality> localityList;
	ArrayList<String> ids = new ArrayList<String>();
	TextView mainSearchButton;
	ArrayList<String> selectedLocalityindex;
	LinearLayout wholeSearchBoxContainer;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				mIsFreshSearch = false;
				Bundle bundle = data.getExtras();
				mClResponse = bundle.getParcelable(AppConstants.COMP_LIST_DATA);
				mLocalitySelectorDao = bundle.getParcelable(AppConstants.LOCALITY_DAO_DATA);
				mClRequest = bundle.getParcelable(AppConstants.DATA_LIST_REQUEST);
				mSelctorResp = bundle.getParcelable(AppConstants.REFINE_ATTR_RESPONSE);
				mCatResponse = bundle.getParcelable(AppConstants.REFINE_CAT_RESPONSE);
				mRecordsFoundView.setText(mClResponse.getTotalrecordFound() + " " + getResources().getString(R.string.deal_found));
				// initNavigationButton(mClResponse.getPagesCount());
				addAnEmptyRow();
				ArrayList<CompanyDesc> compListData = mClResponse.getCompanyArrayList();
				mCompListDealAdapter.setData(compListData);
				mCompListDealAdapter.notifyDataSetChanged();
			}
		} else if (requestCode == 2) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				userName = bundle.getString("name");
				userNo = bundle.getString("phoneNo");
				dealDownload();
			}
		} else if (resultCode == RESULT_OK
				&& requestCode == AppConstants.CITY_REQUEST) {
			if (!selectedCity.equalsIgnoreCase(data.getStringExtra("CITY_NAME"))) {
				localityItems = null;
				ids = null;
				selectedLocalityindex = null;
				currentLocality.setText("Choose your Area");
			}
			selectedCity = data.getStringExtra("CITY_NAME");
			currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCity + "</b>"));
			int index = data.getIntExtra("CITY_INDEX", 0);
			if(index==-1) {
				city_id =-1;
			}else {
				city_id = cityList.get(index).getId();
			}
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
				currentLocality.setText(Html.fromHtml("Your Selected Area " + "<b>" + locality + "</b>"));
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AnalyticsHelper.logEvent(FlurryEventsConstants.APPLICATION_DEALS);
		setContentView(R.layout.activity_mydeals_activity1);
		store = MaxisStore.getStore(this);

		Bundle bundle = getIntent().getExtras();

		mClRequest = bundle.getParcelable(AppConstants.DATA_LIST_REQUEST);
		mClResponse = bundle.getParcelable(AppConstants.COMP_LIST_DATA);

		mSubcatResponse = bundle.getParcelable(AppConstants.DATA_SUBCAT_RESPONSE);
		ImageLoader.initialize(DealsActivity.this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(DealsActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler = (ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);

		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mRecordsFoundView = (TextView) findViewById(R.id.col_records_found);

		mCompanyList = (ListView) findViewById(R.id.col_company_list);


		mHeaderTitle = (TextView) findViewById(R.id.header_title);
		mHeaderTitle.setText("Deals");

		mViewAllOnMap = (TextView) findViewById(R.id.col_view_on_map);
		mViewAllOnMap.setOnClickListener(this);

		findViewById(R.id.col_view_on_map1).setOnClickListener(this);

		mRefineSearchView = (TextView) findViewById(R.id.col_refine_search);
		mRefineSearchView.setOnClickListener(this);

		mRefineSearchView1 = (TextView) findViewById(R.id.col_refine_search1);
		mRefineSearchView1.setOnClickListener(this);

		mCatchooser = (Spinner) findViewById(R.id.deal_cat_chooser);
		if (mSubcatResponse != null) {
			ArrayList<String> catNames = new ArrayList<String>();
			catNames.add("All Categories");
			for (SubCategory cName : mSubcatResponse.getCategories()) {
				catNames.add(cName.getCategoryTitle());
			}

			mCategoryAdapter = new ArrayAdapter<String>(DealsActivity.this,
					R.layout.spinner_item, catNames);
			mCatchooser.setAdapter(mCategoryAdapter);
			mCatchooser.setVisibility(View.VISIBLE);
		} else {
			mCatchooser.setVisibility(View.GONE);
		}


		mCatchooser.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View arg1,
					int position, long arg3) {
				if (position > 0) {
					mRefineSearchView1.setText("Modify Result(s)");
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(FlurryEventsConstants.Sub_Category_Title,
							mSubcatResponse.getCategories().get(position - 1)
							.getCategoryTitle().trim());
					map.put(FlurryEventsConstants.Sub_Category_Id,
							mSubcatResponse.getCategories().get(position - 1)
							.getCategoryId().trim());
					AnalyticsHelper.logEvent(
							FlurryEventsConstants.SUB_CATEGORY, map);
					showDealListing(mSubcatResponse.getCategories().get(
							position - 1));
					mSelctorResp = null;
					// }
				} else {
					mRefineSearchView1.setText("Filter Deals");
					showDealListing();
					mSelctorResp = null;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }
		});
		mCompanyList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				AnalyticsHelper.logEvent(FlurryEventsConstants.DEAL_LIST_ITEM_CLICK);
				Log.e("manish", "inside onclick");

				if (position == (mCompListDealAdapter.getCount() - 1)
						&& position != 0
						&& mClResponse.getTotalrecordFound() > 10) {
					// do nothing

				} else {
					CompanyDetailController controller = new CompanyDetailController(
							DealsActivity.this, Events.DEAL_DETAIL);
					String id = ((CompanyDesc) mCompListDealAdapter
							.getItem(position)).getCompId();
					DetailRequest detailRequest = new DetailRequest(
							DealsActivity.this, id, true,
							((CompanyDesc) mCompListDealAdapter
									.getItem(position)).getCat_id());
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
							&& !isModifySearchDialogOpen && mScrollUp && mClResponse.getTotalrecordFound()>100) {
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

		if (mClResponse != null && mClResponse.getTotalrecordFound() <= 10) {
			// Add one blank record.
			CompanyDesc desc = new CompanyDesc();
			desc.setCompId("-1");
			mClResponse.getCompanyArrayList().add(desc);
		}
		if (mClResponse != null) {

			mRecordsFoundView.setText(mClResponse.getTotalrecordFound() + " "
					+ getResources().getString(R.string.deal_found));
			ArrayList<CompanyDesc> compListData = mClResponse
					.getCompanyArrayList();

			mCompListDealAdapter = new CompanyListDealAdapter(
					DealsActivity.this, false);
			mCompListDealAdapter.setData(compListData);
			mCompanyList.setAdapter(mCompListDealAdapter);
		} else {
			showDealListing();
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
				if (!isAdvanceSearchLayoutOpen) {
					isAdvanceSearchLayoutOpen = true;
					advanceSearchLayout.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});
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
		AnalyticsHelper.trackSession(DealsActivity.this, AppConstants.Deal_Listing);
	}

	@Override
	public Activity getMyActivityReference() {
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
			this.finish();
			break;
		case R.id.mainSearchButton:
			mSearchEditText
			.setText(mSearchEditText.getText().toString().trim());

			String JSON_EXTRA = jsonForSearch();
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
			break;
		case R.id.show_profile_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.SHOW_PROFILE_CLICK);
			onProfileClick();
			break;

		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			showHomeScreen();
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

		case R.id.col_refine_search:
		case R.id.col_refine_search1:

			if (mSubcatResponse != null) {
				if (mCatchooser.getSelectedItem().toString()
						.equalsIgnoreCase("All Categories")) {
					Log.e("manish", "inside onclick");
					Toast.makeText(getApplicationContext(),
							"Please select a category first.",
							Toast.LENGTH_SHORT).show();
					mCatchooser.performClick();
				} else {
					refineSearch();
				}
			} else {
				refineSearch();
			}
			AnalyticsHelper.logEvent(FlurryEventsConstants.MODIFY_SEARCH_CLICK);
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
				Intent cityIntent = new Intent(DealsActivity.this,
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
				Intent localityIntent = new Intent(DealsActivity.this,
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
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.REFINE_ATTRIBUTES) {
			Message catRefine = (Message) screenData;
			handler.sendMessage(catRefine);
			return;
		} else if (event == Events.DOWNLOAD_DEAL) {
			Message message = (Message) screenData;
			handler.sendMessage(message);
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
			} else if (event == Events.COMBIND_LISTING_PAGINATION) {
				if (response.getPayload() instanceof CompanyListResponse) {
					CompanyListResponse compListResponse = (CompanyListResponse) response
							.getPayload();
					if (compListResponse.getErrorCode() != 0) {
						message.obj = getResources().getString(
								R.string.communication_failure);
					} else {
						if (compListResponse.getCompanyArrayList().size() < 1) {
							message.obj = new String(getResources().getString(
									R.string.no_result_found));
						} else {
							message.arg1 = 0;
							message.obj = compListResponse;
						}
					}
				} else {
					message.obj = new String(getResources().getString(
							R.string.communication_failure));
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
			} else {
				if (response.getPayload() instanceof CompanyListResponse) {
					mClResponse = (CompanyListResponse) response.getPayload();
					if (mClResponse.getErrorCode() != 0) {
						message.obj = getResources().getString(
								R.string.communication_failure);
						// clResponse.getServerMessage() + " " +
						// clResponse.getErrorCode();
					} else {
						if (mClResponse.getCompanyArrayList().size() < 1) {
							message.obj = new String("No Result Found");
						} else {
							message.arg1 = 0;
							message.obj = mClResponse;
						}
					}
				} else {
					message.obj = new String("Internal Error");
				}
			}

			handler.sendMessage(message);
		}
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE || msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.COMBIND_DEAL_LISTING_NEW_LISTING_PAGE) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				mRecordsFoundView.setText(mClResponse.getTotalrecordFound() + " " + getResources().getString(R.string.deal_found));
				// initNavigationButton(mClResponse.getPagesCount());
				addAnEmptyRow();
				ArrayList<CompanyDesc> compListData = mClResponse.getCompanyArrayList();
				mCompListDealAdapter = new CompanyListDealAdapter(DealsActivity.this, false);
				mCompListDealAdapter.setData(compListData);
				mCompListDealAdapter.notifyDataSetChanged();
				mCompanyList.setAdapter(mCompListDealAdapter);
				isModifySearchDialogOpen = false;
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
				if (mClResponse.getPageNumber() == 10
						|| mClResponse.getTotalrecordFound() == mClResponse
						.getCompanyArrayList().size()) {
					// Add one blank record.
					CompanyDesc desc = new CompanyDesc();
					desc.setCompId("-1");
					mClResponse.getCompanyArrayList().add(desc);
				}
				mCompListDealAdapter.setData(mClResponse.getCompanyArrayList());
				mCompListDealAdapter.notifyDataSetChanged();
			}
			stopSppiner();
		} else if (msg.arg2 == Events.DEAL_DETAIL) {
			if (msg.arg1 == 1) {
				showInfoDialog(getResources().getString(
						R.string.network_unavailable));
			} else {
				compListResp = (CompanyDetail) msg.obj;
				if (!StringUtil.isNullOrEmpty(compListResp.getId())) {
					Intent intent = new Intent(DealsActivity.this,
							DealDetailActivity.class);
					if (mClRequest.isBySearch())
						intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD,
								mClRequest.getKeywordOrCategoryId());
					intent.putExtra(AppConstants.COMP_DETAIL_DATA, compListResp);
					intent.putExtra(AppConstants.DATA_LIST_REQUEST, mClRequest);

					startActivity(intent);
				} else {
					showInfoDialog(getResources().getString(
							R.string.no_result_found));
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
		} else if (msg.arg2 == Events.DOWNLOAD_DEAL) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else if (msg.arg1 == 0) {
				MaxisResponse genResp = (MaxisResponse) msg.obj;
				showInfoDialog(getString(R.string.download_deal));
			}
			stopSppiner();
		} else if (msg.arg2 == Events.CITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				cityList = glistRes.getCityOrLocalityList();
				Intent intent = new Intent(DealsActivity.this,
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
				Intent intent = new Intent(DealsActivity.this,
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
	protected void onDestroy() {
		ImageLoader.clearCache();
		super.onDestroy();
	}

	private void showDealListing(SubCategory cat) {
		CombindListingController listingController = new CombindListingController(
				DealsActivity.this,
				Events.COMBIND_DEAL_LISTING_NEW_LISTING_PAGE);
		mClRequest = new CombinedListRequest(DealsActivity.this);
		mClRequest.setBySearch(false);
		mClRequest.setCompanyListing(false);
		// mClRequest.setKeywordOrCategoryId("-1");
		mClRequest.setKeywordOrCategoryId(cat.getCategoryId());
		mClRequest.setLatitude(GPS_Data.getLatitude());
		mClRequest.setLongitude(GPS_Data.getLongitude());
		mClRequest.setCategoryTitle(cat.getCategoryTitle());
		mClRequest.setParentThumbUrl(cat.getThumbUrl());
		mClRequest.setGroupActionType(cat.getmGroupActionType());
		mClRequest.setGroupType(cat.getMgroupType());
		setRequest(mClRequest);
		startSppiner();
		listingController.requestService(mClRequest);

	}

	private void showDealListing() {
		CombindListingController listingController = new CombindListingController(
				DealsActivity.this,
				Events.COMBIND_DEAL_LISTING_NEW_LISTING_PAGE);
		mClRequest = new CombinedListRequest(DealsActivity.this);
		mClRequest.setBySearch(false);
		mClRequest.setCompanyListing(false);
		// mClRequest.setKeywordOrCategoryId("-1");
		mClRequest.setKeywordOrCategoryId("");
		mClRequest.setLatitude(GPS_Data.getLatitude());
		mClRequest.setLongitude(GPS_Data.getLongitude());
		// mClRequest.setCategoryTitle(cat.getCategoryTitle());
		// mClRequest.setParentThumbUrl(cat.getThumbUrl());
		// mClRequest.setGroupActionType(cat.getmGroupActionType());
		// mClRequest.setGroupType(cat.getMgroupType());
		setRequest(mClRequest);
		startSppiner();
		listingController.requestService(mClRequest);

	}

	private void loadPageData(int pageNumber) {
		CombindListingController listingController = new CombindListingController(
				DealsActivity.this, Events.COMBIND_LISTING_PAGINATION);
		mClRequest.setPageNumber(pageNumber);
		startSppiner();
		listingController.requestService(mClRequest);
	}

	@Override
	public void onPositiveDialogButton(int id) {
		if (id == CustomDialog.CONFIRMATION_DIALOG) {
			if (mCatchooser.getSelectedItem().toString()
					.equalsIgnoreCase("All Categories")) {
				Log.e("manish", "inside onclick");
				Toast.makeText(getApplicationContext(),
						"Please select a category first.", Toast.LENGTH_SHORT)
						.show();
				mCatchooser.performClick();
			} else {
				refineSearch();
				isModifySearchDialogOpen = false;

			}
			//
		} else if (id == CustomDialog.DATA_USAGE_DIALOG) {
			showMap();
		} else {
			super.onPositiveDialogButton(id);
		}
	}

	private void showMap() {
		if (isLocationAvailable()) {
			Intent intent = new Intent(DealsActivity.this,
					ViewAllOnMapActivity.class);
			intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD,
					mClRequest.getKeywordOrCategoryId());
			intent.putExtra(AppConstants.MAP_ALL_TITLE, mHeaderTitle.getText()
					.toString().trim());
			intent.putExtra(AppConstants.IS_SEARCH_KEYWORD,
					mClRequest.isBySearch());
			intent.putExtra(AppConstants.IS_DEAL_LIST,
					!mClRequest.isCompanyListing());
			intent.putExtra(AppConstants.COMP_DETAIL_DATA, compListResp);
			intent.putExtra(AppConstants.DATA_LIST_REQUEST, mClRequest);
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

	private void refineSearch() {
		if (mClRequest.isBySearch()) {
			if (mIsFreshSearch) {
				mCatResponse = new RefineCategoryResponse();
				if (mClResponse.getCategoryList() == null
						|| mClResponse.getCategoryList().size() < 1
						|| mClRequest.getKeywordOrCategoryId() != null
						|| mClRequest.getKeywordOrCategoryId()
						.equalsIgnoreCase("")) {
					showAlertDialog(getResources().getString(
							R.string.category_list_not_found));
					return;
				} else {
					if (mClRequest.isBySearch()) {
						ArrayList<CategoryRefine> catList = new ArrayList<CategoryRefine>();
						CategoryRefine categoryRefine = new CategoryRefine();
						categoryRefine.setCategoryId(mClRequest
								.getKeywordOrCategoryId());
						categoryRefine.setCategoryTitle(mClRequest
								.getCategoryTitle());
						catList.add(categoryRefine);
						mCatResponse.setCategories(catList);
					} else
						mCatResponse.setCategories(mClResponse
								.getCategoryList());
					{

					}
				}
				Intent intent = new Intent(DealsActivity.this,
						RefineSearchCategoryActivity.class);
				intent.putExtra(AppConstants.REFINE_CAT_RESPONSE, mCatResponse);
				intent.putExtra(AppConstants.DATA_LIST_REQUEST, mClRequest);
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

	private void displayRefineWithAttributeSpinnersPreloaded(
			RefineSelectorResponse selectorRes, int selectionMode) {
		Intent intent = new Intent(DealsActivity.this,
				RefineSearchActivity.class);
		intent.putExtra(AppConstants.REFINE_ATTR_RESPONSE, selectorRes);
		intent.putExtra(RefineSearchActivity.SELECTOR_MODE, selectionMode);
		intent.putExtra(AppConstants.DATA_LIST_REQUEST, mClRequest);
		// intent.putExtra(AppConstants.THUMB_URL, mCategoryThumbUrl);
		intent.putExtra(AppConstants.LOCALITY_DAO_DATA, mLocalitySelectorDao);
		startActivityForResult(intent, 1);
	}

	private void fetchRefineAttribute(String categoryId) {
		RefineAttributeController refineController = new RefineAttributeController(DealsActivity.this, Events.REFINE_ATTRIBUTES);
		startSppiner();
		RefineSearchRequest refineSearchRequest = new RefineSearchRequest();
		refineSearchRequest.setCategoryId(categoryId);
		refineSearchRequest.setDeal(!mClRequest.isCompanyListing());
		refineController.requestService(refineSearchRequest);
	}

	public void getDownloadDetails(Context context, String id) {
		dealID = id;

		if (store.isLoogedInUser()) {
			userNo = "60"+store.getUserMobileNumberToDispaly();
			userName = store.getUserName();
			dealDownload();

		} else {
			userNo = store.getAuthMobileNumber();
			userName = "";
			dealDownload();
			//			Intent intent = new Intent(context, DealForm.class);
			//			((Activity) context).startActivityForResult(intent, 2);

		}

	}

	public void dealDownload() {
		DownloadDealReq dealReq = new DownloadDealReq();
		dealReq.setName(userName);
		dealReq.setPhoneNo(userNo);
		dealReq.setDeal_id(dealID);

		DownloadDealController downloadDealController = new DownloadDealController(
				DealsActivity.this, Events.DOWNLOAD_DEAL);
		downloadDealController.fromDeal = true;
		startSppiner();
		downloadDealController.requestService(dealReq);
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
}
