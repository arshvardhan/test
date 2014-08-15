package com.kelltontech.maxisgetit.adapters.matta;

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
import com.kelltontech.maxisgetit.model.matta.booths.list.BoothModel;
import com.kelltontech.maxisgetit.ui.activities.matta.MattaBoothListActivity;

public class MattaBoothListAdapter extends BaseAdapter implements OnClickListener {
	private Context mContext;
	private ArrayList<BoothModel> list;
	private Drawable dummyDrawable;
	private Drawable errorDrawable;
	private BoothModel mBoothInfo;

	public MattaBoothListAdapter(Context context) {
		this.mContext = context;
		ImageLoader.initialize(mContext);
		dummyDrawable = mContext.getResources().getDrawable(R.drawable.comp_list_loading);
		errorDrawable = mContext.getResources().getDrawable(R.drawable.comp_list_cross);
	}

	public void setData(ArrayList<BoothModel> boothList) {
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
			convertView = inflater.inflate(R.layout.activity_matta_booth_list_row_layout, null);
			model = new Model();
			model.baseLayout = (LinearLayout) convertView
					.findViewById(R.id.cl_row_base_layout);
			model.boothImg = (ImageView) convertView
					.findViewById(R.id.cl_company_image);
			model.compTitle = (TextView) convertView
					.findViewById(R.id.cl_company_title);
			model.boothLocationTxv = (TextView) convertView
					.findViewById(R.id.cl_company_desc);
			model.destinationsTxv = (TextView) convertView
					.findViewById(R.id.cl_comp_addi_info);
			model.categoryTxv = (TextView) convertView
					.findViewById(R.id.cl_comp_addi_info2);
/*			model.packageTypeTxv = (TextView) convertView
					.findViewById(R.id.cl_comp_addi_info3);*/
			model.dealButton = (TextView) convertView
					.findViewById(R.id.cl_comp_distance);
			convertView.setTag(model);
		} else {
			model = (Model) convertView.getTag();
		}
		mBoothInfo = list.get(position);
		if (mBoothInfo != null
				&& (!StringUtil.isNullOrEmpty(mBoothInfo.getCId()))
				&& ("-1".equalsIgnoreCase(mBoothInfo.getCId()))) {
			int size = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 30, mContext.getResources()
							.getDisplayMetrics());
			model.baseLayout
					.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
							size, size));
			model.boothImg.setVisibility(View.GONE);
			model.compTitle.setVisibility(View.INVISIBLE);
			model.boothLocationTxv.setVisibility(View.INVISIBLE);
			model.destinationsTxv.setVisibility(View.INVISIBLE);
			model.categoryTxv.setVisibility(View.INVISIBLE);
//			model.packageTypeTxv.setVisibility(View.GONE);
			model.dealButton.setVisibility(View.GONE);
		} else {
			model.baseLayout.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
							android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
							android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
			model.boothImg.setVisibility(View.VISIBLE);
			model.compTitle.setVisibility(View.VISIBLE);
			model.boothLocationTxv.setVisibility(View.VISIBLE);
			model.destinationsTxv.setVisibility(View.VISIBLE);
			model.categoryTxv.setVisibility(View.VISIBLE);
//			model.packageTypeTxv.setVisibility(View.GONE);
			model.dealButton.setVisibility(View.VISIBLE);
			ImageLoader.start(mBoothInfo.getImage(), model.boothImg,
					dummyDrawable, errorDrawable);
			if (!StringUtil.isNullOrEmpty(mBoothInfo.getCName()))
				model.compTitle.setText(Html.fromHtml(mBoothInfo.getCName()));
			else
				model.compTitle.setText(Html.fromHtml(""));

			if (!StringUtil.isNullOrEmpty(mBoothInfo.getBoothLocation()))
				model.boothLocationTxv.setText(Html.fromHtml("<b>" + "Booth Location" + " : </b>" + mBoothInfo.getBoothLocation()));
			else
				model.boothLocationTxv.setText("");

			if (mBoothInfo.getAttribute_Group().getValues() != null && mBoothInfo.getAttribute_Group().getValues().size() > 0)
				model.destinationsTxv.setText(Html.fromHtml("<b>" + mBoothInfo.getAttribute_Group().getValues().get(0).getLabel() + " : </b>" + mBoothInfo.getAttribute_Group().getValues().get(0).getValue()));
//			else if (!StringUtil.isNullOrEmpty(mBoothInfo.getAttributes()))
//				model.destinationsTxv.setText(Html.fromHtml(mBoothInfo.getAttributes()));
			else
				model.destinationsTxv.setText("");
			if (mBoothInfo.getAttribute_Group().getValues() != null && mBoothInfo.getAttribute_Group().getValues().size() > 1)
				model.categoryTxv.setText(Html.fromHtml("<b>" + mBoothInfo.getAttribute_Group().getValues().get(1).getLabel() + " : </b>" + mBoothInfo.getAttribute_Group().getValues().get(1).getValue()));
			else
				model.categoryTxv.setText("");
//			if (mBoothInfo.getAttribute_Group().getValues() != null	&& mBoothInfo.getAttribute_Group().getValues().size() > 2)
//				model.packageTypeTxv.setText(Html.fromHtml("<b>" + mBoothInfo.getAttribute_Group().getValues().get(2).getLabel() + " : </b>" + mBoothInfo.getAttribute_Group().getValues().get(2).getValue()));
//			else
//				model.packageTypeTxv.setText("");

			model.dealButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((MattaBoothListActivity) mContext).onDealsButtonClick(position);
				}
			});
		}
		return convertView;
	}

	class Model {
		TextView compTitle;
		TextView boothLocationTxv;
		TextView destinationsTxv;
		TextView categoryTxv;
//		TextView packageTypeTxv;
		TextView dealButton;
		LinearLayout baseLayout;
		ImageView boothImg;
	}

	@Override
	public void onClick(View v) {
	}

}