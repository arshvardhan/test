package com.kelltontech.maxisgetit.model.matta.booths.detail;

import java.io.Serializable;

/**
 * @author arsh.vardhan
 * @modified 19-Aug-2014
 */
public class CompanyModel implements Serializable {
	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = 3387078864689307917L;
	
	private String CName;
	private String Hall;
	private String HallId;
	private String Booth;
	private String Location;
	private String PTCMo;
	private String PTCEmail;
	private String Website;
	private String Image;
	private String Source;
	private PackagesCollection Packages;

	public String getPTCEmail() {
		return PTCEmail;
	}

	public void setPTCEmail(String PTCEmail) {
		this.PTCEmail = PTCEmail;
	}

	public String getHall() {
		return Hall;
	}

	public void setHall(String Hall) {
		this.Hall = Hall;
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

	public String getPTCMo() {
		return PTCMo;
	}

	public void setPTCMo(String PTCMo) {
		this.PTCMo = PTCMo;
	}

	public String getBooth() {
		return Booth;
	}

	public void setBooth(String Booth) {
		this.Booth = Booth;
	}
	
	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		this.Location = location;
	}

	public PackagesCollection getPackages() {
		return Packages;
	}

	public void setPackages(PackagesCollection Packages) {
		this.Packages = Packages;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		this.Image = image;
	}
	
	public String getSource() {
		return Source;
	}

	public void setSource(String source) {
		this.Source = source;
	}

	public String getWebsite() {
		return Website;
	}

	public void setWebsite(String Website) {
		this.Website = Website;
	}

}