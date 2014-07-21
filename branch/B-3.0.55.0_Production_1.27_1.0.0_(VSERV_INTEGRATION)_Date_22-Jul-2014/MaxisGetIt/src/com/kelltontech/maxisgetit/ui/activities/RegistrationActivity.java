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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.RegistrationController;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.dao.SubCategory;
import com.kelltontech.maxisgetit.db.CityTable;
import com.kelltontech.maxisgetit.requests.RegistrationRequest;
import com.kelltontech.maxisgetit.response.ControlDetailResponse;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;
import com.kelltontech.maxisgetit.utils.Utility;

public class RegistrationActivity extends MaxisMainActivity {
	private int mActionType;
	private ControlDetailResponse mTempletData;
	private SubCategory mSelectedCategory;

	private ImageView mHomeIconView, mProfileIconView;
	private EditText mEdName, mEdEmail, mEdPhone;
	private TextView mRegisterView;
	private RegistrationRequest mRequest;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private ImageView mHeaderBackButton;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private CompanyDetail mCompanyDetail;

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
		setContentView(R.layout.activity_registration);
		UiUtils.hideKeyboardOnTappingOutside(
				findViewById(R.id.reg_root_layout), this);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mActionType = bundle.getInt(AppConstants.ACTION_IDENTIFIER,
					AppConstants.ACTION_MY_ACCOUNT);
			if (mActionType == AppConstants.ACTION_REPORT_ERROR) {
				mCompanyDetail = bundle
						.getParcelable(AppConstants.COMP_DETAIL_DATA);
			} else {
				mTempletData = bundle.getParcelable(AppConstants.TEMPLET_DATA);
				mSelectedCategory = bundle
						.getParcelable(AppConstants.SELECTED_CAT_DATA);
			}
		}
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		// searchBtn = (ImageView) findViewById(R.id.search_icon_button);
		// searchBtn.setOnClickListener(RegistrationActivity.this);
		// searchEditText = (EditText) findViewById(R.id.search_box);
		mEdName = (EditText) findViewById(R.id.reg_name);
		mEdEmail = (EditText) findViewById(R.id.reg_email);
		mEdPhone = (EditText) findViewById(R.id.reg_mobile);
		String phNumber = NativeHelper
				.getMy10DigitPhoneNumber(RegistrationActivity.this);
		if (phNumber != null) {
			mEdPhone.setText(phNumber);
			// edPhone.setEnabled(false);
		}
		mRegisterView = (TextView) findViewById(R.id.register_button);
		mRegisterView.setOnClickListener(this);
		MaxisStore store = MaxisStore.getStore(RegistrationActivity.this);
		if (store.isRegisteredUser()) {
			mEdName.setText(store.getUserName());
			mEdPhone.setText(store.getUserMobileNumberToDispaly());
			mEdEmail.setText(store.getUserEmail());
		}
		mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler = (ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		if (!StringUtil.isNullOrEmpty(mSearchKeyword))
			mSearchEditText.setText(mSearchKeyword);

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
		AnalyticsHelper.trackSession(RegistrationActivity.this, AppConstants.Registration_Screen);
	}
	
	
	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	private void register(String name, String mobileNumber, String email) {
		mobileNumber = Utility.getMobileNoForWS(this, mobileNumber);
		RegistrationController regcController = new RegistrationController(
				RegistrationActivity.this, Events.REGISTRATION);
		mRequest = new RegistrationRequest(RegistrationActivity.this, name,
				mobileNumber);
		if (email != null)
			mRequest.setEmail(email);
		startSppiner();
		regcController.requestService(mRequest);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.search_icon_button:
		// searchEditText.setText(searchEditText.getText().toString().trim());
		// performSearch(searchEditText.getText().toString());
		// break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			showHomeScreen();
			break;
		case R.id.show_profile_icon:
//			onProfileClick();
			// Intent intentRegister = new Intent(RegistrationActivity.this,
			// RegistrationActivity.class);
			// intentRegister.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			// startActivity(intentRegister);
			break;
		case R.id.register_button:
			String name = mEdName.getText().toString().trim();
			mEdName.setText(name);
			if (name.equals("")) {
				showAlertDialog("Please enter your name");
				return;
			}
			String mobile = mEdPhone.getText().toString();
			// if (mobile.length() < 10 || mobile.indexOf('+',1)!=-1 ||
			// (mobile.indexOf('+')!=-1 && mobile.length()<=10)){
			if (StringUtil.isNullOrEmpty(mobile)) {
				showAlertDialog(getString(R.string.number_empty));
				return;
			} else if (mobile.length() <= 7 || mobile.length() >= 12
					|| !mobile.startsWith("1")) {
				showAlertDialog(getString(R.string.mobile_number_validation));
				return;
			}
			String email = mEdEmail.getText().toString().trim();
			mEdEmail.setText(email);
			if (email.equals(""))
				email = null;
			else if (!NativeHelper.isValidEmail(email)) {
				showAlertDialog("Please enter valid Email Id");
				return;
			}
			register(name, mobile, email);
			break;
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
		case R.id.mainSearchButton:
			mSearchEditText
					.setText(mSearchEditText.getText().toString().trim());
			String JSON_EXTRA = jsonForSearch();
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA);
			break;
		case R.id.header_btn_back:
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			this.finish();
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
				Intent cityIntent = new Intent(RegistrationActivity.this,
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
				Intent localityIntent = new Intent(RegistrationActivity.this,
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
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.REGISTRATION) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				Intent intent = new Intent(RegistrationActivity.this,
						LoginActivity.class);
				MaxisStore store = MaxisStore
						.getStore(RegistrationActivity.this);
				store.setUserMobileNumber(getString(R.string.country_code_excluding_plus)
						+ mRequest.getMobileNumber().substring(2));
				store.setUserRegistered(true);
				store.setUserName(mRequest.getUserName());
				store.setUserEmailId(mRequest.getEmail());
				MaxisResponse res = (MaxisResponse) msg.obj;
				if (!StringUtil.isNullOrEmpty(res.getServerMessage()))
					intent.putExtra(AppConstants.REGISTRATION_RESPONSE_MESSAGE,
							res.getServerMessage());
				if (mActionType == AppConstants.ACTION_SELL_POST) {
					intent.putExtra(AppConstants.ACTION_IDENTIFIER,
							AppConstants.ACTION_SELL_POST);
					intent.putExtra(AppConstants.TEMPLET_DATA, mTempletData);
					intent.putExtra(AppConstants.SELECTED_CAT_DATA,
							mSelectedCategory);
				} else if (mActionType == AppConstants.ACTION_REPORT_ERROR) {
					intent.putExtra(AppConstants.ACTION_IDENTIFIER,
							AppConstants.ACTION_REPORT_ERROR);
					intent.putExtra(AppConstants.COMP_DETAIL_DATA,
							mCompanyDetail);
					intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD,
							mSearchKeyword);
				} else if (mActionType == AppConstants.ACTION_ADD_FAV) {
					intent.putExtra(AppConstants.ACTION_IDENTIFIER,
							AppConstants.ACTION_ADD_FAV);
					intent.putExtra(AppConstants.IS_FROM_COMP_DETAIL_ADD_FAV,
							true);
					// intent.putExtra(AppConstants.COMP_DETAIL_DATA,
					// mCompanyDetail);
					// intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD,
					// mSearchKeyword);
				}
				startActivity(intent);
				finish();
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
				Intent intent = new Intent(RegistrationActivity.this,
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
				Intent intent = new Intent(RegistrationActivity.this,
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
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.REGISTRATION) {
			handler.sendMessage((Message) screenData);
		} else {
			handler.sendMessage((Message) screenData);
		}
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
			if (index == -1) {
				city_id = -1;
			} else {
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
