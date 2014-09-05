/**
 * Request.java
 * © DbyDx Software ltd. @2011 - 2011+1
 * Confidential and proprietary.
 */
package com.kelltontech.framework.model;

import java.util.Hashtable;

import com.kelltontech.framework.parser.IParser;

/**
 */
public class Request {
	public static final int TYPE_GET = 0; // For Get Request
	public static final int TYPE_POST = 1; // For Post Request
	public static final int TYPE_MULTI_PART = 2; // For Post Request
	public static final int TYPE_LOG = 3;

	private String uri;
	private int requestId;
	private int requestType;
	private int timeoutSeconds;
	private Hashtable<String, String> properties;
	private Hashtable<String, String> header;
	private int priority;
	private IParser parser;
	private byte[] payload;

	public Request() {
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public int getRequestType() {
		return requestType;
	}

	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}

	public int getTimeoutSeconds() {
		return timeoutSeconds;
	}

	public void setTimeoutSeconds(int timeoutSeconds) {
		this.timeoutSeconds = timeoutSeconds;
	}

	public Hashtable<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Hashtable<String, String> properties) {
		this.properties = properties;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public IParser getParser() {
		return parser;
	}

	public void setParser(IParser parser) {
		this.parser = parser;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public Hashtable<String, String> getHeader() {
		return header;
	}

	public void setHeader(Hashtable<String, String> header) {
		this.header = header;
	}

}
