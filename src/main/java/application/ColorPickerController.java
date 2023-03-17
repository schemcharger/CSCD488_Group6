package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class ColorPickerController {

	@FXML private AnchorPane colorAnchorPane;
	@FXML private ColorPicker colorPicker;
	public static Color color;

	public void changeColor(ActionEvent event) {
		color = colorPicker.getValue();
		colorAnchorPane.setBackground(new Background(new BackgroundFill(color, null, null)));
	}

}
