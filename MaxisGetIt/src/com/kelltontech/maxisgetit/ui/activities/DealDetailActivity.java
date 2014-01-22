package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.DealMapInfoWindowAdapter;
import com.kelltontech.maxisgetit.adapters.ViewPagerAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.CompanyDetailController;
import com.kelltontech.maxisgetit.controllers.DownloadDealController;
import com.kelltontech.maxisgetit.controllers.OutLetDetailtController;
import com.kelltontech.maxisgetit.dao.CompanyDesc;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.dao.IconUrl;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.dao.OutLet;
import com.kelltontech.maxisgetit.dao.OutLetDetails;
import com.kelltontech.maxisgetit.requests.CombinedListRequest;
import com.kelltontech.maxisgetit.requests.DetailRequest;
import com.kelltontech.maxisgetit.requests.DownloadDealReq;
import com.kelltontech.maxisgetit.requests.OutLetDetailRequest;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class DealDetailActivity extends MaxisMainActivity implements
		OnGlobalLayoutListener, OnClickListener {

	private ViewPager dealGallery;
	private CompanyDetail compDetailResponse;
	private LinearLayout circleIndicator;
	private int flipperVisibleItemPosition = 0;
	private ArrayList<IconUrl> imgPathList;

	private TextView mHeaderText;
	private TextView mDealTitle;
	private TextView validIn;
	private TextView validDate;
	private TextView aboutUs;
	private TextView nearOutLets;
	private TextView tNc;
	private TextView dealDesc;
	private TextView downloadDeal;
	private TextView viewAllOutlets;
	private LinearLayout mSearchContainer;
	private EditText mSearchEditText;
	private ImageView mSearchBtn;
	private ImageView mProfileIconView;
	private ImageView mHeaderBackButton;
	private ImageView mHomeIconView;
	private ImageView mSearchToggler;
	private CombinedListRequest mClRequest;
	// ArrayList<NearOutLets> outLets;
	private DealMapInfoWindowAdapter mMapInfoWindowAdapter;

	private GoogleMap mMap;
	private Marker sourceMarker;
	private String comp_id;
	private String deal_id;
	private OutLetDetails outLetResponse;
	ArrayList<OutLet> outLets = new ArrayList<OutLet>();
	private String termsNdcond;
	private boolean isNearestOutlet = false;
	LinearLayout outLetsName;
	private final int textId = 1104;
	private int tagId = 0;
	boolean viewAdded = false;
	private MaxisStore store;
	String userNo;
	String userName;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 2) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				userName = bundle.getString("name");
				userNo = bundle.getString("phoneNo");
				dealDownload();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deal_detail);

		circleIndicator = (LinearLayout) findViewById(R.id.indicatorlinearlayout);
		store = MaxisStore.getStore(this);
		outLetsName = (LinearLayout) findViewById(R.id.outlets_names);
		Bundle bundle = getIntent().getExtras();
		compDetailResponse = bundle
				.getParcelable(AppConstants.COMP_DETAIL_DATA);
		mClRequest = bundle.getParcelable(AppConstants.DATA_LIST_REQUEST);

		if (compDetailResponse == null) {
			try {
				comp_id = bundle.getString(AppConstants.COMP_ID);
				String mCategoryid = getIntent().getStringExtra(
						AppConstants.CATEGORY_ID_KEY);
				CompanyDetailController controller = new CompanyDetailController(
						DealDetailActivity.this, Events.DEAL_DETAIL);
				DetailRequest detailRequest = new DetailRequest(
						DealDetailActivity.this, comp_id, getIntent()
								.getExtras().getBoolean(
										AppConstants.IS_DEAL_LIST), "");

				startSppiner();
				controller.requestService(detailRequest);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			comp_id = compDetailResponse.getCid();
			deal_id = compDetailResponse.getId();
			setdata();
			getOutLets();
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
		case R.id.about_deal:
			Log.e("manish", "inside onclick");
			isNearestOutlet = false;
			aboutUs.setBackgroundResource(R.drawable.left_btnselected);
			nearOutLets.setBackgroundResource(R.drawable.center_btn);
			tNc.setBackgroundResource(R.drawable.right_btn);
			outLetsName.setVisibility(View.GONE);
			if (!StringUtil.isNullOrEmpty(compDetailResponse.getDescription()))
				dealDesc.setText(compDetailResponse.getDescription());

			break;
		case R.id.nearest_outlet:
			isNearestOutlet = true;
			outLetsName.setVisibility(View.GONE);
			aboutUs.setBackgroundResource(R.drawable.left_btn);
			nearOutLets.setBackgroundResource(R.drawable.center_btnselected);
			tNc.setBackgroundResource(R.drawable.right_btn);

			setUpMapIfNeeded();
			if (outLets != null && outLets.size() > 0) {
				if (!StringUtil.isNullOrEmpty(outLetResponse.getOutlet().get(0)
						.getAddress())) {
					dealDesc.setText(outLets.get(0).getTitle() + ", "
							+ outLets.get(0).getAddress());
				}
			} else {
				dealDesc.setText("Nearest OutLet Address not available .");
			}
			break;

		case R.id.tnc:
			isNearestOutlet = false;
			outLetsName.setVisibility(View.GONE);
			aboutUs.setBackgroundResource(R.drawable.left_btn);
			nearOutLets.setBackgroundResource(R.drawable.center_btn);
			tNc.setBackgroundResource(R.drawable.right_btnselected);

			dealDesc.setText(termsNdcond);

			break;
		case R.id.search_toggler:
			AnalyticsHelper.logEvent(FlurryEventsConstants.HOME_SEARCH_CLICK);
			if (mSearchContainer.getVisibility() == View.VISIBLE) {
				mSearchContainer.setVisibility(View.GONE);
			} else {
				mSearchContainer.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.header_btn_back:
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			AnalyticsHelper
					.endTimedEvent(FlurryEventsConstants.APPLICATION_COMBINED_LIST);
			this.finish();
			break;
		case R.id.col_refine_search:
		case R.id.col_refine_search1:
			AnalyticsHelper.logEvent(FlurryEventsConstants.MODIFY_SEARCH_CLICK);
			break;
		case R.id.search_icon_button:
			mSearchEditText
					.setText(mSearchEditText.getText().toString().trim());
			performSearch(mSearchEditText.getText().toString());
			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intentHome = new Intent(DealDetailActivity.this,
					HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		case R.id.show_profile_icon:
			onProfileClick();
			break;

		case R.id.deal_download:
			// TODO
			getDownloadDetails();
			break;

		case R.id.deal_all_outlet:
			isNearestOutlet = false;
			setUpMapIfNeeded();
			addOutLets();
			break;
		case textId:
			int index = Integer.parseInt(v.getTag().toString());
			// ((TextView)v).setText("") ;
			// outLetsName.addView(v, index);
			// outLetsName.removeViewAt(index + 1 ) ;
			String catId = outLets.get(index).getCatid();
			String comp_id = outLets.get(index).getId();

			Intent intent = new Intent(DealDetailActivity.this,
					CompanyDetailActivity.class);

			Bundle bundle = new Bundle();
			bundle.putString(AppConstants.COMP_ID, comp_id);
			bundle.putString(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
			bundle.putBoolean(AppConstants.IS_DEAL_LIST, getIntent()
					.getExtras().getBoolean(AppConstants.IS_DEAL_LIST));
			intent.putExtra(AppConstants.CATEGORY_ID_KEY, catId);
			intent.putExtras(bundle);
			startActivity(intent);

			break;

		default:
			break;
		}

	}

	private void indicatorchange(int pos) {
		for (int i = 0; i < imgPathList.size(); i++) {
			circleIndicator.getChildAt(i).setBackgroundResource(
					R.drawable.circle_white);
		}
		circleIndicator.getChildAt(pos).setBackgroundResource(
				R.drawable.circle_blue);
	}

	private void addImage() {
		Log.e("manish", "inside add");

		// circleIndicator = (LinearLayout)
		// findViewById(R.id.indicatorlinearlayout);

		for (int i = 0; i < imgPathList.size(); i++) {
			ImageView image = new ImageView(this);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(0, 0, (int) (5 * getResources()
					.getDisplayMetrics().density), 0);
			int padding = (int) (3 * getResources().getDisplayMetrics().density);
			image.setPadding(padding, padding, padding, padding);
			image.setLayoutParams(layoutParams);

			circleIndicator.addView(image, i);

		}
		indicatorchange(flipperVisibleItemPosition);
	}

	@Override
	public void onGlobalLayout() {
		// TODO Auto-generated method stub
		// setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		mMap.setMyLocationEnabled(true);
		Location loc = mMap.getMyLocation();
		// loc.getLatitude()!=0&&loc.getLongitude()!=0
		LatLng fromPosition = new LatLng(GPS_Data.getLatitude(),
				GPS_Data.getLongitude());
		// LatLng fromPosition=new LatLng(6.419371,99.810822);
		// 28.589345,77.040825

		// LatLng toPosition=new LatLng(28.393213,77.248878);
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		builder.include(fromPosition);
		sourceMarker = mMap.addMarker(new MarkerOptions()
				.position(fromPosition)
				.title("You are here")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.map_user_marker)));
		// builder.include(sourceMarker.getPosition());
		LatLng toPosition = null;
		if (outLets != null) {
			for (int i = 0; i < outLets.size(); i++) {

				if (StringUtil.isNullOrEmpty(outLets.get(i).getLat())
						|| StringUtil.isNullOrEmpty(outLets.get(i).getLat())) {

				} else {
					toPosition = new LatLng(Double.parseDouble(outLets.get(i)
							.getLat()), Double.parseDouble(outLets.get(i)
							.getLongt()));
					mMap.addMarker(new MarkerOptions()
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.map_company_marker))
							.position(toPosition)
							.title(outLets.get(i).getTitle())
							.snippet(getSnippet(outLets.get(i))));
					builder.include(toPosition);
				}
			}
		}
		LatLngBounds bounds = builder.build();
		int padding = 100; // offset from edges of the map in pixels
		CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
		mMap.animateCamera(cu);
		final CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(fromPosition) // Sets the center of the map to Mountain
										// View
				.zoom(15) // Sets the zoom
				.bearing(90) // Sets the orientation of the camera to east
				.tilt(30) // Sets the tilt of the camera to 30 degrees
				.build();
		// mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		if (outLets != null && outLets.size() > 0) {
			if (isNearestOutlet) {
				mMapInfoWindowAdapter = new DealMapInfoWindowAdapter(this);
				ArrayList<OutLet> nearestOutlet = new ArrayList<OutLet>();

				nearestOutlet.add(outLets.get(0));
				mMapInfoWindowAdapter.setData(nearestOutlet);
				mMap.setInfoWindowAdapter(mMapInfoWindowAdapter);

			} else {
				mMapInfoWindowAdapter = new DealMapInfoWindowAdapter(this);
				mMapInfoWindowAdapter.setData(outLets);
				mMap.setInfoWindowAdapter(mMapInfoWindowAdapter);
			}
		}
		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				if (!StringUtil.isNullOrEmpty(marker.getSnippet())) {
					String mCurrentCompId;
					mCurrentCompId = marker.getSnippet().split(
							AppConstants.SPLIT_STRING)[0];

					mCurrentCompId = marker.getSnippet().split(
							AppConstants.SPLIT_STRING)[0];
					OutLet outLet = mMapInfoWindowAdapter
							.getValue(mCurrentCompId);
					Intent intent = new Intent(DealDetailActivity.this,
							CompanyDetailActivity.class);

					Bundle bundle = new Bundle();
					bundle.putString(AppConstants.COMP_ID, mCurrentCompId);
					bundle.putString(AppConstants.GLOBAL_SEARCH_KEYWORD,
							mSearchKeyword);
					bundle.putBoolean(AppConstants.IS_DEAL_LIST, getIntent()
							.getExtras().getBoolean(AppConstants.IS_DEAL_LIST));
					intent.putExtra(AppConstants.CATEGORY_ID_KEY,
							outLet.getCatid());
					intent.putExtras(bundle);
					startActivity(intent);

					/*
					 * Dialog dialog =
					 * CustomDialog.CreateCustomDialog(ViewAllOnMapActivity
					 * .this, CustomDialog.MAP_DIALOG, marker.getTitle());
					 * dialog.show();
					 */
				}
			}
		});
		mMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
			@Override
			public void onMyLocationChange(Location location) {
				if (location.getLatitude() != 0 && location.getLongitude() != 0) {
					GPS_Data.setLatitude(location.getLatitude());
					GPS_Data.setLongitude(location.getLongitude());
					sourceMarker.setPosition(new LatLng(GPS_Data.getLatitude(),
							GPS_Data.getLongitude()));
				}
			}
		});
		mMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
			@Override
			public boolean onMyLocationButtonClick() {
				mMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));
				return true;
			}
		});

	}

	private String getSnippet(OutLet outLet) {
		// id + location + image + distance
		String adddress = "";
		adddress = outLet.getAddress();
		String snippet = outLet.getId() + AppConstants.SPLIT_STRING + adddress
				+ AppConstants.SPLIT_STRING + outLet.getIcon_url()
				+ AppConstants.SPLIT_STRING + outLet.getPhone_no();
		return snippet;
	}

	public void getOutLets() {
		OutLetDetailtController detailtController = new OutLetDetailtController(
				DealDetailActivity.this, Events.OUTLET_DETAIL);
		OutLetDetailRequest detailRequest = new OutLetDetailRequest();

		detailRequest.setComp_id(comp_id);
		detailRequest.setDeal_id(deal_id);
		startSppiner();
		detailtController.requestService(detailRequest);
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.DOWNLOAD_DEAL) {
			Message message = (Message) screenData;
			handler.sendMessage(message);
		} else if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
		} else {
			System.out.println(screenData);
			Response response = (Response) screenData;
			Message message = new Message();
			message.arg2 = event;
			message.arg1 = 1;
			if (response.isError()) {
				message.obj = response.getErrorText();
			}

			else if (event == Events.OUTLET_DETAIL) {
				if (response.getPayload() instanceof OutLetDetails) {
					OutLetDetails outLetDetails = (OutLetDetails) response
							.getPayload();
					if (outLetDetails.getErrorCode() != 0) {
						message.obj = getResources().getString(
								R.string.communication_failure);
					} else {
						if (outLetDetails.getOutlet().size() < 1) {
							message.obj = new String(getResources().getString(
									R.string.no_result_found));
						} else {
							message.arg1 = 0;
							message.obj = outLetDetails;
						}
					}
				} else {
					message.obj = new String(getResources().getString(
							R.string.communication_failure));
				}
			} else if (event == Events.DEAL_DETAIL) {
				if (response.getPayload() instanceof CompanyDetail) {
					CompanyDetail compDetail = (CompanyDetail) response
							.getPayload();
					if (compDetail.getErrorCode() != 0) {
						message.obj = getResources().getString(
								R.string.communication_failure);
					} else {
						message.arg1 = 0;
						message.obj = compDetail;
					}
				} else {
					message.obj = new String(getResources().getString(
							R.string.communication_failure));
				}
			}
			handler.sendMessage(message);
		}
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.OUTLET_DETAIL) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				outLetResponse = (OutLetDetails) msg.obj;
				outLets = outLetResponse.getOutlet();
				setUpMapIfNeeded();

			}
			stopSppiner();
		} else if (msg.arg2 == Events.DOWNLOAD_DEAL) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else if (msg.arg1 == 0) {
				MaxisResponse genResp = (MaxisResponse) msg.obj;
				showInfoDialog("Thank you for Download Deal. The message has been sent to your Phone Number.");
			}
			stopSppiner();
		} else if (msg.arg2 == Events.DEAL_DETAIL) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				compDetailResponse = (CompanyDetail) msg.obj;
				if (!StringUtil.isNullOrEmpty(compDetailResponse.getId())) {
					deal_id = compDetailResponse.getId();
					comp_id = compDetailResponse.getCid();
					setdata();
					getOutLets();
					// intent.putExtra(AppConstants.THUMB_URL,
					// mCategoryThumbUrl);
					// intent.putExtra(AppConstants.IS_DEAL_LIST,
					// !mClRequest.isCompanyListing());
				} else {
					showInfoDialog(getResources().getString(
							R.string.no_result_found));
				}
			}
			stopSppiner();
		} else if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		}
	}

	public void getDownloadDetails() {

		if (store.isLoogedInUser()) {
			userNo = store.getUserMobileNumberToDispaly();
			userName = store.getUserName();
			dealDownload();

		} else {
			Intent intent = new Intent(getApplicationContext(), DealForm.class);
			startActivityForResult(intent, 2);

		}

	}

	public void setdata() {
		termsNdcond = compDetailResponse.getTermsNdCondition();
		// outLets = compDetailResponse.getNearoutlets();

		mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);
		mSearchEditText = (EditText) findViewById(R.id.search_box);

		mHeaderText = (TextView) findViewById(R.id.header_title);
		mDealTitle = (TextView) findViewById(R.id.txt_deal_name);
		validIn = (TextView) findViewById(R.id.validin);
		validDate = (TextView) findViewById(R.id.validdate);
		aboutUs = (TextView) findViewById(R.id.about_deal);
		nearOutLets = (TextView) findViewById(R.id.nearest_outlet);
		tNc = (TextView) findViewById(R.id.tnc);
		dealDesc = (TextView) findViewById(R.id.deal_desc);
		// dealDownload = (Button)findViewById(R.id.);
		downloadDeal = (TextView) findViewById(R.id.deal_download);
		viewAllOutlets = (TextView) findViewById(R.id.deal_all_outlet);

		downloadDeal.setOnClickListener(this);
		viewAllOutlets.setOnClickListener(this);

		if (!StringUtil.isNullOrEmpty(compDetailResponse.getTitle()))
			mHeaderText.setText(compDetailResponse.getTitle());

		if (!StringUtil.isNullOrEmpty(compDetailResponse.getTitle()))
			mDealTitle.setText(compDetailResponse.getTitle());

		if (!StringUtil.isNullOrEmpty(compDetailResponse.getValidIn()))
			validIn.setText(compDetailResponse.getValidIn());

		if (!StringUtil.isNullOrEmpty(compDetailResponse.getValidDate()))
			validDate.setText(compDetailResponse.getValidDate());

		if (!StringUtil.isNullOrEmpty(compDetailResponse.getDescription()))
			dealDesc.setText(compDetailResponse.getDescription());

		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mSearchToggler = (ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);

		aboutUs.setOnClickListener(this);
		tNc.setOnClickListener(this);
		nearOutLets.setOnClickListener(this);
		mSearchContainer.setOnClickListener(this);
		mSearchEditText.setOnClickListener(this);
		mSearchBtn.setOnClickListener(this);

		imgPathList = compDetailResponse.getIconUrl();

		dealGallery = (ViewPager) findViewById(R.id.dealtopbanner);
		if (imgPathList != null && imgPathList.size() > 0) {
			dealGallery.setVisibility(View.VISIBLE);
			ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(
					getSupportFragmentManager(), imgPathList, this);
			if (imgPathList.size() > 1) {
				addImage();
				circleIndicator.setVisibility(View.VISIBLE);
			} else {
				circleIndicator.setVisibility(View.GONE);
			}
			dealGallery.setAdapter(pagerAdapter);
		} else {
			dealGallery.setVisibility(View.GONE);
			circleIndicator.setVisibility(View.GONE);
		}

		dealGallery.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				indicatorchange(position);
				flipperVisibleItemPosition = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		// getOutLets();
	}

	public void addOutLets() {

		if (outLets.size() > 0) {
			// OutLetsName
			// LayoutInflater inflater = (LayoutInflater)
			// getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// View view = inflater.inflate(
			// R.layout.offerlistheader, null, true);
			if (!viewAdded) {
				viewAdded = true;
				for (OutLet title : outLets) {
					TextView name = new TextView(this);
					LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) name
							.getLayoutParams();

					name.setPadding(10, 10, 10, 10);
					// name.setTextSize(, size)
					// name.setCompoundDrawablesWithIntrinsicBounds(
					// R.drawable.circle_blue, 0, 0, 0);
					int count = tagId + 1;
					name.setText(Html.fromHtml("<b><font color='black'>"
							+ count + "</font></b>" + "  "
							+ "<font color='blue'>" + title.getTitle()
							+ "</font>"));

					name.setTag(tagId);
					name.setOnClickListener(this);
					name.setId(textId);
					outLetsName.addView(name);
					tagId++;
				}
			} else {
				outLetsName.setVisibility(View.VISIBLE);
			}

		}
	}

	public void dealDownload() {
		DownloadDealReq dealReq = new DownloadDealReq();
		dealReq.setName(userName);
		dealReq.setPhoneNo("60" + userNo);
		dealReq.setDeal_id(deal_id);

		DownloadDealController downloadDealController = new DownloadDealController(
				DealDetailActivity.this, Events.DOWNLOAD_DEAL);
		startSppiner();
		downloadDealController.requestService(dealReq);
	}
}
