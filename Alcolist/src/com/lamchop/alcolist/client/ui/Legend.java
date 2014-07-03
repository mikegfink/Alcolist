package com.lamchop.alcolist.client.ui;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.lamchop.alcolist.client.AppDataController;

public class Legend extends LayoutPanel {
	
	private static final List<String> TYPES = Arrays.asList("Brewery", "Winery", "Distillery");
	
	private CellList<String>  legend;
	private LegendCell cell;
	private AppDataController theAppDataController;
	private String previousSelected;
	private String currentSelected;
	
	public Legend(AppDataController theAppDataController) {
		
		cell = new LegendCell();
		legend = new CellList<String>(cell);
		this.theAppDataController = theAppDataController;
		init();
	}
	
	private void init() {
		legend.setRowData(0, TYPES);
		add(legend);
		addStyleName("listPanel");
		final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
	    legend.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	        String selected = selectionModel.getSelectedObject();
	        if (selected != null) {
	          theAppDataController.filterByType(selected);
	        }
	      }   
	    });
	    
	    
	    
	    legend.addHandler(new ClickHandler(){
	         @Override
	         public void onClick(ClickEvent event) {
	          Scheduler.get().scheduleDeferred(new ScheduledCommand() {    
	           @Override
	           public void execute() {
	            currentSelected = selectionModel.getSelectedObject();
	            if (currentSelected != null && currentSelected.equals(previousSelected)) {
	            	selectionModel.setSelected(currentSelected, false);
	            	theAppDataController.removeFilter();
	            	currentSelected = null;
	            }
	            previousSelected = currentSelected;
	           }
	          });
	          }
	    },  ClickEvent.getType());
	    
	}
}
