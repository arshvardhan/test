package com.kelltontech.maxisgetit.model.matta.packages.detail;

import java.io.Serializable;

/**
 * @author arsh.vardhan
 * @modified 11-Aug-2014
 */
public class PackageModel implements Serializable {

	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = -3693446745099505907L;
	
	private String Id;
	private String Title;
	private String CId;
	private String CName;
	private String L2Cat;
	private String L3Cat;
	private ImagesUrl Images;
	private ItineraryTab Itinerary;
	private HighlightsTab Highlights;

	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String Title) {
		this.Title = Title;
	}

	public String getCName() {
		return CName;
	}

	public void setCName(String CName) {
		this.CName = CName;
	}

	public String getCId() {
		return CId;
	}

	public void setCId(String CId) {
		this.CId = CId;
	}
	
	public String getL2Cat() {
		return L2Cat;
	}

	public void setL2Cat(String L2Cat) {
		this.L2Cat = L2Cat;
	}

	public String getL3Cat() {
		return L3Cat;
	}

	public void setL3Cat(String L3Cat) {
		this.L3Cat = L3Cat;
	}

	public ImagesUrl getImages() {
		return Images;
	}

	public void setImages(ImagesUrl images) {
		this.Images = images;
	}

	public ItineraryTab getItinerary() {
		return Itinerary;
	}

	public void setItinerary(ItineraryTab itinerary) {
		this.Itinerary = itinerary;
	}

	public HighlightsTab getHighlights() {
		return Highlights;
	}

	public void setHighlights(HighlightsTab highlights) {
		this.Highlights = highlights;
	}

}