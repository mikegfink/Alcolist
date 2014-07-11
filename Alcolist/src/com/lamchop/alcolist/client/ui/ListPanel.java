package com.lamchop.alcolist.client.ui;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static com.google.gwt.dom.client.Style.Unit.PCT;
import static com.google.gwt.dom.client.Style.Unit.PX;

import java.util.List;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextButtonCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.builder.shared.TableCellBuilder;
import com.google.gwt.dom.builder.shared.TableRowBuilder;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.AbstractCellTableBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.ui.buttons.NearMeButton;
import com.lamchop.alcolist.shared.Manufacturer;

public class ListPanel extends LayoutPanel {

	private int PAGESIZE = 10000; // TODO: What about when there are 6000?
	
	private DataGrid<Manufacturer> listGrid;
	private SearchPanel searchPanel;
	private ListDataProvider<Manufacturer> dataProvider;
	private List<Manufacturer> list;
	private Column<Manufacturer, Manufacturer> nameColumn;
	private Column<Manufacturer, String> cityColumn;
	private Column<Manufacturer, String> typeColumn;
	private Column<Manufacturer, String> extraInfo;
	private Manufacturer currentSelected;
	private UIController theUIController;
	private Manufacturer showingInfo;

	public ListPanel(final AppDataController theAppDataController, UIController theUIController) {
		DataGridResource resource = GWT.create(DataGridResource.class);
		listGrid = new DataGrid<Manufacturer>(PAGESIZE, resource);
		listGrid.setEmptyTableWidget(new Label("No Results Found"));
		
		this.theUIController = theUIController;

		addDataProvider();
		list = dataProvider.getList();

		ListHandler<Manufacturer> sortHandler = new ListHandler<Manufacturer>(list);
		listGrid.addColumnSortHandler(sortHandler);

		initListColumns(sortHandler);

//		listGrid.setTableBuilder(new CustomTableBuilder(listGrid));

		add(listGrid);

		addStyleName("listPanel");
		listGrid.setAlwaysShowScrollBars(true);
		
	    addSelectionModel();
	    

		this.setWidgetBottomHeight(listGrid, 0, PCT, 100, PCT);

	}

	private void addSelectionModel() {
		final SingleSelectionModel<Manufacturer> selectionModel = new SingleSelectionModel<Manufacturer>();
	    listGrid.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
		      public void onSelectionChange(SelectionChangeEvent event) {
		        currentSelected = selectionModel.getSelectedObject();
		        if (currentSelected != null)
		        	theUIController.selectOnMap(currentSelected);
 
		      }  
		    });
	}

	public void addDataProvider() {
		dataProvider = new ListDataProvider<Manufacturer>();
		dataProvider.addDataDisplay(listGrid);
	}


	private void initListColumns (ListHandler<Manufacturer> sortHandler) {

		// Make main column
		nameColumn = 
				new Column<Manufacturer, Manufacturer>(new ManufacturerCell()) {
			@Override
			public Manufacturer getValue(Manufacturer object) {
				return object;
			}
		};
			

		// add sort function to main column
		nameColumn.setSortable(true);
		sortHandler.setComparator(nameColumn, new Comparator<Manufacturer>() {
			public int compare(Manufacturer m1, Manufacturer m2) {
				return m1.getName().compareTo(m2.getName());
			}
		});

		// Make city column
		cityColumn = 
				new Column<Manufacturer, String>(new TextCell()) {
			@Override
			public String getValue(Manufacturer object) {
				return object.getCity();
			}

		};
		cityColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);

		// add sort function to city column
		cityColumn.setSortable(true);
		sortHandler.setComparator(cityColumn, new Comparator<Manufacturer>() {
			public int compare(Manufacturer m1, Manufacturer m2) {
				return m1.getCity().compareTo(m2.getCity());
			}
		});


		// Make type column
		typeColumn = 
				new Column<Manufacturer, String>(new TextCell()) {
			@Override
			public String getValue(Manufacturer object) {
				return object.getType();
			}

		};
		typeColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);

		// add sort function to type column
		typeColumn.setSortable(true);
		sortHandler.setComparator(typeColumn, new Comparator<Manufacturer>() {
			public int compare(Manufacturer m1, Manufacturer m2) {
				return m1.getType().compareTo(m2.getType());
			}
		});
		
		SafeHtmlRenderer<String> anchorRenderer = new AbstractSafeHtmlRenderer<String>() {
		      @Override
		      public SafeHtml render(String object) {
		        SafeHtmlBuilder sb = new SafeHtmlBuilder();
		        sb.appendHtmlConstant("<a href=\"javascript:;\">").appendEscaped(object)
		            .appendHtmlConstant("</a>");
		        return sb.toSafeHtml();
		      }
		    };
		
		extraInfo = 
				new Column<Manufacturer, String>(new ClickableTextCell(anchorRenderer)) {
			@Override
			public String getValue(Manufacturer object) {
				if (object.equals(showingInfo))
					return "<<";
				
				else return ">>";
			}
		};
		extraInfo.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		
		extraInfo.setFieldUpdater(new FieldUpdater<Manufacturer, String>() {
		      @Override
		      public void update(int index, Manufacturer object, String value) {
		    	  currentSelected = object;
		    	  if (extraInfo.getValue(object) == "<<")
		    		  showingInfo = null;
		    	  
		    	  else {
		    		  showingInfo = object;
		    		  theUIController.showReviewPanel(showingInfo);
		    	  }
		      }
		    });
