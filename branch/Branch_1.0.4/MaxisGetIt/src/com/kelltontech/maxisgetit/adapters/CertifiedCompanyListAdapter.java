package com.kelltontech.maxisgetit.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.dao.CertifiedCompany;

public class CertifiedCompanyListAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<CertifiedCompany> list;
	private Drawable dummyDrawable;
	private Drawable errorDrawable;
	private CertifiedCompany mCertifiedCompany;

	public CertifiedCompanyListAdapter(Context context) {
		this.mContext = context;
		ImageLoader.initialize(mContext);
		dummyDrawable = mContext.getResources().getDrawable(R.drawable.comp_list_loading);
		errorDrawable = mContext.getResources().getDrawable(R.drawable.comp_list_cross);
	}

	public void setData(ArrayList<CertifiedCompany> certifiedCompanyList) {
		this.list = certifiedCompanyList;
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
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView 			= inflater.inflate(R.layout.certified_company_list_row_layout, null);
			model = new Model();
			model.baseLayout 		= (LinearLayout) convertView.findViewById(R.id.cl_row_base_layout);
			model.stampCompImage 	= (ImageView) convertView.findViewById(R.id.cl_company_image);
			model.stampCompTitle 	= (TextView) convertView.findViewById(R.id.cl_company_title);
			model.stampCompCode 	= (TextView) convertView.findViewById(R.id.cl_company_desc);
			model.stampCompExpDate 	= (TextView) convertView.findViewById(R.id.cl_comp_addi_info);
			convertView.setTag(model);
		} else {
			model = (Model) convertView.getTag();
		}
		mCertifiedCompany = list.get(position);
		if (mCertifiedCompany != null 
				&& (StringUtil.isNullOrEmpty(mCertifiedCompany.getStampId()))
				&& (StringUtil.isNullOrEmpty(mCertifiedCompany.getStampTitle()))) {
			int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, mContext.getResources().getDisplayMetrics());
			model.baseLayout.setLayoutParams(new android.widget.LinearLayout.LayoutParams(size, size));
			model.stampCompImage.setVisibility(View.GONE);
			model.stampCompTitle.setVisibility(View.INVISIBLE);
			model.stampCompCode.setVisibility(View.INVISIBLE);
			model.stampCompExpDate.setVisibility(View.INVISIBLE);
		} else {
			model.baseLayout.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
			model.stampCompImage.setVisibility(View.VISIBLE);
			model.stampCompTitle.setVisibility(View.VISIBLE);
			model.stampCompCode.setVisibility(View.VISIBLE);
			model.stampCompExpDate.setVisibility(View.VISIBLE);
			ImageLoader.start(mCertifiedCompany.getStampImage(), model.stampCompImage, dummyDrawable, errorDrawable);
		
			model.stampCompTitle.setText((!StringUtil.isNullOrEmpty(mCertifiedCompany.getStampTitle())) ? Html.fromHtml(mCertifiedCompany.getStampTitle()) : "");
			model.stampCompCode.setText(((!StringUtil.isNullOrEmpty(mCertifiedCompany.getStampCodeLabel())) 
					&& (!StringUtil.isNullOrEmpty(mCertifiedCompany.getStampCodeValue()))) 
					? Html.fromHtml("<b>" + mCertifiedCompany.getStampCodeLabel() + ": </b>" + mCertifiedCompany.getStampCodeValue()) 
							: "");
			model.stampCompExpDate.setText(((!StringUtil.isNullOrEmpty(mCertifiedCompany.getStampExpDateLabel())) 
					&& (!StringUtil.isNullOrEmpty(mCertifiedCompany.getStampExpDateValue()))) 
					? Html.fromHtml("<b>" + mCertifiedCompany.getStampExpDateLabel() + ": </b>" + mCertifiedCompany.getStampExpDateValue()) 
							: "");
			}
		return convertView;
	}

	class Model {
		TextView 		stampCompTitle;
		TextView 		stampCompCode;
		TextView 		stampCompExpDate;
		LinearLayout 	baseLayout;
		ImageView 		stampCompImage;
	}

}