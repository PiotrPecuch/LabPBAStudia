package com.example.pablab.Services;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class DialogsServices {

    public void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    boolean showConfirmationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setResizable(true);
        alert.setContentText(message);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    public void showAboutInformation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Infomracje o autorze");
        alert.setTitle("O aplikacji");
        alert.setContentText("Autorem apliakcji jest Piotr Pecuch\nGrupa dziekańska: 32ISM-INF-SP\nNumer dziennika: 106749");
        alert.showAndWait();
    }
}
