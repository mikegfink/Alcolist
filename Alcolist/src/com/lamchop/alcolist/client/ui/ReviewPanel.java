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
import com.google.gwt.user.client.ui.Widget;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.ui.buttons.FacebookShareButton;
import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Review;

public class ReviewPanel extends PopupPanel { 
	// TODO: Consider using LayoutPanel instead of popup
	private static final int TEXT_BOX_WIDTH = 90;
	private static final int TEXT_BOX_LEFT = 5;
	private static final int TEXT_BOX_HEIGHT = 70;
	private static final int TEXT_BOX_TOP = 10;
	private static final int EDIT_SAVE_BUTTON_WIDTH = 67;
	private static final int EDIT_SAVE_BUTTON_RIGHT = 0;
	private static final int EDIT_SAVE_BUTON_HEIGHT = 27;
	private static final int EDIT_SAVE_BUTTON_BOT = 3;

	private static final int TEXT_VISIBLE_LINES = 6;
	private static final int TEXT_CHAR_WIDTH = 55;
	private static final int FONT_HEIGHT = 12;
	private TextArea reviewBox;
	private Review review;
	private FacebookShareButton shareButton;
	private Button saveButton;
	private LayoutPanel display;
	private Label reviewText;
	private Manufacturer manufacturer;
	private AppDataController appDataController;
	private Button editButton;

	public ReviewPanel(final Manufacturer manufacturer, final AppDataController appDataController) {	
		super(true);
		this.manufacturer = manufacturer;
		this.appDataController = appDataController;
		review = appDataController.getReview(manufacturer.getID());

		createElements();
		addElements();
		setWidget(display);
		// TODO Consider not using null
		if (review != null) {
			showReview();
		} else {
			showEditReview();			
		}
	}

	private void createElements() {
		display = new LayoutPanel();
		reviewText = new Label();
		shareButton = new FacebookShareButton(manufacturer, review, appDataController);
		saveButton = new Button("Save");
		editButton = new Button("Edit");
		reviewBox = new TextArea();
	}

	private void showEditReview() {
		initReviewBox();	
		initSaveButton(manufacturer, appDataController);		
		showEditDisplay();
		setEditPanelSize();	
	}

	private void setEditPanelSize() {
		this.setSize("400px", "300px");
	}

	private void setReviewPanelSize() {
		String width = "400px";
		int reviewLines = review.getReview().length() / 55 + 1;
		int reviewHeight = reviewLines * FONT_HEIGHT;
		int reviewDisplayHeight = reviewHeight + 30 + 15 + 15;
		String height = Integer.toString(reviewDisplayHeight) + "px";
		this.setSize(width, height);
	}

	private void showReview() {
		reviewText.setText(review.getReview());
		editButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				showEditReview();				
			}
		});

		showReviewDisplay();
		setReviewPanelSize();
	}

	private void showEditDisplay() {
		hideChild(shareButton);
		hideChild(reviewText);
		hideChild(editButton);
		
		display.setWidgetBottomHeight(saveButton, EDIT_SAVE_BUTTON_BOT, PCT, 
				EDIT_SAVE_BUTON_HEIGHT, PX);
		display.setWidgetRightWidth(saveButton, EDIT_SAVE_BUTTON_RIGHT, PCT, 
				EDIT_SAVE_BUTTON_WIDTH, PX);

		display.setWidgetTopHeight(reviewBox, TEXT_BOX_TOP, PCT, TEXT_BOX_HEIGHT, PCT);
		display.setWidgetLeftWidth(reviewBox, TEXT_BOX_LEFT, PCT, TEXT_BOX_WIDTH, PCT);
	}

	private void showReviewDisplay() {
		hideChild(saveButton);
		hideChild(reviewBox);
		
		display.setWidgetBottomHeight(shareButton, 3, PCT, 26, PX);
		display.setWidgetRightWidth(shareButton, 70, PX, 67, PX);

		display.setWidgetTopHeight(reviewText, TEXT_BOX_TOP, PCT, TEXT_BOX_HEIGHT, PCT);
		display.setWidgetLeftWidth(reviewText, TEXT_BOX_LEFT, PCT, TEXT_BOX_WIDTH, PCT);

		display.setWidgetBottomHeight(editButton, EDIT_SAVE_BUTTON_BOT, PCT, 
				EDIT_SAVE_BUTON_HEIGHT, PX);
		display.setWidgetRightWidth(editButton, EDIT_SAVE_BUTTON_RIGHT, PCT, 
				EDIT_SAVE_BUTTON_WIDTH, PX);
	}

	private void addElements() {
		display.add(reviewBox);
		display.add(saveButton);
		display.add(reviewText);
		display.add(editButton);
		display.add(shareButton);
	}

	private void initSaveButton(final Manufacturer manufacturer,
			final AppDataController appDataController) {

		saveButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				review = appDataController.addReview(reviewBox.getText(), manufacturer.getID());
				showReview();				
			}
		});
	}

	private void initReviewBox() {

		reviewBox.setFocus(true);
		reviewBox.setCharacterWidth(TEXT_CHAR_WIDTH);
		reviewBox.setVisibleLines(TEXT_VISIBLE_LINES);
		if (review != null) {
			reviewBox.setText(review.getReview());
		}
	}
	
	public void hideChild(Widget widgetToHide){
		display.setWidgetTopHeight(widgetToHide, widgetToHide.getAbsoluteTop(), PX, 0, PCT);
		display.setWidgetLeftWidth(widgetToHide, widgetToHide.getAbsoluteLeft(), PX, 0, PCT);		
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}
}
