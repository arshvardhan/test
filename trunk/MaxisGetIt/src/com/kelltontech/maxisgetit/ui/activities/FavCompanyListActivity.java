package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.FavCompanyListAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.CompanyDetailController;
import com.kelltontech.maxisgetit.controllers.CompanyDetailRemoveFavController;
import com.kelltontech.maxisgetit.controllers.FavouriteController;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.CompanyDesc;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.FavouriteCompanies;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.dao.SelectorDAO;
import com.kelltontech.maxisgetit.db.CityTable;
import com.kelltontech.maxisgetit.db.FavCompanysTable;
import com.kelltontech.maxisgetit.requests.DetailRequest;
import com.kelltontech.maxisgetit.requests.FavCompanyListRequest;
import com.kelltontech.maxisgetit.response.CompanyListResponse;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.response.RefineSelectorResponse;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class FavCompanyListActivity extends MaxisMainActivity {
	private ListView mCompanyList;
	private FavCompanyListAdapter mCompListAdapter;
	private TextView mRecordsFoundView;
	// private TextView mRefineSearchView;
	// private TextView mMoreLinkView;
	// private TextView mDealBtnView;
	// private TextView mViewAllOnMap;
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
	private FavCompanyListRequest mListRequest;
	private ImageView mProfileIconView;
	private ImageView mHeaderBackButton;
	// private TextView mCategoryTitle;
	// private RefineSelectorResponse mSelctorResp;
	// private RefineCategoryResponse mCatResponse;
	// private boolean mIsFreshSearch = true;
	private SelectorDAO mLocalitySelectorDao;
	private boolean loadingNextPageData = false;
	private boolean isModifySearchDialogOpen;
	private ImageView mHomeIconView;

	private ImageView mEditButton;
	private RelativeLayout mEditFunLayout;
	private RelativeLayout mDeleteFunLayout;
	private TextView markAll;
	private TextView cancel;
	private TextView deleteAll;

	private String userId;
	private boolean mScrollUp;
	private String compCatIdToCompare;
	private MaxisStore store;
	public static boolean isEdit = false;
	public static boolean isAllChecked;
	private String companyId;
	private String categoryId;
	private ArrayList<CompanyDesc> favList;
	public static ArrayList<CompanyDesc> compListData;
	private int index = -1;
	public static boolean isDeleteAllPressed = false;
	private boolean isCompanyListLongPressed = false;
	public static boolean isRemovedFromCompanyDetail = false;
	private int record;
	boolean isShown;

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
		setContentView(R.layout.activity_fav_company_list);
		AnalyticsHelper.logEvent(
				FlurryEventsConstants.APPLICATION_MY_FAVOURITES, true);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.cl_root_layout),
				this);
		mHeaderTitle = (TextView) findViewById(R.id.header_title);
		ImageLoader.initialize(FavCompanyListActivity.this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		userId = getIntent().getExtras().getString("UserID");
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(FavCompanyListActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);

		Bundle bundle = getIntent().getExtras();
		mHeaderTitle.setText(Html.fromHtml(getResources().getString(
				R.string.acc_my_fav)));
		mClResponse = bundle.getParcelable(AppConstants.COMP_LIST_DATA);
		record = mClResponse.getTotalrecordFound();
		mHeaderTitle.setVisibility(View.VISIBLE);

		mRecordsFoundView = (TextView) findViewById(R.id.fav_company__record_found);

		mRecordsFoundView.setText(mClResponse.getTotalrecordFound() + " "
				+ getResources().getString(R.string.fav_companies_found));

		mEditButton = (ImageView) findViewById(R.id.fav_company_edit_btn);
		mEditFunLayout = (RelativeLayout) findViewById(R.id.fav_company_record_found_layout);
		mDeleteFunLayout = (RelativeLayout) findViewById(R.id.fav_company_edit_layout);
		markAll = (TextView) findViewById(R.id.fav_company_mark_all_btn);
		cancel = (TextView) findViewById(R.id.fav_company_cancel_btn);
		deleteAll = (TextView) findViewById(R.id.fav_company_delete_btn);

		mEditButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AnalyticsHelper.logEvent(FlurryEventsConstants.EDIT_FAV_CLICK);
				isEdit = true;
				isAllChecked = false;
				mEditFunLayout.setVisibility(View.GONE);
				mDeleteFunLayout.setVisibility(View.VISIBLE);
				for (CompanyDesc companyDesc : compListData) {
					companyDesc.setChecked(false);
				}
				markAll.setText("Mark All");
				mCompListAdapter.notifyDataSetChanged();
			}
		});

		markAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isEdit) {
					ArrayList<CompanyDesc> compListData = mClResponse
							.getCompanyArrayList();
					isAllChecked = !isAllChecked;
					for (CompanyDesc companyDesc : compListData) {
						companyDesc.setChecked(isAllChecked);
					}
					mCompListAdapter.notifyDataSetChanged();
					if (isAllChecked) {
						markAll.setText("Unmark All");
					} else {
						markAll.setText("Mark All");
					}
				}
			}
		});

		deleteAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				favList = new ArrayList<CompanyDesc>();
				for (CompanyDesc companyDesc : compListData) {
					if (companyDesc.isChecked()) {
						favList.add(companyDesc);
					}
				}
				if (favList.size() > 0) {
					isDeleteAllPressed = true;
					showConfirmationDialog(
							CustomDialog.DELETE_CONFIRMATION_DIALOG,
							getString(R.string.fav_companies_remove_confirmation));
				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.fav_companies_plz_select),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mEditFunLayout.setVisibility(View.VISIBLE);
				mDeleteFunLayout.setVisibility(View.GONE);
				isEdit = false;
				mCompListAdapter.notifyDataSetChanged();
			}
		});

		compListData = mClResponse.getCompanyArrayList();
		if (compListData.size() <= 0) {
			mEditButton.setVisibility(View.GONE);
		} else {
			mEditButton.setVisibility(View.VISIBLE);
		}
		mCompanyList = (ListView) findViewById(R.id.col_company_list);
		mCompListAdapter = new FavCompanyListAdapter(
				FavCompanyListActivity.this, true, this);
		mCompListAdapter.setData(compListData);
		mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler = (ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mCompanyList.setAdapter(mCompListAdapter);
		mCompanyList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				CompanyDetailController controller = new CompanyDetailController(
						FavCompanyListActivity.this, Events.COMPANY_DETAIL);
				String id = ((CompanyDesc) mCompListAdapter.getItem(arg2))
						.getCompId();
				DetailRequest detailRequest = new DetailRequest(
						FavCompanyListActivity.this, id, false,
						((CompanyDesc) mCompListAdapter.getItem(arg2))
								.getCat_id());

				startSppiner();
				index = arg2;
				controller.requestService(detailRequest);
			}
		});

		mCompanyList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				isCompanyListLongPressed = true;
				companyId = ((CompanyDesc) mCompListAdapter.getItem(pos))
						.getCompId();
				categoryId = ((CompanyDesc) mCompListAdapter.getItem(pos))
						.getCat_id();
				compCatIdToCompare = companyId + "-" + categoryId;
				Log.e("manish", "inside on long click :" + compCatIdToCompare);
				showConfirmationDialog(CustomDialog.DELETE_CONFIRMATION_DIALOG,
						getString(R.string.fav_companies_remove_confirmation));
				index = pos;
				return true;
			}
		});

		mCompanyList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView listView,
					int scrollState) {
				if (!isShown
						&& mCompanyList.getLastVisiblePosition() == mCompanyList
								.getAdapter().getCount() - 1) {
					isShown = true;
					if (record > 10 && compListData.size() < record) {
						if (compListData.size() < 10) {
							loadPageData(mClResponse.getPageNumber());
						} else {
							if (record > mCompanyList.getAdapter().getCount())
								loadPageData(mClResponse.getPageNumber() + 1);
						}
					}
				} else {
					isShown = false;
				}

			}

			@Override
			public void onScroll(AbsListView listView, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});

		store = MaxisStore.getStore(this);

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
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.REFINE_ATTRIBUTES) {
			Message catRefine = (Message) screenData;
			handler.sendMessage(catRefine);
			return;
		} else if (event == Events.COMPANY_DETAIL_REMOVE_FAV) {
			handler.sendMessage((Message) screenData);
			return;
		} else if (event == Events.CITY_LISTING
				|| event == Events.LOCALITY_LISTING) {
			Message message = (Message) screenData;
			handler.sendMessage(message);
		} else {
			Response response = (Response) screenData;
			Message message = new Message();
			message.arg2 = event;
			message.arg1 = 1;
			if (response.isError()) {
				message.obj = response.getErrorText();
			} else {
				if (event == Events.COMBIND_LISTING_PAGINATION) {
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
				} else {
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
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.COMPANY_DETAIL) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				CompanyDetail compListResp = (CompanyDetail) msg.obj;
				if (!StringUtil.isNullOrEmpty(compListResp.getId())) {
					Intent intent = new Intent(FavCompanyListActivity.this,
							CompanyDetailActivity.class);
					intent.putExtra(AppConstants.COMP_DETAIL_DATA, compListResp);
					intent.putExtra(AppConstants.THUMB_URL, mCategoryThumbUrl);
					intent.putExtra(AppConstants.IS_DEAL_LIST, false);
					intent.putExtra("index", index + "");

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
						mClResponse.getCompanyArrayList(), true);
				mClResponse.setCompanyList(oldResponse.getCompanyArrayList());
				compListData = mClResponse.getCompanyArrayList();
				mCompListAdapter.setData(compListData);
				mCompListAdapter.notifyDataSetChanged();
			}
			stopSppiner();
		} else if (msg.arg2 == Events.REFINE_ATTRIBUTES) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				displayRefineWithAttributeSpinnersPreloaded(
						(RefineSelectorResponse) msg.obj,
						RefineSearchActivity.ATTR_SELECTION);
			}
			stopSppiner();
		} else if (msg.arg2 == Events.COMPANY_DETAIL_REMOVE_FAV) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
				stopSppiner();
			} else {
				Toast.makeText(FavCompanyListActivity.this,
						getString(R.string.selected_company_remove_from_fav),
						Toast.LENGTH_SHORT).show();
				 if ((!isCompanyListLongPressed) && (favList != null) && (record - favList.size() > 0)) {
					AnalyticsHelper.logEvent(FlurryEventsConstants.DELETE_FAV_CLICK);
					record = record - favList.size();
					mRecordsFoundView.setText(record
							+ " "
							+ getResources().getString(
									R.string.fav_companies_found));
				} else if (isCompanyListLongPressed && (record - 1 > 0)) {
					AnalyticsHelper.logEvent(FlurryEventsConstants.FAV_LIST_ITEM_LONG_PRESS);
					record = record - 1;
					mRecordsFoundView.setText(record
							+ " "
							+ getResources().getString(
									R.string.fav_companies_found));
					isCompanyListLongPressed = false;
				} else {
					record = 0;
					mRecordsFoundView.setText(getResources().getString(
							R.string.fav_companies__no_found));
					mEditButton.setVisibility(View.GONE);
					mEditFunLayout.setVisibility(View.VISIBLE);
					mDeleteFunLayout.setVisibility(View.GONE);
				}
				FavouriteController mfController = new FavouriteController(
						FavCompanyListActivity.this,
						Events.COMBIND_LISTING_PAGINATION);
				mListRequest = new FavCompanyListRequest();
				mListRequest.setUserId(userId);
				mListRequest.setPageNumber(1);
				mfController.requestService(mListRequest);
			}
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
				Intent intent = new Intent(FavCompanyListActivity.this,
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
				Intent intent = new Intent(FavCompanyListActivity.this,
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
		return null;
	}

	public JSONObject verifyInput(String companyId, String categoryId)
			throws JSONException {
		JSONObject postJson = new JSONObject();
		if (StringUtil.isNullOrEmpty(companyId)) {
			return null;
		} else {
			postJson.put("user_id", userId);
		}
		JSONArray companyCategoryArr = new JSONArray();
		JSONObject comIdCatId = new JSONObject();
		if (StringUtil.isNullOrEmpty(companyId)) {
			return null;
		} else {
			comIdCatId.put("company_id", companyId);
		}
		if (StringUtil.isNullOrEmpty(categoryId)) {
			return null;
		} else {
			comIdCatId.put("category_id", categoryId);
		}
		companyCategoryArr.put(comIdCatId);
		postJson.put("company_category", companyCategoryArr);
		return postJson;
	}

	public JSONObject validateData(ArrayList<CompanyDesc> favList)
			throws JSONException {
		JSONObject postJson = new JSONObject();
		if (StringUtil.isNullOrEmpty(userId)) {
			return null;
		} else {
			postJson.put("user_id", userId);
		}
		JSONArray companyCategoryArr = new JSONArray();

		for (int i = 0; i < favList.size(); i++) {
			JSONObject comIdCatId = new JSONObject();
			String companyId = favList.get(i).getCompId();
			String categoryId = favList.get(i).getCat_id();
			if (StringUtil.isNullOrEmpty(companyId)) {
				return null;
			} else {
				comIdCatId.put("company_id", companyId);
			}
			if (StringUtil.isNullOrEmpty(categoryId)) {
				return null;
			} else {
				comIdCatId.put("category_id", categoryId);
			}
			companyCategoryArr.put(comIdCatId);
		}
		postJson.put("company_category", companyCategoryArr);
		return postJson;
	}

	private void loadPageData(int pageNumber) {
		FavouriteController mfController = new FavouriteController(
				FavCompanyListActivity.this, Events.COMBIND_LISTING_PAGINATION);
		startSppiner();
		mListRequest = new FavCompanyListRequest();
		mListRequest.setUserId(userId);
		mListRequest.setPageNumber(pageNumber);
		mfController.requestService(mListRequest);
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
			isEdit = false;
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			AnalyticsHelper
					.endTimedEvent(FlurryEventsConstants.APPLICATION_MY_FAVOURITES);
			this.finish();
			break;
		case R.id.col_refine_search:
		case R.id.col_refine_search1:
			// refineSearch();
			AnalyticsHelper.logEvent(FlurryEventsConstants.MODIFY_SEARCH_CLICK);
			break;
		case R.id.col_deal_btn:
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
			finish();
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
				Intent cityIntent = new Intent(FavCompanyListActivity.this,
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
				Intent localityIntent = new Intent(FavCompanyListActivity.this,
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

	/*
	 * private void refineSearch() { // if (mClRequest.isBySearch() ||
	 * (mClRequest.getGroupActionType().trim().equalsIgnoreCase(AppConstants.
	 * GROUP_ACTION_TYPE_CATEGORY_LIST_FOR_GROUP) &&
	 * (mClRequest.getGroupType().trim
	 * ().equalsIgnoreCase(AppConstants.GROUP_TYPE_CATEGORY)))) { // if
	 * (mIsFreshSearch) { // mCatResponse = new RefineCategoryResponse(); // if
	 * (mClResponse.getCategoryList() == null ||
	 * mClResponse.getCategoryList().size() < 1) { //
	 * showAlertDialog(getResources
	 * ().getString(R.string.category_list_not_found)); // return; // } //
	 * mCatResponse.setCategories(mClResponse.getCategoryList()); // Intent
	 * intent = new Intent(FavCompanyListActivity.this,
	 * RefineSearchCategoryActivity.class); //
	 * intent.putExtra(AppConstants.REFINE_CAT_RESPONSE, mCatResponse); //
	 * intent.putExtra(AppConstants.DATA_LIST_REQUEST, mClRequest); //
	 * startActivityForResult(intent, 1); // } else { // if (mSelctorResp !=
	 * null) { // displayRefineWithAttributeSpinnersPreloaded(mSelctorResp,
	 * RefineSearchActivity.ATTR_SELECTION_BY_SEARCH); // } else { //
	 * fetchRefineAttribute(mClRequest.getSelectedCategoryBySearch()); // } // }
	 * // } else { if (mSelctorResp != null) {
	 * displayRefineWithAttributeSpinnersPreloaded(mSelctorResp,
	 * RefineSearchActivity.ATTR_SELECTION); // } else //
	 * fetchRefineAttribute(mClRequest.getKeywordOrCategoryId()); } }
	 */


	private void displayRefineWithAttributeSpinnersPreloaded(
			RefineSelectorResponse selectorRes, int selectionMode) {
		Intent intent = new Intent(FavCompanyListActivity.this,
				RefineSearchActivity.class);
		intent.putExtra(AppConstants.REFINE_ATTR_RESPONSE, selectorRes);
		intent.putExtra(RefineSearchActivity.SELECTOR_MODE, selectionMode);
		// intent.putExtra(AppConstants.DATA_LIST_REQUEST, mClRequest);
		intent.putExtra(AppConstants.THUMB_URL, mCategoryThumbUrl);
		intent.putExtra(AppConstants.LOCALITY_DAO_DATA, mLocalitySelectorDao);
		startActivityForResult(intent, 1);
	}

	@Override
	public void onPositiveDialogButton(int id) {
		if (id == CustomDialog.CONFIRMATION_DIALOG) {
			// refineSearch();
			// isModifySearchDialogOpen = false;
		} else if (id == CustomDialog.DELETE_CONFIRMATION_DIALOG) {
			if (isDeleteAllPressed) {
				startSppiner();
				removeFromFav(favList);
			} else {
				startSppiner();
				removeFromFav(companyId, categoryId);
			}
		} else if (id == CustomDialog.DATA_USAGE_DIALOG) {
			showMap();
		} else {
			super.onPositiveDialogButton(id);
		}
	}

	private void showMap() {
		if (isLocationAvailable()) {
			Intent intent = new Intent(FavCompanyListActivity.this,
					ViewAllOnMapActivity.class);
			intent.putExtra(AppConstants.MAP_ALL_TITLE, mHeaderTitle.getText()
					.toString().trim());
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

	private void removeFromFav(ArrayList<CompanyDesc> favList) {

		compListData.removeAll(favList);
		mCompListAdapter.notifyDataSetChanged();
		String compCatIdToCom;
		FavCompanysTable FavCompTable = new FavCompanysTable(
				(MyApplication) getApplication());
		ArrayList<FavouriteCompanies> companyIdCategoryId = new ArrayList<FavouriteCompanies>();
		for (int i = 0; i < favList.size(); i++) {
			compCatIdToCom = favList.get(i).getCompId() + "-"
					+ favList.get(i).getCat_id();
			FavouriteCompanies favCompany = new FavouriteCompanies();
			favCompany.setFavComIdCategoryId(compCatIdToCom);
			companyIdCategoryId.add(favCompany);
		}
		FavCompTable.delFavCompaniesList(companyIdCategoryId);

		JSONObject postJson = null;
		try {
			postJson = validateData(favList);
		} catch (JSONException e) {
			showAlertDialog(getResources().getString(R.string.internal_error));
			AnalyticsHelper.onError(FlurryEventsConstants.DATA_VALIDATION_ERR,
					"CompanyDetailActivity : "
							+ AppConstants.DATA_VALIDATION_ERROR_MSG, e);
		}
		if (postJson == null) {
			return;
		}
		CompanyDetailRemoveFavController removeFavController = new CompanyDetailRemoveFavController(
				FavCompanyListActivity.this, Events.COMPANY_DETAIL_REMOVE_FAV);
		removeFavController.fromComapnyList =  false;
		removeFavController.requestService(postJson);
	}

	private void removeFromFav(String companyId, String categoryId) {
		compListData.remove(index);
		mCompListAdapter.notifyDataSetChanged();
		FavCompanysTable FavCompTable = new FavCompanysTable(
				(MyApplication) getApplication());
		ArrayList<FavouriteCompanies> companyIdCategoryId = new ArrayList<FavouriteCompanies>();
		FavouriteCompanies favCompany = new FavouriteCompanies();
		favCompany.setFavComIdCategoryId(compCatIdToCompare);
		companyIdCategoryId.add(favCompany);
		FavCompTable.delFavCompaniesList(companyIdCategoryId);

		JSONObject postJson = null;
		try {
			postJson = verifyInput(companyId, categoryId);
		} catch (JSONException e) {
			showAlertDialog(getResources().getString(R.string.internal_error));
			AnalyticsHelper.onError(FlurryEventsConstants.DATA_VALIDATION_ERR,
					"CompanyDetailActivity : "
							+ AppConstants.DATA_VALIDATION_ERROR_MSG, e);
		}
		if (postJson == null) {
			return;
		}
		CompanyDetailRemoveFavController removeFavController = new CompanyDetailRemoveFavController(
				FavCompanyListActivity.this, Events.COMPANY_DETAIL_REMOVE_FAV);
		removeFavController.fromComapnyList =  false;
		removeFavController.requestService(postJson);
	}

	public void textChange(boolean ischeck) {
		if (ischeck) {
			markAll.setText("Unmark All");
			isAllChecked = true;
		} else {
			markAll.setText("Mark All");
			isAllChecked = false;
			isDeleteAllPressed = false;
		}
	}

	@Override
	public void onBackPressed() {
		isEdit = false;
		super.onBackPressed();
	}

	@Override
	protected void onResume() {

		mCompListAdapter.notifyDataSetChanged();
		if (isRemovedFromCompanyDetail && record > 0) {
			if (record - 1 > 0) {
				record = record - 1;
				mRecordsFoundView.setText(record
						+ " "
						+ getResources()
								.getString(R.string.fav_companies_found));
			} else {
				record = 0;
				mRecordsFoundView.setText(getResources().getString(
						R.string.fav_companies__no_found));
				mDeleteFunLayout.setVisibility(View.GONE);
				mEditFunLayout.setVisibility(View.VISIBLE);
				mEditButton.setVisibility(View.GONE);
			}
			isRemovedFromCompanyDetail = false;
		} else if (record > 0) {
			mRecordsFoundView.setText(record + " "
					+ getResources().getString(R.string.fav_companies_found));
		} else {
			record = 0;
			mRecordsFoundView.setText(getResources().getString(
					R.string.fav_companies__no_found));
			mDeleteFunLayout.setVisibility(View.GONE);
			mEditFunLayout.setVisibility(View.VISIBLE);
			mEditButton.setVisibility(View.GONE);
		}
		super.onResume();
		
		AnalyticsHelper.trackSession(FavCompanyListActivity.this, AppConstants.MyFavourite);
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
			if(index==-1)
			{
				city_id =-1;
			}else
			{
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
