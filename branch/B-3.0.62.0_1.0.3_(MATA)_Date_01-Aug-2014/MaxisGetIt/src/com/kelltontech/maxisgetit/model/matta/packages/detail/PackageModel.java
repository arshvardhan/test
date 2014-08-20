package com.kelltontech.maxisgetit.model.matta.packages.detail;

import java.io.Serializable;
import java.util.List;

/**
 * @author arsh.vardhan
 * @modified 20-Aug-2014
 */
public class PackageModel implements Serializable {

	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = -3693446745099505907L;
	
	private String Id;
	private String Title;
	private String HallId;
	private String CId;
	private String CName;
	private String L2Cat;
	private String L3Cat;
	private String PTCMo;
	private List<ImagesUrl> Images;
	private List<ItineraryTab> Itinerary;
	private List<HighlightsTab> Highlights;
	private String Source;

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
	
	public String getHallId() {
		return HallId;
	}

	public void setHallId(String hallId) {
		this.HallId = hallId;
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
	
	public String getPTCMo() {
		return PTCMo;
	}

	public void setPTCMo(String pTCMo) {
		this.PTCMo = pTCMo;
	}

	public List<ImagesUrl> getImages() {
		return Images;
	}

	public void setImages(List<ImagesUrl> images) {
		this.Images = images;
	}

	public List<ItineraryTab> getItinerary() {
		return Itinerary;
	}

	public void setItinerary(List<ItineraryTab> itinerary) {
		this.Itinerary = itinerary;
	}

	public List<HighlightsTab> getHighlights() {
		return Highlights;
	}

	public void setHighlights(List<HighlightsTab> highlights) {
		this.Highlights = highlights;
	}

	public String getSource() {
		return Source;
	}

	public void setSource(String source) {
		this.Source = source;
	}
	
}