package com.lamchop.alcolist.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Image;
import com.lamchop.alcolist.client.resources.Images;

public class LegendCell extends AbstractCell<String>{
	private static Images images = GWT.create(Images.class);

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			String value, SafeHtmlBuilder sb) {
		if (value == null)
			return;
		sb.appendHtmlConstant("<table>");
		sb.appendHtmlConstant("<tr><td rowspan = '2'>");
		if (value.equals("Brewery"))
			sb.appendHtmlConstant(new Image(images.brewery()).toString());
		if (value.equals("Winery"))
			sb.appendHtmlConstant(new Image(images.winery()).toString());
		if (value.equals("Distillery"))
			sb.appendHtmlConstant(new Image(images.distillery()).toString());	
		sb.appendHtmlConstant("<tr><td style= 'font-weight: bold;'>");
		sb.appendHtmlConstant(value);
		sb.appendHtmlConstant("</td></tr></table>");
		
	}

}
