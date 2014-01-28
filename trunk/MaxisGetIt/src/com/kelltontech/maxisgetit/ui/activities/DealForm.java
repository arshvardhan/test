package com.kelltontech.maxisgetit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.R.id;
import com.kelltontech.maxisgetit.R.layout;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.requests.CombinedListRequest;
import com.kelltontech.maxisgetit.response.CompanyListResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;
import com.kelltontech.maxisgetit.utils.UiUtils;
import com.kelltontech.maxisgetit.utils.Utility;

public class DealForm extends MaxisMainActivity {

	private TextView name;
	private TextView phoneNo;
	private TextView submit;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private ImageView mProfileIconView;
	private CombinedListRequest mClRequest;
	private CompanyListResponse mClResponse;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private ImageView mHeaderBackButton;
	private ImageView mHomeIconView;
	private TextView mHeaderTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deal_form);
		UiUtils.hideKeyboardOnTappingOutside(
				findViewById(R.id.reg_root_layout), this);
		name = (TextView) findViewById(R.id.reg_name);
		phoneNo = (TextView) findViewById(R.id.reg_mobile);
		submit = (TextView) findViewById(R.id.register_button);
		submit.setOnClickListener(this);

		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(DealForm.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler = (ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);

		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mHeaderTitle = (TextView) findViewById(R.id.header_title);
		mHeaderTitle.setText("Deals");

	}

	@Override
	public Activity getMyActivityReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.register_button:
			validateData();

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
			this.finish();
			break;
		case R.id.search_icon_button:
			mSearchEditText
					.setText(mSearchEditText.getText().toString().trim());
			performSearch(mSearchEditText.getText().toString());
			break;
		case R.id.show_profile_icon:
			onProfileClick();
			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intentHome = new Intent(DealForm.this, HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		default:
			break;
		}

	}

	public void validateData() {
		if (StringUtil.isNullOrEmpty(name.getText().toString())) {
			Toast.makeText(getApplicationContext(), "Please enter Name.",
					Toast.LENGTH_SHORT).show();
		} else if (StringUtil.isNullOrEmpty((phoneNo.getText().toString()))
				|| phoneNo.getText().length() > 12
				|| phoneNo.getText().length() < 7) {
			Toast.makeText(getApplicationContext(),
					"Please enter Mobile Number correctly.", Toast.LENGTH_SHORT)
					.show();
		} else {
			Intent returnIntent = new Intent();
			returnIntent.putExtra("name", name.getText().toString());
			returnIntent.putExtra("phoneNo", phoneNo.getText().toString());
			setResult(RESULT_OK, returnIntent);
			finish();
		}
	}

}
