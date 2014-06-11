package com.lamchop.alcolist.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BasicMapWidget extends Composite {

	private final VerticalPanel pWidget;
	private MapWidget mapWidget;

	private Marker markerBasic;
	private Marker markerDrop;

	public BasicMapWidget() {
		pWidget = new VerticalPanel();
		initWidget(pWidget);

		draw();
	}

	private void draw() {
		Button addDropMarkerButton = new Button("Add Marker with Drop");
		addDropMarkerButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (markerDrop != null) {
					markerDrop.clear();
				}
				drawMarkerWithDropAnimation();
			}
		});

		// basic controls to test markers
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(new HTML("<br>Basic Map Example."));
		hp.add(new HTML("&nbsp;"));
		hp.add(addDropMarkerButton);
		hp.setCellVerticalAlignment(addDropMarkerButton, VerticalPanel.ALIGN_BOTTOM);

		pWidget.clear();
		pWidget.add(hp);

		drawMap();
		drawBasicMarker();
	}

	private void drawBasicMarker() {
		LatLng center = LatLng.newInstance(47.8, -121.4);
		MarkerOptions options = MarkerOptions.newInstance();
		options.setPosition(center);
		options.setTitle("Hello World");

		markerBasic = Marker.newInstance(options);
		markerBasic.setMap(mapWidget);

		markerBasic.addClickHandler(new ClickMapHandler() {
			@Override
			public void onEvent(ClickMapEvent event) {
				drawInfoWindow(markerBasic, event.getMouseEvent());
			}
		});
	}

	private void drawMarkerWithDropAnimation() {
		LatLng center = LatLng.newInstance(42.33, -120.81);
		MarkerOptions options = MarkerOptions.newInstance();
		options.setPosition(center);
		options.setTitle("Thanks for clicking on me.");
		options.setAnimation(Animation.DROP);

		markerDrop = Marker.newInstance(options);
		markerDrop.setMap(mapWidget);
	}

	protected void drawInfoWindow(Marker marker, MouseEvent mouseEvent) {
		if (marker == null || mouseEvent == null) {
			return;
		}

		HTML html = new HTML("You clicked on: " + mouseEvent.getLatLng().getToString());

		InfoWindowOptions options = InfoWindowOptions.newInstance();
		options.setContent(html);
		InfoWindow iw = InfoWindow.newInstance(options);
		iw.open(mapWidget, marker);
	}

	private void drawMap() {
		LatLng center = LatLng.newInstance(49.496675, -102.65625);
		MapOptions opts = MapOptions.newInstance();
		opts.setZoom(4);
		opts.setCenter(center);
		opts.setMapTypeId(MapTypeId.HYBRID);

		mapWidget = new MapWidget(opts);
		pWidget.add(mapWidget);
		mapWidget.setSize("750px", "500px");

		mapWidget.addClickHandler(new ClickMapHandler() {
			@Override
			public void onEvent(ClickMapEvent event) {
				// TODO fix the event getting, getting ....
				GWT.log("clicked on latlng=" + event.getMouseEvent().getLatLng());
			}
		});
	}
}
