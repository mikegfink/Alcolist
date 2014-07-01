package com.lamchop.alcolist.client.ui;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.base.LatLngBounds;
import com.google.gwt.maps.client.controls.ControlPosition;
import com.google.gwt.maps.client.events.MouseEvent;
import com.google.gwt.maps.client.events.channelnumber.ChannelNumberChangeMapEvent;
import com.google.gwt.maps.client.events.channelnumber.ChannelNumberChangeMapHandler;
import com.google.gwt.maps.client.events.click.ClickMapEvent;
import com.google.gwt.maps.client.events.click.ClickMapHandler;
import com.google.gwt.maps.client.events.format.FormatChangeMapEvent;
import com.google.gwt.maps.client.events.format.FormatChangeMapHandler;
import com.google.gwt.maps.client.events.mapchange.MapChangeMapEvent;
import com.google.gwt.maps.client.events.mapchange.MapChangeMapHandler;
import com.google.gwt.maps.client.events.position.PositionChangeMapEvent;
import com.google.gwt.maps.client.events.position.PositionChangeMapHandler;
import com.google.gwt.maps.client.overlays.Animation;
import com.google.gwt.maps.client.overlays.InfoWindow;
import com.google.gwt.maps.client.overlays.InfoWindowOptions;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.maps.client.services.Geocoder;
import com.google.gwt.maps.client.services.GeocoderRequest;
import com.google.gwt.maps.client.services.GeocoderRequestHandler;
import com.google.gwt.maps.client.services.GeocoderResult;
import com.google.gwt.maps.client.services.GeocoderStatus;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lamchop.alcolist.shared.Manufacturer;

import static com.google.gwt.dom.client.Style.Unit.PCT;


public class AlcolistMapWidget extends Composite {

	private final LayoutPanel pWidget;
	private MapWidget mapWidget;


	@Override 
	protected void onLoad() {
		super.onLoad();
	}

	public AlcolistMapWidget() {
		pWidget = new LayoutPanel();
		initWidget(pWidget);
		pWidget.setSize("100%", "100%");
		draw();

	}

	private void draw() {
		pWidget.clear();
		drawMap();

	}

	private void drawMap() {
		LatLng center = LatLng.newInstance(51, -120);
		MapOptions opts = MapOptions.newInstance();
		opts.setZoom(7);
		opts.setCenter(center);
		opts.setMapTypeId(MapTypeId.ROADMAP);

		mapWidget = new MapWidget(opts);
		pWidget.add(mapWidget);
		mapWidget.setSize("100%", "100%");

		mapWidget.addClickHandler(new ClickMapHandler() {
			@Override
			public void onEvent(ClickMapEvent event) {
				// TODO fix the event getting, getting ....
				GWT.log("clicked on latlng=" + event.getMouseEvent().getLatLng());
			}
		});
		mapWidget.triggerResize();
	}


//FOR DELETING
//	public void createMarker(String address) {
//
//		// THIS SHOULD WORK BUT NEEDS REFACTORING SO YOU CAN ACTUALLY 
//		// GET AT THE MAP. IT GETS LATLNGS BUT I CAN'T FIND THE MAP.
//		// First thing off the top of my head would be to have the maploader
//		// return a MapPanel class that you define (like ListPanel that has a field
//		// to get at the MyMapWidget that you are designing and then this createMarker
//		// method would be a method in that widget. E.g. BasicMapWidget has drawMarker(). 
//		// This would be part of a method like that in your custom widget class.
//
//
//		GeocoderRequest request = GeocoderRequest.newInstance();
//		Geocoder geoCoder = Geocoder.newInstance();
//		request.setAddress(address);
//
//		geoCoder.geocode(request, new GeocoderRequestHandler() {
//			@Override
//			public void onCallback(JsArray<GeocoderResult> results,
//					GeocoderStatus status) {
//				if (status == GeocoderStatus.OK) {
//					GeocoderResult result = results.shift();
//					LatLng location = result.getGeometry().getLocation();
//					MarkerOptions options = MarkerOptions.newInstance();
//					options.setPosition(location);
//					Marker marker = Marker.newInstance(options);
//					marker.setPosition(location);
//					marker.setMap(mapWidget);
//				} else {
//
//				}
//			}      
//		});
//	}



	public MapWidget getMapWidget() {
		return mapWidget;
	}
	
	public void triggerResize() {
		mapWidget.triggerResize();
	}
}
