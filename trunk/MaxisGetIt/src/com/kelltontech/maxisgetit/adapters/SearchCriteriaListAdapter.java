
package com.kelltontech.maxisgetit.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.dao.SearchAttribute;
import com.kelltontech.maxisgetit.ui.activities.SearchCriteriaChooserActivity;


/**
 * @author arsh.vardhan
 * @modified 28-Jul-2014
 * @description This adapter is created to fill search criteria list items in the FILTER BY TYPE dialog
 */

public class SearchCriteriaListAdapter extends BaseAdapter implements OnClickListener {
	private Context 					mContext;
	private ArrayList<SearchAttribute> 	searchAttributeList;
	private String 						displayIn = "";
	private SearchAttribute 			searchAttribute;
	private RadioButton mSelectedRB;
	private int mSelectedPosition = -1;

	public SearchCriteriaListAdapter(Context context) {
		mContext = context;
	}

	public void setData(ArrayList<SearchAttribute> searchAttributeList, String displayIn) {
		this.searchAttributeList = searchAttributeList;
		this.displayIn = displayIn;
	}

	@Override
	public int getCount() {
		if (searchAttributeList != null)
			return searchAttributeList.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return searchAttributeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Model model;
		if (convertView == null) {
			LayoutInflater inflater 		= 	(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView 					= 	inflater.inflate(R.layout.search_criteria_list_item, null);
			model = new Model();
			model.baseLayout 				= 	(LinearLayout) convertView.findViewById(R.id.cl_row_base_layout);
			model.searchCriteriaRadioBtn 	= 	(RadioButton) convertView.findViewById(R.id.radioBtn_search_criteria);
			convertView.setTag(model);
		} else {
			model = (Model) convertView.getTag();
		}
		searchAttribute = searchAttributeList.get(position);

		//		model.searchCriteriaRadioBtn.setTag(Integer.parseInt(""+position));

		if (searchAttribute != null && (!StringUtil.isNullOrEmpty(searchAttribute.getType()))) {
			model.baseLayout.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT));
			model.searchCriteriaRadioBtn.setVisibility(View.VISIBLE);
			model.searchCriteriaRadioBtn.setText((!StringUtil.isNullOrEmpty(searchAttribute.getLabel())) 
					? "   " + searchAttribute.getLabel() + " ("+ searchAttribute.getRecordFound() + ")" 
							: "   " + searchAttribute.getType() + " ("+ searchAttribute.getRecordFound() + ")");

			String searchValue = model.searchCriteriaRadioBtn.getText().toString().split("\\(")[0].trim();
			if ("UCompanyCategory".equalsIgnoreCase(searchValue)) {
				model.searchCriteriaRadioBtn.setText("   All" + " ("+ searchAttribute.getRecordFound() + ")");
			} else if  ("PCompany".equalsIgnoreCase(searchValue)) {
				model.searchCriteriaRadioBtn.setText("   Companies" + " ("+ searchAttribute.getRecordFound() + ")");
			} else if  ("AttCategory".equalsIgnoreCase(searchValue)) {
				model.searchCriteriaRadioBtn.setText("   Category" + " ("+ searchAttribute.getRecordFound() + ")");
			}

			if (displayIn.trim().equalsIgnoreCase(searchAttribute.getType())) {
				model.searchCriteriaRadioBtn.setChecked(true);
				mSelectedPosition = position;
			} else {
				model.searchCriteriaRadioBtn.setChecked(false);
			}
			model.searchCriteriaRadioBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((SearchCriteriaChooserActivity) mContext).onListViewItemSelection(searchAttributeList.get(position), mSelectedPosition);
					if(position != mSelectedPosition && mSelectedRB != null)
						mSelectedRB.setChecked(false);
					else if (position == mSelectedPosition) {
						mSelectedRB = model.searchCriteriaRadioBtn;
						mSelectedRB.setChecked(true);
					}
					mSelectedPosition = position;
					mSelectedRB = (RadioButton)v;
				}
			});
		}
		return convertView;
	}

	class Model {
		RadioButton 	searchCriteriaRadioBtn;
		LinearLayout 	baseLayout;
	}

	@Override
	public void onClick(View v) { }
}