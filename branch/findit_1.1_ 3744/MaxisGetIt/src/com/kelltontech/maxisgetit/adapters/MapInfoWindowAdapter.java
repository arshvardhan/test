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

public class MapInfoWindowAdapter implements InfoWindowAdapter {
	
	private ArrayList<CompanyDesc> mCompanyDetailList;
	private Context mContext;
	
	public MapInfoWindowAdapter(Context pContext) {
		mContext = pContext;
		ImageLoader.initialize(mContext);
	}
	
	public void setData(ArrayList<CompanyDesc> pMapData)
	{
		mCompanyDetailList = pMapData;
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
			CompanyDesc description = getValue(id);
			//CompanyDesc description = getValue(marker.getPosition());
			if(description != null)
			{
				if(!StringUtil.isNullOrEmpty(description.getTitle()))
					infoTitle.setText(description.getTitle());
				if(!StringUtil.isNullOrEmpty(getAddressText(description)))
					infoLocation.setText(getAddressText(description));
				if(!StringUtil.isNullOrEmpty(description.getDistance()))
						infoDistance.setText(description.getDistance());
				if(!StringUtil.isNullOrEmpty(description.getContactNo()))
					infoPhone.setText(description.getContactNo());
				ImageLoader.start(description.getIconUrl(), companyImage);
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
			CompanyDesc description = getValue(id);
			//CompanyDesc description = getValue(marker.getPosition());
			if(description != null)
			{
				if(!StringUtil.isNullOrEmpty(description.getTitle()))
					infoTitle.setText(description.getTitle());
				if(!StringUtil.isNullOrEmpty(getAddressText(description)))
					infoLocation.setText(getAddressText(description));
				if(!StringUtil.isNullOrEmpty(description.getDistance()))
						infoDistance.setText(description.getDistance());
				if(!StringUtil.isNullOrEmpty(description.getContactNo()))
					infoPhone.setText(description.getContactNo());
				ImageLoader.start(description.getIconUrl(), companyImage);
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
	
	public CompanyDesc getValue(String id)
	{
		CompanyDesc desc = null;
		for(int i =0; i < mCompanyDetailList.size(); i++)
		{
			if(mCompanyDetailList.get(i).getCompId().equalsIgnoreCase(id))//get(i).getLatitude() == position.latitude && mCompanyDetailList.get(i).getLongitude() == position.longitude
			{
				desc = mCompanyDetailList.get(i);
				return desc;
			}
		}
		return desc;
	}
	
	private String getAddressText(CompanyDesc compDetail) {
		//String str = "";
		String adddress = "";
		adddress += compDetail.getLocality()!= null ? compDetail.getLocality() + ", " : "";
		//adddress += companyDesc.getCity() + ", ";
		//adddress += companyDesc.getState() + " ";
		adddress += compDetail.getPincode()!= null ? compDetail.getPincode() : "";
		return adddress;

	}

}
