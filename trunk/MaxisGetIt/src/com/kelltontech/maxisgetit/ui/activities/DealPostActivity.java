package com.kelltontech.maxisgetit.ui.activities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.PostDealCityLocalityListController;
import com.kelltontech.maxisgetit.controllers.PostDealController;
import com.kelltontech.maxisgetit.controllers.PostImageController;
import com.kelltontech.maxisgetit.controllers.RemovePostDealImgController;
import com.kelltontech.maxisgetit.dao.CategoryWithCharge;
import com.kelltontech.maxisgetit.dao.CompanyCategory;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.dao.PostDealCityOrLoc;
import com.kelltontech.maxisgetit.requests.PostDealCityLocalityRequest;
import com.kelltontech.maxisgetit.response.DealsListResponse;
import com.kelltontech.maxisgetit.response.ImageDataResponse;
import com.kelltontech.maxisgetit.response.PostDealCityLocListResponse;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.ui.widgets.MultiSelectSpinner;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;
import com.kelltontech.maxisgetit.utils.BitmapCalculation;
import com.kelltontech.maxisgetit.utils.UiUtils;

public class DealPostActivity extends MaxisMainActivity {
	public static final int START_DATE_PICKER_DIALOG = 1122;
	public static final int END_DATE_PICKER_DIALOG = 1123;
	private static final int IMAGE_PICK = 1001;
	private ArrayAdapter<CategoryWithCharge> mCategoryAdapter;
	private ArrayAdapter<CompanyCategory> mCompanyAdapter;
	private TextView mDealPostBtn, mStartDateBtn, mEndDateBtn;
	private EditText mDealTitle, mDealDesc, mVoucherCount, mVoucherCode;
	private LinearLayout mCityLocalityChooserSpinnerContainer;
	private LinearLayout mCategorySpinnerContainer;
	private LinearLayout mLocalityChooserSpinnerContainer;
	private LinearLayout mVoucherDetailContainer;
	private LinearLayout mVoucherCountContainer;
	private LinearLayout mSingleCodeContainer;
	private LinearLayout mEmailAlertContainer;
	private ImageView imgView[], removeImgView[];
	private RadioButton radioBtns[];
	private RadioGroup mDealApplicableInGroup, mTypeOfDealGroup;
	private RadioGroup mVoucherGroup, mVoucherTypeGroup;
	private RadioButton mSingleCodeRadioBtn, mProvidedBySMERadioBtn;
	private Spinner mCatChooser, mCompanyChooser;
	private MultiSelectSpinner mCityDropDown1, mLocalityDropDown1;
	private TextView mCityDropDown, mLocalityDropDown;
	private CompanyCategory mSingalCompCat, selectedComp;
	private DealsListResponse mMyDealResp;
	private MaxisStore mStore;
	private Bitmap mBitmap;
	private RadioButton mEntireMalaysiaRadioBtn, mSelectiveCities;
	// String imageNameForRemoval;
	// private int imageToRemove;
	private int removeImageIndex;
	private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth,
			mEndDay;
	private boolean mIsSingleCompany;
	private ArrayList<String> imageId = new ArrayList<String>();
	private List<String> selectedCities = new ArrayList<String>();
	private List<String> selectedLocalities = new ArrayList<String>();
	private boolean booleanFirstTime = false;

	private int checkedIndex = 0;
	ArrayList<String> selectedStrings;
	ArrayList<String> items;
	ArrayList<String> localityItems;
	
	private ImageView mProfileIconView;
	private ImageView mHeaderBackButton;
	private ImageView mHomeIconView;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private TextView mHeaderTitle;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deal_post);
