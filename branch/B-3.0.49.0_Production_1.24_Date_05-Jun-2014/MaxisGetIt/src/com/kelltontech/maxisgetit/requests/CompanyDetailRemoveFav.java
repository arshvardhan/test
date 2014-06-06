package com.kelltontech.maxisgetit.requests;

/**
 * @author Arsh Vardhan Atreya
 * @email arshvardhan.atreya@kelltontech.com
 */

public class CompanyDetailRemoveFav {

		private String userId;
		private String companyId;
		private String categoryId;
		
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getCompanyId() {
			return companyId;
		}
		public void setCompanyId(String companyId) {
			this.companyId = companyId;
		}
		public String getCategoryId() {
			return categoryId;
		}
		public void setCategoryId(String categoryId) {
			this.categoryId = categoryId;
		}
	
}
