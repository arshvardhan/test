package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.MultiSelectListAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.CityAreaListController;
import com.kelltontech.maxisgetit.controllers.PostClassifiedController;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.ControlDetails;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.dao.SubCategory;
import com.kelltontech.maxisgetit.db.CityTable;
import com.kelltontech.maxisgetit.response.ControlDetailResponse;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;
import com.kelltontech.maxisgetit.utils.Utility;

public class SaleTempletActivity extends MaxisMainActivity {
	private static final int DROP_DOWN = 1;
	private static final int MULTI_SELECT_lIST_BOX = 2;
	private static final int RADIO_BTN = 3;
	private static final int CHECK_BOX = 4;
	private static final int EDIT_BOX = 5;

	private EditText mTitle, mDescription;
	private TextView mUsernameTxt;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private ImageView mHomeIconView, mProfileIconView;
	private ControlDetailResponse mControlResp;
	private TextView mDescFillDetailContent;
	private LinearLayout mControlholder;
	private ArrayList<View> mControlViewList = new ArrayList<View>();
	private MaxisStore mStore;
	private boolean mIsLoggedInUser;
	private String mUserId = "0";
	private LinearLayout mLayoutGuestDetail;
	private LinearLayout mLocalityContainer;
	private Spinner mCityDropDown;
	private Spinner mLocalityDropDown;
	private TextView mSubmitBtn;
	private String mCategoryId;
	private EditText mEdUserName;
	private EditText mEdUserMobile;
	private TextView mTermsAndConditionLink;
	private CheckBox mTermAgreement;
	private ImageView mSearchToggler;
	private LinearLayout mSearchContainer;
	private ImageView mHeaderBackButton;

	private TextView mLableTitle;
	private boolean isTitleMandatory = true;

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

