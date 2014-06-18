package com.kelltontech.maxisgetit.response;

import java.util.ArrayList;

import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.maxisgetit.dao.CityOrLocality;

public class GenralListResponse extends MaxisResponse{
	private ArrayList<CityOrLocality> objectList;

	public ArrayList<CityOrLocality> getCityOrLocalityList() {
		return objectList;
	}

	public void setCityOrLocalityList(ArrayList<CityOrLocality> objectList) {
//		if(objectList!=null){
//			CityOrLocality temp=new CityOrLocality();
//			temp.setId(-1);
//			temp.setName("Select");
//			objectList.add(0, temp);
//		}
		this.objectList = objectList;
	}

}
