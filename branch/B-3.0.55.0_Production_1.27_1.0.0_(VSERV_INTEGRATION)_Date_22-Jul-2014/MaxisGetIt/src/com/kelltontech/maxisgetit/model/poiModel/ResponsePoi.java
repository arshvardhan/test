package com.kelltontech.maxisgetit.model.poiModel;

import org.json.JSONException;
import org.json.JSONObject;


public class ResponsePoi {

	private int poiCount;
	public int getPoiCount() {
		return poiCount;
	}
	public void setPoiCount(int poiCount) {
		this.poiCount = poiCount;
	}
	public ResponsePoi fromJson(String jsonString)
	{
		try {
			System.out.println("** PoiCount "+jsonString);
			JSONObject jsonObject = new JSONObject(jsonString);
			poiCount=jsonObject.optInt("POIs");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}

}
