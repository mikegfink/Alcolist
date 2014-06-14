package com.lamchop.alcolist.client;

import java.util.List;

import com.lamchop.alcolist.shared.Manufacturer;

public interface UIUpdateInterface {
	
	public void update(List<Manufacturer> manufacturers);
	
	public void update(UserData userData);
}