package com.lamchop.alcolist.server;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.google.gwt.core.client.GWT;
import com.lamchop.alcolist.shared.Manufacturer;

public class LatLongAdder {
	//private static Geocoder geoCoder = Geocoder.newInstance();
	private static LatLngMaker latLngMaker = new LatLngMaker();

	public LatLongAdder() {
	}

	public void makeGeocodeRequest(List<Manufacturer> manufacturers) {
		for (Manufacturer currentManufacturer: manufacturers) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
				// This is here because of the Geocoder request limit.
			} catch (InterruptedException e) {
				GWT.log("Sleep interrupted" + e.getMessage());
			}
			
			try {
				
				LatLngMaker.getLatLong(currentManufacturer);
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