//		UiUtils.hideKeyboardOnTappingOutside(
//				findViewById(R.id.adp_root_layout), this);

		mStore = MaxisStore.getStore(DealPostActivity.this);

		mHeaderTitle = (TextView) findViewById(R.id.header_title);
		mHeaderTitle.setText("Post a Deal");
		
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);
		
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(DealPostActivity.this);
		
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler = (ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		
		final Calendar c = Calendar.getInstance();
		mStartYear = mEndYear = c.get(Calendar.YEAR);
		mStartMonth = mEndMonth = c.get(Calendar.MONTH);
		mStartDay = mEndDay = c.get(Calendar.DAY_OF_MONTH);
		mCityLocalityChooserSpinnerContainer = (LinearLayout) findViewById(R.id.city_locality_chooser_container);
		mLocalityChooserSpinnerContainer = (LinearLayout) findViewById(R.id.locality_chooser_container);
		mCategorySpinnerContainer = (LinearLayout) findViewById(R.id.adp_category_container);
		mVoucherDetailContainer = (LinearLayout) findViewById(R.id.adp_voucher_detail_container);
		mVoucherCountContainer = (LinearLayout) findViewById(R.id.adp_voucher_count_container);
		mSingleCodeContainer = (LinearLayout) findViewById(R.id.adp_single_code_container);
		mEmailAlertContainer = (LinearLayout) findViewById(R.id.adp_provided_by_sme_email_alert_container);
		mDealApplicableInGroup = (RadioGroup) findViewById(R.id.adp_deal_applicable_in_RG);
		mEntireMalaysiaRadioBtn = (RadioButton) findViewById(R.id.adp_entire_malaysia);
		mSelectiveCities = (RadioButton) findViewById(R.id.adp_in_selective_cities);

		mDealApplicableInGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == R.id.adp_entire_malaysia) {
							mCityLocalityChooserSpinnerContainer
									.setVisibility(View.GONE);
						} else if (checkedId == R.id.adp_in_selective_cities) {
							PostDealCityLocalityRequest request = new PostDealCityLocalityRequest();
							if (mCompanyChooser.getSelectedItemPosition() < 1
									|| "Select".equals(mCompanyChooser
											.getSelectedItem())) {
								showAlertDialog("Please select a company.");
								mEntireMalaysiaRadioBtn.setChecked(true);
								mSelectiveCities.setChecked(false);

							} else if (mCompanyChooser
									.getSelectedItemPosition() > 0
									&& (mCatChooser.getSelectedItemPosition() < 1 || "Select"
											.equals(mCatChooser
													.getSelectedItem()))) {
								showAlertDialog("Please select a category.");
								mEntireMalaysiaRadioBtn.setChecked(true);
								mSelectiveCities.setChecked(false);
							} else {
								
								mCityDropDown.setText("Select");
								mLocalityDropDown.setText("select");
								
								if (mMyDealResp.getCompCategoryList().size() == 2) {
									request.setCompanyId(mSingalCompCat
											.getCompanyId());
									request.setCategoryId(mSingalCompCat
											.getCategoryList().get(1)
											.getCategoryId());
								} else {
									request.setCompanyId(selectedComp
											.getCompanyId());
									request.setCategoryId(selectedComp
											.getCategoryList().get(1)
											.getCategoryId());
								}
								PostDealCityLocalityListController clController = new PostDealCityLocalityListController(
										DealPostActivity.this,
										Events.POST_DEAL_CITY_LISTING);
								startSppiner();
								request.setUserId(mStore.getUserID());
								clController.requestService(request);

								mCityLocalityChooserSpinnerContainer
										.setVisibility(View.VISIBLE);
							}

						}
					}
				});
		mTypeOfDealGroup = (RadioGroup) findViewById(R.id.adp_type_of_deal_RG);
		mTypeOfDealGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == R.id.adp_informative) {
							mVoucherDetailContainer.setVisibility(View.GONE);
						} else if (checkedId == R.id.adp_voucher) {
							mVoucherDetailContainer.setVisibility(View.VISIBLE);
						}
					}
				});
		mVoucherGroup = (RadioGroup) findViewById(R.id.adp_voucher_RG);
		mSingleCodeRadioBtn = (RadioButton) findViewById(R.id.adp_single_code);
		mProvidedBySMERadioBtn = (RadioButton) findViewById(R.id.adp_provided_by_sme);
		mEntireMalaysiaRadioBtn = (RadioButton) findViewById(R.id.adp_entire_malaysia);
		/*
		 * mSelectiveCitiesRadioBtn = (RadioButton)
		 * findViewById(R.id.adp_in_selective_cities);
		 */
		mVoucherGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.adp_specific_count) {
					mVoucherCountContainer.setVisibility(View.VISIBLE);
					mProvidedBySMERadioBtn.setVisibility(View.VISIBLE);
					mSingleCodeRadioBtn.setChecked(true);

				} else if (checkedId == R.id.adp_unlimited_codes) {
					mVoucherCountContainer.setVisibility(View.GONE);
					mProvidedBySMERadioBtn.setVisibility(View.GONE);
					mSingleCodeRadioBtn.setChecked(true);
				}
			}
		});
		mTypeOfDealGroup = (RadioGroup) findViewById(R.id.adp_type_of_deal_RG);
		mTypeOfDealGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == R.id.adp_informative) {
							mVoucherDetailContainer.setVisibility(View.GONE);
						} else if (checkedId == R.id.adp_voucher) {
							mVoucherDetailContainer.setVisibility(View.VISIBLE);
						}
					}
				});
		mVoucherGroup = (RadioGroup) findViewById(R.id.adp_voucher_RG);
		mSingleCodeRadioBtn = (RadioButton) findViewById(R.id.adp_single_code);
		mProvidedBySMERadioBtn = (RadioButton) findViewById(R.id.adp_provided_by_sme);
		mVoucherGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.adp_specific_count) {
					mVoucherCountContainer.setVisibility(View.VISIBLE);
					mProvidedBySMERadioBtn.setVisibility(View.VISIBLE);
					mSingleCodeRadioBtn.setChecked(true);

				} else if (checkedId == R.id.adp_unlimited_codes) {
					mVoucherCountContainer.setVisibility(View.GONE);
					mProvidedBySMERadioBtn.setVisibility(View.GONE);
					mSingleCodeRadioBtn.setChecked(true);
				}
			}
		});
		mVoucherTypeGroup = (RadioGroup) findViewById(R.id.adp_voucher_type_RG);
		mVoucherTypeGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == R.id.adp_single_code) {
							mSingleCodeContainer.setVisibility(View.VISIBLE);
							mEmailAlertContainer.setVisibility(View.GONE);
						} else if (checkedId == R.id.adp_system_generated) {
							mSingleCodeContainer.setVisibility(View.GONE);
							mEmailAlertContainer.setVisibility(View.GONE);
						} else if (checkedId == R.id.adp_provided_by_sme) {
							mSingleCodeContainer.setVisibility(View.GONE);
							mEmailAlertContainer.setVisibility(View.VISIBLE);
						}
					}
				});
		mMyDealResp = getIntent().getParcelableExtra(
				AppConstants.MY_DEALS_RESPONSE);
		imgView = new ImageView[4];
		imgView[0] = (ImageView) findViewById(R.id.adp_deal_image);
		imgView[0].setOnClickListener(this);
		imgView[1] = (ImageView) findViewById(R.id.adp_deal_image2);
		imgView[1].setOnClickListener(this);
		imgView[2] = (ImageView) findViewById(R.id.adp_deal_image3);
		imgView[2].setOnClickListener(this);
		imgView[3] = (ImageView) findViewById(R.id.adp_deal_image4);
		imgView[3].setOnClickListener(this);

		radioBtns = new RadioButton[4];
		radioBtns[0] = (RadioButton) findViewById(R.id.adp_cover_photo);
		radioBtns[0].setOnClickListener(this);
		radioBtns[0].setChecked(true);
		radioBtns[1] = (RadioButton) findViewById(R.id.adp_cover_photo2);
		radioBtns[1].setOnClickListener(this);
		radioBtns[2] = (RadioButton) findViewById(R.id.adp_cover_photo3);
		radioBtns[2].setOnClickListener(this);
		radioBtns[3] = (RadioButton) findViewById(R.id.adp_cover_photo4);
		radioBtns[3].setOnClickListener(this);

		FrameLayout.LayoutParams vp = new FrameLayout.LayoutParams(getSize(),
				getSize());
		imgView[0].setLayoutParams(vp);
		imgView[1].setLayoutParams(vp);
		imgView[2].setLayoutParams(vp);
		imgView[3].setLayoutParams(vp);

		removeImgView = new ImageView[4];
		removeImgView[0] = (ImageView) findViewById(R.id.adp_deal_image_cross);
		removeImgView[0].setOnClickListener(this);
		removeImgView[1] = (ImageView) findViewById(R.id.adp_deal_image_cross2);
		removeImgView[1].setOnClickListener(this);
		removeImgView[2] = (ImageView) findViewById(R.id.adp_deal_image_cross3);
		removeImgView[2].setOnClickListener(this);
		removeImgView[3] = (ImageView) findViewById(R.id.adp_deal_image_cross4);
		removeImgView[3].setOnClickListener(this);

		mCompanyChooser = (Spinner) findViewById(R.id.adp_company_chooser);
		mCatChooser = (Spinner) findViewById(R.id.adp_category_chooser);
		// mCityDropDown = (MultiSelectSpinner)
		// findViewById(R.id.adp_city_chooser);

		mCityDropDown = (TextView) findViewById(R.id.adp_city_chooser);
		mCityDropDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showmultipleSelect(items, "Select City(s)", false);
			}
		});
		// mLocalityDropDown = (MultiSelectSpinner)
		// findViewById(R.id.adp_locality_chooser);

		mLocalityDropDown = (TextView) findViewById(R.id.adp_locality_chooser);

		mLocalityDropDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(localityItems!=null && localityItems.size()>0)
				{showmultipleSelect(localityItems, "Select Outlet(s)", true);
				}
				else
				{
					showAlertDialog("Please choose city.");
				}
				
			}
		});

		// tododoodododod
		/*
		 * mCityDropDown.setFocusable(true);
		 * mCityDropDown.setFocusableInTouchMode(true);
		 * mCityDropDown.requestFocus();
		 */

		if (mMyDealResp.getCompCategoryList().size() == 2) {
			mIsSingleCompany = true;
			mSingalCompCat = mMyDealResp.getCompCategoryList().get(1);
			mCompanyAdapter = new ArrayAdapter<CompanyCategory>(
					DealPostActivity.this, R.layout.spinner_item,
					mMyDealResp.getCompCategoryList());
			mCompanyChooser.setAdapter(mCompanyAdapter);
			mCompanyChooser.setSelection(1, true);
			mCompanyChooser.setEnabled(false);
			mCategoryAdapter = new ArrayAdapter<CategoryWithCharge>(
					DealPostActivity.this, R.layout.spinner_item,
					mSingalCompCat.getCategoryList());
			mCatChooser.setAdapter(mCategoryAdapter);
			mCatChooser.setSelection(1, true);
			mCatChooser.setEnabled(false);
		} else {
			mCompanyAdapter = new ArrayAdapter<CompanyCategory>(
					DealPostActivity.this, R.layout.spinner_item,
					mMyDealResp.getCompCategoryList());
			mCompanyChooser.setEnabled(true);
			mCompanyChooser.setAdapter(mCompanyAdapter);
			mCompanyChooser
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							mEntireMalaysiaRadioBtn.setChecked(true);
							if (position == 0) {
								mCategorySpinnerContainer
										.setVisibility(View.GONE);
							} else {
								selectedComp = (CompanyCategory) parent
										.getItemAtPosition(position);
								mCategoryAdapter = new ArrayAdapter<CategoryWithCharge>(
										DealPostActivity.this,
										R.layout.spinner_item, selectedComp
												.getCategoryList());
								mCatChooser.setEnabled(true);
								mCatChooser.setAdapter(mCategoryAdapter);
								mCategorySpinnerContainer
										.setVisibility(View.VISIBLE);
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {

						}
					});

			mCatChooser.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					mEntireMalaysiaRadioBtn.setChecked(true);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
		}
		mDealTitle = (EditText) findViewById(R.id.adp_title_text);
		mDealDesc = (EditText) findViewById(R.id.adp_desc_txt);
		mVoucherCount = (EditText) findViewById(R.id.adp_count_voucher);
		mVoucherCode = (EditText) findViewById(R.id.adp_single_code_edit_txt);
		mStartDateBtn = (TextView) findViewById(R.id.adp_startdate_btn);
		mStartDateBtn.setOnClickListener(this);
		mEndDateBtn = (TextView) findViewById(R.id.adp_enddate_btn);
		mEndDateBtn.setOnClickListener(this);
		mDealPostBtn = (TextView) findViewById(R.id.adp_submit);
		mDealPostBtn.setOnClickListener(this);

		// mCityDropDown.setOnItemSelectedListener(new OnItemSelectedListener()
		// {
		// @Override
		// public void onItemSelected(AdapterView<?> adapter, View arg1,
		// int position, long arg3) {
		// // if (position == 0) {
		// // mLocalityChooserSpinnerContainer.setVisibility(View.GONE);
		// // } else {
		//
		// //
		// if(mCityDropDown.getSelectedStrings().size() > 0) {
		// selectedCities = mCityDropDown.getSelectedStrings();
		//
		//
		// PostDealCityLocalityListController clController = new
		// PostDealCityLocalityListController(
		// DealPostActivity.this,
		// Events.POST_DEAL_LOCALITY_LISTING);
		// startSppiner();
		//
		// JSONObject postJsaon = new JSONObject();
		// JSONArray jArray = new JSONArray();
		// try {
		// for (String string : selectedCities) {
		// jArray.put(string);
		// }
		// postJsaon.put("cities", jArray);
		// } catch (JSONException e) {
		// showAlertDialog(getResources().getString(
		// R.string.internal_error));
		// AnalyticsHelper
		// .onError(FlurryEventsConstants.DATA_VALIDATION_ERR,
		// "DealPostActivity : "
		// + AppConstants.DATA_VALIDATION_ERROR_MSG, e);
		// }
		// clController.requestService(postJsaon);
		// }
		//
		//
		//
		//
		// //
		// /* PostDealCityOrLoc city = (PostDealCityOrLoc) adapter
		// .getItemAtPosition(position);
		// PostDealCityLocalityListController clController = new
		// PostDealCityLocalityListController(
		// DealPostActivity.this,
		// Events.POST_DEAL_LOCALITY_LISTING);
		// startSppiner();
		//
		// JSONObject postJsaon = new JSONObject();
		// JSONArray jArray = new JSONArray();
		// try {
		// String namwe = city.getName();
		// jArray.put(namwe);
		// postJsaon.put("cities", jArray);
		// } catch (JSONException e) {
		// showAlertDialog(getResources().getString(
		// R.string.internal_error));
		// AnalyticsHelper
		// .onError(
		// FlurryEventsConstants.DATA_VALIDATION_ERR,
		// "DealPostActivity : "
		// + AppConstants.DATA_VALIDATION_ERROR_MSG,
		// e);
		// }
		// clController.requestService(postJsaon);*/
		// }
		// // }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		//
		// }
		// });

		// mLocalityChooserSpinnerContainer
		// .setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if (mCityDropDown.getSelectedStrings().size() > 0) {
		// selectedCities = mCityDropDown.getSelectedStrings();
		// PostDealCityLocalityListController clController = new
		// PostDealCityLocalityListController(
		// DealPostActivity.this,
		// Events.POST_DEAL_LOCALITY_LISTING);
		// startSppiner();
		//
		// JSONObject postJsaon = new JSONObject();
		// JSONArray jArray = new JSONArray();
		// try {
		// for (String string : selectedCities) {
		// jArray.put(string);
		// }
		// postJsaon.put("cities", jArray);
		// } catch (JSONException e) {
		// showAlertDialog(getResources().getString(
		// R.string.internal_error));
		// AnalyticsHelper
		// .onError(
		// FlurryEventsConstants.DATA_VALIDATION_ERR,
		// "DealPostActivity : "
		// + AppConstants.DATA_VALIDATION_ERROR_MSG,
		// e);
		// }
		// clController.requestService(postJsaon);
		// }
		// }
		// });

