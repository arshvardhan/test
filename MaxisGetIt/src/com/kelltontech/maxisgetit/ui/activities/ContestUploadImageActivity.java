package com.kelltontech.maxisgetit.ui.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kelltontech.framework.model.MyError;
import com.kelltontech.framework.utils.StorageUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.ContestUploadImageController;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.model.uploadImage.RequestUploadPhoto;
import com.kelltontech.maxisgetit.model.uploadImage.ResponseUploadPhoto;
import com.kelltontech.maxisgetit.service.AppSharedPreference;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;
import com.kelltontech.maxisgetit.utils.BitmapCalculation;

/**
 * This screen is shown after taking image from camera to upload it on server.
 */
public class ContestUploadImageActivity extends ContestBaseActivity {
	//	private static final int CAMERA_REQUEST = 1888;
	// private double mLatitude,mLongitude,mLatitudeN,mLongitudeN;
	private ExifInterface mExifInterface;
	private String mImagePath;
	private Button mUploadBtn;
	private Bitmap mBitmap;
	private int mCid;
	private String mCompanyName;
	private int mCategoryId;
	private RequestUploadPhoto mRequestUploadPhoto;
	private boolean isFetchingBase64;
	private EditText mTitleEditTxt, mNameEditTxt, mNumberEditTxt;
	private final int UPLOAD_IMAGE = 5;
	private Bitmap newBitmap;
	private MaxisStore store;

	private int	mRequestedPostImageType;

	// private ImageView mLogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.contest_upload_img_activity);
			((TextView) findViewById(R.id.header_title)).setText(getString(R.string.header_photo_contest));
			findViewById(R.id.header_btn_back).setOnClickListener(this);
			findViewById(R.id.goto_home_icon).setOnClickListener(this);
			findViewById(R.id.search_toggler).setVisibility(View.INVISIBLE);
			findViewById(R.id.show_profile_icon).setOnClickListener(this);
			store = MaxisStore.getStore(this);

			/*
			 * findViewById(R.id.footer_facebook_icon).setOnClickListener(this);
			 * findViewById(R.id.footer_twitterIcon).setOnClickListener(this); mLogo
			 * = (ImageView) findViewById(R.id.logo);
			 * mLogo.setOnClickListener(ContestUploadImageActivity.this);
			 * 
			 * mLogo.setOnClickListener(new OnClickListener() {
			 * 
			 * @Override public void onClick(View v) {
			 * 
			 * startActivity(new Intent(ContestUploadImageActivity.this,
			 * GetItInfoActivity.class));
			 * 
			 * } });
			 */

			// UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.rootLayout),
			// this);
			mUploadBtn = (Button) findViewById(R.id.upload_btn);
			mUploadBtn.setOnClickListener(this);

			Bundle bundle = getIntent().getExtras();
			if(bundle != null){
				Intent intent = getIntent();
				mCid = intent.getIntExtra(AppConstants.CID_KEY, 0);
				mCategoryId = intent.getIntExtra(AppConstants.CATEGORY_ID_KEY, 0);
				mCompanyName = intent.getStringExtra(AppConstants.COMPANY_NAME_KEY);
				mRequestedPostImageType = intent.getIntExtra(AppConstants.POST_IMAGE_REQUEST_KEY, 0);
			}
			mNumberEditTxt = ((EditText) findViewById(R.id.phone));
			String number = AppSharedPreference.getString(AppSharedPreference.MOBILE_NUMBER, "", getApplicationContext());

			if (store.isLoogedInUser()) {
				mNumberEditTxt.setText(store.getUserMobileNumberToDispaly());
			} else {
				mNumberEditTxt.setText(store.getAuthMobileNumber().substring(2));
			}
			// if (number.length() > 0)
			// mNumberEditTxt.setText(number);
			// else {
			// TelephonyManager mTelephonyMgr = (TelephonyManager)
			// getSystemService(Context.TELEPHONY_SERVICE);
			// String yourNumber = mTelephonyMgr.getLine1Number();
			//
			// if (yourNumber != null && yourNumber.length() > 0) {
			// mNumberEditTxt.setText(yourNumber);
			// }
			// }
			mNameEditTxt = ((EditText) findViewById(R.id.name));
			mNameEditTxt.setText(AppSharedPreference.getString(AppSharedPreference.USER_NAME, "", getApplicationContext()));
			mTitleEditTxt = ((EditText) findViewById(R.id.title));
			mTitleEditTxt.setText(mCompanyName);

