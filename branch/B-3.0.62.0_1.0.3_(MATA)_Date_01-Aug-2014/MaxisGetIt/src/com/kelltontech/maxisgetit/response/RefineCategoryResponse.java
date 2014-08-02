package com.kelltontech.maxisgetit.response;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;
import com.kelltontech.maxisgetit.dao.CategoryRefine;

public class RefineCategoryResponse implements Parcelable {
	private ArrayList<CategoryRefine> categories = new ArrayList<CategoryRefine>();
	private int selectedCategory;
	public ArrayList<CategoryRefine> getCategories() {
		return categories;
	}
	public void setCategories(ArrayList<CategoryRefine> categories){
		this.categories=categories;
		if(this.categories.size()>0 && !((CategoryRefine)this.categories.get(0)).getCategoryId().equals("-1")){
		CategoryRefine catrf=new CategoryRefine();
		catrf.setCategoryId("-1");
		catrf.setCategoryTitle("Select");
		this.categories.add(0, catrf);
		}
	}
	public void addCategory(CategoryRefine cat) {
		categories.add(cat);
	}

	public static final Creator<RefineCategoryResponse> CREATOR = new Creator<RefineCategoryResponse>() {

		@Override
		public RefineCategoryResponse createFromParcel(Parcel source) {
			return new RefineCategoryResponse(source);
		}

		@Override
		public RefineCategoryResponse[] newArray(int size) {
			// TODO Auto-generated method stub
			return new RefineCategoryResponse[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public RefineCategoryResponse(Parcel in) {
		in.readTypedList(categories, CategoryRefine.CREATOR);
		selectedCategory=in.readInt();
	}

	public int getSelectedCategoryIndex() {
		return selectedCategory;
	}

	public void setSelectedCategoryIndex(int selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public RefineCategoryResponse() {
		CategoryRefine catrf=new CategoryRefine();
		catrf.setCategoryId("-1");
		catrf.setCategoryTitle("Select");
		categories.add(catrf);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(categories);
		dest.writeInt(selectedCategory);
	}

}
