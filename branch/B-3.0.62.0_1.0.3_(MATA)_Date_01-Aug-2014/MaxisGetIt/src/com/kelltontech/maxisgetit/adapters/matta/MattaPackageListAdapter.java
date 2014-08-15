package com.kelltontech.maxisgetit.adapters.matta;

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
import com.kelltontech.maxisgetit.model.matta.packages.list.PackageModel;

public class MattaPackageListAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<PackageModel> list;
	private Drawable dummyDrawable;
	private Drawable errorDrawable;
	private PackageModel mPackageInfo;

	public MattaPackageListAdapter(Context context) {
		this.mContext = context;
		ImageLoader.initialize(mContext);
		dummyDrawable = mContext.getResources().getDrawable(R.drawable.comp_list_loading);
		errorDrawable = mContext.getResources().getDrawable(R.drawable.comp_list_cross);
	}

	public void setData(ArrayList<PackageModel> boothList) {
		this.list = boothList;
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
			convertView 			= inflater.inflate(R.layout.activity_matta_package_list_row_layout, null);
			model = new Model();
			model.baseLayout 		= (LinearLayout) convertView.findViewById(R.id.cl_row_base_layout);
			model.packageImg 		= (ImageView) convertView.findViewById(R.id.cl_company_image);
			model.compTitle 		= (TextView) convertView.findViewById(R.id.cl_company_title);
			model.agencyNameTxv 	= (TextView) convertView.findViewById(R.id.cl_company_desc);
			model.destinationsTxv 	= (TextView) convertView.findViewById(R.id.cl_comp_addi_info);
			model.categoryTxv 		= (TextView) convertView.findViewById(R.id.cl_comp_addi_info2);
//			model.packageTypeTxv 	= (TextView) convertView.findViewById(R.id.cl_comp_addi_info3);
			convertView.setTag(model);
		} else {
			model = (Model) convertView.getTag();
		}
		mPackageInfo = list.get(position);
		if (mPackageInfo != null
				&& (!StringUtil.isNullOrEmpty(mPackageInfo.getId()))
				&& ("-1".equalsIgnoreCase(mPackageInfo.getId()))) {
			int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, mContext.getResources().getDisplayMetrics());
			model.baseLayout.setLayoutParams(new android.widget.LinearLayout.LayoutParams(size, size));
			model.packageImg.setVisibility(View.GONE);
			model.compTitle.setVisibility(View.INVISIBLE);
			model.agencyNameTxv.setVisibility(View.INVISIBLE);
			model.destinationsTxv.setVisibility(View.INVISIBLE);
			model.categoryTxv.setVisibility(View.INVISIBLE);
//			model.packageTypeTxv.setVisibility(View.GONE);
		} else {
			model.baseLayout.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
			model.packageImg.setVisibility(View.VISIBLE);
			model.compTitle.setVisibility(View.VISIBLE);
			model.agencyNameTxv.setVisibility(View.VISIBLE);
			model.destinationsTxv.setVisibility(View.VISIBLE);
			model.categoryTxv.setVisibility(View.VISIBLE);
//			model.packageTypeTxv.setVisibility(View.GONE);
			ImageLoader.start(mPackageInfo.getImage(), model.packageImg, dummyDrawable, errorDrawable);
			if (!StringUtil.isNullOrEmpty(mPackageInfo.getTitle()))
				model.compTitle.setText(Html.fromHtml(mPackageInfo.getTitle()));
			else
				model.compTitle.setText(Html.fromHtml(""));

			if (!StringUtil.isNullOrEmpty(mPackageInfo.getCName()))
				model.agencyNameTxv.setText(Html.fromHtml("<b>" + "Agency" + " : </b>" + mPackageInfo.getCName()));
			else
				model.agencyNameTxv.setText("");

			if (mPackageInfo.getAttribute_Group().getValues() != null && mPackageInfo.getAttribute_Group().getValues().size() > 0)
				model.destinationsTxv.setText(Html.fromHtml("<b>" + mPackageInfo.getAttribute_Group().getValues().get(0).getLabel() + " : </b>" + mPackageInfo.getAttribute_Group().getValues().get(0).getValue()));
			else
				model.destinationsTxv.setText("");
			if (mPackageInfo.getAttribute_Group().getValues() != null && mPackageInfo.getAttribute_Group().getValues().size() > 1)
				model.categoryTxv.setText(Html.fromHtml("<b>" + mPackageInfo.getAttribute_Group().getValues().get(1).getLabel() + " : </b>" + mPackageInfo.getAttribute_Group().getValues().get(1).getValue()));
			else
				model.categoryTxv.setText("");
//			if (mPackageInfo.getAttribute_Group().getValues() != null	&& mPackageInfo.getAttribute_Group().getValues().size() > 2)
//				model.packageTypeTxv.setText(Html.fromHtml("<b>" + mPackageInfo.getAttribute_Group().getValues().get(2).getLabel() + " : </b>" + mPackageInfo.getAttribute_Group().getValues().get(2).getValue()));
//			else
//				model.packageTypeTxv.setText("");
		}
		return convertView;
	}

	class Model {
		TextView compTitle;
		TextView agencyNameTxv;
		TextView destinationsTxv;
		TextView categoryTxv;
//		TextView packageTypeTxv;
		LinearLayout baseLayout;
		ImageView packageImg;
	}

}