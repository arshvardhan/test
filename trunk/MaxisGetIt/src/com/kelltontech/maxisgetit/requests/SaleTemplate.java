package com.kelltontech.maxisgetit.requests;

/**
 * @author Arsh Vardhan Atreya
 * @email arshvardhan.atreya@kelltontech.com
 */

public class SaleTemplate {

		private String categoryId;
		private String templateType;
		
		public String getCategoryId() {
			return categoryId;
		}
		public void setCategoryId(String categoryId) {
			this.categoryId = categoryId;
		}
		public String getTemplateType() {
			return templateType;
		}
		public void setTemplateType(String templateType) {
			this.templateType = templateType;
		}
	
}
