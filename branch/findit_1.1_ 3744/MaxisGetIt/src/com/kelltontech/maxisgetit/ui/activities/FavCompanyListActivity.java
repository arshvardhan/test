package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import com.kelltontech.maxisgetit.controllers.FavouriteController;
import com.kelltontech.maxisgetit.controllers.RefineAttributeController;
import com.kelltontech.maxisgetit.dao.CompanyDesc;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.SelectorDAO;
import com.kelltontech.maxisgetit.requests.DetailRequest;
import com.kelltontech.maxisgetit.requests.FavCompanyListRequest;
import com.kelltontech.maxisgetit.requests.RefineSearchRequest;
import com.kelltontech.maxisgetit.response.CompanyListResponse;
import com.kelltontech.maxisgetit.response.RefineCategoryResponse;
import com.kelltontech.maxisgetit.response.RefineSelectorResponse;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class FavCompanyListActivity extends MaxisMainActivity {
	private ListView mCompanyList;
	private FavCompanyListAdapter mCompListAdapter;
	private TextView mRecordsFoundView;
	private static TextView markall;
	private TextView mRefineSearchView;
	// private TextView mMoreLinkView;
	private TextView mDealBtnView;
	private TextView mViewAllOnMap;
	public static boolean isEdit;
	public static boolean isSingleChecked;
	
	boolean isAllchecked = false;
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
	private RefineSelectorResponse mSelctorResp;
	private RefineCategoryResponse mCatResponse;
	private boolean mIsFreshSearch = true;
	private SelectorDAO mLocalitySelectorDao;
	private boolean loadingNextPageData;
	private boolean isModifySearchDialogOpen;
	private ImageView mHomeIconView;
	private String userId;

	private LinearLayout mDealsFooter;
	private LinearLayout mListFooter;
	private boolean mScrollUp;

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
				// mClRequest =
				// bundle.getParcelable(AppConstants.DATA_LIST_REQUEST);
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
				ArrayList<CompanyDesc> compListData = mClResponse
						.getCompanyArrayList();
				mCompListAdapter.setData(compListData);
				mCompListAdapter.notifyDataSetChanged();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fav_company_list);
		AnalyticsHelper.logEvent(
				FlurryEventsConstants.APPLICATION_COMBINED_LIST, true);
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
		mViewAllOnMap = (TextView) findViewById(R.id.col_view_on_map);
		mViewAllOnMap.setOnClickListener(this);
		userId = getIntent().getExtras().getString("UserID");
		findViewById(R.id.col_view_on_map1).setOnClickListener(this);

		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(FavCompanyListActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mDealsFooter = (LinearLayout) findViewById(R.id.cl_deals_footer);
		mListFooter = (LinearLayout) findViewById(R.id.cl_company_list_footer);

		Bundle bundle = getIntent().getExtras();
		// mClRequest = bundle.getParcelable(AppConstants.DATA_LIST_REQUEST);
		// if (mClRequest != null &&
		// !StringUtil.isNullOrEmpty(mClRequest.getCategoryTitle()))
		// mHeaderTitle.setText(Html.fromHtml(mClRequest.getCategoryTitle()));
		// mCategoryThumbUrl = mClRequest.getParentThumbUrl();
		mRefineSearchView = (TextView) findViewById(R.id.col_refine_search);
		mRefineSearchView.setOnClickListener(this);

		findViewById(R.id.col_refine_search1).setOnClickListener(this);

		mClResponse = bundle.getParcelable(AppConstants.COMP_LIST_DATA);
		// if (mClRequest.isBySearch()) {
		// if (mIsFreshSearch) {
		// if (mClResponse.getCategoryList() != null &&
		// mClResponse.getCategoryList().size() == 1) {
		// mIsFreshSearch = false;
		// CategoryRefine tempCatref = mClResponse.getCategoryList().get(0);
		// mClRequest.setSelectedCategoryBySearch(tempCatref.getCategoryId(),
		// tempCatref.getCategoryTitle());
		// } else
		// mRefineSearchView.setText(getResources().getString(R.string.cl_filter_by));
		// //mRefineSearchView.setImageDrawable(getResources().getDrawable(R.drawable.filter_by));
		// }
		// if (mClRequest != null &&
		// !StringUtil.isNullOrEmpty(mClRequest.getKeywordOrCategoryId())){
		// mHeaderTitle.setText(Html.fromHtml(mClRequest.getKeywordOrCategoryId()));
		// mSearchEditText.setText(Html.fromHtml(mClRequest.getKeywordOrCategoryId()));
		// }
		// }else
		// if((mClRequest.getGroupActionType().trim().equalsIgnoreCase(AppConstants.GROUP_ACTION_TYPE_CATEGORY_LIST_FOR_GROUP)
		// &&
		// (mClRequest.getGroupType().trim().equalsIgnoreCase(AppConstants.GROUP_TYPE_CATEGORY))))
		// {
		// if (mIsFreshSearch) {
		// if (mClResponse.getCategoryList() != null &&
		// mClResponse.getCategoryList().size() == 1) {
		// mIsFreshSearch = false;
		// CategoryRefine tempCatref = mClResponse.getCategoryList().get(0);
		// mClRequest.setSelectedCategoryBySearch(tempCatref.getCategoryId(),
		// tempCatref.getCategoryTitle());
		// } else
		// mRefineSearchView.setText(getResources().getString(R.string.cl_filter_by));
		// //mRefineSearchView.setImageDrawable(getResources().getDrawable(R.drawable.filter_by));
		// }
		// }else {
		mHeaderTitle.setVisibility(View.VISIBLE);
		// }

		mRecordsFoundView = (TextView) findViewById(R.id.col_records_found);
		markall =  (TextView) findViewById(R.id.col_records_found1);
		// mMoreLinkView = (TextView) findViewById(R.id.col_more_option);
		// mMoreLinkView.setOnClickListener(this);
		mDealBtnView = (TextView) findViewById(R.id.col_deal_btn);
		mDealBtnView.setOnClickListener(this);
		// if (mClRequest.isCompanyListing()) {
		// mDealBtnView.setOnClickListener(this);
		mDealsFooter.setVisibility(View.GONE);
		mListFooter.setVisibility(View.GONE);
		// } else {
		// mDealBtnView.setVisibility(View.GONE);
		// mDealsFooter.setVisibility(View.VISIBLE);
		// mListFooter.setVisibility(View.GONE);
		// }

		// mRecordsFoundView.setText(mClResponse.getTotalrecordFound() + " " +
		// getResources().getString(R.string.record_found));

		mRecordsFoundView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isEdit) {
					isEdit = false;
				} else {
					isEdit = true;

				}
				mCompListAdapter.notifyDataSetChanged();
			}
		});
		
		
		markall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (isEdit) {
					ArrayList<CompanyDesc> compListData = mClResponse.getCompanyArrayList();
					isAllchecked =  !isAllchecked;
					for (CompanyDesc companyDesc : compListData) {
						
						companyDesc.setIschecked(isAllchecked);
					}
					mCompListAdapter.notifyDataSetChanged();
				}
				
			}
		});
		
		
		// initNavigationButton(mClResponse.getPagesCount());
		ArrayList<CompanyDesc> compListData = mClResponse.getCompanyArrayList();
		
		mCompanyList = (ListView) findViewById(R.id.col_company_list);
		mCompListAdapter = new FavCompanyListAdapter(
				FavCompanyListActivity.this, true);
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
					if ((number) % 10 == 0
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

		// mCompanyList.setOnTouchListener(new OnTouchListener() {
		// private float mInitialX;
		// private float mInitialY;
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// switch (event.getAction()) {
		// case MotionEvent.ACTION_DOWN:
		// mInitialX = event.getX();
		// mInitialY = event.getY();
		// mScrollUp = false;
		// // mScrollDown = true;
		// return true;
		// case MotionEvent.ACTION_MOVE:
		// final float x = event.getX();
		// final float y = event.getY();
		// final float yDiff = y - mInitialY;
		// if (yDiff > 0.0) {
		// Log.d("maxis", "SCROLL DOWN");
		// mScrollUp = false;
		// break;
		//
		// } else if (yDiff < 0.0) {
		// Log.d("maxis", "SCROLL up");
		// mScrollUp = true;
		// break;
		// }
		// break;
		// }
		// return false;
		// }
		//
		//
		// });

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
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.REFINE_ATTRIBUTES) {
			Message catRefine = (Message) screenData;
			handler.sendMessage(catRefine);
			return;
		}
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
					// if(mClRequest.isBySearch())
					// intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD,
					// mClRequest.getKeywordOrCategoryId());
					intent.putExtra(AppConstants.COMP_DETAIL_DATA, compListResp);
					intent.putExtra(AppConstants.THUMB_URL, mCategoryThumbUrl);
					intent.putExtra(AppConstants.IS_DEAL_LIST, false);

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
				oldResponse.appendCompListAtEnd(mClResponse
						.getCompanyArrayList());
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
				mCompListAdapter.setData(mClResponse.getCompanyArrayList());
				mCompListAdapter.notifyDataSetChanged();
			}
			stopSppiner();
		} else if (msg.arg2 == Events.REFINE_ATTRIBUTES) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				// if (mClRequest.isBySearch())
				// displayRefineWithAttributeSpinnersPreloaded((RefineSelectorResponse)
				// msg.obj, RefineSearchActivity.ATTR_SELECTION_BY_SEARCH);
				// else
				displayRefineWithAttributeSpinnersPreloaded(
						(RefineSelectorResponse) msg.obj,
						RefineSearchActivity.ATTR_SELECTION);
			}
			stopSppiner();
		}
	}

	@Override
	public Activity getMyActivityReference() {
		// TODO Auto-generated method stub
		return null;
	}

	private void loadPageData(int pageNumber) {
		// CombindListingController listingController = new
		// CombindListingController(FavCompanyListActivity.this,
		// Events.COMBIND_LISTING_PAGINATION);
		// mClRequest.setPageNumber(pageNumber);
		// startSppiner();
		// listingController.requestService(mClRequest);
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
			// refineSearch();
			AnalyticsHelper.logEvent(FlurryEventsConstants.MODIFY_SEARCH_CLICK);
			break;
		// case R.id.col_more_option:
		// showConfirmationDialog(CustomDialog.CONFIRMATION_DIALOG,
		// getResources().getString(R.string.modify_to_filter));
		// break;
		case R.id.col_deal_btn:
			// CombindListingController listingController = new
			// CombindListingController(FavCompanyListActivity.this,
			// Events.COMBIND_LISTING_NEW_LISTING_PAGE);
			// CombinedListRequest newListReq = new
			// CombinedListRequest(FavCompanyListActivity.this);
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
			// AnalyticsHelper.logEvent(FlurryEventsConstants.HOT_DEALS_CLICK);
			break;
		case R.id.search_icon_button:
			mSearchEditText
					.setText(mSearchEditText.getText().toString().trim());
			performSearch(mSearchEditText.getText().toString());
			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intentHome = new Intent(FavCompanyListActivity.this,
					HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
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

	private void fetchRefineAttribute(String categoryId) {
		RefineAttributeController refineController = new RefineAttributeController(
				FavCompanyListActivity.this, Events.REFINE_ATTRIBUTES);
		startSppiner();
		RefineSearchRequest refineSearchRequest = new RefineSearchRequest();
		refineSearchRequest.setCategoryId(categoryId);
		refineSearchRequest.setDeal(false);
		refineController.requestService(refineSearchRequest);
	}

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
			isModifySearchDialogOpen = false;
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
			// intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD,
			// mClRequest.getKeywordOrCategoryId());
			intent.putExtra(AppConstants.MAP_ALL_TITLE, mHeaderTitle.getText()
					.toString().trim());
			// intent.putExtra(AppConstants.IS_SEARCH_KEYWORD,
			// mClRequest.isBySearch());
			// intent.putExtra(AppConstants.IS_DEAL_LIST,
			// !mClRequest.isCompanyListing());
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
	public static void textChange(boolean ischeck)
	{
	if(ischeck)
	{
		markall.setText("UnmarkAll");
	}
	else
	{
		markall.setText("Mark All");
	}
	}

}
