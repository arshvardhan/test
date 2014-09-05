package com.kelltontech.maxisgetit.ui.widgets;

import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;



public  final class MediaScannerClient implements MediaScannerConnectionClient {
	private final String path;
	private final String mimeType;
	public MediaScannerConnection connection;

	public MediaScannerClient(String path, String mimeType) {
		this.path = path;
		this.mimeType = mimeType;
	}

	@Override
	public void onMediaScannerConnected() {
		connection.scanFile(path, mimeType);
	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
		connection.disconnect();    
	}
}