package com.kelltontech.maxisgetit.response;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.maxisgetit.dao.TypeByCategory;

/**
 * @author Arsh Vardhan Atreya
 * @email arshvardhan.atreya@kelltontech.com
 */

public class TypeByCategoryResponse extends MaxisResponse implements IModel, Parcelable {

	private ArrayList<TypeByCategory> typeByCategoryList = new ArrayList<TypeByCategory>();

	public TypeByCategoryResponse(Parcel source) {
		errorMessage = source.readString();
		errorCode = source.readInt();
		source.readTypedList(typeByCategoryList, TypeByCategory.CREATOR);
	}

	public TypeByCategoryResponse() {
		TypeByCategory typeByCat = new TypeByCategory();
		typeByCat.setLabel("Select");
		typeByCat.setType("-1");
		this.typeByCategoryList.add(0, typeByCat);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(errorMessage);
		dest.writeInt(errorCode);
		dest.writeTypedList(typeByCategoryList);
	}

	public static final Creator<TypeByCategoryResponse> CREATOR = new Creator<TypeByCategoryResponse>() {

		@Override
		public TypeByCategoryResponse createFromParcel(Parcel source) {
			return new TypeByCategoryResponse(source);
		}

		@Override
		public TypeByCategoryResponse[] newArray(int size) {
			return new TypeByCategoryResponse[size];
		}
	};

	public ArrayList<TypeByCategory> getTypeByCategoryList() {
		return typeByCategoryList;
	}

	public void addTypeByCategory(TypeByCategory typeByCategory) {
		this.typeByCategoryList.add(typeByCategory);
	}

	public void setTypeByCategoryList(ArrayList<TypeByCategory> typeByCategory) {
		this.typeByCategoryList = typeByCategory;
		if (this.typeByCategoryList.size() > 0 && !((TypeByCategory) this.typeByCategoryList.get(0)).getLabel().equals("-1")) {
			TypeByCategory typeByCat = new TypeByCategory();
			typeByCat.setLabel("Select");
			typeByCat.setType("-1");
			this.typeByCategoryList.add(0, typeByCat);
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

}
