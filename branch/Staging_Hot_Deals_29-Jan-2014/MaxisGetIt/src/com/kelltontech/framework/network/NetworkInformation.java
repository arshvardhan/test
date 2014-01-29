package com.kelltontech.framework.network;

import java.util.ArrayList;

public class NetworkInformation {

	public class Cell {
		public int cellid;
		public int mnc;
		public int mcc;
		public int lac;
		public int padding;
		public int rssi;

		public String toTextStr() {
			return (padding == 8 ? "W" : "G") + ',' + getPaddedInt(mcc, 3) + ',' + getPaddedInt(mnc, 2) + ',' + getPaddedHex(lac, 4) + ',' + getPaddedHex(cellid, padding) + ',' + (padding == 8 ? "" : rssi) + ',';
		}

		public String getCellidStr() {
			return getPaddedHex(cellid, padding);
		}

		public String getLacStr() {
			return getPaddedHex(lac, 4);
		}

		public String getMncStr() {
			return getPaddedInt(mnc, 2);
		}

		public String getMccStr() {
			return getPaddedInt(mcc, 3);
		}

		public String getRssiStr() {
			return Integer.toString(rssi);
		}
	}

	class Wifi {
		String bssid;
		String ssid;
		int rssid;

		public String toTextStr() {
			return bssid + ',' + ssid + ',' + rssid;
		}

	}

	// Serving cell
	public Cell serving;

	// Neighboring cells
	public ArrayList<Cell> neighbour;

	// Wifi networks
	public ArrayList<Wifi> wifi;

	public NetworkInformation() {
		serving = new Cell();
		neighbour = new ArrayList<Cell>();
		wifi = new ArrayList<Wifi>();
	}

	public void addNeighboringCell(int cellid, int rssi) {
		Cell neighbourCell = new Cell();
		neighbourCell.cellid = cellid;
		neighbourCell.rssi = rssi;
		neighbourCell.mnc = serving.mnc;
		neighbourCell.mcc = serving.mcc;
		neighbourCell.lac = serving.lac;
		neighbourCell.padding = serving.padding;
		neighbour.add(neighbourCell);
	}

	public void addWifiCell(String bssid, String ssid, int rssid) {
		Wifi wifiCell = new Wifi();
		wifiCell.bssid = bssid.replace(':', '-');
		wifiCell.ssid = ssid;
		wifiCell.rssid = rssid;
		wifi.add(wifiCell);
	}

	/**
	 * Convert an int to an hex String and pad with 0's up to minLen.
	 */
	String getPaddedHex(int nr, int minLen) {
		String str = Integer.toHexString(nr);
		if (str != null) {
			while (str.length() < minLen) {
				str = "0" + str;
			}
		}
		return str;
	}

	/**
	 * Convert an int to String and pad with 0's up to minLen.
	 */
	String getPaddedInt(int nr, int minLen) {
		String str = Integer.toString(nr);
		if (str != null) {
			while (str.length() < minLen) {
				str = "0" + str;
			}
		}
		return str;
	}
}
