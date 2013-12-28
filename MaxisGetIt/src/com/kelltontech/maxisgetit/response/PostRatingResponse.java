package com.kelltontech.maxisgetit.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;

public class PostRatingResponse extends MaxisResponse implements Parcelable, IModel{
	
	private float rating;
	private int ratedUserCount;
	
	public PostRatingResponse() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the rating
	 */
	public float getRating() {
		return rating;
	}
	/**
	 * @param rating the rating to set
	 */
	public void setRating(float rating) {
		this.rating = rating;
	}
	/**
	 * @return the ratedUserCount
	 */
	public int getRatedUserCount() {
		return ratedUserCount;
	}
	/**
	 * @param ratedUserCount the ratedUserCount to set
	 */
	public void setRatedUserCount(int ratedUserCount) {
		this.ratedUserCount = ratedUserCount;
	}
	
	

/********** Code for Parcelable **************/
	
	public static final Creator<PostRatingResponse> CREATOR=new Creator<PostRatingResponse>() {

		@Override
		public PostRatingResponse createFromParcel(Parcel source) {
			return new PostRatingResponse(source);
		}

		@Override
		public PostRatingResponse[] newArray(int size) {
			return new PostRatingResponse[size];
		}
	};
	
	public PostRatingResponse(Parcel in) {
		in.readInt();
		in.readFloat();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(ratedUserCount);
		dest.writeFloat(rating);
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
