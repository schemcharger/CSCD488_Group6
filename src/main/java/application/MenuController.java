package application;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import helpers.ItemHelper;
import helpers.PDFHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import magicitem.ItemType;
import magicitem.MagicItem;
import pixelart.GameWindow;

public class MenuController implements Initializable {

	@FXML private Label yourItemsLabel;
	@FXML private Label selectedItem;
	@FXML private ChoiceBox<ItemType> itemTypeChoiceBox;
	@FXML private Label itemTypeLabel;
	@FXML private TextArea descriptionBox;
	@FXML private TextField searchBar;
	@FXML private ListView<MagicItem> listView = new ListView<MagicItem>(ItemHelper.getItemList());
	MagicItem currentItem;
	
	// ItemType array to associate with ItemTypes
	private ItemType[] itemTypes = {ItemType.ARMOUR, ItemType.CATALYST, ItemType.GENERIC, ItemType.WEAPON};
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void switchToGridEditor(ActionEvent event) throws IOException {
		GameWindow.openEditor(currentItem);
	}
	
	public void switchToNewItemEditor(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getClassLoader().getResource("NewItemEditor.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if (Main.opened && !NewItemController.exit) {
			flushDB();
		}
		
		ObservableList<MagicItem> list;
		if (NewItemController.exit) { // If we are coming from exit button in NewItemController, get original itemList
			list = ItemHelper.getItemList();
			listView.getItems().addAll(ItemHelper.getItemList());
		} else {
			list = ItemHelper.readDB();
			listView.getItems().addAll(list);
		}
		listView.setCellFactory(new Callback<ListView<MagicItem>, ListCell<MagicItem>>() { // This allows a cell in the list to be changed to what we want

			@Override
			public ListCell<MagicItem> call(ListView<MagicItem> listView) {
				return new MagicItemListCell();
			}
			
		});
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MagicItem>() {

			@Override
			public void changed(ObservableValue<? extends MagicItem> arg0, MagicItem arg1, MagicItem arg2) {
				currentItem = listView.getSelectionModel().getSelectedItem();
				descriptionBox.setText(currentItem.getDescription());
				itemTypeLabel.setText(currentItem.getType().toString());
				itemTypeChoiceBox.getSelectionModel().select(currentItem.getType());
				selectedItem.setText(currentItem.getName());		
			}
			
		});
		
		// Item Type Section
		itemTypeChoiceBox.getItems().addAll(itemTypes);
		itemTypeChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				itemTypeLabel.setText(itemTypes[arg2.intValue()].toString());
				
			}
			
		});
		itemTypeChoiceBox.setOnAction(this::changeItemType);
		
		// Description Section
		descriptionBox.getText();
		
	}
	
	static class MagicItemListCell extends ListCell<MagicItem> {
		@Override
		public void updateItem(MagicItem item, boolean empty) {
			super.updateItem(item, empty);
			
			if (empty || item == null) {
				setText(null);
			} else {
				setText(item.getName() + " - " + item.getCreated() + ", " + item.getType()); // Basically overrides the toString() that Magic Item has in the ListView
			}
		}
	}
	
	public void addNewItem(MagicItem item) {
		ItemHelper.addItem(item);
		ItemHelper.writeDB(ItemHelper.getItemList());
		listView.getItems().add(item);
	}
	
	public void deleteItem() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete");
		alert.setHeaderText("You are about to delete the selected item");
		alert.setContentText("Are you sure you want to delete this item?");
		
		if(alert.showAndWait().get() == ButtonType.OK) {
			ObservableList<MagicItem> list = ItemHelper.getItemList();
			int index = listView.getSelectionModel().getSelectedIndex();
			list.remove(index);
			flushDB();
			ItemHelper.writeDB(list);
			listView.getItems().remove(index);
		}
	}
	
	public void exportToPDF() {
		if(currentItem != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Export");
			alert.setHeaderText("The currently selected item will be exported to a PDF.");
			alert.setContentText("This will be saved in Documents under 'Magic Item Creator'.");
			
			if(alert.showAndWait().get() == ButtonType.OK) {
				PDFHelper.renderPDF(currentItem);
				System.out.println("Export complete");
				
				Alert confirm = new Alert(AlertType.INFORMATION);
				confirm.setTitle("Success");
				confirm.setHeaderText("Item successfully exported");
				confirm.setContentText("The PDF is located in Documents under 'Magic Item Creator'.");
				confirm.show();
			}
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No Item Selected");
			alert.setHeaderText("No item is currently selected to export.");
			alert.setContentText("Please select an item first.");
			alert.show();
		}
	}
	
	public void search(ActionEvent event) {
		//TODO: Add search functionality across magic items, then update the listView
	}
	
	public void changeItemType(ActionEvent event) {
		ItemType itemType = itemTypeChoiceBox.getValue();
		currentItem = listView.getSelectionModel().getSelectedItem();
		if (itemType.equals(ItemType.ARMOUR)) {
			currentItem.setType(ItemType.ARMOUR);
		} else if (itemType.equals(ItemType.CATALYST)) {
			currentItem.setType(ItemType.CATALYST);
		} else if (itemType.equals(ItemType.GENERIC)) {
			currentItem.setType(ItemType.GENERIC);
		} else if (itemType.equals(ItemType.WEAPON)) {
			currentItem.setType(ItemType.WEAPON);
		}
		
		flushDB();
		listView.refresh();
	}
	
	public void changeDescription(KeyEvent event) {
		String description = descriptionBox.getText();
		currentItem = listView.getSelectionModel().getSelectedItem();
		System.out.println(description); // just for testing
		currentItem.setDescription(description);
		
	}
	
	private void flushDB() {
		File file = new File("itemDB");
		FileWriter fw;
		try {
			fw = new FileWriter(file, false);
			fw.flush();
			fw.close();
			System.out.println("file cleared");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ItemHelper.writeDB(ItemHelper.getItemList());
	}
	
	public static Color colorPicker() {
		Stage colorPicker = new Stage();
		colorPicker.setTitle("Color Picker");
		
		ColorPicker picker = new ColorPicker();
		
		javafx.scene.paint.Color value = picker.getValue();
		VBox box = new VBox(picker);
		Scene s = new Scene(box, 960, 600);
		
		colorPicker.setScene(s);
		colorPicker.show();
		Color c = new Color((float) value.getRed(),
				(float) value.getGreen(),
				(float) value.getBlue(),
				(float) value.getOpacity());
		return c;
	}
}
