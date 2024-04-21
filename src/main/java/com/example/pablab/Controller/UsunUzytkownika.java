package com.example.pablab.Controller;

import com.example.pablab.Services.DialogsServices;
import com.example.pablab.Services.UzytkownikServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.sql.SQLException;

public class UsunUzytkownika{

    private StatusUpdatable statusUpdatable;
    DialogsServices dialogsServices = new DialogsServices();
    UzytkownikServices uzytkownikServices = new UzytkownikServices();
    @FXML
    public void handleMenuCloseButton() {
        System.exit(0);
    }

    @FXML
    private TextField idUserToDelete;

    @FXML
    private Button deleteUserButton;
    public void setStatusUpdatable(StatusUpdatable statusUpdatable) {
        this.statusUpdatable = statusUpdatable;
    }




}
