package com.lamchop.alcolist.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.LayoutPanel;

public class Legend extends LayoutPanel {
	
	private static final List<String> TYPES = Arrays.asList("Brewery", "Winery", "Distillery");
	
	private CellList<String> legend;
	private LegendCell cell;
	
	public Legend() {
//		types = new ArrayList<String>();
//		types.add("Brewery");
//		types.add("Winery");
//		types.add("Distillery");
		
		cell = new LegendCell();
		legend = new CellList<String>(cell);
		
		legend.setRowData(0, TYPES);
		add(legend);
		addStyleName("listPanel");
	}
	
}