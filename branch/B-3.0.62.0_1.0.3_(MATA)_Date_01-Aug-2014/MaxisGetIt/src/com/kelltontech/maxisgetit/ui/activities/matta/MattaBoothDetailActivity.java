package com.kelltontech.maxisgetit.ui.activities.matta;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.matta.MattaViewPagerAdapter;
import com.kelltontech.maxisgetit.adapters.matta.TravelPackagesListAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.constants.matta.MattaConstants;
import com.kelltontech.maxisgetit.constants.matta.MattaEvents;
import com.kelltontech.maxisgetit.controllers.matta.MattaPackageListController;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.model.matta.booths.detail.MattaBoothDetailResponse;
import com.kelltontech.maxisgetit.model.matta.booths.detail.PackageList;
import com.kelltontech.maxisgetit.model.matta.packages.list.MattaPackageListResponse;
import com.kelltontech.maxisgetit.requests.matta.MattaBoothDetailRequest;
import com.kelltontech.maxisgetit.requests.matta.MattaPackageListRequest;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.ui.activities.AdvanceSelectCity;
import com.kelltontech.maxisgetit.ui.activities.AdvanceSelectLocalityActivity;
import com.kelltontech.maxisgetit.ui.activities.GuestBranchingActivity;
import com.kelltontech.maxisgetit.ui.activities.MaxisMainActivity;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.ui.widgets.ExpandableListView;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class MattaBoothDetailActivity extends MaxisMainActivity {

	private ViewPager 					comDetailGallery;
	private EditText 					mSearchEditText;
	private TextView 					mTxtTitle;
	private TextView 					mAddressView;
	private TextView 					currentCity, currentLocality;
	private TextView 					mainSearchButton;
	private TextView 					mHeaderTitle;
	private TextView 					mWebsiteView;
	private TextView 					mEmailView;

	private ImageView 					mHeaderBackButton;
	private ImageView 					mHomeIconView;
	private ImageView 					mProfileIconView;
	private ImageView 					mReferFB, mReferTwitter;
	private ImageView 					mSearchBtn;
	private ImageView 					mSearchToggler;
	private ImageView 					upArrow;

	private	LinearLayout 				mlayoutContacts;
	private LinearLayout 				advanceSearchLayout;
	private LinearLayout 				mAddContainer, mWebContainer, mEmailContainer;
	private LinearLayout 				mSearchContainer;
	private LinearLayout 				circleIndicator;
	private LinearLayout 				wholeSearchBoxContainer;
	private LinearLayout 				mPackageListContainer;

	private ExpandableListView 			mTravelPackagesListView;

	private int 						flipperVisibleItemPosition = 0;
	private int 						city_id = -1;

	private boolean 					isAdvanceSearchLayoutOpen = false;

	private String 						selectedCity = "Entire Malaysia";
	private String 						mNumberToBeCalled;

	private ArrayList<String> 			imgPathList;
	private ArrayList<CityOrLocality> 	localityList;
	private ArrayList<CityOrLocality> 	cityList;
	private ArrayList<PackageList>		packageList;

	private ArrayList<String> 			selectedLocalityItems;
	private ArrayList<String> 			ids = new ArrayList<String>();
	private ArrayList<String> 			cityListString = new ArrayList<String>();
	private ArrayList<String> 			localityItems;
	private ArrayList<String> 			selectedLocalityindex;

	private MattaBoothDetailResponse 	mMattaBoothDetailResponse;
	private MattaBoothDetailRequest     mMattaBoothDetailRequest;
	private MattaPackageListRequest 	packageListReq;
	private TravelPackagesListAdapter 	mTravelPackagesListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_matta_booth_detail);
		ImageLoader.initialize(MattaBoothDetailActivity.this);
		AnalyticsHelper.logEvent(FlurryEventsConstants.MATTA_BOOTH_DETAIL);

		findViewById(R.id.img_add_profile).setOnClickListener(this);
		mlayoutContacts 			=	(LinearLayout) 	findViewById(R.id.layout_contacts);
		advanceSearchLayout 		= 	(LinearLayout) 	findViewById(R.id.advanceSearch);
		wholeSearchBoxContainer 	= 	(LinearLayout) 	findViewById(R.id.whole_search_box_container);
		mReferFB 					= 	(ImageView) 	findViewById(R.id.cd_facebook_icon);
		mReferTwitter 				= 	(ImageView) 	findViewById(R.id.cd_twitterIcon);
		mProfileIconView 			= 	(ImageView) 	findViewById(R.id.show_profile_icon);
		mHeaderBackButton 			= 	(ImageView) 	findViewById(R.id.header_btn_back);
		mHomeIconView 				= 	(ImageView) 	findViewById(R.id.goto_home_icon);
		mSearchBtn 					= 	(ImageView) 	findViewById(R.id.search_icon_button);
		upArrow 					= 	(ImageView) 	findViewById(R.id.upArrow);
		mTxtTitle 					= 	(TextView) 		findViewById(R.id.txt_comp_name);
		currentCity 				= 	(TextView) 		findViewById(R.id.currentCity);
		currentLocality 			= 	(TextView) 		findViewById(R.id.currentLocality);
		mainSearchButton 			= 	(TextView) 		findViewById(R.id.mainSearchButton);
		mSearchEditText 			= 	(EditText) 		findViewById(R.id.search_box);
		mPackageListContainer 		=	(LinearLayout) 	findViewById(R.id.cd_layout_paid_comp_list);

		mHomeIconView.setOnClickListener(this);
		mReferFB.setOnClickListener(this);
		mReferTwitter.setOnClickListener(this);
		mProfileIconView.setOnClickListener(this);
		mHeaderBackButton.setOnClickListener(this);
		mSearchBtn.setOnClickListener(MattaBoothDetailActivity.this);
		upArrow.setOnClickListener(this);
		currentCity.setOnClickListener(this);
		currentLocality.setOnClickListener(this);
		mainSearchButton.setOnClickListener(this);

		try {
			if(getIntent().getExtras() != null) {
				Bundle bundle 					= getIntent().getExtras();
				mMattaBoothDetailResponse 		= (MattaBoothDetailResponse) bundle.getSerializable(MattaConstants.DATA_MATTA_BOOTH_DETAIL_RESPONSE);
				mMattaBoothDetailRequest 		= (MattaBoothDetailRequest) bundle.getSerializable(MattaConstants.DATA_MATTA_BOOTH_DETAIL_REQUEST);
			}
			mMattaBoothDetailRequest.setSource(!StringUtil.isNullOrEmpty(mMattaBoothDetailResponse.getResults().getCompany().getSource()) ? mMattaBoothDetailResponse.getResults().getCompany().getSource() : "");
			advanceSearchLayout.setVisibility(View.GONE);
			currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCity + "</b>"));

			mSearchEditText.setText((!StringUtil.isNullOrEmpty(mSearchKeyword)) ? mSearchKeyword.trim() : "");

			setData();

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

			if (mMattaBoothDetailResponse.getResults().getCompany().getPackages() != null
					&& mMattaBoothDetailResponse.getResults().getCompany().getPackages().get(0) != null
					&& mMattaBoothDetailResponse.getResults().getCompany().getPackages().get(0).getPackage() != null
					&& mMattaBoothDetailResponse.getResults().getCompany().getPackages().get(0).getPackage().size() > 0) {
				packageList = (ArrayList<PackageList>) mMattaBoothDetailResponse.getResults().getCompany().getPackages().get(0).getPackage();

				mTravelPackagesListView		=	(ExpandableListView)  findViewById(R.id.cd_paid_company_list);

				setPackageListData();
				mPackageListContainer.setVisibility(View.VISIBLE);
				mTravelPackagesListView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						String companyId = ((PackageList)mTravelPackagesListAdapter.getItem(position)).getId() ;
						String postJsonPayload = jsonForPkg(((PackageList)mTravelPackagesListAdapter.getItem(position)).getKey(), ((PackageList)mTravelPackagesListAdapter.getItem(position)).getValue());
						MattaPackageListController packageListController = new MattaPackageListController(MattaBoothDetailActivity.this, MattaEvents.MATTA_PACKAGE_LIST_EVENT);
						packageListReq = new MattaPackageListRequest();
						packageListReq.setCompanyId(!StringUtil.isNullOrEmpty(companyId) ? companyId : "");
						packageListReq.setPostJsonPayload(!StringUtil.isNullOrEmpty(postJsonPayload) ? postJsonPayload : "");
						packageListReq.setHallId(!StringUtil.isNullOrEmpty(mMattaBoothDetailResponse.getResults().getCompany().getHallId()) ? mMattaBoothDetailResponse.getResults().getCompany().getHallId() : "");
						packageListReq.setSource(!StringUtil.isNullOrEmpty(mMattaBoothDetailResponse.getResults().getCompany().getSource()) ? mMattaBoothDetailResponse.getResults().getCompany().getSource() : "");
						packageListController.requestService(packageListReq);
						startSppiner();
					}
				});

			} else {
				mPackageListContainer.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		AnalyticsHelper.trackSession(MattaBoothDetailActivity.this, MattaConstants.Matta_Booth_Detail);
	}

	@Override
	protected void onDestroy() {
//		ImageLoader.clearCache();
		super.onDestroy();
	}

	private void setData() {

		mSearchContainer 					= (LinearLayout) 	findViewById(R.id.search_box_container);
		mSearchToggler 						= (ImageView) 		findViewById(R.id.search_toggler);
		mHeaderTitle 						= (TextView) 		findViewById(R.id.header_title);

		mSearchToggler.setOnClickListener(this);

		mHeaderTitle.setText((!StringUtil.isNullOrEmpty(mMattaBoothDetailResponse.getResults().getCompany().getCName())) ? Html.fromHtml(mMattaBoothDetailResponse.getResults().getCompany().getCName()) : "");
		mTxtTitle.setText((!StringUtil.isNullOrEmpty(mMattaBoothDetailResponse.getResults().getCompany().getCName())) ? Html.fromHtml(mMattaBoothDetailResponse.getResults().getCompany().getCName()) : "");
		mWebContainer 						= (LinearLayout) 		findViewById(R.id.cd_website_container);
		mAddContainer 						= (LinearLayout) 		findViewById(R.id.cd_address_container);
		mEmailContainer 					= (LinearLayout) 		findViewById(R.id.cd_email_container);
		mWebsiteView 						= (TextView) 			findViewById(R.id.cd_website);
		mAddressView 						= (TextView) 			findViewById(R.id.cd_address);
		mEmailView 							= (TextView) 			findViewById(R.id.cd_email);

		mWebsiteView.setOnClickListener(this);
		mWebsiteView.setText(Html.fromHtml(mMattaBoothDetailResponse.getResults().getCompany().getWebsite()));

		mEmailView.setOnClickListener(this);
		mEmailView.setText(Html.fromHtml(mMattaBoothDetailResponse.getResults().getCompany().getPTCEmail()));

		if (StringUtil.isNullOrEmpty(mMattaBoothDetailResponse.getResults().getCompany().getWebsite()))
			mWebContainer.setVisibility(View.GONE);
		if (StringUtil.isNullOrEmpty(mMattaBoothDetailResponse.getResults().getCompany().getPTCEmail()))
			mEmailContainer.setVisibility(View.GONE);
		mAddressView.setText(Html.fromHtml(getAddressText()));

		if (StringUtil.isNullOrEmpty(getAddressText())) 
			mAddContainer.setVisibility(View.GONE);

		ArrayList<String> contact_numbers = new ArrayList<String>(); 

		contact_numbers.add(mMattaBoothDetailResponse.getResults().getCompany().getPTCMo());
		inflateContacts(contact_numbers);

		imgPathList 		= new ArrayList<String>();
		imgPathList.add(mMattaBoothDetailResponse.getResults().getCompany().getImage());

		comDetailGallery 	= (ViewPager) 		findViewById(R.id.comp_image_pager);
		circleIndicator 	= (LinearLayout) 	findViewById(R.id.indicatorlinearlayout);

		if (imgPathList != null && imgPathList.size() > 0) {
			comDetailGallery.setVisibility(View.VISIBLE);
			MattaViewPagerAdapter pagerAdapter = new MattaViewPagerAdapter(getSupportFragmentManager(), imgPathList, this, MattaConstants.FLOW_FROM_MATTA_BOOTH_DETAIL);
			if (imgPathList.size() > 1) {
				addImage();
				circleIndicator.setVisibility(View.VISIBLE);
			} else
				circleIndicator.setVisibility(View.GONE);
			comDetailGallery.setAdapter(pagerAdapter);
		} else {
			comDetailGallery.setVisibility(View.GONE);
			circleIndicator.setVisibility(View.GONE);
		}

		comDetailGallery.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				indicatorchange(position);
				flipperVisibleItemPosition = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				comDetailGallery.getParent().requestDisallowInterceptTouchEvent(true);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) { }
		});
		stopSppiner();
	}

	private String getAddressText() {
		String str = "";
		str += (!StringUtil.isNullOrEmpty(mMattaBoothDetailResponse.getResults().getCompany().getLocation())) ? mMattaBoothDetailResponse.getResults().getCompany().getLocation() : "";
		return str;
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE 
				|| msg.arg2 == Events.USER_DETAIL
				|| msg.arg2 == MattaEvents.MATTA_BANNER_LANDING_BOOTH_DETAIL_EVENT) {
			super.updateUI(msg);
		} else if (msg.arg2 == MattaEvents.MATTA_BOOTH_DETAIL_EVENT) {
			if (msg.arg1 == 1) {
				showFinalDialog((String) msg.obj);
				stopSppiner();
			} else {
				mMattaBoothDetailResponse = (MattaBoothDetailResponse) msg.obj;
				setData();
			}
		}  else if (msg.arg2 == Events.CITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				cityList = glistRes.getCityOrLocalityList();
				Intent intent = new Intent(MattaBoothDetailActivity.this, AdvanceSelectCity.class);
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
			if (msg.arg1 == 1) 
				showInfoDialog((String) msg.obj);
			else {
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				localityList = glistRes.getCityOrLocalityList();
				Intent intent = new Intent(MattaBoothDetailActivity.this, AdvanceSelectLocalityActivity.class);
				localityItems = new ArrayList<String>();
				for (CityOrLocality dealCityOrLoc : localityList) {
					localityItems.add(dealCityOrLoc.getName());
				}
				intent.putExtra("LOCALITY_LIST", localityItems);
				intent.putStringArrayListExtra("LOCALITY_INDEX", selectedLocalityindex);
				startActivityForResult(intent, AppConstants.LOCALITY_REQUEST);
			}
		} else if (msg.arg2 == MattaEvents.MATTA_PACKAGE_LIST_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				MattaPackageListResponse packageRes = (MattaPackageListResponse) msg.obj;
				Intent packageListIntent = new Intent(MattaBoothDetailActivity.this, MattaPackageListActivity.class);
				packageListIntent.putExtra(MattaConstants.DATA_MATTA_PACKAGE_LIST_REQUEST, packageListReq);
				packageListIntent.putExtra(MattaConstants.DATA_MATTA_PACKAGE_LIST_RESPONSE, packageRes);
				startActivity(packageListIntent);
			}
			stopSppiner();
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE 
				|| event == Events.USER_DETAIL
				|| event == MattaEvents.MATTA_BANNER_LANDING_BOOTH_DETAIL_EVENT) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.CITY_LISTING || event == Events.LOCALITY_LISTING) {
			Message message = (Message) screenData;
			handler.sendMessage(message);
			return;
		} else if (event == MattaEvents.MATTA_PACKAGE_LIST_EVENT) {
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
		} else {
			Response response = (Response) screenData;
			Message message = new Message();
			message.arg2 = event;
			message.arg1 = 1;
			if (response.isError()) {
				message.obj = response.getErrorText();
			} else {
				if (response.getPayload() instanceof MattaBoothDetailResponse) {
					MattaBoothDetailResponse boothDetailResponse = (MattaBoothDetailResponse) response.getPayload();
					if (Integer.parseInt(boothDetailResponse.getResults().getError_Code()) != 0) {
						message.obj = getResources().getString(R.string.communication_failure);
					} else {
						message.arg1 = 0;
						message.obj = boothDetailResponse;
					}
				} else {
					message.obj = new String(getResources().getString(R.string.communication_failure));
				}
			}
			handler.sendMessage(message);
		}
	}

	private void inflateContacts(ArrayList<String> pContacts) {
		if (pContacts == null || pContacts.size() == 0) {
			return;
		}
		for (int i = 0; i < pContacts.size(); i++) {
			if (!StringUtil.isNullOrEmpty(pContacts.get(i))) {
				TextView textView = new TextView(this);
				textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
				textView.setGravity(Gravity.CENTER_VERTICAL);
				textView.setPadding(5, 2, 5, 2);
				textView.setTextColor(getResources().getColor(R.color.hyperlink_blue));
				textView.setTextSize(13);
				textView.setClickable(true);
				textView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						checkPreferenceAndMakeCall(((TextView) v).getText().toString());
					}
				});
				textView.setText(Html.fromHtml("<u>" + pContacts.get(i) + "</u>"));
				mlayoutContacts.addView(textView);
			}
		}
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	private void addToContact(String mNumber, String name) {
		ArrayList<String> contact_numberlist =  new ArrayList<String>();
		contact_numberlist.add(mMattaBoothDetailResponse.getResults().getCompany().getPTCMo());
		if (contact_numberlist.size() > 0 && !StringUtil.isNullOrEmpty(contact_numberlist.get(0))) {
			Intent intent = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT, Uri.parse("tel:" + mNumber));
			intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
			if (contact_numberlist.size() > 1 && !StringUtil.isNullOrEmpty(contact_numberlist.get(1))) {
				intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, contact_numberlist.get(1));
				intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
			}
			if (contact_numberlist.size() > 2 && !StringUtil.isNullOrEmpty(contact_numberlist.get(2))) {
				intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE, contact_numberlist.get(2));
				intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
			}
			if (!StringUtil.isNullOrEmpty(mMattaBoothDetailResponse.getResults().getCompany().getPTCEmail())) {
				intent.putExtra(ContactsContract.Intents.Insert.EMAIL, mMattaBoothDetailResponse.getResults().getCompany().getPTCEmail());
			}
			intent.putExtra(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT, true);
			intent.putExtra(ContactsContract.Intents.EXTRA_FORCE_CREATE, true);
			startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.search_toggler:
			AnalyticsHelper.logEvent(FlurryEventsConstants.HOME_SEARCH_CLICK);
			if (wholeSearchBoxContainer.getVisibility() == View.VISIBLE) {
				wholeSearchBoxContainer.setVisibility(View.GONE);
			} else
				wholeSearchBoxContainer.setVisibility(View.VISIBLE);

			if (mSearchContainer.getVisibility() == View.VISIBLE) 
				mSearchContainer.setVisibility(View.GONE);
			else 
				mSearchContainer.setVisibility(View.VISIBLE);
			break;

		case R.id.header_btn_back:
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			this.finish();
			break;

		case R.id.mainSearchButton:
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			String JSON_EXTRA = jsonForSearch();
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
			mMattaBoothDetailRequest.setKeyword(mSearchEditText.getText().toString());
			mMattaBoothDetailRequest.setCategoryTitle(mSearchEditText.getText().toString());
			break;

		case R.id.show_profile_icon:
			onProfileClick();
			break;

		case R.id.cd_facebook_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.FACEBOOK_CLICK);
			checkPreferenceAndOpenBrowser(AppConstants.FB_PAGE_URL);
			break;

		case R.id.cd_twitterIcon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.TWITTER_CLICK);
			checkPreferenceAndOpenBrowser(AppConstants.TWITTER_PAGE_URL);
			break;

		case R.id.img_add_profile:
			String contact_numberlist = mMattaBoothDetailResponse.getResults().getCompany().getPTCMo();
			if (!StringUtil.isNullOrEmpty(contact_numberlist))
				addToContact(contact_numberlist, mMattaBoothDetailResponse.getResults().getCompany().getCName().trim());
			else 
				showInfoDialog(getResources().getString(R.string.contact_unavailable));
			break;

		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			showHomeScreen();
			break;

		case R.id.cd_website:
			checkPreferenceAndOpenBrowser(mWebsiteView.getText().toString());
			break;

		case R.id.cd_email:
			if (isDialogToBeShown())
				showConfirmationDialog(CustomDialog.DATA_USAGE_DIALOG_FOR_EMAIL, getResources().getString(R.string.cd_msg_data_usage));
			else
				sendEmail();
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
				Intent cityIntent = new Intent(MattaBoothDetailActivity.this, AdvanceSelectCity.class);
				cityIntent.putExtra("CITY_LIST", cityListString);
				cityIntent.putExtra("SELECTED_CITY", selectedCity);
				startActivityForResult(cityIntent, AppConstants.CITY_REQUEST);
			} else
				setSearchCity();
			break;

		case R.id.currentLocality:
			if (localityItems != null && localityItems.size() > 0) {
				Intent localityIntent = new Intent(MattaBoothDetailActivity.this, AdvanceSelectLocalityActivity.class);
				localityIntent.putExtra("LOCALITY_LIST", localityItems);
				localityIntent.putStringArrayListExtra("LOCALITY_INDEX", selectedLocalityindex);
				startActivityForResult(localityIntent, AppConstants.LOCALITY_REQUEST);
			} else 
				setSearchLocality(city_id);
			break;
		}
	}

	private void checkPreferenceAndMakeCall(String numberToBeCalled) {
		mNumberToBeCalled = numberToBeCalled;
		NativeHelper.makeCall(MattaBoothDetailActivity.this, numberToBeCalled);
	}

	private void showGuestBranchingActivity() {
		Intent guestBranchIntent = new Intent(MattaBoothDetailActivity.this, GuestBranchingActivity.class);
		guestBranchIntent.putExtra(AppConstants.IS_FROM_COMP_DETAIL_ADD_FAV, true);
		startActivity(guestBranchIntent);
	}

	private void sendEmail() {
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", !StringUtil.isNullOrEmpty(mMattaBoothDetailResponse.getResults().getCompany().getPTCEmail()) ? mMattaBoothDetailResponse.getResults().getCompany().getPTCEmail() : "", null));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, mMattaBoothDetailResponse.getResults().getCompany().getCName().trim());
		startActivity(Intent.createChooser(emailIntent, "Send Email"));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == AppConstants.AR_REPORT_ERROR_SUCCESS) {
			showInfoDialog(getResources().getString(R.string.are_error_reported));
		} else if (resultCode == AppConstants.AR_REPORT_ERROR_FAILURE) {
			showInfoDialog(getResources().getString(R.string.are_error_occured));
		} else if (resultCode == RESULT_OK && requestCode == AppConstants.CITY_REQUEST) {
			if (!selectedCity.equalsIgnoreCase(data.getStringExtra("CITY_NAME"))) {
				localityItems = null;
				ids = null;
				selectedLocalityindex = null;
				currentLocality.setText("Choose your Area");
			}
			selectedCity = data.getStringExtra("CITY_NAME");
			currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCity + "</b>"));
			int index = data.getIntExtra("CITY_INDEX", 0);
			if (index == -1) {
				city_id = -1;
			} else {
				city_id = cityList.get(index).getId();
			}

		} else if (resultCode == RESULT_OK && requestCode == AppConstants.LOCALITY_REQUEST) {
			String locality = "";

			selectedLocalityItems = data.getStringArrayListExtra("SELECTED_LOCALITIES");

			selectedLocalityindex = data.getStringArrayListExtra("SELECTED_LOCALITIES_INDEX");
			if (selectedLocalityItems != null && selectedLocalityItems.size() > 0) {
				for (int i = 0; i < selectedLocalityItems.size(); i++) {
					if (i == selectedLocalityItems.size() - 1) 
						locality += selectedLocalityItems.get(i);
					else 
						locality += selectedLocalityItems.get(i) + ",";
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
	public void onPositiveDialogButton(int id) {
		if (id == CustomDialog.LOGIN_CONFIRMATION_DIALOG) {
			showGuestBranchingActivity();
		} else if (id == CustomDialog.CONFIRMATION_DIALOG) {
		} else if (id == CustomDialog.DATA_USAGE_DIALOG_FOR_EMAIL) {
			sendEmail();
		} else if (id == CustomDialog.DATA_USAGE_DIALOG_FOR_CALL) {
			NativeHelper.makeCall(MattaBoothDetailActivity.this, mNumberToBeCalled);
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
						localityArray.put("locality_name", selectedLocalityItems.get(i));
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

	public String jsonForPkg(String key, String value) {
		try {
			if (!StringUtil.isNullOrEmpty(key) && !StringUtil.isNullOrEmpty(value)) {
				JSONObject jsonObject = new JSONObject();
				JSONObject jsonSelector = new JSONObject();
				JSONArray jarray = new JSONArray();
				jarray.put(value);
				jsonSelector.put("field_1", jarray);
				jsonObject.put("selector", jsonSelector);
				System.out.println(jsonObject);
				return jsonObject.toString();
			} else {
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void indicatorchange(int pos) {
		for (int i = 0; i < imgPathList.size(); i++) {
			circleIndicator.getChildAt(i).setBackgroundResource(R.drawable.circle_gray);
		}
		circleIndicator.getChildAt(pos).setBackgroundResource(R.drawable.circle_black);
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

	public void viewFlipperTapped() {
		Intent intents = new Intent(MattaBoothDetailActivity.this, MattaPhotoSlideActivity.class);
		intents.putStringArrayListExtra("list", imgPathList);
		intents.putExtra("flowFrom", MattaConstants.FLOW_FROM_MATTA_BOOTH_DETAIL);
		intents.putExtra("position", flipperVisibleItemPosition);
		startActivity(intents);
	}

	private void setPackageListData() {
		mTravelPackagesListAdapter = 	new TravelPackagesListAdapter(MattaBoothDetailActivity.this);
		mTravelPackagesListAdapter.setData(packageList);
		mTravelPackagesListAdapter.notifyDataSetChanged();
		mTravelPackagesListView.setAdapter(mTravelPackagesListAdapter);
		mPackageListContainer.setVisibility(View.VISIBLE);
	}
}