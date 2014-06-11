package com.lamchop.alcolist.client;

import java.util.ArrayList;

import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.maps.client.LoadApi.LoadLibrary;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.LayoutPanel;

public class MapsLoader {
	
	private LayoutPanel theMap;
	
	public MapsLoader() {
		theMap = new LayoutPanel();
	}
	
	public LayoutPanel getMap() {
		return theMap;
	}
	
	public void loadMapApi() {
	    
		
		boolean sensor = true;

	    // load all the libs for use in the maps
	    ArrayList<LoadLibrary> loadLibraries = new ArrayList<LoadApi.LoadLibrary>();
	    loadLibraries.add(LoadLibrary.ADSENSE);
	    loadLibraries.add(LoadLibrary.DRAWING);
	    loadLibraries.add(LoadLibrary.GEOMETRY);
	    loadLibraries.add(LoadLibrary.PANORAMIO);
	    loadLibraries.add(LoadLibrary.PLACES);
	    loadLibraries.add(LoadLibrary.WEATHER);
	    loadLibraries.add(LoadLibrary.VISUALIZATION);

	    Runnable onLoad = new Runnable() {
	      @Override
	      public void run() {
	    	  draw();
	      }
	    };

	    LoadApi.go(onLoad, loadLibraries, sensor);
	    
	  }
	public void draw() {
		
		theMap.add(new BasicMapWidget());	
		
	}
}
