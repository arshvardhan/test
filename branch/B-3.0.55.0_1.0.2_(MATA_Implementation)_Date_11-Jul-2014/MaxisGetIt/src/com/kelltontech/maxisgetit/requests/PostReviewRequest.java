package com.kelltontech.maxisgetit.requests;

/**
 * @author monish.agarwal
 */
public class PostReviewRequest 
{
	private String rating;
	private String 	review;
	private String userId;
	private String compId;
	private String catId;
	private String mobileNumber;
	private String userName;
	/**
	 * @return the rating
	 */
	public String getRating() {
		return rating;
	}
	/**
	 * @param rating the rating to set
	 */
	public void setRating(String rating) {
		this.rating = rating;
	}
	/**
	 * @return the review
	 */
	public String getReview() {
		return review;
	}
	/**
	 * @param review the review to set
	 */
	public void setReview(String review) {
		this.review = review;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the compId
	 */
	public String getCompId() {
		return compId;
	}
	/**
	 * @param compId the compId to set
	 */
	public void setCompId(String compId) {
		this.compId = compId;
	}
	/**
	 * @return the catId
	 */
	public String getCatId() {
		return catId;
	}
	/**
	 * @param catId the catId to set
	 */
	public void setCatId(String catId) {
		this.catId = catId;
	}
	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}
	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
