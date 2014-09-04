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
import android.widget.RatingBar;
import android.widget.TextView;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.dao.CompanyDesc;
import com.kelltontech.maxisgetit.ui.activities.CombindListActivity;

public class CompanyListAdapter extends BaseAdapter implements OnClickListener {
	private Context 				mContext;
	private ArrayList<CompanyDesc> 	list;
	private Drawable 				dummyDrawable;
	private Drawable 				errorDrawable;
	private boolean 				isCompanyListing;
	private CompanyDesc 			compDesc;

	public CompanyListAdapter(Context context, boolean isCompanyType) {
		mContext = context;
		isCompanyListing = isCompanyType;
		ImageLoader.initialize(mContext);
		if (isCompanyType) {
			dummyDrawable = mContext.getResources().getDrawable(R.drawable.comp_list_loading);
			errorDrawable = mContext.getResources().getDrawable(R.drawable.comp_list_cross);
		} else {
			dummyDrawable = mContext.getResources().getDrawable(R.drawable.deal_list_loading);
			errorDrawable = mContext.getResources().getDrawable(R.drawable.deal_list_noimage);
		}
	}

	public void setData(ArrayList<CompanyDesc> compDescList) {
		list = compDescList;
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
			convertView 				= 	inflater.inflate(R.layout.company_list_row, null);
			model = new Model();
			model.baseLayout 			= 	(LinearLayout) convertView.findViewById(R.id.cl_row_base_layout);
			model.compImage 			= 	(ImageView) convertView.findViewById(R.id.cl_company_image);
			model.compTitle 			= 	(TextView) convertView.findViewById(R.id.cl_company_title);
			model.compDesc 				= 	(TextView) convertView.findViewById(R.id.cl_company_desc);
			model.compAdditionalInfo 	=	(TextView) convertView.findViewById(R.id.cl_comp_addi_info);
			model.compAdditionalInfo2 	= 	(TextView) convertView.findViewById(R.id.cl_comp_addi_info2);
			model.compDistance 			= 	(TextView) convertView.findViewById(R.id.cl_comp_distance);
			model.distanceLable 		= 	(TextView) convertView.findViewById(R.id.cl_comp_distance);
			model.compRating 			= 	(RatingBar) convertView.findViewById(R.id.cl_comp_rating);
			model.videoIcon 			= 	(ImageView) convertView.findViewById(R.id.videoIcon);
			model.stampCompanyLink 		= 	(TextView) convertView.findViewById(R.id.cl_view_stamp);
			model.stampImage 			= 	(ImageView) convertView.findViewById(R.id.cl_stamp_image);
			convertView.setTag(model);
		} else {
			model = (Model) convertView.getTag();
		}
		compDesc = list.get(position);
		if (compDesc != null && (!StringUtil.isNullOrEmpty(compDesc.getCompId())) && ("-1".equalsIgnoreCase(compDesc.getCompId()))) {
			int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, mContext.getResources().getDisplayMetrics());
			model.baseLayout.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(size, size));
			model.compImage.setVisibility(View.GONE);
			model.compTitle.setVisibility(View.INVISIBLE);
			model.compDesc.setVisibility(View.INVISIBLE);
			model.compAdditionalInfo.setVisibility(View.INVISIBLE);
			model.compAdditionalInfo2.setVisibility(View.INVISIBLE);
			model.compDistance.setVisibility(View.GONE);
			model.distanceLable.setVisibility(View.GONE);
			model.compRating.setVisibility(View.GONE);
			model.videoIcon.setVisibility(View.INVISIBLE);
			model.stampCompanyLink.setVisibility(View.GONE);
			model.stampImage.setVisibility(View.GONE);
		} else {
			model.baseLayout.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT));
			model.compImage.setVisibility(View.VISIBLE);
			model.compTitle.setVisibility(View.VISIBLE);
			model.compDesc.setVisibility(View.VISIBLE);
			model.compAdditionalInfo.setVisibility(View.VISIBLE);
			model.compAdditionalInfo2.setVisibility(View.VISIBLE);
			model.compDistance.setVisibility(View.VISIBLE);
			model.distanceLable.setVisibility(View.VISIBLE);
			model.compRating.setVisibility(View.VISIBLE);
			ImageLoader.start(compDesc.getIconUrl(), model.compImage, dummyDrawable, errorDrawable);
			if (!StringUtil.isNullOrEmpty(compDesc.getStampUrl())) {
				model.stampImage.setVisibility(View.VISIBLE);
				//				((MarginLayoutParams) model.stampCompanyLink.getLayoutParams()).setMargins(0, 0, 0, 0);
				//				((MarginLayoutParams) model.stampImage.getLayoutParams()).setMargins(0, 0, 0, 0);
				ImageLoader.start(compDesc.getStampUrl(), model.stampImage, dummyDrawable, errorDrawable);
			} else 
				model.stampImage.setVisibility(View.INVISIBLE);
			if (!StringUtil.isNullOrEmpty(compDesc.getTitle()))
				model.compTitle.setText(Html.fromHtml(compDesc.getTitle()));
			else
				model.compTitle.setText(Html.fromHtml(""));
			if (compDesc.isStamp() && (!StringUtil.isNullOrEmpty(compDesc.getStampId())) && (StringUtil.isNullOrEmpty(compDesc.getStampUrl()))) {
				model.stampImage.setVisibility(View.GONE);
				//				((MarginLayoutParams) model.stampCompanyLink.getLayoutParams()).setMargins(35, 0, 0, 0);
				model.stampCompanyLink.setVisibility(View.VISIBLE);
				model.stampCompanyLink.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						CompanyDesc companyDescription = list.get(position);
						if (companyDescription.isStamp() && (!StringUtil.isNullOrEmpty(companyDescription.getStampId())) && (StringUtil.isNullOrEmpty(companyDescription.getStampUrl())))
							((CombindListActivity) mContext).viewMoreCopmay(companyDescription);
					}
				});
			} else
				model.stampCompanyLink.setVisibility(View.GONE);

			String city_state 	= 	(compDesc.getLocality() != null) ? compDesc.getLocality() : "";
			if (city_state.trim().length() > 0 && compDesc.getState().trim().length() > 0)
				city_state 	+= 	", "	+ ((compDesc.getCity() != null) ? compDesc.getCity() : "");
			else
				city_state 	+= 	((compDesc.getCity() != null) ? compDesc.getCity() : "");
			if (city_state.trim().length() > 0 && compDesc.getState().trim().length() > 0)
				city_state 	+= 	", " + compDesc.getState();
			else if (city_state.trim().length() == 0)
				city_state 	= 	compDesc.getState();

			if (!StringUtil.isNullOrEmpty(city_state))
				model.compDesc.setText(city_state);
			else
				model.compDesc.setText("");

			if (compDesc.getAttrGroups() != null && compDesc.getAttrGroups().size() > 0) 
				model.compAdditionalInfo.setText(Html.fromHtml("<b>" + compDesc.getAttrGroups().get(0).getLable() + " : </b>" + compDesc.getAttrGroups().get(0).getValues().get(0)));
			else if (!StringUtil.isNullOrEmpty(compDesc.getAttributes()))
				model.compAdditionalInfo.setText(Html.fromHtml(compDesc.getAttributes()));
			else 
				model.compAdditionalInfo.setText("");
			if (compDesc.getAttrGroups() != null && compDesc.getAttrGroups().size() > 1) 
				model.compAdditionalInfo2.setText(Html.fromHtml("<b>" + compDesc.getAttrGroups().get(1).getLable() + " : </b>" + compDesc.getAttrGroups().get(1).getValues().get(0)));
			else
				model.compAdditionalInfo2.setText("");
			if (StringUtil.isNullOrEmpty(compDesc.getDistance()) || compDesc.getDistance().trim().equals("0") || !isCompanyListing)
				model.distanceLable.setVisibility(View.GONE);
			else
				model.compDistance.setText(compDesc.getDistance().trim());
			if (isCompanyListing) {
				if (compDesc.getRating() >= 0)
					model.compRating.setRating(compDesc.getRating());
				else
					model.compRating.setRating(0);
			} else
				model.compRating.setVisibility(View.GONE);

			// condition for videoIcon visibility.
			if (compDesc.getVideo_url() != null) {
				if (compDesc.getVideo_url().equalsIgnoreCase("0"))
					model.videoIcon.setVisibility(View.GONE);
				else
					model.videoIcon.setVisibility(View.VISIBLE);
			}
		}
		return convertView;
	}

	class Model {
		TextView 		compTitle;
		TextView 		compDesc;
		TextView 		compAdditionalInfo;
		TextView 		compAdditionalInfo2;
		TextView 		compDistance;
		RatingBar 		compRating;
		TextView 		distanceLable;
		LinearLayout 	baseLayout;
		ImageView 		videoIcon;
		TextView 		stampCompanyLink;
		ImageView 		compImage;
		ImageView 		stampImage;
	}

	@Override
	public void onClick(View v) { }

}