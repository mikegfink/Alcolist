package com.lamchop.alcolist.client.ui;

import com.google.gwt.user.cellview.client.DataGrid;


public interface DataGridResource extends DataGrid.Resources {

	  @Source({ DataGrid.Style.DEFAULT_CSS, "NewStyle.css" })
	  
	  MyStyle dataGridStyle();
}

