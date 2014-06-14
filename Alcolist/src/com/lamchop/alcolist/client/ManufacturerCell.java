package com.lamchop.alcolist.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.lamchop.alcolist.shared.Manufacturer;

public class ManufacturerCell extends AbstractCell<Manufacturer>{

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			Manufacturer value, SafeHtmlBuilder sb) {
		if (value == null)
			return;
		sb.appendHtmlConstant("<table>");
		
		sb.appendHtmlConstant("<tr><td style='font-size: 130%; font-weight: bold;'>");
		sb.appendHtmlConstant(value.getName());
		sb.appendHtmlConstant("</td></tr><tr><td style='font-size: 95%; font-style: italic;'>");
		sb.appendHtmlConstant(value.getAddress().getStreetAddress());
		sb.appendHtmlConstant("</td></tr></table>");
			
	}

}
