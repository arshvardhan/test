package com.kelltontech.maxisgetit.response;

	import java.util.ArrayList;

import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.maxisgetit.dao.PostDealCityOrLoc;

	public class PostDealCityLocListResponse extends MaxisResponse{
		private ArrayList<PostDealCityOrLoc> objectList;

		public ArrayList<PostDealCityOrLoc> getCityOrLocalityList() {
			return objectList;
		}

		public void setCityOrLocalityList(ArrayList<PostDealCityOrLoc> objectList) {
			this.objectList = objectList;
		}

	}