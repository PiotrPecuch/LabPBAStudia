package com.example.pablab.Controller;

import com.example.pablab.Entity.User;
import com.example.pablab.Services.DialogsServices;
import com.example.pablab.Services.UzytkownikServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DodajUzytkownikaController {

    private UzytkownikServices uzytkownikServices = new UzytkownikServices();
    DialogsServices dialogsServices = new DialogsServices();

    @FXML
    TextField firstNameTextField;
    @FXML
    TextField lastNameTextField;
    @FXML
    TextField emailTextField;
    @FXML
    TextField phoneNumberTextField;
    @FXML
    DatePicker birthDateDatePicker;

    @FXML
    Button addUserButton;
    private StatusUpdatable uzytkownikController;


    @FXML
    public void handleMenuCloseButton() {
        System.exit(0);
    }

    public void setUzytkownikController(UzytkownikController uzytkownikController) {
        this.uzytkownikController = uzytkownikController;
    }

    @FXML
    private String handleDodajUzytkownikaButton(ActionEvent event) {
        String userEmail = emailTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        LocalDate birthDate = birthDateDatePicker.getValue();
        String phoneNumber = phoneNumberTextField.getText();

        if (userEmail.isEmpty()  || firstName.isEmpty() || lastName.isEmpty() || birthDate == null || phoneNumber.isEmpty()) {
            dialogsServices.showErrorDialog("Wszystkie pola są wymagane. Proszę uzupełnić wszystkie dane.");
        }
        User newUser = new User();
        newUser.setFirstName(firstNameTextField.getText());
        newUser.setLastName(lastNameTextField.getText());
        newUser.setUserEmail(emailTextField.getText());
        newUser.setBirthDate(LocalDate.from(birthDateDatePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        newUser.setUserPhoneNumber(phoneNumberTextField.getText());


        if (uzytkownikServices != null) {
            String randomPassword = uzytkownikServices.generateRandomPassword();
            newUser.setPassword(randomPassword);

            uzytkownikServices.addUser(newUser,addUserButton);
            uzytkownikController.updateStatus("Dodano");


        } else {
            System.err.println("UzytkownikServices nie został zainicjowany.");
            return ("Nie zainicjalizowano UzytkownikServices");
        }
        return null;
    }




}