//		mLocalityDropDown.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				if (!booleanFirstTime) {
//					booleanFirstTime = true;
//
//					// TODO Auto-generated method stub
//					if (mSelectedItems.size() > 0) {
//						selectedCities = selectedStrings;
//						PostDealCityLocalityListController clController = new PostDealCityLocalityListController(
//								DealPostActivity.this,
//								Events.POST_DEAL_LOCALITY_LISTING);
//						startSppiner();
//
//						JSONObject postJsaon = new JSONObject();
//						JSONArray jArray = new JSONArray();
//						try {
//							for (String string : selectedCities) {
//								jArray.put(string);
//							}
//							postJsaon.put("cities", jArray);
//						} catch (JSONException e) {
//							showAlertDialog(getResources().getString(
//									R.string.internal_error));
//							AnalyticsHelper
//									.onError(
//											FlurryEventsConstants.DATA_VALIDATION_ERR,
//											"DealPostActivity : "
//													+ AppConstants.DATA_VALIDATION_ERROR_MSG,
//											e);
//						}
//						clController.requestService(postJsaon);
//					} else {
//						showAlertDialog("Please select a city.");
//						booleanFirstTime = false;
//					}
//
//				}
//
//				return false;
//			}
//		});
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header_btn_back:
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			this.finish();
			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intentHome = new Intent(DealPostActivity.this,
					HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		case R.id.show_profile_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.SHOW_PROFILE_CLICK);
