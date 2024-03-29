package com.kelltontech.maxisgetit.ui.activities;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.ui.BaseMainActivity;
import com.kelltontech.framework.ui.IActionController;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.constants.matta.MattaConstants;
import com.kelltontech.maxisgetit.constants.matta.MattaEvents;
import com.kelltontech.maxisgetit.controllers.BannerNavigationController;
import com.kelltontech.maxisgetit.controllers.CityAreaListController;
import com.kelltontech.maxisgetit.controllers.CombindListingController;
import com.kelltontech.maxisgetit.controllers.CompanyDetailController;
import com.kelltontech.maxisgetit.controllers.UserDetailController;
import com.kelltontech.maxisgetit.controllers.matta.MattaBoothDetailController;
import com.kelltontech.maxisgetit.controllers.matta.MattaPackageListController;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.model.bannerModel.LogBannerReportResponse;
import com.kelltontech.maxisgetit.model.matta.booths.detail.MattaBoothDetailResponse;
import com.kelltontech.maxisgetit.model.matta.packages.list.MattaPackageListResponse;
import com.kelltontech.maxisgetit.requests.CombinedListRequest;
import com.kelltontech.maxisgetit.requests.DetailRequest;
import com.kelltontech.maxisgetit.requests.matta.MattaBoothDetailRequest;
import com.kelltontech.maxisgetit.requests.matta.MattaPackageListRequest;
import com.kelltontech.maxisgetit.response.CompanyListResponse;
import com.kelltontech.maxisgetit.response.UserDetailResponse;
import com.kelltontech.maxisgetit.ui.activities.matta.MattaBoothDetailActivity;
import com.kelltontech.maxisgetit.ui.activities.matta.MattaPackageListActivity;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

/**
 * Base class for all Activity of application.
 */
public abstract class MaxisMainActivity extends BaseMainActivity/* implements AnimationListener*/ {
	private CompanyListResponse mClResponse;
	private CombinedListRequest mListRequest;
	private static MaxisStore mStore;
	private String	mUrlToBeOpenedWithChooser;
	protected String mSearchKeyword;
	public static boolean isCitySelected= false;
	private Activity tapEventTriggeringActivity;
	private MattaPackageListRequest mMattaPackageListRequest;
	private MattaBoothDetailRequest mMattaBoothDetailRequest;
	protected boolean isLocationAware() {
		return mStore.isLocalized();
	}

	/**
	 * @param listRequest
	 *            only if you are going to initiate a fresh deal listing or
	 *            company listing by category
	 */
	public void setRequest(CombinedListRequest listRequest) {
		this.mListRequest = listRequest;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mStore = MaxisStore.getStore(MaxisMainActivity.this);
		if(getIntent().getExtras() != null)
		{
			mSearchKeyword =  getIntent().getExtras().getString(AppConstants.GLOBAL_SEARCH_KEYWORD);
		}
		AnalyticsHelper.onActivityCreate();
	}

