package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GridController {
	
	@FXML private Button exit;
	@FXML private AnchorPane sceneGridEditor;

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	
	public void switchToMenu(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void exitProgram(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Exit");
		alert.setHeaderText("You're about to close the program");
		alert.setContentText("Are you sure you want to close before saving?");
		
		if(alert.showAndWait().get() == ButtonType.OK) {
			stage = (Stage) sceneGridEditor.getScene().getWindow();
			System.out.println("You successfully exited the program");
			stage.close();
		}
	}
}
