package com.kelltontech.maxisgetit.response;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.maxisgetit.dao.CategoryGroup;
import com.kelltontech.maxisgetit.dao.SubCategory;

public class SubCategoryResponse extends MaxisResponse implements IModel, Parcelable {
	private CategoryGroup parentCat;
	private ArrayList<SubCategory> categories = new ArrayList<SubCategory>();

	public ArrayList<SubCategory> getCategories() {
		return categories;
	}

	public void setParentCategory(CategoryGroup cat) {
		parentCat = cat;
	}

	public CategoryGroup getParentCategory() {
		return parentCat;
	}

	public void addSubCategory(SubCategory cat) {
		categories.add(cat);
	}

	public static final Creator<SubCategoryResponse> CREATOR = new Creator<SubCategoryResponse>() {

		@Override
		public SubCategoryResponse createFromParcel(Parcel source) {
			return new SubCategoryResponse(source);
		}

		@Override
		public SubCategoryResponse[] newArray(int size) {
			// TODO Auto-generated method stub
			return new SubCategoryResponse[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public SubCategoryResponse(Parcel in) {
		errorMessage = in.readString();
		errorCode = in.readInt();
		in.readStringList(banner);
		parentCat = in.readParcelable(SubCategory.class.getClassLoader());
		in.readTypedList(categories, SubCategory.CREATOR);
	}

	public SubCategoryResponse() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(errorMessage);
		dest.writeInt(errorCode);
		dest.writeStringList(banner);
		dest.writeParcelable(parentCat, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeTypedList(categories);
	}

}
