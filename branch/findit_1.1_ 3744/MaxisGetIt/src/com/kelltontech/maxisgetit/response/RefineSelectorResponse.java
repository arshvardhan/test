package com.kelltontech.maxisgetit.response;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;
import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.maxisgetit.dao.SelectorDAO;

public class RefineSelectorResponse extends MaxisResponse implements IModel, Parcelable {
	private ArrayList<SelectorDAO> selectors = new ArrayList<SelectorDAO>();

	public ArrayList<SelectorDAO> getSelectors() {
		return selectors;
	}

	public void addSelector(SelectorDAO cat) {
		selectors.add(cat);
	}

	public static final Creator<RefineSelectorResponse> CREATOR = new Creator<RefineSelectorResponse>() {

		@Override
		public RefineSelectorResponse createFromParcel(Parcel source) {
			return new RefineSelectorResponse(source);
		}

		@Override
		public RefineSelectorResponse[] newArray(int size) {
			// TODO Auto-generated method stub
			return new RefineSelectorResponse[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public RefineSelectorResponse(Parcel in) {
		errorMessage = in.readString();
		errorCode = in.readInt();
		in.readTypedList(selectors, SelectorDAO.CREATOR);
	}

	public RefineSelectorResponse() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(errorMessage);
		dest.writeInt(errorCode);
		dest.writeTypedList(selectors);
	}

}
