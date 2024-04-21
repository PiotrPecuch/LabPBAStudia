package com.example.pablab.Controller;

import com.example.pablab.Services.UzytkownikServices;
import javafx.scene.control.TableView;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ReloadListController {

    private UzytkownikServices uzytkownikServices = new UzytkownikServices();

    public void reloadUserFoundList(TableView<Map<String, Object>> tableView,  List<Map<String, Object>> user) {
        try {
            tableView.getItems().clear();
            tableView.getItems().addAll(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }




    public void reloadUserList(TableView tableView) {
        uzytkownikServices.fetchUserData();
        tableView.setItems(uzytkownikServices.getUsersData());
    }

}