	@Override
	protected void onResume() {
		super.onResume();
		EditText editText = (EditText) findViewById(R.id.search_box);
		InputFilter filter = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				for (int i = start; i < end; i++) {
					//					if (!Character.isLetterOrDigit(source.charAt(i)) && source.charAt(i) != '\'' && source.charAt(i) != '_' && source.charAt(i) != '-' && source.charAt(i) != ' '&& source.charAt(i) != '&') {
					if (!Character.isLetterOrDigit(source.charAt(i)) && source.charAt(i) != '\'' && source.charAt(i) != '_' && source.charAt(i) != '-' && source.charAt(i) != ' ') {
						return "";
					}
				}
				return null;
			}
		};
		if (null != editText) {
			//editText.setFilters(new InputFilter[] { filter });
		}

	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 * //The onStart() and onStop() methods are overridden for integrating and tracking events via analytics tools such as FLURRY
	 */
	@Override
	protected void onStart() {
		super.onStart();
		if (AppConstants.PRODUCTION) {
			AnalyticsHelper.onActivityStart(this);
			AnalyticsHelper.setLogEnabled(true);
			AnalyticsHelper.getReleaseVersion();
			if(!StringUtil.isNullOrEmpty(mStore.getUserMobileNumber())){
				AnalyticsHelper.setUserID(mStore.getUserMobileNumber());
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (AppConstants.PRODUCTION) {
			AnalyticsHelper.onActivityStop(this);
		}
	}

	protected void performSearch(String searchText , String postJsonPayload, int event) {
		if (searchText == null || searchText.trim().equals("")) {
			showInfoDialog(getResources().getString(R.string.input_search));
			return;
		}
		startSppiner();
		mListRequest = new CombinedListRequest(MaxisMainActivity.this);
		mListRequest.setBySearch(true);
		mListRequest.setCompanyListing(true);
		if (event == Events.COMBIND_LISTING_VIEW_MORE_COMPANY_EVENT) {
			mListRequest.setStampId(searchText.trim().split("-")[0].trim());
			mListRequest.setKeywordOrCategoryId((searchText.split("-")[1].trim().equals("null")) ? "" :searchText.split("-")[1].trim());
		} else
			mListRequest.setKeywordOrCategoryId(searchText.trim());  //
		HashMap<String,String>	map = new HashMap<String,String>();
		map.put(FlurryEventsConstants.HOME_SEARCH_TEXT, searchText);
		AnalyticsHelper.logEvent(FlurryEventsConstants.HOME_SCREEN_SEARCH,map);

		mListRequest.setLatitude(GPS_Data.getLatitude());
		mListRequest.setLongitude(GPS_Data.getLongitude());

		if(postJsonPayload!=null) {
			if (event == Events.COMBIND_LISTING_VIEW_MORE_COMPANY_EVENT) {
				mListRequest.setSearchCriteria(postJsonPayload.trim());
			} else {
				isCitySelected = true;
				mListRequest.setPostJsonPayload(postJsonPayload);
			}
		}
		else {
			isCitySelected = false;
		}

		CombindListingController listingController = new CombindListingController(MaxisMainActivity.this, event);
		listingController.requestService(mListRequest);
	}

	protected void showCompanyDealListing(String categoryId, String categoryTitle,String thumbUrl, boolean iscompanyListing, String groupType, String groupActionType, int event) {
		startSppiner();
		mListRequest = new CombinedListRequest(MaxisMainActivity.this);
		mListRequest.setBySearch(false);
		mListRequest.setCompanyListing(iscompanyListing);
		mListRequest.setKeywordOrCategoryId(categoryId);
		mListRequest.setLatitude(GPS_Data.getLatitude());
		mListRequest.setLongitude(GPS_Data.getLongitude());
		mListRequest.setParentThumbUrl(thumbUrl);
		mListRequest.setCategoryTitle(categoryTitle);
		mListRequest.setGroupType(groupType);
		mListRequest.setGroupActionType(groupActionType);
		CombindListingController listingController = new CombindListingController(MaxisMainActivity.this, event);
		listingController.requestService(mListRequest);
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE 
				|| msg.arg2 == Events.BANNER_LANDING_COMPANY_LISTING_EVENT
				|| msg.arg2 == Events.BANNER_LANDING_SEARCH_EVENT
				|| msg.arg2 == Events.COMBIND_LISTING_VIEW_MORE_COMPANY_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				Intent intent = new Intent(MaxisMainActivity.this, CombindListActivity.class);
				if((msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE) && (mClResponse != null) && (mClResponse.getDisplayIn() != null)) {
					mListRequest.setSearchIn(StringUtil.isNullOrEmpty(mClResponse.getDisplayIn().getType()) ? "" : mClResponse.getDisplayIn().getType());
					mListRequest.setKeywordOrCategoryId(StringUtil.isNullOrEmpty(mClResponse.getDisplayIn().getKeyword()) ? "" : mClResponse.getDisplayIn().getKeyword());
				}
				intent.putExtra(AppConstants.COMP_LIST_DATA, mClResponse);
				intent.putExtra(AppConstants.DATA_LIST_REQUEST, mListRequest);
				startActivity(intent);
			}
			stopSppiner();
		}else if (msg.arg2 == Events.USER_DETAIL) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				Intent intent = new Intent(MaxisMainActivity.this, MyAccountActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
				intent.putExtra(AppConstants.USER_DETAIL_DATA, (UserDetailResponse)msg.obj);
				startActivity(intent);
			}
			stopSppiner();
		} else if (msg.arg2 == Events.BANNER_LANDING_COMPANY_DETAIL_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				CompanyDetail compListResp = (CompanyDetail) msg.obj;
				if (!StringUtil.isNullOrEmpty(compListResp.getId())) {
					Intent intent = new Intent(MaxisMainActivity.this, CompanyDetailActivity.class);
					//					if (mClRequest.isBySearch())
					//						intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD,mClRequest.getKeywordOrCategoryId());
					intent.putExtra(AppConstants.COMP_DETAIL_DATA, compListResp);
					//					if (msg.arg2 == Events.COMPANY_DETAIL) {
					//						intent.putExtra(AppConstants.THUMB_URL, mCategoryThumbUrl);
					//						intent.putExtra(AppConstants.IS_DEAL_LIST,!mClRequest.isCompanyListing());
					//					}
					startActivity(intent);
				} else {
					showInfoDialog(getResources().getString(R.string.no_result_found));
				}
			}
			stopSppiner();
		} else if (msg.arg2 == Events.BANNER_NAVIGATION_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
				stopSppiner();
			} else {
				LogBannerReportResponse logBannerReportResponse = (LogBannerReportResponse) msg.obj;
				if (logBannerReportResponse != null
						&& logBannerReportResponse.getResults() != null
						&& logBannerReportResponse.getResults().getScreenName() != null) {	
					if(AppConstants.COMPANY_LISTING_SCREEN.equals(logBannerReportResponse.getResults().getScreenName().trim())) {
						if(!StringUtil.isNullOrEmpty(logBannerReportResponse.getResults().getCategoryId()) 
								&& !StringUtil.isNullOrEmpty(logBannerReportResponse.getResults().getActionType())
								&& !StringUtil.isNullOrEmpty(logBannerReportResponse.getResults().getType())) {
							showCompanyDealListing(logBannerReportResponse.getResults().getCategoryId().trim(), 
									null, 
									null, 
									true, 
									logBannerReportResponse.getResults().getType().trim(), 
									logBannerReportResponse.getResults().getActionType().trim(), 
									Events.BANNER_LANDING_COMPANY_LISTING_EVENT);
							//		showCompanyDealListing("12", null, null, true, "C", "L", Events.BANNER_LANDING_COMPANY_LISTING_EVENT);
						} 
					} else if(AppConstants.COMPANY_DETAIL_SCREEN.equals(logBannerReportResponse.getResults().getScreenName().trim())) {
						if(!StringUtil.isNullOrEmpty(logBannerReportResponse.getResults().getCategoryId()) 
								&& !StringUtil.isNullOrEmpty(logBannerReportResponse.getResults().getItemId())) {
							DetailRequest mBannerCompanyDetailRequest = new DetailRequest(tapEventTriggeringActivity, 
									logBannerReportResponse.getResults().getItemId().trim(), 
									false, 
									logBannerReportResponse.getResults().getCategoryId().trim());
							//		DetailRequest mBannerCompanyDetailRequest = new DetailRequest(tapEventTriggeringActivity, "241203", false, "202");
							CompanyDetailController bannerCompanyDetailController = new CompanyDetailController(MaxisMainActivity.this, Events.BANNER_LANDING_COMPANY_DETAIL_EVENT);
							startSppiner();
							bannerCompanyDetailController.requestService(mBannerCompanyDetailRequest);
						}

					} else if(AppConstants.DEAL_DETAIL_SCREEN.equals(logBannerReportResponse.getResults().getScreenName().trim())) {

						if(!StringUtil.isNullOrEmpty(logBannerReportResponse.getResults().getCategoryId()) 
								&& !StringUtil.isNullOrEmpty(logBannerReportResponse.getResults().getItemId())) {
							DetailRequest mBannerDealDetailRequest = new DetailRequest(tapEventTriggeringActivity, logBannerReportResponse.getResults().getItemId().trim(), true, logBannerReportResponse.getResults().getCategoryId().trim());
							//							DetailRequest mBannerDealDetailRequest = new DetailRequest(tapEventTriggeringActivity, "3", true, logBannerReportResponse.getResults().getCategoryId().trim());
							CompanyDetailController bannerDealDetailController = new CompanyDetailController(MaxisMainActivity.this, Events.BANNER_LANDING_DEAL_DETAIL_EVENT);
							startSppiner();
							bannerDealDetailController.requestService(mBannerDealDetailRequest);
						}

					} else if(AppConstants.SEARCH_SCREEN.equals(logBannerReportResponse.getResults().getScreenName().trim())) {

						if(!StringUtil.isNullOrEmpty(logBannerReportResponse.getResults().getKeyword())) {
							performSearch(logBannerReportResponse.getResults().getKeyword().trim(), null, Events.BANNER_LANDING_SEARCH_EVENT);
						} 
					} else if(AppConstants.PACKAGE_LIST_SCREEN.equals(logBannerReportResponse.getResults().getScreenName().trim())) {

						MattaPackageListController packageListController = new MattaPackageListController(MaxisMainActivity.this, MattaEvents.MATTA_BANNER_LANDING_PKG_LIST_EVENT);
						mMattaPackageListRequest = new MattaPackageListRequest();
						mMattaPackageListRequest.setSource(!StringUtil.isNullOrEmpty(logBannerReportResponse.getResults().getSource()) ? logBannerReportResponse.getResults().getSource(): "");
						mMattaPackageListRequest.setCompanyId(!StringUtil.isNullOrEmpty(logBannerReportResponse.getResults().getCId()) ? logBannerReportResponse.getResults().getCId() : "");
						mMattaPackageListRequest.setHallId(!StringUtil.isNullOrEmpty(logBannerReportResponse.getResults().getHallId()) ? logBannerReportResponse.getResults().getHallId() : "");
						packageListController.requestService(mMattaPackageListRequest);
						startSppiner();
					} else if(AppConstants.BOOTH_DETAIL_SCREEN.equals(logBannerReportResponse.getResults().getScreenName().trim())) {

						MattaBoothDetailController boothDetailcontroller = new MattaBoothDetailController(MaxisMainActivity.this, MattaEvents.MATTA_BANNER_LANDING_BOOTH_DETAIL_EVENT);
						mMattaBoothDetailRequest = new MattaBoothDetailRequest();
						mMattaBoothDetailRequest.setCompanyId(!StringUtil.isNullOrEmpty(logBannerReportResponse.getResults().getCId()) ? logBannerReportResponse.getResults().getCId() : "");
						mMattaBoothDetailRequest.setSource(!StringUtil.isNullOrEmpty(logBannerReportResponse.getResults().getSource()) ? logBannerReportResponse.getResults().getSource(): "");
						mMattaBoothDetailRequest.setHallId(!StringUtil.isNullOrEmpty(logBannerReportResponse.getResults().getHallId()) ? logBannerReportResponse.getResults().getHallId() : "");
						startSppiner();
						boothDetailcontroller.requestService(mMattaBoothDetailRequest);
					} else {
						stopSppiner();
						showInfoDialog(getResources().getString(R.string.no_result_found));
					}
				}
				Log.e("FINDIT MALAYSIA", ":" + msg.obj);
			}
		} else if (msg.arg2 == Events.BANNER_LANDING_DEAL_DETAIL_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog(getResources().getString(R.string.network_unavailable));
			} else {
				CompanyDetail fromBannerDealDetailResp = (CompanyDetail) msg.obj;
				if (!StringUtil.isNullOrEmpty(fromBannerDealDetailResp.getId())) {
					Intent intent = new Intent(MaxisMainActivity.this, DealDetailActivity.class);
					//				if (mClRequest.isBySearch())
					//					intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mClRequest.getKeywordOrCategoryId());
					intent.putExtra(AppConstants.COMP_DETAIL_DATA, fromBannerDealDetailResp);
					startActivity(intent);
				} else {
					showInfoDialog(getResources().getString(R.string.no_result_found));
				}
			}
			stopSppiner();
		} else if (msg.arg2 == MattaEvents.MATTA_BANNER_LANDING_PKG_LIST_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				MattaPackageListResponse packageResponse = (MattaPackageListResponse) msg.obj;
				Intent packageListIntent = new Intent(MaxisMainActivity.this, MattaPackageListActivity.class);
				packageListIntent.putExtra(MattaConstants.DATA_MATTA_PACKAGE_LIST_REQUEST, mMattaPackageListRequest);
				packageListIntent.putExtra(MattaConstants.DATA_MATTA_PACKAGE_LIST_RESPONSE, packageResponse);
				startActivity(packageListIntent);
			}
			stopSppiner();
		} else if (msg.arg2 == MattaEvents.MATTA_BANNER_LANDING_BOOTH_DETAIL_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				MattaBoothDetailResponse detailResponse = (MattaBoothDetailResponse) msg.obj;
				if (!StringUtil.isNullOrEmpty(detailResponse.getResults().getCompany().getCName())) {
					Intent intent = new Intent(MaxisMainActivity.this, MattaBoothDetailActivity.class);
					if (!StringUtil.isNullOrEmpty(mMattaBoothDetailRequest.getKeyword()))
						intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD,mMattaBoothDetailRequest.getKeyword());
					intent.putExtra(MattaConstants.DATA_MATTA_BOOTH_DETAIL_RESPONSE, detailResponse);
					intent.putExtra(MattaConstants.DATA_MATTA_BOOTH_DETAIL_REQUEST, mMattaBoothDetailRequest);
					startActivity(intent);
				} else {
					showInfoDialog(getResources().getString(R.string.no_result_found));
				}
			}
			stopSppiner();
		}
	}

	protected void onProfileClick() {
		if (mStore.isLoogedInUser()) {
			UserDetailController controller = new UserDetailController(MaxisMainActivity.this, Events.USER_DETAIL);
			controller.requestService(mStore.getUserMobileNumber());
			startSppiner();
			//			Intent intentMyAccount = new Intent(MaxisMainActivity.this, MyAccountActivity.class);
			//			intentMyAccount.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			//			startActivity(intentMyAccount);
		} /*else if (mStore.isRegisteredUser() && !mStore.isVerifiedUser()) {
			Intent intentverify = new Intent(MaxisMainActivity.this, VerifyRegistrationActivity.class);
			intentverify.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentverify);
		} */else {
			Intent intentLogin = new Intent(MaxisMainActivity.this, LoginActivity.class);
			intentLogin.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intentLogin.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
			startActivity(intentLogin);
		}
		AnalyticsHelper.logEvent(FlurryEventsConstants.SHOW_PROFILE_CLICK);
	}
	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if(event==Events.USER_DETAIL){
			handler.sendMessage((Message) screenData);
			return;
		} else if (event == Events.BANNER_NAVIGATION_EVENT) {
			LogBannerReportResponse bannerReportResponse = (LogBannerReportResponse) screenData;
			Message message = new Message();
			message.arg2 = event;
			if ((bannerReportResponse != null) 
					&& (bannerReportResponse.getResults() != null) 
					&& (!StringUtil.isNullOrEmpty(bannerReportResponse.getResults().getError_Code())) 
					&& (bannerReportResponse.getResults().getError_Code().equals("0"))) {
				message.arg1 = 0;
				message.obj = bannerReportResponse;
			} else {
				message.arg1 = 1;
				message.obj = getResources().getString(R.string.communication_failure);
			}
			handler.sendMessage(message);
			return;
		} else if (event == MattaEvents.MATTA_BANNER_LANDING_PKG_LIST_EVENT) {
			MattaPackageListResponse packageListRes = (MattaPackageListResponse) screenData;
			Message message = new Message();
			message.arg2 = event;
			if ((packageListRes.getResults() != null) && (!StringUtil.isNullOrEmpty(packageListRes.getResults().getError_Code())) && (packageListRes.getResults().getError_Code().equals("0"))) {
				if (packageListRes.getResults().getPackage().size() < 1 
						|| StringUtil.isNullOrEmpty(packageListRes.getResults().getTotal_Records_Found()) 
						|| packageListRes.getResults().getTotal_Records_Found().equals("0")) {
					message.arg1 = 1;
					message.obj = new String("No Result Found");
				} else {
					message.arg1 = 0;
					message.obj = packageListRes;
				}
			} else {
				message.arg1 = 1;
				message.obj = getResources().getString(R.string.communication_failure);
			}
			handler.sendMessage(message);
			return;			
		} else if (event == MattaEvents.MATTA_BANNER_LANDING_BOOTH_DETAIL_EVENT) {
			MattaBoothDetailResponse boothDetail = (MattaBoothDetailResponse) screenData;
			Message message = new Message();
			message.arg2 = event;
			if ((boothDetail.getResults() != null) && (!StringUtil.isNullOrEmpty(boothDetail.getResults().getError_Code())) && (boothDetail.getResults().getError_Code().equals("0"))) {
				if (boothDetail.getResults().getCompany() == null || StringUtil.isNullOrEmpty(boothDetail.getResults().getCompany().getCName())) {
					message.arg1 = 1;
					message.obj = new String("No Result Found");
				} else {
					message.arg1 = 0;
					message.obj = boothDetail;
				}
			} else {
				message.arg1 = 1;
				message.obj = getResources().getString(R.string.communication_failure);
			}
			handler.sendMessage(message);
			return;	
		} 
		System.out.println(screenData);
		Response response = (Response) screenData;
		Message message = new Message();
		message.arg2 = event;
		message.arg1 = 1;
		if (response.isError()) {
			message.obj = response.getErrorText() + " " + response.getErrorCode();
		} else {
			if (event == Events.BANNER_LANDING_DEAL_DETAIL_EVENT 
					|| event == Events.BANNER_LANDING_COMPANY_DETAIL_EVENT) {
				if (response.getPayload() instanceof CompanyDetail) {
					CompanyDetail fromBannerDetail = (CompanyDetail) response.getPayload();
					if (fromBannerDetail.getErrorCode() != 0) {
						message.obj = getResources().getString(R.string.communication_failure);
					} else {
						message.arg1 = 0;
						message.obj = fromBannerDetail;
					}
				} else {
					message.obj = new String(getResources().getString(R.string.communication_failure));
				}
			} else if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE 
					|| event == Events.BANNER_LANDING_COMPANY_LISTING_EVENT
					|| event == Events.BANNER_LANDING_SEARCH_EVENT
					|| event == Events.COMBIND_LISTING_VIEW_MORE_COMPANY_EVENT){
				if (response.getPayload() instanceof CompanyListResponse) {
					mClResponse = (CompanyListResponse) response.getPayload();
					if (mClResponse.getErrorCode() != 0) {
						message.obj = getResources().getString(R.string.communication_failure);
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
		}
		handler.sendMessage(message);
	}

	protected void showAlertDialog(CharSequence message) {
		showInfoDialog(message.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		return true;*/
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(MaxisMainActivity.this, ActivityAccountSettings.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			break;
		}
		return true;
	}
	public void showDialogWithTitle(String title,String info) {
		CustomDialog customDialog = new CustomDialog(CustomDialog.INFO_DIALOG, MaxisMainActivity.this);
		mDialog = customDialog.createDisclaimerDialog(title, info);
	}
	public void showInfoDialog(String info) {
		CustomDialog customDialog = new CustomDialog(CustomDialog.INFO_DIALOG, MaxisMainActivity.this);
		mDialog = customDialog.createCustomDialog(info);
	}

	public void showExitAndBackDialog(String info) {
		CustomDialog customDialog = new CustomDialog(CustomDialog.TNC_DIALOG, MaxisMainActivity.this);
		mDialog = customDialog.createCustomDialog(info);
	}

	public void showFinalDialog(String info) {
		CustomDialog customDialog = new CustomDialog(CustomDialog.FINAL_DIALOG, MaxisMainActivity.this);
		mDialog = customDialog.createCustomDialog(info);
	}

	public void showConfirmationDialog(int id, String info) {
		CustomDialog customDialog = new CustomDialog(id, MaxisMainActivity.this);
		mDialog = customDialog.createCustomDialog(info);
	}

	public void showSingleButtonConfirmationDialog( String info) {
		CustomDialog customDialog = new CustomDialog(CustomDialog.SINGLE_BUTTON_CONFIRMATION_DIALOG, MaxisMainActivity.this);
		mDialog = customDialog.createCustomDialog(info);
	}

	public void showPlayStoreDialog(String info) {
		stopSppiner();
		CustomDialog customDialog = new CustomDialog(CustomDialog.PLAY_STORE_DIALOG, MaxisMainActivity.this);
		mDialog = customDialog.createCustomDialog(info);
	}

	public void showImageUploadDialog(int id, String info) {
		CustomDialog customDialog = new CustomDialog(id, MaxisMainActivity.this);
		nativeDialog = customDialog.createNativeDialog(info);
	}

	@Override
	public void onPositiveDialogButton(int pDialogId) {
		switch(pDialogId) {
		case CustomDialog.LOCATION_DIALOG:
			Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS); //intent1.setClassName("com.android.settings", "com.android.settings.WirelessSettings"); 
			startActivity(Intent.createChooser(intent1, "Open Settings"));
			break;
		case CustomDialog.DATA_USAGE_DIALOG_FOR_FACEBOOK:
			openDeviceBrowser(AppConstants.FB_PAGE_URL, false);
			break;
		case CustomDialog.DATA_USAGE_DIALOG_FOR_TWITTER:
			openDeviceBrowser(AppConstants.TWITTER_PAGE_URL, false);
			break;
		case CustomDialog.DATA_USAGE_DIALOG_FOR_WEBSITE:
			openDeviceBrowser(mUrlToBeOpenedWithChooser, true);
			break;
		} 
	}

	@Override
	public void onNegativeDialogbutton(int id) {
		if (mDialog != null) {
			mDialog.dismiss();
		}
	}

	/**
	 * To remove all screens from stack up to and excluding Home Screen 
	 */
	protected void showHomeScreen() {
		Intent intentHome = new Intent(this, HomeActivity.class);
		intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intentHome);
		HomeActivity.fromHomeClick = true;
	}

	/**
	 * To check if location is available and show appropriate dialog
	 */ 
	protected  boolean isLocationAvailable() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			showConfirmationDialog(CustomDialog.LOCATION_DIALOG,getResources().getString(R.string.cd_error_enable_gps));
			//showAlertDialog(getResources().getString(R.string.cd_error_enable_gps));
			return false;
		}
		if(GPS_Data.getLatitude() == 0 || GPS_Data.getLongitude() == 0) {
			showAlertDialog(getResources().getString(R.string.cd_error_location_not_found));
			return false;
		}
		return true;
	}

	protected  boolean isDialogToBeShown() {
		/*long savedUserTime = mStore.getUserDecisionTime();
		int noOfDays = (int)((System.currentTimeMillis() - savedUserTime) / (1000 * 60 *60 *24)) ;
		if(!mStore.isUserDecisionRemembered() || noOfDays > 7)
			return true;
		else
			return false;*/
		return !mStore.isUserDecisionRemembered();
	}

	/**
	 * @param url
	 */
	protected void checkPreferenceAndOpenBrowser(String url) {
		url = StringUtil.getFormattedURL(url);
		if (url.toLowerCase().contains("findit.com.my")) {
			openDeviceBrowser(url, true);
			return;
		} 
		int dialogIdTobeShown = CustomDialog.DATA_USAGE_DIALOG_FOR_WEBSITE;
		if( url.indexOf("facebook") != -1 ) {
			dialogIdTobeShown = CustomDialog.DATA_USAGE_DIALOG_FOR_FACEBOOK;
		} else if( url.indexOf("twitter") != -1 ) {
			dialogIdTobeShown = CustomDialog.DATA_USAGE_DIALOG_FOR_TWITTER;
		}

		if( ! isDialogToBeShown() ) {
			if( dialogIdTobeShown == CustomDialog.DATA_USAGE_DIALOG_FOR_WEBSITE ) {
				openDeviceBrowser(url, true);
			} else {
				openDeviceBrowser(url, false);
			}
		} else {
			if( dialogIdTobeShown == CustomDialog.DATA_USAGE_DIALOG_FOR_WEBSITE ) {
				mUrlToBeOpenedWithChooser = url;
			}
			showConfirmationDialog(dialogIdTobeShown, getString(R.string.cd_msg_data_usage));
		}
	}

	/**
	 * @param url
	 */
	protected boolean overrideWebViewUrlLoading(String url) {
		Log.i("maxis", "Processing webview url click...");
		if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
			if( url.indexOf( getString(R.string.findit_com)) == -1 ) {
				checkPreferenceAndOpenBrowser(url);
			} else {
				openDeviceBrowser(url, true);
			}
			return true;
		} else if (url.startsWith("tel:")) { 
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url)); 
			startActivity(intent); 
			return  true;
		} else {
			return false;
		}
	}
	public void setSearchCity() {
		CityAreaListController clController = new CityAreaListController(MaxisMainActivity.this, Events.CITY_LISTING);
		startSppiner();
		clController.requestService(null);
	}

	public void setSearchLocality(int city_id) {
		if (city_id != -1) {
			CityAreaListController clController = new CityAreaListController(MaxisMainActivity.this, Events.LOCALITY_LISTING);
			startSppiner();
			clController.requestService(city_id + "");
		} else {
			showAlertDialog("Please select a City.");
		}
	}

	public void bannerTapped(int mPosition, String mBannerId, IActionController screen, int eventType) {
		tapEventTriggeringActivity = (Activity) screen;
		BannerNavigationController bannerNavigationController= new BannerNavigationController(MaxisMainActivity.this, eventType);
		startSppiner();
		bannerNavigationController.requestService(mBannerId);
	}

}
