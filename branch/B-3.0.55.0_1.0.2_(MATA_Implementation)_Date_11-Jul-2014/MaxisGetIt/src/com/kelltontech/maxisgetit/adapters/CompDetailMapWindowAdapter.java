package com.kelltontech.maxisgetit.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.dao.CompanyDesc;
import com.kelltontech.maxisgetit.dao.CompanyDetail;

public class CompDetailMapWindowAdapter implements InfoWindowAdapter {
	
	private CompanyDetail mCompanyDetail;
	private Context mContext;
	
	public CompDetailMapWindowAdapter(Context pContext) {
		mContext = pContext;
		ImageLoader.initialize(mContext);
	}
	
	public void setData(CompanyDetail pMapData)
	{
		mCompanyDetail = pMapData;
	}

	@Override
	public View getInfoContents(Marker marker) {
		LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View convertView=inflater.inflate(R.layout.map_info_window, null);
		
		TextView infoTitle = (TextView) convertView.findViewById(R.id.mi_comp_title);
		TextView infoLocation = (TextView) convertView.findViewById(R.id.mi_comp_location);
		TextView infoDistance = (TextView) convertView.findViewById(R.id.mi_comp_distance);
		TextView infoPhone = (TextView) convertView.findViewById(R.id.mi_comp_phone);
		ImageView companyImage = (ImageView)  convertView.findViewById(R.id.mi_comp_image);
		infoTitle.setText(marker.getTitle());
		if(!StringUtil.isNullOrEmpty(marker.getSnippet()))
		{
			String[] markerDetails = marker.getSnippet().split(AppConstants.SPLIT_STRING);
			String id = (markerDetails!=null && markerDetails.length > 0 && !StringUtil.isNullOrEmpty(markerDetails[0])) ?  markerDetails[0] : "";
			//CompanyDesc description = getValue(marker.getPosition());
			if(mCompanyDetail != null)
			{
				if(!StringUtil.isNullOrEmpty(mCompanyDetail.getTitle()))
					infoTitle.setText(mCompanyDetail.getTitle());
				if(!StringUtil.isNullOrEmpty(getAddressText(mCompanyDetail)))
					infoLocation.setText(getAddressText(mCompanyDetail));
				if(!StringUtil.isNullOrEmpty(mCompanyDetail.getDistance()))
						infoDistance.setText(mCompanyDetail.getDistance());
				if(!StringUtil.isNullOrEmpty(mCompanyDetail.getCallNumber()))
					infoPhone.setText(mCompanyDetail.getCallNumber());
				ImageLoader.start(mCompanyDetail.getImageUrl(), companyImage);
			}
			else
			{
				convertView = null;
				//infoTitle.setText(marker.getTitle());
			}
		}
		else
		{
			convertView = null;
			//infoTitle.setText(marker.getTitle());
		}
		return convertView;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View convertView=inflater.inflate(R.layout.map_info_window, null);
		
		TextView infoTitle = (TextView) convertView.findViewById(R.id.mi_comp_title);
		TextView infoLocation = (TextView) convertView.findViewById(R.id.mi_comp_location);
		TextView infoDistance = (TextView) convertView.findViewById(R.id.mi_comp_distance);
		TextView infoPhone = (TextView) convertView.findViewById(R.id.mi_comp_phone);
		ImageView companyImage = (ImageView)  convertView.findViewById(R.id.mi_comp_image);
		infoTitle.setText(marker.getTitle());
		if(!StringUtil.isNullOrEmpty(marker.getSnippet()))
		{
			String[] markerDetails = marker.getSnippet().split(AppConstants.SPLIT_STRING);
			String id = (markerDetails!=null && markerDetails.length > 0 && !StringUtil.isNullOrEmpty(markerDetails[0])) ?  markerDetails[0] : "";
			//CompanyDesc description = getValue(marker.getPosition());
			if(mCompanyDetail != null)
			{
				if(!StringUtil.isNullOrEmpty(mCompanyDetail.getTitle()))
					infoTitle.setText(mCompanyDetail.getTitle());
				if(!StringUtil.isNullOrEmpty(getAddressText(mCompanyDetail)))
					infoLocation.setText(getAddressText(mCompanyDetail));
				if(!StringUtil.isNullOrEmpty(mCompanyDetail.getDistance()))
						infoDistance.setText(mCompanyDetail.getDistance());
				if(!StringUtil.isNullOrEmpty(mCompanyDetail.getContacts().get(0)))
					infoPhone.setText(mCompanyDetail.getContacts().get(0));
				ImageLoader.start(mCompanyDetail.getImageUrl(), companyImage);
			}
			else
			{
				convertView = null;
				//infoTitle.setText(marker.getTitle());
			}
		}
		else
		{
			convertView = null;
			//infoTitle.setText(marker.getTitle());
		}
		return convertView;
	}
	
	
	private String getAddressText(CompanyDetail compDetail) {
		//String str = "";
		String adddress = "";
		adddress += compDetail.getLocality()!= null ? compDetail.getLocality() + ", " : "";
		//adddress += companyDesc.getCity() + ", ";
		//adddress += companyDesc.getState() + " ";
		adddress += compDetail.getPincode()!= null ? compDetail.getPincode() : "";
		return adddress;

	}

}

