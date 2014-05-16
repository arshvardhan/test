package com.kelltontech.maxisgetit.dao;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;

public class OutLetDetails extends MaxisResponse implements Parcelable, IModel {

	private ArrayList<OutLet> outlet = new ArrayList<OutLet>();
	private String total_records;
	private int page_number;
	private int records_per_page;

	public int getPage_number() {
		return page_number;
	}

	public void setPage_number(int page_number) {
		this.page_number = page_number;
	}

	public int getRecords_per_page() {
		return records_per_page;
	}

	public void setRecords_per_page(int records_per_page) {
		this.records_per_page = records_per_page;
	}

	public String getTotal_records() {
		return total_records;
	}

	public void setTotal_records(String total_records) {
		this.total_records = total_records;
	}

	public ArrayList<OutLet> getOutlet() {
		return outlet;
	}

	public OutLetDetails() {

	}

	public void setOutlet(ArrayList<OutLet> outlet) {
		this.outlet = outlet;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public OutLetDetails(Parcel in) {
		in.readTypedList(outlet, OutLet.CREATOR);
		page_number = in.readInt();
		records_per_page = in.readInt();
		total_records = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(outlet);
		dest.writeInt(page_number);
		dest.writeInt(records_per_page);
		dest.writeString(total_records);
	}

	public static final Creator<OutLetDetails> CREATOR = new Creator<OutLetDetails>() {

		@Override
		public OutLetDetails createFromParcel(Parcel source) {
			return new OutLetDetails(source);
		}

		@Override
		public OutLetDetails[] newArray(int size) {
			return new OutLetDetails[size];
		}
	};

	public void appendOutletListAtEnd(ArrayList<OutLet> outletList) {

		outlet.addAll(outletList);
	}

	public void setOutletList(ArrayList<OutLet> outletList) {
		outlet = outletList;
	}
}
