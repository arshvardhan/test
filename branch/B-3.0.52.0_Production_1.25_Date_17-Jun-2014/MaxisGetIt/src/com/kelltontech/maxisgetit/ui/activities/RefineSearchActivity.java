package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.CombindListingController;
import com.kelltontech.maxisgetit.controllers.RefineAttributeController;
import com.kelltontech.maxisgetit.dao.CategoryRefine;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.SelectorDAO;
import com.kelltontech.maxisgetit.db.CityTable;
import com.kelltontech.maxisgetit.requests.CombinedListRequest;
import com.kelltontech.maxisgetit.requests.RefineSearchRequest;
import com.kelltontech.maxisgetit.response.CompanyListResponse;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.response.RefineCategoryResponse;
import com.kelltontech.maxisgetit.response.RefineSelectorResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class RefineSearchActivity extends MaxisMainActivity {
	public static final String SELECTOR_MODE = "SELECTOR_MODE";
	public static final int CAT_SELECTION = 1;
	public static final int ATTR_SELECTION_BY_SEARCH = 4;
	public static final int ATTR_SELECTION = 2;
	public static final int CAT_ATTR_SELECTION_MODI = 3;
	private int selectorMode = CAT_SELECTION;
	private Spinner mCatSelector, mCitySelSpinner, mLocalitySelSpinner;
	private LinearLayout mSpinnerHolder, mLocalitySpinnerContainer;
	private ImageView mSearchBtn;
	private ArrayList<SelectorDAO> mSelectors;
	private EditText mSearchEditText;
	private LinearLayout mCategorySpinnerConatainer;
	private ImageView mProfileIconView;
	private String mCategoryThumbUrl;
	private TextView mRefineBtn;
	private Drawable mThumbLoading;
	private Drawable mThumbError;
	private ArrayList<Spinner> mSpinnerList = new ArrayList<Spinner>();
	private RefineSelectorResponse mSelctorResp;
	private RefineCategoryResponse mCatResponse;
	private CombinedListRequest mClRequest;
	private CompanyListResponse mClResponse;
	private SelectorDAO mLocalitySelectorDao;
	private int defaultCitySelection = 0;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_search);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.ms_root_layout),
				this);
		ImageLoader.initialize(RefineSearchActivity.this);
		mCategorySpinnerConatainer = (LinearLayout) findViewById(R.id.category_spinner_container);
		mLocalitySpinnerContainer = (LinearLayout) findViewById(R.id.ms_locality_chooser_container);
		mLocalitySelSpinner = (Spinner) findViewById(R.id.ms_locality_chooser);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(RefineSearchActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mRefineBtn = (TextView) findViewById(R.id.ms_refine_btn);
		mRefineBtn.setOnClickListener(this);
		mSpinnerHolder = (LinearLayout) findViewById(R.id.ms_spinner_holder);
		Bundle bundle = getIntent().getExtras();
		mCategoryThumbUrl = bundle.getString(AppConstants.THUMB_URL);
		selectorMode = bundle.getInt(RefineSearchActivity.SELECTOR_MODE);
		mClRequest = bundle.getParcelable(AppConstants.DATA_LIST_REQUEST);
		mLocalitySelectorDao = bundle
				.getParcelable(AppConstants.LOCALITY_DAO_DATA);
		
		categoryId = bundle.getString("categoryId");
		mClRequest.setPageNumber(1);
		mCatResponse = (RefineCategoryResponse) bundle
				.get(AppConstants.REFINE_CAT_RESPONSE);
		mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler = (ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);

		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);

		mHeaderTitle = (TextView) findViewById(R.id.header_title);
		mHeaderTitle.setText(mClRequest.getCategoryTitle());
		if (mClRequest.isBySearch()) {
			mSearchEditText.setText(mClRequest.getKeywordOrCategoryId());
			mHeaderTitle.setText(mClRequest.getKeywordOrCategoryId());
		}
		if (selectorMode == ATTR_SELECTION) {
			loadCatThumb();
			mSelctorResp = (RefineSelectorResponse) bundle
					.get(AppConstants.REFINE_ATTR_RESPONSE);
			if (mSelctorResp != null)
				showFilters();
		} else if (selectorMode == ATTR_SELECTION_BY_SEARCH) {
			mCategorySpinnerConatainer.setVisibility(View.GONE);
			TextView catText = (TextView) findViewById(R.id.ms_search_by_category_name_txt);
			catText.setVisibility(View.VISIBLE);
			catText.setText("Filter the search by specifying the attributes of Category :- '"
					+ mClRequest.getSelectedCategoryNameBySearch() + "'");
			mSelctorResp = (RefineSelectorResponse) bundle
					.get(AppConstants.REFINE_ATTR_RESPONSE);
			if (mSelctorResp != null)
				showFilters();
		} else if (selectorMode == CAT_SELECTION) {
			showCategorySpinner();
		} else if (selectorMode == CAT_ATTR_SELECTION_MODI) {
			if (mCatResponse == null) {
				loadCatThumb();
			} else {
				showCategorySpinner();
			}
			mSelctorResp = (RefineSelectorResponse) bundle
					.get(AppConstants.REFINE_ATTR_RESPONSE);
			if (mSelctorResp != null)
				showFilters();
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
	protected void onResume() {
		super.onResume();
		AnalyticsHelper.trackSession(RefineSearchActivity.this, AppConstants.Modify_screen);
	}
	
	private void showLocalitySpinner() {
		if (mLocalitySelectorDao != null
				&& mLocalitySelectorDao.getSelectorValues().size() > 1) {
			ArrayAdapter<String> localityAdp = new ArrayAdapter<String>(
					RefineSearchActivity.this, R.layout.spinner_item,
					mLocalitySelectorDao.getSelectorValues());
			mLocalitySelSpinner.setAdapter(localityAdp);
			mLocalitySpinnerContainer.setVisibility(View.VISIBLE);
			if (mLocalitySelectorDao.getSelectedIndex() > 0)
				mLocalitySelSpinner.setSelection(mLocalitySelectorDao
						.getSelectedIndex());
		} else {
			mLocalitySpinnerContainer.setVisibility(View.GONE);
		}
	}

	private void loadCatThumb() {
		mCategorySpinnerConatainer.setVisibility(View.GONE);
		mThumbLoading = getResources().getDrawable(R.drawable.group_load);
		mThumbError = getResources().getDrawable(R.drawable.group_cross);
	}

	private void showCategorySpinner() {
		if (mCatResponse == null)
			return;
		ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_item,
				mCatResponse.getCategories());
		mCatSelector = (Spinner) findViewById(R.id.ms_spinner1);
		mCatSelector.setAdapter(adapter);
		mCatSelector.setSelection(mCatResponse.getSelectedCategoryIndex());
		mCatSelector.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position == 0) {
					mSpinnerHolder.setVisibility(View.GONE);
				} else {
					{
						startSppiner();
						RefineAttributeController refineController = new RefineAttributeController(
								RefineSearchActivity.this,
								Events.REFINE_ATTRIBUTES);
						CategoryRefine cat = (CategoryRefine) arg0
								.getSelectedItem();
						RefineSearchRequest refineSearchRequest = new RefineSearchRequest();
						refineSearchRequest.setCategoryId(cat.getCategoryId());
						refineSearchRequest.setDeal(!mClRequest
								.isCompanyListing());
						refineController.requestService(refineSearchRequest);
						mCatResponse.setSelectedCategoryIndex(position);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				showInfoDialog("nothing selected");
			}
		});
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
				if (selector.getSearchKey().equalsIgnoreCase(
						AppConstants.KEYWORD_CITY_OF_REFINE)) {
					handleCityOperations(filterSpinner);
					defaultCitySelection = selector.getSelectedIndex();
					showLocalitySpinner();
				}
			}
		}

	}

	private void handleCityOperations(Spinner citySpinner) {
		mCitySelSpinner = citySpinner;
		mCitySelSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position == 0) {
					mLocalitySpinnerContainer.setVisibility(View.GONE);
				} else {
					if (defaultCitySelection == position)
						return;
					defaultCitySelection = position;
					RefineAttributeController localityController = new RefineAttributeController(
							RefineSearchActivity.this,
							Events.REFINE_SEARCH_LOCALITY);
					JSONObject jsonData;
					try {
						jsonData = verifyAndGetSelectorsJson();
					} catch (JSONException e) {
						jsonData = null;
						AnalyticsHelper
								.onError(
										FlurryEventsConstants.DATA_VALIDATION_ERR,
										"RefineSearchActivity"
												+ AppConstants.DATA_VALIDATION_ERROR_MSG,
										e);
					}
					if (jsonData != null) {
						startSppiner();
						RefineSearchRequest refineSearchRequest = new RefineSearchRequest();
						if (mClRequest.isBySearch()) {
							refineSearchRequest.setSearchKeyword(mClRequest
									.getKeywordOrCategoryId());
							refineSearchRequest.setCategoryId(mClRequest
									.getSelectedCategoryBySearch());
						} else {
							if (!StringUtil.isNullOrEmpty(categoryId)) {
								refineSearchRequest.setCategoryId(categoryId);
							} else {
								refineSearchRequest.setCategoryId(mClRequest
										.getKeywordOrCategoryId());
							}
						}
						// refineSearchRequest.setCategoryId(mClRequest.getKeywordOrCategoryId());
						refineSearchRequest.setDeal(!mClRequest
								.isCompanyListing());
						refineSearchRequest.setPostData(jsonData);
						localityController.requestService(refineSearchRequest);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private Spinner inflateFilter(SelectorDAO selector) {
		ArrayList<String> values = selector.getSelectorValues();
		String text = selector.getDisplayName();
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout spinnerContainer = (LinearLayout) inflater.inflate(
				R.layout.refine_search_spinner_layout, null);
		TextView v = (TextView) spinnerContainer.findViewById(R.id.spin_txt);
		v.setText(text);
		Spinner spinner = (Spinner) spinnerContainer
				.findViewById(R.id.spin_spin);
		ArrayAdapter<String> madapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item, values);
		spinner.setAdapter(madapter);
		spinner.setSelection(selector.getSelectedIndex());
		mSpinnerHolder.addView(spinnerContainer);
		spinner.setTag(selector);
		return spinner;
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
		} else if (event == Events.REFINE_SEARCH_RESULT) {
			Response response = (Response) screenData;
			Message message = new Message();
			message.arg2 = event;
			message.arg1 = 1;
			if (response.isError()) {
				message.obj = response.getErrorText() + " "
						+ response.getErrorCode();
			} else {
				if (response.getPayload() instanceof CompanyListResponse) {
					mClResponse = (CompanyListResponse) response.getPayload();
					if (mClResponse.getErrorCode() != 0) {
						message.obj = getResources().getString(
								R.string.communication_failure);
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
		} else if (event == Events.REFINE_SEARCH_LOCALITY) {
			handler.sendMessage((Message) screenData);
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
		} else if (msg.arg2 == Events.REFINE_SEARCH_RESULT) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				mClResponse = (CompanyListResponse) msg.obj;
				Intent intent = new Intent();
				intent.putExtra(AppConstants.COMP_LIST_DATA, mClResponse);
				intent.putExtra(AppConstants.DATA_LIST_REQUEST, mClRequest);
				intent.putExtra(AppConstants.REFINE_ATTR_RESPONSE, mSelctorResp);
				intent.putExtra(AppConstants.REFINE_CAT_RESPONSE, mCatResponse);
				intent.putExtra(AppConstants.LOCALITY_DAO_DATA, mLocalitySelectorDao);
				setResult(RESULT_OK, intent);
				finish();
			}
		} else if (msg.arg2 == Events.REFINE_SEARCH_LOCALITY) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				RefineSelectorResponse glistRes = (RefineSelectorResponse) msg.obj;
				mLocalitySelectorDao = glistRes.getSelectors().get(0);
				showLocalitySpinner();
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
				Intent intent = new Intent(RefineSearchActivity.this,
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
				Intent intent = new Intent(RefineSearchActivity.this,
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
		// TODO Auto-generated method stub
		return null;
	}

	private JSONObject verifyAndGetSelectorsJson() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonSelector = new JSONObject();
		if (mLocalitySpinnerContainer.getVisibility() == View.VISIBLE
				&& mLocalitySelSpinner.getCount() > 0
				&& mLocalitySelSpinner.getSelectedItemPosition() > 0) {
			mLocalitySelectorDao.setSelectedIndex(mLocalitySelSpinner
					.getSelectedItemPosition());
			jsonSelector.put("locality_name", mLocalitySelSpinner
					.getItemAtPosition(mLocalitySelSpinner
							.getSelectedItemPosition()));
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
		if (jsonObject.has(AppConstants.KEYWORD_CITY_OF_REFINE)) {
			MaxisMainActivity.isCitySelected = true;
			mClRequest.setSearch_distance("");
		} else {
			MaxisMainActivity.isCitySelected = false;
		}

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
			mSearchEditText
					.setText(mSearchEditText.getText().toString().trim());

			String JSON_EXTRA = jsonForSearch();
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA);
			break;
		case R.id.show_profile_icon:
			onProfileClick();
			break;
		case R.id.ms_refine_btn:
			try {
				JSONObject postData = verifyAndGetSelectorsJson();
				if (postData != null) {
					mClRequest.setPostJsonPayload(postData.toString());
					CombindListingController controller = new CombindListingController(
							RefineSearchActivity.this,
							Events.REFINE_SEARCH_RESULT);
					startSppiner();
					controller.requestService(mClRequest);
				}
			} catch (JSONException e) {
				AnalyticsHelper.onError(
						FlurryEventsConstants.DATA_VALIDATION_ERR,
						"RefineSearchActivity : "
								+ AppConstants.DATA_VALIDATION_ERROR_MSG, e);
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
				Intent cityIntent = new Intent(RefineSearchActivity.this,
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
				Intent localityIntent = new Intent(RefineSearchActivity.this,
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

			} else {
				return null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