			if (savedInstanceState == null) {
				switch (mRequestedPostImageType) {
				case AppConstants.GALLERY_REQUEST:
					openGalery();
					break;
				case AppConstants.CAMERA_REQUEST:
					takePhoto();
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), getString(R.string.toast_some_error_try_again),Toast.LENGTH_LONG).show();
			finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		AnalyticsHelper.trackSession(ContestUploadImageActivity.this, AppConstants.Add_Images_In_Existing_POI);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AppConstants.CAMERA_REQUEST) {
			if (resultCode != RESULT_OK) {
				finish();
				return;
			}
			Object dataObj = data.getExtras() == null ? null : data.getExtras().get("data");
			if (dataObj == null || !(dataObj instanceof Bitmap)) {
				Toast.makeText(getApplicationContext(),getString(R.string.toast_some_error_try_again),Toast.LENGTH_LONG).show();
				finish();
				return;
			}
			mBitmap = (Bitmap) dataObj;
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			// if(mBitmap.getDensity() > 400)
			// {
			// //
			// mBitmap.
			try {
				Uri uri = data.getData();
				String imagePath = getRealPathFromURI(uri);
				mBitmap = BitmapCalculation.decodeSampledBitmapFromPath(imagePath, 650, 650);

				// TODO:: SET ORIENTATION
				newBitmap = null;
				if (mBitmap != null) {
					mBitmap = decodeSampledBitmapFromFile(imagePath, 650, 650);
					newBitmap = setOrientation(mBitmap, imagePath);
					mBitmap = newBitmap;
				}

			} catch (Exception e) {
			}

			// System.out.println(mBitmap.getDensity());
			// System.out.println(mBitmap.getHeight());
			// System.out.println(mBitmap.getWidth());
			mBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 75, os);
			String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name);
			String imageFileName = getString(R.string.app_name) + "_" + System.currentTimeMillis() + ".jpg";
			boolean fileCreated = StorageUtils.createFile(directoryPath, imageFileName, os.toByteArray());
			if (!fileCreated) {
				Toast.makeText(getApplicationContext(),getString(R.string.toast_unable_to_save_image),Toast.LENGTH_LONG).show();
				finish();
				return;
			}
			mImagePath = directoryPath + "/" + imageFileName;
			manupulateExif();
			// } else {
			// Toast.makeText(getApplicationContext(),
			// getString(R.string.toast_some_error_try_again),
			// Toast.LENGTH_LONG).show();
			// finish();
			// }
		} else if (requestCode == AppConstants.GALLERY_REQUEST) {
			//			if (resultCode == Activity.RESULT_OK) {
			if (resultCode != RESULT_OK) {
				finish();
				return;
			}
			try {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String filePath = cursor.getString(columnIndex);
				cursor.close();
				Log.e("File", "filePath: " + filePath);

				File file = new File(new URI("file://" + filePath.replaceAll(" ", "%20")));
				int maxImageSize = BitmapCalculation.getMaxSize(this);
				mBitmap = BitmapCalculation.getScaledBitmap(file, maxImageSize);

				ByteArrayOutputStream os = new ByteArrayOutputStream();
				mBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 75, os);
				String directoryPath = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/" + getString(R.string.app_name);
				String imageFileName = getString(R.string.app_name) + "_" + System.currentTimeMillis() + ".jpg";
				boolean fileCreated = StorageUtils.createFile(directoryPath, imageFileName, os.toByteArray());
				if (!fileCreated) {
					Toast.makeText(getApplicationContext(),getString(R.string.toast_unable_to_save_image),Toast.LENGTH_LONG).show();
					finish();
					return;
				}
				mImagePath = directoryPath + "/" + imageFileName;
				manupulateExif();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goto_home_icon: {
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			showHomeScreen();
			break;
		}
		case R.id.header_btn_back: {
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			this.finish();
			break;
		}
		case R.id.show_profile_icon: {
			onProfileClick();
			break;
		}
		/*
		 * case R.id.footer_facebook_icon: {
		 * AnalyticsHelper.logEvent(FlurryEventsConstants.FACEBOOK_CLICK);
		 * checkPreferenceAndOpenBrowser(AppConstants.FB_PAGE_URL); break; }
		 * case R.id.footer_twitterIcon: {
		 * AnalyticsHelper.logEvent(FlurryEventsConstants.TWITTER_CLICK);
		 * checkPreferenceAndOpenBrowser(AppConstants.TWITTER_PAGE_URL); break;
		 * }
		 */
		case R.id.upload_btn: {
			AnalyticsHelper.logEvent(FlurryEventsConstants.UPLOAD_IMAGE);
			if (mLattitudeN == 0 && mLattitude == 0) {
				Toast.makeText(getApplicationContext(), "Location not available.", Toast.LENGTH_LONG).show();
				return;
			}
			String title = mTitleEditTxt.getText().toString();
			if (title == null || title.trim().length() == 0) {

				Toast.makeText(getApplicationContext(), "Please enter image title.", Toast.LENGTH_LONG).show();
				return;
			}
			String name = mNameEditTxt.getText().toString();
			if (name == null || name.trim().length() == 0) {
				Toast.makeText(getApplicationContext(), "Please enter name.", Toast.LENGTH_LONG).show();
				return;
			}
			AppSharedPreference.putString(AppSharedPreference.USER_NAME, name, getApplicationContext());
			String phone = mNumberEditTxt.getText().toString();
			if (phone == null || phone.trim().length() == 0) {
				Toast.makeText(getApplicationContext(), getString(R.string.number_empty), Toast.LENGTH_LONG).show();
				return;
				// Need TO Be Change Message.
			} else if (!phone.trim().startsWith("1")) {
				Toast.makeText(getApplicationContext(), getString(R.string.mobile_number_validation_contestapp), Toast.LENGTH_LONG).show();
				return;
			} else if (phone.trim().length() <= 7
					|| phone.trim().length() >= 12) {
				Toast.makeText(getApplicationContext(), getString(R.string.mobile_number_validation_contestapp), Toast.LENGTH_LONG).show();
				return;
			}

			AppSharedPreference.putString(AppSharedPreference.MOBILE_NUMBER,
					phone, getApplicationContext());
			// showDialog(CustomDialog.PROGRESS_DIALOG);
			startSppiner();
			// handler.sendEmptyMessageDelayed(2, 5*1000);
			uploadImageOnServer();
			break;
		}
		}
	}

	/**
	 * Method used to upload image
	 */
	private void uploadImageOnServer() {
		if (!isFetchingBase64) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					handler.removeMessages(UPLOAD_IMAGE);
					ContestUploadImageController contestUploadImageController = new ContestUploadImageController(
							ContestUploadImageActivity.this,
							Events.UPLOAD_IMAGE_EVENT);

					// mRequestUploadPhoto.setImageData(getBase64Image());
					String title = mTitleEditTxt.getText().toString().trim();
					String name = mNameEditTxt.getText().toString().trim();
					String phone = mNumberEditTxt.getText().toString().trim();
					mRequestUploadPhoto.setTitle(title);
					mRequestUploadPhoto.setName(name);
					mRequestUploadPhoto.setNumber("60" + phone);
					// IF NEED TO CHANGE
					// mRequestUploadPhoto.setNumber(phone);
					/*
					 * requestUploadPhoto.setLatitude(mLatitude==0?mLatitudeN:
					 * mLatitude);
					 * requestUploadPhoto.setLongitude(mLongitude==0?
					 * mLongitudeN:mLongitude);
					 */
					mRequestUploadPhoto.setLatitude(mLattitude);
					mRequestUploadPhoto.setLongitude(mLongitude);
					mRequestUploadPhoto.setcId(mCid + "");
					mRequestUploadPhoto.setCategoryId(mCategoryId + "");
					mHitTime = System.currentTimeMillis();
					contestUploadImageController.setHitTime(mHitTime);
					contestUploadImageController.requestService(mRequestUploadPhoto);
				}
			}).start();
		} else
			handler.sendEmptyMessageDelayed(UPLOAD_IMAGE, 500);
	}

	private String getBase64Image() {
		ByteArrayOutputStream full_stream = new ByteArrayOutputStream();
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, full_stream);
		byte[] full_bytes = full_stream.toByteArray();
		String img_full = Base64.encodeToString(full_bytes, Base64.DEFAULT);
		isFetchingBase64 = false;
		return img_full;
	}

	private void manupulateExif() {
		try {
			isFetchingBase64 = true;
			mExifInterface = new ExifInterface(mImagePath);
			StringBuilder builder = new StringBuilder();

			builder.append("Date & Time: "
					+ mExifInterface.getAttribute(ExifInterface.TAG_DATETIME)
					+ "\n\n");
			builder.append("Flash: "
					+ mExifInterface.getAttribute(ExifInterface.TAG_FLASH)
					+ "\n");
			builder.append("Focal Length: "
					+ mExifInterface
					.getAttribute(ExifInterface.TAG_FOCAL_LENGTH)
					+ "\n\n");
			builder.append("GPS Datestamp: "
					+ mExifInterface.getAttribute(ExifInterface.TAG_FLASH)
					+ "\n");

			String lat = mExifInterface
					.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
			String lat_ref = mExifInterface
					.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
			String lon = mExifInterface
					.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
			String lon_ref = mExifInterface
					.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
			formateLatLongToFloat(lat, lat_ref, lon, lon_ref);

			builder.append("GPS Latitude: "
					+ mExifInterface
					.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
					+ "\n");
			builder.append("GPS Latitude Ref: "
					+ mExifInterface
					.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)
					+ "\n");
			builder.append("GPS Longitude: "
					+ mExifInterface
					.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
					+ "\n");
			builder.append("GPS Longitude Ref: "
					+ mExifInterface
					.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)
					+ "\n");
			if (lat == null || lat.length() <= 0) {
				/*
				 * showDialog(CustomDialog.PROGRESS_DIALOG); if(mGpsOn)
				 * handler.sendEmptyMessageDelayed(0, 30*1000);
				 * setLocationListener();
				 */
				getLocation();
				/*
				 * mLongitude=AppSharedPreference.getFloat(AppSharedPreference.
				 * LONGITUDE, 0.0f, getApplication());
				 * mLattitude=AppSharedPreference
				 * .getFloat(AppSharedPreference.LATITUDE, 0.0f,
				 * getApplication()); if(mLongitude==0.0f&&mLattitude==0.0f) {
				 * mLongitudeN
				 * =AppSharedPreference.getFloat(AppSharedPreference.LONGITUDE_N
				 * , 0.0f, getApplication());
				 * mLattitudeN=AppSharedPreference.getFloat
				 * (AppSharedPreference.LATITUDE_N, 0.0f, getApplication());
				 * mLongitude=77.02663799999999fmLongitudeN;
				 * mLattitude=28.459497fmLattitudeN; }
				 */
				UpdateGeoTag(mLattitude, mLongitude);

				builder.append("NEW GPS Latitude: "
						+ mExifInterface
						.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
						+ "\n");
				builder.append("NEW GPS Latitude Ref: "
						+ mExifInterface
						.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)
						+ "\n");
				builder.append("NEW GPS Longitude: "
						+ mExifInterface
						.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
						+ "\n");
				builder.append("NEW GPS Longitude Ref: "
						+ mExifInterface
						.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)
						+ "\n");
			}
			/*
			 * else { mUploadBtn.setEnabled(true); }
			 */

			builder.append("GPS Processing Method: "
					+ mExifInterface
					.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD)
					+ "\n");
			builder.append("GPS Timestamp: "
					+ mExifInterface
					.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP)
					+ "\n\n");
			builder.append("Image Length: "
					+ mExifInterface
					.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)
					+ "\n");
			builder.append("Image Width: "
					+ mExifInterface
					.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)
					+ "\n\n");
			builder.append("Camera Make: "
					+ mExifInterface.getAttribute(ExifInterface.TAG_MAKE)
					+ "\n");
			builder.append("Camera Model: "
					+ mExifInterface.getAttribute(ExifInterface.TAG_MODEL)
					+ "\n");
			builder.append("Camera Orientation: "
					+ mExifInterface
					.getAttribute(ExifInterface.TAG_ORIENTATION) + "\n");
			builder.append("Camera White Balance: "
					+ mExifInterface
					.getAttribute(ExifInterface.TAG_WHITE_BALANCE)
					+ "\n");

			System.out.println(builder);

			// Get base 64 image in a separate thread
			new Thread(new Runnable() {
				@Override
				public void run() {
					mRequestUploadPhoto = new RequestUploadPhoto();
					mRequestUploadPhoto.setImageData(getBase64Image());

				}
			}).start();

		} catch (IOException e) {
			AnalyticsHelper.onError(FlurryEventsConstants.CONTEST_UPLOAD_IMAGE_ERR,"ContestUploadImageActivity : " + AppConstants.CONTEST_UPLOAD_IMAGE_ERROR_MSG, e);
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if (msg.obj instanceof MyError) {
					// removeDialog(CustomDialog.PROGRESS_DIALOG);
					stopSppiner();
					switch (((MyError) msg.obj).getErrorcode()) {
					case MyError.NETWORK_NOT_AVAILABLE:
						Toast.makeText(getApplicationContext(), "Network not available.", Toast.LENGTH_LONG).show();
						break;
					case MyError.EXCEPTION:
					case MyError.UNDEFINED:
						Toast.makeText(getApplicationContext(), "Server not responding.", Toast.LENGTH_LONG).show();
						break;

					}

				} else if (msg.obj instanceof ResponseUploadPhoto) {

					ResponseUploadPhoto photo = (ResponseUploadPhoto) msg.obj;
					if (photo.isSuccess()) {
						startActivity(new Intent(ContestUploadImageActivity.this, ContestUploadMoreActivity.class)
						.putExtra("mImagePath", mImagePath)
						.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
						.putExtra(AppConstants.CID_KEY, mCid)
						.putExtra(AppConstants.CATEGORY_ID_KEY, mCategoryId).putExtra(AppConstants.COMPANY_NAME_KEY, mCompanyName));
						mBitmap = null;
						// removeDialog(CustomDialog.PROGRESS_DIALOG);
						stopSppiner();
						System.gc();
						finish();
					} else {
						// removeDialog(CustomDialog.PROGRESS_DIALOG);
						stopSppiner();
						Toast.makeText(getApplicationContext(), photo.getErrorMsg(), Toast.LENGTH_LONG).show();
					}

				}
			}
			/*
			 * else if(msg.what==0) { unRegisterGpsLocationListener();
			 * unRegisterNetworkLocationListener();
			 * if(mLatitude==0&&mLatitudeN==0)
			 * Toast.makeText(getApplicationContext(),
			 * "Could Not Find Image Location.", 1).show(); else
			 * if(mLatitude==0) {
			 * 
			 * UpdateGeoTag(mLatitudeN, mLongitudeN);
			 * mUploadBtn.setEnabled(true);
			 * Toast.makeText(getApplicationContext(),
			 * "Could Not Find Image Location Using Gps.", 1).show(); } else {
			 * UpdateGeoTag(mLatitude, mLongitude); mUploadBtn.setEnabled(true);
			 * 
			 * }
			 * 
			 * }
			 */
			else if (msg.what == 2) {
				// removeDialog(CustomDialog.PROGRESS_DIALOG);
				stopSppiner();
				ResponseUploadPhoto event = new ResponseUploadPhoto();
				event.setSuccess(true);
				setScreenData(event, 1, mHitTime);
			} else if (msg.what == UPLOAD_IMAGE) {
				uploadImageOnServer();

			}
		}
	};

	/**
	 * @param LATITUDE
	 * @param LATITUDE_REF
	 * @param LONGITUDE
	 * @param LONGITUDE_REF
	 */
	private void formateLatLongToFloat(String LATITUDE, String LATITUDE_REF,
			String LONGITUDE, String LONGITUDE_REF) {

		if ((LATITUDE != null) && (LATITUDE_REF != null) && (LONGITUDE != null)
				&& (LONGITUDE_REF != null)) {
			if (LATITUDE_REF.equals("N")) {
				mLattitude = convertToDegree(LATITUDE);
			} else {
				mLattitude = 0 - convertToDegree(LATITUDE);
			}

			if (LONGITUDE_REF.equals("E")) {
				mLongitude = convertToDegree(LONGITUDE);
			} else {
				mLongitude = 0 - convertToDegree(LONGITUDE);
			}

		}

	}

	/**
	 * 
	 * @param stringDMS
	 * @return
	 */
	private Float convertToDegree(String stringDMS) {
		Float result = null;
		String[] DMS = stringDMS.split(",", 3);

		String[] stringD = DMS[0].split("/", 2);
		Double D0 = new Double(stringD[0]);
		Double D1 = new Double(stringD[1]);
		Double FloatD = D0 / D1;

		String[] stringM = DMS[1].split("/", 2);
		Double M0 = new Double(stringM[0]);
		Double M1 = new Double(stringM[1]);
		Double FloatM = M0 / M1;

		String[] stringS = DMS[2].split("/", 2);
		Double S0 = new Double(stringS[0]);
		Double S1 = new Double(stringS[1]);
		Double FloatS = S0 / S1;
		result = new Float(FloatD + (FloatM / 60) + (FloatS / 3600));
		return result;

	}

	private void UpdateGeoTag(double lat, double lon) {
		double alat = Math.abs(lat);
		String dms = Location.convert(alat, Location.FORMAT_SECONDS);
		String[] splitsArray = dms.split(":");
		String[] secnds = (splitsArray[2]).split("\\.");
		String seconds;
		if (secnds.length == 0) {
			seconds = splitsArray[2];
		} else {
			seconds = secnds[0];
		}

		String latitudeStr = splitsArray[0] + "/1," + splitsArray[1] + "/1,"
				+ seconds + "/1";
		mExifInterface
		.setAttribute(ExifInterface.TAG_GPS_LATITUDE, latitudeStr);

		mExifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF,
				lat > 0 ? "N" : "S");

		double alon = Math.abs(lon);

		dms = Location.convert(alon, Location.FORMAT_SECONDS);
		splitsArray = dms.split(":");
		secnds = (splitsArray[2]).split("\\.");

		if (secnds.length == 0) {
			seconds = splitsArray[2];
		} else {
			seconds = secnds[0];
		}
		String longitudeStr = splitsArray[0] + "/1," + splitsArray[1] + "/1," + seconds + "/1";

		mExifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, longitudeStr);
		mExifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, lon > 0 ? "E" : "W");
		try {
			mExifInterface.saveAttributes();
			// scanFile(this, mImagePath, "image/*");
		} catch (IOException e) {
			AnalyticsHelper.onError(FlurryEventsConstants.UPDATE_GEO_TAG_ERR, "ContestUploadImageActivity : " + AppConstants.UPDATE_GEO_TAG_ERROR_MSG, e);
		}
	}

	@Override
	public String toString() {
		return (String.valueOf(mLattitude) + ", " + String.valueOf(mLongitude));
	}

	public int getLatitudeE6() {
		return (int) (mLattitude * 1000000);
	}

	public int getLongitudeE6() {
		return (int) (mLongitude * 1000000);
	}

	@Override
	public void setScreenData(Object screenData, int event, long hitTime) {
		if (event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, hitTime);
		} else if (mHitTime == hitTime) {
			Message message = new Message();
			message.obj = screenData;
			message.what = 1;
			handler.sendMessage(message);
		}
	}

	@Override
	public Activity getMyActivityReference() {
		return this;
	}

	public static Bitmap decodeSampledBitmapFromFile(String filename,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 650);

		// If we're running on Honeycomb or newer, try to use inBitmap
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		// addInBitmapOptions(options);
		// }

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filename, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int maxImageSize) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		inSampleSize = (int) Math.pow(2.0, (int) Math.round(Math.log(maxImageSize / (double) Math.max(height, width)) / Math.log(0.5)));
		return inSampleSize;
	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}

	public String getRealPathFromURI(Uri uri) {
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		} else {
			Toast.makeText(getApplicationContext(), "manish", Toast.LENGTH_SHORT).show();
		}
		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
		return cursor.getString(idx);
	}

	public Bitmap setOrientation(Bitmap sourceBitmap, String photo) {
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(photo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
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
		Bitmap originalImage = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
		return originalImage;
	}
}
