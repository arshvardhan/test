package com.kelltontech.maxisgetit.ui.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.model.listModel.Category;
import com.kelltontech.maxisgetit.model.listModel.Distance;
import com.kelltontech.maxisgetit.model.listModel.ResponseList;
import com.kelltontech.maxisgetit.utils.ProjectUtils;

public class ContestPoiListAdapter extends BaseAdapter {
	private Activity mActivity;
	private ArrayList<IModel> mlist;
	private int mListType;

	public ContestPoiListAdapter(Activity context, ResponseList baseResponse, int listType) {
		this.mActivity = context;
		if (baseResponse != null) {
			mlist = baseResponse.getList();
			Log.e("", baseResponse.getList().size() + "");
		}
		mListType = listType;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = ((LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.contest_poi_list_row, null);
			holder = new ViewHolder();
			holder.txtHeading = (TextView) convertView.findViewById(R.id.txt1);
			holder.txtMiddle = (TextView) convertView.findViewById(R.id.txv_poi_address);
			holder.txtRight = (TextView) convertView.findViewById(R.id.txt3);
			holder.txtBottom = (TextView) convertView.findViewById(R.id.txt2);
			holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.layout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (mListType == Events.DISTANCE_LIST_EVENT || mListType == Events.POI_SEARCH_EVENT) {
			Distance distance = (Distance) mlist.get(position);
			holder.txtHeading.setText(distance.getCompanyName());

			String addressStr = null;
			if (distance.getAddress() != null) {
				addressStr = distance.getAddress().getAddressStr();
				if (StringUtil.isNullOrEmpty(addressStr)) {
					addressStr = ProjectUtils.getAddressText(distance.getAddress());
					distance.getAddress().setAddressStr(addressStr);
				}
			}
			if (StringUtil.isNullOrEmpty(addressStr)) {
				holder.txtMiddle.setVisibility(View.GONE);
			} else {
				holder.txtMiddle.setVisibility(View.VISIBLE);
				holder.txtMiddle.setText(addressStr);
			}

			if (distance.getCount() == 0) {
				holder.txtBottom.setText("No image submitted");
			} else if (distance.getCount() == 1) {
				holder.txtBottom.setText(distance.getCount() + " image already submitted");
			} else {
				holder.txtBottom.setText(distance.getCount() + " images already submitted");
			}
//			DecimalFormat df = new DecimalFormat("####0.00");
//			holder.txtRight.setText(df.format(distance.getDistance()) + " Km");
			holder.txtRight.setText(distance.getDistance());
		} else {
			Category category = (Category) mlist.get(position);
			if (category.getPoiCount() != 0) {
				convertView.findViewById(R.id.line_separater).setVisibility(View.VISIBLE);
				holder.linearLayout.setVisibility(View.VISIBLE);
			if(!category.getName().equalsIgnoreCase("null"))
				{
					holder.txtHeading.setVisibility(View.VISIBLE);
				holder.txtHeading.setText(category.getName());
				}
				else{
					holder.txtHeading.setVisibility(View.INVISIBLE);
				}
				holder.txtMiddle.setVisibility(View.GONE);
				
				if (category.getCount() == 0) {
					holder.txtBottom.setText("No image submitted");
				} else if (category.getCount() == 1) {
					holder.txtBottom.setText(category.getCount() + " image already submitted");
				} else {
					holder.txtBottom.setText(category.getCount() + " images already submitted");
				}
				holder.txtRight.setText(category.getPoiCount() + " POIs");
			} else {
				holder.linearLayout.setVisibility(View.GONE);
				convertView.findViewById(R.id.line_separater).setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	private class ViewHolder {
		TextView txtHeading;
		TextView txtMiddle;
		TextView txtBottom;
		TextView txtRight;
		LinearLayout linearLayout;
	}

	@Override
	public int getCount() {
		return mlist != null ? mlist.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		if (mlist != null)
			return mlist.get(position);
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void clearData() {
		if (mlist != null)
			mlist.clear();
	}

	public void setData(ResponseList responseList) {
		if (mlist == null)
			mlist = new ArrayList<IModel>();
		else
			mlist.clear();
		mlist = responseList.getList();
	}

	public void addData(ResponseList responseList) {
		if (mlist == null)
			mlist = new ArrayList<IModel>();
		/*
		 * else {
		 */
		mlist.addAll(responseList.getList());
		/* } */
	}
}
