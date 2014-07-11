package com.kelltontech.maxisgetit.response;

import java.util.ArrayList;

import org.w3c.dom.Document;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.maxisgetit.controllers.GMapV2Direction;

public class RouteDetailResponse extends MaxisResponse implements Parcelable, IModel {
	public static final String USER_TYPE_COMPANY = "Company";
	private ArrayList<LatLng> routeLatlngList = new ArrayList<LatLng>();

	public ArrayList<LatLng> getRouteLatlngList() {
		return routeLatlngList;
	}

	public void setRouteLatlngList(ArrayList<LatLng> routeLatlngList) {
		this.routeLatlngList = routeLatlngList;
	}

	public RouteDetailResponse(Document doc) {
		GMapV2Direction direction=new GMapV2Direction();
		routeLatlngList=direction.getDirection(doc);
	}

	public RouteDetailResponse(Parcel in) {
		errorMessage = in.readString();
		errorCode = in.readInt();
		in.readTypedList(routeLatlngList, LatLng.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(errorMessage);
		dest.writeInt(errorCode);
		dest.writeTypedList(routeLatlngList);
	}

	public static final Creator<RouteDetailResponse> CREATOR = new Creator<RouteDetailResponse>() {

		@Override
		public RouteDetailResponse createFromParcel(Parcel source) {
			return new RouteDetailResponse(source);
		}

		@Override
		public RouteDetailResponse[] newArray(int size) {
			return new RouteDetailResponse[size];
		}
	};
}
