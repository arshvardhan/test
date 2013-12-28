package com.kelltontech.maxisgetit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.TempletController;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.dao.SubCategory;
import com.kelltontech.maxisgetit.dao.TypeByCategory;
import com.kelltontech.maxisgetit.requests.SaleTemplate;
import com.kelltontech.maxisgetit.response.ControlDetailResponse;
import com.kelltontech.maxisgetit.response.TypeByCategoryResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

/**
 * @author Arsh Vardhan Atreya
 * @email arshvardhan.atreya@kelltontech.com
 */

public class SaleTemplateTypeActivity extends MaxisMainActivity {
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private TextView mHeaderTitle;
	private TextView submitBtn;
	private SubCategory mSelectdCategory;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private TypeByCategoryResponse mtypeByCatResponse;
	private TypeByCategory mSelectedType;
	private ImageView mProfileIconView;
	private ImageView mHeaderBackButton;
	private ImageView mHomeIconView;
	private Spinner mTypeSelector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sale_template_type);
		
		Bundle bundle = getIntent().getExtras();
		mtypeByCatResponse = bundle.getParcelable(AppConstants.TYPE_BY_CATEGORY_DATA);
		mSelectdCategory = bundle.getParcelable(AppConstants.SELECTED_CAT_DATA);
		
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.sale_template_type_root_layout), this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(SaleTemplateTypeActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler = (ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);

		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);

		mHeaderTitle = (TextView) findViewById(R.id.header_title);
		mHeaderTitle.setText(mSelectdCategory.getCategoryTitle());

		submitBtn = (TextView) findViewById(R.id.sale_template_type_btn);
		submitBtn.setOnClickListener(this);
		
		showCategorySpinner();
	}

	private void showCategorySpinner() {
		if (mtypeByCatResponse == null)
			return;
		ArrayAdapter<TypeByCategory> adapter = new ArrayAdapter<TypeByCategory>(this, R.layout.spinner_item, mtypeByCatResponse.getTypeByCategoryList());
		mTypeSelector = (Spinner) findViewById(R.id.sale_template_type_spinner);
		mTypeSelector.setAdapter(adapter);
		mTypeSelector.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				{
					mSelectedType = (TypeByCategory) parent.getItemAtPosition(position);
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
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE || event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.TEMPLET_LISTING) {
			handler.sendMessage((Message) screenData);
		}
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE || msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.TEMPLET_LISTING) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				MaxisStore store = MaxisStore.getStore(SaleTemplateTypeActivity.this);
				if (store.isLoogedInUser()) {
					Intent intent = new Intent(SaleTemplateTypeActivity.this, SaleTempletActivity.class);
					intent.putExtra(AppConstants.TEMPLET_DATA, (ControlDetailResponse) msg.obj);
					intent.putExtra(AppConstants.SELECTED_CAT_DATA, mSelectdCategory);
					startActivity(intent);
				} else {
					Intent branchIntent = new Intent(SaleTemplateTypeActivity.this, GuestBranchingActivity.class);
					branchIntent.putExtra(AppConstants.TEMPLET_DATA, (ControlDetailResponse) msg.obj);
					branchIntent.putExtra(AppConstants.SELECTED_CAT_DATA, mSelectdCategory);
					startActivity(branchIntent);
				}
			}
			stopSppiner();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			performSearch(mSearchEditText.getText().toString());
			break;
		case R.id.show_profile_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.SHOW_PROFILE_CLICK);
			onProfileClick();
			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intentHome = new Intent(SaleTemplateTypeActivity.this, HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		case R.id.sale_template_type_btn:
			if(("-1").equalsIgnoreCase(mSelectedType.getType())){
				showInfoDialog(getString(R.string.add_type));
			} else {
			showTempletScreen(mSelectdCategory.getCategoryId(), mSelectedType.getType());
			}
			break;
		}
	}

	protected void showTempletScreen(String categoryId, String templateType) {
		TempletController tempcController = new TempletController(SaleTemplateTypeActivity.this, Events.TEMPLET_LISTING);
		startSppiner();
		SaleTemplate saleTemplate = new SaleTemplate();
		saleTemplate.setCategoryId(categoryId);
		saleTemplate.setTemplateType(templateType);
		tempcController.requestService(saleTemplate);
	}

	@Override
	protected void onDestroy() {
		ImageLoader.clearCache();
		super.onDestroy();
	}
}
