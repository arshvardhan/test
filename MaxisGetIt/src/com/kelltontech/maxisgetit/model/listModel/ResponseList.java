package com.kelltontech.maxisgetit.model.listModel;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.maxisgetit.constants.Events;


public class ResponseList extends MaxisResponse implements Parcelable{
	private ArrayList<IModel> list;
	private int eventType;
	private int totalRecord;
	public int getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	public int getEventType() {
		return eventType;
	}
	public void setEventType(int eventType) {
		this.eventType = eventType;
	}


	public ResponseList fromJson(String jsonString,int eventType)
	{
		try {
			System.out.println("** ResponseList "+jsonString);
			this.eventType=eventType;
			if(eventType==Events.CATEGORY_LIST_EVENT)
			{
				JSONArray jsonArray=new JSONArray(jsonString);
				if(jsonArray.length()>0)
				{
					list=new ArrayList<IModel>();
					for(int index=0;index<jsonArray.length();index++)
					{
						list.add(new Category().fromJson(jsonArray.getString(index)));
					}
				}

			}
			else
			{
				JSONObject jsonObject=new JSONObject(jsonString);
				totalRecord=jsonObject.optInt("total_record");
				JSONArray jsonArray=jsonObject.optJSONArray("companies");
				if(jsonArray!=null&&jsonArray.length()>0)
				{
					list=new ArrayList<IModel>();
					for(int index=0;index<jsonArray.length();index++)
					{
						list.add(new Distance().fromJson(jsonArray.getString(index)));
					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return this;
	}
	public ArrayList<IModel> getList() {
		return list;
	}
	public void setList(ArrayList<IModel> list) {
		this.list = list;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	public ResponseList(){}
	public ResponseList(Parcel parcel){
		totalRecord=parcel.readInt();
		eventType=parcel.readInt();
		int size=parcel.readByte();
		if(size!=0)
		{
			list=new ArrayList<IModel>();
			for(int count=0;count<size;count++)
			{
				if(eventType==Events.CATEGORY_LIST_EVENT)
					list.add(new Category(parcel));
				else
					list.add(new Distance(parcel));
			}
		}
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		if(list!=null&&list.size()>0) 
		{
			dest.writeInt(totalRecord);
			dest.writeInt(eventType);
			dest.writeByte((byte)list.size());
			for(IModel category:list)
			{
				if(eventType==Events.CATEGORY_LIST_EVENT)
					((Category)category).writeToParcel(dest, flags);
				else
					((Distance)category).writeToParcel(dest, flags);	
			}
		}
		else dest.writeByte((byte)0); 
	}
	public final static Parcelable.Creator<ResponseList> CREATOR = new Parcelable.Creator<ResponseList>()
			{
		public ResponseList createFromParcel(Parcel in)
		{
			return new ResponseList(in);
		}
		public ResponseList[] newArray(int size)
		{
			return new ResponseList[size];
		}
			};


}

