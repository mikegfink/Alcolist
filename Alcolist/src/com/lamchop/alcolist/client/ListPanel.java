package com.lamchop.alcolist.client;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.lamchop.alcolist.shared.Manufacturer;

public class ListPanel extends LayoutPanel {

	private int PAGESIZE = 500;


	private DataGrid<Manufacturer> listGrid;
	private LayoutPanel searchPanel;
	ListDataProvider<Manufacturer> dataProvider;
	List<Manufacturer> list;
	Column<Manufacturer, Manufacturer> nameColumn;
	Column<Manufacturer, String> cityColumn;
	Column<Manufacturer, String> typeColumn;


	public ListPanel() {
		DataGridResource resource = GWT.create(DataGridResource.class);
		listGrid = new DataGrid<Manufacturer>(PAGESIZE, resource);
		listGrid.setEmptyTableWidget(new Label("Empty"));

		addDataProvider();
		list = dataProvider.getList();

		ListHandler<Manufacturer> sortHandler = new ListHandler<Manufacturer>(list);
		listGrid.addColumnSortHandler(sortHandler);

		initListColumns(sortHandler);

		add(listGrid);
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


		listGrid.addColumn(nameColumn, "Manufacturer");
		listGrid.addColumn(cityColumn, "City");
		listGrid.addColumn(typeColumn, "Type");
		listGrid.setColumnWidth(0,  "50%");

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

}
