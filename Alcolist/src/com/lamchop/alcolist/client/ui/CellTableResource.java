package com.lamchop.alcolist.client.ui;

import com.google.gwt.user.cellview.client.CellTable;

public interface CellTableResource extends CellTable.Resources {
	  

	   @Source({ CellTable.Style.DEFAULT_CSS, "CellTable.css"}) 
	   CellTableStyle cellTableStyle();
}
