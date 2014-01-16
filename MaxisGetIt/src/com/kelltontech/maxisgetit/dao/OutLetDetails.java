package com.kelltontech.maxisgetit.dao;

import java.util.ArrayList;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MaxisResponse;

public class OutLetDetails extends MaxisResponse implements IModel{

	private ArrayList<OutLet> outlet;

	public ArrayList<OutLet> getOutlet() {
		return outlet;
	}

	public void setOutlet(ArrayList<OutLet> outlet) {
		this.outlet = outlet;
	}

}
