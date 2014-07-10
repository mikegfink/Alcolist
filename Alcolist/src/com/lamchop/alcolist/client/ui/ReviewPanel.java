package com.lamchop.alcolist.client.ui;

import static com.google.gwt.dom.client.Style.Unit.PCT;
import static com.google.gwt.dom.client.Style.Unit.PX;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lamchop.alcolist.client.AppDataController;
import com.lamchop.alcolist.client.ui.buttons.FacebookShareButton;
import com.lamchop.alcolist.shared.Manufacturer;
import com.lamchop.alcolist.shared.Review;

public class ReviewPanel extends PopupPanel { 
	// TODO: Consider using LayoutPanel instead of popup
	private static final int SHARE_BOT_PCT = 3;
	private static final int SHARE_HEIGHT_PX = 26;
	private static final int SHARE_WIDTH_PX = 67;
	private static final int SHARE_RIGHT_PX = 70;

	private static final int INFO_TOP_PCT = 0;
	private static final int INFO_HEIGHT_PX = 74;
	private static final int INFO_WIDTH_PCT = 100;
	private static final int INFO_LEFT_PX = 0;
	private static final int TEXT_BOX_WIDTH = 90;
	private static final int TEXT_BOX_LEFT = 5;
	private static final int TEXT_BOX_HEIGHT = 60;
	private static final int TEXT_BOX_TOP = INFO_HEIGHT_PX + 5;
	private static final int EDIT_SAVE_BUTTON_WIDTH = 67;
	private static final int EDIT_SAVE_BUTTON_RIGHT = 0;
	private static final int EDIT_SAVE_BUTON_HEIGHT = 27;
	private static final int EDIT_SAVE_BUTTON_BOT = 3;
	private static final int TEXT_VISIBLE_LINES = 3;
	private static final int TEXT_CHAR_WIDTH = 63;
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
	private Button closeButton;
	private InfoPanel infoPanel;
	private UIController uiController;
	private boolean loggedIn;

	public ReviewPanel(final Manufacturer manufacturer, 
			final AppDataController appDataController, UIController uiController) {	
		
		super(true);
		this.manufacturer = manufacturer;
		this.appDataController = appDataController;
		this.uiController = uiController;
		this.loggedIn = appDataController.isUserLoggedIn();
		review = appDataController.getReview(manufacturer.getID());

		createElements();
		addElements();
		setWidget(display);
		showInfoPanel();
		// TODO Consider not using null
		if (review != null) {
			showReview();
		} else {
			showEditReview();			
		}
	}

	private void showInfoPanel() {
		display.setWidgetTopHeight(infoPanel, INFO_TOP_PCT, PCT, INFO_HEIGHT_PX, PX);
		display.setWidgetLeftWidth(infoPanel, INFO_LEFT_PX, PX, INFO_WIDTH_PCT, PCT);
		
	}

	private void createElements() {
		display = new LayoutPanel();
		infoPanel = new InfoPanel(manufacturer, uiController, loggedIn);
		reviewText = new HTML();
		shareButton = new FacebookShareButton(manufacturer, appDataController);
		saveButton = new Button("Save");
		editButton = new Button("Edit");
		closeButton = new Button("X");
		reviewBox = new TextArea();
		
	}

	private void showEditReview() {
		initReviewBox();	
		initSaveButton(manufacturer, appDataController);		
		showEditDisplay();
		setEditPanelSize();	
	}

	private void setEditPanelSize() {
		this.setSize("460px", "335px");
	}

	private void setReviewPanelSize() {
		String width = "460px";
		int reviewLines = review.getReview().length() / 55 + 1;
		int reviewHeight = reviewLines * FONT_HEIGHT;
		int reviewDisplayHeight = reviewHeight;
		int displayHeight = reviewDisplayHeight + INFO_HEIGHT_PX + SHARE_HEIGHT_PX + 30;
		String height = Integer.toString(displayHeight) + "px";
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

		display.setWidgetTopHeight(reviewBox, TEXT_BOX_TOP, PX, TEXT_BOX_HEIGHT, PCT);
		display.setWidgetLeftWidth(reviewBox, TEXT_BOX_LEFT, PCT, TEXT_BOX_WIDTH, PCT);
	}

	private void showReviewDisplay() {
		hideChild(saveButton);
		hideChild(reviewBox);
		
		display.setWidgetBottomHeight(shareButton, SHARE_BOT_PCT, PCT, SHARE_HEIGHT_PX, PX);
		display.setWidgetRightWidth(shareButton, SHARE_RIGHT_PX, PX, SHARE_WIDTH_PX, PX);

		display.setWidgetTopHeight(reviewText, TEXT_BOX_TOP, PX, TEXT_BOX_HEIGHT, PCT);
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
		display.add(infoPanel);
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
