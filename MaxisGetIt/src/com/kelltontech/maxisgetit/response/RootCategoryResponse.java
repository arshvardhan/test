package com.kelltontech.maxisgetit.response;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.Response;
import com.kelltontech.maxisgetit.dao.CategoryGroup;
import com.kelltontech.maxisgetit.dao.SubCategory;

public class RootCategoryResponse extends MaxisResponse implements IModel, Parcelable {
	private ArrayList<CategoryGroup> categories = new ArrayList<CategoryGroup>();

	public ArrayList<CategoryGroup> getCategories() {
		return categories;
	}

	public void addCategory(CategoryGroup cat) {
		categories.add(cat);
	}

	public static final Creator<RootCategoryResponse> CREATOR = new Creator<RootCategoryResponse>() {

		@Override
		public RootCategoryResponse createFromParcel(Parcel source) {
			return new RootCategoryResponse(source);
		}

		@Override
		public RootCategoryResponse[] newArray(int size) {
			// TODO Auto-generated method stub
			return new RootCategoryResponse[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public RootCategoryResponse(Parcel in) {
		errorMessage = in.readString();
		errorCode = in.readInt();
		in.readTypedList(categories, CategoryGroup.CREATOR);
	}

	public RootCategoryResponse() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(errorMessage);
		dest.writeInt(errorCode);
		dest.writeTypedList(categories);
	}

}
