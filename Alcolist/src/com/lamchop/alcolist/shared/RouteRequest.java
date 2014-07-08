package com.lamchop.alcolist.shared;

import java.io.Serializable;
import java.util.List;

public class RouteRequest implements Serializable {

	/** Address string of starting point of route*/
	private String start;
	/** Address string of end point of route */
	private String end;
	/** List of address strings for additional locations to visit on the route */
	private List<String> midpoints;
	private boolean optimize;

	public RouteRequest(String start, String end, List<String> midpoints, boolean optimize) {
		this.start = start;
		this.end = end;
		this.midpoints = midpoints;
		this.optimize = optimize;
	}
	
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public List<String> getMidpoints() {
		return midpoints;
	}

	public void setMidpoints(List<String> midpoints) {
		this.midpoints = midpoints;
	}

	public boolean getOptimize() {
		return optimize;
	}
	
	public void setOptimize(boolean optimize) {
		this.optimize = optimize;
	}
}
