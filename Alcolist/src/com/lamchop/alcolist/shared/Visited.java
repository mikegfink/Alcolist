package com.lamchop.alcolist.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Visited implements Serializable {

	@PrimaryKey
	@Persistent
	private String userID;
	@Persistent
	private List<String> manufacturerIDs;
	
	public Visited() { 
		// Zero argument default constructor required by Serializable. Does not need to ever
		// be used explicitly in our code.
	}

	public Visited(String userID) {
		this.userID = userID;
		this.manufacturerIDs = new ArrayList<String>();
	}

	public void addManufacturerID(String id) {
		manufacturerIDs.add(id);
	}
	
	public void removeManufacturerID(String id) {
		manufacturerIDs.remove(id);
	}
	
	public List<String> getManufacturerIDs() {
		return manufacturerIDs;
	}
}
