package com.lamchop.alcolist.client.ui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;
import com.lamchop.alcolist.client.resources.Images;

public class LegendCell extends AbstractCell<String>{
	private static Images images = GWT.create(Images.class);

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			String value, SafeHtmlBuilder sb) {
		if (value == null)
			return;
		sb.appendHtmlConstant("<table>");
		sb.appendHtmlConstant("<tr><td rowspan = '1'>");
		if (value.equals("Brewery"))
			sb.appendHtmlConstant(new Image(images.breweryLegend()).toString());
		if (value.equals("Winery"))
			sb.appendHtmlConstant(new Image(images.wineryLegend()).toString());
		if (value.equals("Distillery"))
			sb.appendHtmlConstant(new Image(images.distilleryLegend()).toString());	
		sb.appendHtmlConstant("</td></tr></table>");
		
	}
	
	

}
