package com.lamchop.alcolist.client.ui;

import static com.google.gwt.dom.client.Style.Unit.PCT;
import static com.google.gwt.dom.client.Style.Unit.PX;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Review;

public class ReviewPanel extends PopupPanel { // TODO: Consider using PopupPanel
	private static final int TEXT_VISIBLE_LINES = 6;
	private static final int TEXT_CHAR_WIDTH = 50;
	private TextArea reviewBox;
	private Review review;
	private FacebookShareButton shareButton;
	private Button saveButton;
	private LayoutPanel display;
	private Label reviewText;

	public ReviewPanel(final Manufacturer manufacturer, final AppDataController appDataController) {
	
		super(true);
		display = new LayoutPanel();
		review = appDataController.getReview(manufacturer.getID());
		reviewText = new Label();
		// TODO Consider not using null
		if (review != null) {
			reviewText.setText(review.getReview());
			
		}
	
		initReviewBox();	
		initSaveButton(manufacturer, appDataController);		
		shareButton = new FacebookShareButton(manufacturer, review, appDataController);
				
		initDisplay();
		
		setWidget(display);
		//display.setSize("60%", "60%");
		this.setSize("400px", "300px");
	}

	private void initDisplay() {
		display.add(reviewBox);
		display.add(saveButton);
		display.add(shareButton);
		display.add(reviewText);
		
		display.setWidgetBottomHeight(saveButton, 3, PCT, 10, PCT);
		display.setWidgetRightWidth(saveButton, 30, PCT, 20, PCT);
		display.setWidgetBottomHeight(shareButton, 3, PCT, 10, PCT);
		display.setWidgetRightWidth(shareButton, 3, PCT, 20, PCT);
		
		display.setWidgetTopHeight(reviewBox, 10, PCT, 70, PCT);
		display.setWidgetLeftWidth(reviewBox, 5, PCT, 90, PCT);
		
		display.setWidgetTopHeight(reviewText, 0, PCT, 10, PCT);
		display.setWidgetLeftWidth(reviewBox, 5, PCT, 90, PCT);
	}

	private void initSaveButton(final Manufacturer manufacturer,
			final AppDataController appDataController) {
		saveButton = new Button("Save Review");
		saveButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				review = appDataController.addReview(reviewBox.getText(), manufacturer.getID());
				reviewText.setText(review.getReview());
			}
		});
	}

	private void initReviewBox() {
		reviewBox = new TextArea();
		reviewBox.setFocus(true);
		reviewBox.setCharacterWidth(TEXT_CHAR_WIDTH);
		reviewBox.setVisibleLines(TEXT_VISIBLE_LINES);
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}
}
