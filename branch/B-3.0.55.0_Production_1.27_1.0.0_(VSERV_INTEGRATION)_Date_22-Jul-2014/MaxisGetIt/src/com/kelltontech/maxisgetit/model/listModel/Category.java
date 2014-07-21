package com.kelltontech.maxisgetit.model.listModel;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.kelltontech.framework.model.IModel;

public class Category  implements Parcelable,IModel{
	private String categoryId;
	private String name;
	private int poiCount;
	public int getPoiCount() {
		return poiCount;
	}
	public void setPoiCount(int poiCount) {
		this.poiCount = poiCount;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	private int count;
	public Category(){}
	
	public Category(Parcel parcel)
	{
		categoryId=parcel.readString();
		name=parcel.readString();
		count=parcel.readInt();
		poiCount=parcel.readInt();
	}
	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		parcel.writeString(categoryId);
		parcel.writeString(name);
		parcel.writeInt(count);
		parcel.writeInt(poiCount);
	}
	
	
	public Category fromJson(String jsonString)
	{
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			categoryId=jsonObject.optString("cat_id");
			name=jsonObject.optString("name");
			count=jsonObject.optInt("count");
			poiCount=jsonObject.optInt("POIs");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}
	public final static Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>()
			{
		public Category createFromParcel(Parcel in)
		{
			return new Category(in);
		}
		public Category[] newArray(int size)
		{
			return new Category[size];
		}
			};
			@Override
			public int describeContents() {
				return 0;
			}

}