//			Intent myAccountIntent = new Intent(DealPostActivity.this,
//					MyAccountActivity.class);
//			myAccountIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
//			startActivity(myAccountIntent);
			 onProfileClick();
			break;
			
		case R.id.search_toggler:
			AnalyticsHelper.logEvent(FlurryEventsConstants.HOME_SEARCH_CLICK);
			if (mSearchContainer.getVisibility() == View.VISIBLE) {
				mSearchContainer.setVisibility(View.GONE);
			} else {
				mSearchContainer.setVisibility(View.VISIBLE);
			}
			break;
			
		case R.id.search_icon_button:
			mSearchEditText
					.setText(mSearchEditText.getText().toString().trim());
			performSearch(mSearchEditText.getText().toString());
			break;
		case R.id.adp_submit:
			JSONObject postJson = null;
			try {
				postJson = validateData();
			} catch (JSONException e) {
				showAlertDialog(getResources().getString(
						R.string.internal_error));
				AnalyticsHelper.onError(
						FlurryEventsConstants.DATA_VALIDATION_ERR,
						"DealPostActivity : "
								+ AppConstants.DATA_VALIDATION_ERROR_MSG, e);
			}
			if (postJson == null) {
				return;
			}
			if (imageId != null) {
				try {
					JSONArray array = new JSONArray();
					for (String img : imageId) {
						array.put(img);
					}
					postJson.put("image", array);
				} catch (Throwable e) {
					AnalyticsHelper
							.onError(
									FlurryEventsConstants.IMAGE_SIZE_TOO_LARGE_ERR,
									"DealPostActivity : "
											+ AppConstants.IMAGE_SIZE_TOO_LARGE_ERROR_MSG,
									e);
					return;
				}
			}

			startSppiner();
			PostDealController dealController = new PostDealController(
					DealPostActivity.this, Events.POST_DEAL_EVENT);
			dealController.requestService(postJson);
			break;
		case R.id.adp_startdate_btn:
			showDialog(START_DATE_PICKER_DIALOG);
			break;
		case R.id.adp_enddate_btn:
			showDialog(END_DATE_PICKER_DIALOG);
			break;
		case R.id.adp_deal_image:
			uploadImage();
			break;
		case R.id.adp_deal_image2:
			uploadImage();
			break;
		case R.id.adp_deal_image3:
			uploadImage();
			break;
		case R.id.adp_deal_image4:
			uploadImage();
			break;
		case R.id.adp_cover_photo:
			setCoverImage(0);
			break;
		case R.id.adp_cover_photo2:
			setCoverImage(1);
			break;
		case R.id.adp_cover_photo3:
			setCoverImage(2);
			break;
		case R.id.adp_cover_photo4:
			setCoverImage(3);
			break;
		case R.id.adp_deal_image_cross:
			removeImageIndex = 0;
			showConfirmationDialog(CustomDialog.CONFIRMATION_DIALOG,
					getString(R.string.remove_image_confirmation));
			break;
		case R.id.adp_deal_image_cross2:
			removeImageIndex = 1;
			showConfirmationDialog(CustomDialog.CONFIRMATION_DIALOG,
					getString(R.string.remove_image_confirmation));
			break;
		case R.id.adp_deal_image_cross3:
			removeImageIndex = 2;
			showConfirmationDialog(CustomDialog.CONFIRMATION_DIALOG,
					getString(R.string.remove_image_confirmation));
			break;
		case R.id.adp_deal_image_cross4:
			removeImageIndex = 3;
			showConfirmationDialog(CustomDialog.CONFIRMATION_DIALOG,
					getString(R.string.remove_image_confirmation));
			break;
		default:
			break;
		}

	}

	@Override
	public void onPositiveDialogButton(int id) {
		if (id == CustomDialog.CONFIRMATION_DIALOG) {
			removeImage(removeImageIndex);
		} else {
			super.onPositiveDialogButton(id);
		}
	}

	private JSONObject validateData() throws JSONException {
		String userId = null;
		JSONObject jArray = new JSONObject();
		if (mStore.isLoogedInUser()) {
			userId = mStore.getUserID();
		}
		if (StringUtil.isNullOrEmpty(userId)) {
			showAlertDialog("Please Login again to post a deal.");
			return null;
		} else {
			jArray.put("uid", userId);
		}
		String dealTitleStr = mDealTitle.getText().toString().trim();
		mDealTitle.setText(dealTitleStr);
		if (StringUtil.isNullOrEmpty(dealTitleStr)) {
			showAlertDialog("Please enter Deal Title.");
			return null;
		}
		jArray.put("title", dealTitleStr);

		String dealDescStr = mDealDesc.getText().toString().trim();
		mDealDesc.setText(dealDescStr);
		if (StringUtil.isNullOrEmpty(dealDescStr)) {
			showAlertDialog("Please enter Deal Description.");
			return null;
		}
		jArray.put("description", dealDescStr);

		String compId = null;
		String categoryId = null;
		if (mIsSingleCompany) {
			jArray.put("cid", mSingalCompCat.getCompanyId());
			if (mCatChooser.getSelectedItemPosition() == 0) {
				showAlertDialog("Please choose Category.");
				return null;
			} else {
				CategoryWithCharge catChTemp = (CategoryWithCharge) mCatChooser
						.getSelectedItem();
				categoryId = catChTemp.getCategoryId();
				jArray.put("cat_id", categoryId);
			}
		} else {
			if (mCompanyChooser.getSelectedItemPosition() == 0) {
				showAlertDialog("Please choose Company.");
				return null;
			} else {
				CompanyCategory compCatTemp = (CompanyCategory) mCompanyChooser
						.getSelectedItem();
				compId = compCatTemp.getCompanyId();
				jArray.put("cid", compId);
				if (mCatChooser.getSelectedItemPosition() == 0) {
					showAlertDialog("Please choose category.");
					return null;
				} else {
					CategoryWithCharge catChTemp = (CategoryWithCharge) mCatChooser
							.getSelectedItem();
					categoryId = catChTemp.getCategoryId();
					jArray.put("cat_id", categoryId);
				}
			}
		}

		if (mDealApplicableInGroup.getCheckedRadioButtonId() == R.id.adp_entire_malaysia) {
			jArray.put("applicable_in", "0");
		} else if (mDealApplicableInGroup.getCheckedRadioButtonId() == R.id.adp_in_selective_cities) {
			jArray.put("applicable_in", "1");
			/* String cityId = null; */
			/* String localityId = null; */
			/* if (mCityDropDown.getSelectedItemPosition() == 0) { */
			if (mSelectedItems != null) {
				if (mSelectedItems.size() == 0) {
					showAlertDialog("Please choose City.");
					return null;
				} else {
					/*
					 * PostDealCityOrLoc city = (PostDealCityOrLoc)
					 * mCityDropDown .getSelectedItem();
					 */
					/* cityId = city.getId() + ""; */
					/* jArray.put("city_id", cityId); */
					JSONArray cityArray = new JSONArray();
					for (String string : selectedCities) {
						cityArray.put(string);
					}

					jArray.put("city_id", cityArray);
					selectedLocalities = selectedStrings;
					/* if (mLocalityDropDown.getSelectedItemPosition() == 0) { */
					if (selectedStrings.size() == 0) {
						// showAlertDialog("Please choose Locality.");
						// return null;
					} else {
						/*
						 * PostDealCityOrLoc locality = (PostDealCityOrLoc)
						 * mLocalityDropDown .getSelectedItem();
						 */
						/*
						 * localityId = locality.getId() + "";
						 * jArray.put("locality_id", localityId);
						 */
						JSONArray localityArray = new JSONArray();

						for (String string : selectedLocalities) {

							localityArray.put(string);
						}

						jArray.put("locality_id", localityArray);
					}
					if (imageId.size() > 0) {
						if (checkedIndex >= 0) {
							String coverImage = imageId.get(checkedIndex);
							jArray.put("cover_id", coverImage);
						} else {
							showAlertDialog("Please choose Cover Image");
							return null;
						}
					}
				}
			} else {
				showAlertDialog("Please choose City.");
				return null;
			}
		}

		String startDateStr = mStartDateBtn.getText().toString();
		if (startDateStr.equals(getResources().getString(R.string.date_format))) {
			showAlertDialog("Please select Start Date.");
			return null;
		}
		Calendar calStartDate = Calendar.getInstance(TimeZone
				.getTimeZone("UTC"));
		calStartDate.set(mStartYear, mStartMonth, mStartDay);
		if (calStartDate.getTime().getTime() <= System.currentTimeMillis()) {
			showAlertDialog("Please select a future Start Date.");
			return null;
		}
		jArray.put("start_date", (calStartDate.getTime().getTime() / 1000));

		String endDateStr = mEndDateBtn.getText().toString();
		if (endDateStr.equals(getResources().getString(R.string.date_format))) {
			showAlertDialog("Please Select End Date.");
			return null;
		}
		Calendar calEndDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calEndDate.set(mEndYear, mEndMonth, mEndDay);
		if (calEndDate.getTime().getTime() <= System.currentTimeMillis()) {
			showAlertDialog("Please Select a future End Date.");
			return null;
		}
		// if (!startDateStr
		// .equals(getResources().getString(R.string.date_format))
		// && calStartDate.after(calEndDate)) {
		// showAlertDialog("End Date should not be less than Start Date.");
		// return null;
		// }
		else {
			jArray.put("end_date", (calEndDate.getTime().getTime() / 1000));
		}

		if (mTypeOfDealGroup.getCheckedRadioButtonId() == R.id.adp_voucher) {
			jArray.put("type_of_deal", "1");

			if (mVoucherGroup.getCheckedRadioButtonId() == R.id.adp_specific_count) {
				jArray.put("voc_applicable", "0");
				long voucherCountInt = -1;
				try {
					if (mVoucherCount.getText().toString().equalsIgnoreCase("")) {
						showAlertDialog("Please enter Voucher Count.");
						return null;

					} else {
						voucherCountInt = Long.parseLong(mVoucherCount
								.getText().toString());
					}
				} catch (Exception e) {
					AnalyticsHelper.onError(
							FlurryEventsConstants.VOUCHER_COUNT_ERR,
							"DealPostActivity : "
									+ AppConstants.VOUCHER_COUNT_ERROR_MSG, e);
				}
				if (voucherCountInt == 0) {
					showAlertDialog("Please enter a valid Voucher Count.");
					return null;
				}

				jArray.put("no_of_voc", "" + voucherCountInt);
			} else if (mVoucherGroup.getCheckedRadioButtonId() == R.id.adp_unlimited_codes) {
				jArray.put("voc_applicable", "1");
			}

			if (mVoucherTypeGroup.getCheckedRadioButtonId() == R.id.adp_single_code) {
				jArray.put("voucher_type", "0");
				String voucherCode = mVoucherCode.getText().toString();
				if (!StringUtil.isNullOrEmpty(voucherCode) && voucherCode.length() >= 7 && voucherCode.length() <=10 ) {
					jArray.put("voucher_code", voucherCode);
				} else {
					showAlertDialog("Please enter a valid Voucher Code.");
					return null;
				}
			} else if (mVoucherTypeGroup.getCheckedRadioButtonId() == R.id.adp_system_generated) {
				jArray.put("voucher_type", "1");
			} else if (mVoucherTypeGroup.getCheckedRadioButtonId() == R.id.adp_provided_by_sme) {
				jArray.put("voucher_type", "2");
			}

		} else if (mTypeOfDealGroup.getCheckedRadioButtonId() == R.id.adp_informative) {
			jArray.put("type_of_deal", "0");
		}
		return jArray;
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.POST_DEAL_EVENT) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				setResult(RESULT_OK);
				showFinalDialog("Your Deal is posted successfully.");
			}
		} else if (msg.arg2 == Events.POST_DEAL_CITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
				// mLocalityDropDown.setSelection(0);
			} else {
				booleanFirstTime = false;
				PostDealCityLocListResponse generalListRes = (PostDealCityLocListResponse) msg.obj;
				ArrayList<PostDealCityOrLoc> cityList = generalListRes
						.getCityOrLocalityList();
				// addDefaultSelect(cityList);
				ArrayAdapter<PostDealCityOrLoc> cityListAdp = new ArrayAdapter<PostDealCityOrLoc>(
						DealPostActivity.this, R.layout.spinner_item, cityList);
				/* mCityDropDown.setAdapter(cityListAdp); */

				items = new ArrayList<String>();
				for (PostDealCityOrLoc postDealCityOrLoc : cityList) {
					items.add(postDealCityOrLoc.getName());
				}
				// mCityDropDown.setItems(items);

				mLocalityChooserSpinnerContainer.setVisibility(View.VISIBLE);
			}
		} else if (msg.arg2 == Events.POST_DEAL_LOCALITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
//				showInfoDialog((String) msg.obj);
				// mLocalityDropDown.setSelection(0);
				mLocalityChooserSpinnerContainer.setVisibility(View.GONE);

			} else {
				PostDealCityLocListResponse glistRes = (PostDealCityLocListResponse) msg.obj;
				ArrayList<PostDealCityOrLoc> localityList = glistRes
						.getCityOrLocalityList();
				Log.e("gg", "listSiz+" + localityList.size());
				// addDefaultSelect(localityList);
				ArrayAdapter<PostDealCityOrLoc> localityAdp = new ArrayAdapter<PostDealCityOrLoc>(
						DealPostActivity.this, R.layout.spinner_item,
						localityList);
				// mLocalityDropDown.setAdapter(localityAdp);
				localityItems = new ArrayList<String>();
				for (PostDealCityOrLoc postDealCityOrLoc : localityList) {
					localityItems.add(postDealCityOrLoc.getName());
				}

				// mLocalityDropDown.setItems(localityItems);
				mLocalityChooserSpinnerContainer.setVisibility(View.VISIBLE);
			}
		} else if (msg.arg2 == Events.POST_IMAGE_EVENT) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				ImageDataResponse response = (ImageDataResponse) msg.obj;
				imageId.add(response.getImageId());
				setImageView(mBitmap);
				showInfoDialog("Image has been successfully saved.");
				// Toast.makeText(getApplicationContext(),
				// "Image has been successfully saved.",
				// Toast.LENGTH_LONG).show();
			}
		} else if (msg.arg2 == Events.REMOVE_IMAGE_EVENT) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				/*
				 * MaxisResponse response = (MaxisResponse) msg.obj;
				 * removeImage(removeImageIndex);
				 */// resetImageView(mBitmap);
				replaceImageView(removeImageIndex);
				// removeImgView[removeImageIndex].setVisibility(View.GONE);
				// imgView[removeImageIndex].setPadding(10, 10, 10, 10);
				imageId.remove(removeImageIndex);
				showInfoDialog("Image has been successfully removed.");
				// Toast.makeText(getApplicationContext(),
				// "Image has been successfully removed.",
				// Toast.LENGTH_LONG).show();
			}
		}
	}

	/*
	 * private void addDefaultSelect( ArrayList<PostDealCityOrLoc>
	 * cityOrLocalityList) { if (cityOrLocalityList != null) { PostDealCityOrLoc
	 * temp = new PostDealCityOrLoc(); temp.setName("Select");
	 * cityOrLocalityList.add(0, temp); } }
	 */

	/*
	 * private void addDefaultSelect( List<String> selectedCities
	 * cityOrLocalityList { if (cityOrLocalityList != null) { PostDealCityOrLoc
	 * temp = new PostDealCityOrLoc(); temp.setName("Select");
	 * cityOrLocalityList.add(0, temp); } }
	 */
	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.POST_DEAL_EVENT) {
			handler.sendMessage((Message) screenData);
		} else if (event == Events.POST_DEAL_CITY_LISTING) {
			handler.sendMessage((Message) screenData);
		} else if (event == Events.POST_DEAL_LOCALITY_LISTING) {
			handler.sendMessage((Message) screenData);
		} else if (event == Events.POST_IMAGE_EVENT) {
			handler.sendMessage((Message) screenData);
		} else if (event == Events.REMOVE_IMAGE_EVENT) {
			handler.sendMessage((Message) screenData);
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == START_DATE_PICKER_DIALOG) {
			return new DatePickerDialog(DealPostActivity.this,
					startDatePickerListener, mStartYear, mStartMonth, mStartDay);
		} else if (id == END_DATE_PICKER_DIALOG) {
			return new DatePickerDialog(DealPostActivity.this,
					endDatePickerListener, mEndYear, mEndMonth, mEndDay);
		} else
			return super.onCreateDialog(id);
	}

	private DatePickerDialog.OnDateSetListener startDatePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			mStartYear = selectedYear;
			mStartMonth = selectedMonth;
			mStartDay = selectedDay;

			// set selected date into text view
			mStartDateBtn.setText(new StringBuilder().append(mStartYear)
					.append("-").append(mStartMonth + 1).append("-")
					.append(mStartDay).append(" "));
		}
	};

	private DatePickerDialog.OnDateSetListener endDatePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			mEndYear = selectedYear;
			mEndMonth = selectedMonth;
			mEndDay = selectedDay;

			// set selected date into text view
			mEndDateBtn.setText(new StringBuilder().append(mEndYear)
					.append("-").append(mEndMonth + 1).append("-")
					.append(mEndDay).append(" "));
		}
	};
	private ArrayList<Integer> mSelectedItems;

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == IMAGE_PICK) {
			if (data == null || data.getData() == null)
				return;
			Uri selectedImage = data.getData();
			String imagePath = getRealPathFromURI(selectedImage);
			InputStream imageStream = null;
			try {
				imageStream = getContentResolver().openInputStream(
						selectedImage);
			} catch (FileNotFoundException e) {
				AnalyticsHelper
						.onError(
								FlurryEventsConstants.POST_DEAL_UPLOAD_IMG_NOT_FOUND_ERR,
								"DealPostActivity : "
										+ AppConstants.POST_DEAL_UPLOAD_IMG_NOT_FOUND_ERROR_MSG,
								e);
				return;
			}
			mBitmap = BitmapCalculation.decodeSampledBitmapFromPath(imagePath,
					getSize(), (int) (getSize() * (1.3)));

			// TODO:: SET ORIENTATION
			Bitmap newBitmap = null;
			if(mBitmap!=null)
			{
			 newBitmap = setOrientation(mBitmap,imagePath);
			}
			// BitmapFactory.Options options = new BitmapFactory.Options() ;
			// options.inPurgeable = true;
			// mBitmap = BitmapFactory.decodeStream(imageStream);

			if (newBitmap != null) {

				uploadImagetoServer(newBitmap);

			}
			// System.out.println(getBase64Image());
			// mImagePath = getFileNameByUri(selectedImage);
			// System.out.println("file name by URI"+mImagePath);
			// mImagePath=getRealPathFromURI(selectedImage);
			// System.out.println("real file path by URI"+mImagePath);
			// dealImageV.setImageDrawable(fetchImageFromGallery());
		} /*
		 * else if (requestCode == IMAGE_REMOVE) { if (data == null ||
		 * data.getData() == null) return; removeImageFromServer(mBitmap); }
		 */
	};

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public String getFileNameByUri(Uri uri) {
		String fileName = "unknown"; // default fileName
		Uri filePathUri = uri;
		if (uri.getScheme().toString().compareTo("content") == 0) {
			Cursor cursor = getContentResolver().query(uri, null, null, null,
					null);
			if (cursor.moveToFirst()) {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				filePathUri = Uri.parse(cursor.getString(column_index));
				fileName = filePathUri.getLastPathSegment().toString();
			}
		} else if (uri.getScheme().compareTo("file") == 0) {
			fileName = filePathUri.getLastPathSegment().toString();
		} else {
			fileName = fileName + "_" + filePathUri.getLastPathSegment();
		}
		return fileName;
	}

	/*
	 * private Drawable fetchImageFromGallery() { File imgFile = new
	 * File(mImagePath); if (imgFile.exists()) { Display display =
	 * getWindowManager().getDefaultDisplay(); int width = display.getWidth();
	 * // deprecated int height = display.getHeight(); Bitmap myBitmap =
	 * BitmapCalculation.decodeSampledBitmapFromPath( imgFile.getAbsolutePath(),
	 * width, height); mBitmap = myBitmap; //
	 * Log.e("Image Size",(sizeOf(myBitmap)/1024)+"");
	 * 
	 * ByteArrayOutputStream stream = new ByteArrayOutputStream();
	 * myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	 * 
	 * byte[] imageInByte = stream.toByteArray(); long lengthbmp =
	 * imageInByte.length;
	 * 
	 * // Log.e("***Image Size",(sizeOf(myBitmap)/1024)+"");
	 * 
	 * return new BitmapDrawable(getResources(), myBitmap); } return null; }
	 */
	public void uploadImage() {
		if (imageId.size() < 4) {

			Intent imagePicker = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			imagePicker.setType("image/*");
			startActivityForResult(imagePicker, IMAGE_PICK);
		} else {
			Toast.makeText(getApplicationContext(),
					"Please select an image to Delete", Toast.LENGTH_LONG)
					.show();
		}
	}

	public int getSize() {
		Display mDisplay = this.getWindowManager().getDefaultDisplay();
		int width = mDisplay.getWidth();
		width = width - 30;
		width = width / 2;
		return width;
	}

	public void setImageView(Bitmap bitmap) {
		BitmapDrawable drawable = new BitmapDrawable(
				imgView[imageId.size() - 1].getResources(), bitmap);
		imgView[imageId.size() - 1].setImageDrawable(drawable);
		FrameLayout.LayoutParams vp = new FrameLayout.LayoutParams(getSize(),
				getSize());
		imgView[imageId.size() - 1].setLayoutParams(vp);
		imgView[imageId.size() - 1].setPadding(10, 10, 10, 10);
		removeImgView[imageId.size() - 1].setVisibility(View.VISIBLE);
		radioBtns[imageId.size() - 1].setVisibility(View.VISIBLE);
	}

	public void removeImage(int imageIndex) {
		String imageNameForRemoval = null;
		switch (imageIndex) {
		case 0:
			imageNameForRemoval = imageId.get(0);
			break;
		case 1:
			imageNameForRemoval = imageId.get(1);
			break;
		case 2:
			imageNameForRemoval = imageId.get(2);
			break;
		case 3:
			imageNameForRemoval = imageId.get(3);
			break;
		default:
			break;
		}

		if (!StringUtil.isNullOrEmpty(imageNameForRemoval)) {
			RemovePostDealImgController controller = new RemovePostDealImgController(
					DealPostActivity.this, Events.REMOVE_IMAGE_EVENT);
			controller.requestService(imageNameForRemoval);
			startSppiner();
		}
	}

	public void replaceImageView(int index) {

		if (index != 3) {
			for (int i = index; i < 3; i++) {
				imgView[i].setImageDrawable(imgView[i + 1].getDrawable());
				// removeImgView[i].setImageDrawable(removeImgView[i +
				// 1].getDrawable());
				removeImgView[i].setVisibility(removeImgView[i + 1]
						.getVisibility());
				// radioBtns[i].setChecked(false);
				radioBtns[i].setVisibility(radioBtns[i + 1].getVisibility());

				// imgView[i].setPadding(0, 0, 0, 0);
				/* removeImgView[i].setVisibility(View.GONE); */
			}
			// imgView[3].setBackgroundResource(R.drawable.upload_photo_icon);
			imgView[3].setImageResource(R.drawable.upload_photo_icon);

			imgView[3].setPadding(0, 0, 0, 0);
			removeImgView[3].setVisibility(View.GONE);
		} else {
			imgView[3].setImageResource(R.drawable.upload_photo_icon);
			imgView[3].setPadding(0, 0, 0, 0);
			removeImgView[3].setVisibility(View.GONE);
			radioBtns[3].setChecked(false);
			radioBtns[3].setVisibility(View.INVISIBLE);
		}

		setCoverImageCheck(index);
	}

	public void uploadImagetoServer(Bitmap bitmap) {
		JSONObject jArray = new JSONObject();
		JSONObject postJson = new JSONObject();
		try {
			String base64Img = UiUtils.getBase64Image(bitmap);

			postJson.put("image", base64Img);
			jArray.put("Deal", postJson);
		} catch (Throwable e) {
			e.printStackTrace();
			showInfoDialog("Image size too large to upload");
			AnalyticsHelper.onError(
					FlurryEventsConstants.IMAGE_SIZE_TOO_LARGE_ERR,
					"DealPostActivity : "
							+ AppConstants.IMAGE_SIZE_TOO_LARGE_ERROR_MSG, e);
			return;
		}

		PostImageController imageController = new PostImageController(
				DealPostActivity.this, Events.POST_IMAGE_EVENT);
		imageController.requestService(jArray);
		startSppiner();

	}

	public Bitmap setOrientation(Bitmap sourceBitmap, String photo) {
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(photo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
				ExifInterface.ORIENTATION_NORMAL);
		Log.e("Exif Orientation", "oreination" + orientation);
		Matrix matrix = new Matrix();
		switch (orientation) {
		case ExifInterface.ORIENTATION_ROTATE_90:
			matrix.postRotate(90);
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			matrix.postRotate(180);
			break;
		case ExifInterface.ORIENTATION_ROTATE_270:
			matrix.postRotate(270);
			break;
		}

		Bitmap originalImage = Bitmap
				.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(),
						sourceBitmap.getHeight(), matrix, true);

		return originalImage;
	}

	public void setCoverImageCheck(int index) {
		if (index != 0) {
			if (index == checkedIndex) {
				radioBtns[index].setChecked(false);
				radioBtns[0].setChecked(true);
				checkedIndex = 0;
			} else if (index < checkedIndex) {
				radioBtns[checkedIndex].setChecked(false);
				radioBtns[checkedIndex - 1].setChecked(true);
				checkedIndex = checkedIndex - 1;
			}
		} else {
			if (imageId.size() > 0) {
				radioBtns[0].setChecked(true);
				checkedIndex = 0;
			} else {
				setCoverImage(-1);
			}
		}
	}

	public void setCoverImage(int index) {
		for (int i = 0; i < 4; i++) {
			if (i == index) {
				radioBtns[i].setChecked(true);
				checkedIndex = i;
			} else {
				radioBtns[i].setChecked(false);
				// checkedIndex = index;
			}
		}
	}

	public void showmultipleSelect(final ArrayList<String> totalItems,
			String header, final boolean isLocality) {
		mSelectedItems = new ArrayList<Integer>();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		CharSequence[] charSequences = new CharSequence[totalItems.size()];
		for (int i = 0; i < totalItems.size(); i++) {
			charSequences[i] = totalItems.get(i);
		}
		
		
		// Set the dialog title
		builder.setTitle(header)
				// Specify the list array, the items to be selected by default
				// (null for none),
				// and the listener through which to receive callbacks when
				// items are selected

				.setMultiChoiceItems(charSequences, null,
						new DialogInterface.OnMultiChoiceClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which, boolean isChecked) {
								if (isChecked) {
									// If the user checked the item, add it to
									// the selected items
									mSelectedItems.add(which);
								} else if (mSelectedItems.contains(which)) {
									// Else, if the item is already in the
									// array, remove it
									mSelectedItems.remove(Integer
											.valueOf(which));
								}
							}
						})
				// Set the action buttons
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								selectedStrings = new ArrayList<String>();
								StringBuffer selectedData = new StringBuffer();
								for (int i = 0; i < mSelectedItems.size(); i++) {
									selectedStrings.add(totalItems.get(i));
									selectedData.append(totalItems.get(i));
									if (i < mSelectedItems.size()-1) {
										selectedData.append(", ");
									}
								}
								if (!isLocality) {
									
									mCityDropDown.setText(selectedData.toString());
								getLocality();
								} else {
									mLocalityDropDown.setText(selectedData.toString());
								}
							}

						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
							}
						});

		builder.create();
		builder.show();
	}
	
	public void getLocality()
	{
		if (mSelectedItems.size() > 0) {
			selectedCities = selectedStrings;
			PostDealCityLocalityListController clController = new PostDealCityLocalityListController(
					DealPostActivity.this,
					Events.POST_DEAL_LOCALITY_LISTING);
			startSppiner();

			JSONObject postJsaon = new JSONObject();
			JSONArray jArray = new JSONArray();
			try {
				for (String string : selectedCities) {
					jArray.put(string);
				}
				postJsaon.put("cities", jArray);
			} catch (JSONException e) {
				showAlertDialog(getResources()
						.getString(
								R.string.internal_error));
				AnalyticsHelper
						.onError(
								FlurryEventsConstants.DATA_VALIDATION_ERR,
								"DealPostActivity : "
										+ AppConstants.DATA_VALIDATION_ERROR_MSG,
								e);
			}
			clController.requestService(postJsaon);
		} else {
			showAlertDialog("Please select a city.");
		}
	}

}
