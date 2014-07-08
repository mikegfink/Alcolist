package com.lamchop.alcolist.client.ui;

import java.util.List;

import com.lamchop.alcolist.client.UserData;
import com.lamchop.alcolist.shared.Manufacturer;

public interface UIUpdateInterface {
	
	public void update(List<Manufacturer> manufacturers);
	
	public void update(UserData userData);
	
	public void showList();
	
	public void hideList();
	
	public void hideMap();
	
	public void showNearMeCircle(MyLocation myLocation);

	public void hideNearMeCircle();
}
