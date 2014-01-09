package com.kelltontech.maxisgetit.ui.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.R.drawable;
import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.CityAreaListController;
import com.kelltontech.maxisgetit.controllers.PostDealController;
import com.kelltontech.maxisgetit.controllers.PostImageController;
import com.kelltontech.maxisgetit.dao.CategoryWithCharge;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.CompanyCategory;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.db.CityTable;
import com.kelltontech.maxisgetit.response.DealsListResponse;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.response.ImageDataResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;
import com.kelltontech.maxisgetit.utils.BitmapCalculation;
import com.kelltontech.maxisgetit.utils.UiUtils;

public class DealPostActivity extends MaxisMainActivity {
	public static final int END_DATE_PICKER_DIALOG = 1123;
	public static final int REDEEM_DATE_PICKER_DIALOG = 1124;
	private static final int IMAGE_PICK = 1001;

	private ImageView mHomeIconView, mProfileIconView;
	private DealsListResponse mMyDealResp;
	private ArrayAdapter<CategoryWithCharge> mCategoryAdapter;
	private TextView mDealPostBtn, mEndDateBtn, mRedeemDateBtn;
	private Spinner mCatChooser, mCompanyChooser;
	private LinearLayout mCategorySpinnerContainer, mLocalityContainer;
	private EditText mDealTitle, mDealDesc, mVoucherCount, mRedeemPrice;
	// private ImageView mDealImageV, mDealImageV2, mDealImageV3, mDealImageV4;
	private ImageView imgView[];
	private MaxisStore mStore;
	private String mImagePath;
	private Bitmap mBitmap;
	// private DatePicker endDate, voucherRedeemDate;
	private LinearLayout mVoucherDetailContainer;
	private RadioGroup mVoucherApplicableGroup;
	private Spinner mCityDropDown;
	private Spinner mLocalityDropDown;
	private int mYear1;
	private int mMonth1;
	private int mDay1;
	private int mYear2;
	private int mMonth2;
	private int mDay2;
	private boolean mIsSingalCompany;
	private CompanyCategory mSingalCompCat;

