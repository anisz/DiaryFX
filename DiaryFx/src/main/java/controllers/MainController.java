package controllers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import utils.DialogsUtils;
import utils.FxmlUtils;

import java.util.Optional;

import static javafx.application.Application.STYLESHEET_CASPIAN;
import static javafx.application.Application.STYLESHEET_MODENA;

/**
 * Developed by anisz 
 */
public class MainController {

    @FXML
    private BorderPane borderPane; 

    @FXML
    private TopMenuButtonsController topMenuButtonsController; 

    @FXML
    private void initialize() {
        topMenuButtonsController.setMainController(this);
    }

    public void setCenter(String fxmlPath){
        borderPane.setCenter(FxmlUtils.fxmlLoader(fxmlPath));
    }
    
    public void setLeft(String fxmlPath){
        borderPane.setLeft(FxmlUtils.fxmlLoader(fxmlPath));
    }
    
    public void clearCenter(){
        borderPane.setCenter(null);
    }
    
    public void clearLeft(){
        borderPane.setLeft(null);
    }
 
    public void closeApplication() {
        Optional<ButtonType> result = DialogsUtils.confirmationDialog();
        if(result.get()==ButtonType.OK){
            Platform.exit();
            System.exit(0);
        }
    }

    public void setAlwaysOnTop(ActionEvent actionEvent) {
       Stage stage = (Stage) borderPane.getScene().getWindow();
        boolean value = ((CheckMenuItem) actionEvent.getSource()).selectedProperty().get(); 
        stage.setAlwaysOnTop(value);
    }

    public void about() {
        DialogsUtils.dialogAboutApplication();
    }
}
