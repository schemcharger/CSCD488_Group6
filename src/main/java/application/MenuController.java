package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckComboBox;

import helpers.ItemHelper;
import helpers.PDFHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import magicitem.ItemType;
import magicitem.MagicItem;
import pixelart.GamePanel;
import pixelart.GameWindow;

public class MenuController implements Initializable {

	@FXML private Label yourItemsLabel;
	@FXML private Label selectedItem;
	@FXML private ChoiceBox<ItemType> itemTypeChoiceBox;
	@FXML private Label itemTypeLabel;
	@FXML private TextArea descriptionBox;
	@FXML private TextField searchBar;
	@FXML private ChoiceBox<String> searchChoiceBox;
	@FXML private CheckComboBox<String> traitsComboBox;
	@FXML private TextField addTraitTextField;
	@FXML private TextField deleteTraitTextField;
	@FXML private Button exportButton;
	@FXML private Button editArtButton;
	@FXML private ListView<MagicItem> listView = new ListView<MagicItem>(ItemHelper.getItemList());
	@FXML private ObservableList<String> traitsList = FXCollections.observableArrayList();
	MagicItem currentItem;
	
	// ItemType array to associate with ItemTypes
	private ItemType[] itemTypes = {ItemType.ARMOUR, ItemType.CATALYST, ItemType.GENERIC, ItemType.WEAPON};
	
