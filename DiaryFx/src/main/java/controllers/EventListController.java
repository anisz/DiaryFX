package controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import utils.ApplicationException;
import utils.DialogsUtils;
import utils.FxmlUtils;

import java.io.IOException; 
import java.time.LocalDate;

/**
 * Developed by anisz
 * 
 */

public class EventListController {

	@FXML
	private ComboBox categoryComboBox;
	@FXML
	private ComboBox personComboBox;
	@FXML
	private TableView<EventFx> eventsTableView;
	@FXML
	private TableColumn<EventFx, String> titleColumn;
	@FXML
	private TableColumn<EventFx, String> descColumn;
	@FXML
	private TableColumn<EventFx, PersonFx> personColumn;
	@FXML
	private TableColumn<EventFx, CategoryFx> categoryColumn;
	@FXML
	private TableColumn<EventFx, Number> ratingColumn;
	@FXML
	private TableColumn<EventFx, LocalDate> releaseColumn;
	@FXML
	private TableColumn<EventFx, EventFx> deleteColumn;
	@FXML
	private TableColumn<EventFx, EventFx> editColumn;
	@FXML
	private TableColumn<EventFx, Image> pictureColumn;

	private EventListModel eventListModel;

	@FXML
	void initialize() {
		this.eventListModel = new EventListModel();
		try {
			this.eventListModel.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		this.categoryComboBox.setItems(this.eventListModel.getCategoryFxObservableList());
		this.personComboBox.setItems(this.eventListModel.getPersonFxObservableList());
		this.eventListModel.categoryFxObjectPropertyProperty().bind(this.categoryComboBox.valueProperty()); 
		this.eventListModel.personFxObjectPropertyProperty().bind(this.personComboBox.valueProperty()); 
 
		this.eventsTableView.setItems(this.eventListModel.getEventFxObservableList());
		this.titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());	
		this.descColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
		this.ratingColumn.setCellValueFactory(cellData -> cellData.getValue().ratingProperty());
		this.releaseColumn.setCellValueFactory(cellData -> cellData.getValue().releaseDateProperty());
		this.personColumn.setCellValueFactory(cellData -> cellData.getValue().personFxProperty());
		this.categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryFxProperty());
		this.deleteColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue())); 
		this.editColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue())); 
		this.pictureColumn.setCellValueFactory(cellData ->  cellData.getValue().imageFxProperty()); 

		
		this.deleteColumn.setCellFactory(param -> new TableCell<EventFx, EventFx>() {
			Button button = createButton("../icons/delete.png");

			@Override
			protected void updateItem(EventFx item, boolean empty) {
				super.updateItem(item, empty);

				if (empty) { 
					setGraphic(null);
					return;
				} 

				if (!empty) {
					setGraphic(button); 
					button.setOnAction(event -> {
						try {
							eventListModel.deleteEvent(item);
						} catch (ApplicationException e) {
							DialogsUtils.errorDialog(e.getMessage());
						}
					}); 
				}
			}
		});

		this.editColumn.setCellFactory(param -> new TableCell<EventFx, EventFx>() {
			
			Button button = createButton("../icons/edit.png"); 

			@Override
			protected void updateItem(EventFx item, boolean empty) {
				super.updateItem(item, empty);

				if (empty) {
					setGraphic(null);
					return;
				}

				if (!empty) {
					setGraphic(button); 
					button.setOnAction(event -> {
						FXMLLoader loader = FxmlUtils.getLoader("../fxml/AddEvent.fxml"); 
						
						Scene scene = null;
						try {
							scene = new Scene(loader.load());
						} catch (IOException e) {
							DialogsUtils.errorDialog(e.getMessage());
							e.printStackTrace();
						}
						EventController eventController = loader.getController();
						eventController.getEventModel().setEventFxObjectProperty(item);   
						eventController.bindings();
						
						Stage stage = new Stage();
						stage.setScene(scene);
						stage.initModality(Modality.APPLICATION_MODAL); 
						stage.showAndWait();
						
					
						initialize();
						eventsTableView.refresh();
					});
				}
			}
		});
		
		this.pictureColumn.setCellFactory(param -> new TableCell<EventFx, Image>() {

			Button button = new Button();
		
			@Override
			protected void updateItem(Image item, boolean empty) {
				super.updateItem(item, empty);

				if (empty) {
					setGraphic(null);
					return;
				}

				if (!empty) {
			
					ImageView imageView = new ImageView(item);
					imageView.setFitHeight(50.0);
					imageView.setFitWidth(50.0);
					button.setGraphic(imageView);
					
					button.setOnAction(event -> {
						FXMLLoader loader = FxmlUtils.getLoader("../fxml/ShowPicture.fxml");	
						Scene scene = null;
						try {
							scene = new Scene(loader.load());
						} catch (IOException e) {
							e.printStackTrace();
							DialogsUtils.errorDialog(e.getMessage());
						}
						PictureController pictureController = loader.getController();
				
						pictureController.setImage(item);
						
						Stage stage = new Stage();
						stage.setScene(scene);
						stage.initModality(Modality.APPLICATION_MODAL);
						stage.showAndWait(); 
						});
					setGraphic(button);
				}
			}
		});
		
	}

	private Button createButton(String path) {
		Button button = new Button();
		Image image = new Image(this.getClass().getResource(path).toString());
		ImageView imageView = new ImageView(image);
		button.setGraphic(imageView);
		return button;
	}

	private Button createImage() {
		Button button = new Button();
		Image image = new Image("file:L:\\Programowanie/Kosmo.gif");
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(50.0);
		imageView.setFitWidth(50.0);
		button.setGraphic(imageView);
		return button;
	}
	
	public void filterOnActionComboBox() {
		this.eventListModel.filterEventsList();
	}

	public void clearCategoryComboBox() {
		this.categoryComboBox.getSelectionModel().clearSelection();
	}

	public void clearPersonComboBox() {
		this.personComboBox.getSelectionModel().clearSelection();
	}
}
