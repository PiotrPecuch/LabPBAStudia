package com.example.pablab.Controller;

import com.example.pablab.Entity.User;
import com.example.pablab.Services.UzytkownikServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class ZmodyfikujUzytkownika {

    ObservableList<Map<String, Object>> foundUser = FXCollections.observableArrayList();
    private final UzytkownikServices uzytkownikServices = new UzytkownikServices();
    UzytkownikController uzytkownikController;

    public void setUzytkownikController(UzytkownikController uzytkownikController) {
        this.uzytkownikController = uzytkownikController;
    }

    private Integer userToModifyId;
    @FXML
    TextField nameLabel;
    @FXML
    TextField secNameLabel;
    @FXML
    DatePicker birthLabel;
    @FXML
    TextField emailLabel;
    @FXML
    TextField phoneNumberLabel;

    @FXML
    Label idLabel;

    public void setUserToModifyIdFromExternal(Integer userToModifyId) {
        this.userToModifyId = userToModifyId;
        System.out.println("Ustawiono userToModifyId: " + userToModifyId);
        User userData = uzytkownikServices.findUserById(userToModifyId);
        idLabel.setText(userToModifyId.toString());
        nameLabel.setText((String) userData.getFirstName());
        secNameLabel.setText((String) userData.getLastName());
        phoneNumberLabel.setText((String) userData.getUserPhoneNumber());
        emailLabel.setText((String) userData.getUserEmail());
        birthLabel.setValue((LocalDate) userData.getBirthDate());

        System.out.println(userData);
    }


    public void updateButtonHandle(){
        uzytkownikServices.updateUserById(Long.valueOf(userToModifyId),nameLabel.getText(),secNameLabel.getText(), birthLabel.getValue(),emailLabel.getText(),phoneNumberLabel.getText());
        uzytkownikController.updateStatus("Zaktualizowano");
        Stage stage = (Stage) idLabel.getScene().getWindow();
        stage.close();
     }
}