	private String[] searchFilter = {"Name", "Item Type"};
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void switchToGridEditor(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation((getClass().getResource("/ColorPicker.fxml")));
			Parent newRoot = fxmlLoader.load();
			Stage newStage = new Stage();
			newStage.initModality(Modality.APPLICATION_MODAL);
			newStage.resizableProperty().setValue(false);
			newStage.initStyle(StageStyle.UTILITY);
			newStage.setTitle("Color Picker");
			newStage.setScene(new Scene(newRoot));
			newStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		if (Main.opened && !NewItemController.exit && !GamePanel.gamePanelSave) {
			flushDB();
			System.out.println("Init");
		}
		
		this.disableButtons();
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
				if(currentItem != null) {
					exportButton.setDisable(false);
					editArtButton.setDisable(false);
				}
				
				descriptionBox.setText(currentItem == null ? "" : currentItem.getDescription());
				itemTypeLabel.setText(currentItem == null ? "" : currentItem.getType().toString());
				itemTypeChoiceBox.getSelectionModel().select(currentItem == null ? ItemType.GENERIC : currentItem.getType());
				selectedItem.setText(currentItem == null ? "" : currentItem.getName());
				
				// This checks and unchecks traits for currently selected items
				if (currentItem != null) {
					traitsComboBox.getCheckModel().clearChecks();
					ArrayList<String> currentItemTraitList = currentItem.getTraits();
					for (int i = 0; i < currentItemTraitList.size(); i++) {
						traitsComboBox.getCheckModel().check(currentItemTraitList.get(i));
					}
				}
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
		
		// Search Section
		searchChoiceBox.getItems().addAll(searchFilter);
		
		// Traits Section
		ArrayList<String> traitArrayList = ItemHelper.readTraits();
		for (int i = 0; i < traitArrayList.size(); i++) {
			traitsList.add(traitArrayList.get(i));
		}
		traitsComboBox.getItems().addAll(traitsList);
		traitsComboBox.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {

			@Override
			public void onChanged(Change<? extends String> c) {
				if (currentItem == null) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Magic Item Creator");
					alert.setHeaderText("Oops!");
					alert.setContentText("Please select an item before changing traits");
					alert.show();
				} else {
					Main.saved = false;
					ObservableList<String> list = traitsComboBox.getCheckModel().getCheckedItems();
					ArrayList<String> traitArrayList = new ArrayList<String>();
					for (int i = 0; i < list.size(); i++) {
						traitArrayList.add(list.get(i));
					}
					currentItem.setTraits(traitArrayList);
				}
			}		
		});
	}
	
	private void disableButtons() {
		exportButton.setDisable(true);
		editArtButton.setDisable(true);
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
		listView.getItems().add(item);
		Main.saved = false;
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
		Main.saved = false;
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
		String param = searchBar.getText();
		ObservableList<MagicItem> masterList = ItemHelper.getItemList();
		int sortType = searchChoiceBox.getSelectionModel().getSelectedIndex();
		if(sortType==0||sortType==1){
			ItemHelper.SetSortType(sortType);
		}
		if(param==null || param.isEmpty()){
			this.disableButtons();
			listView.getSelectionModel().select(null);
			listView.setItems(masterList);
		}else{
			ObservableList<MagicItem> searchList = FXCollections.observableArrayList();
			MagicItem item;
			for(int i=0; i<masterList.size(); i++){
				item = masterList.get(i);
				if(ItemHelper.getSortType()==0) {
					if (item.getName().toLowerCase().contains(param.toLowerCase())) {
						searchList.add(item);
					}
				}else if(ItemHelper.getSortType()==1){
					if (item.getType().name().toLowerCase().contains(param.toLowerCase())) {
						searchList.add(item);
					}
				}
			}
			this.disableButtons();
			listView.getSelectionModel().select(null);
			listView.setItems(ItemHelper.Sort(searchList));
		}


	}
	
	public void changeItemType(ActionEvent event) {
		ItemType itemType = itemTypeChoiceBox.getValue();
		currentItem = listView.getSelectionModel().getSelectedItem();
		if(currentItem != null) {
			if(!currentItem.getType().equals(itemType)){
				Main.saved = false;
			}
			if (itemType.equals(ItemType.ARMOUR)) {
				currentItem.setType(ItemType.ARMOUR);
			} else if (itemType.equals(ItemType.CATALYST)) {
				currentItem.setType(ItemType.CATALYST);
			} else if (itemType.equals(ItemType.GENERIC)) {
				currentItem.setType(ItemType.GENERIC);
			} else if (itemType.equals(ItemType.WEAPON)) {
				currentItem.setType(ItemType.WEAPON);
			}
			listView.refresh();
		}
	}
	
	public void changeDescription(KeyEvent event) {
		String description = descriptionBox.getText();
		currentItem = listView.getSelectionModel().getSelectedItem();
		if(currentItem != null) {
			currentItem.setDescription(description);
			Main.saved = false;
		}
	}
	
	public void addTrait(ActionEvent event) {
		String trait = addTraitTextField.getText();
		if (trait == null || trait.isEmpty()) {
			// Change this to an alert pop up
			throw new NullPointerException("addTrait: trait is null");
		}
		ItemHelper.addTrait(trait);
		traitsComboBox.getItems().add(trait);
		Main.saved = false;
	}
	
	public void deleteTrait(ActionEvent event) {
		String trait = deleteTraitTextField.getText();
		if (trait == null || trait.isEmpty()) {
			// Change this to an alert pop up
			throw new NullPointerException("deleteTrait: trait is null");
		}
		traitsComboBox.getItems().remove(trait);
		traitsList.remove(trait);
		ArrayList<String> traitArrayList = new ArrayList<String>();
		for (int i = 0; i < traitsList.size(); i++) {
			traitArrayList.add(traitsList.get(i));
		}
		
		for (int i = 0; i < ItemHelper.getItemList().size(); i++) {
			ItemHelper.getItemList().get(i).removeTrait(trait);
		}
		Main.saved = false;
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
	}
	
	/*public static Color colorPicker() {
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
	}*/
	
	public void save() {
		Main.saved = true;
		System.out.println("This is itemList: " + ItemHelper.getItemList());
		flushDB();
		ItemHelper.writeDB(ItemHelper.getItemList());
		ItemHelper.writeTraits(ItemHelper.getTraitList());
		listView.refresh();
	}
}
