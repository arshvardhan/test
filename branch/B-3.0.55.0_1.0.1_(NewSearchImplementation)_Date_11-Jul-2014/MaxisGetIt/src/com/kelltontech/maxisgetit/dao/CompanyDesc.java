package com.kelltontech.maxisgetit.dao;

import java.util.ArrayList;

import com.kelltontech.framework.utils.StringUtil;

import android.os.Parcel;
import android.os.Parcelable;

public class CompanyDesc implements Parcelable {
	private String iconUrl;
	private String compId;
	private String title;
	private String desc;
	private String additionalInfo;
	private String distance;
	private float rating;
	private String city;
	private String state;
	private String attributes;
	private double latitude;
	private double longitude;
	private String pincode;
	private String locality;
	private String contactNo;
	private ArrayList<AttributeGroup> attrGroups=new ArrayList<AttributeGroup>();
	private String cat_id;
	private String compId_catId;
	private String end_date;
	private String valid_in;
	private String video_url;
	
	private String source;
	private String dealDetailUrl;
	
	private boolean isStamp;
	private String stampId;
	private String stampUrl;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDealDetailUrl() {
		return dealDetailUrl;
	}

	public void setDealDetailUrl(String dealDetailUrl) {
		this.dealDetailUrl = dealDetailUrl;
	}

	public String getVideo_url() {
		return video_url;
	}

	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}

	public String getValid_in() {
		return valid_in;
	}

	public void setValid_in(String valid_in) {
		this.valid_in = valid_in;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	private boolean isChecked;

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getCat_id() {
		return cat_id;
	}

	public void setCat_id(String cat_id) {
		this.cat_id = cat_id;
	}
	
	/**
	 * @return the compId_catId
	 */
	public String getCompId_catId() {
		return compId_catId;
	}

	/**
	 * @param compId_catId the compId_catId to set
	 */
	public void setCompId_catId() {
		if(!StringUtil.isNullOrEmpty(compId) && !StringUtil.isNullOrEmpty(cat_id)) {
		this.compId_catId = compId + "-" + cat_id;
		}
	}

	public CompanyDesc() {
		// TODO Auto-generated constructor stub
	}

	public CompanyDesc(Parcel in) {
		iconUrl = in.readString();
		compId = in.readString();
		title = in.readString();
		desc = in.readString();
		additionalInfo = in.readString();
		distance = in.readString();
		rating = in.readFloat();
		city=in.readString();
		state=in.readString();
		attributes=in.readString();
		latitude = in.readDouble();
		longitude = in.readDouble();
		locality = in.readString();
		pincode = in.readString();
		contactNo = in.readString();
		cat_id = in.readString();
		compId_catId = in.readString();
		end_date = in.readString();
		valid_in = in.readString();
		in.readTypedList(attrGroups, AttributeGroup.CREATOR);
		video_url = in.readString();
		source = in.readString();
		dealDetailUrl = in.readString();
		isStamp = in.readInt()==1?true:false;
		stampId = in.readString();
		stampUrl = in.readString();
	}

	public static final Creator<CompanyDesc> CREATOR = new Creator<CompanyDesc>() {

		@Override
		public CompanyDesc createFromParcel(Parcel source) {

			return new CompanyDesc(source);
		}

		@Override
		public CompanyDesc[] newArray(int size) {
			return new CompanyDesc[size];
		}
	};

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}
	public String getAttributes(){
		/*if(attrGroups == null || attrGroups.size() < 0)
		{
			return attributes;
		}
		else{
			attributes = "";
			for(int i = 0; i< attrGroups.size() && i < 2; i++){
				attributes += "<b>" + attrGroups.get(i).getLable() + " : </b>" + attrGroups.get(i).getValues().get(0);
				if(i < 1)
					attributes += "<br>";
			}
		}*/
		
		return attributes;
	}
	public void appendAttributes(String value){
		if(attributes==null){
			attributes=value;
		}else
			attributes+=", "+value;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(iconUrl);
		dest.writeString(compId);
		dest.writeString(title);
		dest.writeString(desc);
		dest.writeString(additionalInfo);
		dest.writeString(distance);
		dest.writeFloat(rating);
		dest.writeString(city);
		dest.writeString(state);
		dest.writeString(attributes);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeString(locality);
		dest.writeString(pincode);
		dest.writeString(contactNo);
		dest.writeString(cat_id);
		dest.writeString(compId_catId);
		dest.writeString(end_date);
		dest.writeString(valid_in);
		dest.writeTypedList(attrGroups);
		dest.writeString(video_url);
		dest.writeString(source);
		dest.writeString(dealDetailUrl);
		dest.writeInt(isStamp?1:0);
		dest.writeString(stampId);
		dest.writeString(stampUrl);
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	
	public ArrayList<AttributeGroup> getAttrGroups() {
		return attrGroups;
	}

	public void addAttrGroups(AttributeGroup attrGroup) {
		this.attrGroups.add(attrGroup);
	}

	public boolean isStamp() {
		return isStamp;
	}

	public void setStamp(int isStamp) {
		if(isStamp==1)
			this.isStamp=true;
		else
			this.isStamp = false;
	}

	public String getStampId() {
		return stampId;
	}

	public void setStampId(String stampId) {
		this.stampId = stampId;
	}

	public String getStampUrl() {
		return stampUrl;
	}

	public void setStampUrl(String stampUrl) {
		this.stampUrl = stampUrl;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
		result = prime * result + ((attrGroups == null) ? 0 : attrGroups.hashCode());
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + ((cat_id == null) ? 0 : cat_id.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((compId == null) ? 0 : compId.hashCode());
		result = prime * result + ((contactNo == null) ? 0 : contactNo.hashCode());
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((distance == null) ? 0 : distance.hashCode());
		result = prime * result + ((iconUrl == null) ? 0 : iconUrl.hashCode());
	/*	result = prime * result + (isChecked ? 1231 : 1237);*/
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((locality == null) ? 0 : locality.hashCode());
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((pincode == null) ? 0 : pincode.hashCode());
		result = prime * result + Float.floatToIntBits(rating);
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompanyDesc other = (CompanyDesc) obj;
		if (additionalInfo == null) {
			if (other.additionalInfo != null)
				return false;
		} else if (!additionalInfo.equals(other.additionalInfo))
			return false;
		if (attrGroups == null) {
			if (other.attrGroups != null)
				return false;
		} else if (!attrGroups.equals(other.attrGroups))
			return false;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (cat_id == null) {
			if (other.cat_id != null)
				return false;
		} else if (!cat_id.equals(other.cat_id))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (compId == null) {
			if (other.compId != null)
				return false;
		} else if (!compId.equals(other.compId))
			return false;
		if (contactNo == null) {
			if (other.contactNo != null)
				return false;
		} else if (!contactNo.equals(other.contactNo))
			return false;
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
		if (distance == null) {
			if (other.distance != null)
				return false;
		} else if (!distance.equals(other.distance))
			return false;
		if (iconUrl == null) {
			if (other.iconUrl != null)
				return false;
		} else if (!iconUrl.equals(other.iconUrl))
			return false;
	/*	if (isChecked != other.isChecked)
			return false;*/
		if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude))
			return false;
		if (locality == null) {
			if (other.locality != null)
				return false;
		} else if (!locality.equals(other.locality))
			return false;
		if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude))
			return false;
		if (pincode == null) {
			if (other.pincode != null)
				return false;
		} else if (!pincode.equals(other.pincode))
			return false;
		if (Float.floatToIntBits(rating) != Float.floatToIntBits(other.rating))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}
