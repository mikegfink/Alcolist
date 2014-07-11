package com.lamchop.alcolist.client.ui;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowHoverEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.lamchop.alcolist.client.AppDataController;

public class Legend extends LayoutPanel {
	
	private static final List<String> TYPES = Arrays.asList("Brewery", "Winery", "Distillery");
	
	private CellTable<String>  legend;
	private AppDataController theAppDataController;
	private String previousSelected;
	private String currentSelected;
	private PopupPanel legendLabel;
	
	public Legend(AppDataController theAppDataController) {
		
		//to get rid of popup labels
	    addMouseOutHandler();
	    
	    CellTableResource resource = GWT.create(CellTableResource.class);
		legend = new CellTable<String>(3, resource);
		this.theAppDataController = theAppDataController;
		legendLabel = new PopupPanel();
		init();
	}
	
	private void init() {
		Column<String, String> icons = new Column<String, String>(new LegendCell()) {
			@Override
			public String getValue(String object) {
				return object;
			}
		};
		
		legend.addColumn(icons);
		legend.setRowData(0, TYPES);
		legend.setSize("32px", "98px");
		add(legend);
		
		final SingleSelectionModel<String> selectionModel = addSelectionHandler();
		
	    addClickHandler(selectionModel);
	    
	    addPopupLabels();
	}
	
	private void addClickHandler(final SingleSelectionModel<String> selectionModel) {
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

	private SingleSelectionModel<String> addSelectionHandler() {
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
		return selectionModel;
	}

	private void addMouseOutHandler() {
		this.addDomHandler(new MouseOutHandler() {
		    public void onMouseOut(MouseOutEvent event) {
		        legendLabel.hide();
		    }
		}, MouseOutEvent.getType());
	}

	private void addPopupLabels() {
		legend.addRowHoverHandler(new RowHoverEvent.Handler() {
			@Override
			public void onRowHover(RowHoverEvent event) {
				int row = event.getHoveringRow().getRowIndex();
				legendLabel.setWidget(new Label(TYPES.get(row)));
				CellTable<String> source = (CellTable<String>) event.getSource();
				int x = source.getRowElement(row).getAbsoluteRight();
				int y = source.getRowElement(row).getAbsoluteTop();               
				legendLabel.setPopupPosition(x, y);
                legendLabel.show();
			}
	    	
	    });
	}
	
	
}
