package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import magicitem.ItemType;

public class NewItemController implements Initializable {
	
	@FXML private Label itemNameIdentifierText;
	@FXML private TextField itemNameTextField;
	@FXML private Button createButton;
	@FXML private Label itemTypeIdentifierText;
	@FXML private ChoiceBox<ItemType> itemTypeChoiceBox;
	@FXML private Label descriptionIdentifierText;
	@FXML private TextArea descriptionBox;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	// ItemType array to associate with ItemTypes
	private ItemType[] itemTypes = {ItemType.ARMOUR, ItemType.CATALYST, ItemType.GENERIC, ItemType.WEAPON};
	
	String name = "";
	ItemType type;
	String description = "";
	String lore = "";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		itemTypeChoiceBox.getItems().addAll(itemTypes);
		itemTypeChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				type = itemTypes[arg2.intValue()];
			}
			
		});
	}
	
	public void create(ActionEvent event) throws IOException {
		//TODO: ADD NULL CHECKING AND DON'T LET THEM LEAVE UNTIL EVERYTHING IS FILLED OUT
		name = itemNameTextField.getText();
		description = descriptionBox.getText();
		System.out.println("Name: " + name);
		System.out.println("Item Type: " + type);
		System.out.println("Description: " + description);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
		root = loader.load();
		MenuController menuController = loader.getController();
		//TODO: CREATE METHOD IN MENUCONTROLLER THAT GRABS VALUES TAKEN FROM CREATE FUNCTION AND BRINGS THEM BACK TO MENUCONTROLLER
		// 1:21:03 in Bro Code video for reference
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToMenu(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
