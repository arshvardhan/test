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
import android.util.Log;
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
import com.kelltontech.maxisgetit.R.id;
import com.kelltontech.maxisgetit.R.layout;
import com.kelltontech.maxisgetit.R.string;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.OutLetDetailtController;
import com.kelltontech.maxisgetit.controllers.OutletRefineController;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.OutLet;
import com.kelltontech.maxisgetit.dao.OutLetDetails;
import com.kelltontech.maxisgetit.dao.SelectorDAO;
import com.kelltontech.maxisgetit.db.CityTable;
import com.kelltontech.maxisgetit.model.CommonResponse;
import com.kelltontech.maxisgetit.requests.OutLetDetailRequest;
import com.kelltontech.maxisgetit.requests.OutletRefineRequest;
import com.kelltontech.maxisgetit.response.CompanyListResponse;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.response.RefineCategoryResponse;
import com.kelltontech.maxisgetit.response.RefineSelectorResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class RefineOutletActivity extends MaxisMainActivity {
	private Spinner mCitySelSpinner, mLocalitySelSpinner;
	private LinearLayout mSpinnerHolder, mLocalitySpinnerContainer;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private ImageView mProfileIconView;
	private TextView mRefineBtn;
	private RefineCategoryResponse mCatResponse;
	private OutLetDetailRequest detailRequest;
	private CompanyListResponse mClResponse;
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

	private ArrayList<String> citiesList;
	ArrayList<String> localities;
	private OutLetDetails outLetResponse;
	ArrayList<OutLet> outLets = new ArrayList<OutLet>();
	private String city = "";
	private String locality = "";

	private String previousCity;
	private String previousLocality;
	private int isSearchedModified;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refine_outlet);
		AnalyticsHelper.logEvent(FlurryEventsConstants.User_Visits_the_Deals_outlet_Modify_Page);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.ms_root_layout),
				this);
		ImageLoader.initialize(RefineOutletActivity.this);
		mLocalitySpinnerContainer = (LinearLayout) findViewById(R.id.ms_locality_chooser_container);
		mLocalitySelSpinner = (Spinner) findViewById(R.id.ms_locality_chooser);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(RefineOutletActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mRefineBtn = (TextView) findViewById(R.id.ms_refine_btn);
		mRefineBtn.setOnClickListener(this);
		mSpinnerHolder = (LinearLayout) findViewById(R.id.ms_spinner_holder);
		Bundle bundle = getIntent().getExtras();
		detailRequest = bundle.getParcelable("OutletRequest");
		isSearchedModified = bundle.getInt("isSearchedModified");

		if(isSearchedModified == 1) {
			previousCity = bundle.getString("selectedCity");
			previousLocality = bundle.getString("selectedLocality");
			citiesList = bundle.getStringArrayList("cityList");
			localities = bundle.getStringArrayList("localityList");
		} else {
			citiesList = bundle.getStringArrayList("CITIES_ARRAYLIST");
			citiesList.add(0, "Select");
		}
		showCitySpinner();
		if(isSearchedModified == 1) {
			for (int i = 0; i < citiesList.size(); i++) {
				if ((citiesList.get(i)).equals(previousCity))
					mCitySelSpinner.setSelection(i);
			}
			localities.add(0, "Select");
			showLocalitySpinner();
		}

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

		if (!StringUtil.isNullOrEmpty(bundle.getString("deal_title"))) {
			mHeaderTitle.setText(bundle.getString("deal_title"));
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
		AnalyticsHelper.trackSession(RefineOutletActivity.this, AppConstants.Screen_DealOutletModify);
	}

	private void showLocalitySpinner() {
		if (localities != null && localities.size() > 1) {
			ArrayAdapter<String> localityAdp = new ArrayAdapter<String>(
					RefineOutletActivity.this, R.layout.spinner_item,
					localities);
			mLocalitySelSpinner.setAdapter(localityAdp);
			mLocalitySpinnerContainer.setVisibility(View.VISIBLE);
			// if (mLocalitySelectorDao.getSelectedIndex() > 0)
			// mLocalitySelSpinner.setSelection(mLocalitySelectorDao
			// .getSelectedIndex());
			if(isSearchedModified == 1) {
				for (int i = 0; i < localities.size(); i++) {
					if ((localities.get(i)).equals(previousLocality))
						mLocalitySelSpinner.setSelection(i);
				}
			}
		} else {
			mLocalitySpinnerContainer.setVisibility(View.GONE);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void showCitySpinner() {
		if (citiesList == null || citiesList.size() < 1)
			return;
		ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_item,
				citiesList);
		mCitySelSpinner = (Spinner) findViewById(R.id.ms_spinner1);
		mCitySelSpinner.setAdapter(adapter);
		// mCatSelector.setSelection(mCatResponse.getSelectedCategoryIndex());
		mCitySelSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				if (position != 0) {
					String deal_id = detailRequest.getDeal_id();
					String cityName = citiesList.get(position);
					getLocalities(deal_id, cityName);
				} else {
					mLocalitySpinnerContainer.setVisibility(View.GONE);
					
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				showInfoDialog("nothing selected");
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
		} else if (event == Events.LOCALITY_LISTING_OUTLETS) {
			Log.i("manish", "inside setscreendata");
			CommonResponse cResponse = (CommonResponse) screenData;
			Message message = new Message();
			message.arg2 = event;

			if (cResponse.getResults().getError_Code().equalsIgnoreCase("1")) {
				message.arg1 = 1;
				message.obj = getResources().getString(
						R.string.communication_failure);
			} else {
				message.arg1 = 0;
				message.obj = cResponse;
			}
			handler.sendMessage(message);
		} else {
			System.out.println(screenData);
			Response response = (Response) screenData;
			Message message = new Message();
			message.arg2 = event;
			message.arg1 = 1;
			if (response.isError()) {
				message.obj = response.getErrorText();
			} else if (event == Events.OUTLET_DETAIL) {

				try {
					if (response.getPayload() instanceof OutLetDetails) {
						OutLetDetails outLetDetails = (OutLetDetails) response
								.getPayload();
						if (outLetDetails.getErrorCode() != 0) {
							message.obj = getResources().getString(
									R.string.communication_failure);
						} else {
							if (outLetDetails.getOutlet().size() < 1) {
								message.obj = new String(getResources()
										.getString(R.string.no_result_found));
							} else {
								message.arg1 = 0;
								message.obj = outLetDetails;
							}
						}
					} else {
						message.obj = new String(getResources().getString(
								R.string.communication_failure));
					}

				} catch (Exception e) {
					// TODO: handle exception
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
		} else if (msg.arg2 == Events.REFINE_SEARCH_RESULT) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				// mClResponse = (CompanyListResponse) msg.obj;
				// Intent intent = new Intent();
				// intent.putExtra(AppConstants.COMP_LIST_DATA, mClResponse);
				// intent.putExtra(AppConstants.DATA_LIST_REQUEST, mClRequest);
				// intent.putExtra(AppConstants.REFINE_ATTR_RESPONSE,
				// mSelctorResp);
				// intent.putExtra(AppConstants.REFINE_CAT_RESPONSE,
				// mCatResponse);
				// intent.putExtra(AppConstants.LOCALITY_DAO_DATA,
				// mLocalitySelectorDao);
				// setResult(RESULT_OK, intent);
				// finish();
			}
		} else if (msg.arg2 == Events.OUTLET_DETAIL) {
			try {
				if (msg.arg1 == 1) {
					showInfoDialog((String) msg.obj);
				} else {
					outLetResponse = (OutLetDetails) msg.obj;
					outLets = outLetResponse.getOutlet();
					if (outLets != null && outLets.size() > 0) {
						Intent intent = new Intent();
						intent.putExtra(AppConstants.OUTLET_DETAIL_DATA, outLetResponse);
						intent.putExtra("totalCount", Integer.parseInt(outLetResponse.getTotal_records()));
						intent.putExtra("OutletRequest", detailRequest);
						intent.putExtra("isSearchModified", 1);
						intent.putExtra("selectedCity", (StringUtil.isNullOrEmpty(city)) ? "" : city.trim());
						intent.putExtra("selectedLocality", (StringUtil.isNullOrEmpty(locality)) ? "" : locality.trim());
						intent.putStringArrayListExtra("cityList", (citiesList != null && citiesList.size() > 0) ? citiesList : null);
						intent.putStringArrayListExtra("localityList", (localities != null && localities.size() > 0) ? localities : null);
						setResult(RESULT_OK, intent);
						finish();
					} else {
						showInfoDialog("No Results Found.");
					}
				}
				stopSppiner();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		else if (msg.arg2 == Events.REFINE_SEARCH_LOCALITY) {
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
				Intent intent = new Intent(RefineOutletActivity.this,
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
				Intent intent = new Intent(RefineOutletActivity.this,
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
		} else if (msg.arg2 == Events.LOCALITY_LISTING_OUTLETS) {
			// TODO
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				CommonResponse response = (CommonResponse) msg.obj;
				localities = response.getResults().getLocalities()
						.getLocality();
				localities.add(0, "Select");
				showLocalitySpinner();
			}
			stopSppiner();
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

			OutLetDetailtController detailtController = new OutLetDetailtController(
					RefineOutletActivity.this, Events.OUTLET_DETAIL);
			city = citiesList.get(mCitySelSpinner
					.getSelectedItemPosition());
			locality = localities.get(mLocalitySelSpinner
					.getSelectedItemPosition());
			detailRequest.setCityName(("Select".equals(city)) ? "" : city);
			if ("".equals(detailRequest.getCityName())) {
				detailRequest.setLocalityName("");
			}
			else {
			detailRequest.setLocalityName(("Select".equals(locality)) ? "" : locality);
			}
			detailRequest.setPage_number(1);
			startSppiner();
			detailtController.requestService(detailRequest);

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
				Intent cityIntent = new Intent(RefineOutletActivity.this,
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
				Intent localityIntent = new Intent(RefineOutletActivity.this,
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

	public void getLocalities(String deal_id, String cityName) {
		OutletRefineRequest request = new OutletRefineRequest();
		request.setDeal_id(deal_id);
		request.setCityName(cityName);

		OutletRefineController outletRefineController = new OutletRefineController(
				RefineOutletActivity.this, Events.LOCALITY_LISTING_OUTLETS);
		startSppiner();
		outletRefineController.requestService(request);

	}
}
