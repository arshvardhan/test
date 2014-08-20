package com.kelltontech.maxisgetit.dao.matta;

import com.kelltontech.framework.model.IModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author arsh.vardhan
 * @modified 02-Aug-2014
 */
public class MattaHallList implements IModel, Parcelable {

	private String mHallId 				= "";
	private String mHallName 			= "";
	private String mHallBuildingName	= "";
	private String mHallTheme 			= "";
	private String mHallImage 			= "";
	private String mHallSource 			= "";
	private String mHallListType 		= "";
	
	public MattaHallList(Parcel in) {
		mHallId = in.readString();
		mHallName = in.readString();
		mHallBuildingName = in.readString();
		mHallTheme = in.readString();
		mHallImage=in.readString();
		mHallSource=in.readString();
		mHallListType=in.readString();
	}

	public MattaHallList() {
	}

	@Override
	public int describeContents() {
		return 0;
	}


	public static final Creator<MattaHallList> CREATOR = new Creator<MattaHallList>() {

		@Override
		public MattaHallList createFromParcel(Parcel source) {
			return new MattaHallList(source);
		}

		@Override
		public MattaHallList[] newArray(int size) {
			return new MattaHallList[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mHallId +"");
		dest.writeString(mHallName +"");
		dest.writeString(mHallBuildingName +"");
		dest.writeString(mHallTheme +"");
		dest.writeString(mHallImage +"");
		dest.writeString(mHallSource +"");
		dest.writeString(mHallListType +"");
	}
	
	
	public String getmHallId() {
		return mHallId;
	}

	public void setmHallId(String mHallId) {
		this.mHallId = mHallId;
	}

	public String getmHallName() {
		return mHallName;
	}

	public void setmHallName(String mHallName) {
		this.mHallName = mHallName;
	}

	public String getmHallBuildingName() {
		return mHallBuildingName;
	}

	public void setmHallBuildingName(String mHallBuildingName) {
		this.mHallBuildingName = mHallBuildingName;
	}

	public String getmHallTheme() {
		return mHallTheme;
	}

	public void setmHallTheme(String mHallTheme) {
		this.mHallTheme = mHallTheme;
	}

	public String getmHallImage() {
		return mHallImage;
	}

	public void setmHallImage(String mHallImage) {
		this.mHallImage = mHallImage;
	}

	public String getmHallSource() {
		return mHallSource;
	}

	public void setmHallSource(String mHallSource) {
		this.mHallSource = mHallSource;
	}

	public String getmHallListType() {
		return mHallListType;
	}

	public void setmHallListType(String mHallListType) {
		this.mHallListType = mHallListType;
	}

}