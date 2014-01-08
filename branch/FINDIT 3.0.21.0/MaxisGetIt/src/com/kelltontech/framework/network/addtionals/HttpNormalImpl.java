package com.kelltontech.framework.network.addtionals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import android.util.Log;

/**
 * Class that encapsulates the HTTP requests using the "HttpURLConnection"
 * 
 * @author ricardo
 * 
 */
public class HttpNormalImpl {
	private final String CATEGORIA = "livro";

	public final String downloadArquivo(String url) {
		Log.i(CATEGORIA, "Http.downloadArquivo: " + url);
		try {
			// Create a URL
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();

			// Sets the request to get
			// connection.setRequestProperty("Request-Method","GET");
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.setDoOutput(false);
			conn.connect();

			InputStream in = conn.getInputStream();

			// String arquivo = readBufferedString(sb, in);
			String arquivo = readString(in);

			conn.disconnect();

			return arquivo;
		} catch (MalformedURLException e) {
			Log.e(CATEGORIA, e.getMessage(), e);
		} catch (IOException e) {
			Log.e(CATEGORIA, e.getMessage(), e);
		}
		return null;
	}

	public final byte[] downloadImagem(String url) {
		Log.i(CATEGORIA, "Http.downloadImagem: " + url);
		try {
			// Create a URL
			URL u = new URL(url);

			HttpURLConnection connection = (HttpURLConnection) u.openConnection();
			// Sets the request to get
			connection.setRequestProperty("Request-Method", "GET");
			connection.setDoInput(true);
			connection.setDoOutput(false);

			connection.connect();

			InputStream in = connection.getInputStream();

			// String arquivo = readBufferedString(sb, in);
			byte[] bytes = readBytes(in);

			Log.i(CATEGORIA, "Image returned with: " + bytes.length + " bytes");

			connection.disconnect();

			return bytes;

		} catch (MalformedURLException e) {
			Log.e(CATEGORIA, e.getMessage(), e);
		} catch (IOException e) {
			Log.e(CATEGORIA, e.getMessage(), e);
		}
		return null;
	}

	public String doPost(String url, Map<String, ?> params) {
		try {
			String queryString = getQueryString(params);
			String texto = doPost(url, queryString);
			return texto;
		} catch (IOException e) {
			Log.e(CATEGORIA, e.getMessage(), e);
		}
		return url;
	}

	// Makes a POST request at the URL given and returns the text
	// The parameters are sent to the server
	private String doPost(String url, String params) throws IOException {
		Log.i(CATEGORIA, "Http.doPost: " + url + "?" + params);
		URL u = new URL(url);

		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);

		conn.connect();

		OutputStream out = conn.getOutputStream();
		byte[] bytes = params.getBytes("UTF8");
		out.write(bytes);
		out.flush();
		out.close();

		InputStream in = conn.getInputStream();

		// read the text
		String texto = readString(in);

		conn.disconnect();

		return texto;
	}

	// Turns HashMap in a query string to POST
	private String getQueryString(Map<String, ?> params) throws IOException {
		if (params == null || params.size() == 0) {
			return null;
		}
		String urlParams = null;
		Iterator<String> e = (Iterator<String>) params.keySet().iterator();
		while (e.hasNext()) {
			String chave = e.next();
			Object objValor = params.get(chave);
			String valor = objValor.toString();
			urlParams = urlParams == null ? "" : urlParams + "&";
			urlParams += chave + "=" + valor;
		}
		return urlParams;
	}

	// Reads the byte array returned from the InputStream
	private byte[] readBytes(InputStream in) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			byte[] buffer = new byte[1024];
			int len;
			while ((len = in.read(buffer)) > 0) {
				bos.write(buffer, 0, len);
			}
			byte[] bytes = bos.toByteArray();
			return bytes;
		} finally {
			bos.close();
			in.close();
		}
	}

	// Reads the text of the returned InputStream
	private String readString(InputStream in) throws IOException {
		byte[] bytes = readBytes(in);
		String texto = new String(bytes);
		Log.i(CATEGORIA, "Http.readString: " + texto);
		return texto;
	}
}