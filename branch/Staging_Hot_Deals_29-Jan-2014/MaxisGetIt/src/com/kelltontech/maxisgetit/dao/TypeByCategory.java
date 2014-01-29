package com.kelltontech.maxisgetit.dao;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Arsh Vardhan Atreya
 * @email arshvardhan.atreya@kelltontech.com
 */

public class TypeByCategory implements Parcelable {
	private String type;
	private String label;
	private ArrayList<String> typeLabelList = new ArrayList<String>();

	public TypeByCategory(Parcel source) {
		type = source.readString();
		label = source.readString();
		source.readStringList(typeLabelList);
	}

	public TypeByCategory() {
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(type);
		dest.writeString(label);
		dest.writeStringList(typeLabelList);
	}

	public static final Creator<TypeByCategory> CREATOR = new Creator<TypeByCategory>() {

		@Override
		public TypeByCategory createFromParcel(Parcel source) {
			return new TypeByCategory(source);
		}

		@Override
		public TypeByCategory[] newArray(int size) {
			return new TypeByCategory[size];
		}
	};

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public ArrayList<String> gettypelabelList() {
		return typeLabelList;
	}

	public void insertDefaultSelectionlabel() {
		if (typeLabelList.isEmpty() || !typeLabelList.get(0).equals("Select"))
			this.typeLabelList.add(0, "Select");
	}

	public void addtypelabelList(String string) {
		this.typeLabelList.add(string);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public String toString() {
		return label;
	}

}
