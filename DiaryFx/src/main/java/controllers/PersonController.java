package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import model.*;

import java.sql.SQLException; 

import utils.ApplicationException;
import utils.DialogsUtils;

/**
 * Developed by anisz
 */
public class PersonController {

	@FXML
	private TextField nameTextField;
	@FXML
	private TextField surnameTextField;
	@FXML
	private Button addButton;
	@FXML
	private TableView<PersonFx> personTableView;
	@FXML
	private TableColumn<PersonFx, String> nameColumn;
	@FXML
	private TableColumn<PersonFx, String> surnameColumn;
	@FXML
	private MenuItem deleteMenuItem;

	private PersonModel personModel;

	public void initialize() throws Exception {
		this.personModel = new PersonModel();
		try {
			this.personModel.init();
		} catch (ApplicationException e) {
			DialogsUtils.errorDialog(e.getMessage());
		}
		bindings();
		bindingsTableView();
	}

	private void bindingsTableView() {
		this.personTableView.setItems(this.personModel.getPersonFxObservableList());
		this.nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		this.surnameColumn.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());

		this.nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		this.surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		this.personTableView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					this.personModel.setPersonFxObjectPropertyEdit(newValue);
				});
	}

	private void bindings() {
		this.personModel.personFxObjectPropertyProperty().get().nameProperty().bind(this.nameTextField.textProperty());
		this.personModel.personFxObjectPropertyProperty().get().surnameProperty()
				.bind(this.surnameTextField.textProperty());
		this.addButton.disableProperty()
				.bind(this.nameTextField.textProperty().isEmpty().or(this.surnameTextField.textProperty().isEmpty()));
		this.deleteMenuItem.disableProperty() 
				.bind(this.personTableView.getSelectionModel().selectedItemProperty().isNull());
	}

	public void addPersonOnAction() throws Exception {
		try {
			this.personModel.savePersonInDataBase();
		} catch (ApplicationException e) {
			DialogsUtils.errorDialog(e.getMessage());
		}
		this.nameTextField.clear();
		this.surnameTextField.clear();
	}

	public void onEditCommitName(TableColumn.CellEditEvent<PersonFx, String> personFxStringCellEditEvent)
			throws Exception {
		this.personModel.getPersonFxObjectPropertyEdit().setName(personFxStringCellEditEvent.getNewValue());
		updateInDatabase();
	}

	public void onEditCommitSurname(TableColumn.CellEditEvent<PersonFx, String> personFxStringCellEditEvent)
			throws Exception {
		this.personModel.getPersonFxObjectPropertyEdit().setSurname(personFxStringCellEditEvent.getNewValue());
		updateInDatabase();
	}

	private void updateInDatabase() {
		try {
			this.personModel.savePersonEditInDataBase();
		} catch (Exception e) { 
			DialogsUtils.errorDialog(e.getMessage());
		}
	}

	public void deletePersonOnAction() {
		try {
			this.personModel.deletePersonInDataBase();
		} catch (Exception e) { 
			DialogsUtils.errorDialog(e.getMessage());
		}
	}
}
