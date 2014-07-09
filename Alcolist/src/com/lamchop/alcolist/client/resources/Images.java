package com.lamchop.alcolist.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Images extends ClientBundle {
	@Source("winery.png")
	ImageResource winery();

	@Source("distillery.png")
	ImageResource distillery();

	@Source("brewery.png")
	ImageResource brewery();

	@Source("facebookLogin.png")
	ImageResource facebookLogin();

	@Source("facebookLogout.png")
	ImageResource facebookLogout();

	@Source("facebookShare.png")
	ImageResource facebookShare();

	@Source("expandDark.png")
	ImageResource expandDark();

	@Source("expandLight.png")
	ImageResource expandLight();

	@Source("maximizeLight.png")
	ImageResource maximizeLight();

	@Source("maximizeDark.png")
	ImageResource maximizeDark();

	@Source("minimizeLight.png")
	ImageResource minimizeLight();

	@Source("minimizeDark.png")
	ImageResource minimizeDark();

	@Source("starFull.png")
	ImageResource starFull();

	@Source("starEmpty.png")
	ImageResource starEmpty();

	@Source("nearMeDown.png")
	ImageResource nearMeDown();

	@Source("nearMeUp.png")
	ImageResource nearMeUp();
}

