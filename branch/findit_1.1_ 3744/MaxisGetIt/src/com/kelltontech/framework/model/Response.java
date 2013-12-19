/**
 * Response.java
 * © DbyDx Software ltd. @2011 - 2011+1
 * Confidential and proprietary.
 */
package com.kelltontech.framework.model;

/**
 *
 */
public class Response {
	public static final int OK = 0;
	public static final int EXCEPTION = 1;

	private String responseText;
	private IModel payload;
	private String errorText;
	private int errorCode;
	private int requestId;
	private Exception exception;

	public Response() {
	}

	public Response(IModel payload) {
		this.payload = payload;
	}

	public void setRequestId(int v) {
		requestId = v;
	}

	public int getRequestId() {
		return requestId;
	}

	public boolean isError() {
		return getErrorCode() > 0;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorText() {
		return errorText;
	}

	public void setPayload(IModel model) {
		this.payload = model;
	}

	public IModel getPayload() {
		return payload;
	}

	public String getResponseText() {
		return responseText;
	}

	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}
}
