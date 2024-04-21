package com.example.pablab.Controller;

import com.example.pablab.Services.UzytkownikServices;
import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

public class UzytkownikController implements Initializable, StatusUpdatable  {

    ReloadListController reloadListController = new ReloadListController();

    private final UzytkownikServices uzytkownikServices = new UzytkownikServices();

    @FXML
    @Getter
    @Setter
    private TableView<Map<String, Object>> usersTableView;

    @FXML
    public Label statusLabel;
    @FXML
    private TableColumn<Map<String, Object>, Long> usersIdColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> usersFirstNameColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> usersLastNameColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> usersBirthDate;


    @FXML
    private TableColumn<Map<String, Object>, String> usersEmailColumn;
    @FXML
    private TableColumn<Map<String, Object>, String> usersPhoneNumberColumn;
    @FXML
    private Button userRefreshButton;

    private static final Semaphore searchUserSemaphore = new Semaphore(1);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("init");
        initializeTableView();
        uzytkownikServices.fetchUserData();
        usersTableView.setItems(uzytkownikServices.getUsersData());
    }

    private void initializeTableView() {
        usersIdColumn.setCellValueFactory(cellData -> new SimpleLongProperty((Long) cellData.getValue().get("user_id")).asObject());
        usersFirstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("first_name")));
        usersLastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("last_name")));
        usersBirthDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("birth_date").toString()));
        usersEmailColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("user_email")));
        usersPhoneNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("user_phone_number")));
    }

    public void updateStatus(String tekst) {
        statusLabel.setText(tekst);
    }

    @FXML
    public void handleDodajUzytkownikaButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pablab/dodajUzytkownika.fxml"));
            AnchorPane dodajUzytkownika = loader.load();
            DodajUzytkownikaController dodajUzytkownikaController = loader.getController();
            dodajUzytkownikaController.setUzytkownikController(this);

            Stage dodajUzytkownikaStage = new Stage();

            dodajUzytkownikaStage.initModality(Modality.APPLICATION_MODAL);
            dodajUzytkownikaStage.setTitle("Dodawanie");
            dodajUzytkownikaStage.setScene(new Scene(dodajUzytkownika));
            dodajUzytkownikaStage.showAndWait();
            reloadListController.reloadUserList(usersTableView);




        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUsunUzytkownikaButton(ActionEvent event) {
        String toSplit =   usersTableView.getSelectionModel().selectedItemProperty().get().toString();
        String[] parts = toSplit.split(",");
        try {
            statusLabel.setText(uzytkownikServices.deleteUser(Long.valueOf(parts[2].substring(9))));

        } finally{
            reloadListController.reloadUserList(usersTableView);
        }
    }

    @FXML
    private void handleZmodyfikujUzytkownikaButton(ActionEvent event) {
        System.out.println("modyfikacja");
        String toSplit = usersTableView.getSelectionModel().selectedItemProperty().get().toString();
        String[] parts = toSplit.split(",");
        System.out.println(Integer.valueOf(parts[2].substring(9)));

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pablab/ZmodyfikujUzytkownika.fxml"));
            AnchorPane zmodyfikujUzytkownika = loader.load();
            Stage dodajUzytkownikaStage = new Stage();



            ZmodyfikujUzytkownika zmodyfikujUzytkownikaController = loader.getController();
            zmodyfikujUzytkownikaController.setUserToModifyIdFromExternal(Integer.valueOf(parts[2].substring(9)));
            zmodyfikujUzytkownikaController.setUzytkownikController(this);
            statusLabel.setText("Modyfikowanie");
            dodajUzytkownikaStage.initModality(Modality.APPLICATION_MODAL);
            dodajUzytkownikaStage.setTitle("Modyfikacja");
            dodajUzytkownikaStage.setScene(new Scene(zmodyfikujUzytkownika));
            dodajUzytkownikaStage.showAndWait();
            reloadListController.reloadUserList(usersTableView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleWyszukajUzytkownikaButton(ActionEvent event) {
        try {
            if (searchUserSemaphore.tryAcquire()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pablab/WyszukajUzytkownika.fxml"));
                AnchorPane wyszukajUzytkownika = loader.load();
                WyszukajUzytkownika wyszukajUzytkownikaController = loader.getController();
                wyszukajUzytkownikaController.setUzytkownikController(this);

                setUsersTableView(usersTableView);

                Stage wyszukajUzytkownikaStage = new Stage();

                Stage mainWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
                double mainWindowX = mainWindow.getX();
                double mainWindowWidth = mainWindow.getWidth();
                double newX = mainWindowX + mainWindowWidth + 10;
                double newY = mainWindow.getY();
                wyszukajUzytkownikaStage.setX(newX);
                wyszukajUzytkownikaStage.setY(newY);

                wyszukajUzytkownikaStage.setAlwaysOnTop(true);
                wyszukajUzytkownikaStage.setTitle("Wyszukiwanie");
                statusLabel.setText("Wyszukiwanie");
                wyszukajUzytkownikaStage.setScene(new Scene(wyszukajUzytkownika));
                wyszukajUzytkownikaStage.showAndWait();
                searchUserSemaphore.release();
                reloadListController.reloadUserList(usersTableView);
                statusLabel.setText("---");

            } else {
                System.out.println("Okno wyszukiwania jest już otwarte.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void handleRefreshButton(ActionEvent event){
        reloadListController.reloadUserList(usersTableView);
    }
}
