package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.SearchCriteriaListAdapter;
import com.kelltontech.maxisgetit.dao.SearchAttribute;

public class SearchCriteriaChooserActivity extends MaxisMainActivity {

	private ListView searchCriteriaListView;
	private SearchAttribute chosenCriteria;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_criteria_chooser);
		searchCriteriaListView = (ListView) findViewById(R.id.search_criteria_ListView);
		((Button) findViewById(R.id.dialog_ok)).setOnClickListener(this);
		((Button) findViewById(R.id.dialog_cancel)).setOnClickListener(this);

		Bundle bundle = getIntent().getExtras();
		ArrayList<SearchAttribute> searchCriteriaList = new ArrayList<SearchAttribute>();
		String displayIn = "";
		searchCriteriaList = bundle.getParcelableArrayList("SearchCriteria");
		displayIn = bundle.getString("displayIn");

		SearchCriteriaListAdapter adapter = new SearchCriteriaListAdapter(this);
		adapter.setData(searchCriteriaList, displayIn);
		searchCriteriaListView.setAdapter(adapter);
	}

	public void onListViewItemSelection(SearchAttribute chosenSearchCriteria, int defaultSelectedPosition) {
		chosenCriteria = chosenSearchCriteria;
		RadioButton defualtSelectedRadioButton = (RadioButton) searchCriteriaListView.getChildAt(defaultSelectedPosition).findViewById(R.id.radioBtn_search_criteria);
		defualtSelectedRadioButton.setChecked(false);
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_ok:
			Intent intent = new Intent();
			intent.putExtra("ChosenSearchCriteria", chosenCriteria);
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.dialog_cancel:
			finish();
			break;
		default:
			break;
		}
	}
}
