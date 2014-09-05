package com.kelltontech.maxisgetit.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.model.paidcompany.PaidCompany;

public class PaidCompanyListAdapter extends BaseAdapter implements OnClickListener {
	private Context 					mContext;
	private ArrayList<PaidCompany> 		list;
	private Drawable 					dummyDrawable;
	private Drawable 					errorDrawable;
	private PaidCompany 				company;

	public PaidCompanyListAdapter(Context context) {
		mContext = context;
		ImageLoader.initialize(mContext);
			dummyDrawable = mContext.getResources().getDrawable(R.drawable.comp_list_loading);
			errorDrawable = mContext.getResources().getDrawable(R.drawable.comp_list_cross);
	}

	public void setData(ArrayList<PaidCompany> paidCompanyList) {
		list = paidCompanyList;
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Model model;
		if (convertView == null) {
			LayoutInflater inflater 	= 	(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView 				= 	inflater.inflate(R.layout.paid_company_list_row, null);
			model = new Model();
			model.baseLayout 			= 	(LinearLayout) convertView.findViewById(R.id.pcl_row_base_layout);
			model.compImage 			= 	(ImageView) convertView.findViewById(R.id.pcl_company_image);
			model.compTitle 			= 	(TextView) convertView.findViewById(R.id.pcl_company_title);
			model.compDesc 				= 	(TextView) convertView.findViewById(R.id.pcl_company_desc);
			model.compDistance 			= 	(TextView) convertView.findViewById(R.id.pcl_comp_distance);
			model.distanceLable 		= 	(TextView) convertView.findViewById(R.id.pcl_comp_distance);
			convertView.setTag(model);
		} else {
			model = (Model) convertView.getTag();
		}
		company = list.get(position);
		if (company != null && (!StringUtil.isNullOrEmpty(company.getId())) && ("-1".equalsIgnoreCase(company.getId()))) {
			int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, mContext.getResources().getDisplayMetrics());
			model.baseLayout.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(size, size));
			model.compImage.setVisibility(View.GONE);
			model.compTitle.setVisibility(View.INVISIBLE);
			model.compDesc.setVisibility(View.INVISIBLE);
			model.compDistance.setVisibility(View.GONE);
			model.distanceLable.setVisibility(View.GONE);
		} else {
			model.baseLayout.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT));
			model.compImage.setVisibility(View.VISIBLE);
			model.compTitle.setVisibility(View.VISIBLE);
			model.compDesc.setVisibility(View.VISIBLE);
			model.compDistance.setVisibility(View.VISIBLE);
			model.distanceLable.setVisibility(View.VISIBLE);
			ImageLoader.start(company.getIconUrl(), model.compImage, dummyDrawable, errorDrawable);
			if (!StringUtil.isNullOrEmpty(company.getTitle()))
				model.compTitle.setText(Html.fromHtml(company.getTitle()));
			else
				model.compTitle.setText(Html.fromHtml(""));
			
			String city_state 	= 	(company.getAddress().getLocality() != null) ? company.getAddress().getLocality() : "";
			if (city_state.trim().length() > 0 && company.getAddress().getState().trim().length() > 0)
				city_state 	+= 	", "	+ ((company.getAddress().getCity() != null) ? company.getAddress().getCity() : "");
			else
				city_state 	+= 	((company.getAddress().getCity() != null) ? company.getAddress().getCity() : "");
			if (city_state.trim().length() > 0 && company.getAddress().getState().trim().length() > 0)
				city_state 	+= 	", " + company.getAddress().getState();
			else if (city_state.trim().length() == 0)
				city_state 	= 	company.getAddress().getState();

			if (!StringUtil.isNullOrEmpty(city_state))
				model.compDesc.setText(city_state);
			else
				model.compDesc.setText("");
			
			if (StringUtil.isNullOrEmpty(company.getDistance()) || company.getDistance().trim().equals("0"))
				model.distanceLable.setVisibility(View.GONE);
			else
				model.compDistance.setText(company.getDistance().trim());
			}
		return convertView;
	}

	class Model {
		TextView 		compTitle;
		TextView 		compDesc;
		TextView 		compAdditionalInfo;
		TextView 		compAdditionalInfo2;
		TextView 		compDistance;
		TextView 		distanceLable;
		LinearLayout 	baseLayout;
		ImageView 		compImage;
	}

	@Override
	public void onClick(View v) { }

}