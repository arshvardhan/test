package com.kelltontech.maxisgetit.controllers;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;

import android.content.Context;
import android.os.Message;
import com.google.android.gms.maps.model.LatLng;
import com.kelltontech.framework.controller.BaseServiceController;
import com.kelltontech.framework.ui.IActionController;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.response.RouteDetailResponse;

public class MapDirectionController extends BaseServiceController {
	private Context mActivity;
	public final static String MODE_DRIVING = "driving";
	public final static String MODE_WALKING = "walking";

	public MapDirectionController(IActionController screen, int eventType) {
		super(screen, eventType);
		mActivity = (Context) screen;
	}

	@Override
	public void initService() {
	}

	@Override
	public void requestService(Object object) {
	}

	public void requestService(final LatLng start, final LatLng end, final String mode) {
		new Thread(new Runnable() {
			public void run() {
				if (!NativeHelper.isDataConnectionAvailable(mActivity)) {
					responseService(null);
					return;
				}
				String url = "http://maps.googleapis.com/maps/api/directions/xml?" + "origin=" + start.latitude + "," + start.longitude + "&destination=" + end.latitude + "," + end.longitude + "&sensor=false&units=metric&mode="
						+ mode;
				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpContext localContext = new BasicHttpContext();
					HttpPost httpPost = new HttpPost(url);
					HttpResponse response = httpClient.execute(httpPost, localContext);
					InputStream in = response.getEntity().getContent();
					DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					Document doc = builder.parse(in);
					responseService(doc);
				} catch (Exception e) {
					logRequestException(e, "MapDirectionController");
					
					responseService(null);
				}
			}
		}).start();
	}

	@Override
	public void responseService(Object object) {
		Message message = new Message();
		message.arg2 = mEventType;
		message.arg1 = 1;
		if (object == null || !(object instanceof Document)) {
			message.obj = mActivity.getResources().getString(R.string.internal_error);
		} else {
			Document doc = (Document) object;
			RouteDetailResponse rdr = new RouteDetailResponse(doc);
			message.arg1 = 0;
			message.obj = rdr;
		}
		mScreen.setScreenData(message, mEventType, 0);
	}
}