	ArrayList<String> imageNames = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deal_post);
		UiUtils.hideKeyboardOnTappingOutside(
				findViewById(R.id.adp_root_layout), this);
		mStore = MaxisStore.getStore(DealPostActivity.this);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		final Calendar c = Calendar.getInstance();
		mYear1 = mYear2 = c.get(Calendar.YEAR);
		mMonth1 = mMonth2 = c.get(Calendar.MONTH);
		mDay1 = mDay2 = c.get(Calendar.DAY_OF_MONTH);
		mLocalityContainer = (LinearLayout) findViewById(R.id.locality_chooser_container);
		mCategorySpinnerContainer = (LinearLayout) findViewById(R.id.adp_category_container);
		mVoucherDetailContainer = (LinearLayout) findViewById(R.id.adp_voucher_detail_container);
		mVoucherApplicableGroup = (RadioGroup) findViewById(R.id.adp_voucher_applicable_RG);
		mVoucherApplicableGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == R.id.adp_voucher_not_applicable) {
							mVoucherDetailContainer.setVisibility(View.GONE);
						} else if (checkedId == R.id.adp_voucher_applicable) {
							mVoucherDetailContainer.setVisibility(View.VISIBLE);
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

		FrameLayout.LayoutParams vp = new FrameLayout.LayoutParams(getSize(),
				getSize());
		imgView[0].setLayoutParams(vp);
		// mDealImageV.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imgView[1].setLayoutParams(vp);
		imgView[2].setLayoutParams(vp);
		imgView[3].setLayoutParams(vp);

		mCompanyChooser = (Spinner) findViewById(R.id.adp_company_chooser);
		mCatChooser = (Spinner) findViewById(R.id.adp_category_chooser);
		mCityDropDown = (Spinner) findViewById(R.id.adp_city_chooser);
		mLocalityDropDown = (Spinner) findViewById(R.id.adp_locality_chooser);
		// cat_chooser.setVisibility(View.GONE);
		mCategorySpinnerContainer.setVisibility(View.GONE);
		// catChooser.setEnabled(false);
		if (mMyDealResp.getCompCategoryList().size() == 2) {
			mIsSingalCompany = true;
			mSingalCompCat = mMyDealResp.getCompCategoryList().get(1);
			findViewById(R.id.adp_comp_chooser_spinner_container)
					.setVisibility(View.GONE);
			mCategoryAdapter = new ArrayAdapter<CategoryWithCharge>(
					DealPostActivity.this, R.layout.spinner_item,
					mSingalCompCat.getCategoryList());
			mCatChooser.setAdapter(mCategoryAdapter);
			mCategorySpinnerContainer.setVisibility(View.VISIBLE);
		} else {
			ArrayAdapter<CompanyCategory> compCatAdp = new ArrayAdapter<CompanyCategory>(
					DealPostActivity.this, R.layout.spinner_item,
					mMyDealResp.getCompCategoryList());
			mCompanyChooser.setAdapter(compCatAdp);
			mCompanyChooser
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							if (position == 0) {
								// catChooser.setEnabled(false);
								mCategorySpinnerContainer
										.setVisibility(View.GONE);
							} else {
								CompanyCategory selectedComp = (CompanyCategory) parent
										.getItemAtPosition(position);
								mCategoryAdapter = new ArrayAdapter<CategoryWithCharge>(
										DealPostActivity.this,
										R.layout.spinner_item, selectedComp
												.getCategoryList());
								mCatChooser.setAdapter(mCategoryAdapter);
								mCategorySpinnerContainer
										.setVisibility(View.VISIBLE);
								// catChooser.setEnabled(true);//
								// Visibility(View.VISIBLE);
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {

						}
					});
		}
		mDealTitle = (EditText) findViewById(R.id.adp_title_text);
		mDealDesc = (EditText) findViewById(R.id.adp_desc_txt);
		mVoucherCount = (EditText) findViewById(R.id.adp_count_voucher);
		mRedeemPrice = (EditText) findViewById(R.id.adp_voucher_redeem_price);
		mEndDateBtn = (TextView) findViewById(R.id.adp_enddate_btn);
		mEndDateBtn.setOnClickListener(this);
		mRedeemDateBtn = (TextView) findViewById(R.id.adp_voucher_redeem_dt_btn);
		mRedeemDateBtn.setOnClickListener(this);
		// captcha = (EditText) findViewById(R.id.adp_captcha_txt);
		mDealPostBtn = (TextView) findViewById(R.id.adp_submit);
		mDealPostBtn.setOnClickListener(this);
		CityTable cityTable = new CityTable((MyApplication) getApplication());
		ArrayList<CityOrLocality> cityList = cityTable.getAllCitiesList();
		addDefaultSelect(cityList);
		ArrayAdapter<CityOrLocality> cityAdp = new ArrayAdapter<CityOrLocality>(
				DealPostActivity.this, R.layout.spinner_item, cityList);
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
							DealPostActivity.this, Events.LOCALITY_LISTING);
					startSppiner();
					clController.requestService(city.getId() + "");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
			Intent myAccountIntent = new Intent(DealPostActivity.this,
					MyAccountActivity.class);
			myAccountIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(myAccountIntent);
			// onProfileClick();
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
			if (imageNames != null) {
				try {
					JSONArray array = new JSONArray();
					for (String img : imageNames) {
						array.put(img);
					}
					postJson.put("image", array);
				} catch (Throwable e) {
					// showInfoDialog("Image size too large to upload");
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
		case R.id.adp_enddate_btn:
			showDialog(END_DATE_PICKER_DIALOG);
			break;
		case R.id.adp_voucher_redeem_dt_btn:
			showDialog(REDEEM_DATE_PICKER_DIALOG);
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

		default:
			break;
		}

	}

	private JSONObject validateData() throws JSONException {
		String userId = null;
		JSONObject jArray = new JSONObject();
		if (mStore.isLoogedInUser()) {
			userId = mStore.getUserID();
		}
		if (StringUtil.isNullOrEmpty(userId)) {
			showAlertDialog("Please Login again to post a deal");
			return null;
		} else {
			jArray.put("uid", userId);
		}
		String dealTitleStr = mDealTitle.getText().toString().trim();
		mDealTitle.setText(dealTitleStr);
		if (StringUtil.isNullOrEmpty(dealTitleStr)) {
			showAlertDialog("Please Enter the Deal title");
			return null;
		}
		jArray.put("title", dealTitleStr);
		String compId = null;
		String categoryId = null;
		if (mIsSingalCompany) {
			jArray.put("cid", mSingalCompCat.getCompanyId());
			if (mCatChooser.getSelectedItemPosition() == 0) {
				showAlertDialog("Please select a category");
				return null;
			} else {
				CategoryWithCharge catChTemp = (CategoryWithCharge) mCatChooser
						.getSelectedItem();
				categoryId = catChTemp.getCategoryId();
				jArray.put("cat_id", categoryId);
			}
		} else {
			if (mCompanyChooser.getSelectedItemPosition() == 0) {
				showAlertDialog("Please select a company");
				return null;
			} else {
				CompanyCategory compCatTemp = (CompanyCategory) mCompanyChooser
						.getSelectedItem();
				compId = compCatTemp.getCompanyId();
				jArray.put("cid", compId);
				if (mCatChooser.getSelectedItemPosition() == 0) {
					showAlertDialog("Please select a category");
					return null;
				} else {
					CategoryWithCharge catChTemp = (CategoryWithCharge) mCatChooser
							.getSelectedItem();
					categoryId = catChTemp.getCategoryId();
					jArray.put("cat_id", categoryId);
				}
			}
		}
		String cityId = null;
		String localityId = null;
		if (mCityDropDown.getSelectedItemPosition() == 0) {
			showAlertDialog("Please select a City");
			return null;
		} else {
			CityOrLocality city = (CityOrLocality) mCityDropDown
					.getSelectedItem();
			cityId = city.getId() + "";
			jArray.put("city_id", cityId);
			if (mLocalityDropDown.getSelectedItemPosition() == 0) {
				showAlertDialog("Please select a Locality");
				return null;
			} else {
				CityOrLocality locality = (CityOrLocality) mLocalityDropDown
						.getSelectedItem();
				localityId = locality.getId() + "";
				jArray.put("locality_id", localityId);
			}
		}
		String dealDescStr = mDealDesc.getText().toString().trim();
		mDealDesc.setText(dealDescStr);
		if (StringUtil.isNullOrEmpty(dealDescStr)) {
			showAlertDialog("Please Enter the Deal Description");
			return null;
		}
		jArray.put("description", dealDescStr);
		String endDateStr = mEndDateBtn.getText().toString();
		if (endDateStr.equals(getResources().getString(R.string.date_format))) {
			showAlertDialog("Please Select End Date");
			return null;
		}
		Calendar calEndDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calEndDate.set(mYear1, mMonth1, mDay1);
		if (calEndDate.getTime().getTime() < System.currentTimeMillis()) {
			showAlertDialog("Please Select a future End Date");
			return null;
		}

		// Date endDt = new Date(year1, month1, day1);
		jArray.put("end_date", (calEndDate.getTime().getTime() / 1000));
		if (mVoucherApplicableGroup.getCheckedRadioButtonId() == R.id.adp_voucher_applicable) {
			jArray.put("voc_applicable", "1");
			long voucherCountInt = 0;
			try {
				voucherCountInt = Long.parseLong(mVoucherCount.getText()
						.toString());
			} catch (Exception e) {
				AnalyticsHelper.onError(
						FlurryEventsConstants.VOUCHER_COUNT_ERR,
						"DealPostActivity : "
								+ AppConstants.VOUCHER_COUNT_ERROR_MSG, e);
			}
			if (voucherCountInt == 0) {
				showAlertDialog("Please Enter a valid Voucher Count");
				return null;
			}
			jArray.put("no_of_voc", "" + voucherCountInt);
			String redeemDateStr = mRedeemDateBtn.getText().toString();
			if (redeemDateStr.equals(getResources().getString(
					R.string.date_format))) {
				showAlertDialog("Please Select Voucher Redemption Date");
				return null;
			}
			Calendar calRedeemdDt = Calendar.getInstance(TimeZone
					.getTimeZone("UTC"));
			calRedeemdDt.set(mYear2, mMonth2, mDay2);
			if (calRedeemdDt.compareTo(calEndDate) < 0) {
				showAlertDialog("You can't select the Voucher Redemption Date prior to End Date, Please verify End Date and Voucher Redemption Date");
				return null;
			}
			// Date redeemdDt = new Date(year2, month2, day2);
			jArray.put("voc_redem_date", ""
					+ (calRedeemdDt.getTime().getTime() / 1000));
			double redeemPriceVal = 0;
			try {
				redeemPriceVal = Double.parseDouble(mRedeemPrice.getText()
						.toString());
			} catch (Exception e) {
				AnalyticsHelper.onError(FlurryEventsConstants.REDEEM_PRICE_ERR,
						"DealPostActivity : "
								+ AppConstants.REDEEM_PRICE_ERROR_MSG, e);
			}
			if (redeemPriceVal == 0) {
				showAlertDialog("Please Enter valid Voucher Price");
				return null;
			}
			jArray.put("voc_redem_price", mRedeemPrice.getText().toString());
		} else {
			jArray.put("voc_applicable", "0");
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
				showFinalDialog("your Deal has successfully be posted");
			}
		} else if (msg.arg2 == Events.LOCALITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
				mLocalityDropDown.setSelection(0);
				mLocalityContainer.setVisibility(View.GONE);
			} else {
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				ArrayList<CityOrLocality> localityList = glistRes
						.getCityOrLocalityList();
				addDefaultSelect(localityList);
				ArrayAdapter<CityOrLocality> localityAdp = new ArrayAdapter<CityOrLocality>(
						DealPostActivity.this, R.layout.spinner_item,
						localityList);
				mLocalityDropDown.setAdapter(localityAdp);
				mLocalityContainer.setVisibility(View.VISIBLE);
			}
		}

		else if (msg.arg2 == Events.POST_IMAGE_EVENT) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				ImageDataResponse response = (ImageDataResponse) msg.obj;

				imageNames.add(response.getImagename());
				setImageView(mBitmap);
				Toast.makeText(getApplicationContext(),
						"uploaded successfully " + response.getImagename(),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private void addDefaultSelect(ArrayList<CityOrLocality> cityOrLocalityList) {
		if (cityOrLocalityList != null) {
			CityOrLocality temp = new CityOrLocality();
			temp.setId(-1);
			temp.setName("Select");
			cityOrLocalityList.add(0, temp);
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.POST_DEAL_EVENT) {
			handler.sendMessage((Message) screenData);
		} else if (event == Events.LOCALITY_LISTING) {
			handler.sendMessage((Message) screenData);
		} else if (event == Events.POST_IMAGE_EVENT) {
			handler.sendMessage((Message) screenData);
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == END_DATE_PICKER_DIALOG) {
			return new DatePickerDialog(DealPostActivity.this,
					datePickerListener, mYear1, mMonth1, mDay1);
		} else if (id == REDEEM_DATE_PICKER_DIALOG) {
			return new DatePickerDialog(DealPostActivity.this,
					redeemDatePickerListener, mYear2, mMonth2, mDay2);
		} else
			return super.onCreateDialog(id);
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			mYear1 = selectedYear;
			mMonth1 = selectedMonth;
			mDay1 = selectedDay;

			// set selected date into textview
			mEndDateBtn.setText(new StringBuilder().append(mMonth1 + 1)
					.append("-").append(mDay1).append("-").append(mYear1)
					.append(" "));
		}
	};
	private DatePickerDialog.OnDateSetListener redeemDatePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			mYear2 = selectedYear;
			mMonth2 = selectedMonth;
			mDay2 = selectedDay;
			// set selected date into textview
			mRedeemDateBtn.setText(new StringBuilder().append(mMonth2 + 1)
					.append("-").append(mDay2).append("-").append(mYear2)
					.append(" "));
		}
	};

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
			// BitmapFactory.Options options = new BitmapFactory.Options() ;
			// options.inPurgeable = true;
			// mBitmap = BitmapFactory.decodeStream(imageStream);

			if (mBitmap != null) {

				uploadImagetoServer(mBitmap);

			}
			// System.out.println(getBase64Image());
			// mImagePath = getFileNameByUri(selectedImage);
			// System.out.println("file name by URI"+mImagePath);
			// mImagePath=getRealPathFromURI(selectedImage);
			// System.out.println("real file path by URI"+mImagePath);
			// dealImageV.setImageDrawable(fetchImageFromGallery());
		}
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
		String fileName = "unknown";// default fileName
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

	private Drawable fetchImageFromGallery() {
		File imgFile = new File(mImagePath);
		if (imgFile.exists()) {
			Display display = getWindowManager().getDefaultDisplay();
			int width = display.getWidth(); // deprecated
			int height = display.getHeight();
			Bitmap myBitmap = BitmapCalculation.decodeSampledBitmapFromPath(
					imgFile.getAbsolutePath(), width, height);
			mBitmap = myBitmap;
			// Log.e("Image Size",(sizeOf(myBitmap)/1024)+"");

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			/*
			 * byte[] imageInByte = stream.toByteArray(); long lengthbmp =
			 * imageInByte.length;
			 */
			// Log.e("***Image Size",(sizeOf(myBitmap)/1024)+"");

			return new BitmapDrawable(getResources(), myBitmap);
		}
		return null;
	}

	public void uploadImage() {

		if (imageNames.size() < 4) {
			Intent imagePicker = new Intent(Intent.ACTION_GET_CONTENT);
			imagePicker.setType("image/*");
			startActivityForResult(imagePicker, IMAGE_PICK);
		} else {
			Toast.makeText(getApplicationContext(),
					"Please select a image to Delete", Toast.LENGTH_LONG)
					.show();
		}
	}

	public int getSize() {
		Display mDisplay = this.getWindowManager().getDefaultDisplay();
		int width = mDisplay.getWidth();
		int height = mDisplay.getHeight();
		width = width - 30;
		width = width / 2;
		// height = (int) (width * (1.3));

		return width;
	}

	public void setImageView(Bitmap bitmap) {
		BitmapDrawable drawable = new BitmapDrawable(
				imgView[imageNames.size() - 1].getResources(), bitmap);
		imgView[imageNames.size() - 1].setImageDrawable(drawable);
		FrameLayout.LayoutParams vp = new FrameLayout.LayoutParams(getSize(),
				getSize());
		imgView[imageNames.size() - 1].setLayoutParams(vp);
		imgView[imageNames.size() - 1].setPadding(5, 5, 5, 5);

	}

	public void replaceImageView(int index) {
		if (index != 3) {
			for (int i = index; i < 3; i++) {
				imgView[i].setImageDrawable(imgView[i + 1].getDrawable());

			}
			imgView[3].setBackgroundResource(R.drawable.upload_photo_icon);
		}
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
}