//		
//		extraInfo.setFieldUpdater(new FieldUpdater<Manufacturer, String>() {
//			  public void update(int index, Manufacturer object, String value) {
//			    theUIController.showReviewPanel(object);
//	
//			  }
//			});

		listGrid.setColumnWidth(0,  "60%");
		listGrid.setColumnWidth(3,  "7%");
		
		listGrid.addColumn(nameColumn, "Manufacturer Name");
		listGrid.addColumn(cityColumn, "City");
		listGrid.addColumn(typeColumn, "Type");
		listGrid.addColumn(extraInfo);
		
	}

	public void addData(List<Manufacturer> manufacturers) {

		list.clear();
		for (Manufacturer m : manufacturers) {
			list.add(m);
		}

		defaultSort(nameColumn, typeColumn);
	}

	private void defaultSort(Column<Manufacturer, Manufacturer> firstSort, Column<Manufacturer, String> secondSort) {
		listGrid.getColumnSortList().push(firstSort);
		ColumnSortEvent.fire(listGrid, listGrid.getColumnSortList());

		listGrid.getColumnSortList().push(secondSort);
		ColumnSortEvent.fire(listGrid,  listGrid.getColumnSortList());
	}
	

	public void showLoggedIn() {
		// TODO Create buttons for add/edit review/rating/routes
		
	}

	public void showLoggedOut() {
		// TODO Remove buttons for ratings/reviews/routes CONSIDER WHAT TO DO ABOUT DIRECTIONS
		
	}
	

	
//	private class CustomTableBuilder extends AbstractCellTableBuilder<Manufacturer>{
//	
//
//		public CustomTableBuilder(AbstractCellTable<Manufacturer> cellTable) {
//			super(cellTable);
//			
//		}
//
//		@Override
//		protected void buildRowImpl(Manufacturer rowValue, int absRowIndex) {
//			
//			
//			
//			TableRowBuilder row = startRow();
//			TableCellBuilder td = row.startTD();
//	        this.renderCell(td, this.createContext(0), nameColumn, rowValue);
//	        td.endTD();
//	        
//			td = row.startTD();
//	        this.renderCell(td, this.createContext(1), cityColumn, rowValue);
//	        td.endTD();
//	        
//			td = row.startTD();
//	        this.renderCell(td, this.createContext(2), typeColumn, rowValue);
//	        td.endTD();
//	        
//	        row.endTR();
//			
//	        if (currentSelected != null) {
//	        	if (currentSelected.equals(rowValue)) {
//	        		row = startRow();
//	        		td = row.startTD().colSpan(3);
//	        		this.renderCell(td,  this.createContext(0), extraInfo, rowValue);
//	        		td.endTD();
//	        		row.endTR();
//	        	}
//	        }
//		}
//		
//
//		
//	}

}
