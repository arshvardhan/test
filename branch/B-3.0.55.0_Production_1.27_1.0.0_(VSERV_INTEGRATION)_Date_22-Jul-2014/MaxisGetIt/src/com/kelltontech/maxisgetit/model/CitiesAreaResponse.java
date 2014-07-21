package com.kelltontech.maxisgetit.model;


public class CitiesAreaResponse {

	// {"Results":{"Error_Code":0,"Total_Records_Found":2,"Cities":{"City":["Kuala Lumpur","Petaling Jaya"]}}}

	private String Error_Code;
	private String Total_Records_Found;
	private String Error_Message;
	private Cities Cities;
	private Localities Localities;

	public Localities getLocalities() {
		return Localities;
	}

	public void setLocalities(Localities localities) {
		Localities = localities;
	}

	public Cities getCities() {
		return Cities;
	}

	public void setCities(Cities cities) {
		this.Cities = cities;
	}

	public String getError_Message() {
		return Error_Message;
	}

	public void setError_Message(String error_Message) {
		Error_Message = error_Message;
	}

	public String getError_Code() {
		return Error_Code;
	}

	public void setError_Code(String error_Code) {
		Error_Code = error_Code;
	}

	public String getTotal_Records_Found() {
		return Total_Records_Found;
	}

	public void setTotal_Records_Found(String total_Records_Found) {
		Total_Records_Found = total_Records_Found;
	}
}


