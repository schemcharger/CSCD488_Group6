package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Main extends Application {
	
	protected static boolean opened = false;
	public static boolean saved = true;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Menu.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());
			stage.setWidth(850);
			stage.setHeight(850);
			stage.setResizable(true);
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
		
		if (saved) {
			System.out.println("You successfully exited the program");
			stage.close();
		} else {
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
}
