package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
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
import com.kelltontech.maxisgetit.adapters.DealsCategoryAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.CombindListingController;
import com.kelltontech.maxisgetit.controllers.CompanyDetailController;
import com.kelltontech.maxisgetit.controllers.DownloadDealController;
import com.kelltontech.maxisgetit.controllers.RefineAttributeController;
import com.kelltontech.maxisgetit.dao.CategoryRefine;
import com.kelltontech.maxisgetit.dao.CompanyDesc;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.dao.OutLetDetails;
import com.kelltontech.maxisgetit.dao.SelectorDAO;
import com.kelltontech.maxisgetit.dao.SubCategory;
import com.kelltontech.maxisgetit.parsers.GenralResponseParser;
import com.kelltontech.maxisgetit.requests.CombinedListRequest;
import com.kelltontech.maxisgetit.requests.DetailRequest;
import com.kelltontech.maxisgetit.requests.DownloadDealReq;
import com.kelltontech.maxisgetit.requests.RefineSearchRequest;
import com.kelltontech.maxisgetit.response.CompanyListResponse;
import com.kelltontech.maxisgetit.response.RefineCategoryResponse;
import com.kelltontech.maxisgetit.response.RefineSelectorResponse;
import com.kelltontech.maxisgetit.response.SubCategoryResponse;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.ui.widgets.HorizontalListView;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class DealsActivity extends MaxisMainActivity {

	// private HorizontalListView mCategoryListView;
	private SubCategoryResponse mSubcatResponse;
	private ImageView mProfileIconView;
	private ImageView mHeaderBackButton;
	private ImageView mHomeIconView;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private TextView mHeaderTitle;
	// private DealsCategoryAdapter mCatAdapter;
	private SubCategory mSelectdCategory;
	private TextView mRecordsFoundView;

	private TextView mViewAllOnMap;
	private TextView mRefineSearchView;

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
				mRecordsFoundView.setText(mClResponse.getTotalrecordFound()
						+ " " + getResources().getString(R.string.deal_found));
				// initNavigationButton(mClResponse.getPagesCount());
				ArrayList<CompanyDesc> compListData = mClResponse
						.getCompanyArrayList();
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
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mydeals_activity1);
		store = MaxisStore.getStore(this);

		Bundle bundle = getIntent().getExtras();

		mClRequest = bundle.getParcelable(AppConstants.DATA_LIST_REQUEST);
		mClResponse = bundle.getParcelable(AppConstants.COMP_LIST_DATA);

		mSubcatResponse = bundle
				.getParcelable(AppConstants.DATA_SUBCAT_RESPONSE);
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

		// mCategoryListView = (HorizontalListView)
		// findViewById(R.id.category_list);

		mHeaderTitle = (TextView) findViewById(R.id.header_title);
		mHeaderTitle.setText("Deals");

		mViewAllOnMap = (TextView) findViewById(R.id.col_view_on_map);
		mViewAllOnMap.setOnClickListener(this);

		findViewById(R.id.col_view_on_map1).setOnClickListener(this);

		mRefineSearchView = (TextView) findViewById(R.id.col_refine_search);
		mRefineSearchView.setOnClickListener(this);

		findViewById(R.id.col_refine_search1).setOnClickListener(this);

		// mHeaderTitle.setText(parCategory.getCategoryTitle()); TODO
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

		/*
		 * mCatAdapter = new DealsCategoryAdapter(DealsActivity.this);
		 * mCatAdapter.setData(mSubcatResponse.getCategories());
		 * mCategoryListView.setAdapter(mCatAdapter);
		 * mCategoryListView.setVisibility(View.GONE);
		 * mCategoryListView.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> adapter, View arg1,
		 * int position, long arg3) {
		 * 
		 * mSelectdCategory = (SubCategory) adapter
		 * .getItemAtPosition(position); if
		 * (!StringUtil.isNullOrEmpty(mSelectdCategory
		 * .getCategoryTitle().trim()) &&
		 * !StringUtil.isNullOrEmpty(mSelectdCategory .getCategoryId().trim()))
		 * { HashMap<String, String> map = new HashMap<String, String>();
		 * map.put(FlurryEventsConstants.Sub_Category_Title,
		 * mSelectdCategory.getCategoryTitle().trim());
		 * map.put(FlurryEventsConstants.Sub_Category_Id,
		 * mSelectdCategory.getCategoryId().trim()); AnalyticsHelper.logEvent(
		 * FlurryEventsConstants.SUB_CATEGORY, map); }
		 * 
		 * 
		 * if (mSelectdCategory .getmGroupActionType() .trim()
		 * .equalsIgnoreCase( AppConstants.GROUP_ACTION_TYPE_DEAL)) {
		 * showDealListing(mSelectdCategory); }
		 * 
		 * } });
		 */

		mCatchooser.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View arg1,
					int position, long arg3) {
				if (position > 0) {
					// mSelectdCategory = (SubCategory) adapter
					// .getItemAtPosition(position);
					// if (!StringUtil.isNullOrEmpty(mSelectdCategory
					// .getCategoryTitle().trim())
					// && !StringUtil.isNullOrEmpty(mSelectdCategory
					// .getCategoryId().trim())) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(FlurryEventsConstants.Sub_Category_Title,
							mSubcatResponse.getCategories().get(position - 1)
									.getCategoryTitle().trim());
					map.put(FlurryEventsConstants.Sub_Category_Id,
							mSubcatResponse.getCategories().get(position - 1)
									.getCategoryId().trim());
					AnalyticsHelper.logEvent(
							FlurryEventsConstants.SUB_CATEGORY, map);
					// }
					// if (mSelectdCategory
					// .getmGroupActionType()
					// .trim()
					// .equalsIgnoreCase(
					// AppConstants.GROUP_ACTION_TYPE_DEAL)) {
					showDealListing(mSubcatResponse.getCategories().get(
							position - 1));
					mSelctorResp = null;
					// }
				} else {
					showDealListing();
					mSelctorResp = null;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		mCompanyList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.e("manish", "inside onclick");
				CompanyDetailController controller = new CompanyDetailController(
						DealsActivity.this, Events.DEAL_DETAIL);
				String id = ((CompanyDesc) mCompListDealAdapter.getItem(arg2))
						.getCompId();
				DetailRequest detailRequest = new DetailRequest(
						DealsActivity.this, id, true,
						((CompanyDesc) mCompListDealAdapter.getItem(arg2))
								.getCat_id());
				startSppiner();
				controller.requestService(detailRequest);
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
					} else if (number == AppConstants.MAX_RECORD_COUNT + 1
							&& !isModifySearchDialogOpen && mScrollUp) {
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
		case R.id.show_profile_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.SHOW_PROFILE_CLICK);
			onProfileClick();
			break;

		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intentHome = new Intent(DealsActivity.this,
					HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
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
				} else {
					refineSearch();
				}
			} else {
				refineSearch();
			}
			AnalyticsHelper.logEvent(FlurryEventsConstants.MODIFY_SEARCH_CLICK);
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
		} else {
			System.out.println(screenData);
			Response response = (Response) screenData;
			Message message = new Message();
			message.arg2 = event;
			message.arg1 = 1;
			if (response.isError()) {
				message.obj = response.getErrorText() + " "
						+ response.getErrorCode();
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
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.COMBIND_DEAL_LISTING_NEW_LISTING_PAGE) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				mRecordsFoundView.setText(mClResponse.getTotalrecordFound()
						+ " " + getResources().getString(R.string.deal_found));
				// initNavigationButton(mClResponse.getPagesCount());
				ArrayList<CompanyDesc> compListData = mClResponse
						.getCompanyArrayList();

				mCompListDealAdapter = new CompanyListDealAdapter(
						DealsActivity.this, false);
				mCompListDealAdapter.setData(compListData);
				mCompanyList.setAdapter(mCompListDealAdapter);
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
				// TODO append result set in existing
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
				mCompListDealAdapter.setData(mClResponse.getCompanyArrayList());
				mCompListDealAdapter.notifyDataSetChanged();
			}
			stopSppiner();
		} else if (msg.arg2 == Events.DEAL_DETAIL) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
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
					// intent.putExtra(AppConstants.THUMB_URL,
					// mCategoryThumbUrl);
					// intent.putExtra(AppConstants.IS_DEAL_LIST,
					// !mClRequest.isCompanyListing());
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
				showInfoDialog("There is some problem in Downloding deal. Please Try again.");
			} else if (msg.arg1 == 0) {
				MaxisResponse genResp = (MaxisResponse) msg.obj;
				showInfoDialog("Thank you for Download Deal. The message has been sent to your Phone Number.");
			}

		}
		stopSppiner();
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
			// refineSearch();
			isModifySearchDialogOpen = false;
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
						|| mClResponse.getCategoryList().size() < 1 || mClRequest.getKeywordOrCategoryId()!=null || mClRequest.getKeywordOrCategoryId().equalsIgnoreCase("")) {
					showAlertDialog(getResources().getString(
							R.string.category_list_not_found));
					return;
				}
				else
				{
					if(mClRequest.isBySearch())
					{
						 ArrayList<CategoryRefine> catList = new ArrayList<CategoryRefine>();
						 CategoryRefine categoryRefine = new CategoryRefine();
						 categoryRefine.setCategoryId(mClRequest.getKeywordOrCategoryId());
						 categoryRefine.setCategoryTitle(mClRequest.getCategoryTitle());
						 catList.add(categoryRefine);
						 mCatResponse.setCategories(catList);
					}else
						mCatResponse.setCategories(mClResponse.getCategoryList());
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
		RefineAttributeController refineController = new RefineAttributeController(
				DealsActivity.this, Events.REFINE_ATTRIBUTES);
		startSppiner();
		RefineSearchRequest refineSearchRequest = new RefineSearchRequest();
		refineSearchRequest.setCategoryId(categoryId);
		refineSearchRequest.setDeal(!mClRequest.isCompanyListing());
		refineController.requestService(refineSearchRequest);
	}

	public void getDownloadDetails(Context context, String id) {
		dealID = id;

		if (store.isLoogedInUser()) {
			userNo = store.getUserMobileNumberToDispaly();
			userName = store.getUserName();
			dealDownload();

		} else {
			Intent intent = new Intent(context, DealForm.class);
			((Activity) context).startActivityForResult(intent, 2);

		}

	}

	public void dealDownload() {
		DownloadDealReq dealReq = new DownloadDealReq();
		dealReq.setName(userName);
		dealReq.setPhoneNo("60" + userNo);
		dealReq.setDeal_id(dealID);

		DownloadDealController downloadDealController = new DownloadDealController(
				DealsActivity.this, Events.DOWNLOAD_DEAL);
		startSppiner();
		downloadDealController.requestService(dealReq);
	}
}
