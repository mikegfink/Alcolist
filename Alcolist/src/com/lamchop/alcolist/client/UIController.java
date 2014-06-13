package com.lamchop.alcolist.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.lamchop.alcolist.server.Manufacturer;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;

import static com.google.gwt.dom.client.Style.Unit.PCT;

public class UIController {
	private UI uiPanel;
	private MapsLoader mapsLoader;
	private LayoutPanel mapPanel;
	private DataGrid<Manufacturer> listPanel;
	private List<Manufacturer> manufacturers;
	private Label title;
	private LayoutPanel mainPanel;
	
	
	public UIController() {
		
		DataGridResource resource = GWT.create(DataGridResource.class);
		
		manufacturers = new ArrayList<Manufacturer>();
		manufacturers.add(new Manufacturer("Gray Monk", "123 Street", "Kelowna", "BC", "", "Winery", ""));
		manufacturers.add(new Manufacturer("33 Acres", "Main Street", "Vancouver", "BC", "", "Brewery", ""));
		manufacturers.add(new Manufacturer("Craft Beer Market", "Main Street", "Vancouver", "BC", "V5Z 1Z1", "Brewery", "604-555-5555"));
		manufacturers.add(new Manufacturer("A", "Street", "Vancouver", "BC", "", "Winery", ""));

		title = new Label("The Alcolist");
		uiPanel = new UI();
		mapsLoader = new MapsLoader();
		mapsLoader.loadMapApi();
		mapPanel = mapsLoader.getMap();
		listPanel = new DataGrid<Manufacturer>(5, resource);
		mainPanel = new LayoutPanel();
		
		title.addStyleName("title");
		title.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		listPanel.setEmptyTableWidget(new Label("Empty"));
		
		ListDataProvider<Manufacturer> dataProvider = new ListDataProvider<Manufacturer>();
		dataProvider.addDataDisplay(listPanel);
		
		List<Manufacturer> list = dataProvider.getList();
	    for (Manufacturer m : manufacturers) {
	      list.add(m);
	    }
	    
	    ListHandler<Manufacturer> sortHandler = new ListHandler<Manufacturer>(list);
		listPanel.addColumnSortHandler(sortHandler);
		
		// Make main column
		Column<Manufacturer, Manufacturer> visibleColumn = 
				new Column<Manufacturer, Manufacturer>(new ManufacturerCell()) {
				@Override
	            public Manufacturer getValue(Manufacturer object) {
	              return object;
	            }
			};
			
		// add sort function to main column
		visibleColumn.setSortable(true);
		sortHandler.setComparator(visibleColumn, new Comparator<Manufacturer>() {
			public int compare(Manufacturer m1, Manufacturer m2) {
		        return m1.getName().compareTo(m2.getName());
		}
		});
			
		// Make city column
		Column<Manufacturer, String> city = 
				new Column<Manufacturer, String>(new TextCell()) {
					@Override
					public String getValue(Manufacturer object) {
						return object.getAddress().getCity();
					}
			
		};
		city.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		
		// add sort function to city column
		city.setSortable(true);
		sortHandler.setComparator(city, new Comparator<Manufacturer>() {
			public int compare(Manufacturer m1, Manufacturer m2) {
				return m1.getAddress().getCity().compareTo(m2.getAddress().getCity());
			}
		});
		
		// Make type column
		Column<Manufacturer, String> type = 
				new Column<Manufacturer, String>(new TextCell()) {
					@Override
					public String getValue(Manufacturer object) {
						return object.getType();
					}
			
		};
		type.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		
		// add sort function to type column
		type.setSortable(true);
		sortHandler.setComparator(type, new Comparator<Manufacturer>() {
			public int compare(Manufacturer m1, Manufacturer m2) {
				return m1.getType().compareTo(m2.getType());
			}
		});
		
		
		listPanel.addColumn(visibleColumn, "Manufacturer");
		listPanel.addColumn(type, "Type");
		listPanel.addColumn(city, "City");
		
		listPanel.setRowCount(manufacturers.size());
		listPanel.setColumnWidth(0,  "50%");
		
		listPanel.getColumnSortList().push(visibleColumn);
		ColumnSortEvent.fire(listPanel, listPanel.getColumnSortList());
		
		listPanel.getColumnSortList().push(type);
		ColumnSortEvent.fire(listPanel,  listPanel.getColumnSortList());
		
		listPanel.setRowStyles(new RowStyles<Manufacturer>() {

			@Override
			public String getStyleNames(Manufacturer row, int rowIndex) {
				return "list";
			}
		    
		});



		mainPanel.add(listPanel);
		mainPanel.add(mapPanel);
		
		
		mainPanel.setWidgetRightWidth(mapPanel, 0, PCT, 50, PCT);
		mainPanel.setWidgetLeftWidth(listPanel, 0, PCT, 35, PCT);
		
		uiPanel.add(mainPanel);
		uiPanel.add(title);
		
		uiPanel.setWidgetTopHeight(title, 0, PCT, 10, PCT);
		uiPanel.setWidgetTopHeight(mainPanel, 10, PCT, 90, PCT);
		
		
	}
	
	public UI getUI() {
		return uiPanel;
	}
	
}
