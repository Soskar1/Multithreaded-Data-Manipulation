package com.mdm.mdm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML private TableView<Person> personTableView;
    @FXML private TableColumn<Person, Integer> personID;
    @FXML private TableColumn<Person, String> personFirstName;
    @FXML private TableColumn<Person, String> personLastName;
    @FXML private TableColumn<Person, String> personEmail;
    @FXML private TableColumn<Person, String> personGender;
    @FXML private TableColumn<Person, String> personCountry;
    @FXML private TableColumn<Person, String> personDomainName;
    @FXML private TableColumn<Person, String> personBirthDate;

    @FXML private ListView<FileProgressInfo> fileProgressList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableView();

        fileProgressList.setCellFactory(FileProgressInfoListCell::new);

        loadDataFromFile("assets/MOCK_DATA1.csv");
        loadDataFromFile("assets/MOCK_DATA2.csv");
        loadDataFromFile("assets/MOCK_DATA3.csv");
    }

    private void initializeTableView() {
        personID.setCellValueFactory(new PropertyValueFactory<>("id"));
        personFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        personLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        personEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        personGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        personCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        personDomainName.setCellValueFactory(new PropertyValueFactory<>("domainName"));
        personBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
    }

    public synchronized void addPerson(Person person) {
        personTableView.getItems().add(person);
    }

    private void loadDataFromFile(String filePath) {
        FileReader fileReader;
        Path path = Paths.get(filePath);

        try {
            fileReader = new FileReader(this, filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileProgressInfo fileProgressInfo = new FileProgressInfo(path.getFileName().toString(), fileReader);
        fileProgressList.getItems().add(fileProgressInfo);

        Thread thread = new Thread(fileReader);
        thread.start();
    }
}