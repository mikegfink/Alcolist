package com.lamchop.alcolist.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.controls.ZoomControlOptions;
import com.google.gwt.user.client.ui.LayoutPanel;
import static com.google.gwt.maps.client.controls.ControlPosition.RIGHT_BOTTOM;




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
		opts.setMapTypeControl(false);
		ZoomControlOptions zoomOption = ZoomControlOptions.newInstance();
		zoomOption.setPosition(RIGHT_BOTTOM);
		opts.setZoomControlOptions(zoomOption);
		opts.setPanControl(false);
		opts.setStreetViewControl(false);
		
		mapWidget = new MapWidget(opts);
		pWidget.add(mapWidget);
		mapWidget.setSize("100%", "100%");

		mapWidget.triggerResize();
	}

	public MapWidget getMapWidget() {
		return mapWidget;
	}
	
	public void triggerResize() {
		mapWidget.triggerResize();
	}
}
