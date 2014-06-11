package com.lamchop.alcolist.client;

import java.util.ArrayList;

import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.maps.client.LoadApi.LoadLibrary;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class MapsLoader {

	private AbsolutePanel mapPanel;
	
	public MapsLoader() {
		mapPanel = new AbsolutePanel();
	}
	
	
	public AbsolutePanel loadMapApi() {
	    
		
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
	    
	    return mapPanel;
	  }
	
	private void draw() {
		BasicMapWidget basicMap = new BasicMapWidget();
	    mapPanel.add(basicMap);;
	}
}
