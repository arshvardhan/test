package com.kelltontech.maxisgetit.response;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;

public class SendSmsResponse extends MaxisResponse implements IModel{
private int statusCode;

public int getStatusCode() {
	return statusCode;
}

public void setStatusCode(int statusCode) {
	this.statusCode = statusCode;
}

}
