package com.example.pablab.Controller;

import com.example.pablab.Services.DialogsServices;
import com.example.pablab.Services.UzytkownikServices;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class WyszukajUzytkownika {

    UzytkownikController uzytkownikController = new UzytkownikController();

    @FXML
    private TextField wyszukajUzytkownikaImie;
    @FXML
    private TextField wyszukajUzytkownikaNazwisko;
    UzytkownikServices uzytkownikServices = new UzytkownikServices();
    ReloadListController reloadListController = new ReloadListController();

    public void setUzytkownikController(UzytkownikController uzytkownikController) {
        this.uzytkownikController = uzytkownikController;
    }

    @FXML
    public void initialize() {
        wyszukajUzytkownikaImie.textProperty().addListener((observable, oldValue, newValue) -> handleDynamicSearch());
        wyszukajUzytkownikaNazwisko.textProperty().addListener((observable, oldValue, newValue) -> handleDynamicSearch());
    }


    private void handleDynamicSearch() {
        String imie = wyszukajUzytkownikaImie.getText();
        String nazwisko = wyszukajUzytkownikaNazwisko.getText();

        List<Map<String, Object>> users = uzytkownikServices.findUserDynamically(imie, nazwisko);

        reloadListController.reloadUserFoundList(uzytkownikController.getUsersTableView(), users);
    }
}