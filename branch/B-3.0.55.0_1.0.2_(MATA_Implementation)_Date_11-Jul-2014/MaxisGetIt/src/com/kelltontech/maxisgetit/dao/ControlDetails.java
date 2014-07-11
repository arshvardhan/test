package com.kelltontech.maxisgetit.dao;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class ControlDetails implements Parcelable {
	private String searchKey;
	private String controlType;
	private int controlTypeId;
	private String displayName;
	private String fieldId;
	private String columnId;
	private int displayOrder;
	private int searchable;
	private int required;
	private ArrayList<String> values=new ArrayList<String>();

	public ControlDetails(Parcel source) {
		searchKey = source.readString();
		controlType = source.readString();
		controlTypeId = source.readInt();
		displayName = source.readString();
		fieldId = source.readString();
		columnId = source.readString();
		displayOrder = source.readInt();
		searchable = source.readInt();
		required = source.readInt();
		source.readStringList(values);
	}

	public ControlDetails() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(searchKey);
		dest.writeString(controlType);
		dest.writeInt(controlTypeId);
		dest.writeString(displayName);
		dest.writeString(fieldId);
		dest.writeString(columnId);
		dest.writeInt(displayOrder);
		dest.writeInt(searchable);
		dest.writeInt(required);
		dest.writeStringList(values);
	}

	public static final Creator<ControlDetails> CREATOR = new Creator<ControlDetails>() {

		@Override
		public ControlDetails createFromParcel(Parcel source) {
			return new ControlDetails(source);
		}

		@Override
		public ControlDetails[] newArray(int size) {
			return new ControlDetails[size];
		}
	};

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public String getControlType() {
		return controlType;
	}

	public void setControlType(String controlType) {
		this.controlType = controlType;
	}

	public int getControlTypeId() {
		return controlTypeId;
	}

	public void setControlTypeId(int controlTypeId) {
		this.controlTypeId = controlTypeId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public int getSearchable() {
		return searchable;
	}

	public void setSearchable(int searchable) {
		this.searchable = searchable;
	}

	public int getRequired() {
		return required;
	}

	public void setRequired(int required) {
		this.required = required;
	}

	public ArrayList<String> getValues() {
		return values;
	}
	public void insertDefaultSelectionValue() {
		if(values.isEmpty()||!values.get(0).equals("Select"))
		this.values.add(0,"Select");
	}
	public void addValues(String string) {
		this.values.add(string);
	}

	@Override
	public int describeContents() {
		return 0;
	}

}
