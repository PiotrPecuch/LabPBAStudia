package com.example.pablab;


import com.example.pablab.JDBC.DbConnector;
import com.example.pablab.Services.DialogsServices;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    DialogsServices dialogsServices = new DialogsServices();

    @FXML
    private ChoiceBox<String> hotelChoiceBox;

    @FXML
    private Label selectedHotelCity;

    @FXML
    private TableView<Map<String, Object>> roomTableView;

    @FXML
    private Label statusLabel;

    @FXML
    private Button zarezerwujPokojButton;

    @FXML
    private Button uzytkownicyButton;

    @FXML
    private MenuBar menuBar;

    private final ObservableList<Map<String, Object>> roomData = FXCollections.observableArrayList();
    private final Map<String, String> hotelCityMap = new HashMap<>();
    @FXML
    private TableColumn<Map<String, Object>, Integer> availabilityColumn;

    @FXML
    private TableColumn<Map<String, Object>, Double> pricePerNightColumn;

    @FXML
    private TableColumn<Map<String, Object>, Integer> roomNumberColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> roomTypeColumn;

    private void initializeTableView() {
        availabilityColumn.setCellValueFactory(data -> new SimpleIntegerProperty((Integer) data.getValue().get("availability")).asObject());
        pricePerNightColumn.setCellValueFactory(data -> new SimpleDoubleProperty((Double) data.getValue().get("price_per_night")).asObject());
        roomNumberColumn.setCellValueFactory(data -> new SimpleIntegerProperty((Integer) data.getValue().get("room_number")).asObject());
        roomTypeColumn.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue().get("room_type")));

        roomTableView.setItems(roomData);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeChoiceBox();
        initializeTableView();

        hotelChoiceBox.setOnAction(event -> {
            try {
                handleChoiceBoxAction();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void initializeChoiceBox() {
            DbConnector dbConnector = DbConnector.getInstance();

        try (Connection connection = dbConnector.getConnection()) {
            String query = "SELECT name, city FROM hotels";
            statusLabel.setText("Połączono z bazą danych.");


            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                ObservableList<String> hotelNames = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    String hotelName = resultSet.getString("name");
                    String city = resultSet.getString("city");
                    hotelNames.add(hotelName);
                    hotelCityMap.put(hotelName, city);
                }

                hotelChoiceBox.setItems(hotelNames);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Brak połączenia z bazą danych.");

        }
    }


    private void handleChoiceBoxAction() throws SQLException {
        String selectedHotel = hotelChoiceBox.getValue();

        if (selectedHotel != null) {
            String selectedCity = hotelCityMap.get(selectedHotel);
            selectedHotelCity.setText(selectedCity);
            fetchRoomData(selectedHotel);
        }
    }

    private void fetchRoomData(String hotelName) throws SQLException {
        DbConnector dbConnector = DbConnector.getInstance();

        try (Connection connection = dbConnector.getConnection()) {
            String roomQuery = "SELECT availability, price_per_night, room_number, room_type FROM rooms WHERE hotel_id = (SELECT hotel_id FROM hotels WHERE name = ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(roomQuery)) {
                preparedStatement.setString(1, hotelName);

                ResultSet resultSet = preparedStatement.executeQuery();

                roomData.clear();
                int availableRooms = 0;

                while (resultSet.next()) {
                    int availability = resultSet.getInt("availability");

                    if (availability > 0) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("availability", availability);
                        row.put("price_per_night", resultSet.getDouble("price_per_night"));
                        row.put("room_number", resultSet.getInt("room_number"));
                        row.put("room_type", resultSet.getString("room_type"));
                        roomData.add(row);
                        availableRooms++;
                    }
                }
                roomTableView.setItems(roomData);

                statusLabel.setText("Dostępne pokoje: " + availableRooms);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    public void handleMenuCloseButton() {
        System.exit(0);
    }

    @FXML
    private void handleUzytkownicyButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("uzytkownicy.fxml"));
            AnchorPane uzytkownicyPane = loader.load();
            Stage uzytkownicyStage;

            uzytkownicyStage = new Stage();
            uzytkownicyStage.setTitle("uzytkownicy");
            uzytkownicyStage.setScene(new Scene(uzytkownicyPane));
            uzytkownicyStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleZarezerwujPokojButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("rezerwacja.fxml"));
            AnchorPane rezerwacjaPane = loader.load();
            Stage rezerwacjaStage;

            rezerwacjaStage = new Stage();
            rezerwacjaStage.initModality(Modality.APPLICATION_MODAL);
            rezerwacjaStage.setTitle("Rezerwacja");
            rezerwacjaStage.setScene(new Scene(rezerwacjaPane));

            rezerwacjaStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMenuDodajUzytkownika(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pablab/dodajUzytkownika.fxml"));
        Stage dodajUzytkownikaStage = new Stage();
        AnchorPane dodajUzytkownika = loader.load();
        dodajUzytkownikaStage.initModality(Modality.APPLICATION_MODAL);
        dodajUzytkownikaStage.setTitle("Dodawanie");
        dodajUzytkownikaStage.setScene(new Scene(dodajUzytkownika));
        dodajUzytkownikaStage.showAndWait();
    }

    @FXML
    private void handleOAplikacjiButton(){
        dialogsServices.showAboutInformation();
    }


}