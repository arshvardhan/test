package com.kelltontech.maxisgetit.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.CompanyListAdapter.Model;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.dao.CompanyDesc;
import com.kelltontech.maxisgetit.dao.OutLet;
import com.kelltontech.maxisgetit.dao.OutLetDetails;

public class DealMapInfoWindowAdapter implements InfoWindowAdapter {
	
	private ArrayList<OutLet> mOutLetDetailList;
	private Context mContext;
	
	public DealMapInfoWindowAdapter(Context pContext) {
		mContext = pContext;
		ImageLoader.initialize(mContext);
	}
	
	public void setData(ArrayList<OutLet> pMapData)
	{
		mOutLetDetailList = pMapData;
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
			OutLet description = getValue(id);
			//CompanyDesc description = getValue(marker.getPosition());
			if(description != null)
			{
				if(!StringUtil.isNullOrEmpty(description.getTitle()))
					infoTitle.setText(description.getTitle());
				if(!StringUtil.isNullOrEmpty(description.getAddress()))
					infoLocation.setText(description.getAddress());
				if(!StringUtil.isNullOrEmpty(description.getPhone_no()))
					infoPhone.setText(description.getPhone_no());
				ImageLoader.start(description.getIcon_url(), companyImage);
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
			OutLet description = getValue(id);
			//CompanyDesc description = getValue(marker.getPosition());
			if(description != null)
			{
				if(!StringUtil.isNullOrEmpty(description.getTitle()))
					infoTitle.setText(description.getTitle());
				if(!StringUtil.isNullOrEmpty(description.getAddress()))
					infoLocation.setText(description.getAddress());
				if(!StringUtil.isNullOrEmpty(description.getPhone_no()))
					infoPhone.setText(description.getPhone_no());
				ImageLoader.start(description.getIcon_url(), companyImage);
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
	
	public OutLet getValue(String id)
	{
		OutLet desc = null;
		for(int i =0; i < mOutLetDetailList.size(); i++)
		{
			if(mOutLetDetailList.get(i).getId().equalsIgnoreCase(id))//get(i).getLatitude() == position.latitude && mCompanyDetailList.get(i).getLongitude() == position.longitude
			{
				desc = mOutLetDetailList.get(i);
				return desc;
			}
		}
		return desc;
	}
	

}
