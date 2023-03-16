package application;

import helpers.ItemHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class Main extends Application {
	
	protected static boolean opened = false;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Menu.fxml"));
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());
			//String css = this.getClass().getClassLoader().getResource("application.css").toExternalForm();
			//scene.getStylesheets().add(css);
			stage.setWidth(1200);
			stage.setHeight(800);
			stage.setResizable(false);
			stage.setScene(scene);
			stage.show();
			
			stage.setOnCloseRequest(event -> {
					event.consume();
					exitProgram(stage);
				});
			opened = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void exitProgram(Stage stage) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Exit");
		alert.setHeaderText("You're about to close the program");
		alert.setContentText("Are you sure you want to close before saving?");
		
		if(alert.showAndWait().get() == ButtonType.OK) {
			System.out.println("You successfully exited the program");
			stage.close();
		}
	}
}
