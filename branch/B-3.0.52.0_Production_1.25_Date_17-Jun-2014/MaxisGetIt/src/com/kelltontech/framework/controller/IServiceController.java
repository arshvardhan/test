/**
 * IServiceController.java
 * © DbyDx Software ltd. @2010-2011
 * Confidential and proprietary.
 */
package com.kelltontech.framework.controller;

import com.kelltontech.framework.network.ServiceResponse;

/**
 *
 */
public interface IServiceController {

	/**
	 * Initialize request object here.
	 */
	public void initService();

	/**
	 * Raise your request using Connector.
	 */
	public void requestService(Object object);
	
	/**
	 * receive ServiceResponse from n/w layer.
	 */
	public void handleServiceResponse(ServiceResponse serviceResponse);
	

	/**
	 * make response for UI.
	 */
	public void responseService(Object object);

}
