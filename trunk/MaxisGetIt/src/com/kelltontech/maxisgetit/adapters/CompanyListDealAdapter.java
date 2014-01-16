package com.kelltontech.maxisgetit.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.dao.CompanyDesc;
import com.kelltontech.maxisgetit.ui.activities.DealsActivity;

public class CompanyListDealAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<CompanyDesc> list;
	private Drawable dummyDrawable;
	private Drawable errorDrawable;
	private boolean isCompanyListing;
	public CompanyListDealAdapter(Context context,boolean isCompanyType) {
		mContext = context;
		isCompanyListing = isCompanyType;
		ImageLoader.initialize(mContext);
		if(isCompanyType){
			dummyDrawable=mContext.getResources().getDrawable(R.drawable.comp_list_loading);
			errorDrawable=mContext.getResources().getDrawable(R.drawable.comp_list_cross);
		}else{
			dummyDrawable=mContext.getResources().getDrawable(R.drawable.deal_list_loading);
			errorDrawable=mContext.getResources().getDrawable(R.drawable.deal_list_noimage);
		}
	}
	public void setData(ArrayList<CompanyDesc> compDescList) {
		list=compDescList;
	}
	@Override
	public int getCount() {
		if(list!=null)
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
			LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.hotdeal_listitem, null);
			model=new Model();
			model.dealImage=(ImageView) convertView.findViewById(R.id.deals_image);
			model.dealTitle=(TextView) convertView.findViewById(R.id.deal_title);
			model.dealValidArea=(TextView) convertView.findViewById(R.id.validity_area);
			model.dealValidDate=(TextView) convertView.findViewById(R.id.validity_expires);
			model.dealDownload = (Button) convertView.findViewById(R.id.download_btn);
			model.arrow = (ImageView) convertView.findViewById(R.id.arrow);
			model.baseLayout = (RelativeLayout)convertView.findViewById(R.id.hotdeal);
			convertView.setTag(model);
		}else{
			model=(Model) convertView.getTag();
		}
		final CompanyDesc compDesc=list.get(position);
		if(compDesc.getCompId().equalsIgnoreCase("-1"))
		{
			 int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30,mContext.getResources().getDisplayMetrics());
			 model.dealImage.setVisibility(View.GONE);
			 model.dealTitle.setVisibility(View.INVISIBLE);
			 model.dealValidArea.setVisibility(View.INVISIBLE);
			 model.dealValidDate.setVisibility(View.INVISIBLE);
			 model.dealDownload.setVisibility(View.GONE);
			 model.arrow.setVisibility(View.GONE);
		}
		else
		{
			 model.dealImage.setVisibility(View.VISIBLE);
			 model.dealTitle.setVisibility(View.VISIBLE);
			 model.dealValidArea.setVisibility(View.VISIBLE);
			 model.dealValidDate.setVisibility(View.VISIBLE);
			 model.dealDownload.setVisibility(View.VISIBLE);
			 model.arrow.setVisibility(View.VISIBLE);
			ImageLoader.start(compDesc.getIconUrl()	, model.dealImage, dummyDrawable, errorDrawable);
			if(!StringUtil.isNullOrEmpty(compDesc.getTitle()))
				model.dealTitle.setText(Html.fromHtml(compDesc.getTitle()));
			else
				model.dealTitle.setText(Html.fromHtml(""));
			
			
			if(!StringUtil.isNullOrEmpty(compDesc.getEnd_date()))
				model.dealValidDate.setText(Html.fromHtml("till "+compDesc.getEnd_date()));
			else
				model.dealValidDate.setText(Html.fromHtml(""));
			
			String city_state=(compDesc.getLocality()!=null)?compDesc.getLocality():"";
			if(city_state.trim().length()>0 && compDesc.getState().trim().length()>0 )
				city_state += ", "+ ((compDesc.getCity()!=null)?compDesc.getCity():"");
			else
				city_state += ((compDesc.getCity()!=null)?compDesc.getCity():"");
			if(city_state.trim().length()>0 && compDesc.getState().trim().length()>0)
				city_state+=", "+compDesc.getState();
			else if(city_state.trim().length()==0)
				city_state=compDesc.getState();
			
			if(!StringUtil.isNullOrEmpty(compDesc.getValid_in()))
				model.dealValidArea.setText("Valid in "+compDesc.getValid_in());
			else
				model.dealValidArea.setText(Html.fromHtml(""));
			
			/*String city_state=(compDesc.getCity()!=null)?compDesc.getCity():"";
			if(city_state.trim().length()>0 && compDesc.getState().trim().length()>0)
				city_state+=", "+compDesc.getState();
			else if(city_state.trim().length()==0)
				city_state=compDesc.getState();*/
			
//			TODO:: for locality 
/*			if(!StringUtil.isNullOrEmpty(city_state))
				model.compDesc.setText(city_state);
			else
				model.compDesc.setText("");*/
			
			/*if(!StringUtil.isNullOrEmpty(compDesc.getAttributes()))
				model.compAdditionalInfo.setText(Html.fromHtml(compDesc.getAttributes()));
			else
				model.compAdditionalInfo.setText("");*/
		}
		
		model.dealDownload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				DealsActivity activity = (DealsActivity)mContext;
				activity.getDownloadDetails(mContext,compDesc.getCompId());
			}
		});
		
		
		return convertView;
	}

	class Model {
		ImageView dealImage;
		TextView dealTitle;
		TextView dealValidArea;
		TextView dealValidDate;
		Button dealDownload;
		RelativeLayout baseLayout;
		ImageView arrow;
	}

}
