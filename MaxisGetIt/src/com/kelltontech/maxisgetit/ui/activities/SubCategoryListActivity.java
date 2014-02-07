package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.SubCategoryListAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.CityAreaListController;
import com.kelltontech.maxisgetit.controllers.CombindListingController;
import com.kelltontech.maxisgetit.controllers.PostDealCityLocalityListController;
import com.kelltontech.maxisgetit.controllers.SubCategoryController;
import com.kelltontech.maxisgetit.controllers.TypeByCategoryController;
import com.kelltontech.maxisgetit.dao.CategoryGroup;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.dao.PostDealCityOrLoc;
import com.kelltontech.maxisgetit.dao.SubCategory;
import com.kelltontech.maxisgetit.db.CityTable;
import com.kelltontech.maxisgetit.requests.CombinedListRequest;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.response.PostDealCityLocListResponse;
import com.kelltontech.maxisgetit.response.SubCategoryResponse;
import com.kelltontech.maxisgetit.response.TypeByCategoryResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class SubCategoryListActivity extends MaxisMainActivity {
	private static final String GROUP_TYPE_LISTING = "Listing";
	private static final String GROUP_TYPE_SALE = "Sale";
	private static final int ACTION_LISTING = 0;
	private static final int ACTION_SALE = 1;
	private static final int ACTION_DEAL = 2;
	private int mActionType;
	private ListView mGridView;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private TextView mHeaderTitle;
	// private ImageView mParentImage;
	private TextView mParentTitle;
	private SubCategory mSelectdCategory;
	private SubCategoryListAdapter mSubCatAdapter;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private SubCategoryResponse mSubcatResponse;
	private ImageView mProfileIconView;
	private ImageView mHeaderBackButton;
	private ImageView mHomeIconView;
	private ImageView mFacebookIconView;
	private ImageView mTwitterIconView;
	private ImageView mLogo;

	private boolean isAdvanceSearchLayoutOpen = false;
	private LinearLayout advanceSearchLayout;
	private TextView currentCity, currentLocality;
	private ImageView upArrow;
	private ArrayList<String> cityListString = new ArrayList<String>();
	private ArrayList<String> localityItems;
	ArrayList<CityOrLocality> cityList;
	private String selectedCity = "Entire Malasyia";
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
		setContentView(R.layout.activity_subcategory);
		AnalyticsHelper.logEvent(FlurryEventsConstants.APPLICATION_CATEGORY);
		Bundle bundle = getIntent().getExtras();
		mSubcatResponse = bundle
				.getParcelable(AppConstants.DATA_SUBCAT_RESPONSE);
		CategoryGroup parCategory = mSubcatResponse.getParentCategory();
		UiUtils.hideKeyboardOnTappingOutside(
				findViewById(R.id.subcat_root_layout), this);
		ImageLoader.initialize(SubCategoryListActivity.this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(SubCategoryListActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler = (ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);

		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);

		mLogo = (ImageView) findViewById(R.id.logo);
		mLogo.setOnClickListener(SubCategoryListActivity.this);

		mHeaderTitle = (TextView) findViewById(R.id.header_title);
		mHeaderTitle.setText(parCategory.getCategoryTitle());

		mFacebookIconView = (ImageView) findViewById(R.id.footer_facebook_icon);
		mFacebookIconView.setOnClickListener(this);
		mTwitterIconView = (ImageView) findViewById(R.id.footer_twitterIcon);
		mTwitterIconView.setOnClickListener(this);
		// mParentImage = (ImageView) findViewById(R.id.parent_icon_view);

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
		
		wholeSearchBoxContainer = (LinearLayout)findViewById(R.id.whole_search_box_container);

		mParentTitle = (TextView) findViewById(R.id.parent_title_field);
		mGridView = (ListView) findViewById(R.id.grid_list);
		mSubCatAdapter = new SubCategoryListAdapter(
				SubCategoryListActivity.this);
		if (parCategory.getMgroupType().equalsIgnoreCase(GROUP_TYPE_SALE)) {
			mActionType = ACTION_SALE;
		} else if (parCategory.getMgroupType().equalsIgnoreCase(
				GROUP_TYPE_LISTING)) {
			mActionType = ACTION_LISTING;
		} else {
			mActionType = ACTION_DEAL;
		}
		mParentTitle.setText(mSubcatResponse.getCategories().size()
				+ " Categories Found For " + parCategory.getCategoryTitle());
		// ImageLoader.start(parCategory.getThumbUrl(), mParentImage,
		// mHeaderDummyDrawable, mHeaderErrorDrawable);
		mSubCatAdapter.setData(mSubcatResponse.getCategories());
		mGridView.setAdapter(mSubCatAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1,
					int position, long arg3) {
				gotoScreen(adapter, position);
				/*
				 * mSelectdCategory = (SubCategory)
				 * adapter.getItemAtPosition(position); if (mActionType ==
				 * ACTION_LISTING)
				 * showCompanyDealListing(mSelectdCategory.getCategoryId(),
				 * mSelectdCategory.getCategoryTitle(),
				 * mSelectdCategory.getThumbUrl(), true); else
				 * if(mActionType==ACTION_SALE){
				 * showTempletScreen(mSelectdCategory.getCategoryId()); }else {
				 * showDealListing(); }
				 */
			}
		});

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

	protected void gotoScreen(AdapterView<?> adapter, int position) {
		mSelectdCategory = (SubCategory) adapter.getItemAtPosition(position);
		if (!StringUtil.isNullOrEmpty(mSelectdCategory.getCategoryTitle()
				.trim())
				&& !StringUtil.isNullOrEmpty(mSelectdCategory.getCategoryId()
						.trim())) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(FlurryEventsConstants.Sub_Category_Title, mSelectdCategory
					.getCategoryTitle().trim());
			map.put(FlurryEventsConstants.Sub_Category_Id, mSelectdCategory
					.getCategoryId().trim());
			AnalyticsHelper.logEvent(FlurryEventsConstants.SUB_CATEGORY, map);
		}
		/*
		 * if (cat.getGroupType().trim().equalsIgnoreCase(AppConstants.
		 * GROUP_ACTION_TYPE_DEAL)) showDealListing(cat); else
		 * showSubcategories(cat);
		 */
		if (mSelectdCategory.getMgroupType().trim()
				.equalsIgnoreCase(AppConstants.GROUP_TYPE_CATEGORY)) {
			if (mSelectdCategory.getmGroupActionType().trim()
					.equalsIgnoreCase(AppConstants.GROUP_ACTION_TYPE_DEAL)
					|| mSelectdCategory
							.getmGroupActionType()
							.trim()
							.equalsIgnoreCase(
									AppConstants.GROUP_ACTION_TYPE_LIST)) {
				// showDealListing(mSelectdCategory);
				showSubcategories(mSelectdCategory);
			} else if (mSelectdCategory
					.getmGroupActionType()
					.trim()
					.equalsIgnoreCase(
							AppConstants.GROUP_ACTION_TYPE_CATEGORY_LIST_FOR_GROUP)) {
				showCompanyDealListing(mSelectdCategory.getCategoryId(),
						mSelectdCategory.getCategoryTitle(),
						mSelectdCategory.getThumbUrl(), true,
						mSelectdCategory.getMgroupType(),
						mSelectdCategory.getmGroupActionType());
			}
			/*
			 * else
			 * if(mSelectdCategory.getmGroupActionType().trim().equalsIgnoreCase
			 * (AppConstants.GROUP_ACTION_TYPE_LIST)){
			 * showSubcategories(mSelectdCategory); }
			 */

		} else {
			if (mSelectdCategory.getmGroupActionType().trim()
					.equalsIgnoreCase(AppConstants.GROUP_ACTION_TYPE_DEAL)) {
				showDealListing(mSelectdCategory);
			} else if (mSelectdCategory.getmGroupActionType().trim()
					.equalsIgnoreCase(AppConstants.GROUP_ACTION_TYPE_LIST)) {
				showCompanyDealListing(mSelectdCategory.getCategoryId(),
						mSelectdCategory.getCategoryTitle(),
						mSelectdCategory.getThumbUrl(), true,
						mSelectdCategory.getMgroupType(),
						mSelectdCategory.getmGroupActionType());
			} else if (mSelectdCategory.getmGroupActionType().trim()
					.equalsIgnoreCase(AppConstants.GROUP_ACTION_TYPE_ATTRIBUTE)) {
				showSaleTemplateTypeScreen(mSelectdCategory.getCategoryId());
			}
		}
	}

	private void showSubcategories(SubCategory cat) {
		SubCategoryController controller = new SubCategoryController(
				SubCategoryListActivity.this, Events.SUBCATEGORY_EVENT);
		CategoryGroup categoryGroup = new CategoryGroup();
		categoryGroup.setCategoryId(cat.getCategoryId());
		categoryGroup.setCategoryTitle(cat.getCategoryTitle());
		categoryGroup.setIconUrl(cat.getIconUrl());
		categoryGroup.setmGroupActionType(cat.getmGroupActionType());
		categoryGroup.setMgroupType(cat.getMgroupType());
		categoryGroup.setThumbUrl(cat.getThumbUrl());
		controller.requestService(categoryGroup);
		startSppiner();
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.TYPE_BY_CATEGORY_EVENT
				|| event == Events.SUBCATEGORY_EVENT) {
			handler.sendMessage((Message) screenData);
		} else {
			handler.sendMessage((Message) screenData);
		}
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.SUBCATEGORY_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				SubCategoryResponse categoriesResp = (SubCategoryResponse) msg.obj;
				Intent intent = new Intent(SubCategoryListActivity.this,
						SubCategoryListActivity.class);
				intent.putExtra(AppConstants.DATA_SUBCAT_RESPONSE,
						categoriesResp);
				startActivity(intent);
			}
			stopSppiner();
		} else if (msg.arg2 == Events.TYPE_BY_CATEGORY_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				// MaxisStore store =
				// MaxisStore.getStore(SubCategoryListActivity.this);
				// if (store.isLoogedInUser()) {
				Intent intent = new Intent(SubCategoryListActivity.this,
						SaleTemplateTypeActivity.class);
				intent.putExtra(AppConstants.TYPE_BY_CATEGORY_DATA,
						(TypeByCategoryResponse) msg.obj);
				intent.putExtra(AppConstants.SELECTED_CAT_DATA,
						mSelectdCategory);
				startActivity(intent);
				/*
				 * } else { Intent branchIntent = new
				 * Intent(SubCategoryListActivity.this,
				 * GuestBranchingActivity.class);
				 * branchIntent.putExtra(AppConstants.TEMPLET_DATA,
				 * (ControlDetailResponse) msg.obj);
				 * branchIntent.putExtra(AppConstants.SELECTED_CAT_DATA,
				 * mSelectdCategory); startActivity(branchIntent); }
				 */
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
				Intent intent = new Intent(SubCategoryListActivity.this,
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
				Intent intent = new Intent(SubCategoryListActivity.this,
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_toggler:
			AnalyticsHelper.logEvent(FlurryEventsConstants.HOME_SEARCH_CLICK);
			if(wholeSearchBoxContainer.getVisibility()==View.VISIBLE)
			{
				wholeSearchBoxContainer.setVisibility(View.GONE);
			}else
			{
				wholeSearchBoxContainer.setVisibility(View.VISIBLE);
			}
			if (mSearchContainer.getVisibility() == View.VISIBLE) {
				mSearchContainer.setVisibility(View.GONE);
//				if (isAdvanceSearchLayoutOpen) {
//					isAdvanceSearchLayoutOpen = false;
//					advanceSearchLayout.setVisibility(View.GONE);
//				}
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
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA);
			break;
		case R.id.show_profile_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.SHOW_PROFILE_CLICK);
			onProfileClick();
			break;
		case R.id.subcat_image_manager:
			mSelectdCategory = (SubCategory) v.getTag();
			if (mActionType == ACTION_LISTING)
				showCompanyDealListing(mSelectdCategory.getCategoryId(),
						mSelectdCategory.getCategoryTitle(),
						mSelectdCategory.getThumbUrl(), true,
						mSelectdCategory.getMgroupType(),
						mSelectdCategory.getmGroupActionType());
			else if (mActionType == ACTION_SALE) {
				showSaleTemplateTypeScreen(mSelectdCategory.getCategoryId());
			} else {
				showDealListing();
			}
			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intentHome = new Intent(SubCategoryListActivity.this,
					HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		case R.id.footer_facebook_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.FACEBOOK_CLICK);
			checkPreferenceAndOpenBrowser(AppConstants.FB_PAGE_URL);
			break;
		case R.id.footer_twitterIcon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.TWITTER_CLICK);
			checkPreferenceAndOpenBrowser(AppConstants.TWITTER_PAGE_URL);
			break;

		case R.id.logo:
			startActivity(new Intent(SubCategoryListActivity.this,
					GetItInfoActivity.class));
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
				selectedLocalityindex =null;
				Intent cityIntent = new Intent(SubCategoryListActivity.this,
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
				Intent localityIntent = new Intent(
						SubCategoryListActivity.this,
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

	private void showDealListing(SubCategory cat) {
		CombindListingController listingController = new CombindListingController(
				SubCategoryListActivity.this,
				Events.COMBIND_LISTING_NEW_LISTING_PAGE);
		CombinedListRequest newListReq = new CombinedListRequest(
				SubCategoryListActivity.this);
		newListReq.setBySearch(false);
		newListReq.setCompanyListing(false);
		// newListReq.setKeywordOrCategoryId("-1");
		newListReq.setKeywordOrCategoryId(cat.getCategoryId());
		newListReq.setLatitude(GPS_Data.getLatitude());
		newListReq.setLongitude(GPS_Data.getLongitude());
		newListReq.setCategoryTitle(cat.getCategoryTitle());
		newListReq.setParentThumbUrl(cat.getThumbUrl());
		newListReq.setGroupActionType(cat.getmGroupActionType());
		newListReq.setGroupType(cat.getMgroupType());
		setRequest(newListReq);
		startSppiner();
		listingController.requestService(newListReq);

	}

	private void showDealListing() {
		CombindListingController listingController = new CombindListingController(
				SubCategoryListActivity.this,
				Events.COMBIND_LISTING_NEW_LISTING_PAGE);
		CombinedListRequest newListReq = new CombinedListRequest(
				SubCategoryListActivity.this);
		newListReq.setBySearch(true);
		newListReq.setCompanyListing(false);
		newListReq.setKeywordOrCategoryId("-1");
		newListReq.setLatitude(GPS_Data.getLatitude());
		newListReq.setLongitude(GPS_Data.getLongitude());
		setRequest(newListReq);
		startSppiner();
		listingController.requestService(newListReq);
	}

	/*
	 * protected void showTempletScreen(String categoryId) { TempletController
	 * tempcController = new TempletController(SubCategoryListActivity.this,
	 * Events.TEMPLET_LISTING); startSppiner();
	 * tempcController.requestService(categoryId); }
	 */

	protected void showSaleTemplateTypeScreen(String categoryId) {
		TypeByCategoryController tBCController = new TypeByCategoryController(
				SubCategoryListActivity.this, Events.TYPE_BY_CATEGORY_EVENT);
		startSppiner();
		tBCController.requestService(categoryId);
	}

	protected void showSubcategories(CategoryGroup cat) {
		SubCategoryController controller = new SubCategoryController(
				SubCategoryListActivity.this, Events.SUBCATEGORY_EVENT);
		controller.requestService(cat);
		startSppiner();
	}

	@Override
	protected void onDestroy() {
		ImageLoader.clearCache();
		super.onDestroy();
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