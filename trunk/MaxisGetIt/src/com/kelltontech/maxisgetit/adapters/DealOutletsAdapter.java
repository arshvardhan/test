package com.kelltontech.maxisgetit.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.dao.OutLet;
import com.kelltontech.maxisgetit.ui.activities.DealDetailActivity;
import com.kelltontech.maxisgetit.ui.activities.ViewAllOutletsActivity;

public class DealOutletsAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<OutLet> list;
	boolean isFromDealDetail;
	int totalOutletCount;

	public DealOutletsAdapter(Context context, boolean isFrom, int totalOutlets) {
		mContext = context;
		isFromDealDetail = isFrom;
		totalOutletCount = totalOutlets;
	}

	public void setData(ArrayList<OutLet> outlets) {
		list = outlets;
	}

	@Override
	public int getCount() {
		if (list != null)
			return list.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Model model;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.outlet_list_item, null);
			model = new Model();
			model.dealTitle = (TextView) convertView
					.findViewById(R.id.outletName);
			model.dealAddress = (TextView) convertView
					.findViewById(R.id.outletAddress);
			model.mapIcon = (ImageView) convertView.findViewById(R.id.mapIcon);
			model.outletCount = (TextView) convertView
					.findViewById(R.id.outletCount);

			model.leftLayout = (LinearLayout) convertView
					.findViewById(R.id.leftLayout);
			model.addressContainer = (LinearLayout) convertView
					.findViewById(R.id.addressContainer);
			model.RightLayout = (LinearLayout) convertView
					.findViewById(R.id.rightLayout);
			model.seprator = (View) convertView.findViewById(R.id.seprator);
			model.viewMore = (ImageButton) convertView.findViewById(R.id.viewMore);

			convertView.setTag(model);
		} else {
			model = (Model) convertView.getTag();
		}
		if (isFromDealDetail) {
			OutLet outlets = list.get(position);
			model.dealTitle.setText(outlets.getTitle());
			model.dealAddress.setText(outlets.getAddress());
			model.outletCount.setBackground(null);
			model.outletCount.setText(position + 1 + "");
			model.viewMore.setVisibility(View.GONE);
			model.leftLayout.setVisibility(View.VISIBLE);
			model.addressContainer.setVisibility(View.VISIBLE);
			model.RightLayout.setVisibility(View.VISIBLE);
			model.seprator.setVisibility(View.VISIBLE);
			
			if (totalOutletCount > 10 && position == 9) {
				model.viewMore.setVisibility(View.VISIBLE);

			} else {
				model.viewMore.setVisibility(View.GONE);

			}
		} else {
			OutLet outlets = list.get(position);
			model.dealTitle.setText(outlets.getTitle());
			model.dealAddress.setText(outlets.getAddress());
			model.outletCount.setBackground(null);
			model.outletCount.setText(position + 1 + "");
			model.viewMore.setVisibility(View.GONE);
			model.leftLayout.setVisibility(View.VISIBLE);
			model.addressContainer.setVisibility(View.VISIBLE);
			model.RightLayout.setVisibility(View.VISIBLE);
			model.seprator.setVisibility(View.VISIBLE);
		}
		model.mapIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isFromDealDetail)
					((DealDetailActivity) mContext).showMap(position);
				else {
					((ViewAllOutletsActivity) mContext).showMap(position);
				}
			}
		});

		model.viewMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((DealDetailActivity) mContext).viewAllOutlets();
			}
		});

		return convertView;
	}

	class Model {
		TextView dealTitle;
		TextView dealAddress;
		ImageView mapIcon;
		TextView outletCount;
		LinearLayout leftLayout;
		LinearLayout addressContainer;
		LinearLayout RightLayout;
		View seprator;
		ImageButton viewMore;
	}

}
