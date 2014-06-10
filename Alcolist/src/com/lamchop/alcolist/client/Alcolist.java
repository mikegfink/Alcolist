package com.lamchop.alcolist.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Alcolist implements EntryPoint {
	

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		GWTBase gwtBase = new GWTBase();
		RootPanel.get("baseContainer").add(gwtBase.setupBase());
	}
}
