package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.PaidCompanyListAdapter;
import com.kelltontech.maxisgetit.adapters.ViewPagerAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.CompanyDetailAddFavController;
import com.kelltontech.maxisgetit.controllers.CompanyDetailController;
import com.kelltontech.maxisgetit.controllers.CompanyDetailRemoveFavController;
import com.kelltontech.maxisgetit.controllers.PaidCompanyListController;
import com.kelltontech.maxisgetit.dao.AttributeGroup;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.CompanyReview;
import com.kelltontech.maxisgetit.dao.FavouriteCompanies;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.dao.IconUrl;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.db.FavCompanysTable;
import com.kelltontech.maxisgetit.model.paidcompany.PaidCompany;
import com.kelltontech.maxisgetit.model.paidcompany.PaidCompanyResponse;
import com.kelltontech.maxisgetit.requests.DetailRequest;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.ui.widgets.EllipsizingTextView;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class CompanyDetailActivity extends MaxisMainActivity {

	private View 						seprator;
	private Button 						mViewMoreReviews;
	private ViewPager 					comDetailGallery;
	private EditText 					mSearchEditText;
	private RatingBar 					mUserRating;
	private EllipsizingTextView 		mCompDesc;

	private TextView 					mTxtRatedUserCount;
	private TextView 					mTxtTitle;
	private TextView 					mTxtRateMe;
	private TextView 					mTxtDistanceTitle;
	private TextView 					mAddressView;
	private TextView 					mMoreDesc;
	private TextView 					currentCity, currentLocality;
	private TextView 					mainSearchButton;
	private TextView 					mHeaderTitle;
	private TextView 					mWebsiteView;

	private ImageView 					mEndSeparator;
	private ImageView 					mHeaderBackButton;
	private ImageView 					mHomeIconView;
	private ImageView 					mProfileIconView;
	private ImageView 					mReferFB, mReferTwitter;
	private ImageView 					mSearchBtn;
	private ImageView 					mMapView;
	private ImageView 					mCallBtnView;
	private ImageView 					mEmailBtnView;
	private ImageView 					mFavBtnView;
	private ImageView 					mSearchToggler;
	private ImageView 					upArrow;
	private ImageView 					videoThumbnail;

	private	LinearLayout 				mlayoutContacts;
	private LinearLayout 				mReviewList;
	private LinearLayout 				mNoReviewLayout;
	private LinearLayout 				advanceSearchLayout;
	private LinearLayout 				mAddContainer, mWebContainer;
	private LinearLayout 				mAttributeGroupContainer;
	private LinearLayout 				mSearchContainer;
	private LinearLayout 				circleIndicator;
	private LinearLayout 				wholeSearchBoxContainer;
	private LinearLayout 				videoContainer;
	private LinearLayout 				mPaidCompanyListContainer;
	
	private ListView 					mPaidCompanyListView;
	
	private int 						flipperVisibleItemPosition = 0;
	private int 						city_id = -1;

	private boolean 					mIsAddedToFav = false;
	private boolean 					isAdvanceSearchLayoutOpen = false;
	private boolean 					mIsCollapsedView = true;	

	private String 						selectedCity = "Entire Malaysia";
	private String 						compCatIdToCompare;
	private String 						mCategoryid;
	private String 						id;
	private String 						mNumberToBeCalled;

	private ArrayList<IconUrl> 			imgPathList;
	private ArrayList<CityOrLocality> 	localityList;
	private ArrayList<CityOrLocality> 	cityList;
	private ArrayList<PaidCompany>		paidCompanyListData;

	private ArrayList<String> 			selectedLocalityItems;
	private ArrayList<String> 			ids = new ArrayList<String>();
	private ArrayList<String> 			cityListString = new ArrayList<String>();
	private ArrayList<String> 			localityItems;
	private ArrayList<String> 			selectedLocalityindex;

	private CompanyDetail 				mCompanyDetail;
	private PaidCompanyResponse			mPaidCompanyResponse;
	private PaidCompanyListAdapter 		mPaidCompListAdapter;

	private MaxisStore 					store;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_detail);
		ImageLoader.initialize(CompanyDetailActivity.this);
		AnalyticsHelper.logEvent(FlurryEventsConstants.APPLICATION_COMPANY_DETAIL);

		findViewById(R.id.img_add_profile).setOnClickListener(this);
		findViewById(R.id.cd_view_on_map).setOnClickListener(this);
		findViewById(R.id.cd_report_error).setOnClickListener(this);
		findViewById(R.id.cd_txt_tnc).setOnClickListener(this);
		findViewById(R.id.cd_write_review).setOnClickListener(this);
		mReviewList 				= 	(LinearLayout) 	findViewById(R.id.cd_reviews_list);
		mNoReviewLayout 			= 	(LinearLayout) 	findViewById(R.id.cd_no_reviews_layout);
		mAttributeGroupContainer 	= 	(LinearLayout) 	findViewById(R.id.cd_attr_group_container);
		mlayoutContacts 			=	(LinearLayout) 	findViewById(R.id.layout_contacts);
		advanceSearchLayout 		= 	(LinearLayout) 	findViewById(R.id.advanceSearch);
		wholeSearchBoxContainer 	= 	(LinearLayout) 	findViewById(R.id.whole_search_box_container);
		videoContainer 				= 	(LinearLayout) 	findViewById(R.id.cd_video_container);
		seprator 					= 	(View) 			findViewById(R.id.seprator);
		mUserRating 				= 	(RatingBar) 	findViewById(R.id.cd_rating_comp);
		mViewMoreReviews 			= 	(Button) 		findViewById(R.id.cd_btn_view_more);
		mReferFB 					= 	(ImageView) 	findViewById(R.id.cd_facebook_icon);
		mReferTwitter 				= 	(ImageView) 	findViewById(R.id.cd_twitterIcon);
		mProfileIconView 			= 	(ImageView) 	findViewById(R.id.show_profile_icon);
		mHeaderBackButton 			= 	(ImageView) 	findViewById(R.id.header_btn_back);
		mHomeIconView 				= 	(ImageView) 	findViewById(R.id.goto_home_icon);
		mSearchBtn 					= 	(ImageView) 	findViewById(R.id.search_icon_button);
		mCallBtnView 				= 	(ImageView)		findViewById(R.id.cd_call_btn);
		mEmailBtnView 				= 	(ImageView) 	findViewById(R.id.cd_email_btn);
		mFavBtnView 				= 	(ImageView) 	findViewById(R.id.cd_fav_btn);
		mEndSeparator 				= 	(ImageView) 	findViewById(R.id.cd_end_separator);
		mMapView 					= 	(ImageView) 	findViewById(R.id.cd_point_me_there_btn);
		upArrow 					= 	(ImageView) 	findViewById(R.id.upArrow);
		videoThumbnail 				= 	(ImageView) 	findViewById(R.id.img_video_thumbnail);
		mTxtRatedUserCount 			= 	(TextView) 		findViewById(R.id.txt_rated_user_count);
		mTxtTitle 					= 	(TextView) 		findViewById(R.id.txt_comp_name);
		mTxtRateMe 					= 	(TextView) 		findViewById(R.id.txt_rate_me);
		mTxtDistanceTitle 			= 	(TextView) 		findViewById(R.id.cd_title_distance);
		currentCity 				= 	(TextView) 		findViewById(R.id.currentCity);
		currentLocality 			= 	(TextView) 		findViewById(R.id.currentLocality);
		mainSearchButton 			= 	(TextView) 		findViewById(R.id.mainSearchButton);
		mSearchEditText 			= 	(EditText) 		findViewById(R.id.search_box);

		mHomeIconView.setOnClickListener(this);
		mReferFB.setOnClickListener(this);
		mReferTwitter.setOnClickListener(this);
		mProfileIconView.setOnClickListener(this);
		mHeaderBackButton.setOnClickListener(this);
		mSearchBtn.setOnClickListener(CompanyDetailActivity.this);
		mMapView.setOnClickListener(this);
		mCallBtnView.setOnClickListener(this);
		mEmailBtnView.setOnClickListener(this);
		mFavBtnView.setOnClickListener(this);
		mViewMoreReviews.setOnClickListener(this);
		mTxtRateMe.setOnClickListener(this);
		upArrow.setOnClickListener(this);
		currentCity.setOnClickListener(this);
		currentLocality.setOnClickListener(this);
		mainSearchButton.setOnClickListener(this);
		videoThumbnail.setOnClickListener(this);

		Bundle bundle 		= getIntent().getExtras();
		mCompanyDetail 		= bundle.getParcelable(AppConstants.COMP_DETAIL_DATA);
		store 				= MaxisStore.getStore(this);

		if (!mCompanyDetail.isPaid()) {
			
			mPaidCompanyListContainer 	=	(LinearLayout) 	findViewById(R.id.cd_layout_paid_comp_list);
			mPaidCompanyListView		=	(ListView)		findViewById(R.id.cd_paid_company_list);
			
			mPaidCompanyListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						CompanyDetailController companyDetailController = new CompanyDetailController(CompanyDetailActivity.this, Events.PAID_COMPANY_DETAIL_EVENT);
						String id = ((PaidCompany)mPaidCompListAdapter.getItem(arg2)).getId() ;
						DetailRequest detailRequest = new DetailRequest(CompanyDetailActivity.this, id, false,((PaidCompany) mPaidCompListAdapter.getItem(arg2)).getL3Catid());
						startSppiner();
						companyDetailController.requestService(detailRequest);
				}
			});
			
			PaidCompanyListController listContrller = new PaidCompanyListController(CompanyDetailActivity.this, Events.PAID_COMPANY_LIST_EVENT);
			if((!StringUtil.isNullOrEmpty(mCompanyDetail.getCatId()))) {
				listContrller.requestService(mCompanyDetail.getCatId());
				startSppiner();
			}
		}

		advanceSearchLayout.setVisibility(View.GONE);
		currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCity + "</b>"));

		mFavBtnView.setVisibility(View.VISIBLE);

		mSearchEditText.setText((!StringUtil.isNullOrEmpty(mSearchKeyword)) ? mSearchKeyword.trim() : "");

		if (mCompanyDetail == null) {
			try {
				id 									= bundle.getString(AppConstants.COMP_ID);
				mCategoryid 						= getIntent().getStringExtra(AppConstants.CATEGORY_ID_KEY);
				CompanyDetailController controller 	= new CompanyDetailController(CompanyDetailActivity.this, Events.COMPANY_DETAIL);
				DetailRequest detailRequest 		= new DetailRequest(CompanyDetailActivity.this, id, false, mCategoryid);
				startSppiner();
				controller.requestService(detailRequest);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "compid:" + id + "catid:" + mCategoryid + getIntent().getExtras().getBoolean(AppConstants.IS_DEAL_LIST), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		} else {
			setData();
		}

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

	@Override
	protected void onResume() {
		if (store.isLoogedInUser()) {
			boolean isadded = false;
			if (mCompanyDetail == null) {
				compCatIdToCompare = id + "-" + mCategoryid;
			} else {
				if (!StringUtil.isNullOrEmpty(mCompanyDetail.getId()) && !StringUtil.isNullOrEmpty(mCompanyDetail.getCatId())) {
					Log.e("FINDIT", "compId:" + mCompanyDetail.getId() + "catid:" + mCompanyDetail.getCatId());
					compCatIdToCompare = mCompanyDetail.getId() + "-" + mCompanyDetail.getCatId();
				}
			}

			FavCompanysTable FavCompTable = new FavCompanysTable((MyApplication) getApplication());
			ArrayList<FavouriteCompanies> favCompaniesList = FavCompTable.getAllFavCompaniesList();

			for (int i = 0; i < favCompaniesList.size(); i++) {
				if (favCompaniesList.get(i).getFavComIdCategoryId().equals(compCatIdToCompare)) {
					mIsAddedToFav = true;
					isadded = true;
					Log.e("FINDIT", "Got Equal : "	+ favCompaniesList.get(i).getFavComIdCategoryId() + "::" + compCatIdToCompare);
				} /*else {
					// mFavBtnView.setImageResource(R.drawable.selector_cd_fav_btn);
				}*/
			}

			if (isadded)
				mFavBtnView.setImageResource(R.drawable.selector_cd_fav_remove_btn);
			else {
				mFavBtnView.setImageResource(R.drawable.selector_cd_fav_btn);
				mIsAddedToFav = false;
			}
		}

		super.onResume();
		AnalyticsHelper.trackSession(CompanyDetailActivity.this, AppConstants.Company_detail);
	}

	@Override
	protected void onDestroy() {
		ImageLoader.clearCache();
		super.onDestroy();
	}

	private void setData() {

		mSearchContainer 					= (LinearLayout) 	findViewById(R.id.search_box_container);
		mSearchToggler 						= (ImageView) 		findViewById(R.id.search_toggler);
		mHeaderTitle 						= (TextView) 		findViewById(R.id.header_title);

		mSearchToggler.setOnClickListener(this);

		mHeaderTitle.setText(Html.fromHtml(mCompanyDetail.getTitle()));
		mTxtTitle.setText(Html.fromHtml(mCompanyDetail.getTitle()));
		mTxtRatedUserCount.setText("( " + String.valueOf(mCompanyDetail.getRatedUserCount()) + " )");

		if (!StringUtil.isNullOrEmpty(mCompanyDetail.getDescription())) {

			findViewById(R.id.layout_comp_desc).setVisibility(View.VISIBLE);
			mCompDesc 						= (EllipsizingTextView) findViewById(R.id.cd_description);
			mMoreDesc 						= (TextView) 			findViewById(R.id.cd_desc_more);

			mCompDesc.setText(Html.fromHtml(mCompanyDetail.getDescription()));
			mCompDesc.setMaxLines(5);

			if (mCompanyDetail.getDescription().length() < 280)
				mMoreDesc.setVisibility(View.GONE);
			else
				mMoreDesc.setVisibility(View.VISIBLE);

			mMoreDesc.setText(Html.fromHtml("<u>" + getResources().getString(R.string.more) + "</u>"));
			mMoreDesc.setOnClickListener(this);
		} else
			findViewById(R.id.layout_comp_desc).setVisibility(View.GONE);

		mWebContainer 						= (LinearLayout) 		findViewById(R.id.cd_website_container);
		mAddContainer 						= (LinearLayout) 		findViewById(R.id.cd_address_container);
		mWebsiteView 						= (TextView) 			findViewById(R.id.cd_website);
		mAddressView 						= (TextView) 			findViewById(R.id.cd_address);

		mWebsiteView.setOnClickListener(this);
		mWebsiteView.setText(Html.fromHtml("<u>" + mCompanyDetail.getWebsite()	+ "</u>"));

		if (StringUtil.isNullOrEmpty(mCompanyDetail.getWebsite()))
			mWebContainer.setVisibility(View.GONE);

		mAddressView.setText(getAddressText());

		if (StringUtil.isNullOrEmpty(getAddressText())) 
			mAddContainer.setVisibility(View.GONE);

		ArrayList<String> contact_numbers = mCompanyDetail.getContacts();
		inflateContacts(contact_numbers);

		for (int i = 0; i < mCompanyDetail.getAttrGroups().size(); i++) {
			inflateAttributeGroup(mCompanyDetail.getAttrGroups().get(i));
		}

		if (StringUtil.isNullOrEmpty(mCompanyDetail.getDistance()) || mCompanyDetail.getDistance().equals("0"))
			mTxtDistanceTitle.setVisibility(View.GONE);
		else 
			mTxtDistanceTitle.setText(mCompanyDetail.getDistance());

		if (mCompanyDetail.getRecordType().equalsIgnoreCase(AppConstants.COMP_TYPE_DEAL)) {
			mReviewList.setVisibility(View.GONE);
			mNoReviewLayout.setVisibility(View.GONE);
			mUserRating.setVisibility(View.GONE);
			findViewById(R.id.cd_layout_rate_us).setVisibility(View.GONE);
			findViewById(R.id.are_lable_brief_des).setVisibility(View.GONE);
			findViewById(R.id.cd_layout_report_an_error).setVisibility(View.GONE);
			mViewMoreReviews.setVisibility(View.GONE);
		} else {
			inflateReviewsList(mCompanyDetail.getCompanyReviewList());
			mUserRating.setRating(mCompanyDetail.getRating());
		}

		if (StringUtil.isNullOrEmpty(mCompanyDetail.getMailId())) {
			mEmailBtnView.setEnabled(false);
		}
		if (mCompanyDetail.getContacts() == null || mCompanyDetail.getContacts().size() == 0)
			mCallBtnView.setEnabled(false);
		if (StringUtil.isNullOrEmpty(mCompanyDetail.getVideoUrl())) {
			videoContainer.setVisibility(View.GONE);
			seprator.setVisibility(View.GONE);
		} else {
			videoContainer.setVisibility(View.VISIBLE);
			seprator.setVisibility(View.VISIBLE);
		}

		imgPathList 		= mCompanyDetail.getIconUrl();
		comDetailGallery 	= (ViewPager) 		findViewById(R.id.comp_image_pager);
		circleIndicator 	= (LinearLayout) 	findViewById(R.id.indicatorlinearlayout);

		if (imgPathList != null && imgPathList.size() > 0) {
			comDetailGallery.setVisibility(View.VISIBLE);
			ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), imgPathList, this, AppConstants.FLOW_FROM_COMPANY_DETAIL);
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

	}

	private String getAddressText() {
		String str = "";
		str += (!StringUtil.isNullOrEmpty(mCompanyDetail.getBuilding())) ? mCompanyDetail
				.getBuilding() + ", "
				: "";
		str += (!StringUtil.isNullOrEmpty(mCompanyDetail.getLandmark())) ? mCompanyDetail
				.getLandmark() + ", "
				: "";
		str += (!StringUtil.isNullOrEmpty(mCompanyDetail.getStreet())) ? mCompanyDetail
				.getStreet() + ", "
				: "";
		str += (!StringUtil.isNullOrEmpty(mCompanyDetail.getSubLocality())) ? mCompanyDetail
				.getSubLocality() + ", "
				: "";
		str += (!StringUtil.isNullOrEmpty(mCompanyDetail.getLocality())) ? mCompanyDetail
				.getLocality() + ", "
				: "";
		str += (!StringUtil.isNullOrEmpty(mCompanyDetail.getCity())) ? mCompanyDetail
				.getCity() + ", "
				: "";
		str += (!StringUtil.isNullOrEmpty(mCompanyDetail.getState())) ? mCompanyDetail
				.getState() + " "
				: "";
		str += (!StringUtil.isNullOrEmpty(mCompanyDetail.getPincode())) ? mCompanyDetail
				.getPincode() : "";
				return str;
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE || msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.COMPANY_DETAIL) {
			if (msg.arg1 == 1) {
				showFinalDialog((String) msg.obj);
			} else {
				mCompanyDetail = (CompanyDetail) msg.obj;
				setData();
			}
			stopSppiner();
		} else if (msg.arg2 == Events.COMPANY_DETAIL_ADD_FAV) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				addToFav();
			}
		} else if (msg.arg2 == Events.COMPANY_DETAIL_REMOVE_FAV) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				removeFromFav();
				if (FavCompanyListActivity.compListData != null) {
					if (FavCompanyListActivity.compListData.size() > 0) {
						Log.e("FINDIT", "index" + getIntent().getExtras().getString("index"));
						int index = Integer.parseInt(getIntent().getExtras().getString("index"));
						FavCompanyListActivity.compListData.remove(index);
					}
					FavCompanyListActivity.isRemovedFromCompanyDetail = true;
				}
			}
		} else if (msg.arg2 == Events.CITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
//				CityTable cityTable = new CityTable((MyApplication) getApplication());
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				cityList = glistRes.getCityOrLocalityList();
				Intent intent = new Intent(CompanyDetailActivity.this, AdvanceSelectCity.class);
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
				Intent intent = new Intent(CompanyDetailActivity.this, AdvanceSelectLocalityActivity.class);
				localityItems = new ArrayList<String>();
				for (CityOrLocality dealCityOrLoc : localityList) {
					localityItems.add(dealCityOrLoc.getName());
				}
				intent.putExtra("LOCALITY_LIST", localityItems);
				intent.putStringArrayListExtra("LOCALITY_INDEX", selectedLocalityindex);
				startActivityForResult(intent, AppConstants.LOCALITY_REQUEST);
			}
		} else if (msg.arg2 == Events.PAID_COMPANY_LIST_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
				mPaidCompanyListContainer.setVisibility(View.GONE);
			} else {
				mPaidCompanyResponse = (PaidCompanyResponse) msg.obj;
				if (mPaidCompanyResponse != null && mPaidCompanyResponse.getResults() != null) {
					if (mPaidCompanyResponse.getResults().getCompany() != null && mPaidCompanyResponse.getResults().getCompany().size() > 0) {
						paidCompanyListData = mPaidCompanyResponse.getResults().getCompany();
						setPaidCompanyListData();
					} else
						mPaidCompanyListContainer.setVisibility(View.GONE);
				}
			}
			stopSppiner();
		} else if (msg.arg2 == Events.PAID_COMPANY_DETAIL_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				CompanyDetail compListResp = (CompanyDetail) msg.obj;
				if (!StringUtil.isNullOrEmpty(compListResp.getId())) {
					Intent intent = new Intent(CompanyDetailActivity.this,CompanyDetailActivity.class);
					intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
					intent.putExtra(AppConstants.COMP_DETAIL_DATA, compListResp);
					startActivity(intent);
				} else {
					showInfoDialog(getResources().getString(R.string.no_result_found));
				}
			}
			stopSppiner();
		}
	}

	private void addToFav() {
		AnalyticsHelper.logEvent(FlurryEventsConstants.ADD_TO_FAV_CLICK);
		mFavBtnView.setImageResource(R.drawable.selector_cd_fav_remove_btn);
		Toast.makeText(CompanyDetailActivity.this, getString(R.string.company_add_to_fav), Toast.LENGTH_SHORT).show();
		mIsAddedToFav = true;
		FavCompanysTable FavCompTable 						= new FavCompanysTable((MyApplication) getApplication());
		ArrayList<FavouriteCompanies> companyIdCategoryId 	= new ArrayList<FavouriteCompanies>();
		FavouriteCompanies favCompany 						= new FavouriteCompanies();
		favCompany.setFavComIdCategoryId(compCatIdToCompare);
		companyIdCategoryId.add(favCompany);
		FavCompTable.addFavCompaniesList(companyIdCategoryId);
	}

	private void removeFromFav() {
		AnalyticsHelper.logEvent(FlurryEventsConstants.REMOVE_FAV_CLICK);
		mFavBtnView.setImageResource(R.drawable.selector_cd_fav_btn);
		Toast.makeText(CompanyDetailActivity.this,getString(R.string.company_remove_from_fav), Toast.LENGTH_SHORT).show();
		mIsAddedToFav = false;
		FavCompanysTable FavCompTable = new FavCompanysTable((MyApplication) getApplication());
		ArrayList<FavouriteCompanies> companyIdCategoryId = new ArrayList<FavouriteCompanies>();
		FavouriteCompanies favCompany = new FavouriteCompanies();
		favCompany.setFavComIdCategoryId(compCatIdToCompare);
		companyIdCategoryId.add(favCompany);
		FavCompTable.delFavCompaniesList(companyIdCategoryId);
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE || event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.COMPANY_DETAIL_ADD_FAV) {
			handler.sendMessage((Message) screenData);
			return;
		} else if (event == Events.COMPANY_DETAIL_REMOVE_FAV) {
			handler.sendMessage((Message) screenData);
			return;
		} else if (event == Events.CITY_LISTING || event == Events.LOCALITY_LISTING) {
			Message message = (Message) screenData;
			handler.sendMessage(message);
		} else if (event == Events.PAID_COMPANY_LIST_EVENT) {
			PaidCompanyResponse paidCompanyResponse = (PaidCompanyResponse) screenData;
			Message message = new Message();
			message.arg2 = event;
			if (paidCompanyResponse.getResults().getError_Code().equals("1")) {
				message.arg1 = 1;
				message.obj = getResources().getString(R.string.communication_failure);
			} else {
				message.arg1 = 0;
				message.obj = paidCompanyResponse;
			}
			handler.sendMessage(message);
		} else {
			Response response = (Response) screenData;
			Message message = new Message();
			message.arg2 = event;
			message.arg1 = 1;
			if (response.isError()) {
				message.obj = response.getErrorText();
			} else {
				if (response.getPayload() instanceof CompanyDetail) {
					CompanyDetail compDetail = (CompanyDetail) response.getPayload();
					if (compDetail.getErrorCode() != 0) {
						message.obj = getResources().getString(R.string.communication_failure);
					} else {
						message.arg1 = 0;
						message.obj = compDetail;
					}
				} else {
					message.obj = new String(getResources().getString(R.string.communication_failure));
				}
			}
			handler.sendMessage(message);
		}
	}

	private void inflateAttributeGroup(AttributeGroup attrGroup) {
		if (attrGroup.getValues().size() == 0) {
			mAttributeGroupContainer.setVisibility(View.GONE);
			return;
		}
		mAttributeGroupContainer.setVisibility(View.VISIBLE);
		LayoutInflater inflater 		= (LayoutInflater) 	getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout editTxtContainer 	= (LinearLayout) 	inflater.inflate(R.layout.attribute_group_layout, null);
		TextView v 						= (TextView) 		editTxtContainer.findViewById(R.id.agl_text_view);
		v.setText(attrGroup.getLable());
		LinearLayout valuesContainer 	= (LinearLayout) 	editTxtContainer.findViewById(R.id.agl_value_container);
		for (int i = 0; i < attrGroup.getValues().size(); i++) {
			TextView value = new TextView(CompanyDetailActivity.this);
			value.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			value.setTextSize(13);
			value.setTextColor(getResources().getColor(R.color.text_light_grey));
			if (!StringUtil.isNullOrEmpty(attrGroup.getValues().get(i)));
			value.setText(Html.fromHtml(attrGroup.getValues().get(i).replace("|", ", ")));
			valuesContainer.addView(value);
		}
		mAttributeGroupContainer.addView(editTxtContainer);
	}

	private void inflateReviewsList(ArrayList<CompanyReview> reviesList) {
		if (reviesList.size() == 0) {
			mReviewList.setVisibility(View.GONE);
			mViewMoreReviews.setVisibility(View.GONE);
			mNoReviewLayout.setVisibility(View.VISIBLE);
			return;
		}
		mNoReviewLayout.setVisibility(View.GONE);
		if (mCompanyDetail.getTotalReviewCount() > 3)
			mViewMoreReviews.setVisibility(View.VISIBLE);
		else {
			mEndSeparator.setVisibility(View.GONE);
			mViewMoreReviews.setVisibility(View.GONE);
		}

		mReviewList.setVisibility(View.VISIBLE);

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		for (int i = 0; i < reviesList.size(); i++) {
			LinearLayout reviewListRow = (LinearLayout) inflater.inflate(R.layout.reviews_list_row, null);
			TextView reviewTvUser = (TextView) reviewListRow.findViewById(R.id.review_txt_user);
			TextView reviewTvDescription = (TextView) reviewListRow.findViewById(R.id.review_txt_description);
			TextView reviewTvPostedOn = (TextView) reviewListRow.findViewById(R.id.review_txt_posted_on);
			RatingBar reviewRating = (RatingBar) reviewListRow.findViewById(R.id.review_rating);

			CompanyReview companyReview = reviesList.get(i);
			if (!StringUtil.isNullOrEmpty(companyReview.getUserName()))
				reviewTvUser.setText(Html.fromHtml(companyReview.getUserName()));
			else
				reviewTvUser.setText("");

			if (!StringUtil.isNullOrEmpty(companyReview.getReviewDesc()))
				reviewTvDescription.setText(Html.fromHtml(companyReview.getReviewDesc()));
			else
				reviewTvDescription.setText("");

			if (!StringUtil.isNullOrEmpty(companyReview.getReportedOn()))
				reviewTvPostedOn.setText(this.getResources().getString(R.string.postedOn) + " " + Html.fromHtml(companyReview.getReportedOn()));
			else
				reviewTvPostedOn.setText("");

			if (companyReview.getRating() > 0)
				reviewRating.setRating(companyReview.getRating());
			else
				reviewRating.setRating(0);

			mReviewList.addView(reviewListRow);

			ImageView separator = new ImageView(this);
			separator.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
			separator.setImageResource(R.drawable.single_line_seprator);
			separator.setBackgroundColor(getResources().getColor(R.color.bar_light_grey));
			mReviewList.addView(separator);
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
		ArrayList<String> contact_numberlist = mCompanyDetail.getContacts();
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
			if (!StringUtil.isNullOrEmpty(mCompanyDetail.getMailId())) {
				intent.putExtra(ContactsContract.Intents.Insert.EMAIL, mCompanyDetail.getMailId());
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

		case R.id.cd_view_on_map:
			if (isDialogToBeShown())
				showConfirmationDialog(CustomDialog.DATA_USAGE_DIALOG, getResources().getString(R.string.cd_msg_data_usage));
			else
				showMapActivity();
			break;

		case R.id.cd_call_btn:
			ArrayList<String> contact_numbers = mCompanyDetail.getContacts();
			if (contact_numbers != null && contact_numbers.size() > 0 && !StringUtil.isNullOrEmpty(contact_numbers.get(0))) 
				checkPreferenceAndMakeCall(contact_numbers.get(0));
			else 
				showInfoDialog(getResources().getString(R.string.contact_unavailable));
			break;

		case R.id.cd_email_btn:
			if (isDialogToBeShown())
				showConfirmationDialog(CustomDialog.DATA_USAGE_DIALOG_FOR_EMAIL, getResources().getString(R.string.cd_msg_data_usage));
			else
				sendEmail();
			break;

		case R.id.mainSearchButton:
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			String JSON_EXTRA = jsonForSearch();
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
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

		case R.id.cd_desc_more:
			if (mIsCollapsedView) {
				mCompDesc.setMaxLines(Integer.MAX_VALUE);
				mMoreDesc.setText(Html.fromHtml("<u>" + getResources().getString(R.string.less) + "</u>"));
				mIsCollapsedView = false;
			} else {
				mCompDesc.setMaxLines(5);
				mMoreDesc.setText(Html.fromHtml("<u>" + getResources().getString(R.string.more) + "</u>"));
				mIsCollapsedView = true;
			}
			break;

		case R.id.img_add_profile:
			ArrayList<String> contact_numberlist = mCompanyDetail.getContacts();
			if (contact_numberlist.size() > 0 && !StringUtil.isNullOrEmpty(contact_numberlist.get(0))) {
				Log.i("FINDIT", "html form" + Html.fromHtml(mCompanyDetail.getTitle()));
				addToContact(contact_numberlist.get(0), Html.fromHtml(mCompanyDetail.getTitle()).toString());
			} else 
				showInfoDialog(getResources().getString(R.string.contact_unavailable));
			break;

		case R.id.cd_fav_btn:
			if (!mIsAddedToFav) 
				handleAddFavourites();
			else 
				showConfirmationDialog(CustomDialog.DELETE_CONFIRMATION_DIALOG, getString(R.string.fav_companies_remove_confirmation));
			break;

		case R.id.txt_rate_me:
		case R.id.cd_write_review: 
			Intent rateCompanyIntent = new Intent(CompanyDetailActivity.this, RateCompanyActivity.class);
			rateCompanyIntent.putExtra(AppConstants.COMP_DETAIL_DATA, mCompanyDetail);
			rateCompanyIntent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
			startActivity(rateCompanyIntent);
			break;

		case R.id.cd_point_me_there_btn:
			// Open Canvas activity
			pointMeThere();
			break;

		case R.id.cd_report_error: 
			Intent reportErrorIntent = new Intent(CompanyDetailActivity.this, ReportErrorActivity.class);
			reportErrorIntent.putExtra(AppConstants.COMP_DETAIL_DATA, mCompanyDetail);
			reportErrorIntent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
			startActivity(reportErrorIntent);
			break;

		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			showHomeScreen();
			break;

		case R.id.cd_website:
			checkPreferenceAndOpenBrowser(mWebsiteView.getText().toString());
			break;

		case R.id.cd_txt_tnc:
			startActivity(new Intent(CompanyDetailActivity.this, TermsAndConditionActivity.class));
			break;

		case R.id.cd_btn_view_more:
			Intent intent = new Intent(CompanyDetailActivity.this, ViewAllReviewsActivity.class);
			intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
			intent.putExtra(AppConstants.COMP_DETAIL_DATA, mCompanyDetail);
			startActivity(intent);
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
				Intent cityIntent = new Intent(CompanyDetailActivity.this, AdvanceSelectCity.class);
				cityIntent.putExtra("CITY_LIST", cityListString);
				cityIntent.putExtra("SELECTED_CITY", selectedCity);
				startActivityForResult(cityIntent, AppConstants.CITY_REQUEST);
			} else
				setSearchCity();
			break;

		case R.id.currentLocality:
			if (localityItems != null && localityItems.size() > 0) {
				Intent localityIntent = new Intent(CompanyDetailActivity.this, AdvanceSelectLocalityActivity.class);
				localityIntent.putExtra("LOCALITY_LIST", localityItems);
				localityIntent.putStringArrayListExtra("LOCALITY_INDEX", selectedLocalityindex);
				startActivityForResult(localityIntent, AppConstants.LOCALITY_REQUEST);
			} else 
				setSearchLocality(city_id);
			break;

		case R.id.img_video_thumbnail:
			if (isDialogToBeShown()) 
				showConfirmationDialog(CustomDialog.PLAY_VIDEO_DIALOG, getResources().getString(R.string.cd_msg_data_usage));
			else
				playVideo();
			break;
		}
	}

	private void handleAddFavourites() {

		JSONObject postJson = null;
		try {
			postJson = validateData();
		} catch (JSONException e) {
			showAlertDialog(getResources().getString(R.string.internal_error));
			AnalyticsHelper.onError(FlurryEventsConstants.DATA_VALIDATION_ERR, "CompanyDetailActivity : " + AppConstants.DATA_VALIDATION_ERROR_MSG, e);
		}
		if (postJson == null) 
			return;

		CompanyDetailAddFavController addFavController = new CompanyDetailAddFavController(CompanyDetailActivity.this, Events.COMPANY_DETAIL_ADD_FAV);
		startSppiner();
		addFavController.requestService(postJson);
	}

	private void handleRemoveFavourites() {

		JSONObject postJson = null;
		try {
			postJson = verifyInput();
		} catch (JSONException e) {
			showAlertDialog(getResources().getString(R.string.internal_error));
			AnalyticsHelper.onError(FlurryEventsConstants.DATA_VALIDATION_ERR, "CompanyDetailActivity : " + AppConstants.DATA_VALIDATION_ERROR_MSG, e);
		}
		if (postJson == null) 
			return;

		CompanyDetailRemoveFavController removeFavController = new CompanyDetailRemoveFavController(CompanyDetailActivity.this, Events.COMPANY_DETAIL_REMOVE_FAV);
		startSppiner();
		removeFavController.fromComapnyList = true;
		removeFavController.requestService(postJson);
	}

	private JSONObject validateData() throws JSONException {

		String userId = null;
		JSONObject jArray = new JSONObject();
		if (store.isLoogedInUser()) 
			userId = store.getUserID();
		if (StringUtil.isNullOrEmpty(userId)) {
			showConfirmationDialog(CustomDialog.LOGIN_CONFIRMATION_DIALOG, getString(R.string.cd_msg_add_to_fav));
			stopSppiner();
			return null;
		} else 
			jArray.put("uid", userId);
		String compId = mCompanyDetail.getId();
		String categoryId = mCompanyDetail.getCatId();
		if (StringUtil.isNullOrEmpty(compId)) 
			return null;
		else 
			jArray.put("cid", compId);

		if (StringUtil.isNullOrEmpty(categoryId)) 
			return null;
		else 
			jArray.put("cat_id", categoryId);

		return jArray;

	}

	public JSONObject verifyInput() throws JSONException {

		String userId 			= null;
		JSONObject postJson 	= new JSONObject();
		if (store.isLoogedInUser()) 
			userId = store.getUserID();

		if (StringUtil.isNullOrEmpty(userId)) {
			showConfirmationDialog(CustomDialog.LOGIN_CONFIRMATION_DIALOG, getString(R.string.cd_msg_remove_from_fav));
			stopSppiner();
			return null;
		} else 
			postJson.put("user_id", userId);

		JSONArray companyCategoryArr 	= new JSONArray();
		JSONObject comIdCatId 			= new JSONObject();
		String compId 					= mCompanyDetail.getId();
		String categoryId 				= mCompanyDetail.getCatId();
		if (StringUtil.isNullOrEmpty(compId)) 
			return null;
		else 
			comIdCatId.put("company_id", compId);

		if (StringUtil.isNullOrEmpty(categoryId)) 
			return null;
		else 
			comIdCatId.put("category_id", categoryId);

		companyCategoryArr.put(comIdCatId);
		postJson.put("company_category", companyCategoryArr);
		return postJson;

	}

	private void sendEmail() {
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", (mCompanyDetail.getMailId() != null) ? mCompanyDetail.getMailId() : "", null));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, mCompanyDetail.getTitle());
		startActivity(Intent.createChooser(emailIntent, "Send Email"));
	}

	private void checkPreferenceAndMakeCall(String numberToBeCalled) {
		mNumberToBeCalled = numberToBeCalled;
		NativeHelper.makeCall(CompanyDetailActivity.this, numberToBeCalled);
	}

	private void showGuestBranchingActivity() {
		Intent guestBranchIntent = new Intent(CompanyDetailActivity.this, GuestBranchingActivity.class);
		guestBranchIntent.putExtra(AppConstants.IS_FROM_COMP_DETAIL_ADD_FAV, true);
		startActivity(guestBranchIntent);
	}

	private void showMapActivity() {
		if (isLocationAvailable()) {
			String url = "http://maps.google.com/maps?saddr="
					+ GPS_Data.getLatitude() + "," + GPS_Data.getLongitude()
					+ "&daddr=" + mCompanyDetail.getLatitude() + ","
					+ mCompanyDetail.getLongitude() + "&mode=driving";
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
			intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
			startActivity(intent);
		}
	}

	private void pointMeThere() {
		if (!isSensorAvailable()) {
			showDialogWithTitle(getResources().getString(R.string.disclaimer), getResources().getString(R.string.cd_error_sensor_not_found));
			return;
		} else if (!isLocationAvailable()) 
			return;

		Intent intent = new Intent(this, CompassDirectionActivity.class);
		intent.putExtra(AppConstants.COMP_DETAIL_DATA, mCompanyDetail);
		intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
		startActivity(intent);
	}

	@SuppressWarnings("deprecation")
	private boolean isSensorAvailable() {
		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		if (sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) != null)
			return true;
		else
			return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == AppConstants.AR_REPORT_ERROR_SUCCESS) {
			showInfoDialog(getResources()
					.getString(R.string.are_error_reported));
		} else if (resultCode == AppConstants.AR_REPORT_ERROR_FAILURE) {
			showInfoDialog(getResources().getString(R.string.are_error_occured));
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
		} else if (id == CustomDialog.DELETE_CONFIRMATION_DIALOG) {
			handleRemoveFavourites();
		} else if (id == CustomDialog.DATA_USAGE_DIALOG) {
			showMapActivity();
		} else if (id == CustomDialog.DATA_USAGE_DIALOG_FOR_EMAIL) {
			sendEmail();
		} else if (id == CustomDialog.DATA_USAGE_DIALOG_FOR_CALL) {
			NativeHelper
			.makeCall(CompanyDetailActivity.this, mNumberToBeCalled);
		} else if (id == CustomDialog.PLAY_VIDEO_DIALOG) {
			playVideo();
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

	private void playVideo() {
		Intent videoIntent = new Intent(CompanyDetailActivity.this, VideoPlayActivity.class);
		videoIntent.putExtra(AppConstants.VIDEO_URL, mCompanyDetail.getVideoUrl());
		startActivity(videoIntent);
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

	public void viewFlipperTapped() {
		Intent intents = new Intent(CompanyDetailActivity.this,PhotoSlideActivity.class);
		intents.putParcelableArrayListExtra("list", imgPathList);
		intents.putExtra("position", flipperVisibleItemPosition);
		startActivity(intents);
	}
	
	private void setPaidCompanyListData() {
		mPaidCompListAdapter = 	new PaidCompanyListAdapter(CompanyDetailActivity.this);
		mPaidCompListAdapter.setData(paidCompanyListData);
		mPaidCompListAdapter.notifyDataSetChanged();
		mPaidCompanyListView.setAdapter(mPaidCompListAdapter);
		mPaidCompanyListContainer.setVisibility(View.VISIBLE);
	}

}