		setContentView(R.layout.activity_templet_list);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.tl_root_layout),
				this);
		mTermsAndConditionLink = (TextView) findViewById(R.id.atl_terms_link);
		mTermsAndConditionLink.setOnClickListener(this);
		mTermAgreement = (CheckBox) findViewById(R.id.atl_terms_check);
		mStore = MaxisStore.getStore(SaleTempletActivity.this);
		ImageLoader.initialize(SaleTempletActivity.this);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(SaleTempletActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		Bundle bundle = getIntent().getExtras();
		mControlResp = bundle.getParcelable(AppConstants.TEMPLET_DATA);
		// thumbImage = (ImageView) findViewById(R.id.tl_thumb_icon);
		SubCategory selectedCategory = bundle
				.getParcelable(AppConstants.SELECTED_CAT_DATA);
		String catName = selectedCategory.getCategoryTitle();
		mCategoryId = selectedCategory.getCategoryId();

		mLocalityContainer = (LinearLayout) findViewById(R.id.atl_locality_chooser_container);
		mCityDropDown = (Spinner) findViewById(R.id.atl_city_chooser);
		mLocalityDropDown = (Spinner) findViewById(R.id.atl_locality_chooser);
		mDescFillDetailContent = (TextView) findViewById(R.id.atl_desc_content);
		mDescFillDetailContent.setText(getResources().getString(
				R.string.fill_detail_selling1)
				+ " "
				+ catName
				+ " "
				+ getResources().getString(R.string.fill_detail_selling2));
		mControlholder = (LinearLayout) findViewById(R.id.tl_control_holder);
		for (int i = 0; i < mControlResp.getControlList().size(); i++) {
			ControlDetails controlDetail = mControlResp.getControlList().get(i);
			switch (controlDetail.getControlTypeId()) {
			case DROP_DOWN:
				mControlViewList.add(inflateSpinnerControl(controlDetail));
				isTitleMandatory = false;
				break;
			case MULTI_SELECT_lIST_BOX:
				// addMultiselectList(controlDetail);
				mControlViewList.add(addMultiselectList2(controlDetail));
				isTitleMandatory = false;
				break;
			case RADIO_BTN:
				mControlViewList.add(addRadioButtonControl(controlDetail));
				isTitleMandatory = false;
				break;
			case CHECK_BOX:
				// controlViewList.add(addCheckBoxControl(controlDetail));
				mControlViewList.add(addMultiselectList2(controlDetail));
				isTitleMandatory = false;
				break;
			case EDIT_BOX:
				// controlViewList.add(addEditTextControl(controlDetail));
				mControlViewList.add(inflateEditTextControl(controlDetail));
				break;
			}
		}

		mLableTitle = (TextView) findViewById(R.id.atl_lbl_title);
		if (isTitleMandatory) {
			mLableTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.required_icon, 0);
		}
		mTitle = (EditText) findViewById(R.id.atl_title);
		mDescription = (EditText) findViewById(R.id.atl_desc);
		mIsLoggedInUser = mStore.isLoogedInUser();
		mUsernameTxt = (TextView) findViewById(R.id.atl_guest_name);
		mLayoutGuestDetail = (LinearLayout) findViewById(R.id.atl_guest_detail_controller);
		if (mIsLoggedInUser) {
			mUserId = mStore.getUserID();
			mUsernameTxt.setText(getResources().getString(R.string.dear) + " " + mStore.getUserName());
			mLayoutGuestDetail.setVisibility(View.GONE);
		}
		mEdUserName = (EditText) findViewById(R.id.atl_user_name);
		mEdUserMobile = (EditText) findViewById(R.id.atl_user_mobile);
		mSubmitBtn = (TextView) findViewById(R.id.atl_submit_btn);
		mSubmitBtn.setOnClickListener(this);

		mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler = (ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);

		CityTable cityTable = new CityTable((MyApplication) getApplication());
		ArrayList<CityOrLocality> cityList = cityTable.getAllCitiesList();
		if (cityList != null && cityList.size() > 0)
			inflateCityList(cityList);
		else {
			CityAreaListController clController = new CityAreaListController(SaleTempletActivity.this, Events.SALE_CITY_LISTING);
			startSppiner();
			clController.requestService(null);
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
		mIsLoggedInUser = mStore.isLoogedInUser();
		if (mIsLoggedInUser) {
			mUserId = mStore.getUserID();
			mUsernameTxt.setText(getResources().getString(R.string.dear) + " "
					+ mStore.getUserName());
			mLayoutGuestDetail.setVisibility(View.GONE);
		} else {
			mUsernameTxt.setText(getResources().getString(R.string.dear_guest));
			mUserId = mStore.getUserID();
			mLayoutGuestDetail.setVisibility(View.VISIBLE);
		}
		super.onResume();
		AnalyticsHelper.trackSession(SaleTempletActivity.this, AppConstants.Add_Post);
	}

	private void addDefaultSelect(ArrayList<CityOrLocality> cityOrLocalityList) {
		if (cityOrLocalityList != null) {
			CityOrLocality temp = new CityOrLocality();
			temp.setId(-1);
			temp.setName("Select");
			cityOrLocalityList.add(0, temp);
		}
	}

	private void inflateCityList(ArrayList<CityOrLocality> cityList) {
		addDefaultSelect(cityList);
		ArrayAdapter<CityOrLocality> cityAdp = new ArrayAdapter<CityOrLocality>(
				SaleTempletActivity.this, R.layout.spinner_item, cityList);
		mCityDropDown.setAdapter(cityAdp);
		mCityDropDown.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapter, View arg1,
					int position, long arg3) {
				if (position == 0) {
					mLocalityContainer.setVisibility(View.GONE);
				} else {
					CityOrLocality city = (CityOrLocality) adapter
							.getItemAtPosition(position);
					CityAreaListController clController = new CityAreaListController(
							SaleTempletActivity.this,
							Events.SALE_LOCALITY_LISTING);
					startSppiner();
					clController.requestService(city.getId() + "");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	public JSONObject verifyInput() throws JSONException {
		String titleStr = mTitle.getText().toString().trim();
		mTitle.setText(titleStr);
		boolean isTitleEmpty = titleStr.length() == 0 ? true : false;
		int titlePartCount = 0;
		String descStr = mDescription.getText().toString().trim();
		mDescription.setText(descStr);

		// Validation flow changed
		JSONObject postJson = new JSONObject();
		String cityId = null;
		String localityId = null;
		if (mCityDropDown.getSelectedItemPosition() <= 0) {
			showAlertDialog("Please select a City");
			return null;
		} else {
			CityOrLocality city = (CityOrLocality) mCityDropDown
					.getSelectedItem();
			cityId = city.getId() + "";
			postJson.put("city_id", cityId);
			if (mLocalityDropDown.getSelectedItemPosition() <= 0) {
				showAlertDialog("Please select a Locality");
				// mLocalityContainer.setVisibility(View.GONE);
				return null;
			} else {
				CityOrLocality locality = (CityOrLocality) mLocalityDropDown
						.getSelectedItem();
				localityId = locality.getId() + "";
				postJson.put("locality_id", localityId);
			}
		}

		JSONArray selectorsArr = new JSONArray();
		JSONObject selector = null;
		JSONArray valuesJArray;// = new JSONArray();
		for (int i = 0; i < mControlViewList.size(); i++) {
			selector = new JSONObject();
			ControlDetails controlDetail = (ControlDetails) mControlViewList
					.get(i).getTag();
			selector.put("column_id", controlDetail.getColumnId());
			switch (controlDetail.getControlTypeId()) {
			case DROP_DOWN:
				valuesJArray = new JSONArray();
				Spinner spinner = (Spinner) mControlViewList.get(i);
				if (controlDetail.getRequired() == 1
						&& spinner.getSelectedItemPosition() == 0) {
					showInfoDialog("Please select value for "
							+ controlDetail.getDisplayName());
					return null;
				}
				if (spinner.getSelectedItemPosition() != 0) {
					valuesJArray.put(spinner.getSelectedItem());
					if (isTitleEmpty && titlePartCount < 2) {
						titleStr += " " + spinner.getSelectedItem();
						titlePartCount++;
					}
				}
				selector.put("attr_value", valuesJArray);
				selectorsArr.put(selector);
				break;
			case MULTI_SELECT_lIST_BOX:
				ListView list = (ListView) mControlViewList.get(i);
				MultiSelectListAdapter listAdapter = (MultiSelectListAdapter) list
						.getAdapter();
				if (controlDetail.getRequired() == 1
						&& listAdapter.getSelectedValues().size() == 0) {
					showInfoDialog("Please select values for "
							+ controlDetail.getDisplayName());
					return null;
				}
				ArrayList<String> selectedVals = listAdapter
						.getSelectedValues();
				valuesJArray = new JSONArray();
				for (int j = 0; j < selectedVals.size(); j++) {
					valuesJArray.put(selectedVals.get(j));
					if (j == 0 && isTitleEmpty && titlePartCount < 2) {
						titleStr += " " + selectedVals.get(j);
						titlePartCount++;
					}
				}
				selector.put("attr_value", valuesJArray);
				selectorsArr.put(selector);
				break;
			case RADIO_BTN:
				RadioGroup radioGrp = (RadioGroup) mControlViewList.get(i);
				if (controlDetail.getRequired() == 1
						&& radioGrp.getCheckedRadioButtonId() == -1) {
					showInfoDialog("Please select values for "
							+ controlDetail.getDisplayName());
					return null;
				}
				valuesJArray = new JSONArray();
				for (int j = 0; j < radioGrp.getChildCount(); j++) {
					RadioButton button = (RadioButton) radioGrp.getChildAt(j);
					if (button.isSelected() || button.isChecked()) {
						valuesJArray.put(button.getText());
						System.out.println("got selection");
						if (isTitleEmpty && titlePartCount < 2) {
							titleStr += " " + button.getText();
							titlePartCount++;
						}
					}
				}
				selector.put("attr_value", valuesJArray);
				selectorsArr.put(selector);
				break;
			case CHECK_BOX:
				ListView clist = (ListView) mControlViewList.get(i);
				MultiSelectListAdapter clistAdapter = (MultiSelectListAdapter) clist
						.getAdapter();
				if (controlDetail.getRequired() == 1
						&& clistAdapter.getSelectedValues().size() == 0) {
					showInfoDialog("Please select values for "
							+ controlDetail.getDisplayName());
					return null;
				}
				ArrayList<String> cSelectedVals = clistAdapter
						.getSelectedValues();
				valuesJArray = new JSONArray();
				for (int j = 0; j < cSelectedVals.size(); j++) {
					valuesJArray.put(cSelectedVals.get(j));
					if (j == 0 && isTitleEmpty && titlePartCount < 2) {
						titleStr += " " + cSelectedVals.get(j);
						titlePartCount++;
					}
				}
				selector.put("attr_value", valuesJArray);
				selectorsArr.put(selector);
				break;
			case EDIT_BOX:
				EditText edTxtBox = (EditText) mControlViewList.get(i);
				String inputTxt = edTxtBox.getText().toString().trim();
				if (controlDetail.getRequired() == 1 && inputTxt.length() == 0) {
					showInfoDialog("Please Enter text for "
							+ controlDetail.getDisplayName());
					return null;
				}
				valuesJArray = new JSONArray();
				valuesJArray.put(inputTxt);
				selector.put("attr_value", inputTxt);
				selectorsArr.put(selector);
				break;
			}

		}

		if (titleStr.trim().length() == 0) {
			showInfoDialog("Please enter title");
			return null;
		}

		// JSONObject postJson = new JSONObject();
		if (!mIsLoggedInUser) {
			String uName = mEdUserName.getText().toString().trim();
			mEdUserName.setText(uName);
			if (uName.equals("")) {
				showInfoDialog("Please enter your Name");
				return null;
			}
			String mobile = mEdUserMobile.getText().toString();
			// if (mobile.length() < 10 || mobile.indexOf('+',1)!=-1 ||
			// (mobile.indexOf('+')!=-1 && mobile.length()<=10)) {
			if (StringUtil.isNullOrEmpty(mobile)) {
				showInfoDialog(getString(R.string.number_empty));
				return null;
			} else if (mobile.length() <= 7 || mobile.length() >= 12
					|| !mobile.startsWith("1")) {
				showInfoDialog(getString(R.string.mobile_number_validation));
				return null;
			}
			postJson.put("name", uName);
			postJson.put("mobile", Utility.getMobileNoForWS(this, mobile));
		}

		postJson.put("selectors", selectorsArr);
		postJson.put("description", descStr);
		postJson.put("title", titleStr);
		postJson.put("uid", mUserId);
		postJson.put("cat_id", mCategoryId);
		/*
		 * String cityId = null; String localityId = null; if
		 * (mCityDropDown.getSelectedItemPosition() == 0) {
		 * showAlertDialog("Please select a City"); return null; } else {
		 * CityOrLocality city = (CityOrLocality)
		 * mCityDropDown.getSelectedItem(); cityId = city.getId() + "";
		 * postJson.put("city_id", cityId); if
		 * (mLocalityDropDown.getSelectedItemPosition() == 0) {
		 * showAlertDialog("Please select a Locality"); return null; } else {
		 * CityOrLocality locality = (CityOrLocality)
		 * mLocalityDropDown.getSelectedItem(); localityId = locality.getId() +
		 * ""; postJson.put("locality_id", localityId); } }
		 */

		if (!mTermAgreement.isChecked()) {
			showInfoDialog("Please Accept the terms and conditions");
			return null;
		}
		System.out.println(postJson.toString());
		return postJson;
	}

	private View getSpacerView(int heightInDP) {
		View view = new View(SaleTempletActivity.this);
		final float scale = this.getResources().getDisplayMetrics().density;
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (scale * heightInDP)));
		return view;
	}

	private View addMultiselectList2(ControlDetails conDetail) {
		final float scale = this.getResources().getDisplayMetrics().density;
		LinearLayout outerLayout = new LinearLayout(SaleTempletActivity.this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		outerLayout.setLayoutParams(params);
		outerLayout.setBackgroundResource(R.drawable.shape_list_outer);
		outerLayout.setOrientation(LinearLayout.VERTICAL);
		TextView titleView = new TextView(SaleTempletActivity.this);
		titleView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		titleView.setText(conDetail.getDisplayName());
		if (conDetail.getRequired() == 1) {
			titleView.setCompoundDrawablesWithIntrinsicBounds(null, null,
					getResources().getDrawable(R.drawable.required_icon), null);
			titleView.setCompoundDrawablePadding(5);
		}

		titleView.setTextColor(this.getResources().getColor(R.color.white));
		titleView.setTextSize(14);
		titleView.setTypeface(Typeface.DEFAULT_BOLD);
		titleView.setPadding((int) (scale * 10), (int) (scale * 10),
				(int) (scale * 5), (int) (scale * 5));
		outerLayout.addView(titleView);

		LinearLayout llLayout = new LinearLayout(SaleTempletActivity.this);
		llLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (scale * 150)));
		llLayout.setPadding((int) (scale * 5), (int) (scale * 5),
				(int) (scale * 5), (int) (scale * 5));
		llLayout.setBackgroundDrawable(this.getResources().getDrawable(
				R.drawable.shape_list_content_bg));
		ListView list = new ListView(SaleTempletActivity.this);
		list.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		MultiSelectListAdapter adapter = new MultiSelectListAdapter(
				SaleTempletActivity.this, conDetail.getValues());
		list.setAdapter(adapter);
		llLayout.addView(list);
		outerLayout.addView(llLayout);
		mControlholder.addView(outerLayout);
		list.setTag(conDetail);
		mControlholder.addView(getSpacerView(20));
		return list;
	}

	private View addRadioButtonControl(ControlDetails conDetail) {
		final float scale = this.getResources().getDisplayMetrics().density;
		LinearLayout outerLayout = new LinearLayout(SaleTempletActivity.this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		outerLayout.setLayoutParams(params);
		outerLayout.setBackgroundResource(R.drawable.shape_list_outer);
		outerLayout.setOrientation(LinearLayout.VERTICAL);
		TextView titleView = new TextView(SaleTempletActivity.this);
		titleView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		titleView.setText(conDetail.getDisplayName());
		if (conDetail.getRequired() == 1)
			titleView.setCompoundDrawablesWithIntrinsicBounds(null, null,
					getResources().getDrawable(R.drawable.required_icon), null);
		titleView.setTextColor(this.getResources().getColor(R.color.white));
		titleView.setTextSize(14);
		titleView.setTypeface(Typeface.DEFAULT_BOLD);
		titleView.setPadding((int) (scale * 10), (int) (scale * 10),
				(int) (scale * 5), (int) (scale * 5));
		outerLayout.addView(titleView);
		LinearLayout llLayout = new LinearLayout(SaleTempletActivity.this);
		llLayout.setLayoutParams(params);
		llLayout.setPadding((int) (scale * 5), (int) (scale * 5),
				(int) (scale * 5), (int) (scale * 5));
		llLayout.setBackgroundDrawable(this.getResources().getDrawable(
				R.drawable.shape_list_content_bg));
		RadioGroup rg = new RadioGroup(SaleTempletActivity.this);
		rg.setLayoutParams(new TableLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		rg.setOrientation(RadioGroup.VERTICAL);
		rg.setTag(conDetail);
		for (int i = 0; i < conDetail.getValues().size(); i++) {
			RadioButton rb = new RadioButton(SaleTempletActivity.this);
			rb.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			rb.setText(conDetail.getValues().get(i));
			rb.setButtonDrawable(R.drawable.selector_radio_button);
			rg.addView(rb);
		}
		llLayout.addView(rg);
		outerLayout.addView(llLayout);
		mControlholder.addView(outerLayout);
		mControlholder.addView(getSpacerView(20));
		return rg;
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	private View inflateEditTextControl(ControlDetails condDetail) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout editTxtContainer = (LinearLayout) inflater.inflate(
				R.layout.edit_text_layout, null);
		TextView v = (TextView) editTxtContainer
				.findViewById(R.id.etl_text_view);
		v.setText(condDetail.getDisplayName());
		if (condDetail.getRequired() == 1)
			v.setCompoundDrawablesWithIntrinsicBounds(null, null,
					getResources().getDrawable(R.drawable.required_icon), null);
		EditText box = (EditText) editTxtContainer
				.findViewById(R.id.etl_edit_view);
		box.setTag(condDetail);
		mControlholder.addView(editTxtContainer);
		return box;

	}

	private View inflateSpinnerControl(ControlDetails conDetail) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout spinnerContainer = (LinearLayout) inflater.inflate(
				R.layout.spinner_layout, null);
		TextView v = (TextView) spinnerContainer.findViewById(R.id.spin_txt);
		v.setText(conDetail.getDisplayName());
		if (conDetail.getRequired() == 1)
			v.setCompoundDrawablesWithIntrinsicBounds(null, null,
					getResources().getDrawable(R.drawable.required_icon), null);
		Spinner spinner = (Spinner) spinnerContainer
				.findViewById(R.id.spin_spin);
		conDetail.insertDefaultSelectionValue();
		ArrayAdapter<String> madapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item, conDetail.getValues());
		spinner.setAdapter(madapter);
		mControlholder.addView(spinnerContainer);
		spinner.setTag(conDetail);
		return spinner;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_icon_button:
		case R.id.mainSearchButton:
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			String JSON_EXTRA = jsonForSearch();
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			showHomeScreen();
			break;
		case R.id.show_profile_icon:
			onProfileClick();
			break;
		case R.id.atl_terms_link:
			Intent tcIntent = new Intent(SaleTempletActivity.this, TermsAndConditionActivity.class);
			startActivity(tcIntent);
			// showInfoDialog("Terms and Conditions");
			break;
		case R.id.atl_submit_btn:
			try {
				JSONObject postJsonObject = verifyInput();
				if (postJsonObject != null) {
					PostClassifiedController controller = new PostClassifiedController(SaleTempletActivity.this, Events.POST_CLASSIFIED_EVENT);
					controller.requestService(postJsonObject);
					startSppiner();
				}
			} catch (JSONException e) {
				showToast("Internal error : STA");
				AnalyticsHelper.onError(FlurryEventsConstants.DATA_VALIDATION_ERR, "SaleTempletActivity : " + AppConstants.DATA_VALIDATION_ERROR_MSG, e);
			}
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
				Intent cityIntent = new Intent(SaleTempletActivity.this, AdvanceSelectCity.class);
				cityIntent.putExtra("CITY_LIST", cityListString);
				cityIntent.putExtra("SELECTED_CITY", selectedCity);
				startActivityForResult(cityIntent, AppConstants.CITY_REQUEST);
			} else {
				setSearchCity();
			}
			break;

		case R.id.currentLocality:
			if (localityItems != null && localityItems.size() > 0) {
				Intent localityIntent = new Intent(SaleTempletActivity.this, AdvanceSelectLocalityActivity.class);
				localityIntent.putExtra("LOCALITY_LIST", localityItems);
				localityIntent.putStringArrayListExtra("LOCALITY_INDEX", selectedLocalityindex);
				startActivityForResult(localityIntent, AppConstants.LOCALITY_REQUEST);
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
		} else if (msg.arg2 == Events.POST_CLASSIFIED_EVENT) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				String text = mUsernameTxt.getText() + ", " + "your sell requirement has successfully be posted";
				Intent notificationIntent = new Intent(SaleTempletActivity.this, AdPostNotificaitonActivity.class);
				notificationIntent.putExtra(AppConstants.NOTIFICATION_TEXT, text);
				startActivity(notificationIntent);
				finish();
			}
		} else if (msg.arg2 == Events.CITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				CityTable cityTable = new CityTable((MyApplication) getApplication());
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				cityTable.addCityList(glistRes.getCityOrLocalityList());
				ArrayList<CityOrLocality> cityList = cityTable.getAllCitiesList();
				inflateCityList(cityList);
			}
		} else if (msg.arg2 == Events.SALE_LOCALITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				ArrayList<CityOrLocality> localityList = glistRes.getCityOrLocalityList();
				addDefaultSelect(localityList);
				ArrayAdapter<CityOrLocality> localityAdp = new ArrayAdapter<CityOrLocality>(SaleTempletActivity.this, R.layout.spinner_item, localityList);
				mLocalityDropDown.setAdapter(localityAdp);
				mLocalityContainer.setVisibility(View.VISIBLE);
			}
		} else if (msg.arg2 == Events.CITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				cityList = glistRes.getCityOrLocalityList();
				Intent intent = new Intent(SaleTempletActivity.this, AdvanceSelectCity.class);
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
				Intent intent = new Intent(SaleTempletActivity.this, AdvanceSelectLocalityActivity.class);
				localityItems = new ArrayList<String>();
				for (CityOrLocality dealCityOrLoc : localityList) {
					localityItems.add(dealCityOrLoc.getName());
				}
				intent.putExtra("LOCALITY_LIST", localityItems);
				intent.putStringArrayListExtra("LOCALITY_INDEX", selectedLocalityindex);
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
		} else if (event == Events.POST_CLASSIFIED_EVENT) {
			handler.sendMessage((Message) screenData);
		} else if (event == Events.LOCALITY_LISTING) {
			handler.sendMessage((Message) screenData);
		} else if (event == Events.CITY_LISTING) {
			handler.sendMessage((Message) screenData);
		} else {
			handler.sendMessage((Message) screenData);
		}
	}

	@Override
	protected void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		if (resultCode == RESULT_OK && reqCode == AppConstants.CITY_REQUEST) {
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

		} else if (resultCode == RESULT_OK && reqCode == AppConstants.LOCALITY_REQUEST) {
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
