package com.kelltontech.maxisgetit.ui.activities.matta;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.constants.matta.MattaConstants;
import com.kelltontech.maxisgetit.constants.matta.MattaEvents;
import com.kelltontech.maxisgetit.controllers.matta.MattaBoothListController;
import com.kelltontech.maxisgetit.controllers.matta.MattaPackageListController;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.SelectorDAO;
import com.kelltontech.maxisgetit.model.matta.booths.list.MattaBoothListResponse;
import com.kelltontech.maxisgetit.model.matta.packages.list.MattaPackageListResponse;
import com.kelltontech.maxisgetit.requests.matta.MattaBoothListRequest;
import com.kelltontech.maxisgetit.requests.matta.MattaPackageListRequest;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.response.RefineSelectorResponse;
import com.kelltontech.maxisgetit.ui.activities.AdvanceSelectCity;
import com.kelltontech.maxisgetit.ui.activities.AdvanceSelectLocalityActivity;
import com.kelltontech.maxisgetit.ui.activities.MaxisMainActivity;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class MattaFilterSearchActivity extends MaxisMainActivity {
	public static final String SELECTOR_MODE = "SELECTOR_MODE";
	public static final int ATTR_SELECTION_BY_SEARCH = 1;
	public static final int ATTR_SELECTION = 2;
	private int selectorMode = ATTR_SELECTION_BY_SEARCH;
	private Spinner mLocalitySelSpinner;
	private LinearLayout mSpinnerHolder, mLocalitySpinnerContainer;
	private ImageView mSearchBtn;
	private ArrayList<SelectorDAO> mSelectors;
	private EditText mSearchEditText;
	private LinearLayout mCategorySpinnerConatainer;
	private ImageView mProfileIconView;
	private TextView mRefineBtn;
	private ArrayList<Spinner> mSpinnerList = new ArrayList<Spinner>();
	private RefineSelectorResponse mSelctorResp;
	private MattaBoothListRequest mMattaBoothListRequest;
	private MattaPackageListRequest mMattaPackageListRequest;
	private SelectorDAO mLocalitySelectorDao;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private TextView mHeaderTitle;
	private ImageView mHeaderBackButton;
	private ImageView mHomeIconView;

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
	String categoryId = "";

	private String citySelected = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_matta_filter_search);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.ms_root_layout), this);
		ImageLoader.initialize(MattaFilterSearchActivity.this);
		mCategorySpinnerConatainer = (LinearLayout) findViewById(R.id.category_spinner_container);
		mLocalitySpinnerContainer = (LinearLayout) findViewById(R.id.ms_locality_chooser_container);
		mLocalitySelSpinner = (Spinner) findViewById(R.id.ms_locality_chooser);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(MattaFilterSearchActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mRefineBtn = (TextView) findViewById(R.id.ms_refine_btn);
		mRefineBtn.setOnClickListener(this);
		mSpinnerHolder = (LinearLayout) findViewById(R.id.ms_spinner_holder);
		mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler = (ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);

		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);

		mHeaderTitle = (TextView) findViewById(R.id.header_title);

		Bundle bundle = getIntent().getExtras();
		selectorMode = bundle.getInt(MattaFilterSearchActivity.SELECTOR_MODE);

		if (selectorMode == ATTR_SELECTION_BY_SEARCH) {
			mMattaBoothListRequest = (MattaBoothListRequest) bundle.getSerializable(MattaConstants.DATA_MATTA_BOOTH_LIST_REQUEST);
			mMattaBoothListRequest.setPageNumber(1);
			mHeaderTitle.setText(mMattaBoothListRequest.getmHallTitle());
			mCategorySpinnerConatainer.setVisibility(View.GONE);
			mSelctorResp = (RefineSelectorResponse) bundle.get(AppConstants.REFINE_ATTR_RESPONSE);
			if (mSelctorResp != null)
				showFilters();
		} else if (selectorMode == ATTR_SELECTION) {
			mMattaPackageListRequest = (MattaPackageListRequest) bundle.getSerializable(MattaConstants.DATA_MATTA_PACKAGE_LIST_REQUEST);
			mMattaPackageListRequest.setPageNumber(1);
			mHeaderTitle.setText(Html.fromHtml("Tour & Travel Packages"));
			if (mMattaPackageListRequest != null && !StringUtil.isNullOrEmpty(mMattaPackageListRequest.getPostJsonPayload())) {
				try {
					JSONObject jsonObject = new JSONObject(mMattaPackageListRequest.getPostJsonPayload());
					JSONObject jObject = jsonObject.getJSONObject("selector");
					JSONArray jArray = jObject.getJSONArray("field_1");
					citySelected = jArray.getString(0);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			mCategorySpinnerConatainer.setVisibility(View.GONE);
			mSelctorResp = (RefineSelectorResponse) bundle.get(AppConstants.REFINE_ATTR_RESPONSE);
			if (mSelctorResp != null)
				showFilters();
		}

		advanceSearchLayout = (LinearLayout) findViewById(R.id.advanceSearch);
		advanceSearchLayout.setVisibility(View.GONE);

		upArrow = (ImageView) findViewById(R.id.upArrow);
		upArrow.setOnClickListener(this);

		currentCity = (TextView) findViewById(R.id.currentCity);
		currentLocality = (TextView) findViewById(R.id.currentLocality);
		currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCity + "</b>"));

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

	@Override
	protected void onResume() {
		super.onResume();
		AnalyticsHelper.trackSession(MattaFilterSearchActivity.this, MattaConstants.Matta_Filter_Search);
	}

	private void showFilters() {
		mSpinnerHolder.removeAllViews();
		mSpinnerHolder.setVisibility(View.VISIBLE);
		mSpinnerList = new ArrayList<Spinner>();
		mSelectors = mSelctorResp.getSelectors();
		if (mSelectors != null) {
			for (int i = 0; i < mSelectors.size(); i++) {
				SelectorDAO selector = mSelectors.get(i);
				Spinner filterSpinner = inflateFilter(selector);
				mSpinnerList.add(filterSpinner);
				if (selector.getSearchKey().equalsIgnoreCase(AppConstants.KEYWORD_DESTINATION_CITY)) {
					for (int j = 0; j < selector.getSelectorValues().size(); j++) {
						if(selector.getSelectorValues().get(j).equalsIgnoreCase(citySelected)) {
							filterSpinner.setSelection(j);
							break;
						}
					}
				}
			}
		}
	}

	private Spinner inflateFilter(SelectorDAO selector) {
		ArrayList<String> values = selector.getSelectorValues();
		String text = selector.getDisplayName();
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout spinnerContainer = (LinearLayout) inflater.inflate(R.layout.refine_search_spinner_layout, null);
		TextView v = (TextView) spinnerContainer.findViewById(R.id.spin_txt);
		v.setText(text);
		Spinner spinner = (Spinner) spinnerContainer.findViewById(R.id.spin_spin);
		ArrayAdapter<String> madapter = new ArrayAdapter<String>(this,R.layout.spinner_item, values);
		spinner.setAdapter(madapter);
		spinner.setSelection(selector.getSelectedIndex());
		mSpinnerHolder.addView(spinnerContainer);
		spinner.setTag(selector);
		return spinner;
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE || event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.REFINE_ATTRIBUTES) {
			Message catRefine = (Message) screenData;
			handler.sendMessage(catRefine);
		} else if (event == MattaEvents.MATTA_BOOTH_LIST_EVENT) {
			MattaBoothListResponse boothListRes = (MattaBoothListResponse) screenData;
			Message message = new Message();
			message.arg2 = event;
			if ((boothListRes.getResults() != null) && (!StringUtil.isNullOrEmpty(boothListRes.getResults().getError_Code())) && (boothListRes.getResults().getError_Code().equals("0"))) {
				if (boothListRes.getResults().getBooth().size() < 1 
						|| StringUtil.isNullOrEmpty(boothListRes.getResults().getTotalRecordsFound()) 
						|| boothListRes.getResults().getTotalRecordsFound().equals("0")) {
					message.arg1 = 1;
					message.obj = new String("No Result Found");
				} else {
					message.arg1 = 0;
					message.obj = boothListRes;
				}
			} else {
				message.arg1 = 1;
				message.obj = getResources().getString(R.string.communication_failure);
			}
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
		} else if (event == Events.CITY_LISTING
				|| event == Events.LOCALITY_LISTING) {
			Message message = (Message) screenData;
			handler.sendMessage(message);
		}
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.REFINE_ATTRIBUTES) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				mSelctorResp = (RefineSelectorResponse) msg.obj;
				if (mSelctorResp != null)
					showFilters();
			}
			stopSppiner();
		} else if (msg.arg2 == MattaEvents.MATTA_BOOTH_LIST_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				MattaBoothListResponse boothRes = (MattaBoothListResponse) msg.obj;
				Intent intent = new Intent();
				intent.putExtra(MattaConstants.DATA_MATTA_BOOTH_LIST_RESPONSE, boothRes);
				intent.putExtra(MattaConstants.DATA_MATTA_BOOTH_LIST_REQUEST, mMattaBoothListRequest);
				intent.putExtra(AppConstants.REFINE_ATTR_RESPONSE, mSelctorResp);
				setResult(RESULT_OK, intent);
				finish();
			}
			stopSppiner();
		} else if (msg.arg2 == MattaEvents.MATTA_PACKAGE_LIST_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				MattaPackageListResponse packageResponse = (MattaPackageListResponse) msg.obj;
				Intent intent = new Intent();
				intent.putExtra(MattaConstants.DATA_MATTA_PACKAGE_LIST_RESPONSE, packageResponse);
				intent.putExtra(MattaConstants.DATA_MATTA_PACKAGE_LIST_REQUEST, mMattaPackageListRequest);
				intent.putExtra(AppConstants.REFINE_ATTR_RESPONSE, mSelctorResp);
				setResult(RESULT_OK, intent);
				finish();
			}
			stopSppiner();
		} else if (msg.arg2 == Events.CITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				cityList = glistRes.getCityOrLocalityList();
				Intent intent = new Intent(MattaFilterSearchActivity.this,
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
				Intent intent = new Intent(MattaFilterSearchActivity.this,
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

	private JSONObject verifyAndGetSelectorsJson() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonSelector = new JSONObject();
		if (mLocalitySpinnerContainer.getVisibility() == View.VISIBLE
				&& mLocalitySelSpinner.getCount() > 0
				&& mLocalitySelSpinner.getSelectedItemPosition() > 0) {
			mLocalitySelectorDao.setSelectedIndex(mLocalitySelSpinner.getSelectedItemPosition());
			jsonSelector.put("locality_name", mLocalitySelSpinner.getItemAtPosition(mLocalitySelSpinner.getSelectedItemPosition()));
		}

		for (int i = 0; i < mSpinnerList.size(); i++) {
			SelectorDAO selector = (SelectorDAO) mSpinnerList.get(i).getTag();
			selector.setSelectedIndex(mSpinnerList.get(i)
					.getSelectedItemPosition());
			if (selector.getSelectedIndex() != 0) {
				if (selector.getSearchKey().equalsIgnoreCase(
						AppConstants.KEYWORD_CITY_OF_REFINE)) {
					jsonObject.put(
							selector.getSearchKey(),
							selector.getSelectorValues().get(
									selector.getSelectedIndex()));
				} else {
					JSONArray jarray = new JSONArray();
					jarray.put(selector.getSelectorValues().get(
							selector.getSelectedIndex()));
					jsonSelector.put(selector.getSearchKey(), jarray);
				}
			}
		}
		jsonObject.put("selector", jsonSelector);
		System.out.println(mSelctorResp);
		System.out.println(jsonObject);
		return jsonObject;
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
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());

			String JSON_EXTRA = jsonForSearch();
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
			break;
		case R.id.show_profile_icon:
			onProfileClick();
			break;
		case R.id.ms_refine_btn:
			try {
				JSONObject postData = verifyAndGetSelectorsJson();
				if (postData != null) {
					if (selectorMode == ATTR_SELECTION_BY_SEARCH) {
						mMattaBoothListRequest.setPostJsonPayload(postData.toString());
						mMattaBoothListRequest.setSearchRefined(true);
						MattaBoothListController controller = new MattaBoothListController(MattaFilterSearchActivity.this, MattaEvents.MATTA_BOOTH_LIST_EVENT);
						startSppiner();
						controller.requestService(mMattaBoothListRequest);
					} else if (selectorMode == ATTR_SELECTION) {
						mMattaPackageListRequest.setPostJsonPayload(postData.toString());
						mMattaPackageListRequest.setSearchRefined(true);
						MattaPackageListController controller = new MattaPackageListController(MattaFilterSearchActivity.this, MattaEvents.MATTA_PACKAGE_LIST_EVENT);
						startSppiner();
						controller.requestService(mMattaPackageListRequest);
					}
				}
			} catch (JSONException e) {
				AnalyticsHelper.onError(FlurryEventsConstants.DATA_VALIDATION_ERR,"RefineSearchActivity : " + AppConstants.DATA_VALIDATION_ERROR_MSG, e);
			}
			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			showHomeScreen();
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
				Intent cityIntent = new Intent(MattaFilterSearchActivity.this,
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
				Intent localityIntent = new Intent(MattaFilterSearchActivity.this,
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
		default:
			break;
		}
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

		} else if (resultCode == RESULT_OK	&& requestCode == AppConstants.LOCALITY_REQUEST) {
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
}